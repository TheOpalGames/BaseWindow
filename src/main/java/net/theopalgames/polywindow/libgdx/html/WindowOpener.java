package net.theopalgames.polywindow.libgdx.html;

import com.google.gwt.core.client.JavaScriptObject;
import net.theopalgames.polywindow.Window;
import net.theopalgames.polywindow.WindowManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

final class WindowOpener {
    private static final Map<Integer, Window> createdWindows = new HashMap<>();
    private static final AtomicInteger nextId = new AtomicInteger();
    
    private final String callbackName = HtmlNativeCallbacks.randomName();
    private JavaScriptObject newWindow;
    private WindowManager manager;
    private WindowConfig config;
    
    private JavaScriptObject promise;
    private JavaScriptObject loadHandler;

    JavaScriptObject open(WindowManager manager, String name, int x, int y, int z, boolean vsync, boolean showMouse) {
        this.manager = manager;
        config = new WindowConfig(name, x, y, z, vsync, showMouse);
        promise = HtmlNativeCode.newPromise();
        
        HtmlNativeCallbacks.registerCallback(callbackName, this::onLoad);

        newWindow = HtmlNativeCode.newWindow("childWindow.html");
        loadHandler = HtmlNativeCode.addEventListener(newWindow, "load", callbackName);
        
        return promise;
    }

    private Void onLoad() throws Exception {
        HtmlNativeCallbacks.removeCallback(callbackName);
        HtmlNativeCode.removeEventListener(newWindow, "load", loadHandler);
        
        int id = nextId.getAndIncrement();
        Window windowStub = new HtmlStubWindow(manager, config.name, config.x, config.y, config.z, config.vsync, config.showMouse, newWindow);
        createdWindows.put(id, windowStub);
        
        HtmlNativeCode.resolvePromise(promise, id);
        HtmlNativeCode.sendMessage(newWindow, config.serialize()); // window configuration and they get our JS window object.

        return null;
    }
    
    static Window getWindow(int id) {
        return createdWindows.remove(id);
    }
}
