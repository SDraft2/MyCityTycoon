import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/* 데이터 저장에 필요한 요소
 * 돈
 * 클리커 레벨
 * 건물 해금레벨
 * 건물 종류별 갯수, 
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
