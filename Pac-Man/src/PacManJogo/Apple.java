package PacManJogo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Apple extends Rectangle
{
	private static final long serialVersionUID = 1L;

	public Apple(int x, int y)
	{
		setBounds(x+12,y+12,8,8);			
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.yellow);				
		g.fillRect(x,y,width,height);	
	}
}
