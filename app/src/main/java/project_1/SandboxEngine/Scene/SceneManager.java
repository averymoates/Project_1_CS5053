package project_1.SandboxEngine.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

import java.util.*;

import org.joml.Vector2d;

import project_1.SandboxEngine.KeyListener;
import project_1.SandboxEngine.MouseListener;
import project_1.SandboxEngine.Pixel.Blank_pixel;
import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.Sand_pixel;

/**
 * Author: Avery Moates
 * Date:   3/11/2024
 * 
 * Purpose: Class to hold all the user modified values and to draw the pixels onto the screen
 * 
 * Notes:
 * 
 * Screen coordinates is the actual pixel values of the screen. Default is 1920X1080. The mouse/cursor uses screen coordinates.
 * Scene coordinates is scaled down from the screen coordinate by `SQUARE_SIZE`. Pixels positions uses scene coordinates.
 */
public class SceneManager {

    private static SceneManager instance = null;

    //Change this value to change the size of all the pixels
    final private static double SQUARE_SIZE = 10;

    //This will hold all the pixels that a person draws to the scene
    private ArrayList<Pixel> pixels = null;

    //This will keep track of the size of the window at all times
    private int width;
    private int height;

    //Value that keeps track of what the user wants to draw to the scene
    private int pixel_selector;

    private SceneManager(){
        pixels = new ArrayList<Pixel>();
        pixel_selector = 0;

    }

    public static SceneManager get(){
        if(SceneManager.instance == null){
            SceneManager.instance = new SceneManager();
        }

        return SceneManager.instance;
    }

    /**
     * Function to handle all the updates that needs to happen every frame
     */
    public static void update(){
        SceneManager.pull_events();

        for(Pixel pixel: SceneManager.get().pixels){
            pixel.update();
        }
    }

    /**
     * Function to draw everything onto the scene
     */
    public static void draw(){
        for(Pixel pixel: SceneManager.get().pixels){
            pixel.draw();
        }
    }
    
    /**
     * Function to update any values that depend on user inputs
     */
    private static void pull_events(){
        //Check which pixel is selected. This will need to be changed to a menu like selector
        if(KeyListener.isKeyPressed(GLFW_KEY_B)){
            SceneManager.get().pixel_selector = 0;
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_S)){
            SceneManager.get().pixel_selector = 1;
        }

        //Add the selected pixel so that it can be drawn later
        if(MouseListener.isMouseButtonDown(0)){
            Vector2d position = MouseListener.mouse_loc_in_screen();
            if(SceneManager.is_position_empty(position)){
                SceneManager.add_pixel(SceneManager.create_selected_pixel(position));
            }
        }
    }

    //------------------------------------------------------------------------------------------
    //Menu selector functions
    //------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------
    //Pixel array functions
    //------------------------------------------------------------------------------------------
    
    /**
     * Function to add any pixel into the static pixel array
     * 
     * @param pixel The pixel that you want to add to the static pixel array
     */
    public static void add_pixel(Pixel pixel){
        SceneManager.get().pixels.add(pixel);
    }

    /**
     * Function to check if a certain position is free,
     * 
     * @param position
     * @return
     */
    private static boolean is_position_empty(Vector2d position){
        for(Pixel pixel: SceneManager.get().pixels){
            if((pixel.get_position().x == position.x) && (pixel.get_position().y == position.y)){
                return false;
            }
        }
        return true;
    }

    /**
     * Function to remove a specific pixel from the pixel array
     * 
     * @param pixel The pixel that need to be removed
     * @return      True if the operation was successful
     */
    public static boolean remove_pixel(Pixel pixel){
        if(SceneManager.get().pixels.size() == 0){
            System.out.println("Pixel array empty");
            return false;
        }
        else{
            SceneManager.get().pixels.remove(pixel);
            return true;
        }
    }

    /**
     * Function to handle which pixel is drawn to the scene
     * 
     * @param position  The position in scene coordinates
     * @return          
     */
    private static Pixel create_selected_pixel(Vector2d position){
        switch (SceneManager.get().pixel_selector) {
            case 0:
                return new Blank_pixel(position);
            case 1:
                return new Sand_pixel(position);
        
            default:
                return new Blank_pixel(position);
        }
    }

    //------------------------------------------------------------------------------------------
    //Setter functions
    //------------------------------------------------------------------------------------------
    public static void set_width(int w){
        SceneManager.get().width = w;
    }

    public static void set_height(int h){
        SceneManager.get().height = h;
    }

    //------------------------------------------------------------------------------------------
    //getter functions
    //------------------------------------------------------------------------------------------
    public static int get_width(){
        return SceneManager.get().width;
    }

    public static int get_height(){
        return SceneManager.get().height;
    }

    public static double get_square_size(){
        SceneManager.get();
        return SceneManager.SQUARE_SIZE;
    }

    public static ArrayList<Pixel> get_pixels(){
        return SceneManager.get().pixels;
    }
    
}
