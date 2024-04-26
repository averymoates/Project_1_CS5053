package project_1.SandboxEngine.Pixel.Special;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;
import project_1.SandboxEngine.Scene.CellularAutomata;

/**
 * NOTES:
 * 
 * The ant itself will be a redish color. When it is on a null pixel or any other pixel besides the blank pixel, 
 */
public class Langston extends Special{

    /**
     *       0
     *       ^
     * 3 <- Ant -> 1
     *       v
     *       2
     */
    private int current_direction;

    private Pixel buffer_pixel;

    private static int animation_rate;

    public Langston(Vector2d position, boolean isAlive) {
        super(PixelType.Langston, 4, position, isAlive);

        this.red = 158.0f/255.0f;
        this.green = 5.0f/255.0f;
        this.blue = 18.0f/255.0f;

        this.current_direction = 0;
        this.buffer_pixel = null;
    }

    @Override
    public void update(){
        if(is_animating()==false){
            CellularAutomata.get().add_pixel(this, this.position, true);
        }
        else{
            if((animation_counter%animation_rate)==0){
                boolean next_state = applyLangstonRule(this.buffer_pixel);

                //True means that the ant moves clockwise and then flip the pixel to a blank pixel. 
                if(next_state == true){
                    CellularAutomata.get().add_pixel(new Blank_pixel(this.position), this.position, true);

                    this.current_direction = Math.floorMod(this.current_direction+1, 4);

                    Vector2d new_ant_position = new Vector2d(this.position.x, this.position.y);
                    if(this.current_direction==0){
                        new_ant_position.y -= 1;
                        if(CellularAutomata.get().pos_allowed(new_ant_position)){
                            this.buffer_pixel = CellularAutomata.get().remove_pixel(new_ant_position, false);
                            this.position = new_ant_position;
                            CellularAutomata.get().add_pixel(this, this.position, true);
                        }
                    }
                    else if(this.current_direction==1){
                        new_ant_position.x += 1;
                        if(CellularAutomata.get().pos_allowed(new_ant_position)){
                            this.buffer_pixel = CellularAutomata.get().remove_pixel(new_ant_position, false);
                            this.position = new_ant_position;
                            CellularAutomata.get().add_pixel(this, this.position, true);
                        }
                    }
                    else if(this.current_direction==2){
                        new_ant_position.y += 1;
                        if(CellularAutomata.get().pos_allowed(new_ant_position)){
                            this.buffer_pixel = CellularAutomata.get().remove_pixel(new_ant_position, false);
                            this.position = new_ant_position;
                            CellularAutomata.get().add_pixel(this, this.position, true);
                        }
                    }
                    else if(this.current_direction==3){
                        new_ant_position.x -= 1;
                        if(CellularAutomata.get().pos_allowed(new_ant_position)){
                            this.buffer_pixel = CellularAutomata.get().remove_pixel(new_ant_position, false);
                            this.position = new_ant_position;
                            CellularAutomata.get().add_pixel(this, this.position, true);
                        }
                    }
                }

                //False means that the ant moves counter clockwise and then flip the pixel to a white pixel... delete the pixel
                else if(next_state == false){
                    this.current_direction = Math.floorMod(this.current_direction-1, 4);

                    Vector2d new_ant_position = new Vector2d(this.position.x, this.position.y);
                    if(this.current_direction==0){
                        new_ant_position.y -= 1;
                        if(CellularAutomata.get().pos_allowed(new_ant_position)){
                            this.buffer_pixel = CellularAutomata.get().remove_pixel(new_ant_position, false);
                            this.position = new_ant_position;
                            CellularAutomata.get().add_pixel(this, this.position, true);
                        }
                    }
                    else if(this.current_direction==1){
                        new_ant_position.x += 1;
                        if(CellularAutomata.get().pos_allowed(new_ant_position)){
                            this.buffer_pixel = CellularAutomata.get().remove_pixel(new_ant_position, false);
                            this.position = new_ant_position;
                            CellularAutomata.get().add_pixel(this, this.position, true);
                        }
                    }
                    else if(this.current_direction==2){
                        new_ant_position.y += 1;
                        if(CellularAutomata.get().pos_allowed(new_ant_position)){
                            this.buffer_pixel = CellularAutomata.get().remove_pixel(new_ant_position, false);
                            this.position = new_ant_position;
                            CellularAutomata.get().add_pixel(this, this.position, true);
                        }
                    }
                    else if(this.current_direction==3){
                        new_ant_position.x -= 1;
                        if(CellularAutomata.get().pos_allowed(new_ant_position)){
                            this.buffer_pixel = CellularAutomata.get().remove_pixel(new_ant_position, false);
                            this.position = new_ant_position;
                            CellularAutomata.get().add_pixel(this, this.position, true);
                        }
                    }
                }
            }
            else{
                CellularAutomata.get().add_pixel(this, this.position, true);
            }
        }

    }

    /**
     * Function to figure out if the ant should move counter clockwise == false, or move clockwise == true
     * 
     * @param pixel
     * @return
     */
    public static boolean applyLangstonRule(Pixel pixel){
        //Check if the pixel is null. This means that the null pixel is treated as a "white" pixel
        if(pixel == null){
            return true;
        }

        //Treat every other pixel as a "black" pixel
        else{
            return false;
        }


        // double xNextPos = ant.x + antx;
        // double yNextPos = ant.y + anty;
    
        // // Check and wrap around the grid if the ant goes beyond the boundaries
        // int gridWidth = CellularAutomata.get().get_width();   // Assuming total_width is accessible
        // int gridHeight = CellularAutomata.get().get_height(); // Assuming total_height is accessible
    
        // if (xNextPos >= gridWidth) xNextPos = 0;           // Wrap horizontally
        // else if (xNextPos < 0) xNextPos = gridWidth - 1;
    
        // if (yNextPos >= gridHeight) yNextPos = 0;          // Wrap vertically
        // else if (yNextPos < 0) yNextPos = gridHeight - 1;
    
        // Vector2d nextPos = new Vector2d(xNextPos, yNextPos);
        

        // /*
        //  * rules can be found here https://rosettacode.org/wiki/Langton%27s_ant#Java
        //  */
        // if(!CellularAutomata.get().pos_empty(nextPos, false)) {
        //     Pixel p = CellularAutomata.get().get_pixel(nextPos, false);
        //     // Turn left
        //     if (antx == 0) { 
        //         antx = anty;
        //         anty = 0;
        //     } else {
        //         anty = -antx;
        //         antx = 0;
        //     }
        // } else {
        //     if(antx == 0){
        //         antx = -anty;
        //         anty = 0;
        //     } else{
        //         anty = antx;
        //         antx = 0;
        //     }
        // }
        // return nextPos;
    }

    public static void set_animation_rate(int rate){
        animation_rate = rate;
    }

    public static int get_animation_rate(){
        return animation_rate;
    }
    
}
