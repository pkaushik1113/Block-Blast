package engine;

import java.util.Set;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;

public abstract class World extends Pane{

	public AnimationTimer timer;
	boolean timerRunning;
	Set<KeyCode> keys;
	boolean widthSet;
	boolean heightSet;
	
	
	
	
	public World() {
		
		widthSet = false;
		heightSet = false;
		timerRunning = false;
		
		keys = new HashSet<KeyCode>();
		
		widthProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(!widthSet && newValue.doubleValue() > 0) {
					widthSet = true;
					if(heightSet) {
						onDimensionsInitialized();
					}
				}	
			}
		});
		
		heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(!heightSet && newValue.doubleValue() > 0) {
					heightSet = true;
					if(widthSet) {
						onDimensionsInitialized();
					}
				}	
			}
		});
		
		
		sceneProperty().addListener(new ChangeListener<Scene>() {
			@Override
			public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
				if(newValue != null) {
					/*System.out.println();
					System.out.println("HIHIHIHIHIHIHIHIHIHH");
					System.out.println();*/
					requestFocus();
				}
			}
		});
		
		
		
		setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if(!keys.contains(event.getCode())) {
					keys.add(event.getCode());
					/*System.out.println();
					System.out.println("HIHIHIHIHIHIHIHIHIHH");
					System.out.println();*/
				}
			}
		});
		
		
		setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if(keys.contains(event.getCode())) {
					keys.remove(event.getCode());
				}
			}
		});
		
		

		timer = new AnimationTimer() {
			private long then = Long.MIN_VALUE;

			@Override
			public void handle(long now) {
				act(now);
				
				
				List<Actor> temp = getObjects(Actor.class);
				
				for(int i = 0; i < temp.size(); i++) {
					Actor a = temp.get(i);
					if (a.getWorld() != null) {
						a.act(now);
					}	
				}
			}
		};
		
		
	}
	
	public abstract void act(long now);
	
	
	public abstract void onDimensionsInitialized();
	
	
	
	public void add(Actor actor) {
		getChildren().add(actor);
		actor.addedToWorld();
	}
	
	
	public <A extends Actor> List<A> getObjects(Class<A> cls){
		List<A> arr = new ArrayList<>();
		
		for(int i = 0; i < getChildren().size(); i++) {
			if(cls.isInstance(getChildren().get(i))) {
				arr.add((A)getChildren().get(i));
			}
		}
		
		return arr;
		
	}
	
	
	
	public <A extends Actor> List<A> getObjectsAt(double x, double y, Class<A> cls){
		
		List<A> arr = new ArrayList<>();
		
		for(int i = 0; i < getChildren().size(); i++) {
			if(cls.isInstance(getChildren().get(i))) {
				if(getChildren().get(i).getBoundsInParent().contains(x, y)){
					arr.add((A)getChildren().get(i));
				}
			}
		}
		
		return arr;
			
	}
	
	
	
	public boolean isKeyPressed(KeyCode code) {
		return keys.contains(code);
	}
	
	
	
	public boolean isStopped() {
		if(!timerRunning) {
			return true;
		}
		return false;
	}
	
	
	
	public void remove(Actor actor) {
		getChildren().remove(actor);
	}
	
	
	
	public void start() {
		timer.start();
		timerRunning = true;
	}
	
	
	
	public void stop() {
		timer.stop();
		timerRunning = false;
	}
	
	
}
