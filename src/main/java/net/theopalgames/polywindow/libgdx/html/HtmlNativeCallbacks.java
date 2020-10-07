package net.theopalgames.polywindow.libgdx.html;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public enum HtmlNativeCallbacks {
    ; // no instances

    private static final Map<String, Callable<Void>> callbacks = new IdentityHashMap<>();

    @SuppressWarnings("unused") // invoked by JS code
    public static void invokeCallback(String name) throws Exception {
        callbacks.get(name).call();
    }

    public static void registerCallback(String name, Callable<Void> callback) {
        if (callbacks.containsKey(name))
            throw new IllegalArgumentException("Callback " + name + " already exists!");

        callbacks.put(name, callback);
    }

    public static void removeCallback(String name) {
        callbacks.remove(name);
    }

    public static String randomName() {
        return "polywindowCallback_" + String.valueOf(Math.random()).replace(".", "");
    }
}
