
package project_1.SandboxEngine.Utilities;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL30.*;  // For glGenerateMipmap

import org.lwjgl.opengl.GL11;

import java.awt.Color;
import org.lwjgl.glfw.GLFW;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;


/**
 * A class to create a simple axis-aligned rectangular button, with the option to overlay an image texture
 * 
 * @author Jack Cornette
 */
public class Button {

    private float x, y, width, height;
    private String filePath;
    private Color color;
    private int listBase;
    private int textureId;
    private boolean selected;

    /**
     * @x top right corner x
     * @y top right corner y
     * @width x-length
     * @height y-length
     * @filePath path to image you want shown on this button
     * @color shading ontop the image, Color.White does not effect the image
     */
    public Button(float x, float y, float width, float height, String filePath, Color color) {
        this.x = x; // 0-1
        this.y = y;
        this.width = width;
        this.height = height;
        this.filePath = filePath;
        this.color = color;

        if (filePath == "") this.textureId = -1;
        else this.textureId = loadTexture(filePath);

        this.selected = false;
    }
    /** get/set used for shading */
    public boolean isSelected() {
        return this.selected;
    }
    public void selected(boolean selected) {
        this.selected = selected;
    }
    public void changeImage(String filePath) {
        if (filePath == "") this.textureId = -1;
        else this.textureId = loadTexture(filePath);
    }
    

    public void translate(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    /** used to tell if user clicked on simple axis-aligned rectangle */
    public boolean clicked(float mouseX, float mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    /** continually call this function to draw the current state of the button */
    public void render(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // outline button
        if (this.selected) {
            glColor3f(1f, 1f, 1f);
        }
        else {
            glColor3f(0f, 0f, 0f);
        }
        glBegin(GL_LINE_LOOP);
            glVertex2f(x, y);
            glVertex2f(x + width, y);
            glVertex2f(x + width, y + height);
            glVertex2f(x, y + height);
        glEnd();

        // fill button (shaded darker if currently selected)
        if (this.selected) {
            glColor3f(color.getRed()*1.5f / 255.0f, color.getGreen()*1.5f / 255.0f, color.getBlue()*1.5f / 255.0f);
        }
        else {
            glColor3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
        }

       // fill button
        glBegin(GL_QUADS);
            glVertex2f(x, y);
            glVertex2f(x + width, y);
            glVertex2f(x + width, y + height);
            glVertex2f(x, y + height);
        glEnd();
        

        // add texture to button
        if (textureId != -1) {
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, textureId);

            glBegin(GL_QUADS);
            glTexCoord2f(0, 0); glVertex2f(x, y);  // Top-left
            glTexCoord2f(1, 0); glVertex2f(x + width, y);   // Top-right
            glTexCoord2f(1, 1); glVertex2f(x + width, y + height);  // Bottom-right
            glTexCoord2f(0, 1); glVertex2f(x, y + height); // Bottom-left
            glEnd();

            glDisable(GL_TEXTURE_2D);
        }
    }

    /** loads texture image from given filePath and returns its textureID */
    public static int loadTexture(String filePath) {
        int width, height;
        ByteBuffer image;
    
        // Load the image
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer comp = stack.mallocInt(1);
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
    
            // Load image with STB
            image = STBImage.stbi_load(filePath, w, h, comp, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load a texture file!" + System.lineSeparator() + STBImage.stbi_failure_reason());
            }
    
            width = w.get();
            height = h.get();
        }
    
        // Generate a texture ID
        int textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
    
        // Setup wrap mode and texture filtering options
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    
        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        glGenerateMipmap(GL_TEXTURE_2D);
    
        // Free the image memory
        STBImage.stbi_image_free(image);
    
        return textureID;
    }
    
}
