public class Float3 {
    private float x = 0;
    private float y = 0;
    private float z = 0;

    private float r = 0;
    private float g = 0;
    private float b = 0;

    public Float3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.r = x;
        this.g = y;
        this.b = z;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
    public float getR() { return r; }
    public float getG() { return g; }
    public float getB() { return b; }

    public void setX(float x) { this.x = x; this.r = x; }
    public void setY(float y) { this.y = y; this.g = y; }
    public void setZ(float z) { this.z = z; this.g = z; }

    public float dotProduct(Float3 that) {
        return (this.getX() * that.getX()) + (this.getY() * that.getY()) + (this.getZ() * that.getZ());
    }

    public Float3 addFloat3(Float3 that) {
        return new Float3(this.getX() + that.getX(), (this.getY() + that.getY()), this.getZ() + that.getZ());
    }

    public Float2 chopZ() {
        return new Float2(this.getX(), this.getY());
    }
}
