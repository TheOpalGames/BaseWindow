package net.theopalgames.polywindow.metal;

public class MetalCallbacks {
    private MetalWindow window;

    public MetalCallbacks(MetalWindow window) {
        this.window = window;
    }

    @SuppressWarnings("unused") // called by native code
    public void finishInit(long ctx) {
        window.finishInit(ctx);
    }

    @SuppressWarnings("unused")
    public void drawFrame() {
        try {
            window.drawFrame();
        } catch (Throwable e) {
            window.getGame().onError(e);
        }
    }

    @SuppressWarnings("unused")
    public void keyDown(char key) {

    }

    @SuppressWarnings("unused")
    public void keyUp(char key) {

    }

    public void mouseDown() {

    }

    public void mouseUp() {

    }

    @SuppressWarnings("unused")
    public void appClosed() {
        try {
            window.onClose();
        } catch (Throwable e) {
            window.getGame().onError(e);
        }
    }
}
