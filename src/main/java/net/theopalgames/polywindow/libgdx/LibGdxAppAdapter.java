package net.theopalgames.polywindow.libgdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import net.theopalgames.polywindow.*;

public class LibGdxAppAdapter extends ApplicationAdapter {
    private static LibGdxAppAdapter instance;
    private static LibGdxWindow window;

    private static VibrationPlayer vibrationPlayer;
//    private static PlatformHandler platformHandler;
//    public static IKeyboardHeightListener keyboardHeightListener;
// TODO: What is this?

    private static double pointWidth = -1;
    private static double pointHeight = -1;

    private static Application.ApplicationType appType;

    public static void initialize(PlatformHandler platform)
    {
        FileManager files = new LibGdxFileManager();

        Game game;
        try {
            game = Launcher.createGame(files);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        window = new LibGdxWindow(game, platform);

//        window.appType = appType;
        window.fileManager = files;
//        Game.framework = Game.Framework.libgdx;
//        Game.initScript();
//        Game.game.window = window;

        window.getGame().init(window);
    }

    @Override
    public void create()
    {
        instance = this;

        window.absoluteWidth = Gdx.graphics.getWidth();
        window.absoluteHeight = Gdx.graphics.getHeight();
        window.absoluteDepth = 1000;

        window.initialize();

//        Game.game.window.vibrationPlayer = vibrationPlayer;
//        Game.game.window.vibrationsEnabled = vibrationPlayer != null;

//        Game.game.window.platformHandler = platformHandler;
//TODO: Tanks again!
        window.touchscreen = true;

        window.pointWidth = pointWidth;
        window.pointHeight = pointHeight;

//        if (Gdx.app.getType() == Application.ApplicationType.Android)
//            keyboardHeightListener.init();
        // TODO: Keyboard height listener

        window.run();
    }

    @Override
    public void resize(int width, int height)
    {
        window.absoluteWidth = width;
        window.absoluteHeight = height;

        window.updatePerspective();
    }

    @Override
    public void render() {
        window.render();
    }

    @Override
    public void dispose()
    {
//        Game.game.window.windowHandler.onWindowClose();
        window.windowHandler.onWindowClose();
    }

    @Override
    public void pause()
    {
//        if (Game.screen instanceof ScreenExit)
//        {
//            window.windowHandler.onWindowClose();
//            System.exit(0);
//        }
        // TODO: Tanks
    }

    @Override
    public void resume()
    {
        window.lastFrame = System.currentTimeMillis();

        LibGdxSoundPlayer soundPlayer = (LibGdxSoundPlayer) window.soundPlayer;
        if (soundPlayer.musicID != null && soundPlayer.combinedMusicMap.get(soundPlayer.musicID) != null)
        {
            float pos = soundPlayer.currentMusic.getPosition();
            long time = System.currentTimeMillis();

            for (Music m: soundPlayer.combinedMusicMap.get(soundPlayer.musicID))
            {
                m.stop();
            }

            for (Music m: soundPlayer.combinedMusicMap.get(soundPlayer.musicID))
            {
                m.setPosition(pos + (System.currentTimeMillis() - time) / 1000f);
                m.play();
            }
        }
    }
}
