package Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener
{
	Game game;
	
	public KeyHandler(Game game)
	{
		this.game = game;
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{

	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		int code = e.getKeyCode();
		
		if(game.gameStatus == game.play)
		{
			playState(code);
		}
		else if(game.gameStatus == game.init)
		{
			initState(code);
		}
		else if(game.gameStatus == game.win)
		{
			winState(code);
		}
		else if(game.gameStatus == game.lose)
		{
			loseState(code);
		}
	}

	public void playState(int code)
	{
		switch(code)
		{
			case KeyEvent.VK_D:	// fall through
			case KeyEvent.VK_RIGHT: Pacman.nextDir = Character.right; break;
			case KeyEvent.VK_A:	// fall through
			case KeyEvent.VK_LEFT: Pacman.nextDir = Character.left; break;
			case KeyEvent.VK_W:	// fall through
			case KeyEvent.VK_UP: Pacman.nextDir = Character.upwards; break;
			case KeyEvent.VK_S:	// fall through
			case KeyEvent.VK_DOWN: Pacman.nextDir = Character.downwards; break;
		}
	}
	
	public void initState(int code)
	{
		enableScrolling(code, 1);
		
		if(code == KeyEvent.VK_ENTER)
		{
			switch(game.menuOptionIndex)
			{
				case 0: game.enter = true; break;
				case 1: System.exit(0); break;
			}
			
			game.menuOptionIndex = 0;
		}
	}
	
	public void loseState(int code)
	{
		enableScrolling(code, 2);
		
		if(code == KeyEvent.VK_ENTER)
		{
			switch(game.menuOptionIndex)
			{
				case 0: game.space = true; break;
				case 1: game.enter = true; break;
				case 2: System.exit(0); break;
			}
			
			game.menuOptionIndex = 0;
		}
	}
	
	public void winState(int code)
	{
		enableScrolling(code, 2);
		
		if(code == KeyEvent.VK_ENTER)
		{
			switch(game.menuOptionIndex)
			{
				case 0: game.enter = true; break;
				case 1: game.space = true; break;
				case 2: System.exit(0); break;
			}
			
			game.menuOptionIndex = 0;
		}
	}
	
	private void enableScrolling(int code, int maxIndex)
	{
		if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP)
		{
			game.menuOptionIndex--;
			
			if(game.menuOptionIndex < 0)
			{
				game.menuOptionIndex = maxIndex;
			}
		}
		else if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN)
		{
			game.menuOptionIndex++;
			
			if(game.menuOptionIndex > maxIndex)
			{
				game.menuOptionIndex = 0;
			}
		}
	}
}
