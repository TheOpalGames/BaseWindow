package net.theopalgames.polywindow.metal;

public class MetalCallbacks {
    private MetalWindow window;

    public MetalCallbacks(MetalWindow window) {
        this.window = window;
    }

    public void finishInit(long ctx) {
        window.finishInit(ctx);
    }

    public void drawFrame() {
        window.drawFrame();
    }

    public void appClosed() {
        try {
            window.onClose();
        } catch (Throwable e) {
            window.getGame().onError(e);
        }
    }
}
