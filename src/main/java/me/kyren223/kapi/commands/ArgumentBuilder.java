package me.kyren223.kapi.commands;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.Pair;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A utility class for building arguments.
 */
@Kapi
public class ArgumentBuilder<T> {
    
    private final T parent;
    private final Map<Pair<ArgumentType<?>, String>, ArgumentBuilder<ArgumentBuilder<T>>> args;
    
    // Pre-conditions consumers
    private final List<Pair<String, Predicate<CommandContext>>> requirements;
    
    // Final execution consumer
    private Consumer<ExecutionCommandContext> executor;
    private Consumer<SuggestionCommandContext> suggestion;
    
    private ArgumentBuilder(T parent) {
        this.parent = parent;
        this.args = new HashMap<>();
        this.requirements = new ArrayList<>();
    }
    
    static ArgumentBuilder<CommandBuilder> construct(CommandBuilder parent) {
        return new ArgumentBuilder<>(parent);
    }
    
    private static <U> ArgumentBuilder<ArgumentBuilder<U>> construct(ArgumentBuilder<U> parent) {
        return new ArgumentBuilder<>(parent);
    }
    
    public ArgumentBuilder<ArgumentBuilder<T>> argument(ArgumentType<?> type, String name) {
        ArgumentBuilder<ArgumentBuilder<T>> argumentBuilder = construct(this);
        args.put(new Pair<>(type, name), argumentBuilder);
        return argumentBuilder;
    }
    
    public T build() {
        return parent;
    }
    
    public ArgumentBuilder<T> require(String message, Predicate<CommandContext> requirement) {
        requirements.add(new Pair<>(message, requirement));
        return this;
    }
    
    public ArgumentBuilder<T> require(Predicate<CommandContext> requirement) {
        return require(null, requirement);
    }
    
    public ArgumentBuilder<T> execute(Consumer<ExecutionCommandContext> executor) {
        this.executor = executor;
        return this;
    }
    
    public ArgumentBuilder<T> suggest(Consumer<SuggestionCommandContext> suggestion) {
        this.suggestion = suggestion;
        return this;
    }
    
    
    Map<Pair<ArgumentType<?>, String>, ArgumentBuilder<ArgumentBuilder<T>>> getArgs() {
        return args;
    }
    
    List<Pair<String, Predicate<CommandContext>>> getRequirements() {
        return requirements;
    }
    
    Consumer<ExecutionCommandContext> getExecutor() {
        return executor;
    }
    
    Consumer<SuggestionCommandContext> getSuggestion() {
        return suggestion;
    }
}
