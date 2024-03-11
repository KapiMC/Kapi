package me.kyren223.kapi.math;

import org.bukkit.util.Vector;

public class Transform {
    private double[][] matrix;
    
    public Transform(double[][] matrix) {
        this.matrix = matrix.clone();
    }
    
    public Transform(Transform transform) {
        this(transform.matrix);
    }
    
    /**
     * Creates a new transform with the given translation,
     * rotation and scale are set to their defaults.
     * See {@link #getIdentity()} for the default values.
     *
     * @param vector The position of the transform
     * @return A new transform
     */
    public static Transform fromTranslation(Vector vector) {
        return new Transform(new double[][]{
                {1, 0, 0, vector.getX()},
                {0, 1, 0, vector.getY()},
                {0, 0, 1, vector.getZ()},
                {0, 0, 0, 1}
        });
    }
    
    /**
     * Creates a new transform with the given translation,
     * rotation and scale are set to their defaults.
     * See {@link #getIdentity()} for the default values.
     *
     * @param x The x position of the transform
     * @param y The y position of the transform
     * @param z The z position of the transform
     * @return A new transform
     */
    public static Transform fromTranslation(double x, double y, double z) {
        return new Transform(new double[][]{
                {1, 0, 0, x},
                {0, 1, 0, y},
                {0, 0, 1, z},
                {0, 0, 0, 1}
        });
    }
    
    public Vector getTranslation() {
        return new Vector(matrix[0][3], matrix[1][3], matrix[2][3]);
    }
    
    public void setTranslation(Vector vector) {
        matrix[0][3] = vector.getX();
        matrix[1][3] = vector.getY();
        matrix[2][3] = vector.getZ();
    }
    
    public void setTranslation(double x, double y, double z) {
        matrix[0][3] = x;
        matrix[1][3] = y;
        matrix[2][3] = z;
    }
    
    /**
     * Transforms the given vector from a "local" (relative to the origin)
     * to a "global" (relative to the world or parent) coordinate system.
     * Returns the "absolute" or "global" position of the given vector.
     * <p>
     * Calculates this by multiplying the given vector by this transform's matrix.
     *
     * @param vector A vector
     * @return A new vector
     */
    public Vector transformPoint(Vector vector) {
        double x = matrix[0][0] * vector.getX() + matrix[0][1] * vector.getY() + matrix[0][2] * vector.getZ() + matrix[0][3];
        double y = matrix[1][0] * vector.getX() + matrix[1][1] * vector.getY() + matrix[1][2] * vector.getZ() + matrix[1][3];
        double z = matrix[2][0] * vector.getX() + matrix[2][1] * vector.getY() + matrix[2][2] * vector.getZ() + matrix[2][3];
        return new Vector(x, y, z);
    }
    
    /**
     * Transforms the given vector from a "global" (relative to the world or parent)
     * to a "local" (relative to the origin) coordinate system.
     * Returns the "relative" or "local" position of the given vector.
     * <p>
     * Calculates this by subtracting the translation part of the matrix from the given vector,
     * and then taking the dot product of the result with the right, up, and forward vectors.
     *
     * @param vector A vector
     * @return A new vector
     */
    public Vector inversePoint(Vector vector) {
        double x = vector.getX() - matrix[0][3];
        double y = vector.getY() - matrix[1][3];
        double z = vector.getZ() - matrix[2][3];
        Vector relativePoint = new Vector(x, y, z);
        x = relativePoint.dot(getRight());
        y = relativePoint.dot(getUp());
        z = relativePoint.dot(getForward());
        return new Vector(x, y, z);
    }
    
    /**
     * Creates the identity transform.
     * <p></p>
     * Represented as the following matrix:
     * <pre><code>
     *     1 0 0 0
     *     0 1 0 0
     *     0 0 1 0
     *     0 0 0 1
     * </code></pre>
     *
     * @return A new transform
     */
    public Vector getIdentity() {
        return new Vector(matrix[0][0], matrix[1][1], matrix[2][2]);
    }
    
    /**
     * Creates a new vector representing the right direction of the transform.
     *
     * @return A new vector
     */
    public Vector getRight() {
        return new Vector(matrix[0][0], matrix[1][0], matrix[2][0]).normalize();
    }
    
    /**
     * Creates a new vector representing the up direction of the transform.
     *
     * @return A new vector
     */
    public Vector getUp() {
        return new Vector(matrix[0][1], matrix[1][1], matrix[2][1]).normalize();
    }
    
    /**
     * Creates a new vector representing the forward direction of the transform.
     *
     * @return A new vector
     */
    public Vector getForward() {
        return new Vector(matrix[0][2], matrix[1][2], matrix[2][2]).normalize();
    }
    
    /**
     * Multiplies the transform by another transform.
     * Uses matrix multiplication to combine the two transforms.
     * This operation is computationally expensive and should be cached if possible.
     * <p>
     * This calculation is done like this:
     * <pre><code>
     * double[][] result = new double[4][4];
     * for (int i = 0; i < 4; i++) {
     *     for (int j = 0; j < 4; j++) {
     *         result[i][j] = 0;
     *         for (int k = 0; k < 4; k++) {
     *             result[i][j] += matrix[i][k] * other.matrix[k][j];
     *         }
     *     }
     * }
     * </code></pre>
     *
     * @param other The other transform
     * @return The same transform
     */
    public Transform multiply(Transform other) {
        // Do matrix multiplication
        double[][] result = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                    result[i][j] += matrix[i][k] * other.matrix[k][j];
                }
            }
        }
        this.matrix = result;
        return this;
    }
    
    public Transform clone() {
        try {
            Transform clone = (Transform) super.clone();
            clone.matrix = matrix.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported");
        }
    }
    
    /**
     * Creates a new quaternion representing the rotation of the transform.
     * <p>
     * The quaternion is always normalized.
     * <p></p>
     * This is done by the following code:
     * <pre><code>
     *     double w = Math.sqrt(Math.max(0, 1 + matrix[0][0] + matrix[1][1] + matrix[2][2])) / 2;
     *     double x = Math.sqrt(Math.max(0, 1 + matrix[0][0] - matrix[1][1] - matrix[2][2])) / 2;
     *     double y = Math.sqrt(Math.max(0, 1 - matrix[0][0] + matrix[1][1] - matrix[2][2])) / 2;
     *     double z = Math.sqrt(Math.max(0, 1 - matrix[0][0] - matrix[1][1] + matrix[2][2])) / 2;
     *     x *= Math.signum(x * (matrix[2][1] - matrix[1][2]));
     *     y *= Math.signum(y * (matrix[0][2] - matrix[2][0]));
     *     z *= Math.signum(z * (matrix[1][0] - matrix[0][1]));
     * </code></pre>
     *
     * @return A new quaternion
     */
    public Quaternion getRotation() {
        double w = Math.sqrt(Math.max(0, 1 + matrix[0][0] + matrix[1][1] + matrix[2][2])) / 2;
        double x = Math.sqrt(Math.max(0, 1 + matrix[0][0] - matrix[1][1] - matrix[2][2])) / 2;
        double y = Math.sqrt(Math.max(0, 1 - matrix[0][0] + matrix[1][1] - matrix[2][2])) / 2;
        double z = Math.sqrt(Math.max(0, 1 - matrix[0][0] - matrix[1][1] + matrix[2][2])) / 2;
        x *= Math.signum(x * (matrix[2][1] - matrix[1][2]));
        y *= Math.signum(y * (matrix[0][2] - matrix[2][0]));
        z *= Math.signum(z * (matrix[1][0] - matrix[0][1]));
        return new Quaternion(w, x, y, z).normalize();
    }
    
    /**
     * Sets the rotation of the transform to the given quaternion.
     * <p>
     * Normalizes the quaternion to ensure it represents a valid rotation.
     * <p></p>
     * For performance, use {@link #setRotationFast(Quaternion)}
     * if you are sure the quaternion is already normalized.
     * <p></p>
     * Matrix is calculated using the following code:
     * <pre><code>
     *     double xx = q.getX() * q.getX();
     *     double xy = q.getX() * q.getY();
     *     double xz = q.getX() * q.getZ();
     *     double xw = q.getX() * q.getW();
     *     double yy = q.getY() * q.getY();
     *     double yz = q.getY() * q.getZ();
     *     double yw = q.getY() * q.getW();
     *     double zz = q.getZ() * q.getZ();
     *     double zw = q.getZ() * q.getW();
     *     matrix[0][0] = 1 - 2 * (yy + zz);
     *     matrix[0][1] = 2 * (xy - zw);
     *     matrix[0][2] = 2 * (xz + yw);
     *     matrix[1][0] = 2 * (xy + zw);
     *     matrix[1][1] = 1 - 2 * (xx + zz);
     *     matrix[1][2] = 2 * (yz - xw);
     *     matrix[2][0] = 2 * (xz - yw);
     *     matrix[2][1] = 2 * (yz + xw);
     *     matrix[2][2] = 1 - 2 * (xx + yy);
     * </code></pre>
     * @param q
     */
    public void setRotation(Quaternion q) {
        q.normalize(); // Ensure the quaternion represents a valid rotation
        double xx = q.getX() * q.getX();
        double xy = q.getX() * q.getY();
        double xz = q.getX() * q.getZ();
        double xw = q.getX() * q.getW();
        
        double yy = q.getY() * q.getY();
        double yz = q.getY() * q.getZ();
        double yw = q.getY() * q.getW();
        
        double zz = q.getZ() * q.getZ();
        double zw = q.getZ() * q.getW();
        
        matrix[0][0] = 1 - 2 * (yy + zz);
        matrix[0][1] = 2 * (xy - zw);
        matrix[0][2] = 2 * (xz + yw);
        
        matrix[1][0] = 2 * (xy + zw);
        matrix[1][1] = 1 - 2 * (xx + zz);
        matrix[1][2] = 2 * (yz - xw);
        
        matrix[2][0] = 2 * (xz - yw);
        matrix[2][1] = 2 * (yz + xw);
        matrix[2][2] = 1 - 2 * (xx + yy);
    }
    
    /**
     * See {@link #setRotation(Quaternion)}
     * <p>
     * Does not normalize the quaternion for performance.
     * Use this only if you are sure the quaternion is already normalized.
     * <p></p>
     * If the quaternion is not normalized, this method will produce incorrect results.
     * @param q A normalized quaternion
     */
    public void setRotationFast(Quaternion q) {
        double xx = q.getX() * q.getX();
        double xy = q.getX() * q.getY();
        double xz = q.getX() * q.getZ();
        double xw = q.getX() * q.getW();
        
        double yy = q.getY() * q.getY();
        double yz = q.getY() * q.getZ();
        double yw = q.getY() * q.getW();
        
        double zz = q.getZ() * q.getZ();
        double zw = q.getZ() * q.getW();
        
        matrix[0][0] = 1 - 2 * (yy + zz);
        matrix[0][1] = 2 * (xy - zw);
        matrix[0][2] = 2 * (xz + yw);
        
        matrix[1][0] = 2 * (xy + zw);
        matrix[1][1] = 1 - 2 * (xx + zz);
        matrix[1][2] = 2 * (yz - xw);
        
        matrix[2][0] = 2 * (xz - yw);
        matrix[2][1] = 2 * (yz + xw);
        matrix[2][2] = 1 - 2 * (xx + yy);
    }
}
