public class Model {
    Float3[] modelPoints;
    Float3[] triangleColours;

    public Model(Float3[] modelPoints, Float3[] triangleColours) {
        this.modelPoints = modelPoints;
        this.triangleColours = triangleColours;
    }

    public Float3[] getModelPoints() { return modelPoints; }
    public Float3[] getTriangleColours() { return triangleColours; }
}
