package io.gravitee.gateway.core.endpoint.lifecycle;

import io.gravitee.gateway.api.lb.LoadBalancerStrategy;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class LoadBalancedEndpointGroup implements LoadBalancerStrategy {

    private final String name;

    private final LoadBalancerStrategy strategy;

    public LoadBalancedEndpointGroup(final String name, final LoadBalancerStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    @Override
    public io.gravitee.gateway.api.endpoint.Endpoint next() {
        return strategy.next();
    }

    public String getName() {
        return name;
    }
}
