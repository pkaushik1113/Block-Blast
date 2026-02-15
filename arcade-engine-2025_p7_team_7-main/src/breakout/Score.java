package breakout;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ball.BallWorld;

public class Score extends Text{
	
	private int value;
	
	public Score() {
		value = 0;
		setFont(new Font(50));
		updateDisplay();
		
	}
	
	
	public void updateDisplay() {
		setText("" + value);
		BallWorld b = (BallWorld)(getParent());
		if(b != null) {
			setX(b.getWidth()/2 - getBoundsInParent().getWidth()/2);
		}
	}
	
	
	public int getScore() { return value; }
	
	public void setScore(int val) { 
		value = val; 
		updateDisplay();
	}
	

}
