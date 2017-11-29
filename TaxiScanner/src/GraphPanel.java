import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.*;

public class GraphPanel {
	JFrame frame;
	DrawPanel drawPanel;

	int[][] graph;
	
	int diameter;

	int length, height;

	int[][] taxis; // currentnode, destination node, xt, yt

	ArrayList<Integer[]> passengers = new ArrayList<>();

	public GraphPanel(int[][] graph, int numOfTaxis) {
		this.graph = graph;
		diameter = (int) Math.sqrt(graph.length);
		
		frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		drawPanel = new DrawPanel();

		frame.getContentPane().add(BorderLayout.CENTER, drawPanel);

		frame.setResizable(true);
		frame.setSize(600, 600);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);

		taxis = new int[numOfTaxis][4];
		// update("m 1 4 c");
		// update("p 1 8 c");
		// update("m 1 8 c");
		// update("d 1 8 c");
		// update("m 1 9 c");
	}

	class DrawPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g) {
			length = (this.getWidth() - 40) / (diameter-1);
			height = (this.getHeight() - 40) / (diameter-1);
			
			for (int i = 0; i < graph.length; i++) {
				int[] node = graph[i];
				for (int j : node) {
					int xa = (i % diameter) * length + 20;
					int ya = (i / diameter) * height + 20;
					int xb = (j % diameter) * length + 20;
					int yb = (j / diameter) * height + 20;

					g.drawLine(xa, ya, xb, yb);
				}
			}
			for (int[] taxi : taxis) {
				g.drawOval(taxi[2] - 3, taxi[3] - 3, 6, 6);
			}
			for (Integer[] passenger : passengers) {
				String str = "" + passenger[2];
				g.drawString(str, passenger[3] + 5, passenger[4] - 4);
			}
		}
	}

	void addTaxi(int i, int currentPos) {
		taxis[i][0] = currentPos;
	}

	void update(String output) {
		String[] splited = output.split("\\s+");
		for (int i = 0; i < splited.length - 1; i += 3) {
			int out = Integer.parseInt(splited[i + 1]);
			int out2 = Integer.parseInt(splited[i + 2]);
			taxis[(out - 1)][0] = taxis[(out - 1)][1];
			if (splited[i].equals("m")) {
				taxis[(out - 1)][1] = out2;
			} else if (splited[i].equals("p")) {
				for (Integer[] pass : passengers) {
					if (taxis[(out - 1)][0] == pass[1]) {
						pass[0] = out;
						System.out.println(out + " " + pass[0]);
						break;
					}
				}
			} else if (splited[i].equals("d")) {
				for (int j = 0; j < passengers.size(); j++) {
					//System.out.println(passengers.get(0)[0]);
					if (out == passengers.get(j)[0]) {
						passengers.get(j)[0] = 0;
						passengers.get(j)[1] = taxis[(out - 1)][0];
						if (passengers.get(j)[1] == passengers.get(j)[2]) {
							passengers.remove(j);
						}
						break;
					}
				}
			}
		}
		moveIt(taxis, passengers);
	}

	void addPass(int loc, int dest) {
		Integer[] pass = { 0, loc, dest, 0, 0 }; // current taxi, loc, dest, xt, yt
		passengers.add(pass);
	}

	private void moveIt(int[][] taxis, ArrayList<Integer[]> passengers) {
		for (int i = 1; i <= 20; i++) {
			for (int[] taxi : taxis) {
				int xa = (taxi[0] % diameter) * length + 20;
				int ya = (taxi[0] / diameter) * height + 20;
				int xb = (taxi[1] % diameter) * length + 20;
				int yb = (taxi[1] / diameter) * height + 20;

				double t = (0.05) * i;
				taxi[2] = (int) ((1 - t) * xa + t * xb);
				taxi[3] = (int) ((1 - t) * ya + t * yb);
			}
			for (int j = 0; j < passengers.size(); j++) {
				if (passengers.get(j)[0] == 0) {
					passengers.get(j)[3] = (passengers.get(j)[1] % diameter) * length + 20;
					passengers.get(j)[4] = (passengers.get(j)[1] / diameter) * height + 20;
				} else if (passengers.get(j)[0] == 0 && passengers.get(j)[1] == passengers.get(j)[2]) {
					passengers.remove(j);
				} else {
					passengers.get(j)[3] = taxis[passengers.get(j)[0] - 1][2];
					passengers.get(j)[4] = taxis[passengers.get(j)[0] - 1][3];
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			frame.repaint();
		}
	}

}