package net.theopalgames.polywindow.transformation;

import net.theopalgames.polywindow.Window;

import java.util.function.Consumer;

public class Translation extends Transformation
{
    public double x;
    public double y;
    public double z;

    public Translation(Window window, double x, double y, double z)
    {
        super(window);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void addMatrices(Consumer<double[]> registry)
    {
        // TODO
//        window.xOffset += x;
//        window.yOffset += y;
//        window.zOffset += z;
//        transform(window, x, y, z);
        registry.accept(new double[] {1, 0, 0, 0,  0, 1, 0, 0,  0, 0, 1, 0,  x * window.absoluteWidth, y * window.absoluteHeight, z * window.absoluteDepth, 1});
    }

//    public static void transform(BaseWindow window, double x, double y, double z)
//    {
//        window.transform(new double[]{1, 0, 0, 0,  0, 1, 0, 0,  0, 0, 1, 0,  x * window.absoluteWidth, y * window.absoluteHeight, z * window.absoluteDepth, 1});
//    }
}
