package blocks;

import engine.World;
import javafx.scene.text.TextAlignment;

public class ScoreWorld extends World {
	
	BlockScore score;
	
	public ScoreWorld() {
		this.setWidth(500);
		this.setHeight(10);
		this.setPrefSize(500, 10);
	}
	
	@Override
	public void act(long now) {
		
	}
	
	@Override
	public void onDimensionsInitialized() {
		score = new BlockScore();
		score.setX(getWidth()/2 - score.getBoundsInParent().getWidth()/2);
		score.setY(100);
		score.setTextAlignment(TextAlignment.CENTER);
		getChildren().add(score);;
		
	}
	
	public BlockScore getScore() {
		return score;
	}

}
