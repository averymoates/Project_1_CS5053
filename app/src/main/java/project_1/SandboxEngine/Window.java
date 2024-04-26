package project_1.SandboxEngine;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import java.util.*;

import org.joml.Vector2d;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import project_1.SandboxEngine.Scene.CellularAutomata;
import project_1.SandboxEngine.Scene.SceneManager;
import project_1.SandboxEngine.Utilities.ShapeMaker;

/**
 * Author:  Avery Moates
 * Date:    2/17/2024
 */
public class Window {

    private int width, height;
    private String title;
    private long glfwWindow;

    private int[] w = null;
    private int[] h = null;

    // Keep track of the aspect ratio
    private float aspectRatio = 1.0f;

    private static Window window = null;

    // Callback to enforce the window aspect ratio
    private GLFWWindowSizeCallback windowSizeCallback;

    private Window(){

        this.width = 1000;
        this.height = 1000;

        this.title = "SandBox Engine";

    }

    public static Window create(){
        if(Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        if (windowSizeCallback != null) {
            windowSizeCallback.free();
        }

        //Free the memory
        glfwFreeCallbacks(this.glfwWindow);
        glfwDestroyWindow(this.glfwWindow);

        //Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
        //Set up error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        System.out.println("Sandbox Simulator. CS 5053 Project 1");

        //Create the window
        this.glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(this.glfwWindow == NULL){
            throw new IllegalStateException("Failed to create GLFW Window.");
        }

         // Configure GLFW to be resizable
         glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

         // Create the GLFW window
         this.glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
         if (this.glfwWindow == NULL) {
             throw new RuntimeException("Failed to create the GLFW window");
         }
 
         // Create a window resize callback
         windowSizeCallback = new GLFWWindowSizeCallback() {
             @Override
             public void invoke(long window, int width, int height) {
                 // Decide the new size for the window
                 int newSize = Math.min(width, height);
 
                 // Update the GLFW window
                 glfwSetWindowSize(window, newSize, newSize);
 
                 // Update the OpenGL viewport
                 glViewport(0, 0, newSize, newSize);
             }
         };
 
         // Set the resize callback
         glfwSetWindowSizeCallback(this.glfwWindow, windowSizeCallback);



        //Set mouse callback functions
        glfwSetCursorPosCallback(this.glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(this.glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(this.glfwWindow, MouseListener::mouseScrollCallback);

        //Set Keyboard callback functions
        glfwSetKeyCallback(this.glfwWindow, KeyListener::keyCallback);

        //Make the OpenGL context current
        glfwMakeContextCurrent(this.glfwWindow);

        //Enable v-sync
        glfwSwapInterval(GLFW_TRUE);

        //Make the window visible
        glfwShowWindow(this.glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
        GL.createCapabilities();

        //---------------------------------------------------------------------------------------
        //This is where I am going to set up stuff for things that only need to called once
        //---------------------------------------------------------------------------------------
        w = new int[1];
        h = new int[1];
        SceneManager.set_width(this.width);
        SceneManager.set_height(this.height);
        SceneManager.get().init();
        
    }
    
    public void loop(){
        while(!glfwWindowShouldClose(this.glfwWindow)){
           
            //Poll key events
            glfwPollEvents();
            // MouseListener.testMouseFunctions();
            // KeyListener.testSpaceKeyFunction();
            set_up_screen_coords(false);

            //Set the background to a gray color
            glClearColor(173.0f/256.0f,172.0f/256.0f, 166.0f/256.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            SceneManager.get().update();
            SceneManager.get().draw();

            draw_square_cursor();

            glfwSwapBuffers(this.glfwWindow);
        }

    }

    private void set_up_screen_coords(boolean display){
        glfwGetFramebufferSize(glfwWindow, w, h);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glViewport(0, 0, w[0], h[0]);
        glOrtho(0, w[0], h[0], 0, -1.0, 1.0);

        SceneManager.set_width(w[0]);
        SceneManager.set_height(h[0]);

        if(display){
            System.out.println("Width is: " + w[0]);
            System.out.println("Height is: " + h[0] + "\n");
        }
    }

    private void draw_square_cursor(){
        //Draw a square to help the user know where they are placing a pixel
        double x_value = MouseListener.mouse_loc_in_screen().x*CellularAutomata.get().get_square_size();
        double y_value = MouseListener.mouse_loc_in_screen().y*CellularAutomata.get().get_square_size();
        glColor3f(105.0f/256.0f, 103.0f/256.0f, 99.0f/256.0f);
        ShapeMaker.fill_square(x_value, y_value, CellularAutomata.get().get_square_size());

    }
}
