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
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import java.io.File;

import org.joml.Vector2d;
import org.lwjgl.opengl.GL;

/*
 * This class handles the image input into the sandbox. It uses openCV canny edge detection
 *   to conver the image into a pixelated outline of the image. This class reads an image file
 *   and outputs a 2D grid where a 1 is a filled egdge and 0 is empty. This grid output will be
 *   used to draw pixels on the sandbox environment.
 * 
 * The two thresholds used in Canny edge detection determine the minimum and maximum intensity 
 *   gradient values considered for edge detection. Pixels with gradient values above the higher
 *   threshold are considered strong edges, while those between the two thresholds are considered
 *   weak edges. Any weak edge connected to a strong edge is also considered part of the edge.
 */
public class EdgeDetector {
    private static EdgeDetector instance = null;

    private String file = "C:\\Users\\james\\OneDrive\\Desktop\\SPRING_2024\\Computer_Graphics\\Project_1_CS5053\\images\\lebron.jpg"; // image file to detect edges of
    private Mat image; // Matrix 2D grid that reprents the image
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

    public void init() {
        
        EdgeDetector.get().total_width = (int)(SceneManager.get_width()/SQUARE_SIZE) - 2;
        EdgeDetector.get().total_height = (int)(SceneManager.get_height()/SQUARE_SIZE) - 8;
        EdgeDetector.get().thresh1 = 100;
        EdgeDetector.get().thresh2 = 200;

        // EdgeDetector.get().pixel_array = new Pixel[total_height][total_width];
        System.out.println("TOTALWIDTH: "+total_width+" TOTALHEIGHT: "+total_height);

        // Read the image
        EdgeDetector.get().image = Imgcodecs.imread(file);


        EdgeDetector.get().edgeOutput = detectImage(image);
        System.out.println("OUTPUT ROWS: "+edgeOutput.rows()+" OUTPUT COLS: "+edgeOutput.cols());
        // Copy edges to Pixel array
        int sum =0;
        for (int i = 0; i < EdgeDetector.get().total_height; i++) {
            for (int j = 0; j < EdgeDetector.get().total_width; j++) {
                double[] data = EdgeDetector.get().edgeOutput.get(i, j);
                System.out.print(" "+data[0]);
                
                if (data[0] > 0.0) {
                    ++sum;
                    CellularAutomata.get().add_pixel(new Blank_pixel(new Vector2d(j, i)), new Vector2d(j, i), true);//new Moveable_Solid(PixelType.BLANK, i+j, 0.0, 1.0, new Vector2d(j, i)); 
                }
                // else {
                //     CellularAutomata.get().pixel_array[i][j] = null;
                // }
            }
            // System.out.println();
        }
        System.out.println("COUNT: "+sum+ " OF "+EdgeDetector.get().edgeOutput.rows()*EdgeDetector.get().edgeOutput.cols());
    }

    // public Pixel[][] getPixels() {
    //     return pixel_array;
    // }

    public static EdgeDetector get(){
        if(EdgeDetector.instance == null){
            EdgeDetector.instance = new EdgeDetector();
        }

        return EdgeDetector.instance;
    }

    // public void draw(){
    //     for(int col=0; col<EdgeDetector.get().total_width; ++col){
    //         for(int row=0; row<EdgeDetector.get().total_height; ++row){
    //             if(CellularAutomata.get().pixel_array[col][row] != null){
    //                 CellularAutomata.get().pixel_array[col][row].draw();
    //             }
    //         }
    //     }

    // }
    // public void update(){
    //     for(int col=0; col<EdgeDetector.get().total_width; ++col){
    //         for(int row=0; row<EdgeDetector.get().total_height; ++row){
    //             if(CellularAutomata.get().pixel_array[col][row] != null){
    //                 CellularAutomata.get().pixel_array[col][row].update();
    //             }
    //         }
    //     }

    // }

    // public void add_pixel(Pixel pixel, Vector2d position){
    //     if((position.x >= CellularAutomata.get().get_width()) || (position.x <= 0)){
    //         return;
    //     }
    //     if((position.y >= CellularAutomata.get().get_height()) || (position.y <= 0)){
    //         return;
    //     }
    //     CellularAutomata.get().pixel_array[(int)position.x][(int)position.y] = pixel;
    // }

    // public void remove_pixel(Vector2d position){
    //     CellularAutomata.get().pixel_array[(int)position.x][(int)position.y] = null;
    // }

    /**
     * Retrieves the current image.
     *
     * @return The current image.
     */
    public Mat getImage() {
        return image;
    }

    /**
     * Sets the image to the specified file.
     *
     * @param file The file path of the image.
     */
    public void setImage(String file) {
        this.file = file;
        // Read the image
        image = Imgcodecs.imread(this.file);
        // Rotate the image by 90 degrees clockwise
        // Core.rotate(image, image, Core.ROTATE_90_CLOCKWISE);
    }

    /**
     * Detects edges in the specified image.
     *
     * @param img The input image.
     * @return The detected edges.
     */
    public Mat detectImage(Mat img) {
        // Core.rotate(image, image, Core.ROTATE_90_CLOCKWISE);
        Mat resizedImage = resizeImage(total_width, total_height);
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
    private Mat resizeImage(int width, int height) {
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
}
