public class GroundBut extends But
{

	int[] x = new int[4];
	int[] y = new int[4];
	
	
	public boolean contains(int x, int y){

		int xPer = (int)(((double)x/(double)getWidth())*100);
		int yPer = (int)(((double)y/(double)getHeight())*100);
		
		if(x<=getWidth() && x>=0 && y<=getHeight() && y>=0)
		{
			if(yPer <= 50)
			{
				if(xPer<50+yPer && xPer>50-yPer)
					return true;
				else
					return false;
			}
			else if(yPer > 50)
			{
				if(xPer<50+(100-yPer) && xPer>50-(100-yPer))
					return true;
				else
					return false;
			}
			
		}
		else
			return false;
		return false;
	}
	
	
}
