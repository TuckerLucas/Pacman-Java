/**************************************************************
* Created by: Lucas Tucker (tucker.lucas.1404@gmail.com)
* 
* File: Pacman.java
* 
* Description: 
* 
* This file contains the implementation for the Pacman 
* character regarding movement around the map, collisions with
* other characters/objects and animations.
* 
/**************************************************************/

package Game;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Pacman extends Rectangle
{
	private static final long serialVersionUID = 1L;
	
	// Intersected ghost variable
	private int intersectedGhost = -1;
	
	// Counter variable for number of eaten ghosts
	public static int nEatenGhosts = 0;
	
	// Pacman spawn coordinate variables
	public static int spawnX = 320;
	public static int spawnY = 512;
	
	// Spawn variables
	public static final int notCrossingPortal = 0;
	public static final int crossingLeftPortal = 1;
	public static final int crossingRightPortal = 2;
	
	// Direction variables
	public static final int stopped = -1;

	
	// Movement variables
	public int currentDir;
	public static int nextDir;
	
	// Pacman animation variables
	public static int eatingAnimationTime 		= 0;	
	public static int eatingAnimationTargetTime = 6;
	
	public static int deathAnimationTime 		= 0;	
	public static int deathAnimationTargetTime = 6;
	
	public static boolean deathAnimationDisplayed = false;
	
	// Constructor
	public Pacman(int spawnType)
	{
		// Check where to spawn pacman
		switch(spawnType)
		{
			// Normal spawn
			case notCrossingPortal:
				
				// Spawn pacman normally
				spawnPacman(spawnX, spawnY);
				
				// Pacman starts by moving right
				nextDir = Movement.right;
				
				// Make pacman look right on start-up
				currentDir = Movement.right;
				
				break;
				
			// Crossing left portal
			case crossingLeftPortal:
				
				// Spawn pacman at the right portal
				spawnPacman(Game.rightPortalX, Game.rightPortalY);
				
				// Keep pacman moving left
				nextDir = Movement.left;
				
				break;
				
			// Left portal spawn
			case crossingRightPortal:
				
				// Spawn pacman at the left portal
				spawnPacman(Game.leftPortalX, Game.leftPortalY);
				
				// Keep pacman moving right
				nextDir = Movement.right;
				
				break;
		}
	}

	
	// Spawn pacman at the given coordinates
	private void spawnPacman(int xCoordinate, int yCoordinate)
	{
		setBounds(xCoordinate, yCoordinate, Texture.objectWidth,Texture.objectHeight);
	}

		
	// Manage pacman collisions with food
	private void foodCollision()
	{	
		for(int i = 0; i < Level.food.size(); i++) 		
		{    
			// Check for collision with food
			if(this.intersects(Level.food.get(i)))							
			{
				// Eat the food
				Food.eat(i);
				
				break;
			}
		}
	}
	
	// Manage pacman collisions with energizers
	private void energizerCollision()
	{			
		for(int i = 0; i < Level.energizers.size(); i++) 	
		{		
			// Check for collision with energizer
			if(this.intersects(Level.energizers.get(i)))						
			{	
				// Activate the energizer
				Energizer.activate(i);							
				
				break;
			}
		}
	}
	
	
	// Turn all ghosts vulnerable
	public static void makeGhostsVulnerable()
	{
		for(int i = 0; i < Game.ghostArray.length; i++)
		{
			Game.ghostArray[i].isVulnerable = true;
		}
		
		nEatenGhosts = 0;
	}
	
	// Turn all ghosts vulnerable
	public static void makeGhostsNormal()
	{
		for(int i = 0; i < Game.ghostArray.length; i++)
		{
			Game.ghostArray[i].isVulnerable = false;
		}
	}

	// Check if pacman and a ghost have intersected
	public boolean intersectedWithGhost()
	{
		// Iterate through the ghost array
		for(int i = 0; i < Game.ghostArray.length; i++)
		{
			// Check for intersection between ghost and pacman
			if(Game.ghostArray[i].intersects(this))
			{	
				// Identify the intersected ghost
				intersectedGhost = i;
				
				return true;
			}
		}
		
		return false;
	}

	// Make pacman eat intersected ghost
	private void eatGhost()
	{
		// Ghost eaten sound effect
		new Sounds(Sounds.ghostEatenSoundPath);
		
		// Respawn eaten ghost
		Game.ghostArray[intersectedGhost] = new Ghost(intersectedGhost, Ghost.randomMovement, Ghost.notCrossingPortal, false, false);
		
		// Capture the event coordinates
		Game.xEvent = x; 
		Game.yEvent = y;
		
		// Display bonus score 
		BonusScore.display = true;
		
		// Increment the number of eaten ghosts
		nEatenGhosts++;
		
		// Add the bonus score to the game score
		Game.score += BonusScore.bonusScore;
	}
	
	// Manage pacman death
	private void die()
	{
		// Pacman death sound effect
		new Sounds(Sounds.pacmanDeathSoundPath);
		
		// Decrement amount of lives
		Game.lives--;

		Game.gameStatus = Game.lifeLost;				
	}
	
	// Manage pacman collisions with ghosts
	public void ghostCollision()
	{
		// Check for collision between pacman and ghost
		if(intersectedWithGhost() == true)
		{
			// Check if energizer is active
			if(Energizer.isActive == true)
			{
				// Check if ghost hasn't been eaten yet
				if(Game.ghostArray[intersectedGhost].isVulnerable == true)
				{
					// Eat the ghost
					eatGhost();
					
					return;
				}
			}
			
			if(Game.gameStatus == Game.play)
			{
				die();
			}
		}
	}

	
	// Manage pacman crossing map portals
	public void portalCrossing()
	{
		// Pacman going through the left portal
		if(Portal.isAboutToCrossLeftPortal(this))	
		{
			// Spawn pacman on the right side of the map
			Game.pacman = new Pacman(crossingLeftPortal);
		}
		
		// Pacman going through the right portal
		if(Portal.isAboutToCrossRightPortal(this))			
		{
			// Spawn pacman on the left side of the map
			Game.pacman = new Pacman(crossingRightPortal);		
		}
	}
	
	
	// Manage pacman animation timing
	public void eatingAnimation()
	{
		// Increase current animation phase time
		eatingAnimationTime++;
		
		// Check if time for animation phase is complete
		if(eatingAnimationTime == eatingAnimationTargetTime)
		{
			// Reset timer for animation phase
			eatingAnimationTime = 0;
			
			// Move to the next animation phase
			Texture.pacmanEatingAnimationPhase++;
		}
	}
	
	public void deathAnimation()
	{
		// Increase current animation phase time
		deathAnimationTime++;
		
		// Check if time for animation phase is complete
		if(deathAnimationTime == deathAnimationTargetTime)
		{
			// Reset timer for animation phase
			deathAnimationTime = 0;
			
			// Move to the next animation phase
			Texture.pacmanDeathAnimationPhase++;
		}
	}
	
	// Render object
	public void render(Graphics g)
	{
		if(Game.gameStatus == Game.lifeLost)
		{
			nextDir = stopped;
			
			// Check if death animation is in the last phase
			if(Texture.pacmanDeathAnimationPhase == 21)
			{
				deathAnimationDisplayed = true;
				Texture.pacmanDeathAnimationPhase = 0;
				Game.loadGameElements();
				
				if(Game.lives == 0)
				{
					Game.gameStatus = Game.lose;
				}
				else
				{	
					Game.gameStatus = Game.play;
				}
			}
			
			if(!deathAnimationDisplayed)
			{
				g.drawImage(Texture.pacmanDie[Texture.pacmanDeathAnimationPhase], x, y, width, height, null);
			}
		}
		else
		{
			// Check if eating animation is in the last phase
			if(Texture.pacmanEatingAnimationPhase == 3)
			{
				// Restart animation
				Texture.pacmanEatingAnimationPhase = 0;
			}
			
			g.drawImage(Texture.pacmanLook[currentDir][Texture.pacmanEatingAnimationPhase], x, y, width, height, null);
		}
	}

	// Tick function
	public void tick()
	{	
		Movement.movePacmanInGivenDirection(this, nextDir);
		portalCrossing();
		foodCollision();
		energizerCollision();
		ghostCollision();
		
		if(Game.gameStatus == Game.lifeLost)
		{
			deathAnimation();
		}
		else
		{
			eatingAnimation();
		}
	}
}