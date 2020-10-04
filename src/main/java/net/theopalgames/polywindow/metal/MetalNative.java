package net.theopalgames.polywindow.metal;

public class MetalNative {
    public native void init(MetalCallbacks callbacks, boolean vsync, boolean showCursor);
    public native void draw(long ctx, int primitive, float[] vertexData);
    public native void changeMatrices(long ctx, double[][] matrices);

    public native void setTexture(long ctx, long addr, float originX, float originY, float sizeX, float sizeY);

    public native void toggleBoolean(long ctx, int property);
}
