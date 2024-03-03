package me.kyren223.kapi.math;

public class Quaternion {
    private double w, x, y, z;
    
    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Quaternion multiply(Quaternion q) {
        double newW = this.w * q.w - this.x * q.x - this.y * q.y - this.z * q.z;
        double newX = this.w * q.x + this.x * q.w + this.y * q.z - this.z * q.y;
        double newY = this.w * q.y - this.x * q.z + this.y * q.w + this.z * q.x;
        double newZ = this.w * q.z + this.x * q.y - this.y * q.x + this.z * q.w;
        
        return new Quaternion(newW, newX, newY, newZ);
    }
    
    // Convert Euler angles (in degrees) to Quaternion
    public static Quaternion fromEulerAngles(double pitch, double yaw, double roll) {
        // Convert degrees to radians
        double pitchRad = Math.toRadians(pitch);
        double yawRad = Math.toRadians(yaw);
        double rollRad = Math.toRadians(roll);
        
        double cy = Math.cos(yawRad * 0.5);
        double sy = Math.sin(yawRad * 0.5);
        double cp = Math.cos(pitchRad * 0.5);
        double sp = Math.sin(pitchRad * 0.5);
        double cr = Math.cos(rollRad * 0.5);
        double sr = Math.sin(rollRad * 0.5);
        
        double w = cy * cp * cr + sy * sp * sr;
        double x = cy * cp * sr - sy * sp * cr;
        double y = sy * cp * sr + cy * sp * cr;
        double z = sy * cp * cr - cy * sp * sr;
        
        return new Quaternion(w, x, y, z);
    }
    
    public double[] toEulerAngles() {
        double[] angles = new double[3];
        
        // roll (x-axis rotation)
        double sinrCosp = 2 * (w * x + y * z);
        double cosr_cosp = 1 - 2 * (x * x + y * y);
        angles[0] = Math.atan2(sinrCosp, cosr_cosp);
        
        // pitch (y-axis rotation)
        double sinp = 2 * (w * y - z * x);
        if (Math.abs(sinp) >= 1)
            angles[1] = Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
        else
            angles[1] = Math.asin(sinp);
        
        // yaw (z-axis rotation)
        double siny_cosp = 2 * (w * z + x * y);
        double cosy_cosp = 1 - 2 * (y * y + z * z);
        angles[2] = Math.atan2(siny_cosp, cosy_cosp);
        
        return angles;
    }
}
