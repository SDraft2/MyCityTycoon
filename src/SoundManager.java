import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager
{
	AudioInputStream sound;
	Clip clip;
	FloatControl control;
	int Volume = 6;
	
	public void Play(String file)
	{
		if(!StartUI.loadSW)
		{
			file = "Sound/" + file;
			try
			{
				sound = AudioSystem.getAudioInputStream(new File(file));
				clip = AudioSystem.getClip();
				clip.open(sound);
				control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				control.setValue(Volume);
				clip.start();
			}
			catch(Exception e)
			{ 
				e.printStackTrace(); 
			}
		}
	}
	public void Stop()
	{
		clip.stop();
	}
	
	public void SetVolume(int volume)
	{
		Volume = volume;
		if(Volume ==-24)
			Volume  = -80;
	}
}
