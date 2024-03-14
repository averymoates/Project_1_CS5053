# Computer Graphics Class Project

Create a Sandbox application

# Instructions

run these commands in the terminal word for word. This are the linux commands

`./gradlew clean`

`./gradlew installDist`

`./gradlew run`

# Create new Pixels

Depending on the pixel you want to implement, there are certain categories that are already created that you can extend from. These categories are:
- Element
    - Solid
        - Movable solid
    - Liquid
    - Gas
- Special

After the category has been picked, just extend the base class.

Things that all pixels need to have:
- Unique String name
- Unique int ID greater than 0
- RGB color values
- Position (x,y)

Things that you should change:
- `update()`

# Notes

Screen coordinates is the actual pixel values of the screen. Default is 1920X1080. The mouse/cursor uses screen coordinates. In the code, x refers to the width and goes left to right from 0-1920. Y refers to the height and goes from top to bottom from 0-1080.

Scene coordinates is scaled down from the screen coordinate by `SQUARE_SIZE`. Pixel positions uses scene coordinates. 

The drawing space is set to 190X100