# Computer Graphics Class Project

Create a Sandbox application

# Instructions

run these commands in the terminal word for word. This are the linux commands

`./gradlew clean`

`./gradlew installDist`

`./gradlew run`

# Create new Pixels

Create a new pixel by implementing the `Pixel` Class. Doing this will add abstact functions `update()` and `draw()`.

You need to name your new pixel and also give this new pixel a positive unique int value that is not being used by the other created pixels.

`update()` will be called every frame so if your next pixel requires animation or has movement, then put the logic in this function.

`draw()` is the function that will be called to draw the pixel onto the screen.

After the new pixel class is finished, you will need to add your pixel to the pixel selector. This can be done in the `SceneManager.java` class.

Look for the function called `create_selected_pixel(Vector2d position)`. In this function, follow the same format to add your pixel to the pixel selector.

# Notes

The `SceneManager.java` class is a public static class. So, if your pixel need to modify the pixels array that is stored in the `SceneManage.java` you should be able to do so.

You do this by doing this, `SceneManager.get_pixels()`. You will have to be careful doing this. The pixel array has no built in protection, so you can anything that you can to a normal `ArrayList`.

Screen coordinates is the actual pixel values of the screen. Default is 1920X1080. The mouse/cursor uses screen coordinates

Scene coordinates is scaled down from the screen coordinate by `SQUARE_SIZE`. Pixel positions uses scene coordinates