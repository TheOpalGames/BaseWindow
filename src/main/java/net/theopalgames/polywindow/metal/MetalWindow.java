package net.theopalgames.polywindow.metal;

import de.matthiasmann.twl.utils.PNGDecoder;
import net.theopalgames.polywindow.*;
import net.theopalgames.polywindow.transformation.RotationAboutPoint;
import net.theopalgames.polywindow.transformation.Transformation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetalWindow extends Window {
    private static boolean loaded;
    private static Field byteBufAddr;

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

    private Map<String, Long> textures = new HashMap<>();

    private boolean vsync;
    private boolean showMouse;

    private final PolygonBuilder polygons = new PolygonBuilder(this);

    public MetalWindow(Game game, String name, int x, int y, int z, boolean vsync, boolean showMouse) {
        super(game, name, x, y, z, vsync, showMouse);
        this.vsync = vsync;
        this.showMouse = showMouse;
    }

    Game getGame() {
        return game;
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
        metal.init(callbacks, vsync, showMouse);
    }

    void finishInit(long ctx) {
        this.ctx = ctx;
        this.transformations = new MetalTransformationList(ctx, metal);
    }

    @Override
    public void setShowCursor(boolean show) {
        if (show != showMouse)
            metal.toggleBoolean(ctx, MetalConstants.PROPERTY_SHOWCURSOR);
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
        drawImage(x, y, 0, sX, sY, image, scaled);
    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, String image, boolean scaled) {
        drawImage(x, y, z, sX, sY, 0, 0, 1, 1, image, scaled);
    }

    @Override
    public void drawImage(double x, double y, double sX, double sY, double u1, double v1, double u2, double v2, String image, boolean scaled) {
        drawImage(x, y, 0, sX, sY, u1, v1, u2, v2, image, scaled);
    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, double u1, double v1, double u2, double v2, String image, boolean scaled) {
        drawImage(x, y, z, sX, sY, u1, v1, u2, v2, image, scaled, true);
    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, double u1, double v1, double u2, double v2, String image, boolean scaled, boolean depthtest) {
        drawImage(x, y, z, sX, sY, u1, v1, u2, v2, image, 0, scaled, depthtest);
    }

    @Override
    public void drawImage(double x, double y, double sX, double sY, String image, double rotation, boolean scaled) {
        drawImage(x, y, 0, sX, sY, image, rotation, scaled);
    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, String image, double rotation, boolean scaled) {
        drawImage(x, y, z, sX, sY, 0, 0, 1, 1, image, rotation, scaled);
    }

    @Override
    public void drawImage(double x, double y, double sX, double sY, double u1, double v1, double u2, double v2, String image, double rotation, boolean scaled) {
        drawImage(x, y, 0, sX, sY, u1, v1, u2, v2, image, rotation, scaled);
    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, double u1, double v1, double u2, double v2, String image, double rotation, boolean scaled) {
        drawImage(x, y, z, sX, sY, u1, v1, u2, v2, image, rotation, scaled, true);
    }

    @Override
    public void drawImage(double x, double y, double z, double sX, double sY, double u1, double v1, double u2, double v2, String image, double rotation, boolean scaled, boolean depthtest) {
        long addr = textures.computeIfAbsent(image, this::createTexture);
        metal.setTexture(ctx, addr, (float) u1, (float) v1, (float) (u2-u1), (float) (v2-v1));

        if (rotation != 0.0)
            transformations.add(new RotationAboutPoint(this, 0.0, 0.0, rotation, x + sX/2, y + sY/2, z));

        metal.draw(ctx, MetalConstants.PRIMITIVE_TRIANGLE, new float[] { // 2 triangles
                // Triangle 1
                (float) x,           (float) y,           (float) z, 1.0F,        1.0F, 1.0F, 1.0F, 1.0F,
                (float) (x+sX),      (float) y,           (float) z, 1.0F,        1.0F, 1.0F, 1.0F, 1.0F,
                (float) (x+sX),      (float) (y+sY),      (float) z, 1.0F,        1.0F, 1.0F, 1.0F, 1.0F,

                // Triangle 2
                (float) (x+sX),      (float) (y+sY),      (float) z, 1.0F,        1.0F, 1.0F, 1.0F, 1.0F,
                (float) x,           (float) (y+sY),      (float) z, 1.0F,        1.0F, 1.0F, 1.0F, 1.0F,
                (float) x,           (float) y,           (float) z, 1.0F,        1.0F, 1.0F, 1.0F, 1.0F
        });

        if (rotation != 0.0)
            transformations.remove(transformations.size()-1);

        metal.setTexture(ctx, 0, 0, 0, 0, 0);
    }

    private long createTexture(String image) {
        InputStream in = fileManager.getInternalFileContents(image);

        if (in == null)
            in = fileManager.getInternalFileContents("/missing.png");

        try {
            PNGDecoder decoder = new PNGDecoder(in);
            ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
            decoder.decode(buf, decoder.getWidth()*4, PNGDecoder.Format.BGRA);

            return loadAddrField().getLong(buf);
        } catch (IOException | ReflectiveOperationException e) {
            throw new RuntimeException("Could not load texture " + image, e);
        }
    }

    private static synchronized Field loadAddrField() throws ReflectiveOperationException {
        if (byteBufAddr != null)
            return byteBufAddr;

        byteBufAddr = Buffer.class.getDeclaredField("address");
        byteBufAddr.setAccessible(true);
        return byteBufAddr;
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
        if (vsync != enable)
            metal.toggleBoolean(ctx, MetalConstants.PROPERTY_VSYNC);
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

//    @Override
//    public void transform(double[] matrix) {
//
//    }

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

    public void beginPolygon(PolygonType type) {
        if (polygons.isUnused())
            finishPolygon();

        polygons.setType(type);
    }

    @Override
    public void addVertex(double x, double y, double z) {
        polygons.addVertex(new Vector(x, y, z));
    }

    @Override
    public void addVertex(double x, double y) {
        addVertex(x, y, 0);
    }

    public void finishPolygon() {
        polygons.reset();
    }

    public void fillTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        fillTriangle(x1, y1, 0, x2, y2, 0, x3, y3, 0);
    }

    public void fillTriangle(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
        metal.draw(ctx, MetalConstants.PRIMITIVE_TRIANGLE, new float[] {
                (float) x1,      (float) y1,           (float) z1, 1.0F,        1.0F, 1.0F, 1.0F, 1.0F,
                (float) x2,      (float) y2,           (float) z2, 1.0F,        1.0F, 1.0F, 1.0F, 1.0F,
                (float) x3,      (float) y3,           (float) z3, 1.0F,        1.0F, 1.0F, 1.0F, 1.0F,
        });
    }

    public void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        drawTriangle(x1, y1, 0, x2, y2, 0, x3, y3, 0);
    }

    public void drawTriangle(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
        metal.draw(ctx, MetalConstants.PRIMITIVE_LINE_STRIP, new float[] {
                (float) x1,      (float) y1,           (float) z1, 1.0F,        1.0F, 1.0F, 1.0F, 1.0F,
                (float) x2,      (float) y2,           (float) z2, 1.0F,        1.0F, 1.0F, 1.0F, 1.0F,
                (float) x3,      (float) y3,           (float) z3, 1.0F,        1.0F, 1.0F, 1.0F, 1.0F,
        });
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

    @Override
    public void quit() {
        metal.quit();
        System.exit(0);
    }

    @Override
    public boolean canQuit() {
        return true;
    }
}
