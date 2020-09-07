package net.theopalgames.basewindow.swing;

import net.theopalgames.basewindow.swing.input.InputMouse;
import net.theopalgames.basewindow.swing.input.InputScroll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingPanel extends JPanel
{
    public Timer timer;
    public SwingWindow window;

    public SwingPanel(SwingWindow w)
    {
        this.window = w;

        this.addMouseWheelListener(new InputScroll());

        InputMouse mouse = new InputMouse(w);
        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);

        timer = new Timer(0, event ->
        {
            try {
                w.startTiming();

                w.absoluteWidth = getWidth();
                w.absoluteHeight = getHeight();

                w.updater.update();

                repaint();

                w.stopTiming();
            } catch (Exception exception) {
//                    Game.exitToCrash(exception);
                window.getGame().onError(exception);
            }
        });
    }

    public void startTimer()
    {
        timer.start();
        //new SoundThread().execute();
    }

    @Override
    public void paint(Graphics g)
    {
        window.graphics = g;
        window.drawer.draw();
    }
}