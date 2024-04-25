package project_1.SandboxEngine.Scene;
import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;
import project_1.SandboxEngine.Pixel.Special.Blank_pixel;
import project_1.SandboxEngine.Pixel.Element.Solid.Moveable_Solid;
import project_1.SandboxEngine.Scene.CellularAutomata;

import project_1.SandboxEngine.Scene.SceneManager;

// import org.opencv.core.Core;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.scene.Scene;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.joml.Vector2d;
import org.lwjgl.opengl.GL;

/**
 * This class handles the image input into the sandbox. It uses openCV canny edge detection
 *   to conver the image into a pixelated outline of the image. This class reads an image file
 *   and outputs a 2D grid where a 1 is a filled egdge and 0 is empty. This grid output will be
 *   used to draw pixels on the sandbox environment.
 * 
 * The two thresholds used in Canny edge detection determine the minimum and maximum intensity 
 *   gradient values considered for edge detection. Pixels with gradient values above the higher
 *   threshold are considered strong edges, while those between the two thresholds are considered
 *   weak edges. Any weak edge connected to a strong edge is also considered part of the edge.
 * 
 * @author Jack Cornette
 */
public class EdgeDetector {
    private static EdgeDetector instance = null;

    private String file = "images\\lebron.jpg"; // image file to detect edges of
    private String[] imageNames;
    private int idx;
    // private String fileBase = 
    // private Mat image; // Matrix 2D grid that reprents the image
    private Mat edgeOutput; // final edge detected grid output

    // private Pixel[][] pixel_array;

    private int thresh1;
    private int thresh2;

    //These values describe the total draw space.
    private int total_width;
    private int total_height;

    private final double SQUARE_SIZE = 10.0;

    /**
     * Constructs a new EdgeDetector object with default threshold values.
     */
    public EdgeDetector() {
    }

    public static EdgeDetector get(){
        if(EdgeDetector.instance == null){
            EdgeDetector.instance = new EdgeDetector();
        }

        return EdgeDetector.instance;
    }

    public void init() {
        
        EdgeDetector.get().total_width = (int)(SceneManager.get_width()/SQUARE_SIZE) - 2;
        EdgeDetector.get().total_height = (int)(SceneManager.get_height()/SQUARE_SIZE) - 8;
        EdgeDetector.get().thresh1 = 100;
        EdgeDetector.get().thresh2 = 200;

        // EdgeDetector.get().pixel_array = new Pixel[total_height][total_width];
        // System.out.println("TOTALWIDTH: "+total_width+" TOTALHEIGHT: "+total_height);

        EdgeDetector.get().imageNames = getFilesInFolder("images");

        System.out.println("FOUND " + EdgeDetector.get().imageNames.length + " IMAGES");
        EdgeDetector.get().setImage(0);

    }



    /**
     * Changes the currently selected image and grid display to specified name.
     * At the moment, this also clears all blank pixels when switching
     *
     * @imgName The new image to switch to for edge detection.
     */
    public boolean setImage(int idx) {

        if(idx >= 0 && idx < EdgeDetector.get().imageNames.length) {
            EdgeDetector.get().idx = idx;
            System.out.println("SHOWING IMAGE {images\\"+EdgeDetector.get().imageNames[idx]+"}");
            // Read the image file
            // SceneManager.get().getImage().changeImage("images\\"+EdgeDetector.get().imageNames[idx]);
            Mat image = Imgcodecs.imread("images\\"+EdgeDetector.get().imageNames[idx]);
            // clear grid for new drawing
            CellularAutomata.get().empty_curr_grid();
            // draw pixels for this image
            addEdgePixels(image);
            return true;
        }

        return false;
    }

    public int getImageIdx() {
        return EdgeDetector.get().idx;
    }
    public String getCurrentImageName() {
        return EdgeDetector.get().imageNames[EdgeDetector.get().idx];
    }




    /** detects edges on the current image and adds them as pixels to the CellularAutomata grid */
    public void addEdgePixels(Mat image) {

        EdgeDetector.get().edgeOutput = detectImage(image);
        // System.out.println("OUTPUT ROWS: "+edgeOutput.rows()+" OUTPUT COLS: "+edgeOutput.cols());
        // Copy edges to Pixel array
        int sum =0;
        for (int i = 0; i < EdgeDetector.get().total_height; i++) {
            for (int j = 0; j < EdgeDetector.get().total_width; j++) {
                double[] data = EdgeDetector.get().edgeOutput.get(i, j);
                // System.out.print(" "+data[0]);
                
                if (data[0] > 0.0) {
                    ++sum;
                    Pixel p = new Blank_pixel(new Vector2d(j, i));
                    p.set_pixel_color(0,0,0);
                    CellularAutomata.get().add_pixel(p, new Vector2d(j, i), false);
                }

            }
            // System.out.println();
        }
        // System.out.println("COUNT: "+sum+ " OF "+EdgeDetector.get().edgeOutput.rows()*EdgeDetector.get().edgeOutput.cols());
    }

    /**
     * Detects edges in the specified image.
     *
     * @param img The input image.
     * @return The detected edges.
     */
    public Mat detectImage(Mat img) {
        // Core.rotate(image, image, Core.ROTATE_90_CLOCKWISE);
        Mat resizedImage = resizeImage(img, total_width, total_height);
        edgeOutput = detectEdges(resizedImage, thresh1, thresh2);

        // Save the output image for debug purposes
        Imgcodecs.imwrite("output.png", edgeOutput);

        return edgeOutput;
    }

    /**
     * Detects edges in the specified image.
     *
     * @param src The source image.
     * @param thresh1 The first threshold for edge detection.
     * @param thresh2 The second threshold for edge detection.
     * @return The detected edges.
     */
    private Mat detectEdges(Mat src, int thresh1, int thresh2) {
        // Convert image to grayscale
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_RGB2GRAY);

        // Apply Canny edge detection
        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, thresh1, thresh2);

        return edges;
    }

    /**
     * Resizes the input image to a given size.
     *
     * @param width The new pixel width of image.
     * @param height The new pixel height of image.
     * @return The resized image.
     */
    private Mat resizeImage(Mat image, int width, int height) {
        Size newSize = new Size(width, height);
        
        Imgproc.resize(image, image, newSize);
        return image;
    }

    /**
     * Updates the edge detection thresholds.
     *
     * @param update1 The update value for the first threshold.
     * @param update2 The update value for the second threshold.
     */
    public void updateEdgeThresholds(int update1, int update2) {
        thresh1 += update1;
        thresh2 += update2;

        // Check threshold bounds
        if (thresh1 <= 0) {
            thresh1 = 0;
        }
        if (thresh2 <= 0) {
            thresh2 = 0;
        }
        if (thresh1 > thresh2) {
            thresh1 = thresh2;
        }
        System.out.println("EDGE THRESHOLDS: (" + thresh1 + ", " + thresh2 + ")");
    }

    /**
     * Retrieves the first threshold value.
     *
     * @return The first threshold value.
     */
    public int getThresh1() {
        return thresh1;
    }

    /**
     * Retrieves the second threshold value.
     *
     * @return The second threshold value.
     */
    public int getThresh2() {
        return thresh2;
    }

    /**
     * Returns a string array containing file names for every file in the specified folder.
     *
     * @folderPath Folder to read all files of
     */
    public static String[] getFilesInFolder(String folderPath) {
        // Create a File object for the specified folder path.
        File folder = new File(folderPath);

        // Check if the folder exists and is indeed a directory.
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("The specified folder does not exist or is not a directory.");
            return new String[0]; // Return an empty array if the folder doesn't exist.
        }

        // List all files in the folder.
        File[] files = folder.listFiles();
        if (files == null) {
            System.out.println("No files found or an I/O error occurred.");
            return new String[0]; // Return an empty array if an I/O error occurs.
        }

        // Use a List to hold file names because we don't know exactly how many files there are.
        ArrayList<String> fileNames = new ArrayList<>();

        // Iterate over all files in the directory.
        for (File file : files) {
            if (file.isFile()) { // Check if the file object is indeed a file and not a subdirectory.
                fileNames.add(file.getName()); // Add the name of the file to the list.
            }
        }

        // Convert the List of file names to an array of Strings and return it.
        return fileNames.toArray(new String[0]);
    }

    // added this for fun
    public static void placeRandomPixels() {
        Random random = new Random();
        int numPixels = random.nextInt(100); // Number of random pixels to place, adjust as necessary

        for (int p = 0; p < numPixels; p++) {
            // Generate random coordinates within the grid
            int i = random.nextInt(EdgeDetector.get().total_height);
            int j = random.nextInt(EdgeDetector.get().total_width);

            // Generate random RGB color values
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);

            // Create a pixel at random location with random color
            Pixel pixel = new Blank_pixel(new Vector2d(j, i));
            pixel.set_pixel_color(red, green, blue);
            CellularAutomata.get().add_pixel(pixel, new Vector2d(j, i), false);
        }
    }

}
