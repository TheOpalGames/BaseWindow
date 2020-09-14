package net.theopalgames.polywindow.transformation;

import net.theopalgames.polywindow.BaseWindow;

public class RotationAboutPoint extends Transformation
{
    public double yaw;
    public double pitch;
    public double roll;

    public double x;
    public double y;
    public double z;

    public RotationAboutPoint(BaseWindow window, double yaw, double pitch, double roll, double x, double y, double z)
    {
        super(window);

        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void apply()
    {
        window.yaw += yaw;
        window.pitch += pitch;
        window.roll += roll;

        window.angled = !(window.yaw == 0 && window.pitch == 0 && window.roll == 0);
        transform(window, yaw, pitch, roll, x, y, z);
    }

    public static void transform(BaseWindow window, double yaw, double pitch, double roll, double x, double y, double z)
    {
        window.transform(new double[]{1, 0, 0, 0,  0, 1, 0, 0,  0, 0, 1, 0,  x * window.absoluteWidth, y * window.absoluteHeight, z * window.absoluteDepth, 1});

        window.transform(new double[]{Math.cos(roll), -Math.sin(roll), 0, 0,  Math.sin(roll), Math.cos(roll), 0, 0,  0, 0, 1, 0,  0, 0, 0, 1});
        window.transform(new double[]{1, 0, 0, 0,  0, Math.cos(pitch), -Math.sin(pitch), 0,  0, Math.sin(pitch), Math.cos(pitch), 0,  0, 0, 0, 1});
        window.transform(new double[]{Math.cos(yaw), 0, -Math.sin(yaw), 0,  0, 1, 0, 0,  Math.sin(yaw), 0, Math.cos(yaw), 0,  0, 0, 0, 1});

        window.transform(new double[]{1, 0, 0, 0,  0, 1, 0, 0,  0, 0, 1, 0,  -x * window.absoluteWidth, -y * window.absoluteHeight, -z * window.absoluteDepth, 1});
    }
}
