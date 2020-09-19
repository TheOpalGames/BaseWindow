package net.theopalgames.polywindow.metal;

public class MetalNative {
    public native long init(MetalCallbacks callbacks);
    public native void draw(long ctx, int primitive, float[] vertexData);
}
