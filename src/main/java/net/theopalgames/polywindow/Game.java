package net.theopalgames.polywindow;

public interface Game {
    void preInit(BaseFileManager fileManager);
    void init(BaseWindow window);

    String getName();

    IUpdater getUpdater();
    IDrawer getDrawer();
    IWindowHandler getWindowHandler();

    boolean isSwingSupported();

    void onError(Throwable e);
    double getUsableWindowHeight();
}
