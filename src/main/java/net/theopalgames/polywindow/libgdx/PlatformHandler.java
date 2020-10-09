package net.theopalgames.polywindow.libgdx;

import net.theopalgames.polywindow.IDrawer;
import net.theopalgames.polywindow.IUpdater;
import net.theopalgames.polywindow.Window;
import net.theopalgames.polywindow.WindowManager;

public interface PlatformHandler {
    void quit();
    boolean canQuit();

    Window newWindow(WindowManager manager, String name, int x, int y, int z, boolean vsync, boolean showMouse, IUpdater updater, IDrawer drawer);
    boolean canMakeNewWindow();
    
    double getUsableWindowHeight();
}
