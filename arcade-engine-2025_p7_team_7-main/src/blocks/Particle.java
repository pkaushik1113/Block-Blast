package blocks;

import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Particle extends Rectangle {
	double dx;
	double dy;
	double width;
	double height;

	public Particle(double x, double y, Color color) {
		super();
		setOpacity(1.0);
		Random r = new Random();
		dx = -0.5 + r.nextDouble();
		dy = -0.5 + r.nextDouble();
		int size = 5 + r.nextInt(5);
		setWidth(size);
		setHeight(size);
		setX(x);
		setY(y);
		setFill(color);
	}
	
	public void update() {
		setX(getX() + dx);
		setY(getY() + dy);
		setOpacity(getOpacity() - 0.02);
	}
}
