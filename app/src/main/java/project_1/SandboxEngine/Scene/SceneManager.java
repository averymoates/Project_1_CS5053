package project_1.SandboxEngine.Scene;

import java.util.*;

import org.joml.Vector2d;

import project_1.SandboxEngine.MouseListener;
import project_1.SandboxEngine.Pixel.Blank_pixel;
import project_1.SandboxEngine.Pixel.Pixel;

/**
 * Author: Avery Moates
 * Date:   3/11/2024
 */

public class SceneManager {

    private static SceneManager instance = null;

    private ArrayList<Pixel> pixels = null;

    private int width;
    private int height;


    private SceneManager(){
        pixels = new ArrayList<Pixel>();

    }

    public static SceneManager get(){
        if(SceneManager.instance == null){
            SceneManager.instance = new SceneManager();
        }

        return SceneManager.instance;
    }

    public static void add_pixel(Pixel pixel){
        SceneManager.get().pixels.add(pixel);
    }

    public static boolean remove_pixel(Pixel pixel){
        if(SceneManager.get().pixels.size() == 0){
            System.out.println("Pixel array empty");
            return false;
        }
        else{
            SceneManager.get().pixels.remove(pixel);
            return true;
        }
    }

    public static void update(){
        SceneManager.pull_events();

        for(Pixel pixel: SceneManager.get().pixels){
            pixel.update();
        }
    }

    public static void draw(){
        for(Pixel pixel: SceneManager.get().pixels){
            pixel.draw();
        }
    }

    private static void pull_events(){
        if(MouseListener.isMouseButtonDown(0)){
            Vector2d position = MouseListener.mouse_loc_in_screen();
            Blank_pixel pixel = new Blank_pixel(position);
            SceneManager.add_pixel(pixel);
        }

    }

    public static void set_width(int w){
        SceneManager.get().width = w;
    }

    public static void set_height(int h){
        SceneManager.get().height = h;
    }

    public static int get_width(){
        return SceneManager.get().width;
    }

    public static int get_height(){
        return SceneManager.get().height;
    }


    
}
