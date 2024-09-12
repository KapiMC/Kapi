/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the GPL-3.0 license.
 * See https://www.gnu.org/licenses/gpl-3.0 for details.
 * Created for Kapi: https://github.com/kapimc/kapi
 */

package io.github.kapimc.kapi.commands;

import io.github.kapimc.kapi.annotations.Kapi;

/**
 * Representation of an argument.
 * <p>
 * When no subcommand methods match the user's input,
 * a list of all possible subcommands will be displayed.
 * This representation will be used to show what type a given argument is.
 */
@Kapi
public class ArgumentRepresentation {
    
    private String name;
    private String prefix;
    private String suffix;
    
    private ArgumentRepresentation(String representation, String prefix, String suffix) {
        this.name = representation;
        this.prefix = prefix;
        this.suffix = suffix;
    }
    
    /**
     * @param name the name of the argument
     * @return a new argument representation
     */
    @Kapi
    public static ArgumentRepresentation of(String name) {
        return new ArgumentRepresentation(name, "", "");
    }
    
    /**
     * @param name   the name of the argument
     * @param prefix the prefix to use
     * @param suffix the suffix to use
     * @return a new argument representation
     */
    @Kapi
    public static ArgumentRepresentation of(String prefix, String name, String suffix) {
        return new ArgumentRepresentation(name, prefix, suffix);
    }
    
    /**
     * @param prefix the prefix to use
     * @return this, for chaining
     */
    @Kapi
    public ArgumentRepresentation prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }
    
    /**
     * @param suffix the suffix to use
     * @return this, for chaining
     */
    @Kapi
    public ArgumentRepresentation suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }
    
    /**
     * @param name the name to use
     * @return this, for chaining
     */
    @Kapi
    public ArgumentRepresentation name(String name) {
        this.name = name;
        return this;
    }
    
    /**
     * @return the name of the argument
     */
    @Kapi
    public String getName() {
        return name;
    }
    
    /**
     * @return the prefix to use
     */
    @Kapi
    public String getPrefix() {
        return prefix;
    }
    
    /**
     * @return the suffix to use
     */
    @Kapi
    public String getSuffix() {
        return suffix;
    }
    
    /**
     * Get the representation of the argument,
     * in the format of {@code prefix + name + suffix}.
     *
     * @return the representation of the argument
     */
    @Kapi
    public String getRepresentation() {
        return prefix + name + suffix;
    }
}
