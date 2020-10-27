package net.theopalgames.polywindow.metal;

public class MetalNativeCode {
    public native void init(MetalNativeCallbacks callbacks, boolean vsync, boolean showCursor);
    
    public void draw(long ctx, int primitive, float[] vertexData) {
        draw(ctx, primitive, vertexData, false);
    }
    
    public native void draw(long ctx, int primitive, float[] vertexData, boolean skipTransformations);
    
    public native void changeMatrices(long ctx, double[][] matrices);
    public native float[] transform(float[] vector);

    public native long loadTexture(long ctx, long bytes);
    public native void setTexture(long ctx, long addr, float originX, float originY, float sizeX, float sizeY);

    public native void toggleBoolean(long ctx, int property);
    
    public native String getClipboard();
    public native void setClipboard(String clipboard);
    
    public native long newWindow(long ctx, MetalNativeCallbacks newCallbacks);

    public native void quit();
}
