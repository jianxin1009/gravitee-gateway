package io.gravitee.gateway.core.endpoint.lifecycle;

import java.util.Collection;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public interface GroupManager {

    LoadBalancedEndpointGroup get(String groupName);

    Collection<LoadBalancedEndpointGroup> groups();
}
