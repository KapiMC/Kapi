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

package me.kyren223.kapi.utility;


import me.kyren223.kapi.core.Kplugin;
import me.kyren223.kapi.annotations.Kapi;
import me.kyren223.kapi.commands.CommandBuilder;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;

import javax.annotation.Nullable;

/**
 * A utility class for registering commands and event listeners.
 */
@Kapi
public class KapiRegistry {
    
    private KapiRegistry() {
        throw new AssertionError("KapiRegistry should not be instantiated!");
    }
    
    /**
     * Registers a command.<br>
     * Using this method directly is not recommended,
     * use {@link CommandBuilder} instead.
     *
     * @param name The name of the command
     * @param executor The executor for the command
     * @param completer The tab completer for the command or null
     */
    @Kapi
    public static void register(String name, CommandExecutor executor, @Nullable TabCompleter completer) {
        PluginCommand command = Kplugin.get().getCommand(name);
        if (command == null) {
            Log.error("Command " + name + " does not exist!");
            throw new IllegalArgumentException("Command " + name + " does not exist!");
        }
        
        command.setExecutor(executor);
        if (completer != null) command.setTabCompleter(completer);
    }
    
    /**
     * Registers a listener on the server.
     *
     * @param listener An instance of a class that implements Listener
     */
    @Kapi
    public static void register(Listener listener) {
        Kplugin.get().getServer().getPluginManager().registerEvents(listener, Kplugin.get());
    }
}
