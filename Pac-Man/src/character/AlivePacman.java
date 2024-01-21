package character;

import java.awt.Graphics;

import food.Energizer;
import food.Food;
import food.Pellet;
import Game.Animation;
import main.GamePanel;
import main.Sounds;

public class AlivePacman extends Pacman
{	
	GamePanel gp;
	
	private static final long serialVersionUID = 1L;

	private int totalNumberOfFrames = Animation.alivePacmanSprites[currentDir].length;

	public AlivePacman(int cD, int nD, int x, int y, GamePanel gp)
	{
		super(gp);
		this.gp = gp;
		this.x = x;
		this.y = y;
		currentDir = cD;
		nextDir = nD;
		setBounds(x, y, gp.tileSize, gp.tileSize);
	}
	
	public void tick()
	{	
		if(canMove(this, nextDir))
		{
			currentDir = nextDir;
		}
		
		move(this, nextDir);
		portalEvents(this);
		manageAnimationTiming();
		foodCollision();
		ghostCollision();
	}

	public void manageAnimationTiming()
	{
		elapsedFrameTimeInSeconds += gp.secondsPerTick;
		
		if(elapsedFrameTimeInSeconds >= targetTimePerFrameInSeconds)
		{
			elapsedFrameTimeInSeconds = 0;
			
			frameIndex++;
			
			if(frameIndex >= totalNumberOfFrames)
			{
				frameIndex = 0;
			}
		}
	}
	
	private void foodCollision()
	{	
		for(int i = 0; i < gp.foodList.size(); i++) 		
		{    
			if(this.intersects(gp.foodList.get(i)))							
			{
				eat(gp.foodList.get(i));
				break;
			}
		}
	}	

	public void eat(Food food)
	{	
		if(food instanceof Energizer)
		{
			gp.activate();
		}
		else if(food instanceof Pellet)
		{
			Pellet.getEaten();
		}

		gp.score += food.getFoodPoints();
		gp.foodList.remove(food);
	}
	
	private void ghostCollision()
	{
		if(!pacmanIntersectedGhost())
		{
			return;
		}
		
		if(gp.ghostArray[intersectedGhost].isVulnerable)
		{
			eatGhost();
		}
		else 
		{
			die();
		}
	}
	
	public boolean pacmanIntersectedGhost()
	{
		for(int i = 0; i < gp.ghostArray.length; i++)
		{
			if(gp.ghostArray[i].intersects(this))
			{	
				intersectedGhost = i;
				
				return true;
			}
		}
		return false;
	}
	
	private void eatGhost()
	{
		Sounds.playSoundEffect(Sounds.ghostEatenSoundPath);
		
		gp.ghostArray[intersectedGhost] = new Ghost(intersectedGhost, Ghost.randomMovement, notCrossingPortal, false, gp);
				
		gp.numberOfEatenGhosts++;
		
		if(gp.numberOfEatenGhosts == gp.ghostArray.length)
		{
			gp.deactivate();
		}
		
		gp.bonusScore.displayBonusScore(x, y);
		gp.bonusScore.sumBonusScoreToGameScore();
	}
	
	private void die()
	{
		Sounds.playSoundEffect(Sounds.pacmanDeathSoundPath);
		
		gp.pacman = new DeadPacman(x, y, gp);
		gp.gameState = gp.lifeLostState;
	}
	
	public void render(Graphics g)
	{
		g.drawImage(Animation.alivePacmanSprites[currentDir][frameIndex], x, y, width, height, null);
	}
}
