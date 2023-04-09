import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

class Lab extends JLabel
{
		String file;
		int x;
		int y;
		int width;
		int height;
		int oriX;
		double wid;
		double hei;
		
		Image img;
		
		Lab()
		{
		}
		
		Lab(String str)
		{
			setText(str);
		}
		
		Lab(String str, int i)
		{
			setText(str);
			setHorizontalAlignment(i);
		}
		
		public void SetImg(String file)
		{
			this.file = file;
			x = getX();
			y = getY();
			width = getWidth();
			height = getHeight();
			
			try
			{
				img = ImageIO.read(new File(file));
				img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
				repaint();
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(this, file+"파일을 찾을 수 없습니다", "오류", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			repaint();
		}
		/*
		public void SetFont(int type, int size)
		{
			try
			{
				Font f = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(new File("Font.ttf"))).deriveFont(type, size);
				FontMetrics fm = new FontMetrics(font);
				
			} catch (FileNotFoundException e) {
			} catch (FontFormatException e) {
			} catch (IOException e) {
			}
		}
		*/
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		
			if(oriX != getWidth() && file != null)
			{
				SetImg(file);
				oriX = getWidth();
			}
		}
	}