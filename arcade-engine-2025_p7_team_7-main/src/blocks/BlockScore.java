package blocks;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BlockScore extends Text{
	
	private int value;
	
	public BlockScore() {
		value = 0;
		setFont(new Font("Impact", 50));
		setFill(Color.WHITE);
		updateDisplay();
		
	}
	
	public void updateDisplay() {
		setText("" + value);
		ScoreWorld s = (ScoreWorld)(getParent());
		if(s != null) {
			setX(s.getWidth()/2 - getBoundsInParent().getWidth()/2);
		}
	}
	
	public int getScore() { return value; }
	
	public void setScore(int val) { 
		value = val; 
		updateDisplay();
	}
	

}
