package net.theopalgames.polywindow;

import net.theopalgames.polywindow.lwjgl.LwjglWindow;
import net.theopalgames.polywindow.swing.SwingWindow;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.jar.Manifest;

/**
 * Launches PolyWindow on the desktop.
 *
 * @author hallowizer
 */
public enum Launcher {
    ;
    
    /**
     * The default horizontal length of a new window.
     */
    public static final int DEFAULT_X = 1400;
    
    /**
     * The default height of a new window.
     */
    public static final int DEFAULT_Y = 900;
    
    // what is this? render distance?
    public static final int DEFAULT_Z = 1000;
    
    /**
     * Main function, please don't call.
     *
     * @param args The command line arguments.
     * @throws Exception If something goes wrong somewhere.
     */
    public static void main(String[] args) throws Exception{
        FileManager files = new ComputerFileManager();
        Game game = createGame(files);

        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", game.getName());

        Window window;

        if (Arrays.asList(args).contains("swing")) {
            if (!game.isSwingSupported())
                throw new IllegalArgumentException("Cannot use Swing as it is not supported in " + game.getName());
            
            window = new SwingWindow(game);
        } else
            window = new LwjglWindow(game);

        window.fileManager = files;
        game.init(window);

        window.run();
    }
    
    /**
     * Creates a game from a {@link FileManager} and calls its preInit function.
     *
     * @param files The file manager to use.
     * @return An instance of the game.
     * @throws IOException If an error occurs reading the manifest for the game.
     * @throws ClassNotFoundException If the game class doesn't exist.
     * @throws IllegalAccessException Shouldn't throw.
     * @throws InstantiationException Shouldn't throw.
     * @throws NoSuchMethodException If the game class doesn't have a default constructor.
     * @throws InvocationTargetException If the constructor throws an exception.
     */
    @SuppressWarnings("unchecked")
    public static Game createGame(FileManager files) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String gameLocation;

        try (InputStream data = files.getInternalFileContents("META-INF/MANIFEST.MF")) {
            Manifest manifest = new Manifest(data);
            gameLocation = manifest.getMainAttributes().getValue("Game-Class");
        }

        if (gameLocation == null)
            throw new IllegalStateException("No Game class found!");

        Class<? extends Game> clazz = (Class<? extends Game>) Class.forName(gameLocation);
        Game game = clazz.getDeclaredConstructor().newInstance();
        game.preInit(files);

        return game;
    }
}
