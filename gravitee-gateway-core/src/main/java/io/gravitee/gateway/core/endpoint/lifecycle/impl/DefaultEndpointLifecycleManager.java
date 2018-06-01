/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.gateway.core.endpoint.lifecycle.impl;

import io.gravitee.common.component.AbstractLifecycleComponent;
import io.gravitee.definition.model.Api;
import io.gravitee.definition.model.EndpointGroup;
import io.gravitee.gateway.api.endpoint.Endpoint;
import io.gravitee.gateway.api.expression.TemplateContext;
import io.gravitee.gateway.api.expression.TemplateVariableProvider;
import io.gravitee.gateway.core.endpoint.lifecycle.EndpointLifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class DefaultEndpointLifecycleManager extends AbstractLifecycleComponent<EndpointLifecycleManager> implements
        EndpointLifecycleManager, TemplateVariableProvider {

    private final Logger logger = LoggerFactory.getLogger(DefaultEndpointLifecycleManager.class);

    @Autowired
    private Api api;

//    private final Map<String, Endpoint> endpointsByName = new LinkedHashMap<>();
//    private final Map<String, String> endpointsTarget = new LinkedHashMap<>();
//    private final ObservableCollection<Endpoint> endpoints = new ObservableCollection<>(new ArrayList<>());
    private final Map<String, EndpointGroupLifecycleManager> groups = new HashMap<>();

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
                            logger.error("An error occurs while starting endpoint group: name[{}]", groupLifecycleManager);
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

    @Override
    public Endpoint get(String endpointName) {
        // Search under each group
        // return endpointsByName.get(endpointName);
        return null;
    }

    @Override
    public Collection<Endpoint> endpoints() {
        // Search under each groups
        //return endpoints;
        return null;
    }

    @Override
    public void provide(TemplateContext templateContext) {
        // We should add a reference to all the endpoints of the system...
        /*
        Map<String, String> endpointRefs = groups.values().stream()
                .flatMap(group -> group.endpoints().stream())
                .collect(
                        Collectors.toMap(
                                Endpoint::name,
                                endpoint -> "endpoint:" + endpoint.name() + ':'));

        // ... but also to all groups
        Map<String, String> groupRefs = groups.keySet().stream()
                .collect(
                        Collectors.toMap(
                                group -> group,
                                group -> "group:" + group + ':'));

        Map<String, String> references = Stream.concat(endpointRefs.entrySet().stream(), groupRefs.entrySet().stream())
                .collect(Collectors
                        .collectingAndThen(
                                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue),
                                Collections::<String, String>unmodifiableMap));

        templateContext.setVariable("endpoints", references);
        */
    }
}
