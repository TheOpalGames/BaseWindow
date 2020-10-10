package net.theopalgames.polywindow.transformation;

import net.theopalgames.polywindow.Window;

import java.util.function.Consumer;

/**
 * A rotation not necessarily around (0,0,0).
 */
public class RotationAboutPoint extends Rotation
{
    /**
     * The x coordinate of the center of rotation
     */
    public double x;
    
    /**
     * The y coordinate of the center of rotation
     */
    public double y;
    
    /**
     * The z coordinate of the center of rotation.
     */
    public double z;
    
    private final Window window;
    
    /**
     * Creates a rotation around a specific point.
     *
     * @param window The window to rotate the contents of.
     * @param yaw The horizontal angle difference of the 3D rotation.
     * @param pitch The vertical angle difference of the 3D rotation.
     * @param roll The angle to rotate the screen plane by.
     * @param x The x coordinate of the center of rotation.
     * @param y The y coordinate of the center of rotation.
     * @param z The z coordinate of the center of rotation.
     */
    public RotationAboutPoint(Window window, double yaw, double pitch, double roll, double x, double y, double z)
    {
        super(yaw, pitch, roll);

        this.window = window;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void addMatrices(Consumer<double[]> registry)
    {
//        window.yaw += yaw;
//        window.pitch += pitch;
//        window.roll += roll;
//
//        window.angled = !(window.yaw == 0 && window.pitch == 0 && window.roll == 0);
//        transform(window, yaw, pitch, roll, x, y, z);
        // TODO

        registry.accept(new double[]{1, 0, 0, 0,  0, 1, 0, 0,  0, 0, 1, 0,  x * window.absoluteWidth, y * window.absoluteHeight, z * window.absoluteDepth, 1});

        super.addMatrices(registry);

        registry.accept(new double[]{1, 0, 0, 0,  0, 1, 0, 0,  0, 0, 1, 0,  -x * window.absoluteWidth, -y * window.absoluteHeight, -z * window.absoluteDepth, 1});
    }

//    public static void transform(BaseWindow window, double yaw, double pitch, double roll, double x, double y, double z)
//    {
//        window.transform(new double[]{1, 0, 0, 0,  0, 1, 0, 0,  0, 0, 1, 0,  x * window.absoluteWidth, y * window.absoluteHeight, z * window.absoluteDepth, 1});
//
//        window.transform(new double[]{Math.cos(roll), -Math.sin(roll), 0, 0,  Math.sin(roll), Math.cos(roll), 0, 0,  0, 0, 1, 0,  0, 0, 0, 1});
//        window.transform(new double[]{1, 0, 0, 0,  0, Math.cos(pitch), -Math.sin(pitch), 0,  0, Math.sin(pitch), Math.cos(pitch), 0,  0, 0, 0, 1});
//        window.transform(new double[]{Math.cos(yaw), 0, -Math.sin(yaw), 0,  0, 1, 0, 0,  Math.sin(yaw), 0, Math.cos(yaw), 0,  0, 0, 0, 1});
//
//        window.transform(new double[]{1, 0, 0, 0,  0, 1, 0, 0,  0, 0, 1, 0,  -x * window.absoluteWidth, -y * window.absoluteHeight, -z * window.absoluteDepth, 1});
//    }
}
