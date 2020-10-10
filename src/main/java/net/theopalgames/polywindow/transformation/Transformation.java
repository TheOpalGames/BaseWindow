package net.theopalgames.polywindow.transformation;

import java.util.function.Consumer;

/**
 * A linear transformation to be applied to all points on the window.
 */
public abstract class Transformation
{
    /**
     * Feeds all matrices required to perform this transformation to the registry provided.
     *
     * @param registry The consumer to receive the matrices.
     */
    public abstract void addMatrices(Consumer<double[]> registry);
}
