package me.kyren223.kapi.commands;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    /**
     * Adds a new argument to the command that can be referenced by name.
     *
     * @param type The type of the argument
     * @param name The name that will be used to reference the argument
     *             via {@link CommandContext#getArg(String, Class)}
     * @return A new argument builder with the parent being this argument builder
     */
    @Kapi
    public ArgumentBuilder<ArgumentBuilder<T>> argument(ArgumentType<?> type, String name) {
        ArgumentBuilder<ArgumentBuilder<T>> argumentBuilder = construct(this);
        args.put(new Pair<>(type, name), argumentBuilder);
        return argumentBuilder;
    }
    
    /**
     * Adds a new argument to the command without a way to access it.<br>
     * If you want to be able to access the argument, use {@link #argument(ArgumentType, String)}.<br>
     * This is useful for arguments like literals that are used for branching.
     *
     * @param type The type of the argument
     * @return A new argument builder with the parent being this argument builder
     */
    @Kapi
    public ArgumentBuilder<ArgumentBuilder<T>> argument(ArgumentType<?> type) {
        ArgumentBuilder<ArgumentBuilder<T>> argumentBuilder = construct(this);
        args.put(new Pair<>(type, null), argumentBuilder);
        return argumentBuilder;
    }
    
    /**
     * Builds the argument and returns the parent of this argument builder.<br>
     * The parent can be either a
     * {@link CommandBuilder} or another {@link ArgumentBuilder}.
     *
     * @return The parent of this argument builder
     */
    @Kapi
    public T build() {
        return parent;
    }
    
    /**
     * Adds a requirement to the command.
     * See {@link #require(Predicate)} if you don't want to provide an error message.
     *
     * @param message The error message if the requirement is not met (can be null)
     * @param requirement A predicate that checks whether the requirement is met<br>
     *                    Takes a {@link CommandContext} as an argument
     * @return this argument builder for chaining
     */
    public ArgumentBuilder<T> require(@Nullable String message, Predicate<CommandContext> requirement) {
        requirements.add(new Pair<>(message, requirement));
        return this;
    }
    
    /**
     * Adds a requirement to the command.<br>
     * See {@link #require(String, Predicate)} if you want to provide an error message.
     *
     * @param requirement A predicate that checks whether the requirement is met<br>
     *                    Takes a {@link CommandContext} as an argument
     * @return this argument builder for chaining
     */
    @Kapi
    public ArgumentBuilder<T> require(Predicate<CommandContext> requirement) {
        return require(null, requirement);
    }
    
    /**
     * Set the executor for the command.<br>
     * This is called if this "branch" of the command is reached.<br>
     * Will not be called if a requirement fails.
     *
     * @param executor A consumer that takes {@link ExecutionCommandContext}
     * @return this argument builder for chaining
     */
    @Kapi
    public ArgumentBuilder<T> execute(Consumer<ExecutionCommandContext> executor) {
        this.executor = executor;
        return this;
    }
    
    /**
     * Set the suggestion for the command.<br>
     * This is called if this "branch" of the command is reached.<br>
     * Will not be called if a requirement fails.
     *
     * @param suggestion A consumer that takes {@link SuggestionCommandContext}
     * @return this argument builder for chaining
     */
    @Kapi
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
