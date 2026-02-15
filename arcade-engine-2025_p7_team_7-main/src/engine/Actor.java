package engine;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;

public abstract class Actor extends ImageView {

	public Actor() {
		
	}
	
	public abstract void act(long now);
	
	
	public void addedToWorld() {
		
	}

	
	public <A extends Actor> List<A> getIntersectingObjects(Class<A> cls) {
		List<A> obj = getWorld().getObjects(cls);
		
		List<A> inter = new ArrayList<A>();
		
		for(int i = 0; i < obj.size(); i++) {
			if(!obj.get(i).equals(this) && this.getBoundsInParent().intersects(obj.get(i).getBoundsInParent())) {
				inter.add(obj.get(i));
			}
		}
		return inter;
	}
	
	
	public <A extends Actor> A getOneIntersectingObject(Class<A> cls) {
		List<A> obj = getWorld().getObjects(cls);
		
		for(int i = 0; i < obj.size(); i++) {
			if(!obj.get(i).equals(this) && this.getBoundsInParent().intersects(obj.get(i).getBoundsInParent())) {
				return obj.get(i);
			}
		}
		
		return null;
	}
	
	
	public double getWidth() {
		return getBoundsInParent().getWidth();
	}
	
	public double getHeight() {
		return getBoundsInParent().getHeight();
	}
	
	
	
	public World getWorld() {
		return (World)(getParent());
	}
	
	public void move(double dx, double dy) {
		setX(getX() + dx);
		setY(getY() + dy);
	
	}
	
}
