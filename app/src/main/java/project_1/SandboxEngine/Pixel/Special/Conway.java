package project_1.SandboxEngine.Pixel.Special;

import java.util.Vector;

import org.joml.Vector2d;

import javafx.scene.control.Cell;
import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;
import project_1.SandboxEngine.Scene.CellularAutomata;

public class Conway extends Special {
    private boolean isAlive;
    
    //Variable to make sure all the conway pixels move at the same time
    private static boolean animate;
    private static int animation_rate;
    private static int animation_counter;
    private static int mode_selector;
    //Starts moving up
    private static double antx = 1;
    private static double anty = 0;


    public Conway(Vector2d position, boolean isAlive) {
        super(PixelType.CONWAY, 3, position);
        this.isAlive = isAlive;

        this.red = 139.0f/255.0f;
        this.green = 37.0f/255.0f;
        this.blue = 217.0f/255.0f;
    }

    @Override
    public void update(){

        switch (getModeSelector()) {
            //
            case 0:
                //If the animation is turned off, then just move this pixel to the buffer array
                if(Conway.is_animating()==false){
                    CellularAutomata.get().add_pixel(this, this.position, true);
                    System.out.println("Test");
                }
                //If the animation is on, then run the conway rules to see if it makes it to the next generation
                else{
                    if(animation_counter == 0){
                        boolean next_state = Conway.applyGameofLifeRules(this.position, this);
                        //If the next state is to be alive, then just add this pixel to next buffer.
                        if(next_state == true){
                            CellularAutomata.get().add_pixel(this, this.position, true);
                        }
                    }
                    else{
                        CellularAutomata.get().add_pixel(this, this.position, true);
                    }
                }
            break;
                //Langston's Ant
                case 1:
                        if(Conway.is_animating()==false){
                            CellularAutomata.get().add_pixel(this, this.position, true);
                        }
                        else{
                            if(animation_counter != 0){
                                CellularAutomata.get().add_pixel(this, this.position, true);
                            }
                        }
                break;
                    }
        }

    //Static method that will allow the user to stop, start, speed up speed down the conway game of life animations
    public static void toggle_animation(){
        animate = !animate;
        animation_counter = 0;
    }   

    // Select which automata to run.
    public static int getModeSelector(){
        return mode_selector;
    }

    public static void setModeSelector(int x){
        mode_selector = x;
    }

    public static void set_animation(boolean state){
        animate = state;
        animation_counter = 0;
    }

    public static boolean is_animating(){
        return animate;
    }

    public static void set_conway_animation_rate(int rate){
        animation_rate = rate;
    }

    public static int get_animation_rate(){
        return animation_rate;
    }

    public static void increment_counter(){
        if(animate){
            if(animation_counter == animation_rate){
                animation_counter = 0;
            }
            else{
                ++animation_counter; 
            }
        }
        else{
            animation_counter = 0;
        }
    }

    public static int get_counter(){
        return animation_counter;
    }

    public static int countAliveNeighbors(Vector2d position){
        int count = 0;
        for(int dx=-1; dx<=1; ++dx){
            for(int dy=-1; dy<=1; ++dy){
                Vector2d neighorPos = new Vector2d(position.x+dx,position.y+dy);
                //Skip this positon
                if(neighorPos.equals(position)){
                    continue;
                }
                //check if this position is allowed
                if(CellularAutomata.get().pos_allowed(neighorPos)){
                    //Check if this position is null in the current pixel array
                    if(CellularAutomata.get().pos_empty(neighorPos, false)){
                        //True means that there is no neighbor
                    }
                    else{
                        //False means that there is a pixel there. Check if it is a conway pixel
                        Pixel pixel = CellularAutomata.get().get_pixel(neighorPos, false);
                        if(pixel.get_name() == PixelType.CONWAY){
                            ++count;
                        }
                    }

                }
            }
        }
        // System.out.println(count);
        return count;
    }

    public static boolean applyGameofLifeRules(Vector2d position, Pixel pixel){
        int alive_neighbors = Conway.countAliveNeighbors(position);
        
        //check if the conway pixel is dead
        if(pixel == null){
            if(alive_neighbors==3){
                return true;
            }
            return false;
        }
        //Check if the conway pixel is alive
        else if(pixel.get_name() == PixelType.CONWAY){
            if(alive_neighbors<2){
                return false;
            }
            else if(alive_neighbors==2 || alive_neighbors==3){
                return true;
            }
            else{
                return false;
            }
        }
        //If the pixel is anything else, treat it as a dead cell
        else{
            if(alive_neighbors==3){
                return true;
            }
            return false;
        }

    }

    public static Vector2d applyLangstonAntRules(Vector2d ant){
        double xNextPos = ant.x + antx;
        double yNextPos = ant.y + anty;
    
        // Check and wrap around the grid if the ant goes beyond the boundaries
        int gridWidth = CellularAutomata.get().get_width();   // Assuming total_width is accessible
        int gridHeight = CellularAutomata.get().get_height(); // Assuming total_height is accessible
    
        if (xNextPos >= gridWidth) xNextPos = 0;           // Wrap horizontally
        else if (xNextPos < 0) xNextPos = gridWidth - 1;
    
        if (yNextPos >= gridHeight) yNextPos = 0;          // Wrap vertically
        else if (yNextPos < 0) yNextPos = gridHeight - 1;
    
        Vector2d nextPos = new Vector2d(xNextPos, yNextPos);
        

        /*
         * rules can be found here https://rosettacode.org/wiki/Langton%27s_ant#Java
         */
        if(!CellularAutomata.get().pos_empty(nextPos, false)) {
            Pixel p = CellularAutomata.get().get_pixel(nextPos, false);
            // Turn left
            if (antx == 0) { 
                antx = anty;
                anty = 0;
            } else {
                anty = -antx;
                antx = 0;
            }
        } else {
            if(antx == 0){
                antx = -anty;
                anty = 0;
            } else{
                anty = antx;
                antx = 0;
            }
        }
        return nextPos;
    }
    
    public void setAlive(boolean state){
        this.isAlive = state;
    }

    public boolean getAlive(){
        return this.isAlive;
    }
}