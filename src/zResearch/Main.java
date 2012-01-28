package zResearch;

import java.awt.Color;

import javax.swing.JFrame;

public class Main extends JFrame {

	public static void main(String[] args) {
		if (args.length > 0)
			new Main(Integer.parseInt(args[0]));
		else
			new Main(10);
	}

	public Main(int width) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(width, 300);
		setVisible(true);
	}
}
