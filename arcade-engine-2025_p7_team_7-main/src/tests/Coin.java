package tests;

import javafx.scene.image.Image;

public class Coin extends TestActor {
	
	private static final Image COIN_IMAGE = new Image("testresources/Coin.png");
	
	public Coin() {
		setImage(COIN_IMAGE);
		setFitWidth(40);
		setFitHeight(40);
	}
}
