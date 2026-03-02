import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static Float3[] LoadObjFile(String fileName) {
        ArrayList<Float3> allPoints = new ArrayList<>();
        ArrayList<Float3> trianglePoints = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            while (br.ready()) {
                String line = br.readLine();

                if (line.startsWith("v ")) {
                    String[] vals = line.substring(2).replace("\\", "").split(" ");
                    float[] axes = new float[vals.length];
                    for (int i = 0; i < vals.length; i++) {
                        axes[i] = Float.parseFloat(vals[i].trim());
                    }
                    allPoints.add(new Float3(axes[0], axes[1], axes[2]));
                } else if (line.startsWith("f ")) {
                    String[] faceIndexGroups = line.substring(2).split(" ");

                    for (int i = 0; i < faceIndexGroups.length; i++) {
                        String[] vals = faceIndexGroups[i].replace("\\", "").replace("}", "").split("/");
                        int[] indexGroup = new int[faceIndexGroups.length];
                        for (int j = 0; j < vals.length; j++) {
                            indexGroup[j] = Integer.parseInt(vals[j].trim());
                        }

                        int pointIndex = indexGroup[0] - 1; // indices start at 1 in obj files
                        if (i >= 3) { trianglePoints.add(trianglePoints.get(trianglePoints.size() - (3 * i - 6))); }
                        if (i >= 3) { trianglePoints.add(trianglePoints.get(trianglePoints.size() - (2))); }
                        trianglePoints.add(allPoints.get(pointIndex));
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return trianglePoints.toArray(new Float3[trianglePoints.size()]);
    }

    public static Float3[] generateRandomColours(Float3[] objectPoints) {
        Random rand = new Random();
        Float3[] newObjectColours = new Float3[(int) (objectPoints.length / 3)];
        for (int i = 0; i < newObjectColours.length; i++) {
            newObjectColours[i] = new Float3(rand.nextFloat(255), rand.nextFloat(255), rand.nextFloat(255));
        }
        return newObjectColours;
    }

    public static void main(String[] args) {
        String imageFileName = "replace_with_image_name";
        imageFileName = "cubeTest";

        // Cube Example
        Float3[] cubePoints = LoadObjFile("objs/cube.obj");
        Float3[] cubeColours = generateRandomColours(cubePoints);
        Model cubeModel = new Model(cubePoints, cubeColours);
        Float3 cubePosition = new Float3(0, -1, 7);
        Float2 cubeRotation = new Float2(20, -10);


        /* Add more objects to the scene using the following format:
            Float3[] myObjectPoints = LoadObjFile("myObject.obj");
            Float3[] myObjectColours = generateRandomColours(myObjectPoints);
            Model myObjectModel = new Model(myObjectPoints, myObjectColours);
            Float3 myObjectPosition = new Float3(x, y, z);
            Float2 myObjectRotation = new Float2(x, y);
        */

        // Rendering Stuff
        Renderer renderer = new Renderer();

        RenderTarget renderTarget;

        String fileName = "renderedImages/" + imageFileName + ".bmp";

        renderTarget = new RenderTarget(500, 500);

        renderer.render(cubeModel, renderTarget, fileName, new Transform(cubePosition, cubeRotation));
        // Add more lines like the previous for each object you add.
    }
}