package project_1.SandboxEngine.Pixel.Element.Solid;

import org.joml.Vector2d;

import project_1.SandboxEngine.Scene.CellularAutomata;

public class Moveable_Solid extends Solid {
    private double fall_rate;

    public Moveable_Solid(int r, int g, int b, String name, int id, double weight, double fall_rate, Vector2d position) {
        super(r, g, b, name, id, weight, position);
        this.fall_rate = fall_rate;
        this.needs_updating = true;
    }

    @Override
    public void update(){
        if(this.needs_updating){
            this.updated = true;
            ++this.time_step;
            if(this.time_step==fall_rate){
                this.time_step = 0;
                this.fall_down_solid();
            }
        }

        if(CellularAutomata.get().is_position_empty(new Vector2d(this.position.x,this.position.y+1))){
            this.needs_updating = true;
        }

    }

    protected void fall_down_solid(){
        
        if(this.position.y>=(CellularAutomata.get().get_height()-1)){
            this.needs_updating = false;
            return;
        }
        //Check below
        Vector2d check = new Vector2d(this.position.x,this.position.y+1);
        if(CellularAutomata.get().is_position_empty(check)){
            CellularAutomata.get().swap_pixel_positions(this.position, check);
            return;
        }

        if(this.position.x<=(-0)){
            this.needs_updating = false;
            return;
        }
        //Check below left
        check = new Vector2d(this.position.x-1,this.position.y+1);
        if(CellularAutomata.get().is_position_empty(check)){
            CellularAutomata.get().swap_pixel_positions(this.position, check);
            return;
        }

        if(this.position.x>=(CellularAutomata.get().get_width()-1)){
            this.needs_updating = false;
            return;
        }
        //check below right
        check = new Vector2d(this.position.x+1,this.position.y+1);
        if(CellularAutomata.get().is_position_empty(check)){
            CellularAutomata.get().swap_pixel_positions(this.position, check);
            return;
        }

        //Can't move, then do not update anymore
        this.needs_updating = false;
    }
    
}
