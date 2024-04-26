package project_1.SandboxEngine.Pixel.Element;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;

public class Element extends Pixel{

    protected double weight;
    protected int time_step;
    protected static int counter;

    public Element(PixelType name, int id, double weight, Vector2d position){
        super(name, id, position);
        this.weight = weight;
    }

    /**
     * Function to set the static counter value for all elements
     * 
     * @param value
     */
    public static void set_counter(int value){
        counter = value;
    }

    /**
     * Function to incement the static counter
     */
    public static void increment_counter(){
        if(counter == 60){
            counter = 0;
        }
        else{
            ++counter;
        }
    }

    /**
     * Function to get the value of the static counter
     * @return
     */
    public static int get_counter(){
        return counter;
    }
    
}
