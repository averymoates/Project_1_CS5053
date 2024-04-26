package project_1.SandboxEngine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.joml.Vector2d;

import project_1.SandboxEngine.Scene.CellularAutomata;
import project_1.SandboxEngine.Scene.SceneManager;

/**
 * Author:  Avery Moates
 * Date:    2/17/2024
 */
public class MouseListener {

    private static MouseListener instance = null;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;
    
    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    /**
     * Create a single instance of the MouseListener object
     * 
     * @return MouseListener Object
     */
    public static MouseListener get(){
        if(MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    /**
     * Function to handle what happens if the mouse moves
     * 
     * @param window    The pointer address of the GLFW window
     * @param xpos  new x position
     * @param ypos  new y position
     */
    public static void mousePosCallback(long window, double xpos, double ypos){
        MouseListener.get().lastX = MouseListener.get().xPos;
        MouseListener.get().lastY = MouseListener.get().yPos;
        // System.out.println(xpos+" "+ypos);

        //Update the new positions
        MouseListener.get().xPos = xpos;
        MouseListener.get().yPos = ypos;

        //Check dragging
        MouseListener.get().isDragging = MouseListener.get().mouseButtonPressed[0] || MouseListener.get().mouseButtonPressed[1] || MouseListener.get().mouseButtonPressed[2];
    }

    /**
     * Function to handle what happens if a button is pressed on the mouse
     * 
     * @param window    The pointer address of the GLFW window
     * @param button    GLFW value for a button
     * @param action    GLFW actions that occurs
     * @param mods      Special key combos
     */
    public static void mouseButtonCallback(long window, int button, int action, int mods){
        //If the button value is one of the three buttons on the mouse
        if(button < MouseListener.get().mouseButtonPressed.length){
            if(action == GLFW_PRESS){
                MouseListener.get().mouseButtonPressed[button] = true;
            }
            else if(action == GLFW_RELEASE){
                MouseListener.get().mouseButtonPressed[button] = false;
                MouseListener.get().isDragging = false;
            }
        }
        //Else do nothing
        else{
            System.out.println("Mouse button [" + button + "] is not used");
        }
        
    }

    /**
     * Function to handle what happens if the scroll wheel changes
     * 
     * @param window    The pointer address of the GLFW window  
     * @param xOffset   New x scroll position
     * @param yOffset   new y scroll position
     */
    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        MouseListener.get().scrollX = xOffset;
        MouseListener.get().scrollY = yOffset;
    }

    //------------------------------------------------------------------------------------------
    //Getter functions
    //------------------------------------------------------------------------------------------
    public static double getX(){
        return MouseListener.get().xPos;
    }

    public static double getY(){
        return MouseListener.get().yPos;
    }
    
    public static double getDx(){
        return MouseListener.get().lastX - MouseListener.get().xPos;
    }

    public static double getDy(){
        return MouseListener.get().lastY - MouseListener.get().yPos;
    }

    public static double getScrollX(){
        return MouseListener.get().scrollX;
    }

    public static double getScrollY(){
        return MouseListener.get().scrollY;
    }

    public static boolean isDragging(){
        return MouseListener.get().isDragging;
    }

    /**
     * Function to check which button is pressed on the mouse/cursor
     * 
     * @param button    Button value
     * @return          True if that specific button is pressed
     */
    public static boolean isMouseButtonDown(int button){
        if(button < MouseListener.get().mouseButtonPressed.length){
            return MouseListener.get().mouseButtonPressed[button];
        }
        else{
            System.out.println("Mouse button [" + button + "] is not used");
            return false;
        }
    }

    /**
     * Function that converts the mouse/cursor screen coordinates into scene coordinates
     * 
     * @return  Vector2D
     */
    public static Vector2d mouse_loc_in_screen(){
        double new_x = Math.floor(MouseListener.getX()/CellularAutomata.get().get_square_size());
        double new_y = Math.floor(MouseListener.getY()/CellularAutomata.get().get_square_size());

        return new Vector2d(new_x,new_y);
    }

    /**
     * Debug function for testing MouseListener's callback functions
     */
    public static void testMouseFunctions(){
        System.out.println("X position:\t" + MouseListener.getX() + "\n" +
                           "Y position:\t" + MouseListener.getY() + "\n" +
                           "New X position:\t" + MouseListener.mouse_loc_in_screen().x + "\n" +
                           "New Y position:\t" + MouseListener.mouse_loc_in_screen().y + "\n" +
                           "Button [0] pressed:\t" + MouseListener.isMouseButtonDown(0) + "\n" +
                           "Button [1] pressed:\t" + MouseListener.isMouseButtonDown(1) + "\n" +
                           "Button [2] pressed:\t" + MouseListener.isMouseButtonDown(2) + "\n" +
                           "Is dragging:\t" + MouseListener.isDragging() + "\n");
    }
}
