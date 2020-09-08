package net.theopalgames.basewindow.libgdx;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import net.theopalgames.basewindow.BasePlatformHandler;
import net.theopalgames.basewindow.BaseVibrationPlayer;

public class LibGdxAppAdapter extends ApplicationAdapter {
    public static LibGdxAppAdapter instance;
    public static LibGdxWindow window;

    public static BaseVibrationPlayer vibrationPlayer;
    public static BasePlatformHandler platformHandler;
//    public static IKeyboardHeightListener keyboardHeightListener;
// TODO: What is this?

    public static double pointWidth = -1;
    public static double pointHeight = -1;

    public static Application.ApplicationType appType;

    public static void initialize()
    {
        // TODO: Too specific.
        // window = new LibGdxWindow("Tanks", 1400, 900, 1000, new GameUpdater(), new GameDrawer(), new GameWindowHandler(), false, true);

        window.appType = appType;
        window.fileManager = new LibGdxFileManager();
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
    }

    @Override
    public void resize(int width, int height)
    {
        window.absoluteWidth = width;
        window.absoluteHeight = height;

        window.updatePerspective();
    }

    @Override
    public void render()
    {
        window.render();
    }

    @Override
    public void dispose()
    {
//        Game.game.window.windowHandler.onWindowClose();
        // TODO: Tanks
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
