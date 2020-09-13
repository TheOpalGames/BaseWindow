package net.theopalgames.polywindow.metal;

public class MetalNative {
    public native long init();
    public native void draw(long ctx, float[] vertexData);
}
