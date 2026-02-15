package ball;

import breakout.Brick;
import breakout.Paddle;
import engine.Actor;
import javafx.scene.image.Image;

public class Ball extends Actor{
	
	double dx;
	double dy;
	
	boolean prev = false;

	public Ball() {
		
		Image img = new Image("testresources/ball.png");
		this.setImage(img);
		
		dx = -3;
		dy = 5;
	}
	
	@Override
	public void act(long now) {
		this.move(dx, dy);
		
		if (this.getX() <= 0) {
			dx = -dx;
		} else if (this.getX() + this.getWidth() >= this.getWorld().getWidth()) {
			dx = -dx;
		}
		
		if (this.getY() <= 0) {
			dy = -dy;
		} else if (this.getY() + this.getHeight() >= this.getWorld().getHeight()) {
			BallWorld b = (BallWorld)(getWorld());
			b.getScore().setScore(b.getScore().getScore() - 1000);
			dy = -dy;
		}
		
		if(getOneIntersectingObject(Paddle.class) != null) {
			Paddle p = getOneIntersectingObject(Paddle.class);
			if(!p.isMovingLeft() && !p.isMovingRight() && !prev) {
				dy = -dy;
				
			} else {
				int ballX = (int) (getX() + getWidth()/2);
				int pX = (int)(p.getX());
	
				double t1 = pX + p.getWidth()/3;
				double t2 = t1 + p.getWidth()/3;
				
				if(ballX <= t2 && ballX >= t1) {
					dy = -dy;
				} else if(ballX < t1 && p.isMovingLeft()) {
					dx = -Math.abs(dx);
				} else if(ballX > t2 && p.isMovingRight()) {
					dx = Math.abs(dx);
				} else if(ballX < pX) {
					dx = -5;
					if(dy < 0) {
						dy = -3;
					} else {
						dy = 3;
					}
				} else if(ballX > pX + p.getWidth()) {
					dx = 5;
					if(dy < 0) {
						dy = -3;
					} else {
						dy = 3;
					}
				} else {
					dy = -dy;	
				}
				
			}
			
			prev = true;
			
		}
		
		if (getOneIntersectingObject(Brick.class) != null) {
			Brick brick = getOneIntersectingObject(Brick.class);
			if (getX() < brick.getX() + brick.getWidth()/2 && getX() > brick.getX() - brick.getWidth()/2) {
				dy = -dy;
			} else if (getY() < brick.getY() + brick.getHeight()/2 && getY() > brick.getY() - brick.getHeight()/2) {
				dx = -dx;
			} else {
				dy = -dy;
				dx = -dx;
			}
			
			BallWorld b = (BallWorld)(getWorld());
			b.getScore().setScore(b.getScore().getScore() + 100);
			getWorld().remove(brick);
		}
		
		if(getOneIntersectingObject(Paddle.class) == null) {
			prev = false;
		}
		
	}
	
}
