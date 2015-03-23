package org.game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

public class SetupClass extends StateBasedGame{
	
	public static int gameScore = 0;
	public static int gameLives = 3;
	
	public SetupClass (String title){
		super(title);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new SetupClass("Setup Test"));
		
		app.setDisplayMode(800, 600, false);
		app.setAlwaysRender(true);
		
		app.start();
	}

	public void initStatesList(GameContainer container) throws SlickException {
		this.addState(new GameState());
		this.addState(new GameOverState());
		
	}

}
