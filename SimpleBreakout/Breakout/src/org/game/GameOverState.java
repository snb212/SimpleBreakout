package org.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class GameOverState extends BasicGameState{

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		if(SetupClass.gameLives <= 0){
			g.drawString("You Lose.", 300, 200);
		} else {
			g.drawString("You Won! Congratulations!", 300, 200);
		}
		
		g.drawString("Lives: " + SetupClass.gameLives, 300, 250);
		g.drawString("Score: " + SetupClass.gameScore, 300, 300);
		g.setColor(Color.yellow);
		g.drawString("Press <SPACE> to retry", 300, 400);
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		if(container.getInput().isKeyPressed(Input.KEY_SPACE)){
			System.out.println("got here1");
			game.enterState(0, new FadeInTransition(), new FadeOutTransition());
		}
		if(container.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			container.exit();
		}
	}


	public int getID() {
		
		return 1;
	}
	
}
