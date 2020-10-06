package net.theopalgames.polywindow;

public abstract class FontRenderer
{
    public Window home;

    public FontRenderer(Window h)
    {
        this.home = h;
    }

    public abstract void drawString(double x, double y, double z, double sX, double sY, String s);

    public abstract void drawString(double x, double y, double sX, double sY, String s);

    public abstract double getStringSizeX(double sX, String s);

    public abstract double getStringSizeY(double sY, String s);
}
