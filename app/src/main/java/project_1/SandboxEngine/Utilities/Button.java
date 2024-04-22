
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

public class Button {

    private float x, y, width, height;
    private String filePath;
    private Color color;
    private int listBase;
    private int textureId;

    public Button(float x, float y, float width, float height, String filePath, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.filePath = filePath;
        this.color = color;
        this.textureId = loadTexture(filePath);

        // // Setup for bitmap fonts
        // listBase = glGenLists(256);
        // setupText(listBase, x, y);
    }

    public boolean clicked(float mouseX, float mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public void render() {
        // outline button
        glColor3f(0f, 0f, 0f);
        // glLineWidth(5.0f);
        glBegin(GL_LINE_LOOP);
            glVertex2f(x, y);
            glVertex2f(x + width, y);
            glVertex2f(x + width, y + height);
            glVertex2f(x, y + height);
        glEnd();
        // fill button
        glColor3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
        glBegin(GL_QUADS);
            glVertex2f(x, y);
            glVertex2f(x + width, y);
            glVertex2f(x + width, y + height);
            glVertex2f(x, y + height);
        glEnd();
        // border
        

        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureId);

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2f(x, y);  // Top-left
        glTexCoord2f(1, 0); glVertex2f(x + width, y);   // Top-right
        glTexCoord2f(1, 1); glVertex2f(x + width, y + height);  // Bottom-right
        glTexCoord2f(0, 1); glVertex2f(x, y + height); // Bottom-left
        glEnd();

        glDisable(GL_TEXTURE_2D);

    
        // // Improved centering calculations
        // int textWidth = text.length() * 8; // Adjust this value based on your actual font width
        // float textX = x + (width - textWidth) / 2;
        // float textY = y + (height / 2) + 4; // Assuming the text height is about 8 pixels
    
        // glColor3f(0f, 0f, 0f); // Set text color
        // renderText(text, textX, textY);
    }

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
    

    // private void renderText(String text, float x, float y) {
    //     glPushAttrib(GL_LIST_BIT);
    //     glListBase(listBase - 32 + 128);
    //     glRasterPos2f(x, y);
    //     for (char c : text.toCharArray()) {
    //         glCallList(listBase + c);
    //     }
    //     glPopAttrib();
    // }

    // private void setupText(int listBase, float x, float y) {
    //     // Assume an 8x8 bitmap font
    //     for (int i = 0; i < 256; i++) {
    //         glNewList(listBase + i, GL_COMPILE);
    //         glBegin(GL_LINES);
    //             glVertex2f(x, y);
    //             glVertex2f(x+32, y);
    //             glVertex2f(x+32, y+32);
    //             glVertex2f(x, y+32);
    //         glEnd();
    //         glTranslated(8, 0, 0);
    //         glEndList();
    //     }
    // }
}
