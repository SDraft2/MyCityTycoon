import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.*;

public class UIManager
{
	final int MAXBUILDLV;
	
	
	boolean draggDirect = false;
	int x;
	int y;
	int width;
	int height;
	int lineY=0;

	int menuIdx=0;				//현재 메뉴창 인덱스
	int[] menuY = {0,0,0};		//메뉴창이 보고있는 좌표
	
	LayPan BuildWindow;
	LayPan JobWindow;
	LayPan HireWindow;
	LayPan InvestWindow;
	LayPan GroundInfoWindow;
	LayPan JobInfoWindow;
	
	ClickerGame game;
	
	UIManager(ClickerGame game, int MAXBUILDLV, LayPan Build, LayPan Job, LayPan Hire, LayPan Invest, LayPan GroundInfo, LayPan JobInfo)
	{
		this.game = game;
		this.MAXBUILDLV = MAXBUILDLV;
		BuildWindow = Build;
		JobWindow = Job;
		HireWindow = Hire;
		InvestWindow = Invest;
		GroundInfoWindow = GroundInfo;
		JobInfoWindow = JobInfo;
	}
	
	
	public void DrawMenuUI(LayPan MenuLayout, LayPan MenuWindow, JPanel LinePan, Lab Icon, Lab Name, Lab Value, LayPan BuyPan, But BuyBut, Lab BuyTxt, int start, int i)
	{
		MenuWindow.setBounds(MenuLayout.getX()+(int)(MenuLayout.getWidth()*0.02),MenuLayout.getY()+(int)(MenuLayout.getHeight()*0.18), (int)(MenuLayout.getWidth()*0.96),(int)(MenuLayout.getHeight()*0.8135));//0.954
		
		MenuWindow.setLayout(null);
		BuyBut.addActionListener(game);
		LinePan.setLayout(null);
		BuyPan.setLayout(null);
			
		LinePan.add(Icon);
		LinePan.add(Name);
		LinePan.add(Value);
			
		BuyPan.add(BuyBut, new Integer(0));
		BuyPan.add(BuyTxt, new Integer(1));
		LinePan.add(BuyPan, new Integer(2));
		MenuWindow.add(LinePan, new Integer(2));
			
		LinePan.setBounds(0,(int)((MenuWindow.getHeight()*0.22)*(i-start)),(int)(MenuWindow.getWidth()), (int)(MenuWindow.getHeight()*0.16));
		Icon.setBounds(0,0,LinePan.getHeight(),LinePan.getHeight());
		Name.setBounds((int)(LinePan.getWidth()*0.16),(int)(LinePan.getHeight()*0.15),(int)(LinePan.getWidth()*0.6),(int)(LinePan.getHeight()*0.3));
		Value.setBounds((int)(LinePan.getWidth()*0.16),(int)(LinePan.getHeight()*0.6),(int)(LinePan.getWidth()*0.6),(int)(LinePan.getHeight()*0.3));
		BuyPan.setBounds((int)(LinePan.getWidth()*0.73),0, (int)(LinePan.getWidth()*0.25), (int)(LinePan.getHeight()));
		BuyBut.setBounds(0, 0, BuyPan.getWidth(), BuyPan.getHeight());
		BuyTxt.setBounds(0, (int)(BuyPan.getHeight()*0.65), BuyPan.getWidth(),(int)(BuyPan.getHeight()*0.26));
		BuyTxt.setForeground(Color.white);
		MenuWindow.setOpaque(false);
		LinePan.setOpaque(false);
		BuyBut.setEnabled(false);

		Name.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(LinePan.getHeight()*0.28)));
		Value.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(LinePan.getHeight()*0.28)));
		BuyTxt.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(LinePan.getHeight()*0.20)));
	}
	
	public void DrawGroundInfoUI(LayPan MenuLayout, LayPan MenuWindow, Lab Icon, Lab Name, Lab Income, Lab LV, Lab Prc, LayPan SellPan, But SellBut, Lab SellTxt, Lab IncomeIcon, Lab PrcIcon, Lab LvIcon)
	{
		MenuWindow.setBounds((int)(MenuLayout.getWidth()*0.02),0, (int)(MenuLayout.getWidth()*1),(int)(MenuLayout.getHeight()*0.16));//0.954
		
		MenuWindow.setLayout(null);
		MenuWindow.add(Icon, new Integer(0));
		MenuWindow.add(Name, new Integer(0));
		MenuWindow.add(LV, new Integer(0));
		MenuWindow.add(Income, new Integer(0));
		MenuWindow.add(Prc, new Integer(0));
			
		MenuWindow.add(IncomeIcon, new Integer(0));
		MenuWindow.add(PrcIcon, new Integer(0));
		MenuWindow.add(LvIcon, new Integer(0));			
	
		MenuWindow.add(SellPan, new Integer(0));
		SellPan.add(SellBut, new Integer(0));
		SellPan.add(SellTxt, new Integer(1));
		
		Icon.setBounds((int)(MenuWindow.getWidth()*0),(int)(MenuWindow.getHeight()*0.15),(int)(MenuWindow.getHeight()*0.8),(int)(MenuWindow.getHeight()*0.8));
		Name.setBounds((int)(MenuWindow.getWidth()*0.15),(int)(MenuWindow.getHeight()*0.15),(int)(MenuWindow.getWidth()),(int)(MenuWindow.getHeight()*0.4));
		
		IncomeIcon.setBounds((int)(MenuWindow.getWidth()*0.15),(int)(MenuWindow.getHeight()*0.62),(int)(MenuWindow.getHeight()*0.25),(int)(MenuWindow.getHeight()*0.25));
		LvIcon.setBounds((int)(MenuWindow.getWidth()*0.46),(int)(MenuWindow.getHeight()*0.62),(int)(MenuWindow.getHeight()*0.25),(int)(MenuWindow.getHeight()*0.25));
		PrcIcon.setBounds((int)(MenuWindow.getWidth()*0.57),(int)(MenuWindow.getHeight()*0.62),(int)(MenuWindow.getHeight()*0.25),(int)(MenuWindow.getHeight()*0.25));
		
		Income.setBounds((int)(MenuWindow.getWidth()*0.2),(int)(MenuWindow.getHeight()*0.6),(int)(MenuWindow.getWidth()),(int)(MenuWindow.getHeight()*0.3));
		LV.setBounds((int)(MenuWindow.getWidth()*0.51),(int)(MenuWindow.getHeight()*0.6),(int)(MenuWindow.getWidth()),(int)(MenuWindow.getHeight()*0.3));
		Prc.setBounds((int)(MenuWindow.getWidth()*0.62),(int)(MenuWindow.getHeight()*0.6),(int)(MenuWindow.getWidth()),(int)(MenuWindow.getHeight()*0.3));
		
		SellPan.setBounds((int)(MenuWindow.getWidth()*0.7),(int)(MenuWindow.getHeight()*0.15),(int)(MenuWindow.getWidth()*0.24),(int)(MenuWindow.getHeight()*0.81));
		SellBut.setBounds(0, 0, SellPan.getWidth(), SellPan.getHeight());
		SellTxt.setBounds(0, (int)(SellPan.getHeight()*0.65), SellPan.getWidth(),(int)(SellPan.getHeight()*0.26));

		Name.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(SellPan.getHeight()*0.36)));
		Income.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(SellPan.getHeight()*0.30)));
		LV.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(SellPan.getHeight()*0.30)));
		Prc.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(SellPan.getHeight()*0.30)));
		SellTxt.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(SellPan.getHeight()*0.21)));
		
		SellTxt.setForeground(Color.white);
		SellBut.SetImg("image/Menu/SellBut.png", true);

		IncomeIcon.SetImg("image/Menu/Income.png");
		PrcIcon.SetImg("image/Menu/Prc.png");
		LvIcon.SetImg("image/Menu/Lv.png");
		
		MenuLayout.add(MenuWindow);
	}
	public void ResizeMenuUI(JPanel LinePan, Lab Icon, Lab Name, Lab Value, LayPan BuyPan, But BuyBut, Lab BuyTxt, double wid, double hei)
	{
		Resize(LinePan, wid, hei);
		Resize(Icon, wid, hei);
		Resize(Name, wid, hei);
		Resize(Value, wid, hei);
		Resize(BuyPan, wid, hei);
		Resize(BuyBut, wid, hei);
		Resize(BuyTxt, wid, hei);
		
		Name.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(LinePan.getHeight()*0.28)));
		Value.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(LinePan.getHeight()*0.28)));
		BuyTxt.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(LinePan.getHeight()*0.20)));
	}
	
	public void ResizeGroundInfoUI(LayPan MenuLayout, Lab Icon, Lab Name, Lab Income, Lab LV, Lab Prc, LayPan SellPan, But SellBut, Lab SellTxt, Lab IncomeIcon, Lab LvIcon, Lab PrcIcon, double wid, double hei)
	{
		Resize(MenuLayout, wid, hei);
		Resize(Icon, wid, hei);
		Resize(Name, wid, hei);
		Resize(Income, wid, hei);
		Resize(LV, wid, hei);
		Resize(Prc, wid, hei);
		Resize(SellPan, wid, hei);
		Resize(SellBut, wid, hei);
		Resize(SellTxt, wid, hei);
		Resize(IncomeIcon, wid, hei);
		Resize(LvIcon, wid, hei);
		Resize(PrcIcon, wid, hei);
		
		Name.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(SellPan.getHeight()*0.36)));
		LV.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(SellPan.getHeight()*0.30)));
		Income.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(SellPan.getHeight()*0.30)));
		Prc.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(SellPan.getHeight()*0.30)));
		SellTxt.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(SellPan.getHeight()*0.21)));
	}
	
	public void DrawJobInfoUI(LayPan MenuLayout, LayPan MenuWindow, Lab Icon, Lab Name, Lab Value, Lab LV, Lab ValueIcon, Lab LvIcon)
	{
		MenuWindow.setBounds((int)(MenuLayout.getWidth()*0.02),0, (int)(MenuLayout.getWidth()*1),(int)(MenuLayout.getHeight()*0.16));//0.954
		
		MenuWindow.setLayout(null);
		MenuWindow.add(Icon, new Integer(0));
		MenuWindow.add(Name, new Integer(0));
		MenuWindow.add(LV, new Integer(0));
		MenuWindow.add(Value, new Integer(0));
			
		MenuWindow.add(ValueIcon, new Integer(0));
		MenuWindow.add(LvIcon, new Integer(0));		
		
		Icon.setBounds((int)(MenuWindow.getWidth()*0),(int)(MenuWindow.getHeight()*0.15),(int)(MenuWindow.getHeight()*0.8),(int)(MenuWindow.getHeight()*0.8));
		Name.setBounds((int)(MenuWindow.getWidth()*0.15),(int)(MenuWindow.getHeight()*0.15),(int)(MenuWindow.getWidth()),(int)(MenuWindow.getHeight()*0.4));
		
		ValueIcon.setBounds((int)(MenuWindow.getWidth()*0.15),(int)(MenuWindow.getHeight()*0.62),(int)(MenuWindow.getHeight()*0.25),(int)(MenuWindow.getHeight()*0.25));
		LvIcon.setBounds((int)(MenuWindow.getWidth()*0.65),(int)(MenuWindow.getHeight()*0.62),(int)(MenuWindow.getHeight()*0.25),(int)(MenuWindow.getHeight()*0.25));
	
		Value.setBounds((int)(MenuWindow.getWidth()*0.2),(int)(MenuWindow.getHeight()*0.6),(int)(MenuWindow.getWidth()),(int)(MenuWindow.getHeight()*0.3));
		LV.setBounds((int)(MenuWindow.getWidth()*0.7),(int)(MenuWindow.getHeight()*0.6),(int)(MenuWindow.getWidth()),(int)(MenuWindow.getHeight()*0.3));
	
		Name.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(MenuWindow.getHeight()*0.29)));
		Value.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(MenuWindow.getHeight()*0.23)));
		LV.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(MenuWindow.getHeight()*0.23)));
	
		ValueIcon.SetImg("image/Menu/Income.png");
		LvIcon.SetImg("image/Menu/Lv.png");
		
		MenuLayout.add(MenuWindow);
	}
	
	public void ResizeJobInfoUI(LayPan MenuWindow, Lab Icon, Lab Name, Lab Value, Lab LV, Lab ValueIcon, Lab LvIcon, double wid, double hei)
	{
		Resize(MenuWindow, wid, hei);
		Resize(Icon, wid, hei);
		Resize(Name, wid, hei);
		Resize(Value, wid, hei);
		Resize(LV, wid, hei);
		Resize(ValueIcon, wid, hei);
		Resize(LvIcon, wid, hei);

		Name.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(MenuWindow.getHeight()*0.29)));
		Value.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(MenuWindow.getHeight()*0.23)));
		LV.setFont(new Font(ClickerGame.font, ClickerGame.fontType, (int)(MenuWindow.getHeight()*0.23)));
	}
	
	public void SetSlider(JSlider Slider)
	{
		Slider.setOpaque(false);
		Slider.setMajorTickSpacing(15);
		Slider.setPaintTicks(true);
		Slider.setMaximum(6);
		Slider.setMinimum(-24);
		Slider.setUI(new OptionSlider((int)(Slider.getHeight()*0.8)));
	}
	
	public int Loading(Lab lab)
	{
		return lab.getWidth();
	}
	
	public void ScrollMenu(LayPan Window, JPanel[] LinePan, int startLine, final int MAX, int e)
	{
		int scrollAmount = 2;
		int size=(int)((int)(LinePan[MAX-1].getY())-(int)(LinePan[startLine].getY()))-(int)Math.round(Window.getHeight()*0.789);

		//DrawMenu(ScrollWindow, LinePan, Icon, Name, Value, BuyPan, BuyBut, BuyTxt, i);
		menuY[menuIdx] += e*(LinePan[startLine].getHeight()/scrollAmount);
		
		if(menuY[menuIdx]<0)
			menuY[menuIdx] = 0;
		else if(menuY[menuIdx] > size)
			menuY[menuIdx] = size;
		
			for(int i=startLine ; i<MAX ; i++)
				LinePan[i].setBounds(0, (int)((Window.getHeight()*0.22)*(i-startLine))-(menuY[menuIdx]),LinePan[i].getWidth(), LinePan[i].getHeight());
	}
	
	public void DraggMenu(LayPan Window, JPanel[] LinePan, int startLine, final int MAX, int e, int draggPressY, int draggReleaseY)
	{
		int size=(int)((int)(LinePan[MAX-1].getY())-(int)(LinePan[startLine].getY()))-(int)Math.round(Window.getHeight()*0.789);

		if(!draggDirect)
			menuY[menuIdx]=e-draggPressY+draggReleaseY;
		else
			menuY[menuIdx]=e-draggPressY+draggReleaseY;
		
		if(menuY[menuIdx]<0)
			menuY[menuIdx] = 0;
		else if(menuY[menuIdx] > size)
			menuY[menuIdx] = size;
		
			for(int i=startLine ; i<MAX ; i++)
				LinePan[i].setBounds(0, (int)((Window.getHeight()*0.22)*(i-startLine)-menuY[menuIdx]),LinePan[i].getWidth(), LinePan[i].getHeight());
	}
	
	public void ShowMenu(int MenuIdx)
	{
		boolean build = false;
		boolean job = false;
		boolean hire = false;
		//boolean invest = false;

		menuIdx = MenuIdx-1;
		
		switch(MenuIdx)
		{
			case 1:
				GroundInfoWindow.setVisible(true);
				JobInfoWindow.setVisible(false);
				build = true;
				break;
			case 2:
				GroundInfoWindow.setVisible(false);
				JobInfoWindow.setVisible(true);
				job = true;
				break;
			case 3:
				hire = true;
				break;
			/*case 4:
				invest = true;
				break;*/
			default:
				build = false;
				job = false;
				hire = false;
				//invest = false;
				break;
		}
		
		BuildWindow.setVisible(build);
		JobWindow.setVisible(job);
		HireWindow.setVisible(hire);
		//InvestWindow.setVisible(invest);
	}
	
	public void ShowMenu(int MenuIdx, int x, int y)
	{
		BuildWindow.setVisible(false);
		JobWindow.setVisible(false);
		HireWindow.setVisible(false);
		InvestWindow.setVisible(false);
	}
	
	protected void paintComponent(Graphics g)
	{
		
	}
	
	public void setXY(int x, int y, int width, int height)
	{
		
	}
	
	public void setMenuIdx(int idx)
	{
		menuIdx = idx;
	}

	public int getMenuIdx()
	{
		return menuIdx;
	}
	
	/*public int setMenuY(int idx)
	{
		menuY[idx] = y;
	}*/

	public int getMenuY(int idx)
	{
		if(!draggDirect)
			return menuY[idx];
		return -menuY[idx];
	}
	
	public void Resize(LayPan l, double width, double height)
	{
		l.setBounds((int)(l.getX() * width), (int)(l.getY() * height), (int)(l.getWidth() * width), (int)(l.getHeight() * height));
	}
	public void Resize(JPanel l, double width, double height)
	{
		l.setBounds((int)(l.getX() * width), (int)(l.getY() * height), (int)(l.getWidth() * width), (int)(l.getHeight() * height));
	}
	public void Resize(Lab l, double width, double height)
	{
		l.setBounds((int)(l.getX() * width), (int)(l.getY() * height), (int)(l.getWidth() * width), (int)(l.getHeight() * height));
		//l.setFont(l.getFont(), ClickerGame.fontType, );
	}
	public void Resize(But l, double width, double height)
	{
		l.setBounds((int)(l.getX() * width), (int)(l.getY() * height), (int)(l.getWidth() * width), (int)(l.getHeight() * height));
	}
	
	public void Resize(JSlider l, double width, double height)
	{
		l.setBounds((int)(l.getX() * width), (int)(l.getY() * height), (int)(l.getWidth() * width), (int)(l.getHeight() * height));
		l.setUI(new OptionSlider((int)(l.getHeight()*0.8)));
	}
	public void Resize(JRadioButton l, double width, double height)
	{
		l.setBounds((int)(l.getX() * width), (int)(l.getY() * height), (int)(l.getWidth() * width), (int)(l.getHeight() * height));
	}
}
