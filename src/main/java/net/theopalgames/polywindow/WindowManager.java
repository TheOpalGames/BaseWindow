package net.theopalgames.polywindow;

import java.util.ArrayList;
import java.util.List;

public final class WindowManager {
    private final Game game;
    private Window primaryWindow;
    private final List<Window> windows = new ArrayList<>();

    WindowManager(Game game) {
        this.game = game;
    }

    void init(Window window) {
        if (primaryWindow != null)
            throw new IllegalStateException("Already initialized!");

        primaryWindow = window;
        windows.add(window);
    }

    public Game getGame() {
        return game;
    }

    public Window newWindow(String name, int x, int y, int z, boolean vsync, boolean showMouse) {
        Window window = primaryWindow.newWindow(name, x, y, z, vsync, showMouse);
        windows.add(window);
        return window;
    }

    public boolean newWindowSupported() {
        return primaryWindow.canMakeNewWindow();
    }
}
