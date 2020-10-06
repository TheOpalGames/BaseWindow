package net.theopalgames.polywindow;

public interface Game {
    void preInit(FileManager fileManager);
    void init(Window window);

    String getName();

    IUpdater getUpdater();
    IDrawer getDrawer();
    IWindowHandler getWindowHandler();

    boolean isSwingSupported();

    void onError(Throwable e);
    double getUsableWindowHeight();
}
