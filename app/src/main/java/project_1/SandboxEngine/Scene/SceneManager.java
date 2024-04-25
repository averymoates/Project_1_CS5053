package project_1.SandboxEngine.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_COMMA;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PERIOD;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import java.util.*;
import java.awt.Color;


import org.joml.Vector2d;

import project_1.SandboxEngine.KeyListener;
import project_1.SandboxEngine.MouseListener;
import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.Element.Liquid.Water_pixel;
import project_1.SandboxEngine.Pixel.Element.Solid.Sand_pixel;
import project_1.SandboxEngine.Pixel.Special.Blank_pixel;
import project_1.SandboxEngine.Utilities.Button;

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

    private static Button sandButton, waterButton, blankButton, leftArrow, rightArrow, image, clear, redButton, greenButton, blueButton, simulate;
    public Button showImage;

    //Change this value to change the size of all the pixels
    final private static double SQUARE_SIZE = 10;

    //This will keep track of the size of the window at all times
    private int width;
    private int height;

    //Value that keeps track of what the user wants to draw to the scene
    private int pixel_selector;

    private long start_time;
    private long end_time;
    private int frame_counter;

    private int redY=425, greenY=425, blueY=425;

    private SceneManager(){
        this.pixel_selector = 0;
        start_time = System.nanoTime();
        end_time = 0;
        frame_counter = 0;
    }

    public static SceneManager get(){
        if(SceneManager.instance == null){
            SceneManager.instance = new SceneManager();
        }

        return SceneManager.instance;
    }
    
    public void init(){
        CellularAutomata.get().init();
        EdgeDetector.get().init();

        sandButton = new Button(100f, 1100f, 300f, 100f, "images\\buttons\\sand.png", Color.ORANGE);
        waterButton = new Button(450f, 1100f, 300f, 100f, "images\\buttons\\water.png", Color.BLUE);
        blankButton = new Button(800f, 1100f, 300f, 100f, "images\\buttons\\solid.png", Color.GRAY);
        leftArrow = new Button(1200f, 1100f, 150f, 75f, "images\\buttons\\left_arrow.png", Color.WHITE);
        rightArrow = new Button(1400f, 1100f, 150f, 75f, "images\\buttons\\right_arrow.png", Color.WHITE);
        image = new Button(1225f, 1200f, 300f, 85f, "images\\buttons\\image.png", Color.WHITE);
        simulate = new Button(1625f, 1100f, 300f, 100f, "images\\buttons\\simulate.png", Color.green);
        clear = new Button(2000f, 100f, 300f, 85f, "images\\buttons\\clear.png", Color.WHITE);
        redButton = new Button(2025f, (float)redY, 50f, 50f, "", Color.RED);
        greenButton = new Button(2125f, (float)greenY, 50f, 50f, "", Color.GREEN);
        blueButton = new Button(2225f, (float)blueY, 50f, 50f, "", Color.BLUE);
        

        showImage = new Button(2000f, 1000, 300f, 205f, "images\\aaablank.png", Color.WHITE);

        blankButton.selected(true);
    }
    public Button getImage() {
        return this.showImage;
    }

    public void update(){
        fps();
        SceneManager.get().pull_events();

        CellularAutomata.get().update();
    }

    
    /**
     * Function to draw everything onto the scene
     */
    public void draw(){
        // start with a white canvas
        glColor3f(1f,1f,1f);
        glBegin(GL_POLYGON);
            glVertex2f(0, 0);
            glVertex2f(1900f, 0);
            glVertex2f(1900f, 1000f);
            glVertex2f(0, 1000f);
        glEnd();

        // outline sandbox environment
        glColor3f(0f,0f,0f);
        glLineWidth(5.0f);
        glBegin(GL_LINE_LOOP);
            glVertex2f(0, 0);
            glVertex2f(1900f, 0);
            glVertex2f(1900f, 1000f);
            glVertex2f(0, 1000f);
        glEnd();

        /******* COLOR SLIDER ********/
        // 3 color slider lines
        glColor3f(0f,0f,0f);
        glLineWidth(5.0f);
        glBegin(GL_LINES);
            glVertex2f(2050f, 250f);
            glVertex2f(2050f, 625f);
        glEnd();

        glColor3f(0f,0f,0f);
        glLineWidth(5.0f);
        glBegin(GL_LINES);
            glVertex2f(2150f, 250f);
            glVertex2f(2150f, 625f);
        glEnd();

        glColor3f(0f,0f,0f);
        glLineWidth(5.0f);
        glBegin(GL_LINES);
            glVertex2f(2250f, 250f);
            glVertex2f(2250f, 625f);
        glEnd();

        // display current color of sliders
        glColor3f(1.0f-(redY-250)/(400f),1.0f-(greenY-250)/(400f),1.0f-(blueY-250)/(400f));
        glBegin(GL_POLYGON);
            glVertex2f(2050f, 700f);
            glVertex2f(2250f, 700f);
            glVertex2f(2250f, 900f);
            glVertex2f(2050f, 900f);
        glEnd();
        glColor3f(0f,0f,0f);
        glLineWidth(5.0f);
        glBegin(GL_LINE_LOOP);
            glVertex2f(2050f, 700f);
            glVertex2f(2250f, 700f);
            glVertex2f(2250f, 900f);
            glVertex2f(2050f, 900f);
        glEnd();

        CellularAutomata.get().draw();

        // continually display buttons
        sandButton.render();
        waterButton.render(); 
        blankButton.render();
        leftArrow.render();
        rightArrow.render();
        image.render();
        clear.render();
        redButton.render();
        greenButton.render();
        blueButton.render();
        showImage.render();
        simulate.render();
    }


    private boolean fallingEdge = false;
    /**
     * Function to update any values that depend on user inputs (keyboard and mouse events)
     */
    private void pull_events(){

        /******* KEYBOARD EVENTS *********/
        if(KeyListener.isKeyPressed(GLFW_KEY_B)){ // sand
            SceneManager.get().pixel_selector = 0;
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_S)){ // water
            SceneManager.get().pixel_selector = 1;
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_W)){ // blank
            SceneManager.get().pixel_selector = 2;
        }
        else if(KeyListener.isKeyTapped(GLFW_KEY_LEFT)) { // change detected img
            EdgeDetector.get().setImage(EdgeDetector.get().getImageIdx()-1);
            showImage.changeImage("images\\"+EdgeDetector.get().getCurrentImageName());
        }
        else if(KeyListener.isKeyTapped(GLFW_KEY_RIGHT)) { // change detected img
            EdgeDetector.get().setImage(EdgeDetector.get().getImageIdx()+1);
            showImage.changeImage("images\\"+EdgeDetector.get().getCurrentImageName());
        }
        // edge detector thresholds
        else if(KeyListener.isKeyTapped(GLFW_KEY_SEMICOLON)) { // decrease thresh1
            EdgeDetector.get().updateEdgeThresholds(-10, 0);
        }
        else if(KeyListener.isKeyTapped(GLFW_KEY_APOSTROPHE)) { // increase thresh1
            EdgeDetector.get().updateEdgeThresholds(10, 0);
        }
        else if(KeyListener.isKeyTapped(GLFW_KEY_LEFT_BRACKET)) { // decrease thresh2
            EdgeDetector.get().updateEdgeThresholds(0, -10);
        }
        else if(KeyListener.isKeyTapped(GLFW_KEY_RIGHT_BRACKET)) { // decrease thresh2
            EdgeDetector.get().updateEdgeThresholds(0, 10);
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_UP)) { // move selected slider UP
            if(redButton.isSelected()) {
                if (redY-1 > 225) {
                    redY -= 1;
                    redButton.translate(0, -1);
                }
            }
            if(greenButton.isSelected()) {
                if (greenY-1 > 225) {
                    greenY -= 1;
                    greenButton.translate(0, -1);
                }
            }
            if(blueButton.isSelected()) {
                if (blueY-1 > 250) {
                    blueY -= 1;
                    blueButton.translate(0, -1);
                }
            }
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_DOWN)) { // move selected slider DOWN
            if(redButton.isSelected()) {
                if (redY+1 < 600) {
                    redY += 1;
                    redButton.translate(0, 1);
                }
            }
            if(greenButton.isSelected()) {
                if (greenY+1 < 600) {
                    greenY += 1;
                    greenButton.translate(0, 1);
                }
            }
            if(blueButton.isSelected()) {
                if (blueY+1 < 600) {
                    blueY 
                    += 1;
                    blueButton.translate(0, 1);
                }
            }
        }

        /********* MOUSE EVENT HANDLING *********/
        Vector2d position = MouseListener.mouse_loc_in_screen();
        // if mouse button 0 (left click) is pressed down
        if(MouseListener.isMouseButtonDown(0)){
            // if button was clicked and its on a falling edge, do stuff

            /********* PIXEL TYPE BUTTONS ***********/
            if (blankButton.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                // set falling edge
                SceneManager.get().fallingEdge = true;
                // set which pixel type to draw
                SceneManager.get().pixel_selector = 0;
                // shade this button and unshade other pixel button types
                blankButton.selected(true);
                sandButton.selected(false);
                waterButton.selected(false);
                System.out.println("SOLID SELECTED");
            }
            else if (sandButton.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                SceneManager.get().pixel_selector = 1;
                // shade this button and unshade other pixel button types
                blankButton.selected(false);
                sandButton.selected(true);
                waterButton.selected(false);
                System.out.println("SAND SELECTED");
            }
            else if (waterButton.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                SceneManager.get().pixel_selector = 2;
                // shade this button and unshade other pixel button types
                blankButton.selected(false);
                sandButton.selected(false);
                waterButton.selected(true);
                System.out.println("WATER");
            }

            /******** EDGE DETECTION IMAGE BUTTONS **********/
            else if (leftArrow.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                EdgeDetector.get().setImage(EdgeDetector.get().getImageIdx()-1);
                showImage.changeImage("images\\"+EdgeDetector.get().getCurrentImageName());
                // shade this button and unshade other pixel button types
                leftArrow.selected(true);
                rightArrow.selected(false);
                System.out.println("LEFT");
            } else if (rightArrow.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                EdgeDetector.get().setImage(EdgeDetector.get().getImageIdx()+1);
                showImage.changeImage("images\\"+EdgeDetector.get().getCurrentImageName());
                // shade this button and unshade other pixel button types
                leftArrow.selected(false);
                rightArrow.selected(true);
                System.out.println("RIGHT");
            }

            /*************** RGB SLIDER ****************/
            else if (redButton.clicked((float)MouseListener.getX(), (float)MouseListener.getY())){
                // shade this button and unshade other pixel button types
                redButton.selected(true);
                greenButton.selected(false);
                blueButton.selected(false);
                System.out.println("RED");
            }
            else if (greenButton.clicked((float)MouseListener.getX(), (float)MouseListener.getY())){
                // shade this button and unshade other pixel button types
                redButton.selected(false);
                greenButton.selected(true);
                blueButton.selected(false);
                System.out.println("GREEN");
            }
            else if (blueButton.clicked((float)MouseListener.getX(), (float)MouseListener.getY())){
                // shade this button and unshade other pixel button types
                redButton.selected(false);
                greenButton.selected(false);
                blueButton.selected(true);
                System.out.println("BLUE");
            }


            /*** CLEAR BUTTON ****/
            else if (clear.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                CellularAutomata.get().empty_curr_grid();
                // shade this button and unshade other pixel button types
                clear.selected(true);
                System.out.println("SANDBOX CLEARED");
            }
            /***** RANDOM POINTS *******/
            else if (image.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                EdgeDetector.placeRandomPixels();
                // shade this button and unshade other pixel button types
                image.selected(true);
                System.out.println("RANDOMNESSSSSS");
            }
            /********** START SIMULATION *********/
            else if (simulate.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                // shade this button and unshade other pixel button types
                simulate.selected(true);
                System.out.println("SIMULATING");
            }
            // else {
            //     SceneManager.get().fallingEdge = false;
            // }

            if(CellularAutomata.get().pos_allowed(position)){
                if(CellularAutomata.get().pos_empty(position,false)){
                    CellularAutomata.get().add_pixel(SceneManager.get().create_selected_pixel(position),position,false);
                }
            }
        }
        else {
            SceneManager.get().fallingEdge = false;
            leftArrow.selected(false);
            rightArrow.selected(false);
            clear.selected(false);
            image.selected(false);
            simulate.selected(false);
        }
    }

    //------------------------------------------------------------------------------------------
    //Menu selector functions
    //------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------
    //Pixel functions
    //------------------------------------------------------------------------------------------
    private Pixel create_selected_pixel(Vector2d position){
        Pixel pixel;
        switch (SceneManager.get().pixel_selector) {
            case 0:
                pixel = new Blank_pixel(position);
                pixel.set_pixel_color((int)((1.0f-(redY-250)/400f)*256),
                                    (int)((1.0f-(greenY-250)/(400f))*256),
                                    (int)((1.0f-(blueY-250)/(400f))*256));
                return pixel;
            case 1:
                pixel = new Sand_pixel(position);
                // pixel.set_pixel_color((int)(1.0f-(redY-250)/(400f))*256,
                //                     (int)(1.0f-(greenY-250)/(400f))*256,
                //                     (int)(1.0f-(blueY-250)/(400f))*256);
                return pixel;
            case 2:
                pixel = new Water_pixel(position);
                // pixel.set_pixel_color((int)(1.0f-(redY-250)/(400f))*256,
                //                     (int)(1.0f-(greenY-250)/(400f))*256,
                //                     (int)(1.0f-(blueY-250)/(400f))*256);
                return pixel;
            default:
            pixel = new Blank_pixel(position);
            pixel.set_pixel_color((int)((1.0f-(redY-250)/400f)*256),
                                (int)((1.0f-(greenY-250)/(400f))*256),
                                (int)((1.0f-(blueY-250)/(400f))*256));
            return pixel;
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

    //------------------------------------------------------------------------------------------
    //Other functions
    //------------------------------------------------------------------------------------------
    private void fps(){
        ++frame_counter;
        end_time = System.nanoTime();
        if((end_time-start_time) >= 1000000000){
            System.out.println("FPS: " + frame_counter);
            start_time = System.nanoTime();
            end_time = 0;
            frame_counter = 0;
        }
    }
    
}
