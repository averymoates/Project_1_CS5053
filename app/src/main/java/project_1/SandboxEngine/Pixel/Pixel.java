package project_1.SandboxEngine.Pixel;

import org.joml.Vector2d;

import project_1.SandboxEngine.Scene.SceneManager;

/**
 * Author: Avery Moates
 * Date:   3/11/2024
 * 
 * Purpose: Abstract class to group all the pixels together. Extend this class to create different kinds of pixels
 */
abstract public class Pixel {

    //Values that will be used to color the pixel
    protected float red;
    protected float green;
    protected float blue;

    //Size of each of the squares. This need to stay the for all pixels
    protected double square_size = SceneManager.get_square_size();

    //A way to identify each of the different pixels
    protected String name;
    protected int ID_name;

    //Position where the pixel is
    protected Vector2d position;

    /**
     * Abstract Constructor. Create a pixel object
     * 
     * @param red       Red value ranging from 0-255
     * @param green     Green value ranging from 0-255
     * @param blue      Blue value ranging from 0-255
     * @param name      Single String descriter name
     * @param ID        Unique int for a specific pixel
     * @param position  Vector2D
     */
    public Pixel(int red, int green, int blue, String name, int ID, Vector2d position){
        this.red = (float)red/256.0f;
        this.green = (float)green/256.0f;
        this.blue = (float)blue/256.0f;

        this.name = name;
        this.ID_name = ID;

        //I may not use this
        this.position = new Vector2d(position.x,position.y);
    }

    //------------------------------------------------------------------------------------------
    //Getter functions
    //------------------------------------------------------------------------------------------
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

    //------------------------------------------------------------------------------------------
    //Abstract functions
    //------------------------------------------------------------------------------------------
    public abstract void draw();

    public abstract void update();
}
