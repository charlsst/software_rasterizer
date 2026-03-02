public class Image {
    private int width, height;
    private Float3[][] image;
    private String name;

    public Image(int width, int height, String name) {
        this.width = width;
        this.height = height;
        this.image = new Float3[height][width];
        this.name = name;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getFileName() { return name; }
    public Float3 getPixel(int x, int y) {
        if (image[y][x] == null) {
            return new Float3(0, 0, 0);
        } else {
            return image[y][x];
        }
    }
    public Float3[][] getImage() { return image; }

    public void setPixel(int x, int y, Float3 pixel) {
        image[y][x] = pixel;
    }
    public void setImage(Float3[][] image) { this.image = image; }
}
