package project_1.SandboxEngine.Utilities;

import static org.lwjgl.opengl.GL11.glColor3f;

public class ColorPicker {

    public static void set_color(int red, int green, int blue){
        glColor3f(red/256.0f, green/256.0f, blue/256.0f);
    }

    public static void set_debug_color(){
        set_color(219, 7, 7);
    }
    
}
