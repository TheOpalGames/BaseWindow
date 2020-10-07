package net.theopalgames.polywindow.libgdx.html;

import com.google.gwt.core.client.JavaScriptObject;
import net.theopalgames.polywindow.Window;
import net.theopalgames.polywindow.WindowManager;
import net.theopalgames.polywindow.libgdx.PlatformHandler;

public final class HtmlPlatformHandler implements PlatformHandler {
    @Override
    public void quit() {
        throw new UnsupportedOperationException("Cannot quit in HTML!");
    }

    @Override
    public boolean canQuit() {
        return false;
    }

    @Override
    public Window newWindow(WindowManager manager, String name, int x, int y, int z, boolean vsync, boolean showMouse) {
        JavaScriptObject promise = new WindowOpener().open(manager, name, x, y, z, vsync, showMouse);
        int res = HtmlNativeCode.awaitPromise(promise);
        return WindowOpener.getWindow(res);
    }

    @Override
    public boolean canMakeNewWindow() {
        return true;
    }
}
