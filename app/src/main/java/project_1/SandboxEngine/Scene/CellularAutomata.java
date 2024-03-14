package project_1.SandboxEngine.Scene;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;

public class CellularAutomata {

    private static CellularAutomata instance = null;

    private Pixel[][] pixel_array;

    //These values describe the total draw space.
    private int total_width;
    private int total_height;

    private int total_pixels;

    private final double SQUARE_SIZE = 10.0;

    private CellularAutomata(){
    }

    public static CellularAutomata get(){
        if(CellularAutomata.instance == null){
            CellularAutomata.instance = new CellularAutomata();
        }

        return CellularAutomata.instance;
    }

    public void init(){
        CellularAutomata.get().total_width = (int)(SceneManager.get_width()/CellularAutomata.get().SQUARE_SIZE) - 2;
        CellularAutomata.get().total_height = (int)(SceneManager.get_height()/CellularAutomata.get().SQUARE_SIZE) - 8;
        CellularAutomata.get().total_pixels = CellularAutomata.get().total_height*CellularAutomata.get().total_width;
        CellularAutomata.get().pixel_array = new Pixel[CellularAutomata.get().total_width][CellularAutomata.get().total_height];
        CellularAutomata.get().empty_array();

        System.out.println("Cellular Automata Size: "+CellularAutomata.get().total_width+"X"+CellularAutomata.get().total_height);
    }

    public void update(){
        for(int col=0; col<CellularAutomata.get().total_width; ++col){
            for(int row=0; row<CellularAutomata.get().total_height; ++row){
                if(CellularAutomata.get().pixel_array[col][row] != null){
                    CellularAutomata.get().pixel_array[col][row].update();
                }
            }
        }

    }

    public void draw(){
        for(int col=0; col<CellularAutomata.get().total_width; ++col){
            for(int row=0; row<CellularAutomata.get().total_height; ++row){
                if(CellularAutomata.get().pixel_array[col][row] != null){
                    CellularAutomata.get().pixel_array[col][row].draw();
                }
                
            }
        }

    }

    public void empty_array(){
        //Set everything in the 2D array to null
        CellularAutomata.get().pixel_array = new Pixel[CellularAutomata.get().total_width][CellularAutomata.get().total_height];

    }

    //------------------------------------------------------------------------------------------
    //Array functions
    //------------------------------------------------------------------------------------------

    public void add_pixel(Pixel pixel, Vector2d position){
        if((position.x >= CellularAutomata.get().get_width()) || (position.x <= 0)){
            return;
        }
        if((position.y >= CellularAutomata.get().get_height()) || (position.y <= 0)){
            return;
        }
        CellularAutomata.get().pixel_array[(int)position.x][(int)position.y] = pixel;
    }

    public void remove_pixel(Vector2d position){
        CellularAutomata.get().pixel_array[(int)position.x][(int)position.y] = null;
    }

    /**
     * Function to check if a pixel at certain position is null
     * 
     * @param position
     * @return
     */
    public boolean is_position_empty(Vector2d position){
        if((position.x >= CellularAutomata.get().get_width()) || (position.x <= 0)){
            return false;
        }
        if((position.y >= CellularAutomata.get().get_height()) || (position.y <= 0)){
            return false;
        }
        if(CellularAutomata.get().pixel_array[(int)position.x][(int)position.y] == null){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Function to check if a pixel at a certain position has been updated.
     * The reason for this function is so that a pixel does not get updated 
     * multiple times per frame update call
     * 
     * @param position
     * @return
     */
    public boolean has_pixel_updated(Vector2d position){
        if(CellularAutomata.get().is_position_empty(position)){
            return false;
        }

        return CellularAutomata.get().pixel_array[(int)position.x][(int)position.y].is_updated();
    }

    /**
     * Function to swap pixel A with pixel B. This also updates the position values for each pixels
     * 
     * @param positionA Pixel A Location
     * @param positionB Pixel B Location
     */
    public void swap_pixel_positions(Vector2d positionA, Vector2d positionB){
        //Keep a temp B
        Pixel temp = CellularAutomata.get().pixel_array[(int)positionB.x][(int)positionB.y];

        //Move A into position B
        CellularAutomata.get().pixel_array[(int)positionB.x][(int)positionB.y] = CellularAutomata.get().pixel_array[(int)positionA.x][(int)positionA.y];
        if(CellularAutomata.get().pixel_array[(int)positionB.x][(int)positionB.y] != null){
            CellularAutomata.get().pixel_array[(int)positionB.x][(int)positionB.y].set_position(positionB);
        }
        
        //Move temp B into position A
        CellularAutomata.get().pixel_array[(int)positionA.x][(int)positionA.y] = temp;
        if(CellularAutomata.get().pixel_array[(int)positionA.x][(int)positionA.y] != null){
            CellularAutomata.get().pixel_array[(int)positionA.x][(int)positionA.y].set_position(positionA);
        }
        
    }

    //------------------------------------------------------------------------------------------
    //Getter functions
    //------------------------------------------------------------------------------------------

    public int get_height(){
        return CellularAutomata.get().total_height;
    }

    public int get_width(){
        return CellularAutomata.get().total_width;
    }

    public int get_total_pixels(){
        return CellularAutomata.get().total_pixels;
    }

    
    
}
