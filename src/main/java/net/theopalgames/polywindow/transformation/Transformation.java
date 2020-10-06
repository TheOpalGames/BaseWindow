package net.theopalgames.polywindow.transformation;

import net.theopalgames.polywindow.Window;

import java.util.function.Consumer;

public abstract class Transformation
{
    public Window window;

    public Transformation(Window window)
    {
        this.window = window;
    }

    public abstract void addMatrices(Consumer<double[]> registry);
}
