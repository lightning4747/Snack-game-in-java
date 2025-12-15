package snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    
    // Game constants
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int UNIT_SIZE = 20;
    // private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE); just for refernce in the future.
    private static final int DELAY = 100; // in ms
    
    // Game objects
    private final Snake snake;
    private final Food food; // Assuming Food class exists
    
    // Game state
    private boolean running = false; 
    private boolean gameOver = false; 
    private int score = 0;
    
    // Timer for game loop
    private Timer timer;
    
    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        
        snake = new Snake(WIDTH / 2, HEIGHT / 2, UNIT_SIZE);
        food = new Food(WIDTH, HEIGHT, UNIT_SIZE);
    }
    
    public void startGame() {
        running = true;
        gameOver = false;
        score = 0;
        snake.reset();
        food.generateNewPosition(snake.getBody());
        
        // Ensure timer is only started once or restarted properly
        if (timer == null) {
            timer = new Timer(DELAY, this);
        } else {
            timer.stop(); // Stop previous timer if any
            timer.setDelay(DELAY); // Reset delay if needed
        }
        timer.start();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    private void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // Improve drawing quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (running) {
            // Draw food
            g.setColor(Color.RED);
            g.fillOval(food.getX(), food.getY(), UNIT_SIZE, UNIT_SIZE);
            
            // Draw snake
            snake.draw(g);
            
            // Draw score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Score: " + score, 10, 20);
        } else if (gameOver) {
            gameOver(g);
        } else {
            // Initial state: not running and not game over
            startScreen(g);
        }
    }
    
    private void startScreen(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        
        String message = "SNAKE GAME";
        int messageWidth = g.getFontMetrics().stringWidth(message);
        g.drawString(message, (WIDTH - messageWidth) / 2, HEIGHT / 2 - 50);
        
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        message = "Press SPACE to Start";
        messageWidth = g.getFontMetrics().stringWidth(message);
        g.drawString(message, (WIDTH - messageWidth) / 2, HEIGHT / 2);
        
        message = "Use Arrow Keys to Move";
        messageWidth = g.getFontMetrics().stringWidth(message);
        g.drawString(message, (WIDTH - messageWidth) / 2, HEIGHT / 2 + 30);
    }
    
    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        
        String message = "Game Over";
        int messageWidth = g.getFontMetrics().stringWidth(message);
        g.drawString(message, (WIDTH - messageWidth) / 2, HEIGHT / 2 - 50);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 25));
        message = "Score: " + score;
        messageWidth = g.getFontMetrics().stringWidth(message);
        g.drawString(message, (WIDTH - messageWidth) / 2, HEIGHT / 2);
        
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        message = "Press SPACE to Restart";
        messageWidth = g.getFontMetrics().stringWidth(message);
        g.drawString(message, (WIDTH - messageWidth) / 2, HEIGHT / 2 + 50);
    }
    
    private void checkFood() {
        if (snake.getHeadX() == food.getX() && snake.getHeadY() == food.getY()) {
            snake.grow();
            score++;
            food.generateNewPosition(snake.getBody());
        }
    }
    
    private void checkCollisions() {
        // Check wall collision
        if (snake.getHeadX() < 0 || snake.getHeadX() >= WIDTH || 
            snake.getHeadY() < 0 || snake.getHeadY() >= HEIGHT) {
            
            running = false;
            gameOver = true;
        }
        
        // Check self collision
        if (snake.checkSelfCollision()) {
            running = false;
            gameOver = true;
        }

        if (!running && timer != null) {
            timer.stop();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            snake.move();
            checkFood();
            checkCollisions();
        }
        // Repaint is always called to update the screen (either game or game over/start screen)
        repaint(); 
    }
    
    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            
            if (key == KeyEvent.VK_SPACE) {
                // If not running (start screen or game over screen)
                if (!running) { // <<< Fixed logic: if the game isn't running, start it
                    startGame();
                }
                return; // Consume space key
            }
            
            if (running) {
                // Direction changes are only allowed if the game is running
                Snake.Direction currentDir = snake.getDirection();
                
                switch (key) {
                    case KeyEvent.VK_LEFT:
                        if (currentDir != Snake.Direction.RIGHT) {
                            snake.setDirection(Snake.Direction.LEFT);
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (currentDir != Snake.Direction.LEFT) {
                            snake.setDirection(Snake.Direction.RIGHT);
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (currentDir != Snake.Direction.DOWN) {
                            snake.setDirection(Snake.Direction.UP);
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (currentDir != Snake.Direction.UP) {
                            snake.setDirection(Snake.Direction.DOWN);
                        }
                        break;
                }
            }
        }
    }
}