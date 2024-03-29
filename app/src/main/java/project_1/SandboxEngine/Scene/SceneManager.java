package project_1.SandboxEngine.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

import java.util.*;

import org.joml.Vector2d;

import project_1.SandboxEngine.KeyListener;
import project_1.SandboxEngine.MouseListener;
import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.Element.Solid.Sand_pixel;
import project_1.SandboxEngine.Pixel.Special.Blank_pixel;

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

    //This will keep track of the size of the window at all times
    private int width;
    private int height;

    //Value that keeps track of what the user wants to draw to the scene
    private int pixel_selector;

    private SceneManager(){
        this.pixel_selector = 0;

    }

    public static SceneManager get(){
        if(SceneManager.instance == null){
            SceneManager.instance = new SceneManager();
        }

        return SceneManager.instance;
    }

    public void init(){
        CellularAutomata.get().init();
    }

    public void update(){
        SceneManager.get().pull_events();

        CellularAutomata.get().update();
    }

    /**
     * Function to draw everything onto the scene
     */
    public void draw(){
        CellularAutomata.get().draw();
    }
    
    /**
     * Function to update any values that depend on user inputs
     */
    private void pull_events(){
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
            if(CellularAutomata.get().is_position_empty(position)){
                CellularAutomata.get().add_pixel(SceneManager.get().create_selected_pixel(position),position);
            }
        }
    }

    //------------------------------------------------------------------------------------------
    //Menu selector functions
    //------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------
    //Pixel functions
    //------------------------------------------------------------------------------------------
    private Pixel create_selected_pixel(Vector2d position){
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
    
}
