package project_1.SandboxEngine.Pixel;

import static org.lwjgl.opengl.GL11.glColor3f;

import org.joml.Vector2d;

import project_1.SandboxEngine.Scene.SceneManager;
import project_1.SandboxEngine.Utilities.ShapeMaker;

public class Blank_pixel extends Pixel{

    private int time_step;
    private static int fall_rate = 10;

    public Blank_pixel(Vector2d position){
        super(0, 0, 0, "Blank", 0, position);
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
            this.position.y += 1;
            if(this.position.y > Math.floor(SceneManager.get_height()/this.get_sqaure_size())){
                this.position.y = Math.floor(SceneManager.get_height()/this.get_sqaure_size());
            }
        }
    }
    
}
