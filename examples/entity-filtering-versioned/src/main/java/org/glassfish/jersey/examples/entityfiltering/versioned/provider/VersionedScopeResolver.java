package org.glassfish.jersey.examples.entityfiltering.versioned.provider;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.glassfish.jersey.message.filtering.spi.ScopeResolver;

public class VersionedScopeResolver implements ScopeResolver {

	@Override
	public Set<String> resolve(final Annotation[] annotations) {
		return VersionedHelper.getFilteringScopes(annotations);
	}

}
