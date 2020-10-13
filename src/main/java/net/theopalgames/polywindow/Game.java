package net.theopalgames.polywindow;

/**
 * A class with event methods determining the game's configuration.
 *
 * @author hallowizer
 */
public interface Game {
    /**
     * Does initialization with a {@link FileManager}.
     *
     * @param fileManager The file manager to initialize with.
     */
    void preInit(FileManager fileManager);
    
    /**
     * Does the main initialization with a {@link Window}.
     *
     * @param window The window created for the game.
     */
    void init(Window window);
    
    /**
     * Gets the name of the game for purposes such as the menu bar.
     *
     * @return The game's name.
     */
    String getName();
    
    /**
     * Sets the settings of the first window created.
     *
     * @return A {@link WindowConfiguration} with the settings for the first window.
     */
    WindowConfiguration getInitialWindowConfig();
    
    /**
     * Gets an instance of {@link IWindowHandler} that gets called for window-related events.
     *
     * @return an instance of {@link IWindowHandler}
     */
    IWindowHandler getWindowHandler();
    
    /**
     * Determines whether this game can run Swing, useful for games that require 3D.
     *
     * @return whether Swing is supported.
     */
    boolean isSwingSupported();
    
    /**
     * Gets called when there is an error.
     *
     * @param e the error or exception that was caught.
     */
    void onError(Throwable e);
}
