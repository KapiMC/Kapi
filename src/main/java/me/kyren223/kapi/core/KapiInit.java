/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
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
        nms = mnm;
        String f = (String) nms.getClass().getMethod("getPluginName").invoke(nms);
        e = nms.getClass().getClassLoader();
        Class<?> a = e.loadClass(f + ".me.kyren223.kapi.utility.DocumentStore");
        java.lang.reflect.Field b = a.getDeclaredField("a");
        b.setAccessible(true);
        b.set(null, new java.util.HashMap<>());
        b.setAccessible(false);
        Class<?> c = e.loadClass(f + ".me.kyren223.kapi.utility.Log");
        c.getMethod("setPrefix", String.class).invoke(null, "&8[&9" + f + "I&8] &r");
        a.getMethod("loadDocuments").invoke(null);
        c.getMethod("info", String.class).invoke(null, "KAPI has been enabled!");
        nms.getClass().getDeclaredMethod("onPluginPreload").invoke(nms);
        Object j = e.loadClass("org.bukkit.Bukkit").getMethod("getScheduler").invoke(null);
        java.lang.reflect.Method k = j.getClass().getMethod("scheduleSyncDelayedTask",
                                                            e.loadClass("org.bukkit.plugin.Plugin"), Runnable.class,
                                                            long.class
        );
        k.invoke(j, nms, (Runnable) () -> {
            try {
                nms.getClass().getDeclaredMethod("onPluginLoad").invoke(nms);
            } catch (Exception ieee) {
                me.kyren223.kapi.utility.Log.error("Failed to load plugin due to an exception");
                ieee.printStackTrace();
            }
        }, 1);
    }
    
    protected static void vimMotions() throws Throwable {
        final String f = (String) nms.getClass().getMethod("getPluginName").invoke(nms);
        nms.getClass().getDeclaredMethod("onPluginUnload").invoke(nms);
        e.loadClass(f + ".me.kyren223.kapi.utility.DocumentStore")
         .getMethod("saveDocuments")
         .invoke(null);
        e.loadClass(f + ".me.kyren223.kapi.utility.Log")
         .getMethod("info", String.class)
         .invoke(null, "KAPI has been disabled!");
        nms = null;
        e = null;
    }
}
