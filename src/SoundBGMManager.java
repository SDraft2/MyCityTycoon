import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundBGMManager
{
	AudioInputStream sound;
	Clip clip;
	FloatControl control;
	
	Thread track;
	
	boolean trackSW;
	int Volume = 6;
	int bgmIdx = 1;
	
	SoundBGMManager()
	{
	}
	
	SoundBGMManager(String file)
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
				clip.loop(-1);;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	}
	
	public void Play(String file)
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
				Track();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	}
	public void Stop()
	{
		clip.stop();
	}
	
	public void Track()
	{
		trackSW = false;
		track = new Thread()
		{
			public void run()
			{
				try
				{
					while(clip.getFramePosition() < clip.getFrameLength())
					{
						Thread.sleep(10);
					}
					
					clip.close();
					bgmIdx++;
					if(bgmIdx==4)
						bgmIdx=1;
					trackSW = false;
					Play("BGM" + bgmIdx + ".wav");
				}
				catch (InterruptedException e){}
			}
		};
		track.start();
	}
	
	public void SetVolume(int volume)
	{
		Volume = volume;
		if(Volume ==-24)
			Volume  = -80;
		control.setValue(Volume);
	}
}
