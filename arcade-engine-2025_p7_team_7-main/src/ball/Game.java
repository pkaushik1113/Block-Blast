package ball;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Game extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override 
	public void start(Stage stage) throws Exception {
		
		stage.setTitle("Ball Game");
		stage.setResizable(false);
		
		BorderPane bp = new BorderPane();
		BallWorld world = new BallWorld();
		
		bp.setCenter(world);
		
		world.start();
		
		Scene myScene = new Scene(bp);
		
		stage.setScene(myScene);
		stage.show();
	}
	
}
