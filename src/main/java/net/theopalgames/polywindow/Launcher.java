package net.theopalgames.polywindow;

import net.theopalgames.polywindow.lwjgl.LwjglWindow;
import net.theopalgames.polywindow.swing.SwingWindow;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.jar.Manifest;

public class Launcher {
    public static final int DEFAULT_X = 1400;
    public static final int DEFAULT_Y = 900;
    public static final int DEFAULT_Z = 1000;

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

    @SuppressWarnings("unchecked")
    public static Game createGame(FileManager files) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        String gameLocation;

        try (InputStream data = files.getInternalFileContents("META-INF/MANIFEST.MF")) {
            Manifest manifest = new Manifest(data);
            gameLocation = manifest.getMainAttributes().getValue("Game-Class");
        }

        if (gameLocation == null)
            throw new IllegalStateException("No Game class found!");

        Class<? extends Game> clazz = (Class<? extends Game>) Class.forName(gameLocation);
        Game game = clazz.newInstance();
        game.preInit(files);

        return game;
    }
}
