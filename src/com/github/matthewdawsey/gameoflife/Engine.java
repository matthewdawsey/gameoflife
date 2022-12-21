package com.github.matthewdawsey.gameoflife;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

public class Engine extends JPanel implements ActionListener, Scrollable, Runnable {
	private Game game = new Game();
	
	private JFrame frame = new JFrame("Game of Life");
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuOptions = new JMenu("Options");
	private JMenuItem menuItemReset = new JMenuItem("Reset");
	private JMenuItem menuItemRandomize = new JMenuItem("Randomize");
	private JMenuItem menuItemCreateGlider = new JMenuItem("Create Glider");
	private JMenuItem menuItemDeadCellColor = new JMenuItem("Set Color(dead)");
	private JMenuItem menuItemAliveCellColor = new JMenuItem("Set Color(alive)");
	private JMenuItem menuItemExit = new JMenuItem("Exit");
	
	private Thread thread = new Thread(this);
	private volatile boolean running = false;
	
	public Engine() {
		final Engine instance = this;
		this.menuItemReset.addActionListener(this);
		this.menuItemRandomize.addActionListener(this);
		this.menuItemCreateGlider.addActionListener(this);
		this.menuItemDeadCellColor.addActionListener(this);
		this.menuItemAliveCellColor.addActionListener(this);
		this.menuItemExit.addActionListener(this);
		
		Dimension size = new Dimension(256 * this.game.getScale(), 256 * this.game.getScale());
		this.setSize(size);
		this.setPreferredSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.frame.setLayout(new BorderLayout());
		
		this.menuOptions.add(this.menuItemReset);
		this.menuOptions.add(this.menuItemRandomize);
		this.menuOptions.add(this.menuItemCreateGlider);
		this.menuOptions.add(this.menuItemDeadCellColor);
		this.menuOptions.add(this.menuItemAliveCellColor);
		this.menuOptions.add(this.menuItemExit);
		this.menuBar.add(this.menuOptions);
		this.frame.add(this.menuBar, BorderLayout.NORTH);
		this.frame.add(new JScrollPane(this), BorderLayout.CENTER);
		
		this.frame.setSize(720, 640);
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o instanceof JMenuItem) {
			String item = ((JMenuItem) o).getText();
			switch (item) {
			case "Reset":
				this.game.resetCells();
				break;
			case "Randomize":
				this.game.randomizeCells(8);
				break;
			case "Create Glider":
				int x = 0, y = 0, d = 0;
				try {
					x = Integer.parseInt(JOptionPane.showInputDialog("X"));
					y = Integer.parseInt(JOptionPane.showInputDialog("Y"));
					d = Integer.parseInt(JOptionPane.showInputDialog("Direction (0-3)"));
				} catch (NumberFormatException ex) {
					System.out.println("Invalid coordinates");
					break;
				}
				
				this.game.createGlider(x, y, d);
				break;
			case "Set Color(dead)":
				Color deadColor = JColorChooser.showDialog(this.frame, "Set Color(dead)", this.game.getDeadCellColor());
				if (deadColor != null)
					this.game.setDeadCellColor(deadColor);
				break;
			case "Set Color(alive)":
				Color aliveColor = JColorChooser.showDialog(this.frame, "Set Color(alive)", this.game.getAliveCellColor());
				if (aliveColor != null)
					this.game.setAliveCellColor(aliveColor);
				break;
			case "Exit":
				this.frame.dispose();
				this.stop();
				break;
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		this.game.render(g);
	}
	
	@Override
	public void run() {
		long lastTime = System.currentTimeMillis(), lastUpdate = System.currentTimeMillis(), lastRender = System.currentTimeMillis();
		int updates = 0, frames = 0, updateLimit = 10, renderLimit = 30;
		this.log(Level.INFO, "Running");
		while (this.running) {
			if (!this.frame.isVisible())
				this.stop();
			
			if (System.currentTimeMillis() - lastUpdate >= (1000 / updateLimit)) {
				this.game.update();
				lastUpdate = System.currentTimeMillis();
				updates ++;
			}
			
			if (System.currentTimeMillis() - lastRender >= (1000 / renderLimit)) {
				this.repaint();
				lastRender = System.currentTimeMillis();
				frames ++;
			}
			
			if (System.currentTimeMillis() - lastTime >= 1000) {
				this.log(Level.INFO, updates + " updates / sec; " + frames + " frames / sec");
				updates = 0;
				frames = 0;
				lastTime = System.currentTimeMillis();
			}
		}
	}
	
	public synchronized void start() {
		this.log(Level.INFO, "Starting");
		this.running = true;
		this.thread.start();
	}
	
	public synchronized void stop() {
		this.log(Level.INFO, "Stopping");
		this.running = false;
	}
	
	public void log(Level level, String info) {
		System.out.println("[" + level.getName() + "]: " + info);
	}
	
	public static void main(String[] args) {
		Engine engine = new Engine();
		engine.start();
	}

	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(720, 640);
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 128;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 128;
	}
}
