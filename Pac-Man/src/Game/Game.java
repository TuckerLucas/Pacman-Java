/**************************************************************
* Created by: Lucas Tucker (tucker.lucas.1404@gmail.com)
* 
* File: Game.java
* 
* Description: 
* 
* This file manages the game states and is responsible for 
* loading the characters into the map as well as displaying
* the appropriate content/messages to the user depending on the 
* current state of the game.
* 
/**************************************************************/

package Game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import GUI.CLayout;
import GUI.LeaderboardPanel;

public class Game extends Canvas implements Runnable, KeyListener
{
	private static final long serialVersionUID = 1L;
	
	// Game running variables
	private static Thread thread;
	private static boolean isRunning = false;	

	// Game boundaries variables
	public static final int WIDTH  = 672;
	public static final int	HEIGHT = 800;	
	
	// Game difficulty variable
	public static int difficulty = 1;
	
	// Game object variables
	public static Pacman pacman;			
	public static Ghost ghostArray[] = new Ghost[4];
	public static Energizer energizer;
	public static Door door;
	public static BonusScore bonusScore;
	public static Level level;
	public static Texture texture;

	private static int LIVES = 3;
	
	// Pacman lives variable
	public static int lives = LIVES;
	
	// Ghost identifiers
	public static final int blinkyID = 0;
	public static final int inkyID 	 = 1;
	public static final int pinkyID  = 2;
	public static final int clydeID  = 3;
	
	// Portal coordinate variables
	public static int leftPortalX  = 0;
	public static int leftPortalY  = 320;
	public static int rightPortalX = 640;
	public static int rightPortalY = 320;
	
	// Center box coordinate variables
	public static int centerBoxX = 320;
	public static int centerBoxY = 320;

	// Game status variables
	public static int gameStatus 	 = 0;
	public static final int init 	 = 1;		
	public static final int play 	 = 2;
	public static final int win 	 = 3;
	public static final int lose 	 = 4; 
	public static final int lifeLost = 5;
	
	// Score variables
	public static int highscore;
	public static int score 		 = 0;
	public static int foodScore 	 = 10;
	public static int energizerScore = 50;
	
	// Game key flag variables
	public boolean enter = false;					
	public boolean space = false;
	
	// Animation variables
	private int blinkTime 	 = 0;							
	private int targetFrames = 30;
	private static boolean showText = true;
	
	// Map coordinate variables to locate events
	public static int xEvent;
	public static int yEvent;
	
	public static int directionsArray[] = new int[4];
	
	// Paths to required resources
	String scoresPath = "res/Files/Scores.txt";
	

	
	// Constructor
	public Game()
	{
		addKeyListener(this);
		gameStatus = init;			
		texture = new Texture();
		
		// Get game's highscore
    	try
		{
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(scoresPath));
			String line = reader.readLine();
			highscore = Integer.parseInt(line);
	        reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	
	// Start a new game thread
	public synchronized void startGame()
	{
		// Check if game is already running
		if(isRunning)
		{
			return;
		}
			
		// Start the game
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	// Stop the running game thread
	public synchronized static void stopGame()
	{
		// Check if game is running
		if(!isRunning)
		{
			return;
		}
		
		// Stop the game
		isRunning = false;
		
		try
		{
			thread.join();
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	
	// Load the required game elements
	public static void loadGameElements()
	{
		// Load game characters
		pacman 			= new Pacman(Pacman.notCrossingPortal);
		ghostArray[0] 	= new Ghost(0, Ghost.randomMovement, Ghost.notCrossingPortal, false, true); 
		ghostArray[1] 	= new Ghost(1, Ghost.randomMovement, Ghost.notCrossingPortal, false, true);
		ghostArray[2] 	= new Ghost(2, Ghost.randomMovement, Ghost.notCrossingPortal, false, true);
		ghostArray[3] 	= new Ghost(3, Ghost.randomMovement, Ghost.notCrossingPortal, false, true);

		// Load other game objects based on game status
		switch(gameStatus)
		{
			case init:
				
				door 	  = new Door(0, 0);
				energizer = new Energizer(0, 0);
				
				// fall through
				
			case win:
				
				// fall through
				
			case lose:
				
				// Load bonus score object
				bonusScore = new BonusScore();
				
				level = new Level(); 
				break;
				
			default: break;
		}
	}

	// Make game's screen text blink
	private void blinkText()
	{
		if(showText)
		{
			showText = false;
			return;
		}
		showText = true;
	}

	// Set game's screen lettering style
	public static void setLetteringStyle(Graphics g, Color color, String font, int fontSize)
	{
		// Lettering colour, size and font
		g.setColor(color);												
		g.setFont(new Font(font, Font.BOLD, fontSize)); 
	}
	
	
	// Draw game's initial screen
	private void drawInitScreen(Graphics g)
	{	
		// Display text
		if(showText)
		{
			g.drawString("ENTER", 266, 350); 
		}
		g.drawString("PRESS       TO START!", 170, 350); 
	}

	// Draw game's winning screen
	private void drawWinScreen(Graphics g)
	{		
		// Display text
		g.drawString("WELL DONE!", 210, 0);
		if(showText)
		{
			g.drawString("ENTER", 156, 350);
		}
		g.drawString("PRESS       TO CONTINUE GAME", 60, 350);
	}
	
	// Draw game's losing screen
	public static void drawLoseScreen(Graphics g)
	{		
		// Display text
		g.drawString("BAD LUCK!", 270, 100);
		if(showText)
		{
			g.drawString("ENTER", 156, 350);
			g.drawString("SPACE", 156, 400);
		}
		g.drawString("PRESS       TO RESTART GAME", 60, 350);
		g.drawString("PRESS       TO GO HOME", 60, 400);
	}
	
	private void drawPacmanDying(Graphics g)
	{
		pacman.render(g);
		
		if(gameStatus != lose)
		{
			level.render(g);
		}
	}

	// Tick function
	private void tick()
	{
		switch(gameStatus)
		{
			case play:
				
				pacman.tick();
				ghostArray[0].tick(); 
				ghostArray[1].tick();
				ghostArray[2].tick();
				ghostArray[3].tick();
				bonusScore.tick();
				energizer.tick();
				level.tick();
				texture.tick();
				
				Pacman.deathAnimationDisplayed = false;

				break;
				
			case init:
				
				blinkTime++;
				
				if(blinkTime == targetFrames)
				{
					blinkTime = 0;
					blinkText();
				}
				
				if(enter)
				{
					enter = false;
					loadGameElements();
					gameStatus = play;

            		Sounds.loop(Sounds.sirenSoundPath);
				}
				
				break;
				
			case win:
				
				BonusScore.display = false;
				
				Energizer.isActive = false;				

				Pacman.makeGhostsVulnerable();
				
				Energizer.activeTime = 0;
				
				blinkTime++;
				
				if(blinkTime == targetFrames)
				{
					blinkTime = 0;
					blinkText();
				}
				
				if(enter)
				{
					enter = false;
					loadGameElements();
					gameStatus = play;
				}
				
				break;
				
			case lose:
				
				LeaderboardPanel.read_from_file();
				LeaderboardPanel.swap_values();
				LeaderboardPanel.write_to_file();
				
				BonusScore.display = false;
				
				lives = LIVES;
				
				if(score >= highscore)
				{
	        		highscore = score;
				}
				
				blinkTime++;
				
				if(blinkTime == targetFrames)
				{
					blinkTime = 0;
					blinkText(); 
				}
				
				if(enter == true)
				{
					enter = false;
					score = 0;
					loadGameElements();
					gameStatus = play;
				}
				
				if(space == true)
				{
					space = false;
					score = 0;
					CLayout.cardLayout.show(CLayout.panelContainer, "Home");
					gameStatus = init;
				}
				
				break;
				
			case lifeLost:
				
				BonusScore.display = false;
				Energizer.deactivate();
				pacman.tick();
				
				break;
		}
					
		BonusScore.animationTime++;
		
		if(BonusScore.animationTime == BonusScore.animationTargetTime)
		{
			BonusScore.animationTime = 0;
			Texture.bonusScoreAnimationPhase++;
		}
		
		if(Game.score >= Game.highscore)
		{
			Game.highscore = Game.score;
		}
	}
	
	// Render/draw game's screens based on game status 
	private void render()
	{
		BufferStrategy bs = getBufferStrategy();
		
		if(bs == null)				
		{
			createBufferStrategy(3);	
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		// Configure screen background and lettering
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		setLetteringStyle(g, Color.white, Font.DIALOG_INPUT, 26);
		
		// Render screens based on status of the game
		switch(gameStatus)
		{
			case init: drawInitScreen(g); break;
			case win: drawWinScreen(g); break;
			case lose: drawLoseScreen(g); break;
			case lifeLost: drawPacmanDying(g); break; 
			case play: level.render(g); break;
		}
		
		g.dispose();
		bs.show();
	}
	
	// Manage game's timing
	@Override
	public void run() 
	{
		requestFocus();					
		
		long lastTime = System.nanoTime();		// Previous game instant
		double targetTick = 60.0; 				// Game speed
		double delta = 0;						// Time difference between game instants
		double ns = 1000000000 / targetTick; 	// Time interval between ticks
				
		// Game loop
		while(isRunning)				
		{	
			long now = System.nanoTime();		// This game instant
			delta += (now - lastTime) / ns;		// Calculate time difference between instants
			lastTime = now;						// Update previous game instant
			
			while(delta >= 1)					// Check if enough time has elapsed	to justify updating the game		
			{
				tick();							// Check for in game character positions and collisions			
				render();						// Render game components
				delta--;						// Update time difference between instants
			}
		}
		
		stopGame();							
	}

	
	// Listen for pressed keys
	@Override
	public void keyPressed(KeyEvent e)
	{
		switch(gameStatus)
		{
			case play: 
				
				// Read game keys
				switch(e.getKeyCode())
				{
					case KeyEvent.VK_D:	// fall through
					case KeyEvent.VK_RIGHT: Pacman.nextDir = Movement.right; break;
					case KeyEvent.VK_A:	// fall through
					case KeyEvent.VK_LEFT: Pacman.nextDir = Movement.left; break;
					case KeyEvent.VK_W:	// fall through
					case KeyEvent.VK_UP: Pacman.nextDir = Movement.up; break;
					case KeyEvent.VK_S:	// fall through
					case KeyEvent.VK_DOWN: Pacman.nextDir = Movement.down; break;
				}
				
				break;
				
			case lose:
				
				if(e.getKeyCode() == KeyEvent.VK_SPACE)
				{
					space = true;
				}
				
				// fall through
				
			case init:	
				
				// fall through
				
			case win:
				
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					enter = true;					
				}
				
				break;
		}
	}

	// Unimplemented method
	@Override
	public void keyReleased(KeyEvent e)
	{

	}
	
	// Unimplemented method
	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}
}