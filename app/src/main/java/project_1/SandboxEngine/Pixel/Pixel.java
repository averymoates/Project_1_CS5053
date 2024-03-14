package project_1.SandboxEngine.Pixel;

import static org.lwjgl.opengl.GL11.glColor3f;

import org.joml.Vector2d;

import project_1.SandboxEngine.Scene.SceneManager;
import project_1.SandboxEngine.Utilities.ShapeMaker;

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

    //Keep track where this pixel is located
    protected Vector2d position;

    //This will be used to check if this pixel needs to be removed from the array
    protected boolean is_alive;

    //Values to make sure a pixel does not get updated multiple times during a singal frame update call
    protected boolean updated;
    protected boolean needs_updating;

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
        this.set_position(position);
        this.is_alive = true;
        this.updated = false;
        this.needs_updating = false;
    }

    //------------------------------------------------------------------------------------------
    //Getter functions
    //------------------------------------------------------------------------------------------
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

    public Vector2d get_position(){
        return this.position;
    }

    public boolean is_updated(){
        return this.updated;
    }

    public boolean needs_updating(){
        return this.needs_updating;
    }

    //------------------------------------------------------------------------------------------
    //Setter functions
    //------------------------------------------------------------------------------------------
    public void set_position(Vector2d position){
        this.position = new Vector2d(position.x,position.y);
    }

    //------------------------------------------------------------------------------------------
    //Other functions
    //------------------------------------------------------------------------------------------
    public void draw(){
        glColor3f(this.get_r(), this.get_g(), this.get_b());
        ShapeMaker.fill_square(this.position.x*this.get_sqaure_size(), this.position.y*this.get_sqaure_size(), this.get_sqaure_size());
        if(this.needs_updating){
            this.updated = false;
        }
    }

    public void update(){
        if(this.needs_updating){
            this.updated = true;
        }
    }
}
