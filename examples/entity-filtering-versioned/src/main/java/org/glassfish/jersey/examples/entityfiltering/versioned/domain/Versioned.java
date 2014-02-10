package org.glassfish.jersey.examples.entityfiltering.versioned.domain;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.glassfish.hk2.api.AnnotationLiteral;

/**
 * Specifies that a field is Versioned. Applied to fields or getter methods.
 * 
 * @since Versionator 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
// TODO consider adding to ElementType.TYPE and ElementType.PACKAGE
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface Versioned {

	public static String BEGINNING_OF_TIME = "BOT";

	public static String END_OF_TIME = "EOT";

	String since();

	String until() default END_OF_TIME;

	public static class VersionedImpl extends AnnotationLiteral<Versioned> implements Versioned {

		private static final long serialVersionUID = 6672579125108150016L;

		private final String since;

		private final String until;

		public VersionedImpl(String until) {
			this.until = until;
			this.since = since();
		}

		public VersionedImpl(String since, String until) {
			this.until = until;
			this.since = since;
		}

		@Override
		public String since() {
			return this.since;
		}

		@Override
		public String until() {
			return this.until;
		}

		@Override
		public String toString() {
			return String.format("Versioned[since: %s, until: %s]", since, until);
		}

	}
}