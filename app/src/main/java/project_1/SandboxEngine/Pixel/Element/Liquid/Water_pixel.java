package project_1.SandboxEngine.Pixel.Element.Liquid;

import org.joml.Vector2d;

import project_1.SandboxEngine.Pixel.PixelType;

public class Water_pixel extends Liquid{

    public Water_pixel(Vector2d position) {
        super(PixelType.WATER, 2, 1, 3, 1, position);
        this.red = 108.0f/256.0f;
        this.green = 148.0f/256.0f;
        this.blue = 230.0f/256.0f;
    }
    
}
