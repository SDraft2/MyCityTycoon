import javax.swing.*;

public class EffThread extends Thread
{
	int x;
	int y;
	int timer;
	JLabel label;
	LayPan Screen;
	
	EffThread(JLabel label)
	{
		this.label = label;
	}
	
	EffThread(int x, int y, int timer, JLabel label, LayPan Screen)
	{
		this.x = x;
		this.y = y;
		this.timer = timer;
		this.label = label;
		this.Screen = Screen;
	}
	
	public void run()
	{
		for(int i=0 ; i<=timer ; i++)
		{
			try
			{
				label.setBounds(x, y-i*(int)(Screen.getHeight()*0.003), (int)(Screen.getWidth()*0.4), (int)(Screen.getHeight()*0.4));
				Thread.sleep(10);
				if(i==49)
				{
					label.setText("");
				}
			}
			
			catch (InterruptedException e){
				e.printStackTrace();
			}
		}
			Screen.remove(label);
	}
	
	public void setXY(int x, int y, JLabel eff)
	{
		x++;
		y++;
		eff.setBounds(x, y, 100, 100);
	}
}
