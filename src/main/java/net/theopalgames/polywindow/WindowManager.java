package net.theopalgames.polywindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class that handles the windows the game has opened and creates new ones.
 *
 * @author hallowizer
 */
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
    
    /**
     * Retrieves the game associated with this window manager.
     *
     * @return The game.
     */
    public Game getGame() {
        return game;
    }
    
    /**
     * Creates a new {@link Window}.
     *
     * @param name The name to be shown on the window.
     * @param x The horizontal length of the window.
     * @param y The height of the window.
     * @param z
     * @param vsync Should vsync be enabled?
     * @param showMouse Should the mouse be shown?
     * @return The newly created window.
     *
     * @throws UnsupportedOperationException If window creation is not supported.
     */
    public Window newWindow(String name, int x, int y, int z, boolean vsync, boolean showMouse) {
        Window window = primaryWindow.newWindow(name, x, y, z, vsync, showMouse);
        windows.add(window);
        return window;
    }
    
    /**
     * Determines whether new windows can be created.
     *
     * @return Whether new windows can be made.
     */
    public boolean newWindowSupported() {
        return primaryWindow.canMakeNewWindow();
    }
    
    /**
     * Yields all windows that exist under this window manager.
     *
     * @return An immutable list containing the windows controlled by this window manager.
     */
    public List<Window> getWindows() {
        return Collections.unmodifiableList(windows);
    }
}
