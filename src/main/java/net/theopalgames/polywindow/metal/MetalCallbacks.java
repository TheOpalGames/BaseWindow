package net.theopalgames.polywindow.metal;

public class MetalCallbacks {
    private MetalWindow window;

    public MetalCallbacks(MetalWindow window) {
        this.window = window;
    }

    public void drawFrame() {
        window.drawFrame();
    }

    public void appClosed() {
        window.onClose();
    }
}
