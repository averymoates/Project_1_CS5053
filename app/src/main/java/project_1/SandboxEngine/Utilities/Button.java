package project_1.SandboxEngine.Utilities;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.glfw.GLFW;

public class Button {

    private float x, y, width, height;
    private String text;
    private Runnable action;

    public Button(float x, float y, float width, float height, String text, Runnable action) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.action = action;
    }

    // public void render() {
    //     // Assuming a modern OpenGL context (with shaders)

    //     // Set the color (change as needed)
    //     glColor3f(0.5f, 0.5f, 0.5f); // A grey color

    //     // Draw a rectangle for the button
    //     glBegin(GL_QUADS); // Begin drawing quads
    //         glVertex2f(x, y); // Top-left corner
    //         glVertex2f(x + width, y); // Top-right corner
    //         glVertex2f(x + width, y + height); // Bottom-right corner
    //         glVertex2f(x, y + height); // Bottom-left corner
    //     glEnd(); // End drawing
    // }

    public void update(long window) {
        // Here you should check if the mouse is over the button
        // and if the button is clicked
        // You can use GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_1) to check for clicks
        // and GLFW.glfwGetCursorPos(window, xPos, yPos) to get the mouse position

        double[] xPos = new double[1];
        double[] yPos = new double[1];
        GLFW.glfwGetCursorPos(window, xPos, yPos);

        if (xPos[0] > x && xPos[0] < x + width && yPos[0] > y && yPos[0] < y + height) {
            // The mouse is over the button
            if (GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS) {
                // The button is clicked
                if (action != null) {
                    action.run();
                }
            }
        }
    }
}
