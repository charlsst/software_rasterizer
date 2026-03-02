public class Float2 {
    private float x = 0;
    private float y = 0;

    public Float2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }

    public float dotProduct(Float2 that) {
        return (this.getX() * that.getX()) + (this.getY() * that.getY());
    }

    public Float2 getPerpendicular() {
        return new Float2(this.getY(), -this.getX()); // clockwise
    }
}
