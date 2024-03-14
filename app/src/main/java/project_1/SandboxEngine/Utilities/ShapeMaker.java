package project_1.SandboxEngine.Utilities;

import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

/**
 * Author: Avery Moates
 * Date: 3/11/2024
 * 
 * Purpose: Class of instructions on how to draw certain basic shapes
 */
public class ShapeMaker {


    public static void fill_square(double x, double y, double width){
        glBegin(GL_POLYGON);
        glVertex2d(x, y);
        glVertex2d(x+width, y);
        glVertex2d(x+width, y+width);
        glVertex2d(x, y+width);
        glEnd();
        
    }

    public static void edge_square(double x, double y, double width){
        glBegin(GL_LINE_LOOP);
        glVertex2d(x, y);
        glVertex2d(x+width, y);
        glVertex2d(x+width, y+width);
        glVertex2d(x, y+width);
        glEnd();
    }


    
}
