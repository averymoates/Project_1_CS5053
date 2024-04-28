package project_1.SandboxEngine.Scene;

import org.joml.Vector2d;

import javafx.css.SimpleStyleableDoubleProperty;
import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;
import project_1.SandboxEngine.Pixel.Special.Blank_pixel;
import project_1.SandboxEngine.Pixel.Special.Conway;
import project_1.SandboxEngine.Pixel.Special.Langston;
import project_1.SandboxEngine.Pixel.Special.Special;
import project_1.SandboxEngine.Pixel.Element.Element;
import project_1.SandboxEngine.Pixel.Element.Solid.Sand_pixel;
import project_1.SandboxEngine.Pixel.Element.Liquid.Water_pixel;

public class CellularAutomata {

    private static CellularAutomata instance = null;

    //This will hold the current state of the grid. This should only be changed by the draw function
    private Pixel[][] current_grid;

    //Always make changes to this grid.
    private Pixel[][] buffer_grid;
    // public Pixel[][] pixel_array;

    //These values describe the total draw space.
    private int total_width;
    private int total_height;

    private int total_pixels;

    private double SQUARE_SIZE;
    private int grid_offset = 100;

    private Vector2d ant = null;
    private Vector2d next_ant;
    private CellularAutomata(){
    }

    public static CellularAutomata get(){
        if(CellularAutomata.instance == null){
            CellularAutomata.instance = new CellularAutomata();
        }

        return CellularAutomata.instance;
    }

    public void init(){
        SQUARE_SIZE = SceneManager.get_height()*0.75*0.01;

        CellularAutomata.get().total_width =  100;//(int)((SceneManager.get_width()*0.75)/CellularAutomata.get().SQUARE_SIZE);
        CellularAutomata.get().total_height = 100;//(int)((SceneManager.get_height()*0.75)/CellularAutomata.get().SQUARE_SIZE);

        // CellularAutomata.get().total_width = (int)((SceneManager.get_width() - 660)/CellularAutomata.get().SQUARE_SIZE);
        // CellularAutomata.get().total_height = (int)((SceneManager.get_height() - 400)/CellularAutomata.get().SQUARE_SIZE);
        System.out.println(total_width);
        System.out.println(total_height);
        CellularAutomata.get().total_pixels = CellularAutomata.get().total_height*CellularAutomata.get().total_width;
        CellularAutomata.get().current_grid = new Pixel[CellularAutomata.get().total_width][CellularAutomata.get().total_height];
        CellularAutomata.get().buffer_grid = new Pixel[CellularAutomata.get().total_width][CellularAutomata.get().total_height];
        CellularAutomata.get().empty_curr_grid();
        CellularAutomata.get().empty_buff_grid();

        System.out.println("Cellular Automata Size: "+CellularAutomata.get().total_width+"X"+CellularAutomata.get().total_height);

        //place any functions that needs to be called once that is related to pixels
        Element.set_counter(0);
        
        Conway.set_animation_rate(30);
        Langston.set_animation_rate(30);
        Special.set_animation(false);
    }

    public void update(){
        SQUARE_SIZE = SceneManager.get_height()*0.75*0.01;
        // System.out.println("Cellular Automata Update call");
        CellularAutomata.get().empty_buff_grid();
        Conway.increment_counter();
        for(int col=0; col<CellularAutomata.get().total_width; ++col){
            for(int row=0; row<CellularAutomata.get().total_height; ++row){
                if(CellularAutomata.get().current_grid[col][row] != null){
                    CellularAutomata.get().current_grid[col][row].update();
                    continue;
                }
                //When the special automata is being animated, it needs to now check all the empty pixel spaces
                if(Special.is_animating()){
                    if((Special.get_counter() % Conway.get_animation_rate()) == 0){
                        Vector2d position = new Vector2d(col,row);
                        boolean add_conway = Conway.applyConwayRules(position, null);
                        if(add_conway == true){
                            Conway new_conway = new Conway(position, true);
                            CellularAutomata.get().add_pixel(new_conway, position, true);
                        }
                    }
                }
            }
        }

        // if(Conway.is_animating() && Conway.getModeSelector() == 1){
        //     //First Iteration, pick a random point, we will just select one near the center for now.
        //     //Gets next position for the ant, and the next_ant
        //     if(ant == null){
        //         ant = new Vector2d(CellularAutomata.get().total_width / 2, CellularAutomata.get().total_height / 2);
        //         System.out.println(ant);
        //         next_ant = new Vector2d(Conway.applyLangstonAntRules(ant));
        //         System.out.println(next_ant);
        //     } else{
        //         ant = next_ant;
        //         next_ant = new Vector2d(Conway.applyLangstonAntRules(ant));
        //     }

        //     // Can handle any type of cell, essentially eating away and reforming the grid, 
        //     // If there is an empty cell that needs to be filled in, use a Conway cell, if a cell needs to be flipped, remove the cell from the grid.
        //     if(CellularAutomata.get().pos_empty(ant, false)){
        //         CellularAutomata.get().add_pixel(new Conway(ant, true), ant, true);
        //         System.out.print("This ant was empty");
        //     } else{
        //         CellularAutomata.get().remove_pixel(ant);
        //                         System.out.print("This ant wasnt empty");
        //     }
        //     System.out.println(next_ant);
        // }
    }

    public void draw(){
        for(int col=0; col<CellularAutomata.get().total_width; ++col){
            for(int row=0; row<CellularAutomata.get().total_width; ++row){
                if(CellularAutomata.get().buffer_grid[col][row] != null){

                    CellularAutomata.get().buffer_grid[col][row].draw();
                }
                CellularAutomata.get().current_grid[col][row] = CellularAutomata.get().buffer_grid[col][row];
            }
        }
    }
        

    public void empty_curr_grid(){
        CellularAutomata.get().current_grid = new Pixel[CellularAutomata.get().total_width][CellularAutomata.get().total_height];
    }

    public void empty_buff_grid(){
        CellularAutomata.get().buffer_grid = new Pixel[CellularAutomata.get().total_width][CellularAutomata.get().total_height];
    }

    //------------------------------------------------------------------------------------------
    //Array functions
    //------------------------------------------------------------------------------------------

    /**
     * Call this function to move and/or add a pixel into the buffer or current Pixel 2d array.
     * Buffered array is the array that holds all the updated pixels and it is the array that is always used to draw from.
     * Current array holds the states from the previous frame. The update function will always be called on the current array.
     * 
     * @param pixel         The Pixel object you want to add
     * @param position      The position in the array that you want the pixel to be
     * @param buffer_array  True means you want to add the pixel to the buffered array. False means you want to add the pixel to the current array.
     */
    public void add_pixel(Pixel pixel, Vector2d position, boolean buffer_array){
        // System.out.println("MOUSE: "+position.x+", "+position.y);
        // if(position.x <=100 && position.y <= 100) {
        // }
        // position.x *= SQUARE_SIZE;
        // position.y *= SQUARE_SIZE;
        
        if(buffer_array){
            CellularAutomata.get().buffer_grid[(int)position.x][(int)position.y] = pixel;
        }
        else{
            CellularAutomata.get().current_grid[(int)position.x][(int)position.y] = pixel;
        }
    }

    /**
     * Function to remove a certain pixel from either the current grid or buffer grid. It also returns the removed pixel if you need it
     * @param position
     * @param buffer_array
     * @return
     */
    public Pixel remove_pixel(Vector2d position, boolean buffer_array){
        if(buffer_array){
            Pixel temp = CellularAutomata.get().buffer_grid[(int)position.x][(int)position.y];
            CellularAutomata.get().buffer_grid[(int)position.x][(int)position.y] = null;
            return temp;
        }
        else{
            Pixel temp = CellularAutomata.get().current_grid[(int)position.x][(int)position.y];
            CellularAutomata.get().current_grid[(int)position.x][(int)position.y] = null;
            return temp;
        }
    }

    /**
     * Function to check if a pixel at certain position is null
     * 
     * @param position
     * @return true/false
     */
    public boolean pos_empty(Vector2d position, boolean buffer_array){
        if(buffer_array){
            if(CellularAutomata.get().buffer_grid[(int)position.x][(int)position.y] == null){
                return true;
            }
            return false;
        }
        else{
            if(CellularAutomata.get().current_grid[(int)position.x][(int)position.y] == null){
                return true;
            }
            return false;
        }
    }

    public boolean pos_allowed(Vector2d position){
        if(position.x<0 || position.x>=CellularAutomata.get().total_width){
            return false;
        }
        if(position.y<0 || position.y>=CellularAutomata.get().total_height){
            return false;
        }
        return true;
    }

    //
    //Game of Life Functions
    //

    // /*
    //  * Converts all of the sand pixels (only enum type implemented currently) to game of life pixels to run the simulation on them.
    //  * Activates when the user clicks the letter 'C', can be found in SceneManager.
    //  */
    public void convertToGameOfLife(int type) {
        System.out.println("called 2");

        for(int col=0; col<CellularAutomata.get().total_width; ++col){
            for(int row=0; row<CellularAutomata.get().total_height; ++row){
                Pixel p = CellularAutomata.get().current_grid[col][row];
                Vector2d position = new Vector2d(col, row);
                if(p != null && p.get_ID() == 0){
                    // determine what pixel to draw
                    switch (type) {
                        case 0:
                            p = new Blank_pixel(position);
                            break;
                        case 1:
                            p = new Sand_pixel(position);
                            break;
                        case 2:
                            p = new Water_pixel(position);
                            break;
                        case 3:
                            p = new Conway(position, true);
                            break;
                        case 4:
                            p = new Langston(position, true);
                            break;
                        default:
                            p = new Blank_pixel(position);
                            break;
                    }
                    CellularAutomata.get().add_pixel(p, p.get_position(), false);
                }
            }
        }
    }
    

    // /*
    //  * The MouseListener() will call this function depending on where the mouse cursor (SceneManager) is when clicked.
    //  */
    // public void togglePixelState(Vector2d position) {
    //     int x = (int)position.x;
    //     int y = (int)position.y;
    //     if (pos_allowed(position) && current_grid[x][y] instanceof Conway) {
    //         Conway pixel = (Conway)current_grid[x][y];
    //         pixel.setAlive(!pixel.getAlive());
    //     }
    // }

    // public void updateConway(){
    //     for (int x = 0; x < get_width(); x++){
    //         for (int y = 0; y < get_height(); y++){
    //             Pixel pixel = current_grid[x][y];
    //             if (pixel instanceof Conway){
    //                 ((Conway) pixel).update();
    //             }
    //         }
    //     }
    // }

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

    public double get_square_size() {
        return SQUARE_SIZE;
    }

    public Pixel get_pixel(Vector2d position, boolean buffer_array){
        if(buffer_array){
            return CellularAutomata.get().buffer_grid[(int)position.x][(int)position.y];
        }
        else{
            return CellularAutomata.get().current_grid[(int)position.x][(int)position.y];
        }
    }    
}
