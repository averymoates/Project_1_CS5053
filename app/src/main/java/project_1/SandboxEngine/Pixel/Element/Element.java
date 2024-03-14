package project_1.SandboxEngine.Pixel.Element;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;

public class Element extends Pixel{

    protected double weight;
    protected int time_step;

    public Element(int r, int g, int b, String name, int id, double weight, Vector2d position){
        super(r, g, b, name, id, position);
        this.weight = weight;
        time_step = 0;
    }
    
}
