/**************************************************************
* Created by: Lucas Tucker (tucker.lucas.1404@gmail.com)
* 
* File: Ghost.java
* 
* Description: 
* 
* This file contains the implementation for ghost behaviour 
* regarding movement within the game map as well as animations.
* 
/**************************************************************/

package Game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Ghost extends Rectangle
{
	private static final long serialVersionUID = 1L;
	
	private int spd = 2;
	
	private Random randomGen;
	
	// Type of movement variables
	private int movementType;
	
	private final int random 		= 0;
	private final int smart  		= 1;
	private final int find_path 	= 2;
	
	public static int flashTime  = 60*5;
	
	public static final int right = 0; 
	public static final int left  = 1;
	public static final int up 	  = 2;
	public static final int down  = 3;
	
	private int dir 	= -1;
	private int lastDir = -1;
	
	private int smartTime			= 0;
	private int smartTargetTime; 
	private boolean coolDown		= false;
	private int coolDownTime    	= 0;
	private int coolDownTargetTime	= 60*3; 
	private int timeImage 			= 0;		
	private int targetTimeImage 	= 4;
	
	private boolean zone1 	= false;
	private boolean zone2 	= false;
	private boolean zone3 	= false;
	private boolean zone4 	= false;
	private boolean zone5 	= false;
	private boolean zone6 	= false;
	private boolean zone7 	= false;
	private boolean zone8 	= false;
	private boolean zone9 	= false;
	private boolean zone10 	= false;
	private boolean zone11 	= false;
	private boolean zone12 	= false;
	private boolean zone13 	= false;
	private boolean zone14 	= false;
	private boolean zone15 	= false;
	private boolean zone16 	= false;
	
	private boolean left4 	= false;
	private boolean right4 	= false;
	private boolean left13 	= false;
	private boolean right13 = false;
	private boolean up8 	= false;
	private boolean down8 	= false;
	private boolean up9 	= false;
	private boolean down9 	= false;
	
	private int difx;
	private int dify;
	private int radius;
	
	private int spawn;
	
	// Spawn variables
	public static final int spawnInBox = 0;
	private static final int spawnRight = 1;
	private static final int spawnLeft 	= 2;
	
	private int imageIndexEnemy = 0;
	
	public boolean isVulnerable = false;
	public static boolean isFlashing = false;
	
	public int enemyID;
	
	// Constructor
	public Ghost(int ID, int spawnPoint, boolean IV)
	{	
		// Set default ghost movement type
		movementType = random;
		
		spawn 			= spawnPoint;
		enemyID  		= ID;
		isVulnerable 	= IV;
		
		switch(spawn)
		{
			case spawnInBox:
				setBounds(320,320,32,32);
				break;
			case spawnLeft:
				setBounds(640,320,32,32);
				move(left);
				break;
			case spawnRight:
				setBounds(0,320,32,32);
				move(right);
				break;
		}

		switch(Game.difficulty)
		{
			case 1:
				radius = 80;
				smartTargetTime = 60 * 5;
				break;
			case 2:
				radius = 100;
				smartTargetTime = 60 * 8;
				break;
			case 3:
				radius = 120;
				smartTargetTime = 60 * 12;
				break;
			case 4:
				radius = 150;
				smartTargetTime = 60 * 20;
				break;
		}
		
		randomGen = new Random();
		dir = randomGen.nextInt(4);
	}
	
	private void updateZone(int currentZone)
	{
		resetZones();
		
		switch(currentZone)
		{
			case 1:  zone1  = true; break;
			case 2:  zone2  = true; break;
			case 3:  zone3  = true; break;
			case 4:  zone4  = true; break;
			case 5:  zone5  = true; break;
			case 6:  zone6  = true; break;
			case 7:  zone7  = true; break;
			case 8:  zone8  = true; break;
			case 9:  zone9  = true; break;
			case 10: zone10 = true; break;
			case 11: zone11 = true; break;
			case 12: zone12 = true; break;
			case 13: zone13 = true; break;
			case 14: zone14 = true; break;
			case 15: zone15 = true; break;
			case 16: zone16 = true; break;
		}
	}
	
	private void resetZones()
	{
		zone1 	= false;
		zone2 	= false;
		zone3 	= false;
		zone4 	= false;
		zone5 	= false;
		zone6 	= false;
		zone7 	= false;
		zone8 	= false;
		zone9 	= false;
		zone10	= false;
		zone11 	= false;
		zone12 	= false;
		zone13 	= false;
		zone14 	= false;
		zone15 	= false;
		zone16 	= false;
	}

	private void getToZone(int dir1, int dir2)
	{
		if(canMove(dir1))
		{
			move(dir1);
		}
		else if(canMove(dir2))
		{
			move(dir2);
		}
		else
		{
			movementType = find_path;
		}
	}
	
	
	private void moveToZone(int zone)
	{
		updateZone(zone);
		
		switch(zone)
		{
			case 1: getToZone(left, up);
					break;
					
			case 2: getToZone(up, left);
					break;
				
			case 3: getToZone(up, left);
					break;
					
			case 4: 
				if(canMove(up))
				{
					move(up);
				}
				else if(canMove(left))
				{
					left4 = true;
					movementType = find_path;
				}
				else if(canMove(right))
				{
					right4 = true;
					movementType = find_path;
				}
				break;
				
			case 5: getToZone(up, right);
					break;

			case 6: getToZone(up, right);
					break;
					
			case 7: getToZone(right, up);
					break;
					
			case 8: 
				if(canMove(left))
				{
					move(left);
				}
				else if(canMove(up))
				{
					up8 = true;
					movementType = find_path;
				}
				else if(canMove(down))
				{
					down8 = true;
					movementType = find_path;
				}
				break;
				
			case 9: 
				if(canMove(right))
				{
					move(right);
				}
				else if(canMove(up))
				{
					up9 = true;
					movementType = find_path;
				}
				else if(canMove(down))
				{
					down9 = true;
					movementType = find_path;
				}
				break;
				
			case 10: getToZone(left, down);
					 break;

			case 11: getToZone(down, left);
					 break;
					 
			case 12: getToZone(down, left);
					 break;
					 
			case 13: 
				if(canMove(down))
				{
					move(down);
				}
				else if(canMove(left))
				{
					left13 = true;
					movementType = find_path;
				}
				else if(canMove(right))
				{
					right13 = true;
					movementType = find_path;
				}
				break;
				
			case 14: getToZone(down, right);
					 break;
					
			case 15: getToZone(down, right);
					 break;
					 
			case 16: getToZone(right, down);
					 break;
		}
	}

	private boolean randomMovement(int direction)
	{
		if(canMove(direction))			
		{
			move(direction);
			dir = randomGen.nextInt(4);
			return true;
		}
		
		return false;
	}
	
	private void moveRandomly()
	{
		difx = x - Game.pacman.x;
		dify = y - Game.pacman.y;
		
		if(coolDown == true)
		{
			coolDownTime++;
			
			if(coolDownTime == coolDownTargetTime)
			{
				coolDown = false;
				coolDownTime = 0;
			}
		}
		else if(coolDown == false)
		{
			if((difx < radius && difx > -radius) && (dify < radius && dify > -radius))
			{
				if(isVulnerable == false && !inBox())
				{
					movementType = smart;
				}
			}
		}
		if(spawn == spawnLeft)
		{
			lastDir = left;
			
			x -= spd;
			
			if(x == 480 && y == 320)
			{
				spawn = spawnInBox;
			}
		}
		else if(spawn == spawnRight)
		{
			lastDir = right;
			
			x += spd;
			
			if(x == 160 && y == 320)
			{
				spawn = spawnInBox;
			}
		}
		else if(spawn == spawnInBox)
		{
			switch(dir)
			{
				case right:
					
					if(randomMovement(right))
						break;
					else							
					{
						if(lastDir == left && canMove(left))
						{
							move(left);
						}
						else if(lastDir == up && canMove(up))
						{
							move(up);
						}
						else if(lastDir == down && canMove(down))
						{
							move(down);
						}
						else 
						{
							dir = randomGen.nextInt(4); 
						}
						
					}
					
					break;
					
				case left:
					
					if(randomMovement(left))
						break;
					else							
					{
						if(lastDir == right && canMove(right))
						{
							x+=spd;
						}
						else if(lastDir == up && canMove(up))
						{
							y-=spd;
						}
						else if(lastDir == down && canMove(down))
						{
							y+=spd;
						}
						else 
						{
							dir = randomGen.nextInt(4); 
						}
					}
								
					break;
					
				case up:
					
					if(randomMovement(up))
					{
						break;
					}
					else							
					{
						if(lastDir == left && canMove(left))
						{
							x-=spd;
						}
						else if(lastDir == right && canMove(right))
						{
							x+=spd;
						}
						else if(lastDir == down && canMove(down))
						{
							y+=spd;
						}
						else 
						{
							dir = randomGen.nextInt(4);
						}
					}
					
					break;
					
				case down:
					
					if(randomMovement(down))
						break;
					else							
					{
						if(lastDir == left && canMove(left))
						{
							x-=spd;
						}
						else if(lastDir == up && canMove(up))
						{
							y-=spd;
						}
						else if(lastDir == right && canMove(right))
						{
							x+=spd;
						}
						else 
						{
							dir = randomGen.nextInt(4); 
						}
					}	
					
					break;
			}
		}
	}
	
	// Check if ghost is in a portal
	private boolean inPortal()
	{
		return ((x < 160 || x > 480) && y == 320) ? true : false;
	}
	
	
	private void moveSmartly()
	{
		difx = x - Game.pacman.x;
		dify = y - Game.pacman.y;

		if(Energizer.isActive == true || inBox())
		{
			movementType = random;
		}
		
		if(spawn == spawnLeft)
		{
			lastDir = left;
			x-=spd;
			
			if(x == 480 && y == 320)
			{
				spawn = spawnInBox;
			}
		}
		else if(spawn == spawnRight)
		{
			lastDir = right;
			x+=spd;
			
			if(x == 160 && y == 320)
			{
				spawn = spawnInBox;
			}
		}
		else if(spawn == spawnInBox)
		{
			// Ghost in right portal moving right
			if(inPortal() && lastDir == right)
			{
				// Ensure ghost crosses portal to the other side of the map
				spawn = spawnRight;
			}
			// Ghost in left portal moving left
			else if(inPortal() && lastDir == left)
			{
				// Ensure ghost crosses portal to the other side of the map
				spawn = spawnLeft;
			}
			// Ghost not in a portal
			else
			{
				if(difx > 0 && dify > 0 && difx > dify)			//Zona 1
				{
					moveToZone(1);
				}
				else if(difx > 0 && dify > 0 && difx == dify)	//Zona 2				
				{
					moveToZone(2);
				}
				else if(difx > 0 && dify > 0 && dify > difx)	//Zona 3					
				{
					moveToZone(3);
				}
				else if(difx == 0 && dify > 0)					//Zona 4				
				{
					moveToZone(4);
				}
				else if(difx < 0 && dify > 0 && dify > -difx)	//Zona 5					
				{
					moveToZone(5);
				}
				else if(difx < 0 && dify > 0 && -difx == dify)	//Zona 6					
				{
					moveToZone(6);
				}
				else if(difx < 0 && dify > 0 && -difx > dify)	//Zona 7					
				{
					moveToZone(7);
				}
				else if(difx > 0 && dify == 0)					//Zona 8					
				{
					moveToZone(8);
				}
				else if(difx < 0 && dify == 0)					//Zona 9					
				{
					moveToZone(9);
				}
				else if(difx > 0 && dify < 0 && difx > -dify)	//Zona 10 					
				{
					moveToZone(10);
				}
				else if(difx > 0 && dify < 0 && difx == -dify)	//Zona 11 					
				{
					moveToZone(11);
				}
				else if(difx > 0 && dify < 0 && -dify > difx)	//Zona 12					
				{
					moveToZone(12);
				}
				else if(difx == 0 && dify < 0)					//Zona 13					
				{
					moveToZone(13);
				}
				else if(difx < 0 && dify < 0 && -dify > -difx)	//Zona 14					
				{
					moveToZone(14);
				}
				else if(difx < 0 && dify < 0 && -dify == -difx)	//Zona 15					
				{
					moveToZone(15);
				}
				else if(difx < 0 && dify < 0 && -difx > -dify)	//Zona 16					
				{
					moveToZone(16);
				}
			}
		}
		smartTime++;								
		
		if(smartTime == smartTargetTime) 				
		{
			coolDown = true;
			movementType = random;							
			smartTime = 0;						
		}
	}

	private void findingPath()
	{
		if(zone1)
		{
			moveUntil(down, left);
		}
		else if(zone2)
		{
			moveUntil(right, up);
		}
		else if(zone3)
		{
			moveUntil(right, up);
		}
		else if(zone4)
		{
			if(left4 == true)
			{
				if(canMove(left))
				{
					moveUntil(left, up);
				}
				else
				{
					left4 = false;
					zone4 = false;
					moveUntil(-1, -1);
				}
			}
			else if(right4 == true)
			{
				if(canMove(right))
				{
					moveUntil(right, up);
				}
				else
				{
					right4 = false;
					zone4 = false;
					moveUntil(-1, -1);
				}
			}
		}
		else if(zone5)
		{
			moveUntil(left, up);
		}
		else if(zone6)
		{
			moveUntil(left, up);
		}
		else if(zone7)
		{
			moveUntil(down, right);
		}
		else if(zone8)
		{
			if(up8 == true)
			{
				if(canMove(up))
				{
					moveUntil(up, left);
				}
				else
				{
					up8 = false;
					zone8 = false;
					moveUntil(-1,-1);
				}
			}
			else if(down8 == true)
			{
				if(canMove(down))
				{
					moveUntil(down, left);
				}
				else
				{
					down8 = false;
					zone8 = false;
					moveUntil(-1,-1);
				}
			}
		}
		else if(zone9)
		{
			if(up9 == true)
			{
				if(canMove(up))
				{
					moveUntil(up, right);
				}
				else
				{
					up9 = false;
					zone9 = false;
					moveUntil(-1,-1);
				}
			}
			else if(down9 == true)
			{
				if(canMove(down))
				{
					moveUntil(down, right);
				}
				else
				{
					down9 = false;
					zone9 = false;
					moveUntil(-1,-1);
				}
			}
		}
		else if(zone10)
		{
			moveUntil(up, left);
		}
		else if(zone11)
		{
			moveUntil(right, down);
		}
		else if(zone12)
		{
			moveUntil(right, down);
		}
		else if(zone13)
		{
			if(left13 == true)
			{
				if(canMove(left))
				{
					moveUntil(left, down);
				}
				else
				{
					zone13 = false;
					left13 = false;
					moveUntil(-1,-1);
				}
			}
			else if(right13 == true)
			{
				if(canMove(right))
				{
					moveUntil(right, down);
				}
				else
				{
					zone13 = false;
					right13 = false;
					moveUntil(-1,-1);
				}
			}
		}
		else if(zone14)
		{
			moveUntil(left, down);
		}
		else if(zone15)
		{
			moveUntil(left, down);
		}
		else if(zone16)
		{
			moveUntil(up, right);
		}
	}

	private void moveUntil(int allowed_direction, int desired_direction)
	{
		switch(allowed_direction)
		{
			case right:
				
				move(right);
				
				if(desired_direction == up && canMove(up))
				{
					right4 = false;
					movementType = smart;
				}
				else if(desired_direction == down && canMove(down))
				{
					right13 = false;
					movementType = smart;
				}
				
				break;
			
			case left: 
				
				move(left);
				
				if(desired_direction == up && canMove(up))
				{
					left4 = false;
					movementType = smart;
				}
				else if(desired_direction == down && canMove(down))
				{
					left13 = false;
					movementType = smart;
				}
				
				break;
				
			case up:
				
				move(up);
				
				if(desired_direction == right && canMove(right))
				{
					up9 = false;
					movementType = smart;
				}
				else if(desired_direction == left && canMove(left))
				{
					up8 = false;
					movementType = smart;
				}
				
				break;
				
			case down:
				
				move(down);
				
				if(desired_direction == right && canMove(right))
				{
					down9 = false;
					movementType = smart;
				}
				else if(desired_direction == left && canMove(left))
				{
					down8 = false;
					movementType = smart;
				}
				
				break;
				
			case -1:	
				
				movementType = smart; 
			
				break;
		}
	}
	
	// Check if ghost is in spawn box
	private boolean inBox()
	{
		return ((x < 385 && x > 255) && (y < 385 && y > 255)) ? true : false;
	}

	// Manage ghost movement
	private void ghostMovement()
	{
		switch(movementType)
		{
			case random: 	moveRandomly(); break;	// Move in a random fashion
			case smart: 	moveSmartly();  break;	// Chase pacman
			case find_path: findingPath();  break;	// Find path to pacman when stuck
		}			
	}
	
	private void portalCrossing()
	{
		if(x == 0 && y == 320)								
		{
			lastDir = left;
			spawn = spawnLeft;
			
			switch(enemyID)
			{
				case 0: Game.ghostArray[0] = new Ghost(0, spawn, isVulnerable); break;
				case 1: Game.ghostArray[1] = new Ghost(1, spawn, isVulnerable); break;
				case 2: Game.ghostArray[2] = new Ghost(2, spawn, isVulnerable); break;
				case 3: Game.ghostArray[3] = new Ghost(3, spawn, isVulnerable); break;
			}
		}
		else if(x == 640 && y == 320)
		{
			lastDir = right;
			spawn = spawnRight;

			switch(enemyID)
			{
				case 0: Game.ghostArray[0] = new Ghost(0, spawn, isVulnerable); break;
				case 1: Game.ghostArray[1] = new Ghost(1, spawn, isVulnerable); break;
				case 2: Game.ghostArray[2] = new Ghost(2, spawn, isVulnerable); break;
				case 3: Game.ghostArray[3] = new Ghost(3, spawn, isVulnerable); break;
			}
		}
	}
	
	private void animation()
	{
		timeImage ++;
		
		if(timeImage == targetTimeImage)
		{
			timeImage = 0;
			imageIndexEnemy ++;
		}
		
		Game.flashAnimationTime++;
		
		if(Game.flashAnimationTime == Game.flashAnimationTargetTime)
		{
			Game.flashAnimationTime = 0;
			
			if(Texture.animationPhaseFlash == 3)
			{
				Texture.animationPhaseFlash = 0;
			}
			else
			{
				Texture.animationPhaseFlash++;
			}
		}
	}
	
	
	private void flashGhost(Graphics g)
	{
		g.drawImage(Texture.flashGhost[Texture.animationPhaseFlash], x, y, width, height, null);
	}
	
	private void stayBlue(Graphics g)
	{
		g.drawImage(Texture.blueGhost[imageIndexEnemy], x, y, width, height, null);
	}
	
	private void look(int where, Graphics g)
	{
		switch(where)
		{
			case right: 
				
				switch(this.enemyID)
				{
					case 0: g.drawImage(Texture.blinkyLookRight[imageIndexEnemy], x, y, width, height, null); break;
					case 1: g.drawImage(Texture.inkyLookRight[imageIndexEnemy], x, y, width, height, null);   break;
					case 2: g.drawImage(Texture.pinkyLookRight[imageIndexEnemy], x, y, width, height, null);  break;
					case 3: g.drawImage(Texture.clydeLookRight[imageIndexEnemy], x, y, width, height, null);  break;
				}
				
				break;
				
			case left:
				
				switch(enemyID)
				{
					case 0: g.drawImage(Texture.blinkyLookLeft[imageIndexEnemy], x, y, width, height, null); break;
					case 1: g.drawImage(Texture.inkyLookLeft[imageIndexEnemy], x, y, width, height, null);   break;
					case 2: g.drawImage(Texture.pinkyLookLeft[imageIndexEnemy], x, y, width, height, null);  break;
					case 3: g.drawImage(Texture.clydeLookLeft[imageIndexEnemy], x, y, width, height, null);  break;
				}
				
				break;
				
			case up:
				
				switch(enemyID)
				{
					case 0: g.drawImage(Texture.blinkyLookUp[imageIndexEnemy], x, y, width, height, null); break;
					case 1: g.drawImage(Texture.inkyLookUp[imageIndexEnemy], x, y, width, height, null);   break;
					case 2: g.drawImage(Texture.pinkyLookUp[imageIndexEnemy], x, y, width, height, null);  break;
					case 3: g.drawImage(Texture.clydeLookUp[imageIndexEnemy], x, y, width, height, null);  break;
				}
				
				break;
				
			case down:
				
				switch(enemyID)
				{
					case 0:	g.drawImage(Texture.blinkyLookDown[imageIndexEnemy], x, y, width, height, null); break;
					case 1: g.drawImage(Texture.inkyLookDown[imageIndexEnemy], x, y, width, height, null);   break;
					case 2: g.drawImage(Texture.pinkyLookDown[imageIndexEnemy], x, y, width, height, null);  break;
					case 3: g.drawImage(Texture.clydeLookDown[imageIndexEnemy], x, y, width, height, null);  break;
				}
				
				break;
		}
	}

	public void render(Graphics g)
	{
		if(imageIndexEnemy == 2)
		{
			imageIndexEnemy = 0;
		}

		if(isVulnerable == false)
		{
			look(lastDir, g);
		}
		else
		{
			if(isFlashing)
			{
				flashGhost(g);
			}
			else if(!isFlashing)
			{
				stayBlue(g);
			}
		}
	}
	
	public void tick()
	{			
		ghostMovement();
		portalCrossing();
		animation();
	}
	
	private void setDirection(int dir)
	{
		switch(dir)
		{
			case right: x+=spd; lastDir = right; break;
			case left: 	x-=spd; lastDir = left; break;
			case up: 	y-=spd; lastDir = up; break;
			case down: 	y+=spd; lastDir = down; break;
		}
	}
	
	private void move(int dir)
	{
		if(canMove(dir))
		{
			setDirection(dir);
		}
	}
	
	private boolean canMove(int dir)
	{
		int nextx = 0, nexty = 0;
		
		switch(dir)
		{
			case right:	nextx = x+spd; nexty = y; break;
			case left:	nextx = x-spd; nexty = y; break;
			case up:	nextx = x; nexty = y-spd; break;
			case down:	if(x == 320 && y == 256) {return false;}			// Prevent ghosts from reentering spawn box
						nextx = x; nexty = y+spd; break;
		}
		
		Rectangle bounds = new Rectangle(nextx, nexty, width, height);
		
		for(int xx = 0; xx < Level.tiles.length; xx++)
		{
			for(int yy = 0; yy < Level.tiles[0].length; yy++)
			{
				// If not null, we have intercepted a map wall
				if(Level.tiles[xx][yy] != null)								
				{
					// Intercepted a map wall. Can no longer move in this direction
					if(bounds.intersects(Level.tiles[xx][yy]))				
					{
						return false;								
					}
				}
			}
		}	
		return true;
	}
}
 