package breakout;

import engine.Actor;
import javafx.scene.image.Image;

public class Brick extends Actor{
	
	Image img;

	public Brick() {
		img = new Image("testresources/brick.png");
		this.setImage(img);
	}
	
	@Override
	public void act(long now) {
		
	}
	
}
