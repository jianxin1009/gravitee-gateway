package io.gravitee.gateway.core.endpoint.lifecycle.impl;

import io.gravitee.common.component.AbstractLifecycleComponent;
import io.gravitee.definition.model.Api;
import io.gravitee.definition.model.EndpointGroup;
import io.gravitee.gateway.core.endpoint.lifecycle.GroupLifecyleManager;
import io.gravitee.gateway.core.endpoint.lifecycle.LoadBalancedEndpointGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class DefaultGroupLifecycleManager extends AbstractLifecycleComponent<GroupLifecyleManager>
        implements GroupLifecyleManager {

    private final Logger logger = LoggerFactory.getLogger(DefaultGroupLifecycleManager.class);

    @Autowired
    private Api api;

    private final Map<String, EndpointGroupLifecycleManager> groups = new HashMap<>();

    @Override
    public LoadBalancedEndpointGroup get(String groupName) {
        return groups.get(groupName);
    }

    @Override
    public Collection<LoadBalancedEndpointGroup> groups() {
        return null;
    }

    @Override
    protected void doStart() throws Exception {
        // Wrap endpointsByName with an observable collection
        api.getProxy()
                .getGroups()
                .stream()
                .map(new Function<EndpointGroup, EndpointGroupLifecycleManager>() {
                    @Override
                    public EndpointGroupLifecycleManager apply(EndpointGroup group) {
                        EndpointGroupLifecycleManager groupLifecycleManager = new EndpointGroupLifecycleManager(group);
                        groups.put(group.getName(), groupLifecycleManager);
                        return groupLifecycleManager;
                    }
                })
                .forEach(new Consumer<EndpointGroupLifecycleManager>() {
                    @Override
                    public void accept(EndpointGroupLifecycleManager groupLifecycleManager) {
                        try {
                            groupLifecycleManager.start();
                        } catch (Exception ex) {
                            logger.error("An error occurs while starting a group of endpoints: name[{}]", groupLifecycleManager);
                        }
                    }
                });
    }

    @Override
    protected void doStop() throws Exception {
        Iterator<EndpointGroupLifecycleManager> ite = groups.values().iterator();
        while (ite.hasNext()) {
            ite.next().stop();
            ite.remove();
        }

        groups.clear();
    }
}
