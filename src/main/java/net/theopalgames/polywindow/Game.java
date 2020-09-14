package net.theopalgames.polywindow;

public interface Game {
    String getName();
    void init(BaseWindow window);

    IUpdater getUpdater();
    IDrawer getDrawer();
    IWindowHandler getWindowHandler();

    boolean isSwingSupported();

    void onError(Throwable e);
    double getUsableWindowHeight();
}
