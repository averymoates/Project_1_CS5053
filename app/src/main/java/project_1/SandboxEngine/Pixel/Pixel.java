package project_1.SandboxEngine.Pixel;

import org.joml.Vector2d;

abstract public class Pixel {

    //Values that will be used to color the pixel
    protected float red;
    protected float green;
    protected float blue;

    //Size of each of the squares. This need to stay the for all pixels
    protected double square_size = 20.0;

    //A way to identify each of the different pixels
    protected String name;
    protected int ID_name;

    //I may not use this.
    protected Vector2d position;

    public Pixel(int red, int green, int blue, String name, int ID, Vector2d position){
        this.red = (float)red/256.0f;
        this.green = (float)green/256.0f;
        this.blue = (float)blue/256.0f;

        this.name = name;
        this.ID_name = ID;

        //I may not use this
        this.position = new Vector2d(position.x,position.y);
    }

    //I may not use this
    public Vector2d get_position(){
        return new Vector2d(this.position.x,this.position.y);
    }

    public double get_sqaure_size(){
        return this.square_size;
    }

    public String get_name(){
        return this.name;
    }

    public int get_ID(){
        return this.ID_name;
    }

    public float get_r(){
        return this.red;
    }

    public float get_g(){
        return this.green;
    }
    
    public float get_b(){
        return this.blue;
    }

    public abstract void draw();

    public abstract void update();
}
