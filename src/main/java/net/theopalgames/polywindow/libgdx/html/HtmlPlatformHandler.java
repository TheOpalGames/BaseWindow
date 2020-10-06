package net.theopalgames.polywindow.libgdx.html;

import net.theopalgames.polywindow.libgdx.PlatformHandler;

public final class HtmlPlatformHandler implements PlatformHandler {
    @Override
    public void quit() {
        throw new UnsupportedOperationException("Cannot quit in HTML!");
    }

    @Override
    public boolean canQuit() {
        return false;
    }
}
