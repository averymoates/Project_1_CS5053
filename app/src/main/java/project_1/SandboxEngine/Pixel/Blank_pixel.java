package project_1.SandboxEngine.Pixel;

import static org.lwjgl.opengl.GL11.glColor3f;
import org.joml.Vector2d;
import project_1.SandboxEngine.Utilities.ShapeMaker;

/**
 * Author: Avery Moates
 * Date:   3/11/2024
 * 
 * Purpose: Blank pixel. Use this class as a example on how to create new kinds of pixels
 */
public class Blank_pixel extends Pixel{

    public Blank_pixel(Vector2d position){
        super(0, 0, 0, "Blank", 0, position);
    }

    @Override
    public void draw() {
        glColor3f(this.get_r(), this.get_g(), this.get_b());
        ShapeMaker.fill_square(this.get_position().x*this.get_sqaure_size(), this.get_position().y*this.get_sqaure_size(), this.get_sqaure_size());
    }

    @Override
    public void update() {
        
    }
    
}
