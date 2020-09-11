package net.theopalgames.polywindow;

public interface Game {
    void init(BaseWindow window);

    void onError(Throwable e);
    double getUsableWindowHeight();
}
