package blocks;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Tester extends Application {

	public static void main(String[] args) {
		
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
	
		stage.setTitle("Block Blast");
		stage.setResizable(false);
		
		BlockWorld bw = new BlockWorld();
		bw.start();
		
		Scene myScene = new Scene(bw);
		
		stage.setScene(myScene);
		stage.show();
	}
	
}
