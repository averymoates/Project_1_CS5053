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

    public static void set_counter(int value){
        counter = value;
    }
    
}
