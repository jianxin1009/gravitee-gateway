package io.gravitee.gateway.core.endpoint.ref;

import io.gravitee.gateway.api.endpoint.Endpoint;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class EndpointReference extends AbstractReference {

    public static final String REFERENCE_PREFIX = "endpoint:";

    private final Endpoint endpoint;
    private final String key;

    public EndpointReference(final Endpoint endpoint) {
        this.endpoint = endpoint;
        this.key = REFERENCE_PREFIX + endpoint.name();
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public String name() {
        return endpoint.name();
    }

    @Override
    public Endpoint endpoint() {
        return endpoint;
    }
}
