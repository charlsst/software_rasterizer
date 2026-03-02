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

                br.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return trianglePoints.toArray(new Float3[trianglePoints.size()]);
    }

    public static void main(String[] args) {
        Random rand = new Random();

        //Cube
        Float3[] cubePoints = LoadObjFile("cube.obj");
        Float3[] cubeColours = new Float3[(int) (cubePoints.length / 3)];

        for (int i = 0; i < cubeColours.length; i++) {
            cubeColours[i] = new Float3(0, 0, 200+rand.nextFloat(55));
        }

        Model cubeModel = new Model(cubePoints, cubeColours);

        //Centurion
        Float3[] centurionPoints = LoadObjFile("roman_centurion.obj");
        Float3[] centurionColours = new Float3[(int) (centurionPoints.length / 3)];

        for (int i = 0; i < centurionColours.length; i++) {
            centurionColours[i] = new Float3(255, rand.nextFloat(255), rand.nextFloat(255));
        }

        Model centurionModel = new Model(centurionPoints, centurionColours);

        //Cat Obj
        Float3[] catPoints = LoadObjFile("cat.obj");
        Float3[] catColours = new Float3[(int) (catPoints.length / 3)];

        for (int i = 0; i < catColours.length; i++) {
            catColours[i] = new Float3(213 + rand.nextFloat(42), 207 + rand.nextFloat(48), 255);
        }

        Model catModel = new Model(catPoints, catColours);

        //Rendering stuff
        Renderer renderer = new Renderer();

        RenderTarget renderTarget;

        String fileName = "whattheheck" + ".bmp";

        renderTarget = new RenderTarget(500, 500);
        renderer.render(cubeModel, renderTarget, fileName, new Transform(new Float3(0, -3, 5), new Float2(0, 0)));
        renderer.render(centurionModel, renderTarget, fileName, new Transform(new Float3(0, 0, 5), new Float2(180, 0)));
        renderer.render(catModel, renderTarget, fileName, new Transform(new Float3(35, 20, 100), new Float2(150, 0)));
    }
}