package com.example.e4.rcp.todo.ownannotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@javax.inject.Qualifier
@Documented
@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DirectTodo {

	/**
	 * An id parameter can be passed to the DirectTodo, in order to inject a
	 * java.util.Optional<Todo> with a given id.<br/>
	 * <br/>
	 * Without parameter the injected Todo is an absent java.util.Optional, because Todo ids begin
	 * with 1 and the default is 0 (see ITodoService).
	 * 
	 * @return the id, which is passed to the DirectTodo annotation or 0, if no parameter is passed
	 */
	long id() default 0;
}