package org.glassfish.jersey.examples.entityfiltering.versioned.provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.examples.entityfiltering.versioned.domain.Version;
import org.glassfish.jersey.examples.entityfiltering.versioned.domain.Versioned;

public class VersionedHelper {
	private VersionedHelper() {
	}

	/**
	 * Get filtering scopes for the given annotations
	 * 
	 * @param annotations
	 * @return
	 */
	public static Set<String> getFilteringScopes(Annotation[] annotations) {
		Set<String> filteringScopes = new HashSet<String>();
		if (annotations.length > 0) {
			for (Annotation annotation : annotations) {
				if (annotation instanceof Versioned) {
					String scope = getScope(((Versioned) annotation));
					filteringScopes.add(scope);
				}
			}
		}
		return filteringScopes;
	}

	/**
	 * Get a string key to be used as a scope value for the given versioned
	 * 
	 * @param versioned
	 * @return
	 */
	private static String getScope(Versioned versioned) {
		return String.format("%s_%s:%s", Versioned.class.getName(), versioned.since(), versioned.until());
	}

	/**
	 * Given a requested version, introspect the given entity to determine
	 * "in range" versions based on the requested version.
	 * 
	 * @param entity
	 * @param requestedVersion
	 * @return
	 */
	public static Versioned[] getVersionedAnnotations(Object entity, String requestedVersion) {
		SortedSet<Version> sinces = new TreeSet<Version>();
		SortedSet<Version> untils = new TreeSet<Version>();

		for (Field field : entity.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Versioned.class)) {
				Versioned versioned = field.getAnnotation(Versioned.class);
				sinces.add(new Version(versioned.since()));
				untils.add(new Version(versioned.until()));
			}
		}

		Version requested = new Version(requestedVersion);
		Set<Versioned> versioneds = new HashSet<Versioned>();
		if (requested.getRawVersion().equals(Versioned.END_OF_TIME)) {
			// if no version requested, assume latest; add highest since version
			versioneds.add(new Versioned.VersionedImpl(sinces.last().getRawVersion(), requestedVersion));
		} else {
			// otherwise check each field for inclusion
			for (Version since : sinces) {
				if (requested.isGreaterThan(since) || requested.isEqualTo(since)) {
					for (Version until : untils) {
						if (requested.isLessThan(until)) {
							versioneds.add(new Versioned.VersionedImpl(since.getRawVersion(), until.getRawVersion()));
						}
					}
				}

			}
		}

		return versioneds.toArray(new Versioned[versioneds.size()]);
	}

	public static String getRequestedVersion(ContainerRequestContext context, Configuration configuration) {
		Class<?> paramLocation = (Class<?>) getProperty(configuration, VersionedFilteringFeature.VERSION_PARAM_LOCATION, HeaderParam.class);
		String paramName = (String) getProperty(configuration, VersionedFilteringFeature.VERSION_PARAM_NAME, "version");

		String requestedVersion = null;
		if (HeaderParam.class.equals(paramLocation)) {
			requestedVersion = context.getHeaderString(paramName);
		} else if (QueryParam.class.equals(paramLocation)) {
			requestedVersion = context.getUriInfo().getQueryParameters().getFirst(paramName);
		} else if (PathParam.class.equals(paramLocation)) {
			// TODO
		} else if (MediaType.class.equals(paramLocation)) {
			// TODO
		}
		return requestedVersion == null ? Versioned.END_OF_TIME : requestedVersion;
	}

	/**
	 * Get property from given configuration object; set to given default if
	 * none exists.
	 * 
	 * @param configuration
	 * @param propertyName
	 * @param defaultValue
	 * @return
	 */
	private static Object getProperty(Configuration configuration, String propertyName, Object defaultValue) {
		Object value = configuration.getProperty(propertyName);
		return value != null ? value : defaultValue;
	}

}
