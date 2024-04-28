package project_1.SandboxEngine.Scene;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import java.util.*;
import java.awt.Color;


import org.joml.Vector2d;

import javafx.scene.control.Cell;
import project_1.SandboxEngine.KeyListener;
import project_1.SandboxEngine.MouseListener;
import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.Element.Liquid.Water_pixel;
import project_1.SandboxEngine.Pixel.Element.Solid.Sand_pixel;
import project_1.SandboxEngine.Pixel.Special.Blank_pixel;
import project_1.SandboxEngine.Utilities.Button;
import project_1.SandboxEngine.Pixel.Special.Conway;
import project_1.SandboxEngine.Pixel.Special.Langston;
import project_1.SandboxEngine.Pixel.Special.Special;
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

    private static Button sandButton, waterButton, blankButton, 
                            leftArrow, rightArrow, image, eraser, 
                            redButton, greenButton, blueButton, simulate,
                            conway, langton;
    public Button showImage;

    //Change this value to change the size of all the pixels
    private double SQUARE_SIZE;

    //This will keep track of the size of the window at all times
    private int width;
    private int height;

    //Value that keeps track of what the user wants to draw to the scene
    private int pixel_selector;
    private boolean gameOfLifeMode = false;

    private long start_time;
    private long end_time;
    private int frame_counter;

    private float redY, greenY, blueY, colorYRange, colorYStart;

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
        redY=0.5f;
        greenY=0.5f;
        blueY=0.5f;
        colorYRange=0.3f*height;
        colorYStart=0.15f*height;
        SQUARE_SIZE = SceneManager.get_height()*0.75*0.01;

        sandButton = new Button(width*0.01f, height*0.8f, width*0.2f, height*0.07f, "images\\buttons\\sand.png", Color.ORANGE);
        waterButton = new Button(width*0.26f, height*0.8f, width*0.2f, height*0.07f, "images\\buttons\\water.png", Color.BLUE);
        blankButton = new Button(width*0.51f, height*0.8f, width*0.2f, height*0.07f, "images\\buttons\\solid.png", Color.GRAY);
        leftArrow = new Button(width*0.75f, height*0.8f, width*0.1f, height*0.07f, "images\\buttons\\left_arrow.png", Color.WHITE);
        rightArrow = new Button(width*0.88f, height*0.8f, width*0.1f, height*0.07f, "images\\buttons\\right_arrow.png", Color.WHITE);

        conway = new Button(width*0.01f, height*0.9f, width*0.2f, height*0.07f, "images\\buttons\\conway.png", Color.MAGENTA);
        langton = new Button(width*0.51f, height*0.9f, width*0.2f, height*0.07f, "images\\buttons\\langton.png", Color.RED);

        image = new Button(width*0.76f, height*0.9f, width*0.2f, height*0.07f, "images\\buttons\\convertimage.png", Color.WHITE);
        simulate = new Button(width*0.26f, height*0.9f, width*0.2f, height*0.07f, "images\\buttons\\simulate.png", Color.green);
        eraser = new Button(width*0.77f, height*0.05f, width*0.2f, height*0.07f, "images\\buttons\\eraser.png", Color.WHITE);
        redButton = new Button(width*0.8f, redY*colorYRange+colorYStart, width*0.02f, width*0.02f, "", Color.RED);
        greenButton = new Button(width*0.85f, greenY*colorYRange+colorYStart, width*0.02f, width*0.02f, "", Color.GREEN);
        blueButton = new Button(width*0.9f, blueY*colorYRange+colorYStart, width*0.02f, width*0.02f, "", Color.BLUE);
        

        showImage = new Button(width*0.785f, height*0.62f, width*0.15f, height*0.15f, "images\\0blank.png", Color.WHITE);

        blankButton.selected(true);
    }
    public Button getImage() {
        return this.showImage;
    }

    public void update(){
        fps();
        SceneManager.get().pull_events();
        SQUARE_SIZE = SceneManager.get_width()*0.75*0.01;
        CellularAutomata.get().update();
    }

    
    /**
     * Function to draw everything onto the scene
     */
    public void draw(){
        SQUARE_SIZE = SceneManager.get_width()*0.75*0.01;
        // start with a white canvas
        glColor3f(1f,1f,1f);
        glBegin(GL_POLYGON);
            glVertex2f(0, 0);
            glVertex2f(width*0.75f, 0);
            glVertex2f(width*0.75f, height*0.75f);
            glVertex2f(0, height*0.75f);
        glEnd();
        // System.out.println(width+" "+height);

        // outline sandbox environment
        glColor3f(0f,0f,0f);
        glLineWidth(5.0f);
        glBegin(GL_LINE_LOOP);
            glVertex2f(0, 0);
            glVertex2f(width*0.75f, 0);
            glVertex2f(width*0.75f, height*0.75f);
            glVertex2f(0, height*0.75f);
        glEnd();

        /******* COLOR SLIDER ********/
        // 3 color slider lines
        glColor3f(0f,0f,0f);
        glLineWidth(5.0f);
        glBegin(GL_LINES);
            glVertex2f(width*0.81f, height*0.15f);
            glVertex2f(width*0.81f, height*0.45f);
        glEnd();

        glColor3f(0f,0f,0f);
        glLineWidth(5.0f);
        glBegin(GL_LINES);
            glVertex2f(width*0.86f, height*0.15f);
            glVertex2f(width*0.86f, height*0.45f);
        glEnd();

        glColor3f(0f,0f,0f);
        glLineWidth(5.0f);
        glBegin(GL_LINES);
            glVertex2f(width*0.91f, height*0.15f);
            glVertex2f(width*0.91f, height*0.45f);
        glEnd();

        // display current color of sliders
        // System.out.println(1.0f-redY, 1.0f-greenY, 1.0f-blueY);
        glColor3f(1.0f-redY, 1.0f-greenY, 1.0f-blueY);
        glBegin(GL_POLYGON);
            glVertex2f(width*0.81f, height*0.48f);
            glVertex2f(width*0.91f, height*0.48f);
            glVertex2f(width*0.91f, height*0.58f);
            glVertex2f(width*0.81f, height*0.58f);
        glEnd();
        glColor3f(0f,0f,0f);
        glLineWidth(5.0f);
        glBegin(GL_LINE_LOOP);
        glVertex2f(width*0.81f, height*0.48f);
        glVertex2f(width*0.91f, height*0.48f);
        glVertex2f(width*0.91f, height*0.58f);
        glVertex2f(width*0.81f, height*0.58f);
        glEnd();

        CellularAutomata.get().draw();

        // continually display buttons
        sandButton.render(width*0.01f, height*0.8f, width*0.2f, height*0.07f);
        waterButton.render(width*0.26f, height*0.8f, width*0.2f, height*0.07f); 
        blankButton.render(width*0.51f, height*0.8f, width*0.2f, height*0.07f);

        leftArrow.render(width*0.75f, height*0.8f, width*0.1f, height*0.07f);
        rightArrow.render(width*0.88f, height*0.8f, width*0.1f, height*0.07f);
        image.render(width*0.74f, height*0.9f, width*0.25f, height*0.07f);

        conway.render(width*0.01f, height*0.9f, width*0.2f, height*0.07f);
        simulate.render(width*0.26f, height*0.9f, width*0.2f, height*0.07f);
        langton.render(width*0.51f, height*0.9f, width*0.2f, height*0.07f);

        eraser.render(width*0.77f, height*0.05f, width*0.2f, height*0.07f);

        colorYRange=0.3f*height;
        colorYStart=0.15f*height;
        redButton.render(width*0.8f, redY*colorYRange+colorYStart, width*0.02f, width*0.02f);
        greenButton.render(width*0.85f, greenY*colorYRange+colorYStart, width*0.02f, width*0.02f);
        blueButton.render(width*0.9f, blueY*colorYRange+colorYStart, width*0.02f, width*0.02f);

        showImage.render(width*0.785f, height*0.62f, width*0.15f, height*0.15f);
        
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
        else if(KeyListener.isKeyPressed(GLFW_KEY_SEMICOLON)) { // decrease thresh1
            EdgeDetector.get().updateEdgeThresholds(-10, 0);
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_APOSTROPHE)) { // increase thresh1
            EdgeDetector.get().updateEdgeThresholds(10, 0);
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_LEFT_BRACKET)) { // decrease thresh2
            EdgeDetector.get().updateEdgeThresholds(0, -10);
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_RIGHT_BRACKET)) { // decrease thresh2
            EdgeDetector.get().updateEdgeThresholds(0, 10);
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_UP)) { // move selected slider UP
            if(redButton.isSelected()) {
                if (redY-0.005f >= 0.0f) {
                    redY -= 0.005f;
                    redButton.translate(0, (int)(-0.005f*colorYRange));
                }
            }
            if(greenButton.isSelected()) {
                if (greenY-0.005f >= 0.0f) {
                    greenY -= 0.005f;
                    greenButton.translate(0, (int)(-0.005f*colorYRange));
                }
            }
            if(blueButton.isSelected()) {
                if (blueY-0.005f >= 0.0f) {
                    blueY -= 0.005f;
                    blueButton.translate(0, (int)(-0.005f*colorYRange));
                }
            }
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_DOWN)) { // move selected slider DOWN
            if(redButton.isSelected()) {
                if (redY+0.005f <= 1f) {
                    redY += 0.005f;
                    redButton.translate(0, (int)(0.005f*colorYRange));
                }
            }
            if(greenButton.isSelected()) {
                if (greenY+0.005f <= 1f) {
                    greenY +=  0.005f;
                    greenButton.translate(0, (int)(0.005f*colorYRange));
                }
            }
            if(blueButton.isSelected()) {
                if (blueY+0.005f <= 1f) {
                    blueY +=  0.005f;
                    blueButton.translate(0, (int)(0.005f*colorYRange));
                }
            }
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_C)){
            SceneManager.get().pixel_selector = 3;
        }
        else if(KeyListener.isKeyPressed(GLFW_KEY_L)){
            SceneManager.get().pixel_selector = 4;
        }
        else if(KeyListener.isKeyJustPressed(GLFW_KEY_A)){
            Special.toggle_animation();
            System.out.println("Animation is: " + Conway.is_animating());
        }

        // //Conway game of life setup on C.
        // else if (KeyListener.isKeyPressed(GLFW_KEY_C)){
        //     if (!gameOfLifeMode)
        //         CellularAutomata.get().convertToGameOfLife();
        //     SceneManager.get().pixel_selector = 2;
        //     setGameOfLifeMode(true);
        // }

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
                conway.selected(false);
                langton.selected(false);
                eraser.selected(false);
                System.out.println("SOLID SELECTED");
            }
            else if (sandButton.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                SceneManager.get().pixel_selector = 1;
                // shade this button and unshade other pixel button types
                blankButton.selected(false);
                sandButton.selected(true);
                waterButton.selected(false);
                conway.selected(false);
                langton.selected(false);
                eraser.selected(false);
                System.out.println("SAND SELECTED");
            }
            else if (waterButton.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                SceneManager.get().pixel_selector = 2;
                // shade this button and unshade other pixel button types
                blankButton.selected(false);
                sandButton.selected(false);
                waterButton.selected(true);
                conway.selected(false);
                langton.selected(false);
                eraser.selected(false);
                System.out.println("WATER SELECTED");
            }
            else if (conway.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                SceneManager.get().pixel_selector = 3;
                // shade this button and unshade other pixel button types
                blankButton.selected(false);
                sandButton.selected(false);
                waterButton.selected(false);
                conway.selected(true);
                langton.selected(false);
                eraser.selected(false);
                System.out.println("CONWAY PIXEL");
            }
            else if (langton.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                SceneManager.get().pixel_selector = 4;
                // shade this button and unshade other pixel button types
                blankButton.selected(false);
                sandButton.selected(false);
                waterButton.selected(false);
                conway.selected(false);
                langton.selected(true);
                eraser.selected(false);
                System.out.println("LANGTON PIXEL");
            }
            /*** ERASER BUTTON ****/
            else if (eraser.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                // shade this button and unshade other pixel button types
                blankButton.selected(false);
                sandButton.selected(false);
                waterButton.selected(false);
                conway.selected(false);
                langton.selected(false);
                eraser.selected(true);
                System.out.println("SANDBOX eraserED");
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


            
            /***** CONVERT EDGE IMAGE TO PIXEL *******/
            else if (image.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                CellularAutomata.get().convertToGameOfLife(SceneManager.get().pixel_selector);
                // EdgeDetector.convertToGameOfLife(SceneManager.get().pixel_selector);
                // shade this button and unshade other pixel button types
                image.selected(true);
                System.out.println("RANDOMNESSSSSS");
            }
            /********** START SIMULATION *********/
            else if (simulate.clicked((float)MouseListener.getX(), (float)MouseListener.getY()) && !SceneManager.get().fallingEdge){
                SceneManager.get().fallingEdge = true;
                // shade this button and unshade other pixel button types
                simulate.selected(!simulate.isSelected());
                System.out.println("SIMULATING");
                // CellularAutomata.get().convertToGameOfLife();
                Conway.toggle_animation();
            }


            if(CellularAutomata.get().pos_allowed(position)){
                // System.out.println("MOUSE: "+position.x+", "+position.y);
                // eraser
                if (eraser.isSelected()) {
                    CellularAutomata.get().remove_pixel(position, false);
                }
                // add pixel if eraser not selected
                else if(CellularAutomata.get().pos_empty(position, false)){
                    CellularAutomata.get().add_pixel(SceneManager.get().create_selected_pixel(position), position, false);
                }

            }

        }
        else {
            SceneManager.get().fallingEdge = false;
            leftArrow.selected(false);
            rightArrow.selected(false);
            // eraser.selected(false);
            image.selected(false);
            // simulate.selected(false);
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
                pixel.set_pixel_color((int)((1.0f-redY)*256), 
                                        (int)((1.0f-greenY)*256), 
                                        (int)((1.0f-blueY)*256));
                return pixel;
            case 1:
                pixel = new Sand_pixel(position);
                return pixel;
            case 2:
                pixel = new Water_pixel(position);
                return pixel;
            case 3:
                return new Conway(position, true);

            case 4:
                return new Langston(position, true);
        
            default:
            pixel = new Blank_pixel(position);
            pixel.set_pixel_color((int)((1.0f-redY)*256), 
                                    (int)((1.0f-greenY)*256), 
                                    (int)((1.0f-blueY)*256));
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

    public void setGameOfLifeMode(boolean mode){
        this.gameOfLifeMode = mode;
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

    public boolean getGameOfLifeMode(){
        return this.gameOfLifeMode;
    }

    //------------------------------------------------------------------------------------------
    //Other functions
    //------------------------------------------------------------------------------------------
    private void fps(){
        ++frame_counter;
        end_time = System.nanoTime();
        if((end_time-start_time) >= 1000000000){
            //System.out.println("FPS: " + frame_counter);
            start_time = System.nanoTime();
            end_time = 0;
            frame_counter = 0;
        }
    }
    
}
