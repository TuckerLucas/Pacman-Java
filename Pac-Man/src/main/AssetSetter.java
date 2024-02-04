package main;

import entity.Character;
import entity.Ghost_Hostile;
import entity.Pacman_Alive;
import entity.SpawnBoxDoor;

public class AssetSetter 
{
	GamePanel gp;
	
	public AssetSetter(GamePanel gp)
	{
		this.gp = gp;
	}
	
	public void setGhosts()
	{
		int i = 0;
		
		gp.ghostArray[i] = new Ghost_Hostile(gp, i); 
		gp.ghostArray[i].currentDir = Character.right;
		gp.ghostArray[i].x = 320;
		gp.ghostArray[i].y = 256;
		gp.ghostArray[i].movementType = "random";
		
		i++;
		
		gp.ghostArray[i] = new Ghost_Hostile(gp, i); 
		gp.ghostArray[i].currentDir = Character.right;
		gp.ghostArray[i].x = 288;
		gp.ghostArray[i].y = 320;
		gp.ghostArray[i].movementType = "random";
		
		i++;
		
		gp.ghostArray[i] = new Ghost_Hostile(gp, i); 
		gp.ghostArray[i].currentDir = Character.upwards;
		gp.ghostArray[i].x = 320;
		gp.ghostArray[i].y = 320;
		gp.ghostArray[i].movementType = "random";
		
		i++;
		
		gp.ghostArray[i] = new Ghost_Hostile(gp, i); 
		gp.ghostArray[i].currentDir = Character.left;
		gp.ghostArray[i].x = 352;
		gp.ghostArray[i].y = 320;
		gp.ghostArray[i].movementType = "random";
	}
	
	public void setPacman()
	{
		gp.pacman = new Pacman_Alive(gp);
		gp.pacman.x = 320;
		gp.pacman.y = 512;
		gp.pacman.currentDir = Character.right;
		gp.pacman.nextDir = Character.stopped;
	}
	
	public void setDoor()
	{
		gp.spawnBoxDoor = new SpawnBoxDoor(gp);
		gp.spawnBoxDoor.x = 320;
		gp.spawnBoxDoor.y = 288;
	}
}
