package io.gravitee.gateway.core.endpoint.lifecycle.impl;

import io.gravitee.common.component.AbstractLifecycleComponent;
import io.gravitee.common.util.ChangeListener;
import io.gravitee.common.util.ObservableCollection;
import io.gravitee.common.util.ObservableSet;
import io.gravitee.definition.model.Endpoint;
import io.gravitee.gateway.core.endpoint.factory.EndpointFactory;
import io.gravitee.gateway.core.endpoint.lifecycle.EndpointLifecycleManager;
import io.gravitee.gateway.core.endpoint.lifecycle.LoadBalancedEndpointGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class EndpointGroupLifecycleManager extends AbstractLifecycleComponent<EndpointLifecycleManager>
        implements EndpointLifecycleManager, ChangeListener<Endpoint> {

    private final Logger logger = LoggerFactory.getLogger(DefaultEndpointLifecycleManager.class);

    @Autowired
    private EndpointFactory endpointFactory;

    private final Map<String, io.gravitee.gateway.api.endpoint.Endpoint> endpointsByName = new LinkedHashMap<>();
    private final Map<String, String> endpointsTarget = new LinkedHashMap<>();
    private final ObservableCollection<io.gravitee.gateway.api.endpoint.Endpoint> endpoints = new ObservableCollection<>(new ArrayList<>());

    private final LoadBalancedEndpointGroup group;

    public EndpointGroupLifecycleManager(LoadBalancedEndpointGroup group) {
        this.group = group;
    }

    @Override
    protected void doStart() throws Exception {
        ObservableSet<Endpoint> endpoints = new ObservableSet<>(group.getEndpoints());
        endpoints.addListener(EndpointGroupLifecycleManager.this);
        group.setEndpoints(endpoints);
    }

    @Override
    protected void doStop() throws Exception {
        //TODO:
    }

    protected Predicate<Endpoint> filter() {
        return endpoint -> !endpoint.isBackup();
    }

    public void start(io.gravitee.definition.model.Endpoint model) {
        try {
            logger.info("Create new endpoint: name[{}] type[{}] target[{}]",
                    model.getName(), model.getType(), model.getTarget());

            io.gravitee.gateway.api.endpoint.Endpoint endpoint = endpointFactory.create(model);
            if (endpoint != null) {
                endpoint.connector().start();

                endpoints.add(endpoint);
                endpointsByName.put(endpoint.name(), endpoint);
                endpointsTarget.put(endpoint.name(), endpoint.target());
            }

        } catch (Exception ex) {
            logger.error("Unexpected error while creating endpoint connector", ex);
        }
    }

    public void stop(String endpointName) {
        logger.info("Closing endpoint: name[{}]", endpointName);

        io.gravitee.gateway.api.endpoint.Endpoint endpoint = endpointsByName.remove(endpointName);
        stop(endpoint);
    }

    private void stop(io.gravitee.gateway.api.endpoint.Endpoint endpoint) {
        if (endpoint != null) {
            try {
                endpoints.remove(endpoint);
                endpointsTarget.remove(endpoint.name());
                endpoint.connector().stop();
            } catch (Exception ex) {
                logger.error("Unexpected error while closing endpoint connector", ex);
            }
        } else {
            logger.error("Unknown endpoint. You should never reach this point!");
        }
    }

    @Override
    public boolean preAdd(io.gravitee.definition.model.Endpoint endpoint) {
        return false;
    }

    @Override
    public boolean postAdd(io.gravitee.definition.model.Endpoint endpoint) {
        start(endpoint);
        return false;
    }

    @Override
    public boolean preRemove(io.gravitee.definition.model.Endpoint endpoint) {
        return false;
    }

    @Override
    public boolean postRemove(io.gravitee.definition.model.Endpoint endpoint) {
        stop(endpoint.getName());
        return false;
    }

    @Override
    public io.gravitee.gateway.api.endpoint.Endpoint get(String endpointName) {
        return endpointsByName.get(endpointName);
    }

    @Override
    public Collection<io.gravitee.gateway.api.endpoint.Endpoint> endpoints() {
        return endpoints;
    }
}
