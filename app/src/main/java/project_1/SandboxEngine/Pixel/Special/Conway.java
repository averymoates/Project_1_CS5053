package project_1.SandboxEngine.Pixel.Special;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;
import project_1.SandboxEngine.Scene.CellularAutomata;

public class Conway extends Special {
    private boolean isAlive;
    
    //Variable to make sure all the conway pixels move at the same time
    private static boolean animate;
    private static int animation_rate;
    private static int animation_counter;


    public Conway(Vector2d position, boolean isAlive) {
        super(PixelType.CONWAY, 3, position);
        this.isAlive = isAlive;

        this.red = 139.0f/255.0f;
        this.green = 37.0f/255.0f;
        this.blue = 217.0f/255.0f;
    }

    @Override
    public void update(){
        //If the animation is turned off, then just move this pixel to the buffer array
        if(Conway.is_animating()==false){
            CellularAutomata.get().add_pixel(this, this.position, true);
        }
        //If the animation is on, then run the conway rules to see if it makes it to the next generation
        else{
            if(animation_counter == 0){
                boolean next_state = Conway.get_next_state(this.position, this);
                //If the next state is to be alive, then just add this pixel to next buffer.
                if(next_state == true){
                    CellularAutomata.get().add_pixel(this, this.position, true);
                }
            }
            else{
                CellularAutomata.get().add_pixel(this, this.position, true);
            }
        }
    }

    //Static method that will allow the user to stop, start, speed up speed down the conway game of life animations
    public static void toggle_animation(){
        animate = !animate;
        animation_counter = 0;
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

    public static boolean get_next_state(Vector2d position, Pixel pixel){
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
    
    /*
     * Overrides the update method to go through all the pixels and run the Game of Life rules.
     */
    // @Override
    // public void update(){
    //     int aliveNeighbors = countAliveNeighbors();
    //     boolean nextState = applyGameofLifeRules(isAlive, aliveNeighbors);

    //     if (nextState != this.isAlive){
    //         this.isAlive = nextState;
    //     }
    // }

    /*
     * Goes through and counts the four othogonal cells of the pixel at (x,y) 
     * 
     * | x
     * |xox
     * | x
     * 
     */
    // private int countAliveNeighbors(){
    //     int count = 0;
    //     for (int dx = -1; dx <= 1; dx++){
    //         for (int dy = -1; dy <= 1; dy++){
    //             if (dx == 0 && dy == 0) continue;
    //             Vector2d neighorPos = new Vector2d(this.position.x + dx, this.position.y + dy);
    //             Pixel pixel = CellularAutomata.get().get_pixel(position, false);
    //             if (pixel instanceof Conway){
    //                 Conway p = (Conway) pixel;
    //                 if (p.isAlive){
    //                     count++;
    //                 }
    //             }
    //         }
    //     }

    //     return count;

    // }

    /*
     * Applies game of lifes rules, you can read more here
     * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
     */
    private boolean applyGameofLifeRules(boolean currentState, int aliveNeighbors){
        if (currentState && (aliveNeighbors == 2 || aliveNeighbors == 3)){
            return true;
        } else if (!currentState && aliveNeighbors == 3){
            return true;
        } 
        return false;
    }

    public void setAlive(boolean state){
        this.isAlive = state;
    }

    public boolean getAlive(){
        return this.isAlive;
    }
}