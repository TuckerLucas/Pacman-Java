package Game;

import java.awt.Graphics;
import java.util.Random;

public class Ghost extends Character
{
	private static final long serialVersionUID = 1L;
	
	protected Random randomGen;
	
	public int ghostID;
	protected int portalCrossingStatus;
	protected int movementType;
	public boolean isVulnerable = false;
	
	public static final int randomMovement = 0;
	public static final int methodicalMovement = 1;
	public static final int findingPath = 2;
	
	private boolean findDir1Blocked = false;
	private boolean isFindingPath = false;
	
	private int nextDir = 0;
	public int currentDir = 0;
	
	private double timeMovingMethodicallyInSeconds = 0.0;
	private double targetTimeMovingMethodicallyInSeconds = 12.0; 
	private boolean isCoolingDown = false;
	private double coolDownTimeInSeconds = 0.0;
	private double coolDownTargetTimeInSeconds = 5.0; 
	
	public static int numberOfEatenGhosts = 0;
	
	public static int spawnBoxX = 320;
	public static int spawnBoxY = 320;
	
	public static Ghost ghostArray[] = new Ghost[4];
	
	private int frameIndex = 0;
	private double elapsedFrameTimeInSeconds = 0;		
	private double targetTimePerFrameInSeconds = 0.05;
	private int totalNumberOfFrames = Texture.ghostLook[0][0].length;
	
	private int frameIndexVulnerable = 0;
	private double elapsedFrameTimeInSecondsVulnerable = 0;		
	private double targetTimePerFrameInSecondsVulnerable = 0.05;
	private int totalNumberOfFramesVulnerable = Texture.blueGhost.length;
	
	private int frameIndexFlashing = 0;
	private double elapsedFrameTimeInSecondsFlashing = 0;
	private double targetTimePerFrameInSecondsFlashing = 0.33;
	private int totalNumberOfFramesFlashing = Texture.flashGhost.length;
	
	public boolean isFlashing = false;
	public static double timeInstantToBeginFlashingInSeconds = 5.0;
	
	public static int methodicalDir1;
	public static int methodicalDir2;
	public static int findDir1;
	public static int findDir2;
	
	class zoneDirections
	{	
		int methodicalDir1;
		int methodicalDir2;
		int findDir1;
		int findDir2;
	}
	
	public Ghost()
	{
		
	}
	
	public Ghost(int ID, int movementStatus, int portalStatus, boolean vulnerabilityStatus)
	{		
		ghostID = ID;                  			
		movementType = movementStatus; 			
		isVulnerable = vulnerabilityStatus; 	
		portalCrossingStatus = portalStatus;	
		
		switch(portalCrossingStatus)
		{
			case Character.notCrossingPortal:
				spawnGhost(spawnBoxX, spawnBoxY);		
				break;
			case Character.crossingPortalFromLeftSide:
				spawnGhost(portalRightSideCrossingPointXCoordinate, portalYCoordinate);	
				currentDir = left;									
				break;
			case Character.crossingPortalFromRightSide:
				spawnGhost(portalLeftSideCrossingPointXCoordinate, portalYCoordinate);		
				currentDir = right;									
				break;
		}
		
		randomGen = new Random();
		generateNextDirection();
	}
	
	protected void spawnGhost(int xCoordinate, int yCoordinate)
	{
		setBounds(xCoordinate, yCoordinate, Texture.objectWidth, Texture.objectHeight);
	}
	

	public void tick()
	{		
		portalEvents(this);
		Zone.updateDistanceToPacman(x, y);
		
		if(portalCrossingStatus == notCrossingPortal)
		{
			selectGhostMovementType();
		}
		
		manageAnimationTiming();
		manageVulnerableAnimationTiming();
		manageFlashingAnimationTiming();
	}

	
	protected void selectGhostMovementType()
	{
		switch(movementType)
		{
			case randomMovement: moveRandomly(); break;
			case methodicalMovement: moveMethodically(); break;
		}			
	}
	
	
	private void manageAnimationTiming()
	{
		elapsedFrameTimeInSeconds += Game.secondsPerTick;
		
		if(elapsedFrameTimeInSeconds >= targetTimePerFrameInSeconds)
		{
			elapsedFrameTimeInSeconds = 0;
			frameIndex++;
			
			if(frameIndex == totalNumberOfFrames)
			{
				frameIndex = 0;
			}
		}
	}
	
	private void manageVulnerableAnimationTiming()
	{
		elapsedFrameTimeInSecondsVulnerable += Game.secondsPerTick;
		
		if(elapsedFrameTimeInSecondsVulnerable >= targetTimePerFrameInSecondsVulnerable)
		{
			elapsedFrameTimeInSecondsVulnerable = 0;
			frameIndexVulnerable++;
			
			if(frameIndexVulnerable == totalNumberOfFramesVulnerable)
			{
				frameIndexVulnerable = 0;
			}
		}
	}
	
	private void manageFlashingAnimationTiming()
	{
		// Needs a mechanism change to make tentacles move at same speed
		// as when ghost is in other states. Currently does not in order
		// to show blue or white color for longer which makes the speed 
		// reduced.
		elapsedFrameTimeInSecondsFlashing += Game.secondsPerTick;
		
		if(elapsedFrameTimeInSecondsFlashing >= targetTimePerFrameInSecondsFlashing)
		{
			elapsedFrameTimeInSecondsFlashing = 0;
			frameIndexFlashing++;
			
			if(frameIndexFlashing == totalNumberOfFramesFlashing)
			{
				frameIndexFlashing = 0;
			}
		}
	}
	
	
	protected void moveRandomly()
	{
		if(isCoolingDown == true)
		{
			coolDownTimeInSeconds += Game.secondsPerTick;
			
			if(coolDownTimeInSeconds >= coolDownTargetTimeInSeconds)
			{
				isCoolingDown = false;
				coolDownTimeInSeconds = 0;
			}
		}
		else if(isCoolingDown == false)
		{
			if(Zone.pacmanIsClose() && !isVulnerable && !isInSpawnBox(this))
			{
				movementType = methodicalMovement;
			}
		}
		
		if(canMove(this, nextDir))
		{
			currentDir = nextDir;
			
			move(this, nextDir);
		}
		else if(canMove(this, currentDir))
		{
			move(this, currentDir);
			
			return;
		}
		
		generateNextDirection();
	}

	protected void moveMethodically()
	{
		if(!isFindingPath)
		{
			Zone.updateMethodicalDirections(this, Zone.updatePacmanZone());
			
			if(canMove(this, methodicalDir1))
			{
				currentDir = methodicalDir1;
				
				move(this, methodicalDir1);
			}
			else if(canMove(this, methodicalDir2))
			{
				currentDir = methodicalDir2;
				
				move(this, methodicalDir2);
			}
			else
			{
				isFindingPath = true;
			}
		}
		else if(isFindingPath)
		{
			if(canMove(this, methodicalDir1))
			{
				findDir1Blocked = false;
				isFindingPath = false;
			}
			else
			{
				if(findDir1Blocked == false)
				{
					if(canMove(this, findDir1))
					{
						currentDir = findDir1;
						
						move(this, findDir1);
					}
					else
					{
						findDir1Blocked = true;
					}
				}
				else if(findDir1Blocked == true)
				{
					currentDir = findDir2;
					
					move(this, findDir2);
				}
			}
		}
		
		timeMovingMethodicallyInSeconds += Game.secondsPerTick;	
		
		if(timeMovingMethodicallyInSeconds >= targetTimeMovingMethodicallyInSeconds) 				
		{			
			timeMovingMethodicallyInSeconds = 0;				
			isCoolingDown = true;			
			movementType = randomMovement;	
		}
	}
	
	
	public static boolean isInSpawnBox(Ghost ghost)
	{
		return ((ghost.x < 368 && ghost.x > 272) && (ghost.y < 336 && ghost.y > 256)) ? true : false;
	}
	
	protected void generateNextDirection()
	{
		nextDir = randomGen.nextInt(4);
	}
	

	public static void turnAllVulnerable()
	{
		for(int i = 0; i < ghostArray.length; i++)
		{
			ghostArray[i].isVulnerable = true;
		}
		
		numberOfEatenGhosts = 0;
	}
	
	public static void turnAllHostile()
	{
		for(int i = 0; i < ghostArray.length; i++)
		{
			ghostArray[i].isVulnerable = false;
		}
	}
	
	public static void startFlashing()
	{
		for(int i = 0; i < ghostArray.length; i++)
		{
			if(ghostArray[i].isVulnerable)
			{
				ghostArray[i].isFlashing = true;
			}
		}
	}
	
	public static void stopFlashing()
	{
		for(int i = 0; i < ghostArray.length; i++)
		{
			ghostArray[i].isFlashing = false;
		}
	}

	
	public void render(Graphics g)
	{
		if(isVulnerable == false)
		{
			g.drawImage(Texture.ghostLook[ghostID][currentDir][frameIndex], x, y, width, height, null);
		}
		else if(isVulnerable == true)
		{
			if(isFlashing)
			{
				g.drawImage(Texture.flashGhost[frameIndexFlashing], x, y, width, height, null);
			}
			else if(!isFlashing)
			{
				g.drawImage(Texture.blueGhost[frameIndexVulnerable], x, y, width, height, null);
			}
		}
	}
	

	int getPortalCrossingStatus() 
	{
		return portalCrossingStatus;
	}

	int getMovementType() 
	{
		return movementType;
	}

	boolean getVulnerabilityStatus() 
	{
		return isVulnerable;
	}
	
	public int getCurrentDirection()
	{
		return currentDir;
	}
	
	public void setCurrentDirection(int dir)
	{
		currentDir = dir;
	}
	
	public void setPortalCrossingStatus(int portalStatus)
	{
		portalCrossingStatus = portalStatus;
	}
	
	public int getNextDirection()
	{
		return nextDir;
	}
	
	public int getID()
	{
		return ghostID;
	}
}