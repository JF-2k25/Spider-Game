import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class SpiderGame extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 750;
    int boardHeight = 250;

    // images
    Image spiderImg;
    Image spiderdeadImg;
    Image spider2Img;
    Image btoy1Img;
    Image btoy2Img;
    Image ground1Img;

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image img;

        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    //spider
    int spiderWidth = 88;
    int spiderHeight = 88;
    int spiderX = 50;
    int spiderY = boardHeight - spiderHeight;

    Block spider;

    //toy block
    int btoy1Width = 88;
    int btoy2Width = 88;

    int btoyHeight = 88;
    int btoyX = 700;
    int btoyY = boardHeight - btoyHeight;
    ArrayList<Block> btoyArray;

    //physics
    int velocityX = -12; //toy block moving left speed
    int velocityY = 0; //spider jump speed
    int gravity = 1;

    boolean gameOver = false;
    int score = 0;

    Timer gameLoop;
    Timer placeBtoyTimer;

    public SpiderGame() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);

        spiderImg = new ImageIcon(getClass().getResource("./img/spider walking.gif")).getImage();
        spiderdeadImg = new ImageIcon(getClass().getResource("./img/spider-dead.png")).getImage();
        spider2Img = new ImageIcon(getClass().getResource("./img/spider2.png")).getImage();
        btoy1Img = new ImageIcon(getClass().getResource("./img/btoy1.png")).getImage();
        btoy2Img = new ImageIcon(getClass().getResource("./img/btoy2.png")).getImage();
        ground1Img = new ImageIcon(getClass().getResource("./img/ground1.png")).getImage();
        
        //spider
        spider = new Block(spiderX, spiderY, spiderWidth, spiderHeight, spiderImg);

        //toy block
        btoyArray = new ArrayList<Block>();

        //game timer
        gameLoop = new Timer(1000/60, this); //1000/60 = 60 frames per 1000ms (1s), update
        gameLoop.start();

        //place toy block timer
        placeBtoyTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeBtoy();
            }
        });
        placeBtoyTimer.start();
    }

    void placeBtoy() {
        if (gameOver) {
            return;
        }

        double placeBtoyChance = Math.random(); //0 - 0.999999
        if (placeBtoyChance > .90) { //10% you get btoy2
            Block btoy = new Block(btoyX, btoyY, btoy2Width, btoyHeight, btoy2Img);
            btoyArray.add(btoy);
        }
        else if (placeBtoyChance > .70) { //20% you get btoy1
            Block btoy = new Block(btoyX, btoyY, btoy1Width, btoyHeight, btoy1Img);
            btoyArray.add(btoy);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //spider
        g.drawImage(spider.img, spider.x, spider.y, spider.width, spider.height, null);

        //toy block
        for (int i = 0; i < btoyArray.size(); i++) {
            Block btoy = btoyArray.get(i);
            g.drawImage(btoy.img, btoy.x, btoy.y, btoy.width, btoy.height, null);
        }

        //score
        g.setColor(Color.black);
        g.setFont(new Font("Courier", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), 10, 35);
        }
        else {
            g.drawString(String.valueOf(score), 10, 15);
        }
    }
    
    public void move() {
        //spider
        velocityY += gravity;
        spider.y += velocityY;

        if (spider.y > spiderY) { //stop the spider from falling through the floor
            spider.y = spiderY;
            velocityY = 0;
            spider.img = spiderImg;
        }

        //toy block
        for (int i = 0; i < btoyArray.size(); i++) {
            Block btoy = btoyArray.get(i);
            btoy.x += velocityX;

            if (collision(spider, btoy)) {
                gameOver = true;
                spider.img = spiderdeadImg;
            }
        }

        //score
        score++;
    }

    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&    //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&    //a's top right corner passes b's top left corner
               a.y < b.y + b.height &&   //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;     //a's bottom left corner pases b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver) {
            placeBtoyTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
           // System.out.println("JUMP!");
           if (spider.y == spiderY) {
               velocityY = -17;
               spider.img = spiderImg;
            }

            if (gameOver) {
                //restart game by resetting conditions
                spider.y = spiderY;
                spider.img = spiderImg;
                velocityY = 0;
                btoyArray.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placeBtoyTimer.start();
            }
        }
    } 

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
