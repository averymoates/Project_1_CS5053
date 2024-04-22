// package project_1.SandboxEngine.Utilities;
// import static org.lwjgl.opengl.GL11.glBegin;
// import static org.lwjgl.opengl.GL11.glColor3f;
// import static org.lwjgl.opengl.GL11.glEnd;
// import static org.lwjgl.opengl.GL11.glVertex2f;

// import java.awt.Color;

// import org.lwjgl.glfw.GLFW;

// public class Button {

//     private float x, y, width, height;
//     private String text;
//     private Color color;

//     public Button(float x, float y, float width, float height, String text, Color color) {
//         this.x = x;
//         this.y = y;
//         this.width = width;
//         this.height = height;
//         this.text = text;
//         this.color = color;
//     }

//     public boolean clicked(float mouseX, float mouseY) {
//         return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
//     }

//     public void render() {
//         // Assuming a modern OpenGL context (with shaders)

//         // Set the color (change as needed)
//         glColor3f(0.5f, 0.5f, 0.5f); // A grey color

//         // Draw a rectangle for the button
//         glBegin(GL_QUADS); // Begin drawing quads
//             glVertex2f(x, y); // Top-left corner
//             glVertex2f(x + width, y); // Top-right corner
//             glVertex2f(x + width, y + height); // Bottom-right corner
//             glVertex2f(x, y + height); // Bottom-left corner
//         glEnd(); // End drawing
//     }

// }
package project_1.SandboxEngine.Utilities;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import org.lwjgl.glfw.GLFW;

public class Button {

    private float x, y, width, height;
    private String text;
    private Color color;
    private int listBase;

    public Button(float x, float y, float width, float height, String text, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.color = color;

        // // Setup for bitmap fonts
        // listBase = glGenLists(256);
        // setupText(listBase, x, y);
    }

    public boolean clicked(float mouseX, float mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public void render() {
        // draw red button
        glColor3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
        glBegin(GL_QUADS);
            glVertex2f(x, y);
            glVertex2f(x + width, y);
            glVertex2f(x + width, y + height);
            glVertex2f(x, y + height);
        glEnd();
    
        // // Improved centering calculations
        // int textWidth = text.length() * 8; // Adjust this value based on your actual font width
        // float textX = x + (width - textWidth) / 2;
        // float textY = y + (height / 2) + 4; // Assuming the text height is about 8 pixels
    
        // glColor3f(0f, 0f, 0f); // Set text color
        // renderText(text, textX, textY);
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
