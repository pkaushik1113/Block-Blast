package blocks;

import java.awt.Point;
import engine.*;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockWorld extends World implements Property{
	
	private int[][] board;
	private Point[][] coordinates;
	private boolean[][] cellSearched1;
	private int[][] ifFits;
	private ArrayList<Block> blocks;
	private int points;
	private int width = 400;
	private int height = 700;
	private boolean willFit;
	private boolean first = true;
	private List bl;
	ImageView grid;
	Point spawn1;
	Point spawn2;
	Point spawn3;
	ArrayList<Shape> waiting = new ArrayList();
	ArrayList<Shape> incoming = new ArrayList();
	ArrayList<ChangeListener> listeners = new ArrayList<>();
	
	Color shadow = Color.rgb(49,70,123);
	
	private boolean run = true;
	
	// Link to Codes: 
	// https://docs.google.com/document/d/1-4u3s7Nzq2KX-jv9OThlMa9PCgvUaaozA62XcXeL2mA/edit?usp=sharing
	String[] shapes = new String[] {
			"L1", //L's
			"L2",
			"L3",
			"L4",
			"L5",
			"L6",
			"L7",
			"L8",
			
			"Q1", //Squares		9
			"Q2",
			"Q3",
			
			"R1", //Rectangles	12
			"R2",
			
			"S1", //S's			14
			"S2",
			"S3",
			"S4",
			
			"VL1", //Lines: vertical	18
			"VL2",
			"VL3",
			"VL4",
			
			"HL1", //Lines: horizontal	22
			"HL2",
			"HL3",
			"HL4",
			
			"V1", //V's			26
			"V2", 
			"V3",
			"V4",
			"V5",
			"V6",
			"V7",
			"V8",
			
			"T1", //T's			34
			"T2",
			"T3",
			"T4",
			
	};
	
	
	String[] colors = new String[] {
			"red",
			"orange",
			"yellow",
			"green",
			"cyan",
			"blue",
			"purple",
			"pink"
	};
	
	public int hi = 0;
	
	
	
	public BlockWorld() {
		super();
		setWidth(width);
		setHeight(height);
		setPrefSize(width, height);
	}
	
	public void act(long now) {
		
//		if(isEmpty() && hi != 0) {
//			fillAnimation();
//
//		}
		

		for(int i = 0; i < waiting.size(); i++) {
			Shape s = waiting.get(i);
			if(s.onBoard()) {
				waiting.remove(i);
				i--;
			}
		}	
		
		if(waiting.size() > 0 && !canFit()) {
			checkBoard();
			if(!canFit()) {
				setValue(false);
			}
			
		}
		
		if (waiting.size() == 0) {
			addBlocks();
		}
		
		checkBoard();
	}
	
	public void onDimensionsInitialized() {
		
		Rectangle shad = new Rectangle(330, 330, shadow);
		shad.setX(width/2 - shad.getWidth()/2);
		shad.setY(height* 2/5 - shad.getHeight()/2);
		getChildren().add(shad);
		
		Image b = new Image("images/gridboard.png", 320, 320, true, false);
		grid = new ImageView(b);
		grid.setX(width/2 - grid.getImage().getWidth()/2);
		grid.setY(height * 2/5 - grid.getImage().getHeight()/2);
		getChildren().add(grid);
		
		Shape s = new Shape();
		
		Block b0 = new Block("cyan", s, new Point(0, 0));
				
		spawn1 = new Point((int)(width/7 - b0.getWidth()/2), (int)(height/2 + grid.getImage().getHeight()/2) + 30);
		spawn2 = new Point((int)(width/2 - b0.getWidth()/2), (int)(height/2 + grid.getImage().getHeight()/2)+ 30);
		spawn3 = new Point((int)(width * 6/7 - b0.getWidth()/2), (int)(height/2 + grid.getImage().getHeight()/2) + 30);

		
		board = new int[8][8];
		coordinates = new Point[8][8];
		cellSearched1 = new boolean[8][8];
		ifFits = new int[8][8];
		points = 0;
		
		clearBoard();
		
		int x = (int)(width/2 - grid.getImage().getWidth()/2);
		int y = (int)(height * 2/5 - grid.getImage().getWidth()/2);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				coordinates[i][j] = new Point(x,y);
				x += 40;
			}
			y += 40;
			x = (int)(width/2 - grid.getImage().getWidth()/2);
		}
		
		timer.start();
		
	}
	
	public void checkBoard() {
		if (checkEntireBoard()) {
			points += 300;
			clearBoard();
		} else {
			ArrayList<Integer> rows = new ArrayList<>();
			
			for (int i = 0; i < 8; i++) {
				if (checkRow(i)) {
					rows.add(i);
//					points += 50;
//					clearRow(i);
				}
			}
			
			ArrayList<Integer> cols = new ArrayList<>();
			
			for (int i = 0; i < 8; i++) {
				if (checkCol(i)) {
					cols.add(i);
//					points += 50;
//					clearCol(i);
				}
			}
			
			for(int i = 0; i < rows.size(); i++) {
				points += 50;
				clearRow(rows.get(i));;
			}
			
			for(int i = 0; i < cols.size(); i++) {
				points += 50;
				clearCol(cols.get(i));;
			}
		}
		
		if (this.getParent() != null) {
			BorderPane bp = (BorderPane)this.getParent();
			ScoreWorld s = (ScoreWorld)bp.getTop();
			s.getScore().setScore(points);
		}
	}
	
	
	
	public double nearestX(double x) {
		for(int i = 0; i < coordinates.length; i++) {
			for(int j = 0; j < coordinates[0].length; j++) {
				Point p = coordinates[i][j];
				if(p.getX() + 19 > x && p.getX() - 19 < x) {
					return p.getX();
				}
			}
		}
		return -1;
	}
	
	
	public double nearestY(double y) {
		for(int i = 0; i < coordinates.length; i++) {
			for(int j = 0; j < coordinates[0].length; j++) {
				Point p = coordinates[i][j];
				if(p.getY() + 19 > y && p.getY() - 19 < y) {
					return p.getY();
				}
			}
		}
		return -1;
	}
	
	
	public boolean checkEntireBoard() {
		for (int i = 0; i < 8; i++) {
			if (!checkRow(i)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkRow(int row) {
		for (int i = 0; i < 8; i++) {
			if (board[row][i] == 0) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkCol(int col) {
		for (int i = 0; i < 8; i++) {
			if (board[i][col] == 0) {
				return false;
			}
		}
		return true;
	}
	
	public void clearRow(int row) {
		for (int i = 0; i < 8; i++) {
			double x = coordinates[row][i].getX();
			double y = coordinates[row][i].getY();
			List b = getObjectsAt(x, y, Block.class);
			for (int j = 0; j < b.size(); j++) {
				if (((Block)(b.get(j))).getY() == coordinates[row][i].getY()) {
					FadeTransition fade = new FadeTransition();
					fade.setFromValue(1);
					fade.setToValue(0);
					fade.setDuration(new Duration(10000));
					fade.setCycleCount(1);
					fade.setNode((Block)(b.get(j)));
					fade.play();
					
					Block bl = (Block)(b.get(j));
					playRowParticleEffect(bl.getY() + bl.getHeight()/2, bl.getColor());
					
					remove((Block)(b.get(j)));
				}
			}
			board[row][i] = 0;
		}
	}
	
	public void clearCol(int col) {
		for (int i = 0; i < 8; i++) {
			double x = coordinates[i][col].getX();
			double y = coordinates[i][col].getY();
			List b = getObjectsAt(x, y, Block.class);
			for (int j = 0; j < b.size(); j++) {
				if (((Block)(b.get(j))).getX() == coordinates[i][col].getX()) {
					FadeTransition fade = new FadeTransition();
					fade.setFromValue(1);
					fade.setToValue(00);
					fade.setDuration(new Duration(100000));
					fade.setCycleCount(1);
					fade.setNode((Block)(b.get(j)));
					fade.play();
					
					Block bl = (Block)(b.get(j));
					playColParticleEffect(bl.getX() + bl.getWidth()/2, bl.getColor());
					
					remove((Block)(b.get(j)));
				}
			}
			board[i][col] = 0;
		}
	}
	
	public void playRowParticleEffect(double y, String color) {
		Color col = getColor(color);
		Random r = new Random();
		List<Particle> particles = new ArrayList<Particle>();
		for (int i = 0; i < 5; i++) {
			int x = r.nextInt((int)(grid.getX() + 320));
			while(x < grid.getX()) {
				x = r.nextInt((int)(grid.getX() + 320));
			}
			Particle p = new Particle(x, y, col);
			getChildren().add(p);
			particles.add(p);
		}
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				for (int i = 0; i < particles.size(); i++) {
					Particle p = particles.get(i);
					p.update();
					if (p.getOpacity() <= 0) {
		                getChildren().remove(p);
		                particles.remove(i);
		                i--;
					}
				}
				
				if (particles.isEmpty()) {
					this.stop();
				}
			}
		};
		timer.start();
	}
	
	
	
	public void playColParticleEffect(double x, String color) {
		Color col = getColor(color);
		Random r = new Random();
		List<Particle> particles = new ArrayList<Particle>();
		for (int i = 0; i < 5; i++) {
			int y = r.nextInt((int)(grid.getY() + 320));
			while(y < grid.getY()) {
				y = r.nextInt((int)(grid.getY() + 320));
			}
			Particle p = new Particle(x, y, col);
			getChildren().add(p);
			particles.add(p);
		}
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				for (int i = 0; i < particles.size(); i++) {
					Particle p = particles.get(i);
					p.update();
					if (p.getOpacity() <= 0) {
		                getChildren().remove(p);
		                particles.remove(i);
		                i--;
					}
				}
				
				if(particles.isEmpty()) {
					this.stop();
				}
			}
		};
		timer.start();
	}
	
	public Color getColor(String col) {
		if (col.equals("blue")) {
			return Color.rgb(73,101,231);
		} else if (col.equals("cyan")) {
			return Color.rgb(54,178,226);
		} else if (col.equals("green")) {
			return Color.rgb(60,181,62);
		} else if (col.equals("orange")) {
			return Color.rgb(236,120,34);
		} else if(col.equals("pink")) {
			return Color.rgb(211,96,214);
		} else if (col.equals("purple")) {
			return Color.rgb(141,96,214);
		} else if (col.equals("red")) {
			return Color.rgb(201,50,50);
		} else {
			return Color.rgb(236,182,49);
		}
	}
	
	public void clearBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = 0;
			}
		}
	}
	
	public int getBoard(int x, int y) {
		return board[x][y];
	}
	
	public ImageView getGrid() {
		return grid;
	}
	
	public boolean blockAt(double x, double y) {
		int row = (int)(convertToGrid(x, y).getY());
		int col = (int)(convertToGrid(x, y).getX());
		if(row != -1 && col != -1) {
			return board[row][col] == 1;
			
		}
		return false;
	}
	
	public boolean ifBlockAt(double x, double y) {
		int row = (int)(convertToGrid(x, y).getY());
		int col = (int)(convertToGrid(x, y).getX());
		if(row != -1 && col != -1) {
			return ifFits[row][col] == 1;
			
		}
		return false;
	}
	
	public Point convertToGrid(double x, double y) {
		int row = -1;
		int col = -1;
		if(y == 0) {
			double xcor = nearestX(x);
			
			for(int i = 0; i < coordinates[0].length; i++) {
				Point p = coordinates[0][i];
				if(p.getX() == xcor) {
					row = i;
				}
				
			}
		} else {
			double xcor = nearestX(x);
			double ycor = nearestY(y);
			for(int i = 0; i < coordinates.length; i++) {
				for(int j = 0; j < coordinates[0].length; j++) {
					Point p = coordinates[i][j];
					if(p.getX() == xcor && p.getY() == ycor) {
						row = i;
						col = j;
					}
				}
			}
		}
		
		return new Point(col, row);
	}
	
	
	public void setBlock(double x, double y) {
		int row = (int)(convertToGrid(x, y).getY());
		int col = (int)(convertToGrid(x, y).getX());
		if(row != -1 && col != -1) {
			board[row][col] = 1;
			points += 3;
			if (this.getParent() != null) {
				BorderPane bp = (BorderPane)this.getParent();
				ScoreWorld s = (ScoreWorld)bp.getTop();
				s.getScore().setScore(points);
			}
		}
		
	}
	
	public void setBoard(int x, int y, int num) {
		board[x][y] = num;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setPoints(int point) {
		points = point;
	}
	
	public void addPoints(int point) {
		points += point;
	}
	
	public int getWorldWidth() {
		return width;
	}
	
	public int getWorldHeight() {
		return height;
	}
	
	public Point getCoordinates(int x, int y) {
		return coordinates[x][y];
	}
	
	
	public boolean fits(Shape s) {
		for(int i = 0; i < coordinates.length; i++) {
			for(int j = 0; j < coordinates[0].length; j++) {
				if(s.fits(coordinates[i][j].getX(), coordinates[i][j].getY())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean ifFits(Shape s) {
		for(int i = 0; i < coordinates.length; i++) {
			for(int j = 0; j < coordinates[0].length; j++) {
				if(s.ifFits(coordinates[i][j].getX(), coordinates[i][j].getY())) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	public void printBoard() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				System.out.printf("%2d", board[i][j]);
			}
			System.out.println();
		}
	}
	
	
	public boolean canFit() {
		for(int i = 0; i < waiting.size(); i++) {
			if(fits(waiting.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean isGameOver() { return !run; }
	
	

	@Override
	public Object getBean() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(ChangeListener listener) {
		listeners.add(listener);
		
	}

	@Override
	public void removeListener(ChangeListener listener) {
		listeners.remove(listener);
		
	}

	@Override
	public Object getValue() {
		return run;
	}

	@Override
	public void addListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(Object value) {
		boolean prev = run;
		run = (boolean)value;
		for(ChangeListener listener: listeners) {
			listener.changed(null, prev, run);
		}
		
	}

	@Override
	public void bind(ObservableValue observable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unbind() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isBound() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void bindBidirectional(Property other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unbindBidirectional(Property other) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	public boolean completesRow(double x, double y) {
		int col = (int)(convertToGrid(nearestX(x), nearestY(y)).getX());
		int row = (int)(convertToGrid(nearestX(x), nearestY(y)).getY());
		
		if(col >= 0 && row >= 0) {
			for(int i = 0; i < board[0].length; i++) {
				if(board[row][i] != 1 && i != col) {
					return false;
				}
			}
			
			return true;
		} 
		return false;
	}
	
	
	
	public void setRowCol(double xcor, String col) {
		int row = (int)(convertToGrid(xcor, 0).getY());
		//System.out.println(row);
		
		if(row >= 0) {
			for(int i = 0; i < board[0].length; i++) {
				double x = coordinates[row][i].getX();
				double y = coordinates[row][i].getY();
				
				
				List<Block> arr = getObjectsAt(x, y, Block.class);
				for(Block b: arr) {
					System.out.println("SETTING COL");
					b.setColor(col);
					
				}
			}
		}
		
	}
	
	
	public boolean isEmpty() {
		for(int i =0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j] != 0){
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	
	public void fillAnimation() {
	    bl = getObjects(Block.class);
	    blocks = new ArrayList<>();

	    AnimationTimer timer = new AnimationTimer() {
	        long prev = 0;
	        int ind = 7;
	        int rowsAdded = 0;
	        boolean finished = false;
	        
	        @Override
	        public void handle(long now) {
	            if (finished) {
	                return;
	            }
	            
	            if(now - prev >= 100000000) {
	                if (rowsAdded < 8) {
	                    addRandomBlock(coordinates[ind][0].getY(), blocks);
	                    ind--;
	                    rowsAdded++;
	                    
	                    if (rowsAdded >= 8) {
	                        finished = true;
	                        // Stop on next frame to avoid any issues
	                        Platform.runLater(() -> this.stop());
	                    }
	                }
	                prev = now;
	            }
	        }
	    };
	    timer.start();
	}
	
	
	
	
	
	public void removeBlocks(double ycor, ArrayList<Block> bs) {
		if (first) {
			for (int i = 0; i < bl.size(); i++) {
				Block b = (Block)(bl.get(i));
				remove(b);
			}
			first = false;
		}
		int first = bs.size() - 1;
		int last = bs.size() - 8;
		for (int i = first; i >= last; i--) {
			if (i < 0) {
				break;
			}
			Block b = bs.get(i);
			remove(b);
			bs.remove(b);
		}
	}
	
	
	public void addRandomBlock(double ycor, ArrayList<Block> bs) {
		double y = nearestY(ycor);
		for(int i = 0; i < 8; i++) {
			String col = colors[(int)(Math.random() * colors.length)];
			Point p = new Point((int)(coordinates[0][i].getX()), (int)y);
			Block b = new Block(col, new Shape(), p);
			b.setX(p.getX());
			b.setY(p.getY());
			b.placedOnBoard();
			getChildren().add(b);
			bs.add(b);
		}
		
		
	}
	
	
	
	public boolean doesFit() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				cellSearched1[i][j] = false;
				ifFits[i][j] = board[i][j];
			}
		}
		
		if (willFit(0)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	public boolean willFit(int index) {
		if (index == incoming.size()) {
			return true;
		}
		
		Shape s = incoming.get(index);
		if (ifFits(s)) {
			addShape(s, true, index);
			if (willFit(index + 1)) {
				return true;
			}
		} else {
			//addShape(s, false, index);
			//willFit(index);
		}
		
		return false;
	}
	
	
	
	public void addShape(Shape s, boolean add, int index) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (s.ifFits(coordinates[i][j].getX(), coordinates[i][j].getY())) {
					if (add) {
						if (cellSearched1[i][j] == false) {
							changeIfFits(coordinates[i][j].getX(), coordinates[i][j].getY(),add);
							cellSearched1[i][j] = true;
						}
					} else {
						changeIfFits(coordinates[i][j].getX(), coordinates[i][j].getY(), add);
					}
				}
			}
		}
	}
	
	public void changeIfFits(double x, double y, boolean add) {
		int row = (int)(convertToGrid(x, y).getY());
		int col = (int)(convertToGrid(x, y).getX());
		if(row != -1 && col != -1) {
			if (add) {
				ifFits[row][col] = 1;
			} else {
				ifFits[row][col] = 0;
			}
		}
	}
	
	public void addBlocks() {
		
		willFit = false;
		
		while (!willFit) {
			
			for (int i = 0; i < incoming.size(); i++) {
				Shape s = incoming.get(i);
				incoming.remove(s);
				i--;
			}
			
			for(int i = 0; i < 3; i++) {
			
				Point curr;
				if(i == 0) {
					curr = spawn1;
				} else if(i == 1) {
					curr = spawn2;
				} else {
					curr = spawn3;
				}
				
				String key = shapes[(int)(Math.random() * shapes.length)];
				String col = colors[(int)(Math.random() * colors.length)];
			
				if(key.equals("L1")) {
					
					curr = new Point((int)curr.getX() - 20, (int)curr.getY());
					
					Shape s = new Shape();
					
					Point spawn11 = new Point((int)curr.getX(), (int)curr.getY() - 40);
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn14 = new Point((int)curr.getX() + 40, (int)curr.getY() - 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					Point spawn12 = new Point ((int)curr.getX(), (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point((int)curr.getX(), (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("L2")) {
					
					curr = new Point((int)curr.getX() - 20, (int)curr.getY());
					Shape s = new Shape();
					
					Point spawn11 = new Point((int)curr.getX(), (int)curr.getY() - 40);
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn14 = new Point((int)curr.getX() + 40, (int)curr.getY() - 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					Point spawn12 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("L3")) {
					curr = new Point((int)curr.getX() - 20, (int)curr.getY());
					Shape s = new Shape();
					
					Point spawn11 = new Point((int)curr.getX(), (int)curr.getY() - 40);
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn14 = new Point((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					Point spawn12 = new Point ((int)curr.getX(), (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point((int)curr.getX(), (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("L4")) {
					curr = new Point((int)curr.getX() - 20, (int)curr.getY());
					Shape s = new Shape();
					
					Point spawn11 = new Point((int)curr.getX(), (int)curr.getY() + 40);;
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn14 = new Point((int)curr.getX() + 40, (int)curr.getY() - 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					Point spawn12 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("L5")) {
					
					curr = new Point((int)curr.getX(), (int)curr.getY() - 20);
					
					Shape s = new Shape();
					
					Point spawn11 = new Point((int)curr.getX() - 40, (int)curr.getY());;
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn14 = curr;
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					Point spawn12 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("L6")) {
					
					curr = new Point((int)curr.getX(), (int)curr.getY() - 20);
					Shape s = new 	Shape();
					
					Point spawn11 = new Point((int)curr.getX() - 40, (int)curr.getY() + 40);
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn14 = new Point((int)curr.getX(), (int)curr.getY() + 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					Point spawn12 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point((int)curr.getX() + 40, (int)curr.getY());
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("L7")) {
					curr = new Point((int)curr.getX(), (int)curr.getY() - 20);

					Shape s = new Shape();
					
					Point spawn11 = new Point((int)curr.getX() - 40, (int)curr.getY());;
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn14 = curr;
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					Point spawn12 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point((int)curr.getX() - 40, (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("L8")) {
					curr = new Point((int)curr.getX(), (int)curr.getY() - 20);
					Shape s = new 	Shape();
					
					Point spawn11 = new Point((int)curr.getX() - 40, (int)curr.getY() + 40);
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn14 = new Point((int)curr.getX(), (int)curr.getY() + 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					Point spawn12 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point((int)curr.getX() - 40, (int)curr.getY());
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("Q1")) {
					Shape s = new 	Shape();
					
					Point spawn11 = curr;
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					s.addBlock(b1);
					
					getChildren().addAll(b1);
					incoming.add(s);
					
				} else if(key.equals("Q2")) {
					
					curr = new Point((int)curr.getX() - 20, (int)curr.getY() - 20);
					
					Shape s = new 	Shape();
					
					Point spawn11 = curr;
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn12 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					Point spawn14 = new Point ((int)curr.getX(), (int)curr.getY() + 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
	
				} else if(key.equals("Q3")) {
					Shape s = new 	Shape();
					
					Point spawn11 = new Point ((int)curr.getX() - 40, (int)curr.getY() - 40);
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn12 = new Point ((int)curr.getX() - 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point ((int)curr.getX() - 40, (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					Point spawn14 = new Point ((int)curr.getX(), (int)curr.getY() - 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					Point spawn15 = new Point ((int)curr.getX(), (int)curr.getY());
					Block b5 = new Block(col, s, spawn15);
					b5.setX(spawn15.getX());
					b5.setY(spawn15.getY());
					
					Point spawn16 = new Point ((int)curr.getX(), (int)curr.getY() + 40);
					Block b6 = new Block(col, s, spawn16);
					b6.setX(spawn16.getX());
					b6.setY(spawn16.getY());
					
					
					Point spawn17 = new Point ((int)curr.getX() + 40, (int)curr.getY() - 40);
					Block b7 = new Block(col, s, spawn17);
					b7.setX(spawn17.getX());
					b7.setY(spawn17.getY());
					
					Point spawn18 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b8 = new Block(col, s, spawn18);
					b8.setX(spawn18.getX());
					b8.setY(spawn18.getY());
					
					Point spawn19 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b9 = new Block(col, s, spawn19);
					b9.setX(spawn19.getX());
					b9.setY(spawn19.getY());
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					s.addBlock(b5);
					s.addBlock(b6);
					s.addBlock(b7);
					s.addBlock(b8);
					s.addBlock(b9);
					
					getChildren().addAll(b1, b2, b3, b4, b5, b6, b7, b8, b9);
					incoming.add(s);
					
				} else if(key.equals("R1")) {
					Shape s = new 	Shape();
					
					curr = new Point((int)curr.getX() - 20, (int)curr.getY());
					
					Point spawn14 = new Point ((int)curr.getX(), (int)curr.getY() - 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					Point spawn15 = new Point ((int)curr.getX(), (int)curr.getY());
					Block b5 = new Block(col, s, spawn15);
					b5.setX(spawn15.getX());
					b5.setY(spawn15.getY());
					
					Point spawn16 = new Point ((int)curr.getX(), (int)curr.getY() + 40);
					Block b6 = new Block(col, s, spawn16);
					b6.setX(spawn16.getX());
					b6.setY(spawn16.getY());
					
					
					Point spawn17 = new Point ((int)curr.getX() + 40, (int)curr.getY() - 40);
					Block b7 = new Block(col, s, spawn17);
					b7.setX(spawn17.getX());
					b7.setY(spawn17.getY());
					
					Point spawn18 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b8 = new Block(col, s, spawn18);
					b8.setX(spawn18.getX());
					b8.setY(spawn18.getY());
					
					Point spawn19 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b9 = new Block(col, s, spawn19);
					b9.setX(spawn19.getX());
					b9.setY(spawn19.getY());
					
				
					s.addBlock(b4);
					s.addBlock(b5);
					s.addBlock(b6);
					s.addBlock(b7);
					s.addBlock(b8);
					s.addBlock(b9);
					
					getChildren().addAll(b4, b5, b6, b7, b8, b9);
					incoming.add(s);
					
				} else if(key.equals("R2")) {
					Shape s = new 	Shape();
					
					curr = new Point((int)curr.getX(), (int)curr.getY() - 20);
					
					
					Point spawn12 = new Point ((int)curr.getX() - 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point ((int)curr.getX() - 40, (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					
					Point spawn15 = new Point ((int)curr.getX(), (int)curr.getY());
					Block b5 = new Block(col, s, spawn15);
					b5.setX(spawn15.getX());
					b5.setY(spawn15.getY());
					
					Point spawn16 = new Point ((int)curr.getX(), (int)curr.getY() + 40);
					Block b6 = new Block(col, s, spawn16);
					b6.setX(spawn16.getX());
					b6.setY(spawn16.getY());
					
					
					
					
					Point spawn18 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b8 = new Block(col, s, spawn18);
					b8.setX(spawn18.getX());
					b8.setY(spawn18.getY());
					
					Point spawn19 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b9 = new Block(col, s, spawn19);
					b9.setX(spawn19.getX());
					b9.setY(spawn19.getY());
					
					
					s.addBlock(b2);
					s.addBlock(b3);
				
					s.addBlock(b5);
					s.addBlock(b6);
					
					s.addBlock(b8);
					s.addBlock(b9);
					
					getChildren().addAll(b2, b3, b5, b6, b8, b9);
					incoming.add(s);
					
				} else if(key.equals("S1")) {
					curr = new Point((int)curr.getX(), (int)curr.getY() - 20);
					Shape s = new Shape();
					
					Point spawn1 = new Point((int)curr.getX() - 40, (int)curr.getY());
					Block b1 = new Block(col, s, spawn1);
					b1.setX(spawn1.getX());
					b1.setY(spawn1.getY());
					
					Point spawn2 = new Point((int)curr.getX(), (int)curr.getY());
					Block b2 = new Block(col, s, spawn2);
					b2.setX(spawn2.getX());
					b2.setY(spawn2.getY());
					
					Point spawn3 = new Point((int)curr.getX(), (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn3);
					b3.setX(spawn3.getX());
					b3.setY(spawn3.getY());
					
					Point spawn4 = new Point((int)curr.getX() + 40, (int)curr.getY() +40);
					Block b4 = new Block(col, s, spawn4);
					b4.setX(spawn4.getX());
					b4.setY(spawn4.getY());
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("S2")) {
					curr = new Point((int)curr.getX(), (int)curr.getY() - 20);
					Shape s = new Shape();
					
					Point spawn1 = new Point((int)curr.getX() - 40, (int)curr.getY() + 40);
					Block b1 = new Block(col, s, spawn1);
					b1.setX(spawn1.getX());
					b1.setY(spawn1.getY());
					
					Point spawn2 = new Point((int)curr.getX(), (int)curr.getY());
					Block b2 = new Block(col, s, spawn2);
					b2.setX(spawn2.getX());
					b2.setY(spawn2.getY());
					
					Point spawn3 = new Point((int)curr.getX(), (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn3);
					b3.setX(spawn3.getX());
					b3.setY(spawn3.getY());
					
					Point spawn4 = new Point((int)curr.getX() + 40, (int)curr.getY());
					Block b4 = new Block(col, s, spawn4);
					b4.setX(spawn4.getX());
					b4.setY(spawn4.getY());
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
						
				} else if(key.equals("S3")) {
					curr = new Point((int)curr.getX() - 20, (int)curr.getY());
					Shape s = new Shape();
					
					Point spawn1 = new Point((int)curr.getX(), (int)curr.getY() - 40);
					Block b1 = new Block(col, s, spawn1);
					b1.setX(spawn1.getX());
					b1.setY(spawn1.getY());
					
					Point spawn2 = new Point((int)curr.getX(), (int)curr.getY());
					Block b2 = new Block(col, s, spawn2);
					b2.setX(spawn2.getX());
					b2.setY(spawn2.getY());
					
					Point spawn3 = new Point((int)curr.getX() + 40, (int)curr.getY());
					Block b3 = new Block(col, s, spawn3);
					b3.setX(spawn3.getX());
					b3.setY(spawn3.getY());
					
					Point spawn4 = new Point((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b4 = new Block(col, s, spawn4);
					b4.setX(spawn4.getX());
					b4.setY(spawn4.getY());
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("S4")) {
					curr = new Point((int)curr.getX() - 20, (int)curr.getY());
					Shape s = new Shape();
					
					Point spawn1 = new Point((int)curr.getX(), (int)curr.getY() - 40);
					Block b1 = new Block(col, s, spawn1);
					b1.setX(spawn1.getX());
					b1.setY(spawn1.getY());
					
					Point spawn2 = new Point((int)curr.getX(), (int)curr.getY());
					Block b2 = new Block(col, s, spawn2);
					b2.setX(spawn2.getX());
					b2.setY(spawn2.getY());
					
					Point spawn3 = new Point((int)curr.getX() + 40, (int)curr.getY());
					Block b3 = new Block(col, s, spawn3);
					b3.setX(spawn3.getX());
					b3.setY(spawn3.getY());
					
					Point spawn4 = new Point((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b4 = new Block(col, s, spawn4);
					b4.setX(spawn4.getX());
					b4.setY(spawn4.getY());
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("VL1")) {
					curr = new Point((int)curr.getX(), (int)curr.getY() - 20);
					Shape s = new Shape();
					
					Point spawn1 = curr;
					Block b1 = new Block(col, s, spawn1);
					b1.setX(spawn1.getX());
					b1.setY(spawn1.getY());
					
					Point spawn2 = new Point((int)curr.getX(), (int)curr.getY() + 40);
					Block b2 = new Block(col, s, spawn2);
					b2.setX(spawn2.getX());
					b2.setY(spawn2.getY());
					
					s.addBlock(b1);
					s.addBlock(b2);
					
					getChildren().addAll(b1, b2);
					incoming.add(s);
					
				} else if(key.equals("VL2")) {
					
					Shape s = new Shape();
					
					Point spawn1 = curr;
					Block b1 = new Block(col, s, spawn1);
					b1.setX(spawn1.getX());
					b1.setY(spawn1.getY());
					
					Point spawn2 = new Point((int)curr.getX(), (int)curr.getY() + 40);
					Block b2 = new Block(col, s, spawn2);
					b2.setX(spawn2.getX());
					b2.setY(spawn2.getY());
					
					Point spawn3 = new Point((int)curr.getX(), (int)curr.getY() - 40);
					Block b3 = new Block(col, s, spawn3);
					b3.setX(spawn3.getX());
					b3.setY(spawn3.getY());
					
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					
					getChildren().addAll(b1, b2, b3);
					incoming.add(s);
					
				} else if(key.equals("VL3")) {
					curr = new Point((int)curr.getX(), (int)curr.getY() - 20);
					Shape s = new Shape();
					
					Point spawn1 = curr;
					Block b1 = new Block(col, s, spawn1);
					b1.setX(spawn1.getX());
					b1.setY(spawn1.getY());
					
					
					Point spawn3 = new Point((int)curr.getX(), (int)curr.getY() - 40);
					Block b3 = new Block(col, s, spawn3);
					b3.setX(spawn3.getX());
					b3.setY(spawn3.getY());
					
					Point spawn2 = new Point((int)curr.getX(), (int)curr.getY() + 40);
					Block b2 = new Block(col, s, spawn2);
					b2.setX(spawn2.getX());
					b2.setY(spawn2.getY());
					
					Point spawn4 = new Point((int)curr.getX(), (int)curr.getY() + 40*2);
					Block b4 = new Block(col, s, spawn4);
					b4.setX(spawn4.getX());
					b4.setY(spawn4.getY());
					
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("VL4")) {
					Shape s = new Shape();
					
					Point spawn1 = curr;
					Block b1 = new Block(col, s, spawn1);
					b1.setX(spawn1.getX());
					b1.setY(spawn1.getY());
					
					Point spawn2 = new Point((int)curr.getX(), (int)curr.getY() + 40);
					Block b2 = new Block(col, s, spawn2);
					b2.setX(spawn2.getX());
					b2.setY(spawn2.getY());
					
					Point spawn3 = new Point((int)curr.getX(), (int)curr.getY() - 40);
					Block b3 = new Block(col, s, spawn3);
					b3.setX(spawn3.getX());
					b3.setY(spawn3.getY());
					
					Point spawn4 = new Point((int)curr.getX(), (int)curr.getY() - 40*2);
					Block b4 = new Block(col, s, spawn4);
					b4.setX(spawn4.getX());
					b4.setY(spawn4.getY());
					
					
					Point spawn5 = new Point((int)curr.getX(), (int)curr.getY() + 40*2);
					Block b5 = new Block(col, s, spawn5);
					b5.setX(spawn5.getX());
					b5.setY(spawn5.getY());
				
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					s.addBlock(b5);
					
					getChildren().addAll(b1, b2, b3, b4, b5);
					incoming.add(s);					
					
				} else if(key.equals("HL1")){
					curr = new Point((int)curr.getX() - 20, (int)curr.getY());
					Shape s = new Shape();
					
					Point spawn1 = curr;
					Block b1 = new Block(col, s, spawn1);
					b1.setX(spawn1.getX());
					b1.setY(spawn1.getY());
					
					Point spawn2 = new Point((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn2);
					b2.setX(spawn2.getX());
					b2.setY(spawn2.getY());
					
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					
					getChildren().addAll(b1, b2);
					incoming.add(s);
					
				} else if(key.equals("HL2")) {
					Shape s = new Shape();
					
					Point spawn1 = curr;
					Block b1 = new Block(col, s, spawn1);
					b1.setX(spawn1.getX());
					b1.setY(spawn1.getY());
					
					Point spawn2 = new Point((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn2);
					b2.setX(spawn2.getX());
					b2.setY(spawn2.getY());
					
					Point spawn3 = new Point((int)curr.getX() - 40, (int)curr.getY() );
					Block b3 = new Block(col, s, spawn3);
					b3.setX(spawn3.getX());
					b3.setY(spawn3.getY());
					
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					
					getChildren().addAll(b1, b2, b3);
					incoming.add(s);
					
				} else if(key.equals("HL3")) {
					
					curr = new Point((int)curr.getX() - 20, (int)curr.getY());
					Shape s = new Shape();
					
					Point spawn1 = curr;
					Block b1 = new Block(col, s, spawn1);
					b1.setX(spawn1.getX());
					b1.setY(spawn1.getY());
					
					
					Point spawn3 = new Point((int)curr.getX() - 40, (int)curr.getY());
					Block b3 = new Block(col, s, spawn3);
					b3.setX(spawn3.getX());
					b3.setY(spawn3.getY());
					
					Point spawn2 = new Point((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn2);
					b2.setX(spawn2.getX());
					b2.setY(spawn2.getY());
					
					Point spawn4 = new Point((int)curr.getX() + 40*2, (int)curr.getY());
					Block b4 = new Block(col, s, spawn4);
					b4.setX(spawn4.getX());
					b4.setY(spawn4.getY());
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("HL4")) {
					Shape s = new Shape();
					
					Point spawn1 = curr;
					Block b1 = new Block(col, s, spawn1);
					b1.setX(spawn1.getX());
					b1.setY(spawn1.getY());
					
					Point spawn2 = new Point((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn2);
					b2.setX(spawn2.getX());
					b2.setY(spawn2.getY());
					
					Point spawn3 = new Point((int)curr.getX() - 40, (int)curr.getY());
					Block b3 = new Block(col, s, spawn3);
					b3.setX(spawn3.getX());
					b3.setY(spawn3.getY());
					
					Point spawn4 = new Point((int)curr.getX() - 40*2, (int)curr.getY());
					Block b4 = new Block(col, s, spawn4);
					b4.setX(spawn4.getX());
					b4.setY(spawn4.getY());
					
					
					Point spawn5 = new Point((int)curr.getX() + 40*2, (int)curr.getY());
					Block b5 = new Block(col, s, spawn5);
					b5.setX(spawn5.getX());
					b5.setY(spawn5.getY());
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					s.addBlock(b5);
					
					getChildren().addAll(b1, b2, b3, b4, b5);
					incoming.add(s);
					
				} else if(key.equals("V1")) {
					curr = new Point((int)curr.getX() - 20, (int)curr.getY() - 20);
					
					Shape s = new 	Shape();
					
					Point spawn11 = curr;
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					
					
					Point spawn13 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					Point spawn14 = new Point ((int)curr.getX(), (int)curr.getY() + 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					s.addBlock(b1);
				
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("V2")) {
					curr = new Point((int)curr.getX() - 20, (int)curr.getY() -20);
					
					Shape s = new 	Shape();
					
					
					Point spawn12 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					Point spawn14 = new Point ((int)curr.getX(), (int)curr.getY() + 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b2, b3, b4);
					incoming.add(s);					
					
				} else if(key.equals("V3")) {
					curr = new Point((int)curr.getX() - 20, (int)curr.getY() - 20);
					
					Shape s = new 	Shape();
					
					Point spawn11 = curr;
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn12 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					
					Point spawn14 = new Point ((int)curr.getX(), (int)curr.getY() + 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					s.addBlock(b1);
					s.addBlock(b2);
					
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b4);
					incoming.add(s);					
					
				} else if(key.equals("V4")) {
					curr = new Point((int)curr.getX() - 20, (int)curr.getY() - 20);
					
					Shape s = new 	Shape();
					
					Point spawn11 = curr;
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn12 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					
					getChildren().addAll(b1, b2, b3);
					incoming.add(s);
					
				} else if(key.equals("V5")){
					Shape s = new 	Shape();
					
					Point spawn11 = new Point ((int)curr.getX() - 40, (int)curr.getY() - 40);
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn12 = new Point ((int)curr.getX() - 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point ((int)curr.getX() - 40, (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					Point spawn16 = new Point ((int)curr.getX(), (int)curr.getY() + 40);
					Block b6 = new Block(col, s, spawn16);
					b6.setX(spawn16.getX());
					b6.setY(spawn16.getY());
					
					Point spawn19 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b9 = new Block(col, s, spawn19);
					b9.setX(spawn19.getX());
					b9.setY(spawn19.getY());
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b6);
					s.addBlock(b9);
					
					getChildren().addAll(b1, b2, b3, b6, b9);
					incoming.add(s);
					
				} else if(key.equals("V6")) {
					Shape s = new 	Shape();
					
					Point spawn13 = new Point ((int)curr.getX() - 40, (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
		
					Point spawn16 = new Point ((int)curr.getX(), (int)curr.getY() + 40);
					Block b6 = new Block(col, s, spawn16);
					b6.setX(spawn16.getX());
					b6.setY(spawn16.getY());
					
					
					Point spawn17 = new Point ((int)curr.getX() + 40, (int)curr.getY() - 40);
					Block b7 = new Block(col, s, spawn17);
					b7.setX(spawn17.getX());
					b7.setY(spawn17.getY());
					
					Point spawn18 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b8 = new Block(col, s, spawn18);
					b8.setX(spawn18.getX());
					b8.setY(spawn18.getY());
					
					Point spawn19 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b9 = new Block(col, s, spawn19);
					b9.setX(spawn19.getX());
					b9.setY(spawn19.getY());
					
					s.addBlock(b3);
					s.addBlock(b6);
					s.addBlock(b7);
					s.addBlock(b8);
					s.addBlock(b9);
				
					getChildren().addAll(b3, b6, b7, b8, b9);
					incoming.add(s);
					
				} else if(key.equals("V7")) {
					Shape s = new 	Shape();
					
					Point spawn11 = new Point ((int)curr.getX() - 40, (int)curr.getY() - 40);
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					
					Point spawn14 = new Point ((int)curr.getX(), (int)curr.getY() - 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					Point spawn17 = new Point ((int)curr.getX() + 40, (int)curr.getY() - 40);
					Block b7 = new Block(col, s, spawn17);
					b7.setX(spawn17.getX());
					b7.setY(spawn17.getY());
					
					Point spawn18 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b8 = new Block(col, s, spawn18);
					b8.setX(spawn18.getX());
					b8.setY(spawn18.getY());
					
					Point spawn19 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b9 = new Block(col, s, spawn19);
					b9.setX(spawn19.getX());
					b9.setY(spawn19.getY());
					
					s.addBlock(b1);
					s.addBlock(b4);
					s.addBlock(b7);
					s.addBlock(b8);
					s.addBlock(b9);
		
					getChildren().addAll(b1, b4, b7, b8, b9);
					incoming.add(s);
					
				} else if(key.equals("V8")) {
					Shape s = new 	Shape();
					
					Point spawn11 = new Point ((int)curr.getX() - 40, (int)curr.getY() - 40);
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn14 = new Point ((int)curr.getX(), (int)curr.getY() - 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					Point spawn17 = new Point ((int)curr.getX() + 40, (int)curr.getY() - 40);
					Block b7 = new Block(col, s, spawn17);
					b7.setX(spawn17.getX());
					b7.setY(spawn17.getY());
					
					Point spawn18 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b8 = new Block(col, s, spawn18);
					b8.setX(spawn18.getX());
					b8.setY(spawn18.getY());
					
					Point spawn19 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b9 = new Block(col, s, spawn19);
					b9.setX(spawn19.getX());
					b9.setY(spawn19.getY());
					
					s.addBlock(b1);
					
					s.addBlock(b4);
					
					s.addBlock(b7);
					s.addBlock(b8);
					s.addBlock(b9);
					
					getChildren().addAll(b1, b4, b7, b8, b9);
					incoming.add(s);
					
				} else if(key.equals("T1")) {
					curr = new Point((int)curr.getX() - 20, (int)curr.getY());
					Shape s = new Shape();
					
					Point spawn11 = new Point((int)curr.getX(), (int)curr.getY() - 40);
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn14 = new Point((int)curr.getX(), (int)curr.getY() + 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					Point spawn12 = new Point ((int)curr.getX(), (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point((int)curr.getX() + 40, (int)curr.getY());
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("T2")) {

					curr = new Point((int)curr.getX() - 20, (int)curr.getY());
					Shape s = new Shape();
					
					Point spawn11 = new Point((int)curr.getX(), (int)curr.getY());
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn14 = new Point((int)curr.getX() + 40, (int)curr.getY() - 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					Point spawn12 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("T3")) {
					curr = new Point((int)curr.getX(), (int)curr.getY() - 20);
					
					Shape s = new Shape();
					
					Point spawn11 = new Point((int)curr.getX() - 40, (int)curr.getY());;
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn14 = curr;
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					Point spawn12 = new Point ((int)curr.getX() + 40, (int)curr.getY());
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point((int)curr.getX(), (int)curr.getY() + 40);
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
					
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
					
				} else if(key.equals("T4")) {
					curr = new Point((int)curr.getX(), (int)curr.getY() - 20);
					Shape s = new 	Shape();
					
					Point spawn11 = new Point((int)curr.getX() - 40, (int)curr.getY() + 40);
					Block b1 = new Block(col, s, spawn11);
					b1.setX(spawn11.getX());
					b1.setY(spawn11.getY());
					
					Point spawn14 = new Point((int)curr.getX(), (int)curr.getY() + 40);
					Block b4 = new Block(col, s, spawn14);
					b4.setX(spawn14.getX());
					b4.setY(spawn14.getY());
					
					
					Point spawn12 = new Point ((int)curr.getX() + 40, (int)curr.getY() + 40);
					Block b2 = new Block(col, s, spawn12);
					b2.setX(spawn12.getX());
					b2.setY(spawn12.getY());
					
					Point spawn13 = new Point((int)curr.getX(), (int)curr.getY());
					Block b3 = new Block(col, s, spawn13);
					b3.setX(spawn13.getX());
					b3.setY(spawn13.getY());
					
					
					s.addBlock(b1);
					s.addBlock(b2);
					s.addBlock(b3);
					s.addBlock(b4);
			
					getChildren().addAll(b1, b2, b3, b4);
					incoming.add(s);
				} 
			}
			if (doesFit()) {
				willFit = true;
				
				for (int x = 0; x < incoming.size(); x++) {
					Shape s = incoming.get(x);
					waiting.add(s);
				}
			} else {
				for (int x = 0; x < incoming.size(); x++) {
					Shape s = incoming.get(x);
					List blocks = s.getBlocks();
					for (int y = 0; y < blocks.size(); y++) {
						getChildren().remove(blocks.get(y));
					}
				}
			}
		}
	}
}