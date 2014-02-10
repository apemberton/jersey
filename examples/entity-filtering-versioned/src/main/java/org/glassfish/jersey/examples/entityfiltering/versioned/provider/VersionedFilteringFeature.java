package org.glassfish.jersey.examples.entityfiltering.versioned.provider;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.glassfish.jersey.message.filtering.EntityFilteringFeature;

public class VersionedFilteringFeature implements Feature {

	public static final String VERSION_PARAM_LOCATION = VersionedScopeResolver.class.getName() + ".PARAM_LOCATION";

	public static final String VERSION_PARAM_NAME = VersionedScopeResolver.class.getName() + ".PARAM_NAME";

	@Override
	public boolean configure(final FeatureContext context) {
		final Configuration config = context.getConfiguration();

		if (!config.isRegistered(VersionedEntityProcessor.class)) {

			// register EntityFilteringFeature
			if (!config.isRegistered(EntityFilteringFeature.class)) {
				context.register(EntityFilteringFeature.class);
			}
			// Entity Processors.
			context.register(VersionedEntityProcessor.class);
			// Scope Resolver.
			context.register(VersionedScopeResolver.class);
			// Response Filter.
			// context.register(VersionedResponseFilter.class);

			return true;
		}
		return true;
	}
}
