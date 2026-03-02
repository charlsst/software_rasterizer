import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Renderer {
    public static void WriteImageToFile(Image image) {
        // Variable Declarations
        int height = image.getHeight();
        int width = image.getWidth();

        int rowSize = (width * 3 + 3) & ~3; // pad row to 4-byte boundary
        int imageSize = width * height;
        int fileSize = 14 + 40 + imageSize;

        byte[] bmpHeader = new byte[14];
        byte[] dibHeader = new byte[40];

        // BMP Header
        bmpHeader[0] = 'B';
        bmpHeader[1] = 'M';
        bmpHeader[2] = (byte) fileSize;
        bmpHeader[3] = (byte) (fileSize>>8);
        bmpHeader[4] = (byte) (fileSize>>16);
        bmpHeader[5] = (byte) (fileSize>>24);
        bmpHeader[10] = 54; // pixel data offset = 14 + 40

        // DIB Header (BITMAPINFOHEADER)
        dibHeader[0] = 40; // header size
        dibHeader[4] = (byte) width;
        dibHeader[5] = (byte) (width >> 8);
        dibHeader[6] = (byte) (width >> 16);
        dibHeader[7] = (byte) (width >> 24);
        dibHeader[8] = (byte) height;
        dibHeader[9] = (byte) (height >> 8);
        dibHeader[10] = (byte) (height >> 16);
        dibHeader[11] = (byte) (height >> 24);
        dibHeader[12] = 1; // colour planes
        dibHeader[14] = 24; // bits per pixel
        dibHeader[20] = (byte) imageSize;
        dibHeader[21] = (byte) (imageSize>>8);
        dibHeader[22] = (byte) (imageSize>>16);
        dibHeader[23] = (byte) (imageSize>>24);

        // Writing row
        try (FileOutputStream fos = new FileOutputStream(image.getFileName())) {
            fos.write(bmpHeader);
            fos.write(dibHeader);

            for (int y = 0; y < height; y++) {
                byte[] pixelRow = new byte[rowSize];
                for (int x = 0; x < width; x++) {
                    int index = x * 3;
                    pixelRow[index] = (byte) image.getPixel(x, y).getB(); // Blue
                    pixelRow[index + 1] = (byte) image.getPixel(x, y).getG(); // Green
                    pixelRow[index + 2] = (byte) image.getPixel(x, y).getR(); // Red
                }
                fos.write(pixelRow);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Image createTestImage(int width, int height, String fileName) {
        Image image = new Image(width, height, fileName);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float rVal = (float) x * 255 / (float) width;
                float gVal = (float) y * 255 / (float) height;
                Float3 pixel = new Float3(rVal, gVal, 0);
                image.setPixel(x, y, pixel);
            }
        }
        return image;
    }

    public static Float3 vertexToScreen(Float3 vertex, Transform transform, Float2 numPixels) {
        Float3 vertex_world = transform.toWorldPoint(vertex);

        float fov = 60;
        float screenHeight_world = (float) Math.tan(Math.toRadians(fov / 2)) * 2;
        float pixelsPerWorldUnit = numPixels.getY() / screenHeight_world / vertex_world.getZ();

        Float2 pixelOffset = new Float2(vertex_world.getX()*pixelsPerWorldUnit, vertex_world.getY()*pixelsPerWorldUnit);
        return new Float3(numPixels.getX() / 2 + pixelOffset.getX(), numPixels.getY() / 2 + pixelOffset.getY(), vertex_world.getZ());
    }

    public void render(Model model, RenderTarget renderTarget, String fileName, Transform transform) {
        for (int i = 0; i < model.getModelPoints().length; i += 3) {
            Float3 a = vertexToScreen(model.getModelPoints()[i], transform, renderTarget.getSize());
            Float3 b = vertexToScreen(model.getModelPoints()[(i + 1)], transform, renderTarget.getSize());
            Float3 c = vertexToScreen(model.getModelPoints()[(i + 2)], transform, renderTarget.getSize());

            float minX = Math.min(Math.min(a.getX(), b.getX()), c.getX());
            float minY = Math.min(Math.min(a.getY(), b.getY()), c.getY());
            float maxX = Math.max(Math.max(a.getX(), b.getX()), c.getX());
            float maxY = Math.max(Math.max(a.getY(), b.getY()), c.getY());

            int blockStartX = Math.clamp((int)minX, 0, renderTarget.getWidth() - 1);
            int blockStartY = Math.clamp((int)minY, 0, renderTarget.getHeight() - 1);
            int blockEndX = Math.clamp((int)maxX, 0, renderTarget.getWidth() - 1);
            int blockEndY = Math.clamp((int)maxY, 0, renderTarget.getHeight() - 1);

            for (int y = blockStartY; y <= blockEndY; y++) {
                for (int x = blockStartX; x <= blockEndX; x++) {
                    Triangle triangle = new Triangle(a.chopZ(), b.chopZ(), c.chopZ());
                    if (triangle.pointInTriangle(new Float2(x, y))) {
                        Float3 depths = new Float3(1 / a.getZ(), 1/ b.getZ(), 1/ c.getZ());
                        Float3 weights = triangle.triangleAreaWeights(new Float2(x, y));
                        float depth = 1 / depths.dotProduct(weights);
                        if (renderTarget.getDepthBuffer()[y][x] != null) {
                            if (depth > renderTarget.getDepthBuffer()[y][x]) {
                                continue;
                            }
                        }

                        renderTarget.setColourBuffer(x, y, model.getTriangleColours()[i/3]);
                        renderTarget.setDepthBuffer(x, y, depth);
                    }
                }
            }
        }
        Image image = new Image(renderTarget.getWidth(), renderTarget.getHeight(), fileName);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image.setPixel(x, y, renderTarget.getColourBuffer()[y][x]);
            }
        }
        WriteImageToFile(image);
    }
}
