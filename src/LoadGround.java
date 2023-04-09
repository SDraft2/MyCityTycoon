import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoadGround
{
	public Data_Ground[][] Load()
	{
		//Data data;
		
		try
		{
			FileInputStream fileIn = new FileInputStream("Save/Ground.bin");
			try
			{
				ObjectInputStream obIn = new ObjectInputStream(fileIn);
				try
				{
					StartUI.loadGSW=true;
					return (Data_Ground[][])obIn.readObject();
				}
				catch (ClassNotFoundException e)
				{
				}
			}
			catch (IOException e)
			{
			}
		}
		catch (FileNotFoundException e1)
		{
			StartUI.loadError = 1;
			e1.printStackTrace();
		}
		return null;
		
	}
	
}
