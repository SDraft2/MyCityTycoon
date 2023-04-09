import java.awt.Image;
import java.awt.Toolkit;

public class Main
{
	static boolean loadError = false;
	
	public static void main(String[] args)
	{

		
		StartUI start = new StartUI();
			
		while(!start.turnOff)
		{
			try
			{
				Thread.sleep(10);
			}
			catch(InterruptedException e)
			{
					
			}
		}
		
		ClickerGame game = new ClickerGame();
		
	}
	
}
