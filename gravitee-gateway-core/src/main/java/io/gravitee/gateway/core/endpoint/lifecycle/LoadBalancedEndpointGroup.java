package io.gravitee.gateway.core.endpoint.lifecycle;

import io.gravitee.definition.model.Endpoint;
import io.gravitee.gateway.api.http.loadbalancer.LoadBalancerStrategy;

import java.util.Collection;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class LoadBalancedEndpointGroup implements LoadBalancerStrategy {

    private final String name;

    private Collection<Endpoint> endpoints;

    private final LoadBalancerStrategy strategy;

    public LoadBalancedEndpointGroup(final String name, final Collection<Endpoint> endpoints, final LoadBalancerStrategy strategy) {
        this.name = name;
        this.endpoints = endpoints;
        this.strategy = strategy;
    }

    @Override
    public String next() {
        return strategy.next();
    }

    public String getName() {
        return name;
    }

    public Collection<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Collection<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }
}
