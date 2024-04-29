package project_1.SandboxEngine.Pixel.Element.Liquid;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;
import project_1.SandboxEngine.Pixel.Element.Element;
import project_1.SandboxEngine.Pixel.Element.Solid.Moveable_Solid;
import project_1.SandboxEngine.Scene.CellularAutomata;

public class Liquid extends Element{
    private int flow_rate;
    private int flow_distance;
    private boolean can_flow_down;
    private boolean is_flowing;

    public Liquid(PixelType name, int id, double weight, int flow_rate, int flow_distance, Vector2d position) {
        super(name, id, weight, position);
        this.flow_rate = flow_rate;
        this.flow_distance = flow_distance;
        this.is_flowing = true;
        this.can_flow_down = true;
    }

    @Override
    public void update(){
        if((Element.counter % this.flow_rate) == 0){
            this.flow_liquid();
            CellularAutomata.get().add_pixel(this, this.position, true);
            if(this.check_above_weight()){
                CellularAutomata.get().needs_second_update(this);
            }
        }
        else{
            CellularAutomata.get().add_pixel(this, this.position, true);
        }
    }

    @Override
    public void second_update(){
        Vector2d above = new Vector2d();
        above.x = this.position.x;
        above.y = this.position.y-1;

        CellularAutomata.get().swap_pixels(this.position, above, true);
    }

    protected void flow_liquid(){
        Vector2d check = new Vector2d();
        this.can_flow_down = true;
        this.is_flowing = true;

        //Check below
        check.x = this.position.x;
        check.y = this.position.y+1;
        if(this.can_flow_down_check(check)){
            this.position = check;
            return;
        }
        
        //Randomly check left or right first
        double random_value = Math.random();
        int next_value;
        int last_value;
        if(random_value > 0.5){
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
        if(this.can_flow_down_check(check)){
            this.position = check;
            return;
        }
        
        //Check either below left or below right
        check.x = this.position.x + last_value;
        check.y = this.position.y+1;
        if(this.can_flow_down_check(check)){
            this.position = check;
            return;
        }

         //If it can not do any of the above, then it must not be able to flow down. Try flowing side to side
         this.can_flow_down = false;

        //Random pick between left or right again
        random_value = Math.random();
        if(random_value > 0.5){
            next_value = -1;
            last_value = 1;
        }
        else{
            next_value = 1;
            last_value = -1;
        }

        //Either go left or right
        check.x = this.position.x + next_value;
        check.y = this.position.y;
        if(this.can_flow_side_check(check)){
            this.position = check;
            return;
        }

        check.x = this.position.x + last_value;
        check.y = this.position.y;
        if(this.can_flow_side_check(check)){
            this.position = check;
            return;
        }
        
        //If this pixel can not do any of the above, then it is not flowable
        this.is_flowing = false;

    }

    private boolean can_flow_down_check(Vector2d check_pos){
        if(CellularAutomata.get().pos_allowed(check_pos)){
            if(CellularAutomata.get().pos_empty(check_pos, false)){
                if(CellularAutomata.get().pos_empty(check_pos, true)){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                Pixel pixel = CellularAutomata.get().get_pixel(check_pos, false);
                if(pixel instanceof Liquid){
                    if(((Liquid)pixel).can_flow_down()){
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

    private boolean can_flow_side_check(Vector2d check_pos){
        if(CellularAutomata.get().pos_allowed(check_pos)){
            if(CellularAutomata.get().pos_empty(check_pos, false)){
                if(CellularAutomata.get().pos_empty(check_pos, true)){
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
        else{
            return false;
        }
    }

    private boolean check_above_weight(){
        Vector2d check = new Vector2d();
        check.x = this.position.x;
        check.y = this.position.y-1;

        if(CellularAutomata.get().pos_allowed(check)){
            if(CellularAutomata.get().pos_empty(check, true)){
                return false;
            }
            else{
                Pixel pixel = CellularAutomata.get().get_pixel(check, true);
                if(pixel instanceof Element){
                    if(((Element) pixel).get_weight() > this.weight){
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

    public boolean can_flow_down(){
        return can_flow_down;
    }

    public boolean get_is_flowing(){
        return is_flowing;
    }
    
}
