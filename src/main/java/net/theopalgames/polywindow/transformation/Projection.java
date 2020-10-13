package net.theopalgames.polywindow.transformation;

import java.util.function.Consumer;

/**
 * A transformation to create perspective drawing in 3D instead of simple layers.
 *
 * @author hallowizer
 */
public class Projection extends Transformation {
    /**
     * The distance from the apex of the pyramid to the upper base of the frustum.
     */
    public int cameraDepth;
    
    /**
     * The z component of the window size.
     */
    public int maxDepth;
    
    /**
     * The x component of the window size.
     */
    public int width;
    
    /**
     * The y component of the window size.
     */
    public int height;
    
    @Override
    public void addMatrices(Consumer<double[]> registry) {
        registry.accept(new double[] { // transposed
                2*cameraDepth/width, 0, 1, 0,
                0, 2*cameraDepth/height, 1, 0,
                0, 0, -(maxDepth+2*cameraDepth)/maxDepth, -1,
                0, 0, -2*cameraDepth*(maxDepth+cameraDepth)/maxDepth, 0
        });
    }
}
