public class RenderTarget {
    private Float3[][] colourBuffer;
    private Float[][] depthBuffer;
    private int width;
    private int height;
    public Float2 size;

    public RenderTarget(int width, int height) {
        this.width = width;
        this.height = height;

        this.colourBuffer = new Float3[height][width];
        this.depthBuffer = new Float[height][width];
        this.size = new Float2(width, height);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Float2 getSize() { return size; }
    public Float3[][] getColourBuffer() { return colourBuffer; }
    public Float[][] getDepthBuffer() { return depthBuffer; }

    public void setColourBuffer(int x, int y, Float3 val) { this.colourBuffer[y][x] = val; }
    public void setDepthBuffer(int x, int y, float depth) { this.depthBuffer[y][x] = depth; }
}
