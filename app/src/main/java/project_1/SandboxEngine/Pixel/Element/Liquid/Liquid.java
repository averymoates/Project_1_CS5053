package project_1.SandboxEngine.Pixel.Element.Liquid;

import javax.annotation.MatchesPattern;
import javax.crypto.Mac;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.Pixel;
import project_1.SandboxEngine.Pixel.PixelType;
import project_1.SandboxEngine.Pixel.Element.Element;
import project_1.SandboxEngine.Pixel.Element.Solid.Moveable_Solid;
import project_1.SandboxEngine.Scene.CellularAutomata;

public class Liquid extends Element{
    private double flow_rate;
    private boolean is_flowing;

    public Liquid(PixelType name, int id, double weight, double flow_rate, Vector2d position) {
        super(name, id, weight, position);
        this.flow_rate = flow_rate;
        this.is_flowing = true;
    }

    @Override
    public void update(){
        if(this.time_step == flow_rate){
            this.time_step = 0;
            if(is_flowing){
                flow_liquid();
            }
            else{
                CellularAutomata.get().add_pixel(this, this.position, true);
            }
        }
        else{
            ++this.time_step;
            CellularAutomata.get().add_pixel(this, this.position, true);
        }
    }

    public void force_flow(){
        this.is_flowing = true;
        this.flow_liquid();
    }

    public void force_still(){
        this.is_flowing = false;
    }

    public boolean get_is_flowing(){
        return this.is_flowing;
    }

    protected void flow_liquid(){

        //Check below
        Vector2d check = new Vector2d(this.position.x, this.position.y+1);
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
            if((temp instanceof Liquid) && ((Liquid)temp).get_is_flowing()){
                this.position = check;
                CellularAutomata.get().add_pixel(this, check, true);
                if(this.position.y>=CellularAutomata.get().get_height()){
                    this.force_still();
                }
                return;
            }
            else if((temp instanceof Liquid) && !((Liquid)temp).get_is_flowing()){
                Vector2d check_2 = new Vector2d(this.position.x,this.position.y+2);
                if(CellularAutomata.get().pos_allowed(check_2) && CellularAutomata.get().pos_empty(check,false)){
                    ((Liquid) temp).force_flow();
                }
            }
        }

        //Check below right
        check = new Vector2d(this.position.x+1, this.position.y+1);
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

        //Check below left
        check = new Vector2d(this.position.x-1, this.position.y+1);
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

        //Check right
        check = new Vector2d(this.position.x+1, this.position.y);
        if(CellularAutomata.get().pos_allowed(check)){
            if(CellularAutomata.get().pos_empty(check,false)){
                this.position = check;
                CellularAutomata.get().add_pixel(this, check, true);
                return;
            }
        }

        //Check left
        check = new Vector2d(this.position.x-1, this.position.y);
        if(CellularAutomata.get().pos_allowed(check)){
            if(CellularAutomata.get().pos_empty(check,false)){
                this.position = check;
                CellularAutomata.get().add_pixel(this, check, true);
                return;
            }
        }

        // double value = Math.random();
        // if(value > 0.5){
        //     //Check below right
        //     check = new Vector2d(this.position.x+1, this.position.y+1);
        //     if(CellularAutomata.get().pos_allowed(check)){
        //         if(CellularAutomata.get().pos_empty(check,false)){
        //             this.position = check;
        //             CellularAutomata.get().add_pixel(this, check, true);
        //             if(this.position.y>=CellularAutomata.get().get_height()){
        //                 this.force_still();
        //             }
        //             return;
        //         }
        //     }

        //     //Check below left
        //     check = new Vector2d(this.position.x-1, this.position.y+1);
        //     if(CellularAutomata.get().pos_allowed(check)){
        //         if(CellularAutomata.get().pos_empty(check,false)){
        //             this.position = check;
        //             CellularAutomata.get().add_pixel(this, check, true);
        //             if(this.position.y>=CellularAutomata.get().get_height()){
        //                 this.force_still();
        //             }
        //             return;
        //         }
        //     }
        // }
        // else{
        //     //Check below left
        //     check = new Vector2d(this.position.x-1, this.position.y+1);
        //     if(CellularAutomata.get().pos_allowed(check)){
        //         if(CellularAutomata.get().pos_empty(check,false)){
        //             this.position = check;
        //             CellularAutomata.get().add_pixel(this, check, true);
        //             if(this.position.y>=CellularAutomata.get().get_height()){
        //                 this.force_still();
        //             }
        //             return;
        //         }
        //     }

        //     //Check below right
        //     check = new Vector2d(this.position.x+1, this.position.y+1);
        //     if(CellularAutomata.get().pos_allowed(check)){
        //         if(CellularAutomata.get().pos_empty(check,false)){
        //             this.position = check;
        //             CellularAutomata.get().add_pixel(this, check, true);
        //             if(this.position.y>=CellularAutomata.get().get_height()){
        //                 this.force_still();
        //             }
        //             return;
        //         }
        //     }
        // }

        // value = Math.random();

        // if(value > 0.5){
        //     //Check right
        //     check = new Vector2d(this.position.x+1, this.position.y);
        //     if(CellularAutomata.get().pos_allowed(check)){
        //         if(CellularAutomata.get().pos_empty(check,false)){
        //             this.position = check;
        //             CellularAutomata.get().add_pixel(this, check, true);
        //             return;
        //         }
        //     }

        //     //Check left
        //     check = new Vector2d(this.position.x-1, this.position.y);
        //     if(CellularAutomata.get().pos_allowed(check)){
        //         if(CellularAutomata.get().pos_empty(check,false)){
        //             this.position = check;
        //             CellularAutomata.get().add_pixel(this, check, true);
        //             return;
        //         }
        //     }
        // }
        // else{
        //     //Check left
        //     check = new Vector2d(this.position.x-1, this.position.y);
        //     if(CellularAutomata.get().pos_allowed(check)){
        //         if(CellularAutomata.get().pos_empty(check,false)){
        //             this.position = check;
        //             CellularAutomata.get().add_pixel(this, check, true);
        //             return;
        //         }
        //     }

        //     //Check right
        //     check = new Vector2d(this.position.x+1, this.position.y);
        //     if(CellularAutomata.get().pos_allowed(check)){
        //         if(CellularAutomata.get().pos_empty(check,false)){
        //             this.position = check;
        //             CellularAutomata.get().add_pixel(this, check, true);
        //             return;
        //         }
        //     }
        // }

    }
    
}
