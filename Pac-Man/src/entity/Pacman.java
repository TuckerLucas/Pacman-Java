package entity;

import java.awt.Graphics;

import main.GamePanel;

public class Pacman extends Character
{
	private static final long serialVersionUID = 1L;
	
	public String currentDir;
	public String nextDir;
	
	public Pacman(GamePanel gp)
	{
		super(gp);
		
		frameIndex = 0;
		elapsedFrameTimeInSeconds = 0.0;	
		targetTimePerFrameInSeconds = 0.1;
	}
	
	public String getCurrentDirection()
	{
		return currentDir;
	}
	
	public void tick() 
	{
		
	}
	
	public void render(Graphics g) 
	{
		
	}
}