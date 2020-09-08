package net.theopalgames.basewindow;

public interface Game {
    void init(BaseWindow window);

    void onError(Throwable e);
    double getUsableWindowHeight();
}
