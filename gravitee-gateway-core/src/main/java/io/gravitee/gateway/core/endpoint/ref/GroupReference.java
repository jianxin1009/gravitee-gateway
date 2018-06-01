package io.gravitee.gateway.core.endpoint.ref;

import io.gravitee.gateway.api.endpoint.Endpoint;
import io.gravitee.gateway.core.endpoint.lifecycle.LoadBalancedEndpointGroup;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class GroupReference extends AbstractReference {

    public static final String REFERENCE_PREFIX = "group:";

    private final LoadBalancedEndpointGroup group;
    private final String key;

    public GroupReference(final LoadBalancedEndpointGroup group) {
        this.group = group;
        this.key = REFERENCE_PREFIX + group.getName();
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public String name() {
        return group.getName();
    }

    @Override
    public Endpoint endpoint() {
        return group.next();
    }
}
