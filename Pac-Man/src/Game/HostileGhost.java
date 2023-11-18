package Game;

import java.awt.Graphics; // remove when ghost abstract

public class HostileGhost extends Ghost
{
	private static final long serialVersionUID = 1L;

	private int ghostID;
	
	public HostileGhost(int ID)
	{
		ghostID = ID;
		spawnGhost(spawnBoxX, spawnBoxY);
	}
	
	void tick()
	{
		manageHostileGhostAnimationTiming();
	}
	
	void render(Graphics g)
	{
		g.drawImage(Texture.ghostLook[ghostID][currentDir][frameIndexHostile], x, y, width, height, null);
	}
}
