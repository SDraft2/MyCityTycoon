import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.plaf.metal.MetalSliderUI;

public class OptionSlider extends MetalSliderUI
{
	ImageIcon IconA;
	Image IconB;
	Image IconC;
	ImageIcon Icon;
	int size;
	OptionSlider(int size)
	{
		IconA = new ImageIcon("image/SliderThumb.png");
		IconB = IconA.getImage();
		IconC = IconB.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
		Icon = new ImageIcon(IconC);
	}
	
	public void paintThumb(Graphics g)  {
        Rectangle knobBounds = thumbRect;
        g.translate( knobBounds.x, knobBounds.y);
        Icon.paintIcon(slider, g, 0, 0);
        g.translate( -knobBounds.x, -knobBounds.y);
        
    }
	
    protected Dimension getThumbSize() {
        Dimension size = new Dimension();
        
        size.width = Icon.getIconWidth();
        size.height = Icon.getIconHeight();
  
        return size;
	}
    
}
