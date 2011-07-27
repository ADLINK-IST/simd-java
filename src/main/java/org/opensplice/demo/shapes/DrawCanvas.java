package org.opensplice.demo.shapes;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.opensplice.demo.ShapeType;

public class DrawCanvas extends Canvas implements Observer {

    private RectanglePainter rectanglePainter = new RectanglePainter();
    private TrianglePainter trianglePainter = new TrianglePainter();
    private CirclePainter circlePainter = new CirclePainter();
    private Map<String, Color> colors = new HashMap<String, Color>();

    private abstract class FigurePainter {
        public void paint(Iterator<ShapeType> it, Graphics2D g) {
            while (it.hasNext()) {
                ShapeType shape = it.next();
                Color color = colors.get(shape.color.toLowerCase());
                if (color == null) {
                    color = Color.BLACK;
                }
                paintFigure(g, shape.x, shape.y, shape.shapesize, color);
            }
        }

        public abstract void paintFigure(Graphics2D g, int x, int y, int size,
                Color color);
    }

    private class RectanglePainter extends FigurePainter {
        public void paintFigure(Graphics2D g, int x, int y, int size,
                Color color) {
            g.setColor(color);
            g.drawRect(x, y, size, size);
        }
    }

    private class TrianglePainter extends FigurePainter {
        private int[] xp = new int[3];
        private int[] yp = new int[3];

        public void paintFigure(Graphics2D g, int x, int y, int size,
                Color color) {
            g.setColor(color);
            xp[0] = x;
            xp[1] = x + size / 2;
            xp[2] = x + size;
            yp[0] = y;
            yp[1] = y + size;
            yp[2] = y;
            g.drawPolygon(xp, yp, 3);
        }
    }

    private class CirclePainter extends FigurePainter {
        public void paintFigure(Graphics2D g, int x, int y, int size,
                Color color) {
            g.setColor(color);
            BasicStroke stroke = new BasicStroke(size / 2);
            g.setStroke(stroke);
            g.drawOval(x, y, size, size);
        }
    }

    public DrawCanvas() {
        colors.put("red", Color.RED);
        colors.put("green", Color.GREEN);
        colors.put("blue", Color.BLUE);
        colors.put("yellow", Color.YELLOW);
        colors.put("orange", Color.ORANGE);
        colors.put("gray", Color.GRAY);
        colors.put("cyan", Color.CYAN);
        setBackground(Color.WHITE);
        ShapeData.getInstance().addObserver(this);
    }

    private Image mImage;

    private void checkOffscreenImage() {
        Dimension d = getSize();
        if (mImage == null || mImage.getWidth(null) != d.width
                || mImage.getHeight(null) != d.height) {
            mImage = createImage(d.width, d.height);
        }
    }

    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        System.out.println("Painting...");
        checkOffscreenImage();
        Dimension d = getSize();
        Graphics2D g2 = (Graphics2D) mImage.getGraphics();
        g2.setColor(getBackground());
        g2.fillRect(0, 0, d.width, d.height);
        Iterator<ShapeType> it = ShapeData.getInstance().getRectangle();
        rectanglePainter.paint(it, g2);
        it = ShapeData.getInstance().getTriangle();
        trianglePainter.paint(it, g2);
        it = ShapeData.getInstance().getCircle();
        circlePainter.paint(it, g2);
        g.drawImage(mImage, 0, 0, null);
    }

    public void update(Observable o, Object arg) {
        repaint();
    }
}
