package project_1.SandboxEngine.Scene;

import org.joml.Vector2d;

import javafx.css.SimpleStyleableDoubleProperty;
import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;
import project_1.SandboxEngine.Pixel.Special.Conway;
import project_1.SandboxEngine.Pixel.Element.Element;
import project_1.SandboxEngine.Pixel.Element.Solid.Sand_pixel;

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
        
        Conway.set_conway_animation_rate(30);
        Conway.set_animation(false);
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
                //When the conway game of life is being animated, it need to now check all the empty pixel spaces
                if(Conway.is_animating()){
                    if(Conway.get_counter() == 0){
                        Vector2d position = new Vector2d(col,row);
                        boolean add_conway = Conway.get_next_state(position, null);
                        if(add_conway == true){
                            Conway new_conway = new Conway(position, true);
                            CellularAutomata.get().add_pixel(new_conway, position, true);
                        }
                    }
                }
            }
        }
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

    public void remove_pixel(Vector2d position){
        CellularAutomata.get().buffer_grid[(int)position.x][(int)position.y] = null;
    }

    /**
     * Function to check if a pixel at certain position is null
     * 
     * @param position
     * @return
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
    // public void convertToGameOfLife() {
    //     System.out.println("called 2");
    //     for (int x = 0; x < get_width(); x++) {
    //         for (int y = 0; y < get_height(); y++) {
    //             Pixel p = current_grid[x][y];
    //             if (p != null && (p instanceof Sand_pixel || p instanceof Conway)) {
    //                 boolean isAlive = true;
    //                 buffer_grid[x][y] = new Conway(PixelType.CONWAY, p.get_ID(), new Vector2d(x, y), isAlive);
    //                 buffer_grid[x][y].set_pixel_color(255, 0, 0); 
    //                 System.out.println(current_grid[x][y]);
    //             } else if (p != null) {
    //                 // Copy non-Sand pixels as they are
    //                 buffer_grid[x][y] = p;
    //             } else {
    //                 buffer_grid[x][y] = null;
    //             }
    //         }
    //     }
    //     // After conversion, you might want to swap grids or copy back as needed
    //     draw();  // This needs to handle buffer properly
    // }
    

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
