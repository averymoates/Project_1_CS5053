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
 */
public class SceneManager {

    private static SceneManager instance = null;

    //Change this value to change the size of all the pixels
    final private static double SQUARE_SIZE = 10;

    //This will hold all the pixels that a person draws to the screen
    private ArrayList<Pixel> pixels = null;

    //This will keep track of the size of the window at all times
    private int width;
    private int height;

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

    public static void update(){
        SceneManager.pull_events();

        for(Pixel pixel: SceneManager.get().pixels){
            pixel.update();
        }
    }

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
    public static void add_pixel(Pixel pixel){
        SceneManager.get().pixels.add(pixel);
    }

    private static boolean is_position_empty(Vector2d position){
        for(Pixel pixel: SceneManager.get().pixels){
            if((pixel.get_position().x == position.x) && (pixel.get_position().y == position.y)){
                return false;
            }
        }
        return true;
    }

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
