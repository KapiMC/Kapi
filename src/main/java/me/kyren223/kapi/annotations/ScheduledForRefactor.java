package me.kyren223.kapi.annotations;

/**
 * This annotation is used to mark code that is scheduled for refactoring.<br>
 * The refactoring specifically means that the external interface of the code will change.<br>
 * This annotation should not be used in your own code.<br>
 * <br>
 * Sometimes code annotated with this will be the only code that can do a certain task.<br>
 * So don't hesitate to use it, but be aware that the code will change in the future.<br>
 * <br>
 * In most cases, the code will be refactored to be more user-friendly and easier to use.<br>
 * Then this annotation will be replaced with {@link Deprecated}
 * and the code will be removed in a future version.<br>
 *
 */
public @interface ScheduledForRefactor {
    String description() default "This code is scheduled for refactoring.";
}
