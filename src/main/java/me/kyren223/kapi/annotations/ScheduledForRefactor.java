package me.kyren223.kapi.annotations;

public @interface ScheduledForRefactor {
    String description() default "This code is scheduled for refactoring.";
}
