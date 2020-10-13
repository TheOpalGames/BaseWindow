package net.theopalgames.polywindow.transformation;

import net.theopalgames.polywindow.Window;

import java.util.function.Consumer;

/**
 * A transformation that shifts all points equally.
 *
 * @author aehmttw and hallowizer
 */
public class Translation extends Transformation
{
    /**
     * The x coordinate to add to every point.
     */
    public double x;
    
    /**
     * The y coordinate to add to every point.
     */
    public double y;
    
    /**
     * The z coordinate to add to every point.
     */
    public double z;
    
    private final Window window;
    
    /**
     * Creates a new translation.
     *
     * @param window The window to translate.
     * @param x The shift in the x coordinate.
     * @param y The shift in the y coordinate.
     * @param z The shift in the z coordinate.
     */
    public Translation(Window window, double x, double y, double z)
    {
        this.window = window;
        
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
