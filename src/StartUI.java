import java.awt.*;
import java.awt.event.ActionEvent.*;
import javax.swing.*;

public class StartUI extends JFrame
{
	Dimension size = Toolkit.getDefaultToolkit().getScreenSize();	//해상도 구해오는 객체
	boolean turnOff = false; //종료시 인식하기위한 불린

	static boolean loadGSW = false;
	static boolean loadISW = false;
	static boolean loadSW = false;
	
	LoadGround loadGround = new LoadGround();
	LoadInfo loadInfo = new LoadInfo();
	static Data_Ground[][] ground;
	static Info info;
	static int loadError = 0;


	Lab background = new Lab();;
	But StartBut = new But();
	But LoadBut = new But();
	But ExitBut = new But();
	JPanel ButPan = new JPanel();
	LayPan Pan = new LayPan();

	int width = size.width/2;
	int height = size.height/2;

	
    Toolkit toolkit = Toolkit.getDefaultToolkit();
	Image icon = toolkit.getImage("Image/Icon.png");
	SoundBGMManager bgm = new SoundBGMManager("StartBGM.wav");
	SoundManager effSound = new SoundManager();
	
	StartUI()
	{
		setIconImage(icon);
		setTitle("마이시티 타이쿤");
		ground = loadGround.Load();
		info = loadInfo.Load();
		
		setSize(width,height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation((int)(size.getWidth()/2)-(this.getWidth()/2), (int)(size.getHeight()/2)-(this.getHeight()/2));
		
		background.setBounds(0,0,width,height);
		ButPan.setBounds((int)(width*0.425),(int)(height*0.6),(int)(width*0.15),(int)(height*0.25));
		StartBut.setBounds(0, 0, ButPan.getWidth(), (int)(ButPan.getHeight()*(1/3.0)));
		LoadBut.setBounds(0, (int)(ButPan.getHeight()*(1/3.0)), ButPan.getWidth(), (int)(ButPan.getHeight()*(1/3.0)));
		ExitBut.setBounds(0, (int)(ButPan.getHeight()*(2/3.0)), ButPan.getWidth(), (int)(ButPan.getHeight()*(1/3.0)));
		
		
		
		ButPan.setLayout(null);
		Pan.setLayout(null);
		
		ButPan.add(StartBut);
		ButPan.add(LoadBut);
		ButPan.add(ExitBut);
		
		Pan.add(background, new Integer(0));
		Pan.add(ButPan, new Integer(1));
		
		add(Pan);
		

		StartBut.addActionListener((e) -> {
			effSound.Play("Press.wav");
			turnOff = true; dispose();
			bgm.Stop();
		});
		LoadBut.addActionListener((e) -> {
			effSound.Play("Press.wav");
			if(loadGSW && loadISW)
				loadSW=true;
			
			if(loadSW)
			{
				turnOff = true;
				dispose();
				bgm.Stop();
			}
			else if(loadError != 0)
			{

				switch(loadError)
				{
					case 1:
						JOptionPane.showMessageDialog(this, "Save\\Ground.bin파일을 찾을 수 없습니다", "오류", JOptionPane.ERROR_MESSAGE); 
						break;
					case 2:
						JOptionPane.showMessageDialog(this, "Save\\Info.bin파일을 찾을 수 없습니다", "오류", JOptionPane.ERROR_MESSAGE); 
						break;
					case 3:
						JOptionPane.showMessageDialog(this, "뭐야?", "오류", JOptionPane.ERROR_MESSAGE); 
						break;
				}
			}
		});
		ExitBut.addActionListener((e) -> {
			dispose(); System.exit(0);}
		);
		ButPan.setOpaque(false);
		background.SetImg("Image/StartBG.png");
		StartBut.SetImg("Image/StartBut.png", false);
		LoadBut.SetImg("Image/LoadBut.png", false);
		ExitBut.SetImg("Image/ExitBut.png", false);
		setUndecorated(true);
		setVisible(true);
		setResizable(false);
	}
	
	public static Data_Ground[][] ReturnGround()
	{
		return ground;
	}
	public static Info ReturnData()
	{
		return info;
	}
}
