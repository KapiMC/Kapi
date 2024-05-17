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
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a command argument type for a double.
 */
@Kapi
@ApiStatus.Experimental
@NullMarked
public class DoubleArgumentType implements ArgumentType<Double> {
    
    private double min;
    private double max;
    private @Nullable Predicate<Double> predicate;
    private String errorMessage;
    
    private DoubleArgumentType() {
        this.min = Double.MIN_VALUE;
        this.max = Double.MAX_VALUE;
        this.predicate = null;
        this.errorMessage = "Decimal %f is invalid";
    }
    
    /**
     * Creates a new double argument type.
     *
     * @return the double argument type
     */
    @Kapi
    public static DoubleArgumentType decimal() {
        return new DoubleArgumentType();
    }
    
    /**
     * Sets the minimum value (inclusive) for the double.
     *
     * @param min the minimum value
     * @return this, for chaining
     */
    @Kapi
    public DoubleArgumentType min(double min) {
        this.min = min;
        return this;
    }
    
    /**
     * Sets the maximum value (inclusive) for the double.
     *
     * @param max the maximum value
     * @return this, for chaining
     */
    @Kapi
    public DoubleArgumentType max(double max) {
        this.max = max;
        return this;
    }
    
    /**
     * Sets the range for the double.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return this, for chaining
     */
    @Kapi
    public DoubleArgumentType range(double min, double max) {
        this.min = min;
        this.max = max;
        return this;
    }
    
    /**
     * Sets the predicate for the double.<br>
     * The predicate should return true if the double is valid, false otherwise.<br>
     * See {@link #predicate(Predicate, String)} for a version with an error message.
     *
     * @param predicate the predicate
     * @return this, for chaining
     */
    @Kapi
    public DoubleArgumentType predicate(Predicate<Double> predicate) {
        this.predicate = predicate;
        return this;
    }
    
    /**
     * Sets the predicate for the double.<br>
     * The predicate should return true if the double is valid, false otherwise.<br>
     * See {@link #predicate(Predicate)} for a version without an error message.
     *
     * @param predicate    the predicate
     * @param errorMessage the error message if the predicate fails, use "%d" as a placeholder for the integer
     * @return this, for chaining
     */
    @Kapi
    public DoubleArgumentType predicate(Predicate<Double> predicate, @NotNull String errorMessage) {
        this.predicate = predicate;
        this.errorMessage = errorMessage;
        return this;
    }
    
    @Kapi
    @Override
    public Result<Double,String> parse(List<String> arguments) {
        String input = arguments.remove(0);
        double value;
        try {
            value = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return Result.err("Invalid decimal: " + input);
        }
        
        if (value < min) {
            return Result.err(String.format("Decimal %f is less than the minimum value %f", value, min));
        } else if (value > max) {
            return Result.err(String.format("Decimal %f is greater than the maximum value %f", value, max));
        } else if (predicate != null && !predicate.test(value)) {
            return Result.err(String.format(errorMessage, value));
        } else {
            return Result.ok(value);
        }
    }
    
    @Kapi
    @Override
    public void getSuggestions(SuggestionCommandContext context) {
        // Do nothing
    }
}
