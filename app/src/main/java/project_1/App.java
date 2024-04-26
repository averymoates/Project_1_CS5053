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
            System.setProperty("java.library.path", "native");

            // Update the system paths
            Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);

            // Load the OpenCV library
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                System.loadLibrary("opencv_java342");
            } else {
                System.out.println("This app is not avialable on this operating system.");
                throw new Exception();
            }

            // System.loadLibrary("opencv_java342");
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