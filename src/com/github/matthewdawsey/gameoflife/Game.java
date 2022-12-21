package com.github.matthewdawsey.gameoflife;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Game {
	private Cell[][] cells = new Cell[256][256];
	private int scale = 16;
	
	private Random rand = new Random();
	private Color deadCellColor = Color.darkGray, aliveCellColor = Color.lightGray;
	
	public Game() {
		this.resetCells();
	}
	
	public void update() {
		Cell[][] generation = new Cell[this.cells.length][this.cells[0].length];
		for (int x = 0; x < generation.length; x ++) {
			for (int y = 0; y < generation[x].length; y ++) {
				generation[x][y] = Cell.DEAD;
			}
		}
		
		for (int x = 0; x < this.cells.length; x ++) {
			for (int y = 0; y < this.cells[x].length; y ++) {
				int aliveNeighbors = 0;
				for (int i = -1; i < 2; i ++) {
					for (int j = -1; j < 2; j ++) {
						if (i == 0 && j == 0)
							continue;
						try {
							if (this.cells[x + i][y + j] == Cell.ALIVE)
								aliveNeighbors ++;
						} catch (ArrayIndexOutOfBoundsException e) {
							continue;
						}
					}
				}
				
				Cell cell = this.cells[x][y];
				if (cell == Cell.ALIVE) {
					if (aliveNeighbors == 2 || aliveNeighbors == 3)
						generation[x][y] = Cell.ALIVE;
				} else if (cell == Cell.DEAD) {
					if (aliveNeighbors == 3)
						generation[x][y] = Cell.ALIVE;
				} else {
					generation[x][y] = Cell.DEAD;
				}
			}
		}
		
		this.cells = generation;
	}
	
	public void render(Graphics g) {
		for (int x = 0; x < this.cells.length; x ++) {
			for (int y = 0; y < this.cells[x].length; y ++) {
				Cell cell = this.cells[x][y];
				if (cell == Cell.DEAD)
					g.setColor(this.deadCellColor);
				else if (cell == Cell.ALIVE)
					g.setColor(this.aliveCellColor);
				
				g.fillRect(x * this.scale, y * this.scale, this.scale, this.scale);
				g.setColor(Color.black);
				g.drawRect(x * this.scale, y * this.scale, this.scale, this.scale);
			}
		}
	}
	
	public void createGlider(int x, int y, int direction) {
		switch (direction) {
		case 0:
			this.cells[x - 1][y - 1] = Cell.ALIVE;
			this.cells[x][y - 1] = Cell.ALIVE;
			this.cells[x + 1][y - 1] = Cell.ALIVE;
			this.cells[x - 1][y] = Cell.ALIVE;
			this.cells[x][y + 1] = Cell.ALIVE;
			break;
		case 1:
			this.cells[x - 1][y - 1] = Cell.ALIVE;
			this.cells[x - 1][y] = Cell.ALIVE;
			this.cells[x - 1][y + 1] = Cell.ALIVE;
			this.cells[x][y + 1] = Cell.ALIVE;
			this.cells[x + 1][y] = Cell.ALIVE;
			break;
		case 2:
			this.cells[x + 1][y - 1] = Cell.ALIVE;
			this.cells[x + 1][y] = Cell.ALIVE;
			this.cells[x + 1][y + 1] = Cell.ALIVE;
			this.cells[x][y + 1] = Cell.ALIVE;
			this.cells[x - 1][y] = Cell.ALIVE;
			break;
		case 3:
			this.cells[x - 1][y - 1] = Cell.ALIVE;
			this.cells[x][y - 1] = Cell.ALIVE;
			this.cells[x + 1][y - 1] = Cell.ALIVE;
			this.cells[x + 1][y] = Cell.ALIVE;
			this.cells[x][y + 1] = Cell.ALIVE;
			break;
		}
	}
	
	public void randomizeCells(int probability) {
		for (int x = 0; x < this.cells.length; x ++) {
			for (int y = 0; y < this.cells[x].length; y ++) {
				this.cells[x][y] = Cell.DEAD;
				if (this.rand.nextInt(probability) == 0)
					this.cells[x][y] = Cell.ALIVE;
			}
		}
	}
	
	public void resetCells() {
		for (int x = 0; x < this.cells.length; x ++) {
			for (int y = 0; y < this.cells[x].length; y ++) {
				this.cells[x][y] = Cell.DEAD;
			}
		}
	}
	
	public void setDeadCellColor(Color color) {
		this.deadCellColor = color;
	}
	
	public void setAliveCellColor(Color color) {
		this.aliveCellColor = color;
	}
	
	public Color getDeadCellColor() {
		return this.deadCellColor;
	}
	
	public Color getAliveCellColor() {
		return this.aliveCellColor;
	}
	
	public void setScale(int scale) {
		this.scale = scale;
	}
	
	public int getScale() {
		return this.scale;
	}
}
