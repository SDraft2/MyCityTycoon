import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.JLayeredPane;

class LayPan extends JLayeredPane
	{
		int x;
		int y;
		int width;
		int height;
		Image img;
		
		LayPan()
		{
		}
		public void SetImgSize(int width, int height)
		{
			
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		}
	}