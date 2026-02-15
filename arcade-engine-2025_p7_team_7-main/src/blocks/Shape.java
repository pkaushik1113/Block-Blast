package blocks;
import java.awt.Point;
import java.util.ArrayList;

public class Shape {
	
	ArrayList<Block> arr;
	boolean lastOne = false;
	boolean firstOne = true;
	int[][] ifBoard = new int[8][8];
	
	public Shape() {
		arr = new ArrayList<Block>();
		
		
	}
	
	public void addBlock(Block b) { arr.add(b); }

	
	public void move(double x, double y) {
		
		for(int i = 0; i < arr.size(); i++){
			Block b = arr.get(i);
			b.setX(b.getX() + x);;
			b.setY(b.getY() + y);
		}
	}
	
	
	public void click(double x, double y) {
		
		for(int i = 0; i < arr.size(); i++){
			Block b = arr.get(i);
			BlockWorld bw = (BlockWorld)(b.getWorld());
			//System.out.println(b);
			if(b.inBounds(b.getX() + x, b.getY() + y) && !bw.blockAt(b.getX() + x, b.getY() + y)) {
				b.setX(b.getX() + x);
				b.setY(b.getY() + y);
			} else {
				sendToStart();
				return;
			}
		}
		
		for(int i = arr.size() - 1; i >= 0; i--){
			Block b = arr.get(i);
			b.placedOnBoard();
			BlockWorld bw = (BlockWorld)(b.getWorld());
			bw.setBlock(b.getX(), b.getY());
			//bw.hi = 100;
		}
		
	}
	
	
	public boolean fits(double xcor, double ycor) {
		double x = xcor - arr.get(0).getX();
		double y = ycor - arr.get(0).getY();
		for(int i = 0; i < arr.size(); i++){
			Block b = arr.get(i);
			BlockWorld bw = (BlockWorld)(b.getWorld());
			if (bw != null) {
				if(!b.inBounds(b.getX() + x, b.getY() + y) || bw.blockAt(b.getX() + x, b.getY() + y)) {
					return false;
					
				}
			}
		}

		return true;
	}
	
	
	public boolean fits(double xcor, double ycor, Block bl) {
		double x = xcor - bl.getX();
		double y = ycor - bl.getY();
		for(int i = 0; i < arr.size(); i++){
			Block b = arr.get(i);
			BlockWorld bw = (BlockWorld)(b.getWorld());
			if (bw != null) {
				if(!b.inBounds(b.getX() + x, b.getY() + y) || bw.blockAt(b.getX() + x, b.getY() + y)) {
					return false;
					
				}
			}
		}

		return true;
	}
	
	
	public boolean ifFits(double xcor, double ycor) {
		double x = xcor - arr.get(0).getX();
		double y = ycor - arr.get(0).getY();
		for(int i = 0; i < arr.size(); i++){
			Block b = arr.get(i);
			BlockWorld bw = (BlockWorld)(b.getWorld());
			if (bw != null) {
				if(!b.inBounds(b.getX() + x, b.getY() + y) || bw.ifBlockAt(b.getX() + x, b.getY() + y)) {
					return false;
					
				}
			}
		}

		return true;
	}
	
	
	public void completeRow() {
		
		for(int i = 0; i < arr.size(); i++) {
			Block b = arr.get(i);
			BlockWorld bw = (BlockWorld)(b.getWorld());
			if(bw.completesRow(b.getX(), b.getY())){
				System.out.println(b.getX());
				bw.setRowCol(b.getX(), b.getColor());
				
			}
			
		}
	}
	
	public boolean ifClicked(double x, double y) {
			
		double xCoor = -1;
		double yCoor = -1;
		lastOne = false;
	
		Block bk = arr.get(0);
		BlockWorld bw = (BlockWorld)(bk.getWorld());
		for (int d = 0; d < 8; d++) {
			for (int j = 0; j < 8; j++) {
				ifBoard[d][j] = bw.getBoard(d, j);
			}
		}
			
		for (int i = 0; i < arr.size(); i++) {
			Block b = arr.get(i);
			if (i == arr.size() - 1) {
				lastOne = true;
			}
				
			if(b.inBounds(b.getX() + x, b.getY() + y) && !bw.blockAt(b.getX() + x, b.getY() + y)) {
				xCoor = b.getX() + x;
				yCoor = b.getY() + y;
				for (int j = 0; j < 8; j++) {
					for (int k = 0; k < 8; k++) {
						if (bw.getCoordinates(j, k).getX() == xCoor && bw.getCoordinates(j, k).getY() == yCoor) {
							ifBoard[j][k] = 1;
							if (lastOne) {
								for (int a = 0; a < arr.size(); a++) {
									Block n = arr.get(a);
									int nX = (int)(bw.convertToGrid(n.getX(), n.getY()).getX());
									int nY = (int)(bw.convertToGrid(n.getX(), n.getY()).getY());
									if (checkIfRow(nY) || checkIfCol(nX)) {
										return true;
									}
								}
							}
						}
					}
				}
					
			}
		}
		return false;
	}
	
	public boolean checkIfRow(int row) {
		for (int i = 0; i < 8; i++) {
			if (ifBoard[row][i] == 0) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkIfCol(int col) {
		for (int i = 0; i < 8; i++) {
			if (ifBoard[i][col] == 0) {
				return false;
			}
		}
		return true;
	}
	
	public void sendToStart() {
		for(int i = 0; i < arr.size(); i++) {
			Block b = arr.get(i);
			b.setX(b.getStartX());
			b.setY(b.getStartY());
		}
	}
	
	
	public boolean onBoard() {
		for(int i = 0; i < arr.size(); i++) {
			Block b = arr.get(i);
			if(b.onBoard()) {
				return true;
			}
		}
		return false;
	}

	
	public boolean fits() {
		return false;
	}
	
	
	public ArrayList<Point> getCoordinates(){
		ArrayList<Point> p = new ArrayList<>();
		for(int i = 0; i < arr.size(); i++) {
			int x = (int)(arr.get(i).getX());
			int y = (int)(arr.get(i).getY());
			p.add(new Point(x, y));
		}
		return p;
	}
	
	
	public void setCoordinates(ArrayList<Point> p) {
		for(int i = 0; i < arr.size(); i++) {
			arr.get(i).setX(p.get(i).getX());
			arr.get(i).setY(p.get(i).getY());
		}
	}
	
	public Block getFirst() { return arr.get(0); }
	
	
	public void bringToFront() {
		for(int i = 0; i < arr.size(); i++) {
			arr.get(i).toFront();
		}
	}
	
	public ArrayList<Block> getBlocks() { return arr; }

}
