package org.glassfish.jersey.examples.entityfiltering.versioned.provider;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.glassfish.jersey.message.filtering.spi.AbstractEntityProcessor;
import org.glassfish.jersey.message.filtering.spi.EntityGraph;

public class VersionedEntityProcessor extends AbstractEntityProcessor {

	@Override
	protected Result process(String fieldName, Class<?> fieldClass, Annotation[] fieldAnnotations, Annotation[] annotations,
			EntityGraph graph) {
		if (fieldAnnotations.length > 0) {
			Set<String> filteringScopes = VersionedHelper.getFilteringScopes(fieldAnnotations);

			if (!filteringScopes.isEmpty()) {
				addFilteringScopes(fieldName, fieldClass, filteringScopes, graph);
			}
		}

		return Result.APPLY;
	}
}
