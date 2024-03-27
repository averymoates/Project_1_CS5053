package project_1.SandboxEngine.Pixel.Element.Solid;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;
import project_1.SandboxEngine.Scene.CellularAutomata;

public class Moveable_Solid extends Solid {
    private double fall_rate;
    private boolean is_falling;

    public Moveable_Solid(PixelType name, int id, double weight, double fall_rate, Vector2d position) {
        super(name, id, weight, position);
        this.fall_rate = fall_rate;
        this.is_falling = true;
    }

    @Override
    public void update(){
        if(this.time_step == fall_rate){
            this.time_step = 0;
            if(is_falling){
                fall_down_solid();
            }
            else{
                set_above_still();
            }
        }
        else{
            ++this.time_step;
            CellularAutomata.get().add_pixel(this, this.position, true);
        }
    }

    public void force_fall(){
        this.is_falling = true;
        this.fall_down_solid();
    }

    public void force_still(){
        this.is_falling = false;
    }

    protected void fall_down_solid(){
        //Check Below
        Vector2d check = new Vector2d(this.position.x,this.position.y+1);
        if(CellularAutomata.get().pos_allowed(check)){
            if(CellularAutomata.get().pos_empty(check,false)){
                this.position = check;
                CellularAutomata.get().add_pixel(this, check, true);
                if(this.position.y>=CellularAutomata.get().get_height()){
                    this.force_still();
                }
                return;
            }

            Pixel temp = CellularAutomata.get().get_pixel(check, false);
            if((temp instanceof Moveable_Solid) && ((Moveable_Solid)temp).get_is_falling()){
                this.position = check;
                CellularAutomata.get().add_pixel(this, check, true);
                if(this.position.y>=CellularAutomata.get().get_height()){
                    this.force_still();
                }
                return;
            }
            else if((temp instanceof Moveable_Solid) && !((Moveable_Solid)temp).get_is_falling()){
                Vector2d check_2 = new Vector2d(this.position.x,this.position.y+2);
                if(CellularAutomata.get().pos_allowed(check_2) && CellularAutomata.get().pos_empty(check,false)){
                    ((Moveable_Solid) temp).force_fall();
                }
            }
            //I need to do a check if the below pixel is a liquid and if this weight is heavier
        }

        //Check Right
        check = new Vector2d(this.position.x+1,this.position.y+1);
        if(CellularAutomata.get().pos_allowed(check)){
            if(CellularAutomata.get().pos_empty(check,false)){
                this.position = check;
                CellularAutomata.get().add_pixel(this, check, true);
                if(this.position.y>=CellularAutomata.get().get_height()){
                    this.force_still();
                }
                return;
            }
            
        }

        //Check Left
        check = new Vector2d(this.position.x-1,this.position.y+1);
        if(CellularAutomata.get().pos_allowed(check)){
            if(CellularAutomata.get().pos_empty(check,false)){
                this.position = check;
                CellularAutomata.get().add_pixel(this, check, true);
                if(this.position.y>=CellularAutomata.get().get_height()){
                    this.force_still();
                }
                return;
            }
            
        }

        this.force_still();
        CellularAutomata.get().add_pixel(this, this.position, true);
        
    }

    protected void set_above_still(){
        //Check left
        Vector2d check = new Vector2d(this.position.x-1,this.position.y);
        if(CellularAutomata.get().pos_allowed(check)){
            if(CellularAutomata.get().pos_empty(check,true)){
                CellularAutomata.get().add_pixel(this, this.position, true);
                return;
            }
        }

        //Check right
        check = new Vector2d(this.position.x+1,this.position.y);
        if(CellularAutomata.get().pos_allowed(check)){
            if(CellularAutomata.get().pos_empty(check,true)){
                CellularAutomata.get().add_pixel(this, this.position, true);
                return;
            }
        }

        //Check above
        check = new Vector2d(this.position.x,this.position.y-1);
        if(CellularAutomata.get().pos_allowed(check)){
            if(CellularAutomata.get().pos_empty(check,true)){
                CellularAutomata.get().add_pixel(this, this.position, true);
                return;
            }

            Pixel temp = CellularAutomata.get().get_pixel(check, true);
            if((temp.getClass() == Moveable_Solid.class)){
                CellularAutomata.get().add_pixel(this, this.position, true);
                ((Moveable_Solid) temp).force_still();
                return;
            }            
        }

    }

    public boolean get_is_falling(){
        return this.is_falling;
    }
    
}
