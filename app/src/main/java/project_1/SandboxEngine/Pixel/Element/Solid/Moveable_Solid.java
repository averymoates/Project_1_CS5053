package project_1.SandboxEngine.Pixel.Element.Solid;

import java.util.Random;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.Element.Element;
import project_1.SandboxEngine.Pixel.PixelType;
import project_1.SandboxEngine.Scene.CellularAutomata;

public class Moveable_Solid extends Solid {
    private int fall_rate;
    private boolean is_falling;

    public Moveable_Solid(PixelType name, int id, double weight, int fall_rate, Vector2d position) {
        super(name, id, weight, position);
        this.fall_rate = fall_rate;
        this.is_falling = true;
    }

    @Override
    public void update(){
        if((Element.counter % this.fall_rate)==0){
            this.fall_down_solid();
            // CellularAutomata.get().add_pixel(this, this.position, true);
        }
        else{
            CellularAutomata.get().add_pixel(this, this.position, true);
        }
    }

    protected void fall_down_solid(){
        Vector2d check = new Vector2d();

        //Check below
        check.x = this.position.x;
        check.y = this.position.y+1;
        if(this.can_fall_check(check)){
            this.position = check;
            CellularAutomata.get().add_pixel(this, this.position, true);
            return;
        }
        
        //Randomly check left or right first
        double random_value = Math.random();
        int next_value;
        int last_value;
        if(random_value > .5){
            next_value = -1;
            last_value = 1;
        }
        else{
            next_value = 1;
            last_value = -1;
        }

        //Check either below left or below right
        check.x = this.position.x + next_value;
        check.y = this.position.y+1;
        if(this.can_fall_check(check)){
            this.position = check;
            CellularAutomata.get().add_pixel(this, this.position, true);
            return;
        }
        
        //Check either below left or below right
        check.x = this.position.x + last_value;
        check.y = this.position.y+1;
        if(this.can_fall_check(check)){
            this.position = check;
            CellularAutomata.get().add_pixel(this, this.position, true);
            return;
        }

        //If it can not do any of the above, then it must not be moveable
        this.is_falling = false;
        CellularAutomata.get().add_pixel(this, this.position, true);
        
    }

    private boolean can_fall_check(Vector2d check_pos){
        if(CellularAutomata.get().pos_allowed(check_pos)){
            if(CellularAutomata.get().pos_empty(check_pos, false)){
                return true;
            }
            else{
                Pixel pixel = CellularAutomata.get().get_pixel(check_pos, false);
                if(pixel instanceof Moveable_Solid){
                    if(((Moveable_Solid)pixel).get_is_falling()){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return false;
                }
            }
        }
        else{
            return false;
        }
    }

    public boolean get_is_falling(){
        return this.is_falling;
    }
    
}
