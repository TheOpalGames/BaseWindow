package net.theopalgames.polywindow.transformation;

import net.theopalgames.polywindow.Window;

import java.util.function.Consumer;

/**
 * A rotation transformation, creating angle POP', where (0,0,0) is the center.
 */
public class Rotation extends Transformation
{
    /**
     * The horizontal angle difference of the 3D rotation.
     */
    public double yaw;
    
    /**
     * The vertical angle difference of the 3D rotation.
     */
    public double pitch;
    
    /**
     * Angle to rotate the actual screen by.
     */
    public double roll;
    
    /**
     * Creates a rotation.
     *
     * @param yaw The horizontal angle difference of the 3D rotation.
     * @param pitch The vertical angle difference of the 3D rotation.
     * @param roll The angle to rotate the screen plane by.
     */
    public Rotation(double yaw, double pitch, double roll)
    {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }
    
    @Override
    public void addMatrices(Consumer<double[]> registry)
    {
//        window.yaw += yaw;
//        window.pitch += pitch;
//        window.roll += roll;
//
//        window.angled = !(window.yaw == 0 && window.pitch == 0 && window.roll == 0);
//        transform(window, yaw, pitch, roll);
        registry.accept(new double[]{Math.cos(roll), -Math.sin(roll), 0, 0,  Math.sin(roll), Math.cos(roll), 0, 0,  0, 0, 1, 0,  0, 0, 0, 1});
        registry.accept(new double[]{1, 0, 0, 0,  0, Math.cos(pitch), -Math.sin(pitch), 0,  0, Math.sin(pitch), Math.cos(pitch), 0,  0, 0, 0, 1});
        registry.accept(new double[]{Math.cos(yaw), 0, -Math.sin(yaw), 0,  0, 1, 0, 0,  Math.sin(yaw), 0, Math.cos(yaw), 0,  0, 0, 0, 1});

    }

//    public static void transform(BaseWindow window, double yaw, double pitch, double roll)
//    {
//        window.transform(new double[]{Math.cos(roll), -Math.sin(roll), 0, 0,  Math.sin(roll), Math.cos(roll), 0, 0,  0, 0, 1, 0,  0, 0, 0, 1});
//        window.transform(new double[]{1, 0, 0, 0,  0, Math.cos(pitch), -Math.sin(pitch), 0,  0, Math.sin(pitch), Math.cos(pitch), 0,  0, 0, 0, 1});
//        window.transform(new double[]{Math.cos(yaw), 0, -Math.sin(yaw), 0,  0, 1, 0, 0,  Math.sin(yaw), 0, Math.cos(yaw), 0,  0, 0, 0, 1});
//    }
}
