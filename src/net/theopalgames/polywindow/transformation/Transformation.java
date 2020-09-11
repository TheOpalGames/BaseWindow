package net.theopalgames.polywindow.transformation;

import net.theopalgames.polywindow.BaseWindow;

public abstract class Transformation
{
    public BaseWindow window;

    public Transformation(BaseWindow window)
    {
        this.window = window;
    }

    public abstract void apply();
}
