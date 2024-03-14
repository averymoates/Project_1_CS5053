package project_1.SandboxEngine.Pixel.Special;

import static org.lwjgl.opengl.GL11.glColor3f;
import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Utilities.ShapeMaker;

/**
 * Author: Avery Moates
 * Date:   3/11/2024
 * 
 * Purpose: Black Pixel
 */
public class Blank_pixel extends Special{

    public Blank_pixel(Vector2d position){
        super(0,0,0,"Blank",0,position);
        this.needs_updating = false;
    }

    
}
