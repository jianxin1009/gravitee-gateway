package io.gravitee.gateway.core.endpoint.ref.impl;

import io.gravitee.gateway.api.expression.TemplateContext;
import io.gravitee.gateway.api.expression.TemplateVariableProvider;
import io.gravitee.gateway.core.endpoint.ref.Reference;
import io.gravitee.gateway.core.endpoint.ref.ReferenceRegister;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class DefaultReferenceRegister implements ReferenceRegister, TemplateVariableProvider {

    private final static String TEMPLATE_VARIABLE_KEY = "endpoints";

    private final Map<String, Reference> references = new HashMap<>();

    @Override
    public void add(Reference reference) {
        references.put(reference.key(), reference);
    }

    @Override
    public void remove(String reference) {
        references.remove(reference);
    }

    @Override
    public Reference get(String reference) {
        return references.get(reference);
    }

    @Override
    public Collection<Reference> references() {
        return references.values();
    }

    @Override
    public Collection<Reference> referencesByType(Class<? extends Reference> refClass) {
        return references()
                .stream()
                .filter(reference -> reference.getClass().equals(refClass))
                .collect(Collectors.toSet());
    }

    @Override
    public void provide(TemplateContext context) {
        context.setVariable(
                TEMPLATE_VARIABLE_KEY,
                references.values().stream().map(Reference::name).collect(Collectors.toSet()));
    }
}
