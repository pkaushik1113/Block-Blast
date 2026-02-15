package breakout;

import engine.Actor;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Paddle extends Actor{
	
	
	Image img;
	private boolean isMovingLeft;
	private boolean isMovingRight;
	
	public Paddle() {
		img = new Image("testresources/paddle.png");
		this.setImage(img);
	}
	
	@Override
	public void act(long now) {
		if(getWorld().isKeyPressed(KeyCode.RIGHT)) {
			if(getX() + getWidth() + 5 < getWorld().getWidth()) {
				move(5, 0);
				isMovingLeft = false;
				isMovingRight = true;
			} else {
				setX(getWorld().getWidth() - getWidth());
			}
		}
		if(getWorld().isKeyPressed(KeyCode.LEFT)) {
			if(getX() - 5 > 0) {
				move(-5, 0);
				isMovingLeft = true;
				isMovingRight = false;
			} else {
				setX(0);
			}
		}
		isMovingLeft = false;
		isMovingRight = false;
	}
	
	public boolean isMovingLeft() { return isMovingLeft; }
	
	public void setMovingLeft(boolean b) { isMovingLeft = b; }
	
	public boolean isMovingRight() { return isMovingRight; }
	
	public void setMovingRight(boolean b) { isMovingRight = b; }
	
	
	
}
