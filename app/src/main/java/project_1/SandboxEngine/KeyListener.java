package project_1.SandboxEngine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Author: Avery Moates
 * Date: 3/11/2024
 */
public class KeyListener {
    private static KeyListener instance = null;
    private boolean keyPressed[] = new boolean[350];

    private KeyListener(){

    }

    public static KeyListener get(){
        if(KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(key < KeyListener.get().keyPressed.length){
            if(action == GLFW_PRESS){
                KeyListener.get().keyPressed[key] = true;
            }
            else if(action == GLFW_RELEASE){
                KeyListener.get().keyPressed[key] = false;
            }
        }
        else{
            System.out.println("Key press [" + key + "] is not accounted for.");
        }
    }

    public static boolean isKeyPressed(int keyCode){
        if(keyCode < KeyListener.get().keyPressed.length){
            return KeyListener.get().keyPressed[keyCode];
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
