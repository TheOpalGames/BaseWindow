package net.theopalgames.polywindow.libgdx.html;

import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import net.theopalgames.polywindow.libgdx.LibGdxAppAdapter;

public class HtmlLauncher extends GwtApplication {
    @Override
    public GwtApplicationConfiguration getConfig ()
    {
        return new GwtApplicationConfiguration(480, 320);
    }

    @Override
    public LibGdxAppAdapter createApplicationListener ()
    {
        LibGdxAppAdapter.initialize(new HtmlPlatformHandler()); // before the return
        return new LibGdxAppAdapter();
    }
}
