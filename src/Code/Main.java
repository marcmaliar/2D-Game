package Code;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setUndecorated(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		GameWorld gm = new GameWorld((int) width, (int) height);
		// GameWorld gm = new GameWorld(1000, 1000);
		frame.add(gm);
		frame.setContentPane(gm);
		frame.pack();
		frame.setVisible(true);

	}

}
