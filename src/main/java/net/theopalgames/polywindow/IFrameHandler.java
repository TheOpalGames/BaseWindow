package net.theopalgames.polywindow;

/**
 * Single interface that implements both {@link IUpdater} and {@link IDrawer}. Useful for only passing one object.
 *
 * @author hallowizer
 */
public interface IFrameHandler extends IUpdater, IDrawer {
    // just to implement both
}
