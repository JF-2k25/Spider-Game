import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 750;
        int boardHeight = 250;

        JFrame frame = new JFrame("Spider Game");
        // frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SpiderGame SpiderGame = new SpiderGame();
        frame.add(SpiderGame);
        frame.pack();
        SpiderGame.requestFocus();
        frame.setVisible(true);
    }
}
