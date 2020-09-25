package net.theopalgames.polywindow.transformation;

import net.theopalgames.polywindow.BaseWindow;

import java.util.function.Consumer;

public abstract class Transformation
{
    public BaseWindow window;

    public Transformation(BaseWindow window)
    {
        this.window = window;
    }

    public abstract void addMatrices(Consumer<double[]> registry);
}
