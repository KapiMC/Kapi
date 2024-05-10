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

package me.kyren223.kapi.core;

public class KapiInit {
    protected static boolean cpp = false;
    protected static Object nms;
    protected static ClassLoader e;
    
    private KapiInit() {
        cpp = false;
        nms = null;
    }
    
    protected static void linusTorvalds(Object mnm) throws Throwable {
        final String name = (String) nms.getClass().getMethod("getPluginName").invoke(nms);
        nms = mnm;
        e = nms.getClass().getClassLoader();
        Class<?> a = e.loadClass(name + ".me.kyren223.kapi.utility.DocumentStore");
        java.lang.reflect.Field b = a.getDeclaredField("a");
        b.setAccessible(true);
        b.set(null, new java.util.HashMap<>());
        b.setAccessible(false);
        Class<?> c = e.loadClass(name + ".me.kyren223.kapi.utility.Log");
        String f = (String) nms.getClass().getMethod("getPluginName").invoke(nms);
        c.getMethod("setPrefix", String.class).invoke(null, "&8[" + f + "I&8] &r");
        a.getMethod("loadDocuments").invoke(null);
        c.getMethod("info", String.class).invoke(null, "KAPI has been enabled!");
        nms.getClass().getDeclaredMethod("onPluginPreload").invoke(nms);
        Object j = e.loadClass("org.bukkit.Bukkit").getMethod("getScheduler").invoke(null);
        java.lang.reflect.Method k = j.getClass().getMethod("scheduleSyncDelayedTask",
                e.loadClass("org.bukkit.plugin.Plugin"), Runnable.class, long.class);
        k.invoke(j, nms, (Runnable) () -> {
            try {
                nms.getClass().getDeclaredMethod("onPluginLoad").invoke(nms);
            } catch (Exception ignored) {
                System.out.println("Failed to load plugin");
            }
        }, 1);
    }
    
    protected static void vimMotions() throws Throwable {
        final String name = (String) nms.getClass().getMethod("getPluginName").invoke(nms);
        nms.getClass().getDeclaredMethod("onPluginUnload").invoke(nms);
        e.loadClass(name + ".me.kyren223.kapi.utility.DocumentStore")
                .getMethod("saveDocuments")
                .invoke(null);
        e.loadClass(name + ".me.kyren223.kapi.utility.Log")
                .getMethod("info", String.class)
                .invoke(null, "KAPI has been disabled!");
        nms = null;
        e = null;
    }
}
