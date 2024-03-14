package project_1.SandboxEngine.Pixel;

import static org.lwjgl.opengl.GL11.glColor3f;

import org.joml.Vector2d;

import project_1.SandboxEngine.Scene.SceneManager;
import project_1.SandboxEngine.Utilities.ShapeMaker;

/**
 * Author: Avery Moates
 * Date:   3/11/2024
 * 
 * Purpose: The sand pixel. Falls down.
 */
public class Sand_pixel extends Pixel{

    private int time_step;
    private static int fall_rate = 10;

    public Sand_pixel(Vector2d position){
        super(217, 180, 78, "Sand", 1, position);
        this.time_step = 0;
    }

    @Override
    public void draw() {
        glColor3f(this.get_r(), this.get_g(), this.get_b());
        ShapeMaker.fill_square(this.get_position().x*this.get_sqaure_size(), this.get_position().y*this.get_sqaure_size(), this.get_sqaure_size());
    }

    @Override
    public void update() {
        ++time_step;
        if(time_step == fall_rate){
            time_step = 0;
            double new_y = this.position.y + 1;

            //Check if there is a pixel below this pixel
            for(Pixel pixel: SceneManager.get_pixels()){
                if(pixel.get_position().x == this.position.x){
                    if(pixel.get_position().y == new_y){
                        return;
                    }
                }
            }

            //If there isn't, then move this pixel down one pixel
            this.position.y = new_y;
            if(this.position.y > Math.floor(SceneManager.get_height()/this.get_sqaure_size())){
                this.position.y = Math.floor(SceneManager.get_height()/this.get_sqaure_size());
            }
        }
    }
    
}

