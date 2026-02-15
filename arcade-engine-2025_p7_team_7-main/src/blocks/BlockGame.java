package blocks;

import java.io.File;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class BlockGame extends Application {

	BlockWorld bw;
	ScoreWorld sw;
	Scene gameOverScene;
	Stage theStage;
	Text score;
	Scene game;
	int width = 500;
	int height = 700;
	ImageView startButton;
	ImageView restartButton;
	ImageView setting;
	Stage settings;
	ImageView close;
	BorderPane bp;
	ImageView newGame;
	ImageView toHome;
	Scene start;
	ImageView closes;
	ImageView restart;
	Handler gameHandler; // Single handler instance
	Text in;
	private boolean gameActive = false; // Controls when game over should trigger
	Stage instructions;

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		theStage = stage;
		
		stage.setTitle("Block Blast!");
		stage.setResizable(false);
		
		
	// Instructions
		int inWidth = 300;
		int inHeight = 500;
		
		instructions = new Stage();
		instructions.setResizable(false);
		Image l = new Image("images/close.png", 47, 47, true, false);
		closes = new ImageView(l);
		closes.setX(250);
		closes.setY(20);
		closes.setOnMousePressed(new EffectHandler());
		closes.setOnMouseEntered(new EffectHandler());
		closes.setOnMouseExited(new EffectHandler());
		
		Image h = new Image("images/instructions.png", 200, 47, true, false);
		ImageView hv = new ImageView(h);
		hv.setX(inWidth/2 - hv.getBoundsInParent().getWidth()/2);
		hv.setY(20);
		
		Image star = new Image("images/button.png", 296, 108, true, false);
		Rectangle r = new Rectangle(250, 350);
		r.setFill(Color.rgb(59,61,163));
		r.setX(inWidth/2 - r.getWidth()/2);
		r.setY(105);
		
		
		Image how = new Image("images/howTo.png", 250, 318, true, false);
		ImageView howTo = new ImageView(how);
		howTo.setX(inWidth/2 - howTo.getBoundsInParent().getWidth()/2);
		howTo.setY(125);
		
		Pane pan = new Pane();
		pan.setPrefSize(inWidth, inHeight);
		pan.getChildren().addAll(closes, r, hv, howTo);
		Scene scr = new Scene(pan);
		scr.setFill(Color.rgb(95,119,241));
		instructions.setScene(scr);
		
		
	// Settings
		int setWidth = 300;
		int setHeight = 400;
		settings = new Stage();
		BorderPane set = new BorderPane();
		
		Image ll = new Image("images/settings.png", 230, 54, true, false);
		ImageView llv = new ImageView(ll);
		llv.setX(setWidth/2 - llv.getBoundsInParent().getWidth()/2);
		llv.setY(20);
		
		
		close = new ImageView(l);
		close.setX(250);
		close.setY(20);
		close.setOnMousePressed(new EffectHandler());
		close.setOnMouseEntered(new EffectHandler());
		close.setOnMouseExited(new EffectHandler());
		
		Pane ppp = new Pane();
		ppp.setPrefWidth(setWidth);
		ppp.setPrefHeight(80);
		ppp.getChildren().addAll(llv, close);
		set.setTop(ppp);
		
		Pane b = new Pane();
		b.setPrefSize(300, 20);
		set.setBottom(b);
		
		StackPane t = new StackPane();
		Pane bb = new Pane();
		bb.setPrefSize(250, 400);
		Image toh = new Image("images/toHome.png", 222, 81, true, false);
		toHome = new ImageView(toh);
		toHome.setX(40);
		toHome.setY(170);
		toHome.setOnMousePressed(new EffectHandler());
		toHome.setOnMouseEntered(new EffectHandler());
		toHome.setOnMouseExited(new EffectHandler());
		
		Image re = new Image("images/restart.png", 222, 81, true, false);
		restart = new ImageView(re);
		restart.setX(250/2 - 85);
		restart.setY(260);
		restart.setOnMousePressed(new EffectHandler());
		restart.setOnMouseEntered(new EffectHandler());
		restart.setOnMouseExited(new EffectHandler());
		
		Image log = new Image("images/logo.png", 60, 60, true, false);
		ImageView logo = new ImageView(log);
		logo.setX(60);
		logo.setY(70);
		
		Text info = new Text("Block Blast!");
		info.setFill(Color.WHITE);
		info.relocate(150, 80);
		
		Text cred = new Text("By: Prisha, Clare");
		cred.setFill(Color.WHITE);
		cred.relocate(150, 100);
		
		bb.getChildren().addAll(toHome, restart, logo, info, cred);
	
		Rectangle rect = new Rectangle(250, 350);
		rect.setFill(Color.rgb(59,61,163));
		rect.setX(setWidth/2 - rect.getWidth()/2);
		rect.setY(50);
		t.getChildren().addAll(rect, bb);
		set.setCenter(t);
		
		Scene sss = new Scene(set);
		sss.setFill(Color.rgb(95,119,241));
		
		settings.setScene(sss);
		settings.setResizable(false);
		
		// Initialize the game scene
		initializeGameScene();
		
		
		
		// START SCENE
		Image ii = new Image("images/home.png", width, height, true, false);
		ImageView iiv = new ImageView(ii);
		
		StackPane spi = new StackPane();
		Pane pi = new Pane();
		
		startButton = new ImageView(star);
		startButton.setX(width/2 - startButton.getBoundsInParent().getWidth()/2);
		startButton.setY(height/2 - startButton.getBoundsInParent().getHeight()/2 + 150);
		startButton.setOnMouseEntered(new EffectHandler());
		startButton.setOnMouseExited(new EffectHandler());
		startButton.setOnMousePressed(new EffectHandler());
		
		in = new Text("Instructions");
		in.setX(width/2 - in.getBoundsInParent().getWidth()/2);
		in.setY(560);
		in.setFill(Color.WHITE);
		in.setOnMouseEntered(new TextHandler());
		in.setOnMouseExited(new TextHandler());
		in.setOnMousePressed(new TextHandler());
		
		
		pi.getChildren().addAll(startButton, in);
		spi.getChildren().addAll(iiv, pi);
		
		start = new Scene(spi);
		
		
		
		// GAME OVER SCENE
		Image i = new Image("images/game_over.png", width, height, true, false);
		ImageView iv = new ImageView(i);
		
		score = new Text();
		restartButton = new ImageView(star);
		restartButton.setX(width/2 - startButton.getBoundsInParent().getWidth()/2);
		restartButton.setY(height/2 - startButton.getBoundsInParent().getHeight()/2 + 150);
		restartButton.setOnMouseEntered(new EffectHandler());
		restartButton.setOnMouseExited(new EffectHandler());
		restartButton.setOnMousePressed(new EffectHandler());
		
		
		
		
		StackPane sp = new StackPane();
		Pane p = new Pane();
		p.getChildren().addAll(score, restartButton);
		sp.getChildren().addAll(iv, p);
		
		gameOverScene = new Scene(sp);
		
		stage.setScene(start);
		stage.show();
	}
	
	
	private void initializeGameScene() {
		if (bw != null) {
			try {
				bw.stop();
			} catch (Exception e) {
			}
		}
		
		bp = new BorderPane();
		sw = new ScoreWorld();
		bp.setTop(sw);
		BlockScore s = sw.getScore();
		
		bw = new BlockWorld();
		bw.start();
		
		// Create a new handler for each new game
		gameHandler = new Handler();
		bw.addListener(gameHandler);
		bp.setCenter(bw);
		
		Color bg = Color.rgb(67,93,162);
		game = new Scene(bp);
		game.setFill(bg);
		Image iii = new Image("images/setting1.png", 49, 49, true, false);
		setting = new ImageView(iii);
		
		setting.setX(-10);
		setting.setY(0); 
		setting.setOnMousePressed(new EffectHandler());
		setting.setOnMouseEntered(new EffectHandler());
		setting.setOnMouseExited(new EffectHandler());
		Pane side = new Pane();
		side.setPrefSize(50, 700);
		
		side.getChildren().add(setting);
		bp.setRight(side);
		
		Pane sides = new Pane();
		sides.setPrefSize(50, 700);
		bp.setLeft(sides);
		
	}
	
	
	
	public class Handler implements ChangeListener<Boolean>{
		@Override
		public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			if(newValue == false && gameActive && theStage.getScene() == game) {
				gameActive = false; // Game is no longer active
				bw.fillAnimation();
				
				AnimationTimer timer  = new AnimationTimer() {
					long prev = 0;
					int count = 0;
					@Override
					public void handle(long now) {
						if (now - prev >= 1000) {
							count++;
							if (count >= 120) {
								theStage.setScene(gameOverScene);
								score.setText("" + sw.getScore().getScore());
								score.setFont(new Font("Impact", 50));
								score.setFill(Color.WHITE);
								score.setX(width/2 - score.getBoundsInParent().getWidth()/2);
								score.setY(height/2 - score.getBoundsInParent().getHeight()/2 + 80);
								stop();
							}
							prev = now;
						}
					}
				};
				timer.start();
			}
		}
	}
	
	public class EffectHandler implements EventHandler<MouseEvent>{
		@Override
		public void handle(MouseEvent event) {
			if(event.getEventType() == MouseEvent.MOUSE_ENTERED) {
				if((ImageView)event.getSource() == setting) {
					setting.setImage(new Image("images/setting2.png", 49, 49, true, false));
				} else {
					Lighting light = new Lighting();
					((Node) event.getSource()).setEffect(light);
				}
				
			} else if(event.getEventType() == MouseEvent.MOUSE_EXITED) {
				if((ImageView)event.getSource() == setting) {
					setting.setImage(new Image("images/setting1.png", 49, 49, true, false));
				} else {
					((Node) event.getSource()).setEffect(null);
				}
			} else if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				if((ImageView)event.getSource() == startButton) {
					gameActive = true; // Game is now active
					theStage.setScene(game);
					instructions.close();
				} else if((ImageView)event.getSource() == restartButton || (ImageView)event.getSource() == restart) {
					initializeGameScene();
					gameActive = true; 
					theStage.setScene(game);
					settings.close();
				} else if((ImageView)event.getSource() == setting) {
					settings.show();
				} else if((ImageView)event.getSource() == close) {
					settings.close();
				}  else if((ImageView)event.getSource() == toHome) {
					gameActive = false;
					settings.close();
					theStage.setScene(start);
				} else if((ImageView)event.getSource() == closes) {
					instructions.close();
				}
			}
		}
	}
	
	
	public class TextHandler implements EventHandler<MouseEvent>{
		
		@Override
		public void handle(MouseEvent event) {
			if(event.getEventType() == MouseEvent.MOUSE_ENTERED) {
				Lighting light = new Lighting();
				((Node) event.getSource()).setEffect(light);
			
			} else if(event.getEventType() == MouseEvent.MOUSE_EXITED) {
				((Node) event.getSource()).setEffect(null);
			} else if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				instructions.show();
			}
		}
		
	}	
}
	