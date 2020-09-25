package net.theopalgames.polywindow.metal;

import net.theopalgames.polywindow.BaseWindow;
import net.theopalgames.polywindow.Framework;
import net.theopalgames.polywindow.Game;
import net.theopalgames.polywindow.transformation.Transformation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MetalWindow extends BaseWindow {
    private static boolean loaded;

    private static synchronized void loadNative() throws IOException {
        if (loaded)
            return;

        File file = File.createTempFile("polywindow-metal", ".dylib");

        try {
            InputStream in = MetalWindow.class.getResourceAsStream("/metal.dylib");
            byte[] bytes = new byte[in.available()];
            in.read(bytes);

            try (FileOutputStream out = new FileOutputStream(file)) {
                out.write(bytes);
            }

            System.load(file.getName());
        } finally {
            file.delete();
        }

        loaded = true;
    }

    private MetalNative metal;
    private long ctx;
    private MetalCallbacks callbacks; // only to prevent GC.

    private float colorR;
    private float colorG;
    private float colorB;
    private float colorA;

    private List<Transformation> transformations;

    public MetalWindow(Game game, String name, int x, int y, int z, boolean vsync, boolean showMouse) {
        super(game, name, x, y, z, vsync, showMouse);
    }

    @Override
    public void run() {
        try {
            loadNative();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Metal native", e);
        }

        metal = new MetalNative();

        callbacks = new MetalCallbacks(this);
        metal.init(callbacks);
    }

    void finishInit(long ctx) {
        this.ctx = ctx;
        this.transformations = new MetalTransformationList(ctx, metal);
    }

    @Override
    public void setShowCursor(boolean show) {

    }

    @Override
    public void setIcon(String icon) {
                // why is this here anyway? It's useless. Window icons don't exist.
    }

    @Override
    public void fillOval(double x, double y, double sX, double sY) {

    }

    @Override
    public void fillOval(double x, double y, double z, double sX, double sY, boolean depthTest) {

    }

    @Override
    public void fillFacingOval(double x, double y, double z, double sX, double sY, boolean depthTest) {

    }

    @Override
    public void fillGlow(double x, double y, double sX, double sY) {

    }

    @Override
    public void fillGlow(double x, double y, double z, double sX, double sY, boolean depthTest) {

    }

    @Override
    public void fillFacingGlow(double x, double y, double z, double sX, double sY, boolean depthTest) {

    }

    @Override
    public void setColor(double r, double g, double b, double a) {
        colorR = (float) r/255;
        colorG = (float) g/255;
        colorB = (float) b/255;
        colorA = (float) a/255;
    }

    @Override
    public void setColor(double r, double g, double b) {
        setColor(r, g, b, 255.0);
    }

    @Override
    public void drawOval(double x, double y, double sX, double sY) {

    }

    @Override
    public void drawOval(double x, double y, double z, double sX, double sY) {

    }

    @Override
    public void fillRect(double x, double y, double sX, double sY) {
        fillQuad(x, y, x+sX, y, x+sX, y+sY, x, y+sY);
    }

    @Override
    public void fillBox(double x, double y, double z, double sX, double sY, double sZ) {
        fillBox(x, y, z, sX, sY, sZ, (byte) 0);
    }

    @Override
    public void fillBox(double x, double y, double z, double sX, double sY, double sZ, byte options) {
        fillQuadBox(x, y, x+sX, y, x+sX, y+sY, x, y+sY, z, sZ, options);
    }

    @Override
    public void fillQuad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        fillQuad(x1, y1, x2, y2, x3, y3, x4, y4, 0.0);
    }

    public void fillQuad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double z) {
        metal.draw(ctx, MetalConstants.PRIMITIVE_TRIANGLE, new float[] { // 2 triangles
                // Triangle 1
                (float) x1,      (float) y1,      (float) z, 1.0F,        colorR, colorG, colorB, colorA,
                (float) x2,      (float) y2,      (float) z, 1.0F,        colorR, colorG, colorB, colorA,
                (float) x3,      (float) y3,      (float) z, 1.0F,        colorR, colorG, colorB, colorA,

                // Triangle 2
                (float) x3,      (float) y3,      (float) z, 1.0F,        colorR, colorG, colorB, colorA,
                (float) x4,      (float) y4,      (float) z, 1.0F,        colorR, colorG, colorB, colorA,
                (float) x1,      (float) y1,      (float) z, 1.0F,        colorR, colorG, colorB, colorA
        });
    }

    @Override
    public void fillQuadBox(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double z, double sZ, byte options) {
        // 0x01: hide back
        // 0x02: hide front
        // 0x04: hide bottom
        // 0x08: hide top
        // 0x10: hide left
        // 0x20: hide right

        if ((options & 0x02) == 0) // front
            fillQuad(x1, y1, x2, y2, x3, y3, x4, y4, z);

        if ((options & 0x01) == 0) // back
            fillQuad(x1, y1, x2, y2, x3, y3, x4, y4, z+sZ);

        if ((options & 0x04) == 0) // bottom
            metal.draw(ctx, MetalConstants.PRIMITIVE_TRIANGLE, new float[] {
                    // Triangle 1
                    (float) x4, (float) y4, (float) z,      1.0F,    colorR, colorG, colorB, colorA,
                    (float) x4, (float) y4, (float) (z+sZ), 1.0F,    colorR, colorG, colorB, colorA,
                    (float) x3, (float) y3, (float) z,      1.0F,    colorR, colorG, colorB, colorA,

                    // Triangle 2
                    (float) x4, (float) y4, (float) (z+sZ), 1.0F,    colorR, colorG, colorB, colorA,
                    (float) x3, (float) y3, (float) (z+sZ), 1.0F,    colorR, colorG, colorB, colorA,
                    (float) x3, (float) y3, (float) z,      1.0F,    colorR, colorG, colorB, colorA
            });

        if ((options & 0x08) == 0) // top
            metal.draw(ctx, MetalConstants.PRIMITIVE_TRIANGLE, new float[] {
                    // Triangle 1
                    (float) x1, (float) y1, (float) z,      1.0F,    colorR, colorG, colorB, colorA,
                    (float) x1, (float) y1, (float) (z+sZ), 1.0F,    colorR, colorG, colorB, colorA,
                    (float) x2, (float) y2, (float) z,      1.0F,    colorR, colorG, colorB, colorA,

                    // Triangle 2
                    (float) x1, (float) y1, (float) (z+sZ), 1.0F,    colorR, colorG, colorB, colorA,
                    (float) x2, (float) y2, (float) (z+sZ), 1.0F,    colorR, colorG, colorB, colorA,
                    (float) x2, (float) y2, (float) z,      1.0F,    colorR, colorG, colorB, colorA
            });

        if ((options & 0x10) == 0) // left
            metal.draw(ctx, MetalConstants.PRIMITIVE_TRIANGLE, new float[] {
                    // Triangle 1
                    (float) x4, (float) y4, (float) z,      1.0F,     colorR, colorG, colorB, colorA,
                    (float) x4, (float) y4, (float) (z+sZ), 1.0F,     colorR, colorG, colorB, colorA,
                    (float) x1, (float) y1, (float) z,      1.0F,     colorR, colorG, colorB, colorA,

                    // Triangle 2
                    (float) x4, (float) y4, (float) (z+sZ), 1.0F,     colorR, colorG, colorB, colorA,
                    (float) x1, (float) y1, (float) (z+sZ), 1.0F,     colorR, colorG, colorB, colorA,
                    (float) x1, (float) y1, (float) z,      1.0F,     colorR, colorG, colorB, colorA
            });

        if ((options & 0x20) == 0) // right
            metal.draw(ctx, MetalConstants.PRIMITIVE_TRIANGLE, new float[] {
                    // Triangle 1
                    (float) x3, (float) y3, (float) z,      1.0F,       colorR, colorG, colorB, colorA,
                    (float) x2, (float) y2, (float) (z+sZ), 1.0F,       colorR, colorG, colorB, colorA,
                    (float) x2, (float) y2, (float) z,      1.0F,       colorR, colorG, colorB, colorA,

                    // Triangle 2
                    (float) x3, (float) y3, (float) (z+sZ), 1.0F,       colorR, colorG, colorB, colorA,
                    (float) x2, (float) y2, (float) (z+sZ), 1.0F,       colorR, colorG, colorB, colorA,
                    (float) x2, (float) y2, (float) z,      1.0F,       colorR, colorG, colorB, colorA
            });
    }

    @Override
    public void drawRect(double x, double y, double sX, double sY) {
        metal.draw(ctx, MetalConstants.PRIMITIVE_LINE_STRIP, new float[] {
                (float) x,      (float) y,      0.0F, 1.0F,       colorR, colorG, colorB, colorA,
                (float) (x+sX), (float) y,      0.0F, 1.0F,       colorR, colorG, colorB, colorA,
                (float) (x+sX), (float) (y+sY), 0.0F, 1.0F,       colorR, colorG, colorB, colorA,
                (float) x,      (float) (y+sY), 0.0F, 1.0F,       colorR, colorG, colorB, colorA,
                (float) x,      (float) y,      0.0F, 1.0F,       colorR, colorG, colorB, colorA // closed loop
        });
    }

//    @Override
//    public void setUpPerspective() {
//
//    }
//
//    @Override
//    public void applyTransformations() {
//        for (int i = transformations.size()-1; i >= 0; i--)
//            transformations.get(i).apply();
//    }
//
//    @Override
//    public void loadPerspective() {
//
//    }

    @Override
    public void drawImage(double x, double y, double sX, double sY, String image, boolean scaled) {

    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, String image, boolean scaled) {

    }

    @Override
    public void drawImage(double x, double y, double sX, double sY, double u1, double v1, double u2, double v2, String image, boolean scaled) {

    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, double u1, double v1, double u2, double v2, String image, boolean scaled) {

    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, double u1, double v1, double u2, double v2, String image, boolean scaled, boolean depthtest) {

    }

    @Override
    public void drawImage(double x, double y, double sX, double sY, String image, double rotation, boolean scaled) {

    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, String image, double rotation, boolean scaled) {

    }

    @Override
    public void drawImage(double x, double y, double sX, double sY, double u1, double v1, double u2, double v2, String image, double rotation, boolean scaled) {

    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, double u1, double v1, double u2, double v2, String image, double rotation, boolean scaled) {

    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, double u1, double v1, double u2, double v2, String image, double rotation, boolean scaled, boolean depthtest) {

    }

    @Override
    public String getClipboard() {
        return null;
    }

    @Override
    public void setClipboard(String s) {

    }

    @Override
    public void setVsync(boolean enable) {

    }

    @Override
    public ArrayList<Integer> getRawTextKeys() {
        return null;
    }

    @Override
    public String getKeyText(int key) {
        return null;
    }

    @Override
    public String getTextKeyText(int key) {
        return null;
    }

    @Override
    public int translateKey(int key) {
        return 0;
    }

    @Override
    public int translateTextKey(int key) {
        return 0;
    }

    @Override
    public void transform(double[] matrix) {

    }

    @Override
    public double getEdgeBounds() {
        return 0;
    }

    @Override
    public void setBatchMode(boolean enabled, boolean quads, boolean depth) {

    }

    @Override
    public void setBatchMode(boolean enabled, boolean quads, boolean depth, boolean glow) {

    }

    @Override
    public void addVertex(double x, double y, double z) {

    }

    @Override
    public void addVertex(double x, double y) {

    }

    @Override
    public List<Transformation> getTransformations() {
        return transformations;
    }

    @Override
    public Framework getFramework() {
        return Framework.METAL;
    }

    void drawFrame() {
        updater.update();
        drawer.draw();
    }

    void onClose() {
        windowHandler.onWindowClose();
    }
}
