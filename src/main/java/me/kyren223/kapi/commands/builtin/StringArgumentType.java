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

import java.util.List;

/**
 * Represents a command argument type for a string.
 */
@Kapi
@ApiStatus.Experimental
public class StringArgumentType implements ArgumentType<String> {
    
    private boolean toLowerCase;
    private boolean toUpperCase;
    
    private StringArgumentType() {
        this.toLowerCase = false;
        this.toUpperCase = false;
    }
    
    /**
     * Creates a new string argument type.
     *
     * @return the string argument type
     */
    @Kapi
    public static StringArgumentType string() {
        return new StringArgumentType();
    }
    
    /**
     * Converts the input to lower case.
     *
     * @return this, for chaining
     * @throws IllegalStateException if already converting to upper case
     */
    @Kapi
    public StringArgumentType toLowerCase() {
        if (toUpperCase) {
            throw new IllegalStateException("Cannot convert to lower case if already converting to upper case.");
        }
        this.toLowerCase = true;
        return this;
    }
    
    /**
     * Converts the input to upper case.
     *
     * @return this, for chaining
     * @throws IllegalStateException if already converting to lower case
     */
    @Kapi
    public StringArgumentType toUpperCase() {
        if (toLowerCase) {
            throw new IllegalStateException("Cannot convert to upper case if already converting to lower case.");
        }
        this.toUpperCase = true;
        return this;
    }
    
    @Kapi
    @Override
    public Result<String, String> parse(List<String> arguments) {
        String input = arguments.remove(0);
        String output;
        if (toLowerCase) {
            output = input.toLowerCase();
        } else if (toUpperCase) {
            output = input.toUpperCase();
        } else {
            output = input;
        }
        return Result.ok(output);
    }
    
    @Kapi
    @Override
    public void getSuggestions(SuggestionCommandContext context) {
        // Does nothing
    }
}
