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

package me.kyren223.kapi.data;

import me.kyren223.kapi.annotations.Kapi;

/**
 * A simple pair of two objects.
 * @param <T> The first object
 * @param <U> The second object
 */
@Kapi
public class Pair<T, U> {
    @Kapi private T first;
    @Kapi private U second;
    
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
    
    public Pair() {
        this(null, null);
    }
    
    @Kapi
    public T getFirst() {
        return first;
    }
    
    @Kapi
    public void setFirst(T first) {
        this.first = first;
    }
    
    @Kapi
    public U getSecond() {
        return second;
    }
    
    @Kapi
    public void setSecond(U second) {
        this.second = second;
    }
    
    /**
     * Creates a new pair with the same values as this pair.<br>
     * Note: This is a shallow copy and does not copy the values themselves.
     *
     * @return A new pair with the same values as this pair
     */
    @Kapi
    public Pair<T, U> copy() {
        return new Pair<>(first, second);
    }
    
    /**
     * Returns a string representation of this pair.<br>
     * The string will be in the format "Pair(first, second)".
     *
     * @return A string representation of this pair
     */
    @Kapi
    @Override
    public String toString() {
        return "Pair(" + first + ", " + second + ")";
    }
    
    /**
     * Creates a new pair with the given values.
     *
     * @param first The first value
     * @param second The second value
     * @param <T> The type of the first value
     * @param <U> The type of the second value
     * @return A new pair with the given values
     */
    @Kapi
    public static <T, U> Pair<T, U> of(T first, U second) {
        return new Pair<>(first, second);
    }
    
}
