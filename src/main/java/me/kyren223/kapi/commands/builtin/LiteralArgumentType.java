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

package me.kyren223.kapi.commands.builtin;

import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.commands.ArgumentType;
import me.kyren223.kapi.commands.SuggestionCommandContext;
import me.kyren223.kapi.data.Result;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * Represents a command argument type for a literal.
 */
@Kapi
@ApiStatus.Experimental
@NullMarked
public class LiteralArgumentType implements ArgumentType<String> {
    
    private final String literal;
    private boolean ignoreCase;
    private boolean suggest;
    
    private LiteralArgumentType(String literal) {
        this.literal = literal;
        this.ignoreCase = false;
    }
    
    /**
     * Creates a new literal argument type.
     *
     * @param literal the literal
     * @return the literal argument type
     */
    @Kapi
    public static LiteralArgumentType literal(String literal) {
        return new LiteralArgumentType(literal);
    }
    
    /**
     * Ignores the case of the input.
     *
     * @return this, for chaining
     */
    @Kapi
    public LiteralArgumentType ignoreCase() {
        this.ignoreCase = true;
        return this;
    }
    
    @Kapi
    @Override
    public Result<String,String> parse(List<String> arguments) {
        String input = arguments.remove(0);
        if (ignoreCase) {
            if (input.equalsIgnoreCase(literal)) {
                return Result.ok(literal);
            } else {
                return Result.err("Invalid literal: " + input);
            }
        } else {
            if (input.equals(literal)) {
                return Result.ok(literal);
            } else {
                return Result.err("Invalid literal: " + input);
            }
        }
    }
    
    /**
     * Adds the literal as a suggestion.
     */
    @Kapi
    public void autoSuggest() {
        suggest = true;
    }
    
    @Kapi
    @Override
    public void getSuggestions(SuggestionCommandContext context) {
        if (!suggest) return;
        context.addSuggestion(literal);
    }
    
}
