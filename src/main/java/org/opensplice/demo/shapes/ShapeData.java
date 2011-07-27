package org.opensplice.demo.shapes;

import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

import org.opensplice.demo.ShapeType;

public class ShapeData extends Observable {

    private Map<String, ShapeType> square = new ConcurrentHashMap<String, ShapeType>();
    private Map<String, ShapeType> circle = new ConcurrentHashMap<String, ShapeType>();
    private Map<String, ShapeType> triangle = new ConcurrentHashMap<String, ShapeType>();
    private static ShapeData instance = null;

    public static ShapeData getInstance() {
        if (instance == null) {
            instance = new ShapeData();
        }
        return instance;
    }

    public void putShape(String shape, String key, ShapeType value) {
        if ("Circle".equals(shape)) {
            circle.put(key, value);
        } else if ("Triangle".equals(shape)) {
            triangle.put(key, value);
        } else if ("Square".equals(shape)) {
            square.put(key, value);
        }
        notifyChange();
    }

    public void removeShape(String shape, String key) {
        if ("Circle".equals(shape)) {
            circle.remove(key);
        } else if ("Triangle".equals(shape)) {
            triangle.remove(key);
        } else if ("Square".equals(shape)) {
            square.remove(key);
        }
        notifyChange();
    }

    public Iterator<ShapeType> getRectangle() {
        return square.values().iterator();
    }

    public Iterator<ShapeType> getCircle() {
        return circle.values().iterator();
    }

    public Iterator<ShapeType> getTriangle() {
        return triangle.values().iterator();
    }

    private void notifyChange() {
        setChanged();
        notifyObservers();
    }
}
