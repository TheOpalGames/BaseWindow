package net.theopalgames.polywindow.metal;

public class MetalNativeCallbacks {
    private MetalWindow window;
    
    MetalNativeCallbacks() {}
    MetalNativeCallbacks(MetalWindow window) {
        this.window = window;
    }
    
    void setWindow(MetalWindow window) {
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

    @SuppressWarnings("unused")
    public void mouseDown() {

    }

    @SuppressWarnings("unused")
    public void mouseUp() {

    }

    @SuppressWarnings("unused")
    public void windowClosed() {
        try {
            window.onClose();
        } catch (Throwable e) {
            window.getGame().onError(e);
        }
    }
}
