package snakegame;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

public class Food {
    
    private int x;
    private int y;
    private final int unitSize;
    private final int maxX;
    private final int maxY;
    private final Random random;
    
    public Food(int width, int height, int unitSize) {
        this.unitSize = unitSize;
        this.maxX = width - unitSize;
        this.maxY = height - unitSize;
        this.random = new Random();
    }
    
    public void generateNewPosition(List<Rectangle> snakeBody) {
        boolean validPosition = false;
        
        while (!validPosition) {
            // Generate random position on grid
            x = (random.nextInt(maxX / unitSize) * unitSize);
            y = (random.nextInt(maxY / unitSize) * unitSize);
            
            // Check if food doesn't overlap with snake
            validPosition = true;
            Rectangle foodRect = new Rectangle(x, y, unitSize, unitSize);
            
            for (Rectangle segment : snakeBody) {
                if (foodRect.intersects(segment)) {
                    validPosition = false;
                    break;
                }
            }
        }
    }
    
    // Getters
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}