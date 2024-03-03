package me.kyren223.kapi.particles;

import me.kyren223.kapi.math.Quaternion;
import org.bukkit.util.Vector;

public class Transform {
    private double x, y, z;
    private double pitch, yaw, roll;
    private double scaleX, scaleY, scaleZ; // TODO: Implement scaling
    
    public Transform(double x, double y, double z, double pitch, double yaw, double roll, double scaleX, double scaleY, double scaleZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }
    
    public Transform(Transform transform) {
        this.x = transform.x;
        this.y = transform.y;
        this.z = transform.z;
        this.pitch = transform.pitch;
        this.yaw = transform.yaw;
        this.roll = transform.roll;
        this.scaleX = transform.scaleX;
        this.scaleY = transform.scaleY;
        this.scaleZ = transform.scaleZ;
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
    
    public Vector getTranslation() {
        return new Vector(x, y, z);
    }
    
    public void setTranslation(Vector vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }
    
    public double getPitch() {
        return pitch;
    }
    
    public void setPitch(double pitch) {
        this.pitch = pitch;
    }
    
    public double getYaw() {
        return yaw;
    }
    
    public void setYaw(double yaw) {
        this.yaw = yaw;
    }
    
    public double getRoll() {
        return roll;
    }
    
    public void setRoll(double roll) {
        this.roll = roll;
    }
    
    public Vector getRotation() {
        return new Vector(pitch, yaw, roll);
    }
    
    public void setRotation(Vector vector) {
        this.pitch = vector.getX();
        this.yaw = vector.getY();
        this.roll = vector.getZ();
    }
    
    public double getScaleX() {
        return scaleX;
    }
    
    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }
    
    public double getScaleY() {
        return scaleY;
    }
    
    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }
    
    public double getScaleZ() {
        return scaleZ;
    }
    
    public void setScaleZ(double scaleZ) {
        this.scaleZ = scaleZ;
    }
    
    public Vector getScale() {
        return new Vector(scaleX, scaleY, scaleZ);
    }
    
    public void setScale(Vector vector) {
        this.scaleX = vector.getX();
        this.scaleY = vector.getY();
        this.scaleZ = vector.getZ();
    }
    
    public static Transform fromTranslation(Vector vector) {
        return new Transform(
                vector.getX(), vector.getY(), vector.getZ(),
                0, 0, 0,
                0, 0, 0
        );
    }
    
    public static Transform fromTranslation(double x, double y, double z) {
        return new Transform(
                x, y, z,
                0, 0, 0,
                0, 0, 0
        );
    }
    
    public static Transform fromRotation(Vector vector) {
        return new Transform(
                0, 0, 0,
                vector.getX(), vector.getY(), vector.getZ(),
                0, 0, 0
        );
    }
    
    public static Transform fromRotation(double pitch, double yaw, double roll) {
        return new Transform(
                0, 0, 0,
                pitch, yaw, roll,
                0, 0, 0
        );
    }
    
    public static Transform fromScale(Vector vector) {
        return new Transform(
                0, 0, 0,
                0, 0, 0,
                vector.getX(), vector.getY(), vector.getZ()
        );
    }
    
    public static Transform fromScale(double x, double y, double z) {
        return new Transform(
                0, 0, 0,
                0, 0, 0,
                x, y, z
        );
    }
    
    public Transform combine(Transform other) {
        double newX = this.x + other.x;
        double newY = this.y + other.y;
        double newZ = this.z + other.z;
        
        Quaternion thisRotation = Quaternion.fromEulerAngles(this.pitch, this.yaw, this.roll);
        Quaternion otherRotation = Quaternion.fromEulerAngles(other.pitch, other.yaw, other.roll);
        Quaternion newRotation = thisRotation.multiply(otherRotation);
        
        // Convert quaternion back to Euler angles in degrees
        double[] eulerAngles = newRotation.toEulerAngles();
        double newPitch = Math.toDegrees(eulerAngles[0]);
        double newYaw = Math.toDegrees(eulerAngles[1]);
        double newRoll = Math.toDegrees(eulerAngles[2]);
        
        
        double newScaleX = this.scaleX * other.scaleX;
        double newScaleY = this.scaleY * other.scaleY;
        double newScaleZ = this.scaleZ * other.scaleZ;
        
        return new Transform(newX, newY, newZ, eulerAngles[0], eulerAngles[1], eulerAngles[2], newScaleX, newScaleY, newScaleZ);
    }
    
    public double[] applyToPoint(Point point) {
        // Convert rotation to radians
        double yawRad = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);
        double rollRad = Math.toRadians(roll);
        
        // Calculate rotation matrix
        double[][] rotationMatrix = new double[][] {
                {Math.cos(yawRad) * Math.cos(pitchRad), Math.cos(yawRad) * Math.sin(pitchRad) * Math.sin(rollRad) - Math.sin(yawRad) * Math.cos(rollRad), Math.cos(yawRad) * Math.sin(pitchRad) * Math.cos(rollRad) + Math.sin(yawRad) * Math.sin(rollRad)},
                {Math.sin(yawRad) * Math.cos(pitchRad), Math.sin(yawRad) * Math.sin(pitchRad) * Math.sin(rollRad) + Math.cos(yawRad) * Math.cos(rollRad), Math.sin(yawRad) * Math.sin(pitchRad) * Math.cos(rollRad) - Math.cos(yawRad) * Math.sin(rollRad)},
                {-Math.sin(pitchRad), Math.cos(pitchRad) * Math.sin(rollRad), Math.cos(pitchRad) * Math.cos(rollRad)}
        };
        
        // Apply rotation
        double rotatedX = point.getX() * rotationMatrix[0][0] + point.getY() * rotationMatrix[0][1] + point.getZ() * rotationMatrix[0][2];
        double rotatedY = point.getX() * rotationMatrix[1][0] + point.getY() * rotationMatrix[1][1] + point.getZ() * rotationMatrix[1][2];
        double rotatedZ = point.getX() * rotationMatrix[2][0] + point.getY() * rotationMatrix[2][1] + point.getZ() * rotationMatrix[2][2];
        
        // Apply translation
        double translatedX = rotatedX + x;
        double translatedY = rotatedY + y;
        double translatedZ = rotatedZ + z;
        
        return new double[] {translatedX, translatedY, translatedZ};
    }
}
