/*
 * Copyright (c) 2024 Kapi Contributors. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted if the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions, the following disclaimer and the list of contributors.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation and/or
 *    other materials provided with the distribution.
 *
 * 3. The buyer of the "Kapi" API is granted the right to use this software
 *    as a dependency in their own software projects. However, the buyer
 *    may not resell or distribute the "Kapi" API, in whole or in part, to other parties.
 *
 * 4. The buyer may include the "Kapi" API in a "fat jar" along with their own code.
 *    The license for the "fat jar" is at the buyer's discretion and may allow
 *    redistribution of the "fat jar", but the "Kapi" API code inside the "fat jar"
 *    must not be modified.
 *
 * 5. Neither the name "Kapi" nor the names of its contributors may be used to endorse
 *    or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY "Kapi" API, AND ITS CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL "Kapi" API, AND CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Kapi Contributors:
 * - Kyren223
 */
package me.kyren223.kapi.commands;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.data.Option;
import me.kyren223.kapi.data.Pair;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A utility class for building command arguments.
 */
@Kapi
@ApiStatus.Internal
@NullMarked
public class ArgumentBuilder<T> {
    
    private final T parent;
    private final Map<Pair<ArgumentType<?>,String>,ArgumentBuilder<ArgumentBuilder<T>>> args;
    
    // Pre-conditions consumers
    private final List<Pair<Predicate<CommandContext>,Option<String>>> requirements;
    
    // Final execution consumer
    private @Nullable Consumer<ExecutionCommandContext> executor;
    private @Nullable Consumer<SuggestionCommandContext> suggestion;
    
    private ArgumentBuilder(T parent) {
        this.parent = parent;
        this.args = new LinkedHashMap<>();
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
        args.put(Pair.of(type, name), argumentBuilder);
        return argumentBuilder;
    }
    
    /**
     * Adds a new argument to the command without a way to access it.<br>
     * If you want to be able to access the argument, use {@link #argument(ArgumentType, String)}.<br>
     * This is useful for arguments like literals that are used for branching.<br>
     * This uses a {@link UUID#randomUUID()#toString()} as the name.
     *
     * @param type The type of the argument
     * @return A new argument builder with the parent being this argument builder
     */
    @Kapi
    public ArgumentBuilder<ArgumentBuilder<T>> argument(ArgumentType<?> type) {
        ArgumentBuilder<ArgumentBuilder<T>> argumentBuilder = construct(this);
        args.put(Pair.of(type, UUID.randomUUID().toString()), argumentBuilder);
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
     * @param requirement A predicate that checks whether the requirement is met<br>
     *                    Takes a {@link CommandContext} as an argument
     * @param message     The error message if the requirement is not met (can be null)
     * @return this argument builder for chaining
     */
    public ArgumentBuilder<T> require(Predicate<CommandContext> requirement, @Nullable String message) {
        requirements.add(Pair.of(requirement, Option.ofNullable(message)));
        return this;
    }
    
    /**
     * Adds a requirement to the command.<br>
     * See {@link #require(Predicate, String)} if you want to provide an error message.
     *
     * @param requirement A predicate that checks whether the requirement is met<br>
     *                    Takes a {@link CommandContext} as an argument
     * @return this argument builder for chaining
     */
    @Kapi
    public ArgumentBuilder<T> require(Predicate<CommandContext> requirement) {
        return require(requirement, null);
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
    
    
    Map<Pair<ArgumentType<?>,String>,ArgumentBuilder<ArgumentBuilder<T>>> getArgs() {
        return args;
    }
    
    List<Pair<Predicate<CommandContext>,Option<String>>> getRequirements() {
        return requirements;
    }
    
    @Nullable
    Consumer<ExecutionCommandContext> getExecutor() {
        return executor;
    }
    
    @Nullable
    Consumer<SuggestionCommandContext> getSuggestion() {
        return suggestion;
    }
}
