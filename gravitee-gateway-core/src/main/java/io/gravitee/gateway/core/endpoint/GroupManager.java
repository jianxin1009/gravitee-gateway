package io.gravitee.gateway.core.endpoint;

import io.gravitee.gateway.core.endpoint.lifecycle.LoadBalancedEndpointGroup;

import java.util.Collection;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public interface GroupManager {

    LoadBalancedEndpointGroup get(String groupName);

    LoadBalancedEndpointGroup getDefault();

    Collection<LoadBalancedEndpointGroup> groups();
}
