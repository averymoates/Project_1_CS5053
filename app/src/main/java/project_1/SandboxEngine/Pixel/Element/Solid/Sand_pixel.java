package project_1.SandboxEngine.Pixel.Element.Solid;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.PixelType;

/**
 * Author: Avery Moates
 * Date:   3/11/2024
 * 
 * Purpose: The sand pixel. 
 */
public class Sand_pixel extends Moveable_Solid{

    public Sand_pixel(Vector2d position){
        super(PixelType.SAND, 1, 2, 5, position);
        this.set_random_sand_color();
        this.time_step = 0;
    }

    private void set_random_sand_color(){
        double value = Math.random();

        if(value > 0.8){
            this.red = 199.0f/256.0f;
            this.green = 163.0f/256.0f;
            this.blue = 74.0f/256.0f;
            return;
        }
        else if(value > 0.6){
            this.red = 181.0f/256.0f;
            this.green = 158.0f/256.0f;
            this.blue = 100.0f/256.0f;
            return;
        }
        else if(value > 0.4){
            this.red = 196.0f/256.0f;
            this.green = 156.0f/256.0f;
            this.blue = 55.0f/256.0f;
            return;
        }
        else if(value > 0.2){
            this.red = 199.0f/256.0f;
            this.green = 162.0f/256.0f;
            this.blue = 70.0f/256.0f;
            return;
        }
        else{
            this.red = 217.0f/256.0f;
            this.green = 180.0f/256.0f;
            this.blue = 78.0f/256.0f;
            return;
        }
    }
    
}
