public class Triangle {
    private Float2 a;
    private Float2 b;
    private Float2 c;

    public Triangle(Float2 a, Float2 b, Float2 c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Float2 getA() { return a; }
    public Float2 getB() { return b; }
    public Float2 getC() { return c; }

    public float signedTriangleArea(Float2 a, Float2 b, Float2 c) {
        Float2 ac = new Float2(c.getX() - a.getX(), c.getY() - a.getY());
        Float2 abPerp = new Float2(b.getX() - a.getX(), b.getY() - a.getY()).getPerpendicular();
        return ac.dotProduct(abPerp) / 2;
    }

    public boolean pointInTriangle(Float2 p) {
        Float2 a = this.getA();
        Float2 b = this.getB();
        Float2 c = this.getC();
        float areaABP = signedTriangleArea(a, b, p);
        float areaBCP = signedTriangleArea(b, c, p);
        float areaCAP = signedTriangleArea(c, a, p);

        return areaABP >= 0 && areaBCP >= 0 && areaCAP >= 0 && (areaABP + areaBCP + areaCAP) > 0;
    }

    public Float3 triangleAreaWeights(Float2 p) {
        Float2 a = this.getA();
        Float2 b = this.getB();
        Float2 c = this.getC();

        float areaABP = signedTriangleArea(a, b, p);
        float areaBCP = signedTriangleArea(b, c, p);
        float areaCAP = signedTriangleArea(c, a, p);

        float invAreaSum = 1 / (areaABP + areaBCP + areaCAP);
        return new Float3(areaBCP * invAreaSum, areaCAP * invAreaSum, areaABP * invAreaSum);
    }
}
