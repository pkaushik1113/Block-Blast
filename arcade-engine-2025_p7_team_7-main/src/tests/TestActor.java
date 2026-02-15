package tests;

import java.util.List;

import engine.Actor;

public abstract class TestActor extends Actor {
	
	private int frameCount = 0;
	private int stopFrameCount = 0;
	private boolean addedToWorld = false;
	private boolean worldIsAccessibleInAddedToWorld = false;
	private boolean shouldRemoveSelf = false;
	
	@Override
	public void addedToWorld() {
		addedToWorld = true;
		System.out.println("Added to world was called.");
		if (getWorld() != null) {
			worldIsAccessibleInAddedToWorld = true;
		} else {
			System.out.println("WARNING: World is null in addedToWorld()");
		}
	}

	@Override
	public void act(long now) {
		frameCount++;
		TestWorld tw = (TestWorld)getWorld();
		try {
			if (shouldRemoveSelf) {
				System.out.println("removing " + this);
				tw.remove(this);
			} else if (tw.isShouldRemoveCoinThisFrame()) {
				Actor actorToRemove = tw.getObjects(Actor.class).get(tw.getObjects(Actor.class).size() - 1);
				int indexInChildren = tw.getChildren().indexOf(actorToRemove);
				System.out.println("removing child at index " +  indexInChildren + " in act method of child at index " + tw.getChildren().indexOf(this));
				tw.remove(actorToRemove);
				tw.setShouldRemoveCoinThisFrame(false);
			}
		} catch (Exception err) {
			TestWorld.exceptions.add(err);
			err.printStackTrace();
		}
	}
	
	public void recordStopFrameCount() {
		stopFrameCount = frameCount;
	}

	public int getFrameCount() {
		return frameCount;
	}

	public void setFrameCount(int frameCount) {
		this.frameCount = frameCount;
	}

	public int getStopFrameCount() {
		return stopFrameCount;
	}

	public void setStopFrameCount(int stopFrameCount) {
		this.stopFrameCount = stopFrameCount;
	}

	public boolean isAddedToWorld() {
		return addedToWorld;
	}

	public boolean isWorldIsAccessibleInAddedToWorld() {
		return worldIsAccessibleInAddedToWorld;
	}

	public boolean isShouldRemoveSelf() {
		return shouldRemoveSelf;
	}

	public void setShouldRemoveSelf(boolean shouldRemoveSelf) {
		this.shouldRemoveSelf = shouldRemoveSelf;
	}
	
	
	
}
