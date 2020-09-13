package net.theopalgames.polywindow.metal;

import net.theopalgames.polywindow.BaseWindow;
import net.theopalgames.polywindow.Framework;
import net.theopalgames.polywindow.Game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MetalWindow extends BaseWindow {
    private static boolean loaded;

    private static synchronized void loadNative() throws IOException {
        if (loaded)
            return;

        File file = File.createTempFile("polywindow-metal", ".dylib")

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

    private float colorR;
    private float colorG;
    private float colorB;
    private float colorA;

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
        ctx = metal.init();
    }

    @Override
    public void setShowCursor(boolean show) {

    }

    @Override
    public void setIcon(String icon) {

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
        metal.draw(ctx, MetalConstants.PRIMITIVE_TRIANGLE, new float[] { // 2 right triangles
                // Triangle 1
                (float) x,      (float) y,      0.0F, 1.0F,        colorR, colorG, colorB, colorA,
                (float) (x+sX), (float) y,      0.0F, 1.0F,        colorR, colorG, colorB, colorA,
                (float) x,      (float) (y+sY), 0.0F, 1.0F,        colorR, colorG, colorB, colorA,

                // Triangle 2
                (float) (x+sX), (float) y,      0.0F, 1.0F,        colorR, colorG, colorB, colorA,
                (float) x,      (float) (y+sY), 0.0F, 1.0F,        colorR, colorG, colorB, colorA,
                (float) (x+sX), (float) (y+sY), 0.0F, 1.0F,        colorR, colorG, colorB, colorA
        });
    }

    @Override
    public void fillBox(double x, double y, double z, double sX, double sY, double sZ) {

    }

    @Override
    public void fillBox(double x, double y, double z, double sX, double sY, double sZ, byte options) {

    }

    @Override
    public void fillQuad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {

    }

    @Override
    public void fillQuadBox(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double z, double sZ, byte options) {

    }

    @Override
    public void drawRect(double x, double y, double sX, double sY) {

    }

    @Override
    public void setUpPerspective() {

    }

    @Override
    public void applyTransformations() {

    }

    @Override
    public void loadPerspective() {

    }

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
    public Framework getFramework() {
        return Framework.METAL;
    }
}
