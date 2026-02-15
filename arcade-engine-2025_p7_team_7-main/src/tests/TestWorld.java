package tests;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import engine.World;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class TestWorld extends World {

	private static final int WIDTH = 500;
	private static final int HEIGHT = 300;
	private Robot rob;
	private int frameCount = 0;
	private int frameCountAtStop = 0;
	private boolean removePlayer1 = false;
	private boolean shouldRemoveCoinThisFrame = false;
	private boolean hasCalledOnDimensionsInitialized = false;
	private boolean testingFrameCount = true;
	public static List<Exception> exceptions = new ArrayList<>();
	private boolean passedAllTests = false;
	private Player player1;
	private Player player2;
	private Coin coin;
	private ArrayList<CounterCoin> counterCoins = new ArrayList<>();

	public TestWorld() {
		setPrefSize(500, 300);
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void onDimensionsInitialized() {
		if (!hasCalledOnDimensionsInitialized) {
			hasCalledOnDimensionsInitialized = true;
			passedAllTests = true;
		}
		else {
			passedAllTests = false;
			System.out.println("You should call onDimensionsInitialized() exactly once --> failed");
		}
		try {
			System.out.println("*** Testing World onDimensionsInitialized() ***");
			
			double width = getWidth();
			double expectedWidth = WIDTH;
			boolean pass = expectedWidth == width;
			String passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getWidth() --> " + width + " --> " + passState);

			double height = getHeight();
			double expectedHeight = HEIGHT;
			pass = expectedHeight == height;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getHeight() --> " + height + " --> " + passState);

			System.out.println("*** Testing World add(Actor actor) ***");
			player1 = new Player();
			add(player1);
			
			System.out.println("*** Testing Actor addedToWorld()");
			boolean added = player1.isAddedToWorld();
			boolean expectedAdded = true;
			pass = expectedAdded == added;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("addedToWorld() called on player1 --> " + added + " --> " + passState);
			
			boolean accessible = player1.isWorldIsAccessibleInAddedToWorld();
			boolean expectedAccessible = true;
			pass = expectedAccessible == accessible;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("world is accessible in addedToWorld() --> " + accessible + " --> " + passState);

			System.out.println("*** Testing World getObjects(Class cls) ***");
			System.out.println("Testing when a single player is the only child.");
			boolean contains = getObjects(Player.class).contains(player1);
			boolean expectedContains = true;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(Player.class).contains(player1) --> " + contains + " --> " + passState);

			int size = getObjects(Player.class).size();
			int expectedSize = 1;
			pass = expectedSize == size;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(Player.class).size() --> " + size + " --> " + passState);

			System.out.println("Testing when two players are in the world.");
			player2 = new Player();
			add(player2);
			contains = getObjects(Player.class).contains(player1);
			expectedContains = true;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(Player.class).contains(player1) --> " + contains + " --> " + passState);

			contains = getObjects(Player.class).contains(player2);
			expectedContains = true;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(Player.class).contains(player2) --> " + contains + " --> " + passState);

			size = getObjects(Player.class).size();
			expectedSize = 2;
			pass = expectedSize == size;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(Player.class).size() --> " + size + " --> " + passState);

			System.out.println("Testing when two players and a non-Actor are in the world.");
			Label label = new Label("label");
			getChildren().add(label);

			size = getObjects(Player.class).size();
			expectedSize = 2;
			pass = expectedSize == size;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(Player.class).size() --> " + size + " --> " + passState);

			System.out.println("Removing the label.");
			getChildren().remove(label);

			System.out.println("Testing when two players and a Coin are in the world.");
			coin = new Coin();
			add(coin);
			contains = getObjects(Coin.class).contains(coin);
			expectedContains = true;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(Coin.class).contains(coin) --> " + contains + " --> " + passState);

			contains = getObjects(Coin.class).contains(player1);
			expectedContains = false;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(Coin.class).contains(player1) --> " + contains + " --> " + passState);

			size = getObjects(Coin.class).size();
			expectedSize = 1;
			pass = expectedSize == size;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(Coin.class).size() --> " + size + " --> " + passState);

			contains = getObjects(TestActor.class).contains(player1);
			expectedContains = true;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(TestActor.class).contains(player1) --> " + contains + " --> " + passState);

			contains = getObjects(TestActor.class).contains(player2);
			expectedContains = true;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(TestActor.class).contains(player2) --> " + contains + " --> " + passState);

			contains = getObjects(TestActor.class).contains(coin);
			expectedContains = true;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(TestActor.class).contains(coin) --> " + contains + " --> " + passState);

			size = getObjects(TestActor.class).size();
			expectedSize = 3;
			pass = expectedSize == size;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(TestActor.class).size() --> " + size + " --> " + passState);

			System.out.println("*** Testing World getObjectsAt(double x, double y, Class cls) ***");
			coin.setX(50);
			coin.setY(100);
			System.out.println("coin at (" + coin.getX() + ", " + coin.getY() + ")");
			contains = getObjectsAt(55, 105, Coin.class).contains(coin);
			expectedContains = true;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjectsAt(55, 105, Coin.class).contains(coin) --> " + contains + " --> " + passState);

			contains = getObjectsAt(55, 105, TestActor.class).contains(coin);
			expectedContains = true;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjectsAt(55, 105, Coin.class).contains(coin) --> " + contains + " --> " + passState);

			contains = getObjectsAt(55, 105, Player.class).contains(coin);
			expectedContains = false;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjectsAt(55, 105, Player.class).contains(coin) --> " + contains + " --> " + passState);

			contains = getObjectsAt(150, 200, Coin.class).contains(coin);
			expectedContains = false;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjectsAt(150, 200, Coin.class).contains(coin) --> " + contains + " --> " + passState);

			contains = getObjectsAt(50 + 41, 105, Coin.class).contains(coin);
			expectedContains = false;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjectsAt(50 + 41, 105, Coin.class).contains(coin) --> " + contains + " --> " + passState);

			contains = getObjectsAt(55, 100 + 41, Coin.class).contains(coin);
			expectedContains = false;
			pass = expectedContains == contains;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjectsAt(55, 100 + 41, Coin.class).contains(coin) --> " + contains + " --> " + passState);

			System.out.println("*** Testing World remove(Actor actor) ***");
			remove(coin);

			System.out.println("removed coin");
			size = getObjects(Coin.class).size();
			expectedSize = 0;
			pass = expectedSize == size;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("getObjects(Coin.class).size() --> " + size + " --> " + passState);

			System.out.println("added coin back");
			add(coin);

			System.out.println("*** Testing Actor getWorld() ***");
			World world = player1.getWorld();
			World expectedWorld = this;
			pass = expectedWorld == world;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player1.getWorld() --> " + world + " --> " + passState);

			System.out.println("Removing player1");
			remove(player1);
			world = player1.getWorld();
			expectedWorld = null;
			pass = expectedWorld == world;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player1.getWorld() --> " + world + " --> " + passState);

			System.out.println("adding player1 back");
			add(player1);

			System.out.println("*** Testing Actor move(double dx, double dy) ***");
			player1.setX(100);
			player1.setY(150);
			System.out.println("Starting out at (" + player1.getX() + ", " + player1.getY() + ")");
			player1.move(5,  10);
			double x = player1.getX();
			double y = player1.getY();
			double expectedX = 105;
			double expectedY = 160;
			pass = expectedX == x && expectedY == y;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player1.move(5, 10) --> (" + player1.getX() + ", " + player1.getY() + ")" + " --> " + passState);

			System.out.println("*** Testing Actor getWidth() and getHeight() ***");
			width = player1.getWidth();
			expectedWidth = 30;
			pass = expectedWidth == width;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player1.getWidth() --> " + width + " --> " + passState);

			height = player1.getHeight();
			expectedHeight = 32;
			pass = expectedHeight == height;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player1.getHeight() --> " + height + " --> " + passState);

			System.out.println("*** Testing Actor getIntersectingObjects(Class cls) ***");
			player1.setX(50);
			player1.setY(75);
			player2.setX(70);
			player2.setY(100);
			coin.setX(60);
			coin.setY(90);
			System.out.println("player1 at (" + player1.getX() + ", " + player1.getY() + ")");
			System.out.println("player2 at (" + player2.getX() + ", " + player2.getY() + ")");
			System.out.println("coin at (" + coin.getX() + ", " + coin.getY() + ")");
			boolean touching = coin.getIntersectingObjects(Player.class).contains(player1);
			boolean expectedTouching = true;
			pass = expectedTouching == touching;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("coin.getIntersectingObjects(Player.class).contains(player1) --> " + touching + " --> " + passState);

			touching = coin.getIntersectingObjects(Player.class).contains(player2);
			expectedTouching = true;
			pass = expectedTouching == touching;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("coin.getIntersectingObjects(Player.class).contains(player2) --> " + touching + " --> " + passState);

			size = coin.getIntersectingObjects(Player.class).size();
			expectedSize = 2;
			pass = expectedSize == size;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("coin.getIntersectingObjects(Player.class).size() --> " + size + " --> " + passState);

			touching = coin.getIntersectingObjects(Coin.class).contains(coin);
			expectedTouching = false;
			pass = expectedTouching == touching;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("coin.getIntersectingObjects(Coin.class).contains(coin) --> " + touching + " --> " + passState);

			touching = player1.getIntersectingObjects(TestActor.class).contains(player2);
			expectedTouching = true;
			pass = expectedTouching == touching;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player1.getIntersectingObjects(TestActor.class).contains(player2) --> " + touching + " --> " + passState);

			touching = player1.getIntersectingObjects(TestActor.class).contains(player1);
			expectedTouching = false;
			pass = expectedTouching == touching;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player1.getIntersectingObjects(TestActor.class).contains(player1) --> " + touching + " --> " + passState);

			coin.setX(200);
			coin.setY(190);
			System.out.println("coin at (" + coin.getX() + ", " + coin.getY() + ")");
			touching = coin.getIntersectingObjects(Player.class).contains(player2);
			expectedTouching = false;
			pass = expectedTouching == touching;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("coin.getIntersectingObjects(Player.class).contains(player2) --> " + touching + " --> " + passState);

			size = coin.getIntersectingObjects(Player.class).size();
			expectedSize = 0;
			pass = expectedSize == size;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("coin.getIntersectingObjects(Player.class).size() --> " + size + " --> " + passState);


			System.out.println("*** Testing Actor getOneIntersectingObject(Class cls) ***");
			player1.setX(50);
			player1.setY(80);
			System.out.println("player1 at (" + player1.getX() + ", " + player1.getY() + ")");
			coin.setX(60);
			coin.setY(90);
			System.out.println("coin at (" + coin.getX() + ", " + coin.getY() + ")");
			TestActor touched = player1.getOneIntersectingObject(Coin.class);
			TestActor expectedTouched = coin;
			System.out.println("touched = " + touched + " and expectedTouched = " + expectedTouched);
			pass = expectedTouched == touched;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player1.getOneIntersectingObject(Coin.class) --> " + touched + " --> " + passState);

			touched = player1.getOneIntersectingObject(Player.class);
			expectedTouched = player2;
			pass = expectedTouched == touched;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player1.getOneIntersectingObject(Player.class) --> " + touched + " --> " + passState);

			coin.setX(160);
			coin.setY(190);
			System.out.println("coin at (" + coin.getX() + ", " + coin.getY() + ")");
			touched = player1.getOneIntersectingObject(Coin.class);
			expectedTouched = null;
			pass = expectedTouched == touched;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player1.getOneIntersectingObject(Coin.class) --> " + touched + " --> " + passState);

			player2.setX(170);
			player2.setY(300);
			System.out.println("player2 at (" + player2.getX() + ", " + player2.getY() + ")");
			touched = player1.getOneIntersectingObject(Player.class);
			expectedTouched = null;
			pass = expectedTouched == touched;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player1.getOneIntersectingObject(Player.class) --> " + touched + " --> " + passState);

			System.out.println("*** Testing Actor is using correct bounds for getWidth, getHeight and getOneIntersectingObject ***");
			// testing that correct bounds is used that takes into account transformations
			System.out.println("Removing player1 from the world.");
			remove(player1);
			player2.setX(0);
			player2.setY(0);
			coin.setX(200);
			coin.setY(200);
			System.out.println("Before scaling coin dimensions are (" + coin.getWidth() + ", " + coin.getHeight() + ")");
			double prevScaleX = coin.getScaleX();
			double prevScaleY = coin.getScaleY();
			coin.setScaleX(10);
			coin.setScaleY(10);
			System.out.println("player2 at (" + player2.getX() + ", " + player2.getY() + ")");
			System.out.println("coin at (" + coin.getX() + ", " + coin.getY() + ")");
			System.out.println("coin scaled to (" + coin.getScaleX() + "x" + coin.getScaleY() + ")");
			System.out.println("(coin.getWidth(), coin.getHeight()) = (" + coin.getWidth() + ", " + coin.getHeight() + "))");
			
			double coinW = coin.getWidth();
			pass = coinW == coin.getBoundsInParent().getWidth();
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("coin.getWidth() --> " + coinW + " --> " + passState);

			double coinH = coin.getHeight();
			pass = coinH == coin.getBoundsInParent().getHeight();
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("coin.getHeight() --> " + coinH + " --> " + passState);

			// coin should touch player2
			touched = coin.getOneIntersectingObject(Player.class);
			expectedTouched = player2;
			pass = expectedTouched == touched;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("coin.getOneIntersectingObject(Player.class) --> " + touched + " --> " + passState);

			// same test but with getIntersectingObjects(Player.class)
			System.out.println("Adding player1 back to the world.");
			add(player1);
			List<Player> touchedList = coin.getIntersectingObjects(Player.class);
			Set<Player> expectedPlayers = new HashSet<>(Arrays.asList(player1, player2));
			pass = expectedPlayers.equals(new HashSet<>(touchedList));
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("coin.getIntersectingObjects(Player.class) --> " + touchedList + " --> " + passState);
			
			coin.setScaleX(prevScaleX);
			coin.setScaleY(prevScaleY);
			
			try {
				rob = new Robot();
			} catch (AWTException e1) {
				exceptions.add(e1);
				e1.printStackTrace();
			}

			System.out.println("*** Testing World start() and act() ***");
			
			for (int i = 0; i < 5; i++) {
				CounterCoin c = new CounterCoin();
				counterCoins.add(c);
				add(c);
			}
			
			KeyFrame testStart = new KeyFrame(Duration.seconds(0.5), e -> testStart());
			KeyFrame stopAndRecordFrameCount = new KeyFrame(Duration.seconds(1), e -> stopAndRecordFrameCount());
			KeyFrame testAfterStop = new KeyFrame(Duration.seconds(1.5), e -> testAfterStop());
			KeyFrame startAgain = new KeyFrame(Duration.seconds(2.0), e -> startAgain());
			KeyFrame pressUpAndRightKeys = new KeyFrame(Duration.seconds(2.5), e -> pressUpAndRightKeys());
			KeyFrame testUpAndRightKeyPress = new KeyFrame(Duration.seconds(3.0), e -> testUpAndRightKeyPress());
			KeyFrame releaseUpKey = new KeyFrame(Duration.seconds(3.5), e -> releaseUpKey());
			KeyFrame testUpKeyRelease = new KeyFrame(Duration.seconds(4.0), e -> testUpKeyRelease());
			KeyFrame releaseRightKey = new KeyFrame(Duration.seconds(4.5), e -> releaseRightKey());
			KeyFrame testRightKeyRelease = new KeyFrame(Duration.seconds(5.0), e -> testRightKeyRelease()); 
			KeyFrame removeCoin = new KeyFrame(Duration.seconds(5.5), e -> removeCoin());
			KeyFrame removePlayer1 = new KeyFrame(Duration.seconds(6.0), e -> removePlayer1());
			KeyFrame finishTest = new KeyFrame(Duration.seconds(6.5), e -> finishTest());
			Timeline timeline = new Timeline(
					testStart,
					stopAndRecordFrameCount,
					testAfterStop,
					startAgain,
					pressUpAndRightKeys,
					testUpAndRightKeyPress,
					releaseUpKey,
					testUpKeyRelease,
					releaseRightKey,
					testRightKeyRelease,
					removeCoin,
					removePlayer1,
					finishTest
			);
			Platform.runLater(() -> timeline.play());
			start();
		} catch (Exception err) {
			exceptions.add(err);
			err.printStackTrace();
		}
	}

	@Override
	public void act(long now) {
		try {
			frameCount++;
			if (frameCount == 5) {
				System.out.println("First coin will removing itself this frame.");
				counterCoins.get(0).setShouldRemoveSelf(true);
			} else if (frameCount == 6) {
				System.out.println("Confirm CounterCoins have correct frame count.");
				for (CounterCoin cn : counterCoins) {
					if (cn.getFrameCount() != 5) {
						passedAllTests = false;
						System.out.println("No actors skipped when an actor is removed while looping through actors and calling act() --> failed");
					}
				}
			}
			if (removePlayer1) {
				System.out.println("Removing player1 in the World act() method.");
				remove(player1);
				removePlayer1 = false;
			}
		} catch (Exception err) {
			exceptions.add(err);
			err.printStackTrace();
		}
	}

	private void testStart() {
		try {
			System.out.println("frameCount of World after 0.5 seconds:");
			boolean pass = frameCount > 3;
			String passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("frameCount --> " + frameCount + " --> " + passState);

			System.out.println("frameCount of player1 after 0.5 seconds:");
			pass = player1.getFrameCount() == frameCount;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player1.getFrameCount() --> " + player1.getFrameCount() + " --> " + passState);

			System.out.println("frameCount of player2 after 0.5 seconds:");
			pass = player2.getFrameCount() == frameCount;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player2.getFrameCount() --> " + player2.getFrameCount() + " --> " + passState);

			System.out.println("frameCount of coin after 0.5 seconds:");
			pass = coin.getFrameCount() == frameCount;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("coin.getFrameCount() --> " + coin.getFrameCount() + " --> " + passState);

			System.out.println("isStopped() value after 0.5 seconds while timer is running.");
			boolean isStopped = isStopped();
			boolean expectedStop = false;
			pass = isStopped == expectedStop;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("isStopped() --> " + isStopped + " --> " + passState);
		} catch (Exception err) {
			exceptions.add(err);
			err.printStackTrace();
		}
	}

	private void stopAndRecordFrameCount() {
		try {
			System.out.println("*** Testing World stop() ***");
			System.out.println("calling stop()");
			stop();
			frameCountAtStop = frameCount;
			System.out.println("frameCountAtStop of World = " + frameCountAtStop);
			player1.recordStopFrameCount();
			System.out.println("frameCountAtStop of player1 = " + player1.getStopFrameCount());
			player2.recordStopFrameCount();
			System.out.println("frameCountAtStop of player2 = " + player2.getStopFrameCount());
			coin.recordStopFrameCount();
			System.out.println("frameCountAtStop of coin = " + coin.getStopFrameCount());
		} catch (Exception err) {
			exceptions.add(err);
			err.printStackTrace();
		}
	}

	private void testAfterStop() {
		try {
			System.out.println("isStopped() value 0.5 seconds after stop()");
			boolean isStopped = isStopped();
			boolean expectedStop = true;
			boolean pass = isStopped == expectedStop;
			String passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("isStopped() --> " + isStopped + " --> " + passState);

			System.out.println("frameCount of World 0.5 seconds ater stop()");
			pass = frameCount == frameCountAtStop;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("frameCount --> " + frameCount + " --> " + passState);

			System.out.println("frameCount of player1 0.5 seconds ater stop()");
			pass = player1.getFrameCount() == player1.getStopFrameCount() && player1.getFrameCount() == frameCount;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player1.getFrameCount() --> " + player1.getFrameCount() + " --> " + passState);

			System.out.println("frameCount of player2 0.5 seconds ater stop()");
			pass = player2.getFrameCount() == player2.getStopFrameCount() && player2.getFrameCount() == frameCount;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("player2.getFrameCount() --> " + player2.getFrameCount() + " --> " + passState);

			System.out.println("frameCount of coin 0.5 seconds ater stop()");
			pass = coin.getFrameCount() == coin.getStopFrameCount() && coin.getFrameCount() == frameCount;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("coin.getFrameCount() --> " + coin.getFrameCount() + " --> " + passState);
		} catch (Exception err) {
			exceptions.add(err);
			err.printStackTrace();
		}
	}
	
	private void startAgain() {
		start();
	}

	private void pressUpAndRightKeys() {
		try {
			System.out.println("Pressing the up arrow and right arrow keys");
			rob.keyPress(KeyEvent.VK_UP);
			rob.keyPress(KeyEvent.VK_RIGHT);
		} catch (Exception err) {
			exceptions.add(err);
			err.printStackTrace();
		}
	}

	private void testUpAndRightKeyPress() {
		try {
			System.out.println("** Testing World isKeyPressed() after pressing up and right keys");
			boolean upKeyPressed = isKeyPressed(KeyCode.UP);
			boolean expectedUpKey = true;
			boolean pass = upKeyPressed == expectedUpKey;
			String passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("isKeyPressed(KeyCode.UP) --> " + upKeyPressed + " --> " + passState);

			boolean rightKeyPressed = isKeyPressed(KeyCode.RIGHT);
			boolean expectedRightKey = true;
			pass = rightKeyPressed == expectedRightKey;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("isKeyPressed(KeyCode.RIGHT) --> " + rightKeyPressed + " --> " + passState);

			boolean leftKeyPressed = isKeyPressed(KeyCode.LEFT);
			boolean expectedLeftKey = false;
			pass = leftKeyPressed == expectedLeftKey;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("isKeyPressed(KeyCode.LEFT) --> " + leftKeyPressed + " --> " + passState);
		} catch (Exception err) {
			exceptions.add(err);
			err.printStackTrace();
		}
	}

	private void releaseUpKey() {
		try {
			System.out.println("Releasing the up arrow key");
			rob.keyRelease(KeyEvent.VK_UP);
		} catch (Exception err) {
			exceptions.add(err);
			err.printStackTrace();
		}
	}

	private void testUpKeyRelease() {
		try {
			System.out.println("** Testing World isKeyPressed() after releasing up arrow key");
			boolean upKeyPressed = isKeyPressed(KeyCode.UP);
			boolean expectedUpKey = false;
			boolean pass = upKeyPressed == expectedUpKey;
			String passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("isKeyPressed(KeyCode.UP) --> " + upKeyPressed + " --> " + passState);

			boolean rightKeyPressed = isKeyPressed(KeyCode.RIGHT);
			boolean expectedRightKey = true;
			pass = rightKeyPressed == expectedRightKey;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("isKeyPressed(KeyCode.RIGHT) --> " + rightKeyPressed + " --> " + passState);
		} catch (Exception err) {
			exceptions.add(err);
			err.printStackTrace();
		}
	}

	private void releaseRightKey() {
		try {
			System.out.println("Releasing the right arrow key");
			rob.keyRelease(KeyEvent.VK_RIGHT);
		} catch (Exception err) {
			exceptions.add(err);
			err.printStackTrace();
		}
	}

	private void testRightKeyRelease() {
		try {
			System.out.println("** Testing World isKeyPressed() after releasing right arrow key");
			boolean upKeyPressed = isKeyPressed(KeyCode.UP);
			boolean expectedUpKey = false;
			boolean pass = upKeyPressed == expectedUpKey;
			String passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("isKeyPressed(KeyCode.UP) --> " + upKeyPressed + " --> " + passState);

			boolean rightKeyPressed = isKeyPressed(KeyCode.RIGHT);
			boolean expectedRightKey = false;
			pass = rightKeyPressed == expectedRightKey;
			passState = pass ? "passed" : "failed";
			if (!pass) passedAllTests = false;
			System.out.println("isKeyPressed(KeyCode.RIGHT) --> " + rightKeyPressed + " --> " + passState);
		} catch (Exception err) {
			exceptions.add(err);
			err.printStackTrace();
		}
	}

	private void removeCoin() {
		System.out.println("Testing removing a coin from world while looping through and calling act on actors().");
		shouldRemoveCoinThisFrame = true;
	}

	private void removePlayer1() {
		System.out.println("Testing removing player1 from world in TestWorld act().");
		removePlayer1 = true;
	}

	private void finishTest() {
		try {
			stop();
		} catch (Exception err) {
			exceptions.add(err);
			err.printStackTrace();
		}
		if (passedAllTests && exceptions.size() == 0) System.out.println("PASSED ALL TESTS WITH NO EXCEPTIONS!");
		else {
			if (!passedAllTests) System.out.println("ONE OR MORE TESTS FAILED!");
			if (exceptions.size() > 0) System.out.println("EXCEPTIONS WERE THROWN!");
		}
		Platform.exit();
	}

	public boolean isShouldRemoveCoinThisFrame() {
		return shouldRemoveCoinThisFrame;
	}

	public void setShouldRemoveCoinThisFrame(boolean shouldRemoveCoinThisFrame) {
		this.shouldRemoveCoinThisFrame = shouldRemoveCoinThisFrame;
	}

	public boolean isTestingFrameCount() {
		return testingFrameCount;
	}

	public void setTestingFrameCount(boolean testingFrameCount) {
		this.testingFrameCount = testingFrameCount;
	}
	
	

}
