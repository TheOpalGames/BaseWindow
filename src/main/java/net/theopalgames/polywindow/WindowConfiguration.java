package net.theopalgames.polywindow;

/**
 * A class containing the information needed to create a window. Used to create the initial window and can be used for other windows as well.
 *
 * @author hallowizer
 */
public class WindowConfiguration {
    /**
     * The default horizontal length of a new window.
     */
    public static final int DEFAULT_X = 1400;
    
    /**
     * The default height of a new window.
     */
    public static final int DEFAULT_Y = 900;
    
    /**
     * The default max depth that can be drawn in the window.
     */
    public static final int DEFAULT_Z = 1000;
    
    /**
     * The mouse starts shown by default.
     */
    public static final boolean SHOW_MOUSE_DEFAULT = true;
    
    /**
     * Vsync starts enabled by default.
     */
    public static final boolean VSYNC_DEFAULT = true;
    
    /**
     * The name of the window, to be displayed on the title bar.
     */
    public final String name;
    
    /**
     * The horizontal width of the window.
     */
    public final int x;
    
    /**
     * The height of the window.
     */
    public final int y;
    
    /**
     * The maximum depth that can be drawn in the window.
     */
    public final int z;
    
    /**
     * Whether vsync should be enabled at the start.
     */
    public final boolean vsync;
    
    /**
     * Whether the mouse should be shown at the start.
     */
    public final boolean showMouse;
    
    /**
     * The updater that gets called every frame.
     */
    public final IUpdater updater;
    
    /**
     * The drawer that gets called every frame.
     */
    public final IDrawer drawer;
    
    /**
     * Creates a new {@link WindowConfiguration}.
     *
     * @param name The name of the window, to be displayed on the title bar.
     * @param frameHandler The frame handler that gets called every frame.
     */
    public WindowConfiguration(String name, IFrameHandler frameHandler) {
        this(name, VSYNC_DEFAULT, SHOW_MOUSE_DEFAULT, frameHandler);
    }
    
    /**
     * Creates a new {@link WindowConfiguration}.
     *
     * @param name The name of the window, to be displayed on the title bar.
     * @param updater The updater that gets called every frame.
     * @param drawer The drawer that gets called every frame.
     */
    public WindowConfiguration(String name, IUpdater updater, IDrawer drawer) {
        this(name, VSYNC_DEFAULT, SHOW_MOUSE_DEFAULT, updater, drawer);
    }
    
    /**
     * Creates a new {@link WindowConfiguration}.
     *
     * @param name The name of the window, to be displayed on the title bar.
     * @param vsync Whether vsync should be enabled at the start.
     * @param showMouse Whether the mouse should be shown at the start.
     * @param frameHandler The frame handler that gets called every frame.
     */
    public WindowConfiguration(String name, boolean vsync, boolean showMouse, IFrameHandler frameHandler) {
        this(name, DEFAULT_X, DEFAULT_Y, DEFAULT_Z, vsync, showMouse, frameHandler);
    }
    
    /**
     * Creates a new {@link WindowConfiguration}.
     *
     * @param name The name of the window, to be displayed on the title bar.
     * @param vsync Whether vsync should be enabled at the start.
     * @param showMouse Whether the mouse should be shown at the start.
     * @param updater The updater that gets called every frame.
     * @param drawer The drawer that gets called every frame.
     */
    public WindowConfiguration(String name, boolean vsync, boolean showMouse, IUpdater updater, IDrawer drawer) {
        this(name, DEFAULT_X, DEFAULT_Y, DEFAULT_Z, vsync, showMouse, updater, drawer);
    }
    
    /**
     * Creates a new {@link WindowConfiguration}.
     *
     * @param name The name of the window, to be displayed on the title bar.
     * @param x The horizontal width of the window.
     * @param y The height of the window.
     * @param z The maximum depth that can be drawn in the window.
     * @param frameHandler The frame handler that gets called every frame.
     */
    public WindowConfiguration(String name, int x, int y, int z, IFrameHandler frameHandler) {
        this(name, x, y, z, VSYNC_DEFAULT, SHOW_MOUSE_DEFAULT, frameHandler);
    }
    
    /**
     * Creates a new {@link WindowConfiguration}.
     *
     * @param name The name of the window, to be displayed on the title bar.
     * @param x The horizontal width of the window.
     * @param y The height of the window.
     * @param z The maximum depth that can be drawn in the window.
     * @param updater The updater that gets called every frame.
     * @param drawer The drawer that gets called every frame.
     */
    public WindowConfiguration(String name, int x, int y, int z, IUpdater updater, IDrawer drawer) {
        this(name, x, y, z, VSYNC_DEFAULT, SHOW_MOUSE_DEFAULT, updater, drawer);
    }
    
    /**
     * Creates a new {@link WindowConfiguration}.
     *
     * @param name The name of the window, to be displayed on the title bar.
     * @param x The horizontal width of the window.
     * @param y The height of the window.
     * @param z The maximum depth that can be drawn in the window.
     * @param vsync Whether vsync should be enabled at the start.
     * @param showMouse Whether the mouse should be shown at the start.
     * @param frameHandler The frame handler that gets called every frame.
     */
    public WindowConfiguration(String name, int x, int y, int z, boolean vsync, boolean showMouse, IFrameHandler frameHandler) {
        this(name, x, y, z, vsync, showMouse, frameHandler, frameHandler);
    }
    
    /**
     * Creates a new {@link WindowConfiguration}.
     *
     * @param name The name of the window, to be displayed on the title bar.
     * @param x The horizontal width of the window.
     * @param y The height of the window.
     * @param z The maximum depth that can be drawn in the window.
     * @param vsync Whether vsync should be enabled at the start.
     * @param showMouse Whether the mouse should be shown at the start.
     * @param updater The updater that gets called every frame.
     * @param drawer The drawer that gets called every frame.
     */
    public WindowConfiguration(String name, int x, int y, int z, boolean vsync, boolean showMouse, IUpdater updater, IDrawer drawer) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.vsync = vsync;
        this.showMouse = showMouse;
        this.updater = updater;
        this.drawer = drawer;
    }
}
