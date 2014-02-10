package org.glassfish.jersey.examples.entityfiltering.versioned.provider;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.examples.entityfiltering.versioned.domain.Versioned;

@Provider
public class VersionedResponseFilter implements ContainerResponseFilter {

	@Context
	private Configuration configuration;

	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		String requestedVersion = VersionedHelper.getRequestedVersion(request, configuration);

		Object entity = response.getEntity();
		Annotation[] annotations = response.getEntityAnnotations();
		MediaType mediaType = response.getMediaType();

		Versioned[] versionedAnnotations = VersionedHelper.getVersionedAnnotations(entity, requestedVersion);

		// TODO combine annotations
		response.setEntity(entity, versionedAnnotations, mediaType);

	}
}
