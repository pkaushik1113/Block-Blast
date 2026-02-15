package ball;

import java.util.Random;

import breakout.Brick;
import breakout.Paddle;
import breakout.Score;
import engine.World;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextAlignment;

public class BallWorld extends World{
	
	Score score;
	
	public BallWorld() {
		this.setWidth(800);
		this.setHeight(800);
		setPrefSize(800, 800);
		//onDimensionsInitialized();
	}

	@Override
	public void act(long now) {
		
	}
	
	@Override
	public void onDimensionsInitialized() {
		
		score = new Score();
		score.setX(getWidth()/2 - score.getBoundsInParent().getWidth()/2);
		score.setY(60);
		score.setTextAlignment(TextAlignment.CENTER);
		getChildren().add(score);
		
		
		
		Ball ball = new Ball();
		this.add(ball);
		
		ball.setX(getWidth()/2 - ball.getWidth()/2);
		ball.setY(getHeight()/2 - ball.getHeight()/2);
		
		Paddle pad = new Paddle();
		this.add(pad);
		
		pad.setX(getWidth()/2 - pad.getWidth()/2);
		pad.setY(getHeight()/2 - pad.getHeight()/2);
		
//		setOnMousePressed(new EventHandler<MouseEvent> () {
//			@Override
//			public void handle(MouseEvent e) {
//				pad.setX(e.getX());
//				pad.setMoving(true);
//			}
//		});
		
		setOnMouseDragged(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent e) {
				if(pad.getX() < e.getX()) {
					pad.setMovingRight(true);
					pad.setMovingLeft(false);
				} else {
					pad.setMovingRight(false);
					pad.setMovingLeft(true);
				}
				pad.setX(e.getX());
				
			}
		});
		
		setOnMouseReleased(new EventHandler<MouseEvent> () {
			@Override
			public void handle(MouseEvent e) {
				
				pad.setMovingLeft(false);
				pad.setMovingRight(false);
			}
		});
	
		
//		Brick b = new Brick();
//		this.add(b);
//		b.setX(200);	
//		b.setY(100);
//		Brick c = new Brick();
//		this.add(c);
//		c.setX(100);	
//		c.setY(100);
//		//Add 5 bricks
		int numBricks = 10;
		for (int i = 0; i < numBricks; i++) {
			Brick b = new Brick();
			this.add(b);
			
			Random r = new Random();
			int x = r.nextInt((int)(getWidth() - b.getWidth()/2));
			int y = r.nextInt((int)(getHeight() - b.getHeight()/2));
			
			b.setX(x);
			b.setY(y);
		
		}
		
	}
	
	
	
	public Score getScore() { return score; }
	
}
