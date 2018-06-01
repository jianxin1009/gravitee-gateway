package io.gravitee.gateway.core.endpoint.ref;

import io.gravitee.gateway.api.endpoint.Endpoint;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public interface Reference {

    String key();

    String name();

    Endpoint endpoint();
}
