package project_1.SandboxEngine.Pixel.Element;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;

public class Element extends Pixel{

    protected double weight;
    protected int time_step;

    public Element(PixelType name, int id, double weight, Vector2d position){
        super(name, id, position);
        this.weight = weight;
        time_step = 0;
    }
    
}
