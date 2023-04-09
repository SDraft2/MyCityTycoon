import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoadInfo
{
	public Info Load()
	{
		//Data data;
		
		try
		{
			FileInputStream fileIn = new FileInputStream("Save/Info.bin");
			try
			{
				ObjectInputStream obIn = new ObjectInputStream(fileIn);
				try
				{
					StartUI.loadISW=true;
					return (Info)obIn.readObject();
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
			StartUI.loadError = 2;
			e1.printStackTrace();
		}
		return null;
		
	}
	
}
