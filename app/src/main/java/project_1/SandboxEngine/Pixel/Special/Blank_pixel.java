package project_1.SandboxEngine.Pixel.Special;

import org.joml.Vector2d;
import project_1.SandboxEngine.Pixel.PixelType;

/**
 * Author: Avery Moates
 * Date:   3/11/2024
 * 
 * Purpose: Black Pixel
 */
public class Blank_pixel extends Special{

    public Blank_pixel(Vector2d position){
        super(PixelType.BLANK,0,position);
    }

    
}
