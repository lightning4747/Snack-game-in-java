package snakegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    
    private List<Rectangle> body;
    private Direction direction;
    private final int unitSize;
    private int startX, startY;
    private boolean pendingGrowth = false; // <<< FIX: Flag to manage growth

    
    public Snake(int startX, int startY, int unitSize) {
        this.startX = startX;
        this.startY = startY;
        this.unitSize = unitSize;
        reset();
    }
    
    public void reset() {
        body = new ArrayList<>();
        // Initialize with a single head segment
        body.add(new Rectangle(startX, startY, unitSize, unitSize)); 
        direction = Direction.RIGHT;
        pendingGrowth = false;
    }
    
    public void move() {
        // Create new head based on current direction
        Rectangle head = body.get(0);
        // Create a copy of the current head for the new position
        Rectangle newHead = new Rectangle(head); 
        
        switch (direction) {
            case UP:
                newHead.y -= unitSize;
                break;
            case DOWN:
                newHead.y += unitSize;
                break;
            case LEFT:
                newHead.x -= unitSize;
                break;
            case RIGHT:
                newHead.x += unitSize;
                break;
        }
        
        // Add new head to the beginning
        body.add(0, newHead);
        
        // Remove tail (unless growth is pending)
        if (!pendingGrowth) { // <<< FIX: Only remove tail if not growing
            if (body.size() > 1) {
                body.remove(body.size() - 1);
            }
        } else {
            pendingGrowth = false;
        }
    }
    
    public void grow() {
        pendingGrowth = true; 
    }
    
    public void draw(Graphics g) {
        // Draw head with different color
        Rectangle head = body.get(0);
        g.setColor(Color.GREEN);
        g.fillRect(head.x, head.y, head.width, head.height);
        
        // Draw eyes
        g.setColor(Color.BLACK);
        int eyeSize = unitSize / 5;
        switch (direction) {
            case RIGHT:
                g.fillRect(head.x + head.width - eyeSize, head.y + eyeSize * 2, eyeSize, eyeSize);
                g.fillRect(head.x + head.width - eyeSize, head.y + head.height - eyeSize * 3, eyeSize, eyeSize);
                break;
            case LEFT:
                g.fillRect(head.x, head.y + eyeSize * 2, eyeSize, eyeSize);
                g.fillRect(head.x, head.y + head.height - eyeSize * 3, eyeSize, eyeSize);
                break;
            case UP:
                g.fillRect(head.x + eyeSize * 2, head.y, eyeSize, eyeSize);
                g.fillRect(head.x + head.width - eyeSize * 3, head.y, eyeSize, eyeSize);
                break;
            case DOWN:
                g.fillRect(head.x + eyeSize * 2, head.y + head.height - eyeSize, eyeSize, eyeSize);
                g.fillRect(head.x + head.width - eyeSize * 3, head.y + head.height - eyeSize, eyeSize, eyeSize);
                break;
        }
        
        // Draw body
        g.setColor(new Color(0, 200, 0)); // Slightly darker green
        for (int i = 1; i < body.size(); i++) {
            Rectangle segment = body.get(i);
            g.fillRect(segment.x, segment.y, segment.width, segment.height);
            
            // Add some detail to body segments
            g.setColor(Color.BLACK);
            g.drawRect(segment.x, segment.y, segment.width, segment.height);
            g.setColor(new Color(0, 200, 0));
        }
    }
    
    public boolean checkSelfCollision() {
        Rectangle head = body.get(0);
        
        // Check if head collides with any body segment (starting from index 1)
        for (int i = 1; i < body.size(); i++) {
            if (head.intersects(body.get(i))) {
                return true;
            }
        }
        return false;
    }
    
    // Getters and setters
    public Direction getDirection() {
        return direction;
    }
    
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    
    public int getHeadX() {
        return body.get(0).x;
    }
    
    public int getHeadY() {
        return body.get(0).y;
    }
    
    public List<Rectangle> getBody() {
        return body;
    }
}