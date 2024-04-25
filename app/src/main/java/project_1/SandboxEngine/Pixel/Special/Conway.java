package project_1.SandboxEngine.Pixel.Special;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;
import project_1.SandboxEngine.Scene.CellularAutomata;

public class Conway extends Special {
    private boolean isAlive;

    //Variable to make sure all the conway pixels move at the same time
    private static boolean animate;

    public Conway(Vector2d position, boolean isAlive) {
        super(PixelType.CONWAY, 3, position);
        this.isAlive = isAlive;

        this.red = 139.0f/255.0f;
        this.green = 37.0f/255.0f;
        this.blue = 217.0f/255.0f;
    }

    public static void toggle_animation(){
        animate = !animate;
    }

    public static boolean is_animating(){
        return animate;
    }

    public static int countAliveNeighbors(Vector2d position){
        int count = 0;
        for(int dx=-1; dx<=1; ++dx){
            for(int dy=-1; dy<=1; ++dy){
                Vector2d neighorPos = new Vector2d(position.x+dx,position.y+dy);
                //check if this position is allowed
                if(CellularAutomata.get().pos_allowed(neighorPos)){
                    //Check if this position is null in the current pixel array
                    if(CellularAutomata.get().pos_empty(position, false)){
                        //True means that there is no neighbor
                    }
                    else{
                        //False means that there is a pixel there. Check if it is a conway pixel
                        Pixel pixel = CellularAutomata.get().get_pixel(position, false);
                        if(pixel.get_name() == PixelType.CONWAY){
                            ++count;
                        }
                    }

                }
            }
        }

        return count;
    }

    public static boolean get_next_state(Vector2d position, Pixel pixel){
        int alive_neighbors = Conway.countAliveNeighbors(position);
        
        //Check if the conway pixel is alive
        if(pixel.get_name() == PixelType.CONWAY){
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
        //check if the conway pixel is dead
        else if(pixel == null){
            if(alive_neighbors==3){
                return true;
            }
            return false;
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