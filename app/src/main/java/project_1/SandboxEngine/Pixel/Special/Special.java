package project_1.SandboxEngine.Pixel.Special;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;

public class Special extends Pixel{

    //Static variables
    protected static boolean animate;
    protected static int animation_counter;

    //Other variables
    protected boolean is_alive;

    public Special(PixelType name, int ID, Vector2d position, boolean is_alive) {
        super(name, ID, position);
        this.is_alive = is_alive;
    }

    //Static method that will allow the user to stop, start, speed up speed down the special automata animations
    public static void toggle_animation(){
        animate = !animate;
        animation_counter = 1;
    }   

    public static void set_animation(boolean state){
        animate = state;
        animation_counter = 1;
    }

    public static boolean is_animating(){
        return animate;
    }

    public static void increment_counter(){
        if(animate){
            if(animation_counter == 61){
                animation_counter = 1;
            }
            else{
                ++animation_counter; 
            }
        }
        else{
            animation_counter = 1;
        }
    }

    public static int get_counter(){
        return animation_counter;
    }
    
}
