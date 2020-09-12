package net.theopalgames.polywindow;

import net.theopalgames.polywindow.lwjgl.LwjglWindow;
import net.theopalgames.polywindow.swing.SwingWindow;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.jar.Manifest;

public class Launcher {
    public static void main(String[] args) throws Exception{
        BaseFileManager files = new ComputerFileManager();
        Game game = createGame(files);

        BaseWindow window;

        if (Arrays.asList(args).contains("swing")) {
            if (game.isSwingSupported())
                window = new SwingWindow(game, game.getName(), 1400, 900, 1000, false, true);

            throw new IllegalArgumentException("Cannot use Swing as it is not supported in " + game.getName());
        } else
            window = new LwjglWindow(game, game.getName(), 1400, 900, 1000, false, true);

        window.fileManager = files;
        game.init(window);

        window.run();
    }

    public static Game createGame(BaseFileManager files) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        InputStream data = files.getInternalFileContents("META-INF/MANIFEST.MF");
        Manifest manifest = new Manifest(data);

        String gameLocation = manifest.getMainAttributes().getValue("Game-Class");
        if (gameLocation == null)
            throw new IllegalStateException("No Game class found!");

        Class<? extends Game> clazz = (Class<? extends Game>) Class.forName(gameLocation);
        return clazz.newInstance();
    }
}
