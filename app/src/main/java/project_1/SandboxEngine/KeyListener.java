package project_1.SandboxEngine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Avery Moates
 * Date: 3/11/2024
 */
public class KeyListener {
    private static KeyListener instance = null;
    private boolean keyPressed[] = new boolean[350];
    private int lastKey = 0;
    private boolean fallingEdge = false;


    private boolean keyPressDown[] = new boolean[350];


    private KeyListener(){

    }

    public static KeyListener get(){
        if(KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        // System.out.println(KeyListener.get().fallingEdge);
        if(key < KeyListener.get().keyPressed.length){
            if(action == GLFW_PRESS){
                // System.out.println(key + " " + KeyListener.get().lastKey);

                KeyListener.get().keyPressed[key] = true;
                KeyListener.get().fallingEdge = true;
                
            }
            else if(action == GLFW_RELEASE){
                KeyListener.get().keyPressed[key] = false;
            }
            else {
                KeyListener.get().keyPressDown[key] = false;
            }
        }
        else{
            System.out.println("Key press [" + key + "] is not accounted for.");
        }
    }

    /**
     * Function to call to see if a certain keypress is down.
     * 
     * @param keyCode
     * @return
     */
    public static boolean isKeyPressed(int keyCode){
        if(keyCode < KeyListener.get().keyPressed.length){
            return KeyListener.get().keyPressed[keyCode];
        }
        else{
            System.out.println("Key press [" + keyCode + "] is not accounted for.");
            return false;
        }
    }

    // tapping a button registers as multiple key events. This function basically gets falling edge of button
    public static boolean isKeyTapped(int keyCode) {
        if(keyCode < KeyListener.get().keyPressed.length){
            if (KeyListener.get().fallingEdge && KeyListener.get().keyPressed[keyCode]) {
                KeyListener.get().fallingEdge = false;
                return true;
            }
            return false;
        }
        else{
            System.out.println("Key press [" + keyCode + "] is not accounted for.");
            return false;
        }
    }

    /**
     * Function to call if you want something to only happen once per keypress
     * 
     * @param keyCode
     * @return
     */
    public static boolean isKeyJustPressed(int keyCode){
        if(keyCode < KeyListener.get().keyPressed.length){
            KeyListener.get();
            if(KeyListener.isKeyPressed(keyCode)){
                if(KeyListener.get().keyPressDown[keyCode] == false){
                    KeyListener.get().keyPressDown[keyCode] = true;
                    return true;
                }
                return false;
            }
            return false;
        }
        else{
            System.out.println("Key press [" + keyCode + "] is not accounted for.");
            return false;
        }
    }

    /**
     * Debug function to test KeyListener's callback functions
     */
    public static void testSpaceKeyFunction(){
        System.out.println("Space key pressed:\t" + KeyListener.get().keyPressed[GLFW_KEY_SPACE] + "\n");
    }
    
}
