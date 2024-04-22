package project_1.SandboxEngine.Pixel.Special;

import org.joml.Vector2d;

import project_1.SandboxEngine.Scene.CellularAutomata;

public class Conway extends Special {
    private boolean isAlive;

    public Conway(PixelType name, int id, Vector2d position, boolean isAlive) {
        super(name, id, position);
        this.isAlive = isAlive;
    }
    
    /*
     * Overrides the update method to go through all the pixels and run the Game of Life rules.
     */
    @Override
    public void update(){
        int aliveNeighbors = countAliveNeighbors();
        boolean nextState = applyGameofLifeRules(isAlive, aliveNeighbors);

        if (nextState != this.isAlive){
            this.isAlive = nextState;
        }
    }

    /*
     * Goes through and counts the four othogonal cells of the pixel at (x,y) 
     * 
     * | x
     * |xox
     * | x
     * 
     */
    private int countAliveNeighbors(){
        int count = 0;
        for (int dx = -1; dx <= 1; dx++){
            for (int dy = -1; dy <= 1; dy++){
                if (dx == 0 && dy == 0) continue;
                Vector2d neighorPos = new Vector2d(this.position.x + dx, this.position.y + dy);
                if (CellularAutomata.get().isCellAlive(neighorPos)){
                    count++;
                }
            }
        }

        return count;

    }

    /*
     * Applies game of lifes rules, you can read more here
     * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
     */
    private boolean applyGameofLifeRules(boolean currentState, int aliveNeighbors){
        if (currentState && (aliveNeighbors == 2 || aliveNeighbors == 3)){
            return true;
        } else if (!currentState && aliveNeighbors == 3){
            return true;
        } 
        return false;
    }

    public void setAlive(boolean state){
        this.isAlive = state;
    }

    public boolean getAlive(){
        return this.isAlive;
    }
}