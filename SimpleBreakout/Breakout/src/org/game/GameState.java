package org.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.game.objects.BreakoutBall;
import org.game.objects.BreakoutBlock;
import org.game.objects.BreakoutMouseBar;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.ResourceLoader;

public class GameState extends BasicGameState {

	private final static int SCREEN_WIDTH = 800;
	private final static int SCREEN_HEIGHT = 600;
	private static float BALL_STARTING_SPEED = 4;

	private ArrayList<BreakoutBall> balls;
	private ArrayList<BreakoutBlock> blocks;
	private BreakoutMouseBar mouseBar;
	private int mouseBarVelocityTimer;
	private int bounceTimer;
	private Random random;
	private int currentBallId;
	private int currentBlockId;
	private float ballSpeed;
	private boolean gridBuilt;
	private boolean paused;
	private boolean discoMode;

	private Music discoMusic2;

	private List<Color> colors = Arrays.asList(Color.red, Color.blue, Color.cyan, Color.green, Color.magenta, Color.orange, Color.pink, Color.yellow);;
	//cooldown for bounces, without it objects can get stuck constantly changing direction
	private boolean bounceWait;

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {

		reset();

			discoMusic2 = new Music("resources/audio/Sonic-3-Music-Invincibility.ogg");
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (container.getInput().isKeyPressed(Input.KEY_1)) {
			game.enterState(1, new FadeOutTransition(), new FadeInTransition());
		}
		if (container.getInput().isKeyPressed(Input.KEY_2)) {
			reset();
		}
		if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			container.exit();
		}
		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			paused = !paused;
		}
		if (container.getInput().isKeyPressed(Input.KEY_R)) {
			discoMode = !discoMode;
			if(discoMode){
				discoMusic2.loop();
			} else {
				discoMusic2.stop();
			}
		}

		if (!paused) {

			bounceTimer += delta;

			if (bounceTimer > 3) {
				bounceWait = false;
				bounceTimer = 0;
			}

			mouseBar.setCenterX(container.getInput().getAbsoluteMouseX());
			mouseBar.setCenterY(500);
			mouseBarVelocityTimer = mouseBarVelocityTimer + delta;
			//if no ball exists make one
			if (balls.isEmpty()) {
				createBreakoutBall();
			}

			//move the ball to it's new position
			for (BreakoutBall ball : balls) {
				//to avoid dividing an integer by and integer use float 
				System.out.println("Ball moved from: " + ball.getPositionX() + " to " + (ball.getPositionX() + ball.getVectorX()) + " and " + ball.getPositionY() + " to " + (ball.getPositionY() + ball.getVectorY()));

				ball.setPositionY(ball.getPositionY() + (delta / ballSpeed) * ball.getVectorY());
				ball.setPositionX(ball.getPositionX() + (delta / ballSpeed) * ball.getVectorX());
			}

			//reading array back to front so that removing items does not effect the list locator id

			if (!gridBuilt) {
				buildGrid(10, 5);
			}

			moveBalls();

			if (mouseBarVelocityTimer > 10) {
				mouseBar.updateLocation(container.getInput().getAbsoluteMouseX(), delta);
				mouseBarVelocityTimer = 0;
			} else {
				//timePassed =+ delta;
				System.out.println("Time Passed3: " + mouseBarVelocityTimer);
			}
			if (SetupClass.gameLives <= 0 || blocks.size() == 0) {
				game.enterState(1, new FadeOutTransition(), new FadeInTransition());
			}
		}
		SoundStore.get().poll(0);
	}

	private void buildGrid(int numLong, int numDeep) {
		//((screen width - side margin) / (number of blocks) ) - block margin & number of blocks
		int blocksWidth = ((SCREEN_WIDTH - 180) / numLong);

		float blockPadding = 5;
		float heightPadding = 40;
		float currentHeight = 0;
		float prevBlockX = blocksWidth + blockPadding;

		//build out on x axis
		for (int i = 0; i < numLong; i++) {

			//y axis
			for (int j = 0; j < numDeep; j++) {
				System.out.println("j: " + j);
				//float positionX = prevBlockX + blocksWidth + blockPadding;

				blocks.add(new BreakoutBlock(prevBlockX, heightPadding + currentHeight, blocksWidth, 30, currentBlockId, colors.get(random.nextInt(colors.size()))));
				currentBlockId++;
				currentHeight += heightPadding;
			}
			//prevBlockX = positionX;
			prevBlockX = prevBlockX + blocksWidth + blockPadding;

			currentHeight = 0;

		}

		gridBuilt = true;

	}

	private void moveBalls() {
		ArrayList<BreakoutBlock> removals = new ArrayList<BreakoutBlock>();

		for (int i = balls.size() - 1; i >= 0; i--) {
			BreakoutBall ball = balls.get(i);
			if (ball.getPositionY() > SCREEN_HEIGHT + 10 && !bounceWait) {
				balls.remove(i);
				SetupClass.gameLives--;
				createBreakoutBall();
			} else if (ball.intersects(mouseBar) && !bounceWait) {
				ball.setVectorY(-(Math.abs(ball.getVectorY())));
				ball.setVectorX(ball.getVectorX() + mouseBar.getVelocity() / 10);
			} else if (ball.getPositionY() <= 10 && !bounceWait) {
				ball.setVectorY(-ball.getVectorY());
			} else if (ball.getPositionX() <= 0 || ball.getPositionX() >= (SCREEN_WIDTH - 10) && !bounceWait) {
				ball.setVectorX(-ball.getVectorX());
			}

			for (BreakoutBlock block : blocks) {
				if (ball.intersects(block)) {
					removals.add(block);
					ball.setVectorY(-ball.getVectorY());
					SetupClass.gameScore++;
				}
			}
		}
		for (int i = blocks.size() - 1; i >= 0; i--) {
			int id = blocks.get(i).getBlockId();

			if (removals.contains(blocks.get(i))) {
				System.out.println("Hut Block id-" + blocks.get(i).getBlockId() + " removed");
				blocks.remove(blocks.get(i));

			}
		}
	}

	private void createBreakoutBall() {
		float vectorX = random.nextInt(100) / 100f;
		float vectorY = (random.nextInt(50) + 50) / 100f;

		int positionX = random.nextInt(500);
		balls.add(new BreakoutBall(positionX, 300, vectorX, vectorY, currentBallId, true));

		System.out.println("Building new ball at x:" + positionX + " y:200 vx:" + vectorX + " vy:" + vectorY);
		currentBallId++;
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		//g.drawString("Press the \"1\" Button!", 300, 200);
		g.setColor(Color.yellow);
		g.drawString("Score: " + SetupClass.gameScore, 50, 380);
		g.drawString("Lives: " + SetupClass.gameLives, 50, 395);
		g.drawString("Velocity: " + (String) String.format("%.2f", mouseBar.getVelocity()).toString(), 50, 410);
		g.drawString("Current Blocks: " + blocks.size(), 50, 365);
		g.drawString("Press R for Party Mode ", 50, 550 );

		if (paused) {
			g.drawString("Paused!", 300, 400);
		}

		g.setColor(Color.blue);
		g.fill(mouseBar);

		g.setColor(Color.red);
		for (BreakoutBall c : balls) {
			g.fill(c);
		}

		for (BreakoutBlock block : blocks) {
			//breakout rave mode
			if (discoMode) {
				g.setColor(colors.get(random.nextInt(colors.size())));
			} else {
				g.setColor(block.getColor());
			}
			g.fill(block);
		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		reset();
	}

	public void reset() {
		balls = new ArrayList<BreakoutBall>();
		mouseBar = new BreakoutMouseBar(300, 500, 100, 20);
		mouseBarVelocityTimer = 0;
		bounceTimer = 0;
		random = new Random();
		SetupClass.gameScore = 0;
		SetupClass.gameLives = 3;
		currentBallId = 0;
		currentBlockId = 0;
		ballSpeed = BALL_STARTING_SPEED;
		gridBuilt = false;
		this.blocks = new ArrayList<BreakoutBlock>();
		paused = false;
		bounceWait = false;
		discoMode = false;

		System.out.println("Reset all Game State variables");
	}

	public int getID() {

		return 0;
	}

}
