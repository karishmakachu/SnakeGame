import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends  JPanel implements  ActionListener, KeyListener{
    private  class Tile{
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    Tile food;
    Random random;
    Timer gameLoop;
    int velcocityx;
    int velcocityy;
    boolean gameOver = false;
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new  Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();


        food = new Tile(10, 10);
        random = new  Random();
        placeFood();

        velcocityx = 0;
        velcocityy = 0;

        gameLoop = new Timer(100,this);
        gameLoop.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //grid
        for (int i = 0; i < boardWidth/tileSize; i++) {
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }
        //food
        g.setColor(Color.red);
       // g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);
            
        
        //head
        g.setColor(Color.green);
       // g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        //head
        for (int i = 0; i < snakeBody.size();i++) {
       Tile snakePart = snakeBody.get(i);
       //  g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
       g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
               }
        //score
        g.setFont(new  Font("Arial",Font.PLAIN, 16));
        if(gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }
    public void placeFood() {
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // Eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
    
        // Move the snake body
        for (int i = snakeBody.size() - 1; i > 0; i--) {
            Tile currentPart = snakeBody.get(i);
            Tile prevPart = snakeBody.get(i - 1);
            currentPart.x = prevPart.x;
            currentPart.y = prevPart.y;
        }
        if (!snakeBody.isEmpty()) {
            Tile firstPart = snakeBody.get(0);
            firstPart.x = snakeHead.x;
            firstPart.y = snakeHead.y;
        }
    
        // Move the snake head
        snakeHead.x += velcocityx;
        snakeHead.y += velcocityy;
    
        // Check collisions
        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
                return;
            }
        }
    
        // Check out-of-bounds
        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSize ||
            snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) {
            gameOver = true;
            return;
        }
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
      repaint();
      if (gameOver) {
        gameLoop.stop();
      }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velcocityy != 1) {
            velcocityx = 0;
            velcocityy = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velcocityy != -1) {
            velcocityx = 0;
            velcocityy = 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velcocityx != 1) {
            velcocityx = -1;
            velcocityy = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velcocityx != -1) {
            velcocityx = 1;
            velcocityy = 0;
        }
    }
    //not required
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
       
    }
    
}
