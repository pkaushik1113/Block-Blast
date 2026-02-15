package blocks;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import engine.*;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Block extends Actor {
	
	String color;
	final String pastColor;
	Shape shape;
	boolean onBoard;
	Point start;
	double refX = -1;
	double refY = -1;
	Block b = this;
	
	
	public Block(String col, Shape s, Point star) {
		
		color = col;
		pastColor = col;
		Image img = new Image("images/" + col + ".png", 40, 40, true, false);
		this.setImage(img);
		
		shape = s;
		onBoard = false;
		start = star;
		
		
		this.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(!onBoard()) {
					
				
					double diffX = event.getX() - getX();
					double diffY = event.getY() - getY();
					shape.move(diffX - refX, diffY - refY);
										
					BlockWorld bw = (BlockWorld)(getWorld());
					List blocks = bw.getObjects(Block.class);
					if(bw.nearestX(getX()) > 0 && bw.nearestY(getY()) > 0 && shape.fits(bw.nearestX(getX()), bw.nearestY(getY()), b)) {
						double x = bw.nearestX(getX());
						double y = bw.nearestY(getY());
						for (int i = 0; i < blocks.size(); i++) {
							Block b = (Block)(blocks.get(i));
							if (shape.ifClicked(x - getX(), y - getY())) {
								ArrayList<Block> sBlocks = shape.getBlocks();
								for (int j = 0; j < sBlocks.size(); j++) {
									Block nb = sBlocks.get(j);
									int newX = (int)(bw.convertToGrid(nb.getX(), nb.getY()).getX());
									int newY = (int)(bw.convertToGrid(nb.getX(), nb.getY()).getY());
									int bX = (int)(bw.convertToGrid(b.getX(), b.getY()).getX());
									int bY = (int)(bw.convertToGrid(b.getX(), b.getY()).getY());
									if (shape.checkIfRow(newY)) {
										if (bY == newY) {
											b.setColor(color);
											break;
										}
									} else if (shape.checkIfCol(newX)) {
										if (bX == newX) {
											b.setColor(color);
											break;
										}
									} else {
										b.setColor(b.getPastColor());
									}
								}
							} else {
								b.setColor(b.getPastColor());
							}
						}
					} else {
						for (int i = 0; i < blocks.size(); i++) {
							Block b = (Block)(blocks.get(i));
							b.setColor(b.getPastColor());
						}
					}
				}
				shape.bringToFront();	
			}
		});
		
		
		this.setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					if(!onBoard()) {
						refX = event.getX() - getX();
						refY = event.getY() - getY();
					}	
				}
			});
		
		
		this.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if(!onBoard()) {
					BlockWorld bw = (BlockWorld)(getWorld());
					if(bw.nearestX(getX()) > 0 && bw.nearestY(getY()) > 0) {
						double x = bw.nearestX(getX());
						double y = bw.nearestY(getY());
						shape.click(x - getX(), y - getY());
					} else {
						shape.sendToStart();
					}
					shape.bringToFront();
				}	
			}
		});
		
//		shape.addBlock(this);
		

//		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
//
//			@Override
//			public void handle(MouseEvent event) {
//				/System.out.println("HIHHIHIHH");
//				if(!onBoard()) {
//					shape.move(10, 10);
//				}
//				
//			}
//			
//		});
		
		
	}
	
	
	
	public double getStartX() { return start.getX(); }
	
	public double getStartY() { return start.getY(); }
	
	
	@Override
	public void act(long now) {
		
	
	}
	
	
	public boolean inBounds(double x, double y) {
		BlockWorld bw = (BlockWorld)(getWorld());
		if(bw != null && bw.nearestX(x) > 0 && bw.nearestY(y) > 0) {
			return true;
		}
		return false;
	}
	
	
	
	public String getColor() { return color; }
	
	public String getPastColor() { return pastColor; }
	
	public void setColor(String col) {
		color = col;
		Image img = new Image("images/" + col + ".png", 40, 40, true, false);
		this.setImage(img);
	}
	
	public Shape getShape() { return shape; }
	
	public boolean onBoard() { return onBoard; }
	
	public void placedOnBoard() { onBoard = true; }
	
	
	
	
	

}
