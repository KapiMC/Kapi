package me.kyren223.kapi.math;

import org.bukkit.util.Vector;

/**
 * @deprecated Use {@link org.joml.Quaternionf} instead
 */
@Deprecated
public class Quaternion {
    private double w;
    private double x;
    private double y;
    private double z;
    
    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Quaternion(Quaternion other) {
        this(other.w, other.x, other.y, other.z);
    }
    
    public Quaternion() {
        this(1, 0, 0, 0);
    }
    
    public double getW() {
        return w;
    }
    
    public void setW(double w) {
        this.w = w;
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
    
    /**
     * Calculates the magnitude of the quaternion
     * @return The magnitude of the quaternion
     */
    private double magnitude() {
        return Math.sqrt(x * x + y * y + z * z + w * w);
    }
    
    /**
     * Normalizes the quaternion to become a unit quaternion
     * The magnitude of the quaternion will be 1
     * <p></p>
     * Calculates this by dividing each component by the magnitude
     * @return The same quaternion
     */
    public Quaternion normalize() {
        double magnitude = magnitude();
        x /= magnitude;
        y /= magnitude;
        z /= magnitude;
        w /= magnitude;
        return this;
    }
    
    /**
     * Calculates the conjugate of the quaternion
     * By negating the x, y, and z components
     * @return The same quaternion
     */
    public Quaternion conjugate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }
    
    /**
     * Multiplies the quaternion by another quaternion
     * <p>
     * Calculates this by doing the following calculations:
     * <pre><code>
     * double w = this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z
     * double x = this.w * other.x + this.x * other.w + this.y * other.z - this.z * other.y
     * double y = this.w * other.y - this.x * other.z + this.y * other.w + this.z * other.x
     * double z = this.w * other.z + this.x * other.y - this.y * other.x + this.z * other.w
     * </code></pre>
     * @param other The other quaternion
     * @return The same quaternion
     */
    public Quaternion multiply(Quaternion other) {
        double w = this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z;
        double x = this.w * other.x + this.x * other.w + this.y * other.z - this.z * other.y;
        double y = this.w * other.y - this.x * other.z + this.y * other.w + this.z * other.x;
        double z = this.w * other.z + this.x * other.y - this.y * other.x + this.z * other.w;
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    /**
     * Rotates the quaternion by a given angle around a given axis
     * <p></p>
     * Calculates this by converting the axis and angle to a quaternion
     * Then multiplying this quaternion by the new quaternion
     * Based on <a href="https://danceswithcode.net/engineeringnotes/quaternions/quaternions.html">...</a>
     * @param axis The axis to rotate around
     * @param angle The angle to rotate by, in degrees
     * @return The same quaternion
     */
    public Quaternion rotate(Vector axis, double angle) {
        double angleInRadians = Math.toRadians(angle);
        // Based on https://danceswithcode.net/engineeringnotes/quaternions/quaternions.html
        double sinHalfAngleConstant = Math.sin(angleInRadians / 2);
        
        double q0 = Math.cos(angleInRadians / 2);
        double q1 = axis.getX() * sinHalfAngleConstant;
        double q2 = axis.getY() * sinHalfAngleConstant;
        double q3 = axis.getZ() * sinHalfAngleConstant;
        
        return multiply(new Quaternion(q0, q1, q2, q3));
    }
}
