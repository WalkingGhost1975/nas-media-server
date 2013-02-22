package de.gisdesign.nas.media.domain;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Annotation type used as stereotype for {@link MetaDataCriteria} implementations.
 * @author Denis Pasek
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Scope("prototype")
public @interface Criteria {
	/**
	 * The value may indicate a suggestion for a logical component name,
	 * to be turned into a Spring bean in case of an autodetected component.
	 * @return the suggested component name, if any
	 */
	String value() default "";
}
