package de.traviadan.lib.db;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention( value = java.lang.annotation.RetentionPolicy.RUNTIME )
@Target( java.lang.annotation.ElementType.METHOD )
public @interface DbFieldGetter {
	String name();
	String title();
	String join() default "";
	String constraint() default "";
	boolean visibility() default true;
	int order() default 0;
}
