package project_1;

import org.lwjgl.system.Configuration;

import project_1.SandboxEngine.Window;
import org.opencv.core.Core;
import java.lang.reflect.Field;

/**
 * Author:  Avery Moates
 * Data:    2/17/2024
 */
public class App {
    static {
        try {
            // Set the java.library.path to the directory containing the DLL
            System.setProperty("java.library.path", "C:\\Users\\james\\Downloads\\opencv\\build\\java\\x64");

            // Update the system paths
            Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);

            // Load the OpenCV library
            // System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.loadLibrary("opencv_java340");
            System.out.println("NATIVE IMPORTED!!");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load opencv native library", e);
        }
    }

    

    public static void main(String[] args){
        // Configure LWJGL to suppress error messages
        Configuration.DEBUG.set(false);
        

        Window window = Window.create();
        window.run();
    }
}