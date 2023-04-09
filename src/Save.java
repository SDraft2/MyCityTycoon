import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/* ������ ���忡 �ʿ��� ���
 * ��
 * Ŭ��Ŀ ����
 * �ǹ� �رݷ���
 * �ǹ� ������ ����, 
 * 
 * */

public class Save
{
	public void SaveData(Info info, Data_Ground[][] ground)
	{
		try
		{
			FileOutputStream iFileOut = new FileOutputStream("Save/Info.bin");
			FileOutputStream gFileOut = new FileOutputStream("Save/Ground.bin");
			try
			{
				ObjectOutputStream iObOut = new ObjectOutputStream(iFileOut);
				ObjectOutputStream gObOut = new ObjectOutputStream(gFileOut);
				iObOut.writeObject(info);
				gObOut.writeObject(ground);
				iObOut.close();
				gObOut.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
