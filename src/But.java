import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class But extends JButton
{
	boolean pressFlag;
	int x;
	int y;
	int width;
	int height;
	int oriX;
	Image img;
	Image pressImg;
	Image nowImg;
	String file;
	String oriFile;
	
	But()
	{
		DefalutSetting();
	}
	
	But(String str)
	{
		setText(str);
		DefalutSetting();
	}
	
	private void DefalutSetting()
	{
		setBorderPainted(false);
		setContentAreaFilled(false);
		setFocusPainted(false);
		this.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e)
			{
				if(pressFlag)
				{
					nowImg = pressImg;
					repaint();
				}
			}
			public void mouseReleased(MouseEvent e)
			{
				if(pressFlag)
				{
					nowImg = img;
					repaint();
				}
			}
		});
	}
	
	public void SetImg(String file, boolean press)
	{
		pressFlag = press;
		oriFile = file;
		String filePress = file.substring(0, file.length()-4);
		filePress += "Press.png";
		x = getX();
		y = getY();
		width = getWidth();
		height = getHeight();
		try
		{
			img = ImageIO.read(new File(file));
			img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(this, file+"파일을 찾을 수 없습니다", "오류", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		if(press)
		{
			try
			{
				pressImg = ImageIO.read(new File(filePress));
				pressImg = pressImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);			
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(this, filePress+"파일을 찾을 수 없습니다", "오류", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
		}
		nowImg = img;

		this.file = file;
		repaint();
	}
	
	public void SetImgSize(int width, int height)
	{
		
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g.drawImage(nowImg, 0, 0, getWidth(), getHeight(), null);
		
		if(oriX != getWidth() && file != null)
		{
			SetImg(file, pressFlag);
			oriX = getWidth();
		}
		if(!isEnabled())
		{
			//setDisabledIcon("d");
		}
	}
}
