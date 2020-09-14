package net.theopalgames.polywindow;

public class InputVector extends InputPoint {
    public double startX;
    public double startY;

    public InputVector(double x, double y) {
        super(x, y);
        this.startX = x;
        this.startY = y;
    }
}
