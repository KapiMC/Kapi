package me.kyren223.kapi.engine;

import me.kyren223.kapi.engine.renderable.Renderable;
import org.bukkit.util.Vector;

public class Point {
    private double x;
    private double y;
    private double z;
    private Renderable renderable;
    
    public Point(double x, double y, double z, Renderable renderable) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.renderable = renderable;
    }
    
    public Point(Vector vector, Renderable renderable) {
        this(vector.getX(), vector.getY(), vector.getZ(), renderable);
    }
    
    /**
     * Copy constructor (makes a deep copy)
     * @param point The point to copy
     */
    public Point(Point point) {
        this(point.x, point.y, point.z, point.getRenderable().clone());
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getZ() {
        return z;
    }
    
    public void setZ(double z) {
        this.z = z;
    }
    
    public Vector getVector() {
        return new Vector(x, y, z);
    }
    
    public void setVector(Vector vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }
    
    public Renderable getRenderable() {
        return renderable;
    }
    
    public void setRenderable(Renderable renderable) {
        this.renderable = renderable;
    }
}




