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
import me.kyren223.kapi.data.Result;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.List;

/**
 * Represents a Command Argument Type.
 *
 * @param <T> The java type of the argument (cannot be null).
 */
@Kapi
@ApiStatus.Experimental
@NullMarked
public interface ArgumentType<T> {
    /**
     * For classes that implement {@link ArgumentType}, this method parses the argument.<br>
     *
     * @param arguments The list of command arguments that haven't been parsed yet<br>
     *                  A list is being used because the argument may be a multi-word string.<br>
     *                  Guaranteed to have at least one element.<br>
     *                  <br>
     *                  Note: This is a mutable list, you should remove all the arguments you've parsed.<br>
     *                  Removing the parsed arguments ensures that the next argument type
     *                  won't parse the same arguments.<br>
     * @return A Result containing the parsed argument or an error message.
     */
    @Kapi
    Result<T,String> parse(List<String> arguments);
    
    /**
     * Should NOT be called by the user.<br>
     * <br>
     * This should only be implemented by classes that implement {@link ArgumentType}.<br>
     * To add a suggestion value, you should call the
     * {@link SuggestionCommandContext#addSuggestion(String)} method.<br>
     *
     * @param context The context of the suggestion.
     */
    @Kapi
    void getSuggestions(SuggestionCommandContext context);
}
