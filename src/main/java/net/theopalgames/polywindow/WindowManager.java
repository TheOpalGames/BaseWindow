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
    private List<Window> immutableWindowList;

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
     * @param name The name to be shown.
     * @param x The horizontal length of the window
     * @param y The height of the window.
     * @param z The max depth that can be drawn in the window.
     * @param vsync Should vsync be enabled?
     * @param showMouse Should the mouse be shown?
     * @param frameHandler The {@link IFrameHandler} that handles drawing and updating.
     * @return The new window.
     */
    public Window newWindow(String name, int x, int y, int z, boolean vsync, boolean showMouse, IFrameHandler frameHandler) {
        return newWindow(name, x, y, z, vsync, showMouse, frameHandler, frameHandler);
    }
    
    /**
     * Creates a new {@link Window}.
     *
     * @param name The name to be shown on the window.
     * @param x The horizontal length of the window.
     * @param y The height of the window.
     * @param z The max depth that can be drawn in the window.
     * @param vsync Should vsync be enabled?
     * @param showMouse Should the mouse be shown?
     * @param updater The updater that gets called every frame.
     * @param drawer The drawer that gets called every frame.
     * @return The newly created window.
     *
     * @throws UnsupportedOperationException If window creation is not supported.
     */
    public Window newWindow(String name, int x, int y, int z, boolean vsync, boolean showMouse, IUpdater updater, IDrawer drawer) {
        Window window = primaryWindow.newWindow(name, x, y, z, vsync, showMouse, updater, drawer);
        windows.add(window);
        immutableWindowList = null;
        
        window.run();
        return window;
    }
    
    /**
     * Creates a window from a {@link WindowConfiguration}.
     *
     * @param config The config with the settings for this window.
     * @return The newly created window.
     */
    public Window newWindow(WindowConfiguration config) {
        return newWindow(config.name, config.x, config.y, config.z, config.vsync, config.showMouse, config.updater, config.drawer);
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
        if (immutableWindowList != null)
            return immutableWindowList;
        
        immutableWindowList = Collections.unmodifiableList(windows);
        return immutableWindowList;
    }
}
