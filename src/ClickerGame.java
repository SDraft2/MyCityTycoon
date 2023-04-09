import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

import javax.swing.*;

import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class ClickerGame extends JFrame	implements ActionListener
{
	private static final long serialVersionUID = 1162840687832604695L;	//버전
	
	/* 상수 설명
	CLICKDELAY		
	
	INVESTDELAY		
	
	MAXBUILD		건물종류 갯수
	MAXJOB			직업(클릭)종류 갯수
	MAXHIRE			??갯수
	MAXINVEST		투자 종류 갯수
	GROUNDWIDTH		5일경우 땅이 5x5로 나옴
	GROUNDHEIGHT	땅 생성을 처리하기 위한 상수
	 * */
	final int MAIN_THREAD_TIME_UNIT = 5;
	final int EFFNORMAL = 1;
	final int EFFARBITE = 2;
	
	final int ERRTIME = 1500;		//에러 메세지 띄우는 시간	1000 = 1초
	final int INFOTIME = 2500;		//정보 메세지 띄우는 시간
	
	final int CLICKDELAY = 35;		//클릭 딜레이 (오토마우스 방지)
	final int INVESTDELAY = 1000;	//투자 갱신시간
	
	final int MAXBUILD = 8 + 1;
	final int MAXBUILDLV = 30;
	final int MAXJOB = 10 + 1;
	final int MAXJOBLV = 5;
	final int MAXHIRE = 6;
	final int MAXINVEST = 3;

	final int STARTBUILDLINE = 1;	// 메뉴가 몇레벨부터 표시되는지 
	final int STARTJOBLINE = 1;
	
	/*final int MAXBUILDNUM = 30;
	final int MAXBUILDLV = MAXBUILDNUM/10;*/
	final int GROUNDWIDTH = 5;				//땅 최대 길이
	final int GROUNDHEIGHT = GROUNDWIDTH*2;

	boolean scrFlag = false;			//전체화면, 창화면 했는지 여부 확인
	boolean draggFlag = false;			//드래그를 했는지 여부 확인
	boolean optionFlag = false;			//옵션창 띄여졌는지 여부 확인
	boolean errFlag = false;			//에러창 띄워졌는지 여부 확인
	boolean ivSubFlag = false;			//도박창 띄워졌늕 여부 확인
	boolean infoFlag = false;
	boolean delayFlag = false;		//오토마우스 방지
	
	int mouseX = 0;
	int mouseY = 0;
	int scrollY = 0;			//스크롤했을때 좌표
	int draggPressY = 0;		//드래그했을때 좌표
	int draggReleaseY = 0;		//드래그했을때 좌표
	int effNum = 0;
	int ran;
	
	int groundX = 0;
	int groundY = 0;

	int currIvIdx = 0;
	int ivIdxMouse = 0;
	
	int pastGroundX = GROUNDWIDTH;
	int pastGroundY = GROUNDWIDTH;	//이전에 클릭했던 땅의 좌표

	//스레드 타이머 변수들
	int arbiteTimer = 0;
	int delayTimer=0;
	int errTimer = 0;
	int infoTimer = 0;
	int ivTimer = 0;
	int[] ivUpdateTimers = new int[MAXINVEST];
	
	Thread mainThread; //메인 스레드
	
	EffThread clickEffThr;	//이펙트 스레드
	
	String money = "";
	
	Container contentPane = getContentPane();
	Dimension dimension = new Dimension();	//프레임 객체
	JFrame fullFrame;		
	SoundBGMManager BGM = new SoundBGMManager();
	SoundManager effSound = new SoundManager();
	Random random = new Random();
	//타이머 사용 객체
	
	Info data = new Info(0,0,1,MAXBUILD, MAXHIRE, MAXINVEST);			//데이터 (돈, 클리커레벨, 건물해금레벨, 건물종류 갯수)
	

	Timer incomeTimer = new Timer();		
	Timer updateTimer = new Timer();
	
	TimerTask incomeTask = new TimerTask()
	{
		public void run()		
		{
			data.addMoney(data.getIncome()/10);
			//data.addMoney(data.getIncome()/10*data.getaValue());
		}
	};
	TimerTask updateTask = new TimerTask()
	{
		public void run()
		{
			UpdateInfo();
		}
	};
	
	
	
	Data_Ground[][] ground = new Data_Ground[GROUNDHEIGHT][GROUNDWIDTH];	//땅 정보
	Data_Building[] build = new Data_Building[MAXBUILD];					//건물 
	Data_Job[] job = new Data_Job[MAXJOB];									//클릭레벨
	Data_Hire[] hire = new Data_Hire[MAXHIRE];								//알바고용
	Data_Invest[] iv = new Data_Invest[MAXINVEST];						//투자

	int[] groundMaxWidth = new int[GROUNDHEIGHT];
	
	
	LayPan Screen = new LayPan();
	Lab Loading = new Lab();
	Lab BGImg = new Lab();
	JPanel mouseXY = new JPanel();
	
	/*
	 * MenuBarPan		상단 메뉴바
	 * 하위요소들은 상단메뉴바에 들어가있는것
	 * 
	 * */
	
	LayPan MenuBarPan = new LayPan();
	//Lab GoldInfoLab = new Lab();
	Lab CashInfoLab = new Lab();
	JPanel MoneyTxtBG = new JPanel();
	
	But OptionBut = new But();
	But OptionExitBut = new But();
	Lab OptionBG = new Lab();
	JSlider BGMVolumeSlider = new JSlider();
	JSlider EffVolumeSlider = new JSlider();
	But FullScrBut = new But();
	But WindowScrBut = new But();
	ButtonGroup ScrCheck = new ButtonGroup();
	
	Lab MoneyTxt = new Lab(data.getMoney()+"", SwingConstants.RIGHT);
	Lab MoneyIncomeTxt = new Lab("", SwingConstants.RIGHT);
	Lab ClickEffTxt = new Lab();
	Lab ErrTxt = new Lab("", SwingConstants.CENTER);
	Lab InfoTxt = new Lab("", SwingConstants.RIGHT);
	
	//Lab labJobNextPriceText = new Lab("다음 레벨업까지 : "+data.money);
	
	But ClickBut = new But();
	But ClickLvUpBut = new But();
	

	/*MenuLayout			좌측 메뉴패널
	 * 
	 * */
	

	
	Lab OptionWindow = new Lab();

	
	But SaveBut = new But();
	But SaveCloseBut = new But();
	But b1 = new But();
	But b2 = new But();
	But OptionLayoutBut = new But();		//뒤의 요소까지 클릭되는거 방지하는 목적임

	LayPan MenuLayout = new LayPan();
	Lab MenuBG = new Lab();

	LayPan GroundBuildField = new LayPan();
	LayPan GroundField = new LayPan();		//건물을 표시하는 판넬
	JPanel[] GroundLine = new JPanel[GROUNDHEIGHT];	//라인당 건물
	JPanel[] GroundBuildLine = new JPanel[GROUNDHEIGHT];	//라인당 건물
	GroundBut[][] Ground = new GroundBut[GROUNDHEIGHT][GROUNDWIDTH+1];
	Lab[][] GroundBuild = new Lab[GROUNDHEIGHT][GROUNDWIDTH+1];
	Lab GroundBorder = new Lab();
	

	
	LayPan GroundInfoMenu = new LayPan();
	Lab GroundInfoIcon = new Lab();
	Lab GroundInfoName = new Lab();
	Lab GroundInfoIncomeTxt = new Lab();
	Lab GroundInfoLVTxt = new Lab();
	Lab GroundInfoBuildNTxt = new Lab();
	LayPan GroundSellPan = new LayPan();
	But GroundSellBut = new But();
	Lab GroundSellTxt = new Lab("", SwingConstants.CENTER);

	LayPan JobInfoMenu = new LayPan();
	Lab JobInfoIcon = new Lab();
	Lab JobInfoName = new Lab();
	Lab JobInfoValue = new Lab();
	Lab JobInfoLv = new Lab();
	Lab	JobInfoValueTxt = new Lab();
	Lab JobInfoLvTxt = new Lab();
	
	Lab GroundIncomeIcon = new Lab();
	Lab GroundPrcIcon = new Lab();
	Lab GroundLvIcon = new Lab();
	
	LayPan BuildMenu = new LayPan();
	But BuildMenuBut = new But();
	JPanel[] BuildDataPan = new JPanel[MAXBUILD];
	Lab[] BuildIcon = new Lab[MAXJOB];
	Lab[] BuildValue = new Lab[MAXBUILD];
	Lab[] BuildName = new Lab[MAXBUILD];
	LayPan[] BuildBuyPan = new LayPan[MAXBUILD];
	But[] BuildBuyBut = new But[MAXBUILD];
	Lab[] BuildBuyTxt = new Lab[MAXBUILD];
	//
	LayPan JobMenu = new LayPan();
	But JobMenuBut = new But();
	JPanel[] JobDataPan = new JPanel[MAXJOB];
	Lab[] JobIcon = new Lab[MAXJOB];
	Lab[] JobValue = new Lab[MAXJOB];
	Lab[] JobName = new Lab[MAXJOB];
	LayPan[] JobBuyPan = new LayPan[MAXJOB];
	But[] JobBuyBut = new But[MAXJOB];
	Lab[] JobBuyTxt = new Lab[MAXJOB];
	//
	LayPan HireMenu = new LayPan();
	But HireMenuBut = new But();
	JPanel[] HireDataPan = new JPanel[MAXHIRE];
	Lab[] HireIcon = new Lab[MAXHIRE];
	Lab[] HireValue = new Lab[MAXHIRE];
	Lab[] HireName = new Lab[MAXHIRE];
	LayPan[] HireBuyPan = new LayPan[MAXHIRE];
	But[] HireBuyBut = new But[MAXHIRE];
	Lab[] HireBuyTxt = new Lab[MAXHIRE];
	//
	LayPan IvMenu = new LayPan();
	But IvMenuBut = new But();
	JPanel[] IvDataPan = new JPanel[MAXINVEST];
	Lab[] IvIcon = new Lab[MAXINVEST];
	Lab[] IvValue = new Lab[MAXINVEST];
	Lab[] IvName = new Lab[MAXINVEST];
	LayPan[] IvBuyPan = new LayPan[MAXINVEST];
	But[] IvBuyBut = new But[MAXINVEST];
	Lab[] IvBuyTxt = new Lab[MAXINVEST];
	
	Lab IvSubMenu = new Lab();
	Lab IvSubName = new Lab("", SwingConstants.CENTER);
	Lab IvSubMoney = new Lab("", SwingConstants.CENTER);
	Lab IvSubTimer = new Lab("", SwingConstants.CENTER);
	LayPan IvSubBuyPan = new LayPan();
	LayPan IvSubSellPan = new LayPan();
	But IvSubBuyBut = new But();
	But IvSubSellBut = new But();
	Lab IvSubInfo = new Lab();
	Lab IvSubInfoProbaTxt = new Lab("", SwingConstants.CENTER);
	Lab IvSubInfoWinLateStr = new Lab("성공확률", SwingConstants.CENTER);
	Lab IvSubInfoWinValueTxt = new Lab("", SwingConstants.CENTER);
	Lab IvSubInfoWinMoneyTxt = new Lab("", SwingConstants.CENTER);
	Lab IvSubInfoLoseValueTxt = new Lab("", SwingConstants.CENTER);
	Lab IvSubInfoLoseMoneyTxt = new Lab("", SwingConstants.CENTER);
	Lab IvSubBuyTxt = new Lab("", SwingConstants.CENTER);
	Lab IvSubSellTxt = new Lab();
	But[] IvSubBut = new But[MAXINVEST];
	But IvSubExitBut = new But();
	But IvSubLayoutBut = new But();
	
	//
	//기본 UI 컴포넌트
	//이미지
	Save save = new Save();						//저장 클래스 선언
	LoadInfo loadInfo = new LoadInfo();						//불러오기 클래스 선언
	LoadGround loadGround = new LoadGround();
	UIManager ui = new UIManager(this, MAXBUILD, BuildMenu, JobMenu, HireMenu, IvMenu, GroundInfoMenu, JobInfoMenu);							//UI관리하는 클래스 선언
	
	//private static GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	//private static GraphicsDevice device = env.getDefaultScreenDevice();
    //private static final boolean fullScrFlag = device.isFullScreenSupported();	//장치가 전체화면이 호환 되는지 안되는지
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    Toolkit toolkit = Toolkit.getDefaultToolkit();
	Image icon = toolkit.getImage("Image/Icon.png");
	
	final int winWidth = (int)(size.getWidth()*(2/3.0));
	final int winHeight = (int)(size.getHeight()*(2/3.0));

	int width = winWidth;
	int height = winHeight;

	static String font = "맑은 고딕";
	static int fontType = Font.BOLD;
	
	ClickerGame()
	{
			setIconImage(icon);
			setTitle("마이시티 타이쿤");
	    	setResizable(false);
			setVisible(true);
			
			fullFrame = new JFrame();
			fullFrame.setUndecorated(true);
			dimension = new Dimension(width,height);	//프레임 객체
			
			contentPane.setPreferredSize(dimension);
			Screen.setBounds(0,0,width,height);
			pack();
			//setSize(width,height);//해상도에 비례한 창 크기
			setLocationRelativeTo(null);
			
			contentPane.add(Loading);
			Loading.setBounds(0,0,Screen.getWidth(),Screen.getHeight());
			Loading.SetImg("Image/Loading.png");

			job[0] = new Data_Job("백수",				1,					0);		//이름, 클릭당 머니, 가격
			job[1] = new Data_Job("상하차 알바",		2000000000,			100);
			job[2] = new Data_Job("일용직 알바",		10,					1000);
			job[3] = new Data_Job("공장 알바",			100,				15000);
			job[4] = new Data_Job("마트 알바",			1000,				100000);
			job[5] = new Data_Job("카페 알바",			10000,				500000);
			job[6] = new Data_Job("회사원",			150000,				3000000);
			job[7] = new Data_Job("팀장",				1000000,			75000000);
			job[8] = new Data_Job("과장",				10000000,			800000000);
			job[9] = new Data_Job("부장",				100000000,			1500000000);
			job[10] = new Data_Job("회장",			1000000000,			2000000000);

			build[0] = new Data_Building("빈 땅",					"빈 땅",				"빈 땅",					0,		0);
			build[1] = new Data_Building("원룸",					"아파트",				"엠파이어 스테이트", 		1,		300);	//1,2,3레벨 이름, 초당 수익, 시작가격
			build[2] = new Data_Building("동네슈퍼",				"대형마트",			"백화점",					3,		1500);
			build[3] = new Data_Building("카페",					"스타벅스",			"커피의 여신상",			10,		30000);
			build[4] = new Data_Building("아이스크림 가게",			"설빙",				"맛의 정상",				40,		150000);
			build[5] = new Data_Building("놀이터",				"놀이공원",			"쥐라기공원"	,			150,	500000);
			build[6] = new Data_Building("게스트 하우스",			"호텔",				"마리나 베이 샌즈",			1000,	2500000);
			build[7] = new Data_Building("시청",					"청와대",				"미국 의회",				5000,	35000000);
			build[8] = new Data_Building("미술관",				"박물관",				"루브르 박물관",			35000,	1500000000);
			
			hire[0] = new Data_Hire("아르바이트 고용",		5,				500);	//이름, 초당 클릭되는 양(작으면 더 빠름), 가격
			hire[1] = new Data_Hire("전단지 광고",			1,				25000);	//이름, 체인점 밸류, 시작 가격
			hire[2] = new Data_Hire("지하철 광고",			5,				150000);
			hire[3] = new Data_Hire("유튜브 광고",			10,				1000000);
			hire[4] = new Data_Hire("라디오 광고",			25,				15000000);
			hire[5] = new Data_Hire("TV 광고",				50,				100000000);
									//이름, 			(초)주기, 	성공확률, 			차감액, 	성공시 오를 배수, 	실패시 곱할 배수
			iv[0] = new Data_Invest("주식", 			7000, 			0, 			20, 	2, 				0.5);					
			iv[1] = new Data_Invest("부동산",			6000,			30,			50, 	3, 				0.4);
			iv[2] = new Data_Invest("비트코인",		5000, 			100,		100,	5, 				0.3);		
			
			Screen.setLayout(null);
			mouseXY.setLayout(null);
			
			//클릭 액션
			//타이머

			OptionWindow.setVisible(false);
			DrawMenuUI();
			DrawGround();
			DrawMenuBarUI();
			DrawOptionUI();
			DrawIvSubMenu();
			SetButtonEnable();
			SetComponunt();
			ui.ShowMenu(1);
			ShowSubIv(0);

			Inicial();
			UISetting();

			Event();
			EventMouse();
			EventButton();

			//로드버튼 클릭했을때
			
			incomeTimer.schedule(incomeTask, 30, 100);
			updateTimer.schedule(updateTask, 30, 10);

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
			
			if(StartUI.loadSW)
			{
				
				ground = StartUI.ReturnGround();
				data = StartUI.ReturnData();	//Data클래스를 반환받아서 data에 넣음
				
				boolean flag = false;
				int buildNum;
				int jobLv = (data.getClickLv()-1)*MAXJOBLV;
				int jobNum = data.getClickNum();
				int arbiteLv = data.getArbiteLv();
				int[] adNum = new int[MAXHIRE];
				//double[] adPrice = new double[MAXHIRE];
				double money = data.getMoney();
					//Data클래스를 반환받아서 data에 넣음

				data.setIncome(0);
				data.setClickLv(0);
				data.setClickNum(1);
				for(int i=1 ; i<=jobLv+jobNum ; i++)
				{
						JobBuy();					
				}
				if(data.getClickLv()==MAXJOB-1)
					ClickLvUpBut.setEnabled(false);
				

				for(groundY=0 ; groundY<GROUNDHEIGHT ; groundY++)
				{
					for(groundX=0 ; groundX<GROUNDHEIGHT-(groundY+1) && flag ; groundX++)
					{
						if(ground[groundY][groundX].getBuildLv()!=0 && ground[groundY][groundX].getBuildNum()!=0)
						{
							buildNum = ground[groundY][groundX].getBuildNum();
							ground[groundY][groundX].setBuildNum(0);
							
							for(int l=1 ; l<=buildNum ; l++)
							{
								BuildBuy(ground[groundY][groundX].getBuildLv());
							}
						}
					}
					for(groundX=0 ; groundX<=groundY && !flag ; groundX++)
					{
						if(ground[groundY][groundX].getBuildLv()!=0 && ground[groundY][groundX].getBuildNum()!=0)
						{
							buildNum = ground[groundY][groundX].getBuildNum();
							ground[groundY][groundX].setBuildNum(0);
							
							for(int l=1 ; l<=buildNum ; l++)
							{
								BuildBuy(ground[groundY][groundX].getBuildLv());
							}
						}
						
						if(groundX==GROUNDWIDTH-1)
						{
							flag=true;
						}
					}
				}
				groundX=0;
				groundY=0;
				data.setArbiteLv(0);
				for(int i=1 ; i<=arbiteLv ; i++)
				{
					ArbiteBuy();
				}
				
				data.setAdValue(1);
				for(int i=1 ; i<MAXHIRE ; i++)
				{
					adNum[i] = data.getAdNum(i);
					data.setAdNum(0, i);
					data.setAdPrice(hire[i].getPrice(), i);
					for(int j=0 ; j < adNum[i] ; j++)
						AdBuy(i);
				}

				infoTimer=INFOTIME;
				
				SetStr(0);
				ClickBut.SetImg("Image/ClickBut/"+data.getClickLv()+".png", true);
				
				groundX = 0;
				groundY = 0;
				StartUI.loadSW = false;
				data.setMoney(money);
				UpdateGroundInfo();
			}
			
			contentPane.add(Screen);
			contentPane.remove(Loading);

			BGM.Play("BGM1.wav");
			UpdateGroundInfo();
			
			StartMainThread();
		}
	
		public void StartMainThread()
		{
			mainThread = new Thread()
			{
				public void run()
				{
					while(true)
					{
						try
						{
							if(data.getArbiteLv() > 0)
							{
								arbiteTimer += MAIN_THREAD_TIME_UNIT;
								
								if(arbiteTimer >= (long)data.getArbiteValue())
								{
									arbiteTimer = 0;
									
									data.addMoney(ClickIncrease());
									ClickEffect(EFFARBITE);
								}
							}
							

							if(ivTimer < INVESTDELAY)
							{
								ivTimer += MAIN_THREAD_TIME_UNIT;
								
								IvSubTimer.setText("갱신시간 : " + (Math.round(ivUpdateTimers[currIvIdx] / 1000)) +"초");
							}
							else
							{
								ivTimer = 0;
								
								IvSubTimer.setText("갱신시간 : " + (Math.round(ivUpdateTimers[currIvIdx] / 1000)) +"초");
								
								for(int i=0 ; i < MAXINVEST ;i++)
								{
									if(ivUpdateTimers[i] <= 0)
									{
										ivUpdateTimers[i] = iv[i].getTimer();
											
										ran = random.nextInt(100)+1;
										
										if(ran < iv[i].getProba())
										{
											data.setIvMoney(data.getIvMoney(i)*iv[i].getWinValue(), i);
										}
										else
										{
											data.setIvMoney(data.getIvMoney(i)*iv[i].getLoseValue(), i);
										}
										
										SetStr(0);	
									}
									else if(ivUpdateTimers[i] > 0)
									{
										ivUpdateTimers[i] -= INVESTDELAY;
									}
								}
							}
						
							
							//클릭딜레이
							if(delayFlag == true && delayTimer < CLICKDELAY)
							{
								delayTimer += MAIN_THREAD_TIME_UNIT;
								
								if(delayTimer >= CLICKDELAY)
								{
									delayFlag = false;
									delayTimer = 0;
								}
							}
							
							//정보 스레드 딜레이
							if(infoFlag == true && infoTimer < INFOTIME)
							{
								infoTimer += MAIN_THREAD_TIME_UNIT;
								
								if(infoTimer >= INFOTIME)
								{
									infoFlag = false;
									InfoTxt.setText("");
								}
							}
							
							//에러 메세지 스레드
							if(errFlag == true && errTimer < ERRTIME)
							{
								errTimer += MAIN_THREAD_TIME_UNIT;

								if(errTimer >= ERRTIME)
								{
									errTimer = 0;
									errFlag = false;
									ErrTxt.setText("");
								}
							}

							Thread.sleep(MAIN_THREAD_TIME_UNIT);
						}
						catch (InterruptedException e)
						{
							
						}
					}
				}
			};
			
			mainThread.start();
		}
		
		
		public double ClickIncrease()
		{
			return job[data.getClickLv()].getValue()*data.getClickNum();
		}
		
		public void ClickEffect(int i)
		{
			ClickEffTxt = new Lab(MoneyStr(ClickIncrease())+"+");
			ClickEffTxt.setForeground(Color.white);
			Screen.add(ClickEffTxt, new Integer(4));
			switch(i)
			{
				case 1:
					int ranX = random.nextInt(30)+random.nextInt(30)*-1;
					int ranY = random.nextInt(30)+random.nextInt(30)*-1;
					ClickEffTxt.setFont(new Font(font, fontType, (int)(Screen.getHeight()*0.04)));
					clickEffThr = new EffThread((int)(ClickBut.getX()*0.97)+mouseX+ranX, (int)(ClickBut.getY()*0.8)+mouseY+ranY, 50, ClickEffTxt, Screen);
					break;
				case 2:
					ranX = random.nextInt((int)(ClickBut.getX()*0.3))+random.nextInt((int)(ClickBut.getX()*0.3))*-1;
					ClickEffTxt.setFont(new Font(font, fontType, (int)(Screen.getHeight()*0.04)));
					ClickEffTxt.setForeground(Color.yellow);
					clickEffThr = new EffThread((int)(ClickBut.getX()*0.9)+ranX+(int)(ClickBut.getWidth()*0.5), (int)(ClickBut.getY()*0.8), 50, ClickEffTxt, Screen);
					break;
			}
			clickEffThr.start();
			/*
			effNum++;
			
			if(effNum == 50)
				effNum = 0;*/
		}
		
		public void BuildBuy(int idx)
		{
			if(ground[groundY][groundX].getPrice() <= data.getMoney() && build[idx].getPrice() <= data.getMoney() && ground[groundY][groundX].getBuildNum() < MAXBUILDLV || StartUI.loadSW == true)
			{
				effSound.Play("Buy.wav");
				data.subIncome(ground[groundY][groundX].getIncome());
				
				if(ground[groundY][groundX].buildLv == 0)
				{
					ground[groundY][groundX].setPrice(build[idx].getPrice());
					ground[groundY][groundX].setValue(build[idx].getValue());
					ground[groundY][groundX].setBuildLv(idx);
				}
				data.subMoney(ground[groundY][groundX].getPrice());
				ground[groundY][groundX].addPrice((int)ground[groundY][groundX].getPrice()/10);
				ground[groundY][groundX].addBuildNum(1);
				
				if(ground[groundY][groundX].buildNum == 10 && idx+1 != MAXBUILD)
				{	
					if(ground[groundY][groundX].getBuildLv() == data.getBuildLv())
					{
						data.addBuildLv(1);
						Info("※ "+build[idx+1].getName(0)+"의 건설이 가능합니다");
						effSound.Play("Notice.wav");
					}
					
					ground[groundY][groundX].setPrice(build[ground[groundY][groundX].getBuildLv()].getPrice()*10);
				}
				else if(ground[groundY][groundX].buildNum == 11)
					ground[groundY][groundX].setValue(build[ground[groundY][groundX].getBuildLv()].getValue()*3);
				
				else if(ground[groundY][groundX].buildNum == 20)
					ground[groundY][groundX].setPrice(build[ground[groundY][groundX].getBuildLv()].getPrice()*50);
				
				else if(ground[groundY][groundX].buildNum == 21)
					ground[groundY][groundX].setValue(build[ground[groundY][groundX].getBuildLv()].getValue()*10);
				
				UpdateGroundInfo();

				data.addIncome(ground[groundY][groundX].getIncome());

				SetStr(1);
				
			}
			else if(ground[groundY][groundX].getPrice() > data.getMoney() && ground[groundY][groundX].getBuildNum() != MAXBUILDLV)
				Error("돈이 부족합니다! "+MoneyStr(Math.floor((ground[groundY][groundX].getPrice()-data.getMoney())))+" 부족");
			else if(ground[groundY][groundX].getBuildNum() != MAXBUILDLV)
				Error("돈이 부족합니다! "+MoneyStr(Math.floor((build[idx].getPrice()-data.getMoney())))+" 부족");
			else
				Error("건물의 레벨이 최대입니다!");
		}
		
		public void JobBuy()
		{
			if(data.getClickPrice() <= data.getMoney() || StartUI.loadSW == true)
			{
				effSound.Play("Buy.wav");
				if(data.getClickLv()==0)
				{
					data.addClickLv(1);
					data.addClickNum(-1);
					data.setClickPrice(job[1].getPrice());
					ClickBut.SetImg("Image/ClickBut/"+data.getClickLv()+".png", true);
					Info("※"+job[data.getClickLv()].getName()+"(으)로 취업하였습니다");
				}
				if(data.getClickLv() != MAXJOB-1 && data.getClickNum() == MAXJOBLV-1)
				{
					JobBuyTxt[data.getClickLv()+1].setText(MoneyStr(job[data.getClickLv()+1].getPrice())+"");
					JobBuyTxt[data.getClickLv()].setText("구매완료");
					JobBuyBut[data.getClickLv()].setEnabled(false);
					JobBuyBut[data.getClickLv()+1].setEnabled(true);
					data.subMoney(data.getClickPrice());
					data.setClickPrice(job[data.getClickLv()+1].getPrice());
				}
				
				else if(data.getClickLv() != MAXJOB-1 && data.getClickNum() == MAXJOBLV)
				{
					if(data.getClickPrice() <= data.getMoney() || StartUI.loadSW == true)
					{
						data.setClickNum(0);
						data.addClickLv(1);
						Info("※"+job[data.getClickLv()].getName()+"(으)로 취업하였습니다");
						ClickBut.SetImg("Image/ClickBut/"+data.getClickLv()+".png", true);
						data.subMoney(data.getClickPrice());
						data.setClickPrice(job[data.getClickLv()].getPrice());
						data.addClickPrice(data.getClickPrice()/5);
						JobBuyTxt[data.getClickLv()].setText(MoneyStr(data.getClickPrice())+"");
						
					}
					
					else
					{
						effSound.Stop();
						Error("돈이 부족합니다! "+MoneyStr(Math.floor(data.getClickPrice()-data.getMoney()))+" 부족");
						data.addClickNum(-1);
					}
				}
				else
				{
					data.subMoney(data.getClickPrice());
					data.addClickPrice(data.getClickPrice()/10);
					JobBuyTxt[data.getClickLv()].setText(MoneyStr(data.getClickPrice())+"");
				}

				data.addClickNum(1);
				

				JobInfoIcon.SetImg("Image/Menu/Job/"+data.getClickLv()+".png");
				JobInfoName.setText("직업 : " + job[data.getClickLv()].getName());
				JobInfoValueTxt.setText("클릭당 : " + MoneyStr(ClickIncrease()));
				JobInfoLvTxt.setText(data.getClickLv()+"");
			}
			else
				Error("돈이 부족합니다! "+MoneyStr(Math.floor(data.getClickPrice()-data.getMoney()))+" 부족");
			
		}
		
		public void ArbiteBuy()
		{
			if(data.getArbitePrice() <= data.getMoney() && data.getArbiteLv()!=10 || StartUI.loadSW == true)
			{
				effSound.Play("Buy.wav");
				data.subMoney(data.getArbitePrice());
				data.addArbiteLv(1);

				HireBuyTxt[0].setText(MoneyStr(data.getArbitePrice())+"");
				
				if(data.getArbiteLv()==1)
				{
					data.setArbiteValue(hire[0].getValue()*1000);
				}
				else if(data.getArbiteLv()==10)
				{
					data.setArbiteValue(data.getArbiteValue()-500);
					HireBuyTxt[0].setText("최대레벨");
					HireValue[0].setText("현재 "+data.getArbiteValue()/1000+"초마다 자동클릭");
					HireBuyBut[0].setEnabled(false);
				}
					
				else
					data.setArbiteValue(data.getArbiteValue()-500);
				
				if(data.getArbiteLv()!=10)
					HireValue[0].setText((data.getArbiteValue()-500)/1000+"초마다 자동클릭");
				


			}
			else
				Error("돈이 부족합니다! "+MoneyStr(Math.floor(data.getArbitePrice()-data.getMoney()))+" 부족");
		}
			
		public void AdBuy(int idx)
		{
			if(data.getAdPrice(idx) <= data.getMoney() || StartUI.loadSW == true)
			{
				effSound.Play("Buy.wav");
				data.subMoney(data.getAdPrice(idx));
				data.setAdPrice(data.getAdPrice(idx)*1.5, idx);
				data.AddAdNum(idx);
				switch(idx)
				{
					case 1:
						data.AddAdValue(0.01);
						break;
					case 2:
						data.AddAdValue(0.05);
						break;
					case 3:
						data.AddAdValue(0.1);
						break;
					case 4:
						data.AddAdValue(0.25);
						break;
					case 5:
						data.AddAdValue(0.50);
						break;
				}
				SetStr(1);
				HireBuyTxt[idx].setText(MoneyStr(data.getAdPrice(idx))+"");
				UpdateGroundInfo();
			}
			else
				Error("돈이 부족합니다! "+MoneyStr(Math.floor(data.getAdPrice(idx)-data.getMoney()))+" 부족");
		}
		
		
	
		
		public void GroundSell()
		{
			data.subIncome(ground[groundY][groundX].getIncome());
			data.addMoney(ground[groundY][groundX].SellPrice());
			SetStr(1);
			
			BuildValue[ground[groundY][groundX].getBuildLv()].setText("초당 "+MoneyStr(build[ground[groundY][groundX].getBuildLv()].getValue()*data.getAdValue())+" 획득");
			BuildBuyTxt[ground[groundY][groundX].getBuildLv()].setText("구매불가");
			BuildName[ground[groundY][groundX].getBuildLv()].setText(build[ground[groundY][groundX].getBuildLv()].getName(0)+" 건설");
			BuildIcon[ground[groundY][groundX].getBuildLv()].SetImg("Image/Menu/Build/"+ground[groundY][groundX].getBuildLv()+"_1.png");
			
			ground[groundY][groundX].setPrice(0);
			ground[groundY][groundX].setBuildLv(0);
			ground[groundY][groundX].setBuildNum(0);
			ground[groundY][groundX].setValue(0);
			UpdateGroundInfo();
			effSound.Play("Sell.wav");
		}
		
		
		public void SetComponunt() // 숫자가 높을수록 더 앞에옴 0배경
		{
			Screen.add(BGImg, new Integer(0));
			Screen.add(ClickBut, new Integer(1));
			Screen.add(MenuBarPan, new Integer(1));
			Screen.add(MenuLayout, new Integer(1));
			Screen.add(BuildMenu, new Integer(2));
			Screen.add(JobMenu, new Integer(2));
			Screen.add(HireMenu, new Integer(2));
			Screen.add(IvMenu, new Integer(2));
			Screen.add(ErrTxt, new Integer(3));
			Screen.add(InfoTxt, new Integer(3));
			Screen.add(IvSubMenu, new Integer(4));
			Screen.add(OptionWindow ,new Integer(5));
			
			ClickBut.setBounds((int)(width*0.515),(int)(height*0.78),(int)(width*0.475),(int)(height*0.2));

			
		}
		public void UpdateInfo()
		{
			MoneyTxt.setText(MoneyStr(Math.floor(data.getMoney()))+"");
			ClickLvUpBut.setText("레벨업("+MoneyStr(job[data.getClickLv()].getPrice())+")");
			MoneyStr(data.getMoney());
		}
		
		
		public void UpdateGroundInfo()
		{
			int lv = (ground[groundY][groundX].getBuildNum()+9)/10;
			
			if(ground[groundY][groundX].getBuildLv() == 0)
			{
				for(int i=STARTBUILDLINE ; i<=data.getBuildLv() && i<MAXBUILD ; i++)
				{
					BuildValue[i].setText("초당 "+MoneyStr(build[i].getValue()*data.getAdValue())+" 획득");
					BuildBuyTxt[i].setText(MoneyStr(build[i].getPrice())+"");
					BuildBuyBut[i].setEnabled(true);
					GroundSellBut.setEnabled(false);
				}
			}
			else
			{
				for(int i=STARTBUILDLINE ; i<=data.getBuildLv() && i<MAXBUILD ; i++)
				{
					BuildValue[i].setText("초당 "+MoneyStr(build[i].getValue()*data.getAdValue())+" 획득");
					BuildBuyTxt[i].setText("구매불가");
					BuildBuyBut[i].setEnabled(false);
					GroundSellBut.setEnabled(true);
				}
				BuildValue[ground[groundY][groundX].getBuildLv()].setText("초당 "+MoneyStr(ground[groundY][groundX].getValue()*data.getAdValue())+" 획득");

				if(ground[groundY][groundX].getBuildNum() < MAXBUILDLV)
				{
					BuildBuyBut[ground[groundY][groundX].getBuildLv()].setEnabled(true);
					BuildBuyTxt[ground[groundY][groundX].getBuildLv()].setText(MoneyStr(ground[groundY][groundX].getPrice())+"");
				}
				else
				{
					BuildBuyBut[ground[groundY][groundX].getBuildLv()].setEnabled(false);
					BuildBuyTxt[ground[groundY][groundX].getBuildLv()].setText("최대레벨");
				}
			}
			if(pastGroundX != GROUNDWIDTH && ground[pastGroundY][pastGroundX].getBuildLv() != 0)
			{
				BuildName[ground[pastGroundY][pastGroundX].getBuildLv()].setText(build[ground[pastGroundY][pastGroundX].getBuildLv()].getName(0)+" 건설");
				BuildIcon[ground[pastGroundY][pastGroundX].getBuildLv()].SetImg("Image/Menu/Build/"+ground[pastGroundY][pastGroundX].getBuildLv()+"_1.png");
			}
			if(ground[groundY][groundX].getBuildLv()==0)
				GroundInfoName.setText("건물: 빈 땅");
			else
			{
				GroundInfoName.setText("건물:"+build[ground[groundY][groundX].getBuildLv()].getName(lv-1));
				BuildName[ground[groundY][groundX].getBuildLv()].setText(build[ground[groundY][groundX].getBuildLv()].getName(lv-1)+" 건설");
				BuildIcon[ground[groundY][groundX].getBuildLv()].SetImg("Image/Menu/Build/"+ground[groundY][groundX].getBuildLv()+"_"+lv+".png");
			}
			
			if(ground[groundY][groundX].buildNum == 10)
			{
				BuildName[ground[groundY][groundX].getBuildLv()].setText(build[ground[groundY][groundX].getBuildLv()].getName(lv)+" 건설");
				BuildValue[ground[groundY][groundX].getBuildLv()].setText("초당 "+MoneyStr(build[ground[groundY][groundX].getBuildLv()].getValue()*3*data.getAdValue())+" 획득");
				BuildIcon[ground[groundY][groundX].getBuildLv()].SetImg("Image/Menu/Build/"+ground[groundY][groundX].getBuildLv()+"_"+(lv+1)+".png");
			}
			else if(ground[groundY][groundX].buildNum == 20)
			{
				BuildName[ground[groundY][groundX].getBuildLv()].setText(build[ground[groundY][groundX].getBuildLv()].getName(lv)+" 건설");
				BuildValue[ground[groundY][groundX].getBuildLv()].setText("초당 "+MoneyStr(build[ground[groundY][groundX].getBuildLv()].getValue()*10*data.getAdValue())+" 획득");
				BuildIcon[ground[groundY][groundX].getBuildLv()].SetImg("Image/Menu/Build/"+ground[groundY][groundX].getBuildLv()+"_"+(lv+1)+".png");
			}
				
			GroundBuild[groundY][groundX].SetImg("Image/Ground/Build/"+ground[groundY][groundX].getBuildLv()+"_"+lv+".png");
			GroundInfoIcon.SetImg("Image/Menu/Build/"+ground[groundY][groundX].getBuildLv()+"_"+lv+".png");
			
			GroundInfoIncomeTxt.setText(""+MoneyStr(Math.floor(ground[groundY][groundX].getIncome()*data.getAdValue())));
			GroundInfoBuildNTxt.setText(""+ground[groundY][groundX].getBuildNum());
			GroundInfoLVTxt.setText(""+(ground[groundY][groundX].getBuildLv()));
			GroundSellTxt.setText(""+MoneyStr(ground[groundY][groundX].SellPrice()));
			GroundBorder.setBounds(Ground[groundY][groundX].getX(), GroundLine[groundY].getY(), GroundBorder.getWidth(), GroundBorder.getHeight());
			
			pastGroundX = groundX;
			pastGroundY = groundY;
			repaint();
		}
		
		public void UISetting()
		{
			
			BGImg.setBounds(0, 0,Screen.getWidth(),Screen.getHeight());
			MenuBG.setBounds(0, 0, MenuLayout.getWidth(), MenuLayout.getHeight());
			OptionBG.setBounds(0, 0, OptionWindow.getWidth(), OptionWindow.getHeight());
			OptionLayoutBut.setBounds(0, 0, OptionWindow.getWidth(), OptionWindow.getHeight());
			
			ErrTxt.setBounds((int)(width*0.5), (int)(height*0.135), (int)(width*0.5), (int)(height*0.05));
			ErrTxt.setFont(new Font(font, fontType, (int)(Screen.getHeight()*0.04)));
			ErrTxt.setForeground(Color.red);
			
			InfoTxt.setBounds(ClickBut.getX(), ClickBut.getY()-(int)(ClickBut.getHeight()*0.3), (int)(ClickBut.getWidth()*0.99), (int)(ClickBut.getHeight()*0.3));
			InfoTxt.setFont(new Font(font, fontType, (int)(Screen.getHeight()*0.032)));
			InfoTxt.setForeground(Color.yellow);
			
			ClickBut.SetImg("Image/ClickBut/"+data.getClickLv()+".png", true);
			BGImg.SetImg("Image/BackGround.png");
			OptionBG.SetImg("Image/OptionFrame.png");
			MenuBG.SetImg("Image/MenuBG.png");
			
			MenuLayout.add(MenuBG, new Integer(0));
			OptionWindow.add(OptionBG, new Integer(0));
			OptionWindow.add(OptionLayoutBut, new Integer(0));
		}


		public void DrawMenuUI()
		{
			//이미지 받으면 배열 생성하고 이미지 각자 만드는 작업 필요 
			
			MenuLayout.setBounds((int)(Screen.getWidth()*0.01), (int)(Screen.getHeight()*0.11), (int)(Screen.getWidth()*0.5), (int)(Screen.getHeight()*0.87));
			MenuLayout.setLayout(null);

			
			//------- 건물메뉴
			for(int i=STARTBUILDLINE ; i<MAXBUILD ; i++)
			{
				BuildDataPan[i] = new JPanel();
				BuildIcon[i] = new Lab();
				BuildName[i] = new Lab(build[i].getName(0)+" 건설");
				BuildValue[i] = new Lab("초당 ??? 원 획득");
				BuildBuyPan[i] = new LayPan();
				BuildBuyBut[i] = new But();
				BuildBuyTxt[i] = new Lab("구매불가", SwingConstants.CENTER);
				
				ui.DrawMenuUI(MenuLayout, BuildMenu, BuildDataPan[i], BuildIcon[i], BuildName[i], BuildValue[i], BuildBuyPan[i], BuildBuyBut[i], BuildBuyTxt[i], STARTBUILDLINE, i);

				BuildIcon[i].SetImg("Image/Menu/Build/"+i+"_1.png");
				BuildBuyBut[i].SetImg("Image/Menu/BuyBut.png", true);
			}
			//------- 직업메뉴
			for(int i=STARTJOBLINE ; i<MAXJOB ; i++)
			{
				JobDataPan[i] = new JPanel();
				JobIcon[i] = new Lab();
				JobName[i] = new Lab(job[i].getName()+"으로 취업합니다");
				JobValue[i] = new Lab("클릭당 "+MoneyStr(job[i].getValue())+" 획득");
				JobBuyPan[i] = new LayPan();
				JobBuyBut[i] = new But();
				JobBuyTxt[i] = new Lab("구매불가", SwingConstants.CENTER);
				
				ui.DrawMenuUI(MenuLayout, JobMenu, JobDataPan[i], JobIcon[i], JobName[i], JobValue[i], JobBuyPan[i], JobBuyBut[i], JobBuyTxt[i], STARTJOBLINE, i);

				JobIcon[i].SetImg("Image/Menu/Job/"+i+".png");
				JobBuyBut[i].SetImg("Image/Menu/BuyBut.png", true);
			}
			ui.DrawJobInfoUI(MenuLayout, JobInfoMenu, JobInfoIcon, JobInfoName, JobInfoValueTxt, JobInfoLvTxt, JobInfoValue, JobInfoLv);

			//------- 고용메뉴
			for(int i=0 ; i<MAXHIRE ; i++)
			{
				HireDataPan[i] = new JPanel();
				HireIcon[i] = new Lab();
				HireName[i] = new Lab(hire[i].getName());
				HireValue[i] = new Lab();
				HireBuyPan[i] = new LayPan();
				HireBuyBut[i] = new But();
				HireBuyTxt[i] = new Lab(MoneyStr(hire[i].getPrice())+"", SwingConstants.CENTER);
				
				ui.DrawMenuUI(MenuLayout, HireMenu, HireDataPan[i], HireIcon[i], HireName[i], HireValue[i], HireBuyPan[i], HireBuyBut[i], HireBuyTxt[i], 0, i);

				if(i==0)
					HireValue[i].setText(hire[i].getValue()+"초마다 자동클릭");
				else
					HireValue[i].setText("건물 초당수익 "+DoubleStr(hire[i].getValue())+"%+");
				
				HireIcon[i].SetImg("Image/Menu/Hire/"+i+".png");
				HireBuyBut[i].SetImg("Image/Menu/BuyBut.png", true);
				HireBuyBut[i].setEnabled(true);			
			}

			ui.DrawGroundInfoUI(MenuLayout, GroundInfoMenu, GroundInfoIcon, GroundInfoName, GroundInfoIncomeTxt, GroundInfoBuildNTxt, GroundInfoLVTxt, GroundSellPan, GroundSellBut, GroundSellTxt, GroundIncomeIcon, GroundPrcIcon, GroundLvIcon);			
		}
		
		public void DrawIvSubMenu()
		{
			IvSubMenu.setVisible(false);
			IvSubMenu.setBounds((int)(width*0.3), (int)(height*0.2), (int)(width*0.4), (int)(height*0.6));
			
			IvSubLayoutBut.setBounds(0, 0, IvSubMenu.getWidth(), IvSubMenu.getHeight());
			IvSubName.setBounds((int)(IvSubMenu.getWidth()*0.05), (int)(IvSubMenu.getHeight()*0.05), (int)(IvSubMenu.getWidth()*0.9), (int)(IvSubMenu.getHeight()*0.1));
			IvSubMoney.setBounds((int)(IvSubMenu.getWidth()*0.05), (int)(IvSubMenu.getHeight()*0.18), (int)(IvSubMenu.getWidth()*0.9), (int)(IvSubMenu.getHeight()*0.1));
			IvSubTimer.setBounds((int)(IvSubMenu.getWidth()*0.05), (int)(IvSubMenu.getHeight()*0.31), (int)(IvSubMenu.getWidth()*0.9), (int)(IvSubMenu.getHeight()*0.1));
			IvSubExitBut.setBounds((int)(IvSubMenu.getWidth()*0.924), (int)(IvSubMenu.getHeight()*0.006), (int)(IvSubMenu.getHeight()*0.085),(int)(IvSubMenu.getHeight()*0.085));
			
			IvSubInfo.setBounds(0, 0, (int)(IvSubMenu.getWidth()*0.35), (int)(IvSubMenu.getHeight()*0.5));
			IvSubInfoWinLateStr.setBounds(0, (int)(IvSubInfo.getHeight()*0.03), (int)(IvSubInfo.getWidth()), (int)(IvSubInfo.getHeight()*0.2));
			IvSubInfoProbaTxt.setBounds(0, (int)(IvSubInfo.getHeight()*0.15), (int)(IvSubInfo.getWidth()), (int)(IvSubInfo.getHeight()*0.2));
			IvSubInfoWinValueTxt.setBounds(0, (int)(IvSubInfo.getHeight()*0.3), (int)(IvSubInfo.getWidth()), (int)(IvSubInfo.getHeight()*0.2));
			IvSubInfoWinMoneyTxt.setBounds(0, (int)(IvSubInfo.getHeight()*0.44), (int)(IvSubInfo.getWidth()), (int)(IvSubInfo.getHeight()*0.2));
			IvSubInfoLoseValueTxt.setBounds(0, (int)(IvSubInfo.getHeight()*0.6), (int)(IvSubInfo.getWidth()), (int)(IvSubInfo.getHeight()*0.2));
			IvSubInfoLoseMoneyTxt.setBounds(0, (int)(IvSubInfo.getHeight()*0.74), (int)(IvSubInfo.getWidth()), (int)(IvSubInfo.getHeight()*0.2));
			
			IvSubBuyPan.setBounds((int)(IvSubMenu.getWidth()*0.15), (int)(IvSubMenu.getHeight()*0.47), (int)(IvSubMenu.getWidth()*0.3), (int)(IvSubMenu.getHeight()*0.19));
			IvSubSellPan.setBounds((int)(IvSubMenu.getWidth()*0.55), (int)(IvSubMenu.getHeight()*0.47), (int)(IvSubMenu.getWidth()*0.3), (int)(IvSubMenu.getHeight()*0.19));
			
			IvSubBuyBut.setBounds(0, 0, IvSubBuyPan.getWidth(), IvSubBuyPan.getHeight());
			IvSubSellBut.setBounds(0, 0, IvSubSellPan.getWidth(), IvSubSellPan.getHeight());
	
			IvSubBuyTxt.setBounds(0, (int)(IvSubBuyPan.getHeight()*0.65), IvSubBuyPan.getWidth(),(int)(IvSubBuyPan.getHeight()*0.26));
			IvSubSellTxt.setBounds(0, (int)(IvSubSellPan.getHeight()*0.65), IvSubSellPan.getWidth(),(int)(IvSubSellPan.getHeight()*0.26));

			IvSubInfo.add(IvSubInfoProbaTxt);
			IvSubInfo.add(IvSubInfoWinLateStr);
			IvSubInfo.add(IvSubInfoWinValueTxt);
			IvSubInfo.add(IvSubInfoLoseValueTxt);
			IvSubInfo.add(IvSubInfoWinMoneyTxt);
			IvSubInfo.add(IvSubInfoLoseMoneyTxt);
			
			IvSubInfoProbaTxt.setForeground(Color.white);
			IvSubInfoWinLateStr.setForeground(Color.white);
			IvSubInfoWinValueTxt.setForeground(Color.white);
			IvSubInfoLoseValueTxt.setForeground(Color.white);
			IvSubInfoWinMoneyTxt.setForeground(Color.white);
			IvSubInfoLoseMoneyTxt.setForeground(Color.white);
			
			
			
			Screen.add(IvSubInfo, new Integer(4));
			
			IvSubInfo.SetImg("Image/Menu/Invest/InfoBG.png");
			
			IvSubBuyPan.add(IvSubBuyBut, new Integer(0));
			IvSubBuyPan.add(IvSubBuyTxt, new Integer(1));
			IvSubSellPan.add(IvSubSellBut, new Integer(0));
			IvSubSellPan.add(IvSubSellTxt, new Integer(1));
			
			IvSubMenu.add(IvSubName);
			IvSubMenu.add(IvSubMoney);
			IvSubMenu.add(IvSubTimer);
			
			IvSubMenu.add(IvSubExitBut);

			IvSubMenu.add(IvSubBuyPan);
			IvSubMenu.add(IvSubSellPan);
			
			IvSubInfo.setVisible(false);

			IvSubInfoWinLateStr.setFont(new Font(font, fontType, (int)(IvSubInfo.getHeight()*0.1)));
			IvSubInfoProbaTxt.setFont(new Font(font, fontType, (int)(IvSubInfo.getHeight()*0.1)));
			IvSubInfoWinValueTxt.setFont(new Font(font, fontType, (int)(IvSubInfo.getHeight()*0.08)));
			IvSubInfoLoseValueTxt.setFont(new Font(font, fontType, (int)(IvSubInfo.getHeight()*0.08)));
			IvSubInfoWinMoneyTxt.setFont(new Font(font, fontType, (int)(IvSubInfo.getHeight()*0.1)));
			IvSubInfoLoseMoneyTxt.setFont(new Font(font, fontType, (int)(IvSubInfo.getHeight()*0.1)));

			IvSubBuyTxt.setFont(new Font(font, fontType, (int)(IvSubBuyPan.getHeight()*0.20)));
			IvSubSellTxt.setFont(new Font(font, fontType, (int)(IvSubSellPan.getHeight()*0.28)));
			IvSubBuyTxt.setForeground(Color.white);
			
			IvSubName.setFont(new Font(font, fontType, (int)(MenuBarPan.getHeight()*0.6)));
			IvSubMoney.setFont(new Font(font, fontType, (int)(MenuBarPan.getHeight()*0.4)));
			IvSubTimer.setFont(new Font(font, fontType, (int)(MenuBarPan.getHeight()*0.4)));
			
			for(int i=0 ; i<MAXINVEST ; i++)
			{
				ivUpdateTimers[i] = iv[i].getTimer();
				IvSubBut[i] = new But();
				IvSubBut[i].setBounds((IvSubMenu.getWidth()/MAXINVEST*i)+(int)(IvSubMenu.getWidth()*0.05), (int)(IvSubMenu.getHeight()*0.69), (int)(IvSubMenu.getHeight()*0.25), (int)(IvSubMenu.getHeight()*0.25));
				IvSubBut[i].SetImg("Image/Menu/Invest/"+i+".png", false);
				IvSubBut[i].addActionListener(this);
				IvSubBut[i].addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent e)
					{
						for(int j=0 ; j<MAXINVEST ; j++)
						{
							if(e.getSource()==IvSubBut[j])
							{
								ivIdxMouse=j;
								IvSubInfoProbaTxt.setText(iv[ivIdxMouse].getProba()+"%");
								IvSubInfoWinValueTxt.setText("성공시 "+(int)iv[ivIdxMouse].getWinValue() + "배");
								IvSubInfoLoseValueTxt.setText("실패시 "+(int)((1-iv[ivIdxMouse].getLoseValue())*100) + "% 감소");
								IvSubInfoWinMoneyTxt.setText(MoneyStr(data.getIvMoney(ivIdxMouse)*iv[ivIdxMouse].getWinValue())+"");
								IvSubInfoLoseMoneyTxt.setText(MoneyStr(data.getIvMoney(ivIdxMouse)*iv[ivIdxMouse].getLoseValue())+"");
								IvSubInfo.setBounds(IvSubMenu.getX() + IvSubBut[j].getX() + IvSubBut[j].getWidth(), IvSubMenu.getY() + IvSubBut[j].getY() - (IvSubBut[j].getHeight()+(IvSubInfo.getHeight()-IvSubBut[j].getHeight()*2)), IvSubInfo.getWidth(), IvSubInfo.getHeight());
								IvSubInfo.setVisible(true);
							}
						}
					}
					
					public void mouseExited(MouseEvent e)
					{
						for(int j=0 ; j<MAXINVEST ; j++)
						{
							if(e.getSource()==IvSubBut[j])
							{
								IvSubInfo.setVisible(false);
							}
						}
					}
				});
				IvSubMenu.add(IvSubBut[i]);
			}
			
			IvSubMenu.SetImg("Image/MenuBG.png");
			IvSubExitBut.SetImg("Image/Menu/ExitBut.png", false);
			IvSubBuyBut.SetImg("Image/Menu/Invest/BuyBut.png", true);
			IvSubSellBut.SetImg("Image/Menu/Invest/SellBut.png", true);
			IvSubMenu.add(IvSubLayoutBut);
		}
		
		public void IvBuy()
		{
			/*if(StartUI.loadSW == true)
			{*/
			if(data.getIvMoney(currIvIdx)==0 && data.getMoney() != 0)
			{
				effSound.Play("Press.wav");
				data.setIvMoney(data.getMoney()*iv[currIvIdx].getDecre(), currIvIdx);
				data.subMoney(data.getMoney()*iv[currIvIdx].getDecre());
				SetStr(0);
			}
			else if(data.getIvMoney(currIvIdx)!=0 && data.getMoney() != 0)
				Error("이미 해당종목에 투자하였습니다");
			else if(data.getMoney() == 0)
				Error("재산이 없습니다");
				
		}
		public void IvSell()
		{
			if(data.getIvMoney(currIvIdx)>0)
			{
				effSound.Play("Press.wav");
				data.addMoney(data.getIvMoney(currIvIdx));
				data.setIvMoney(0, currIvIdx);
				SetStr(0);
			}
			else
				Error("투자한 금액이 없습니다");
		}
		
		public void ShowSubIv(int idx)
		{
			IvSubName.setText(iv[idx].getName()+"");
			SetStr(0);
			IvSubTimer.setText("갱신시간 : " + (Math.round(ivUpdateTimers[idx] / 1000)) +"초");
			
			IvSubBuyTxt.setText("자금의 "+DoubleStr(iv[idx].getDecre()*100)+"%");
			
		}


		public void DrawGround()
		{
			GroundBuildField.setBounds((int)(Screen.getWidth()*0.535), (int)(Screen.getHeight()*0.07), (int)(Screen.getWidth()*0.46), (int)(Screen.getHeight()*0.65));
			GroundField.setBounds(0, (int)(GroundBuildField.getHeight()*0.3), GroundBuildField.getWidth(), (int)(GroundBuildField.getHeight()*0.7));
			
			GroundBuildField.setLayout(null);
			GroundField.setLayout(null);
			GroundBuildField.setOpaque(false);
			GroundField.setOpaque(false);

			boolean flag = false;
			
			for(int i=0 ; i<GROUNDHEIGHT ; i++)
			{
				GroundLine[i] = new JPanel();
				GroundBuildLine[i] = new JPanel();
				
				GroundLine[i].setOpaque(false);
				GroundLine[i].setLayout(null);
				GroundBuildLine[i].setOpaque(false);
				GroundBuildLine[i].setLayout(null);
				
				
				for(int j=0 ; j<GROUNDHEIGHT-(i+1) && flag ; j++)
				{
					ground[i][j] = new Data_Ground(0, 0);
					ground[i][j].setName(0);
					ground[i][j].setValue(0);
					ground[i][j].setPrice(build[0].getPrice());
					
					Ground[i][j] = new GroundBut();
					GroundBuild[i][j] = new Lab();

					GroundLine[i].add(Ground[i][j], new Integer(0));
					GroundBuildLine[i].add(GroundBuild[i][j]);
					Ground[i][j].addActionListener(this);
					
					if(j == GROUNDHEIGHT-(i+2))
						groundMaxWidth[i] = j;
				}

				for(int j=0 ; j<=i && !flag ; j++)
				{
					ground[i][j] = new Data_Ground(0, 0);
					ground[i][j].setName(0);
					ground[i][j].setValue(0);
					ground[i][j].setPrice(build[0].getPrice());
					
					Ground[i][j] = new GroundBut();
					GroundBuild[i][j] = new Lab();
					
					GroundLine[i].add(Ground[i][j], new Integer(0));
					GroundBuildLine[i].add(GroundBuild[i][j]);
					Ground[i][j].addActionListener(this);
					
					if(j==i)
						groundMaxWidth[i] = j;
					
					if(j==GROUNDWIDTH-1)
					{
						flag=true;
					}
				}
				GroundField.add(GroundLine[i], new Integer(0));
				GroundBuildField.add(GroundBuildLine[i], new Integer(i));

				
			}
			
			SetGroundSize(0.8);
			
			GroundBuildField.add(GroundField);
			Screen.add(GroundBuildField, new Integer(1));

			GroundField.add(GroundBorder, new Integer(2));
			GroundBorder.SetImg("Image/ground/GroundBorder.png");

		}
		
		public void SetGroundSize(double size)
		{
			boolean flag = false;
			
			for(int i=0 ; i<GROUNDHEIGHT ; i++)
			{
				GroundLine[i].setBounds(0, (int)(i*(GroundField.getHeight()/(GROUNDHEIGHT))),(int)(GroundField.getWidth()), (int)(GroundField.getHeight()/((GROUNDHEIGHT)/2)));
			
				for(int j=0 ; j<GROUNDHEIGHT-(i+1) && flag ; j++)
				{
					for(int l=0 ; l<=groundMaxWidth[i] ; l++)
					{
						Ground[i][l].setBounds((int)((GroundLine[i].getWidth()/2)-(int)(GroundLine[i].getWidth()/GROUNDWIDTH)/2)-((int)((int)(GroundLine[i].getWidth()/GROUNDWIDTH))*groundMaxWidth[i]/2)+((int)((int)(GroundLine[i].getWidth()/GROUNDWIDTH))*l), 0,(int)(GroundLine[i].getWidth()/GROUNDWIDTH),GroundLine[i].getHeight());
						GroundBuild[i][l].setBounds(Ground[i][l].getX(), (int)(GroundLine[i].getY()+GroundField.getY()+GroundLine[i].getHeight()*-0.25), (int)(GroundLine[i].getWidth()/GROUNDWIDTH), GroundLine[i].getHeight());
					}
				}
				for(int j=0 ; j<=i && !flag ; j++)
				{
					for(int l=0 ; l<=groundMaxWidth[i] ; l++)
					{
						Ground[i][l].setBounds((int)((GroundLine[i].getWidth()/2)-(int)(GroundLine[i].getWidth()/GROUNDWIDTH)/2)-((int)((int)(GroundLine[i].getWidth()/GROUNDWIDTH))*groundMaxWidth[i]/2)+((int)((int)(GroundLine[i].getWidth()/GROUNDWIDTH))*l), 0,(int)(GroundLine[i].getWidth()/GROUNDWIDTH),GroundLine[i].getHeight());
						GroundBuild[i][l].setBounds(Ground[i][l].getX(), (int)(GroundLine[i].getY()+GroundField.getY()+GroundLine[i].getHeight()*-0.25), (int)(GroundLine[i].getWidth()/GROUNDWIDTH), GroundLine[i].getHeight());
					}
					
					if(j==GROUNDWIDTH-1)
					{
						flag=true;
					}
				}
			}

			ResizeGround(size);
		}
		
		public void ResizeGround(double size)
		{
			boolean flag = false;
			for(int i=0 ; i<GROUNDHEIGHT ; i++)
			{
				for(int j=0 ; j<GROUNDHEIGHT-(i+1) && flag ; j++)
				{
					Ground[i][j].setSize((int)(Ground[i][j].getWidth()*size),(int)(Ground[i][j].getHeight()*size));
					GroundBuildLine[i].setBounds(0,GroundBuild[i][j].getY()-(int)(Ground[i][j].getHeight()*1.66), GroundBuildField.getWidth(), (int)(Ground[i][j].getHeight())*3);
					GroundBuild[i][j].setBounds(Ground[i][j].getX(), 0,(int)(Ground[i][j].getWidth()),(int)(Ground[i][j].getHeight())*3);
					Ground[i][j].SetImg("Image/ground/Ground.png", false);
				}


				for(int j=0 ; j<=i && !flag ; j++)
				{
					Ground[i][j].setSize((int)(Ground[i][j].getWidth()*size),(int)(Ground[i][j].getHeight()*size));
					GroundBuildLine[i].setBounds(0,GroundBuild[i][j].getY()-(int)(Ground[i][j].getHeight()*1.66), GroundBuildField.getWidth(), (int)(Ground[i][j].getHeight())*3);
					GroundBuild[i][j].setBounds(Ground[i][j].getX(), 0,(int)(Ground[i][j].getWidth()),(int)(Ground[i][j].getHeight())*3);
					Ground[i][j].SetImg("Image/ground/Ground.png", false);
					
					if(j==GROUNDWIDTH-1)
						flag=true;
				}
			}
			GroundBorder.setBounds(Ground[groundY][groundX].getX(), GroundLine[groundY].getY(), Ground[0][0].getWidth(), Ground[0][0].getHeight());
			
		}
		
		public void DrawMenuBarUI()
		{
			MenuBarPan.setBounds((int)(width*0.01),(int)(height*0.02),(int)(width*0.99),(int)(height*0.09));
			MenuBarPan.setLayout(null);
			
			MenuBarPan.add(BuildMenuBut, new Integer(1));
			MenuBarPan.add(JobMenuBut, new Integer(1));
			MenuBarPan.add(HireMenuBut, new Integer(1));
			MenuBarPan.add(IvMenuBut, new Integer(1));
			MenuBarPan.add(CashInfoLab, new Integer(2));
			
			MenuBarPan.add(OptionBut, new Integer(1));
			MenuBarPan.add(MoneyTxt, new Integer(3));
			MenuBarPan.add(MoneyIncomeTxt, new Integer(3));
			MenuBarPan.add(MoneyTxtBG, new Integer(1));

			int buttonWidth = (int)(MenuBarPan.getWidth()*0.127);
			
			BuildMenuBut.setBounds(0,0,buttonWidth,MenuBarPan.getHeight());
			JobMenuBut.setBounds(buttonWidth*1,0,buttonWidth,MenuBarPan.getHeight());
			HireMenuBut.setBounds(buttonWidth*2,0,buttonWidth,MenuBarPan.getHeight());
			IvMenuBut.setBounds(buttonWidth*3,0,buttonWidth,MenuBarPan.getHeight());
			CashInfoLab.setBounds((int)(MenuBarPan.getWidth()*0.54),0,MenuBarPan.getHeight(),MenuBarPan.getHeight());
			OptionBut.setBounds((int)(MenuBarPan.getWidth()*0.94),0,MenuBarPan.getWidth()/20,MenuBarPan.getHeight());
			MoneyTxt.setBounds((int)(MenuBarPan.getWidth()*0.538),(int)(MenuBarPan.getHeight()*0.05),MenuBarPan.getWidth()/6,MenuBarPan.getHeight());
			MoneyIncomeTxt.setBounds((int)(MenuBarPan.getWidth()*0.7),(int)(MenuBarPan.getHeight()*0.05),(int)(MenuBarPan.getWidth()*0.19),MenuBarPan.getHeight());
			MoneyTxtBG.setBounds((int)(MenuBarPan.getWidth()*0.56),(int)(MenuBarPan.getHeight()*0.25),(int)(MenuBarPan.getWidth()*0.35),(int)(MenuBarPan.getHeight()*0.62));
			
			BuildMenuBut.SetImg("Image/MenuBar/Build.png", true);
			JobMenuBut.SetImg("Image/MenuBar/Job.png", true);
			HireMenuBut.SetImg("Image/MenuBar/Hire.png", true);
			IvMenuBut.SetImg("Image/MenuBar/Invest.png", true);
			OptionBut.SetImg("Image/MenuBar/OptionBut.png", true);
			
			//ui.SetImg(GoldInfoLab, "Image/GoldInfo.png");
			CashInfoLab.SetImg("Image/MenuBar/CashIcon.png");
			
			
			MoneyTxt.setFont(new Font(font, fontType, (int)(MenuBarPan.getHeight()*0.3)));
			MoneyTxt.setForeground(Color.white);
			MoneyIncomeTxt.setFont(new Font(font, fontType, (int)(MenuBarPan.getHeight()*0.3)));
			MoneyIncomeTxt.setForeground(Color.white);
			MoneyTxtBG.setBackground(new Color(0,0,0,55));
			MenuBarPan.setOpaque(false);
			MenuLayout.setOpaque(false);
			/*
			MenuBarPan.add(GoldInfoLab, new Integer(1));
			GoldInfoLab.setBounds((int)(MenuBarPan.getWidth()*0.74),0,MenuBarPan.getHeight(),MenuBarPan.getHeight());	
			GoldInfoLab.SetImg("Image/MenuBar/GoldIcon.png");
			*/
		}
		
		
		public void EventButton()	//버튼의 이벤트 메소드 모음
		{
			ClickBut.addActionListener((e) -> {
				ran = (int)(Math.random()*3)+1;
				if(!delayFlag)
				{
					effSound.Play("Click"+ran+".wav");
					ClickDelay();
					data.addMoney(ClickIncrease());
					ClickEffect(EFFNORMAL);
				}
			});
			//알바 클릭했을때
			
			ClickLvUpBut.addActionListener((e) -> {
				
			});
			//알바 레벨업 클릭했을때
			
			
			BuildMenuBut.addActionListener((e) -> {
				effSound.Play("Press.wav");
				ui.ShowMenu(1);
			});
			
			JobMenuBut.addActionListener((e) -> {
				effSound.Play("Press.wav");
				ui.ShowMenu(2);
			});
			
			HireMenuBut.addActionListener((e) -> {
				effSound.Play("Press.wav");
				ui.ShowMenu(3);
			});
			
			IvMenuBut.addActionListener((e) -> {
				effSound.Play("Press.wav");
				if(ivSubFlag)
				{
					ivSubFlag = false;
					IvSubMenu.setVisible(ivSubFlag);
				}
				else
				{
					ivSubFlag = true;
					IvSubMenu.setVisible(ivSubFlag);
				}
			});
			//건물정보버튼 클릭했을때
			
			GroundSellBut.addActionListener((e) -> {
				GroundSell();
			});
			
			OptionBut.addActionListener((e) -> {
				effSound.Play("Press.wav");
				if(optionFlag)
				{
					IvMenuBut.setEnabled(true);
					OptionWindow.setVisible(false);
					optionFlag = false;
				}
				else if(!optionFlag)
				{
					ivSubFlag = false;
					IvSubMenu.setVisible(ivSubFlag);
					IvMenuBut.setEnabled(false);
					OptionWindow.setVisible(true);
					optionFlag = !optionFlag;
				}
			});
			OptionExitBut.addActionListener((e) -> {
				effSound.Play("Press.wav");
				IvMenuBut.setEnabled(true);
				OptionWindow.setVisible(false);
				optionFlag = false;
			});
			FullScrBut.addActionListener((e) -> {
				if(!scrFlag)
				{
					FullSrc(true);
					FullScrBut.SetImg("Image/Option/CheckBut.png", false);
					WindowScrBut.SetImg("Image/Option/NonCheckBut.png", false);
				}
			});
			

			WindowScrBut.addActionListener((e) -> {
				if(scrFlag)
				{
					FullSrc(false);
					FullScrBut.SetImg("Image/Option/NonCheckBut.png", false);
					WindowScrBut.SetImg("Image/Option/CheckBut.png", false);
				}
			});
			SaveBut.addActionListener((e) -> {
				effSound.Play("Press.wav");
				save.SaveData(data, ground);
				Info("※ 저장되었습니다 ※ ");
			});
			//세이브버튼 클릭했을때
			SaveCloseBut.addActionListener((e) -> {
				effSound.Play("Press.wav");
				save.SaveData(data, ground);
				System.exit(1);
			});
			//세이브 종료버튼 클릭했을때
			
			HireBuyBut[0].addActionListener((e) -> {
				ArbiteBuy();
			});
			
			IvSubExitBut.addActionListener((e) -> {
				effSound.Play("Press.wav");
				ivSubFlag = false;
				IvSubMenu.setVisible(ivSubFlag);
			});
			
			IvSubBuyBut.addActionListener((e) -> {
				if((data.getMoney()*iv[currIvIdx].getDecre()) >= 1)
					IvBuy();
				else
					Error("투자금이 너무 적습니다");
			});
			//투자 구매버튼 클릭했을때
			IvSubSellBut.addActionListener((e) -> {
				IvSell();
			});
			//투자 판매버튼 클릭했을때
		}
		
		public void actionPerformed(ActionEvent e)
		{
			boolean flag = false;
			
			for(int i=STARTBUILDLINE ; i<MAXBUILD ; i++)
			{
				if(e.getSource() == BuildBuyBut[i])
				{
					BuildBuy(i);
				}
			}
			//건물구매 버튼 클릭시
			for(int i=STARTJOBLINE ; i<MAXJOB ; i++)
			{
				if(e.getSource() == JobBuyBut[i])
				{
					if(job[1].getPrice() <= data.getMoney())
						JobBuy();
					else
						Error("돈이 부족합니다! "+MoneyStr(Math.floor(job[1].getPrice()-data.getMoney()))+" 부족");
				}
			}
			//직업구매 버튼 클릭시
			
			for(int i=1 ; i<MAXHIRE ; i++)
			{
				if(e.getSource() == HireBuyBut[i])
				{
					AdBuy(i);
				}
			}
			//광고구매 버튼 클릭시
			
			for(int i=0 ; i<MAXINVEST ; i++)
			{
				if(e.getSource() == IvSubBut[i])
				{
					effSound.Play("Press.wav");
					currIvIdx=i;
					ShowSubIv(currIvIdx);
				}
			}
			//투자 버튼 클릭시
			
			for(int i=0 ; i<GROUNDHEIGHT ; i++)
			{
				for(int j=0 ; j<GROUNDHEIGHT-(i+1) && flag ; j++)
				{
					if(e.getSource() == Ground[i][j])
					{
						groundX = j;
						groundY = i;

						UpdateGroundInfo();
					}
				}
				
				for(int j=0 ; j<=i && !flag ; j++)
				{
					if(e.getSource() == Ground[i][j])
					{
						groundX = j;
						groundY = i;

						UpdateGroundInfo();
					}
					if(j==GROUNDWIDTH-1)
					{
						flag = !flag;
					}
				}
			}
			
			
		}
		
	public void EventMouse()
	{
		Screen.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e)
			{
				GetmouseXY(e);
			}
		});
		ClickBut.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				GetmouseXY(e);
			}
		});
		//드래그 좌표 정보
		BuildMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				draggReleaseY=ui.getMenuY(0);
				if(!draggFlag)
					draggPressY=e.getY();
				draggFlag = !draggFlag;
			}
		});
		BuildMenu.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				draggReleaseY=ui.getMenuY(0);
				draggFlag = false;
			}
		});
		JobMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				draggReleaseY=ui.getMenuY(1);
				if(!draggFlag)
					draggPressY=e.getY();
				draggFlag = !draggFlag;
			}
		});
		JobMenu.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				draggReleaseY=ui.getMenuY(1);
				draggFlag = false;
			}
		});
		HireMenu.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				draggReleaseY=ui.getMenuY(2);
				if(!draggFlag)
					draggPressY=e.getY();
				draggFlag = !draggFlag;
			}
		});
		HireMenu.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				draggReleaseY=ui.getMenuY(2);
				draggFlag = !draggFlag;
			}
		});
		//마우스 드래그 시작
		BuildMenu.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				ui.DraggMenu(BuildMenu, BuildDataPan, STARTBUILDLINE, MAXBUILD, e.getY(), draggPressY, draggReleaseY);				
			}
		});
		JobMenu.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				ui.DraggMenu(JobMenu, JobDataPan, STARTJOBLINE, MAXJOB, e.getY(), draggPressY, draggReleaseY);				
			}
		});
		
		HireMenu.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				ui.DraggMenu(HireMenu, HireDataPan, 0, MAXHIRE,  e.getY(), draggPressY, draggReleaseY);		
			}
		});
		
		/*
				IvMenu.addMouseMotionListener(new MouseMotionAdapter()
				{
					public void mouseDragged(MouseEvent e)
					{
						ui.DraggMenu(IvMenu, IvDataPan, 0, MAXINVEST, e.getY());				
					}
				});*/
		//마우스 드래그 끝
		//마우스휠 시작
		BuildMenu.addMouseWheelListener(new MouseWheelListener()
		{
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				ui.ScrollMenu(BuildMenu, BuildDataPan, STARTBUILDLINE, MAXBUILD, e.getWheelRotation());
				
			}
		});
		JobMenu.addMouseWheelListener(new MouseWheelListener()
		{
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				ui.ScrollMenu(JobMenu, JobDataPan, STARTJOBLINE, MAXJOB, e.getWheelRotation());
				
			}
		});
		HireMenu.addMouseWheelListener(new MouseWheelListener()
		{
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				ui.ScrollMenu(HireMenu, HireDataPan, 0, MAXHIRE, e.getWheelRotation());
				
			}
		});
		/*
		IvMenu.addMouseWheelListener(new MouseWheelListener()
		{
			public void mouseWheelMoved(MouseWheelEvent e)
			{
				ui.ScrollMenu(IvMenu, IvDataPan, 0, MAXINVEST, e.getWheelRotation());
				
			}
		});*/
		//마우스 휠 끝
		
		
	}
	
	public void Event()
	{
		
	}
	//그외 이벤트

	public void ClickDelay()
	{
		if(delayFlag == false && delayTimer <= 0)
		{
			delayFlag = true;
		}
	}
	
	public void Info(String infoMsg)
	{
		InfoTxt.setText(infoMsg);
		
		infoTimer=0;

		if(infoFlag == false)
		{
			infoFlag = true;
		}
	}
	
	public void Error(String errMsg)	//에러메세지 띄우는 메소드
	{
		ErrTxt.setText(errMsg);
		
		errTimer=0;

		if(errFlag == false)
		{
			errFlag = true;
		}
	}
	
	
	
	public void GetmouseXY(MouseEvent e)
	{
		
		mouseX = e.getX();
		mouseY = e.getY();
		mouseXY.setBounds(0, 0, 50, 50);
	}
	
	
	public void DrawQuestionBox()
	{
		
	}
	
	
	public void DrawOptionUI()
	{
		OptionWindow.add(OptionExitBut, new Integer(0));
		OptionWindow.add(SaveBut, new Integer(0));
	
		OptionWindow.add(SaveCloseBut, new Integer(0));
		OptionWindow.add(BGMVolumeSlider, new Integer(0));
		OptionWindow.add(EffVolumeSlider, new Integer(0));

		OptionWindow.add(WindowScrBut, new Integer(0));
		OptionWindow.add(FullScrBut, new Integer(0));
		OptionWindow.setBounds((int)(width*0.35), (int)(height*0.08), (int)(width*0.3), (int)(height*0.8));
		OptionWindow.setOpaque(false);
		
		OptionExitBut.setBounds((int)(OptionWindow.getWidth()*0.902), (int)(OptionWindow.getHeight()*0.006), (int)(OptionWindow.getHeight()*0.06),(int)(OptionWindow.getHeight()*0.06));
		
		SaveBut.setBounds((int)(OptionWindow.getWidth()*0.05), (int)(OptionWindow.getHeight()*(0.8)), (int)(OptionWindow.getWidth()*0.45), (int)(OptionWindow.getHeight()*0.11));
		SaveCloseBut.setBounds((int)(OptionWindow.getWidth()*0.505), (int)(OptionWindow.getHeight()*(0.8)), (int)(OptionWindow.getWidth()*0.45), (int)(OptionWindow.getHeight()*0.11));
		BGMVolumeSlider.setBounds((int)(OptionWindow.getWidth()*0.1), (int)(OptionWindow.getHeight()*(0.25)), (int)(OptionWindow.getWidth()*0.8), (int)(OptionWindow.getHeight()*0.1));
		EffVolumeSlider.setBounds((int)(OptionWindow.getWidth()*0.1), (int)(OptionWindow.getHeight()*(0.45)), (int)(OptionWindow.getWidth()*0.8), (int)(OptionWindow.getHeight()*0.1));
		
		ui.SetSlider(BGMVolumeSlider);
		ui.SetSlider(EffVolumeSlider);
		//BGMVolumeSlider.
		
		WindowScrBut.setBounds((int)(OptionWindow.getWidth()*0.27), (int)(OptionWindow.getHeight()*(0.58)), (int)(OptionWindow.getWidth()*0.13), (int)(OptionWindow.getWidth()*0.13));
		FullScrBut.setBounds((int)(OptionWindow.getWidth()*0.59), (int)(OptionWindow.getHeight()*(0.58)), (int)(OptionWindow.getWidth()*0.13), (int)(OptionWindow.getWidth()*0.13));
		WindowScrBut.setSelected(true);
		
		//FullScrBut.set;
		ScrCheck.add(WindowScrBut);
		ScrCheck.add(FullScrBut);

		OptionExitBut.SetImg("Image/Menu/ExitBut.png" ,false);
		SaveBut.SetImg("Image/Option/SaveBut.png" ,true);
		SaveCloseBut.SetImg("Image/Option/SaveCloseBut.png" ,false);
		FullScrBut.SetImg("Image/Option/NonCheckBut.png", false);
		WindowScrBut.SetImg("Image/Option/CheckBut.png", false);
		
		BGMVolumeSlider.addChangeListener((e)->{
			BGM.SetVolume(BGMVolumeSlider.getValue());
		});
		EffVolumeSlider.addChangeListener((e)->{
			effSound.SetVolume(EffVolumeSlider.getValue());
			
		});
	}
	
	public void FullSrc(boolean flag)
	{
		if(flag)
		{
			scrFlag = flag;
			contentPane.remove(Screen);
			fullFrame.add(Screen);
			width = (int)size.getWidth();
			height = (int)size.getHeight();
			fullFrame.setSize(width, height);
			Bound(size.getWidth()/winWidth, size.getHeight()/winHeight);
			fullFrame.setVisible(true);
			setVisible(false);
			fullFrame.validate();
		}
		else if(!flag)
		{
			scrFlag = flag;
			fullFrame.remove(Screen);
			contentPane.add(Screen);
			width = winWidth;
			height = winHeight;
			Bound(width/size.getWidth(), height/size.getHeight());
			fullFrame.setVisible(false);
			setVisible(true);
		}
		else
		{
			JOptionPane.showMessageDialog(this, "해당 기기는 전체화면을 지원하지 않습니다", "오류", JOptionPane.ERROR_MESSAGE); 
			WindowScrBut.setSelected(true);
		}
			
	}
	
	public void Inicial()
	{
		for(int i=1 ; i<MAXHIRE ; i++)
		{
			data.setAdNum(0, i);
			data.setAdPrice(hire[i].getPrice(), i);
		}
		JobInfoIcon.SetImg("Image/Menu/Job/0.png");
		JobInfoName.setText("직업 : " + job[0].getName());
		JobInfoValueTxt.setText("클릭당 : " + MoneyStr(job[0].getValue()));
		JobInfoLvTxt.setText(data.getClickLv()+"");
		
		
		data.setArbitePrice(hire[0].getPrice());
		data.addMoney(500);
		data.addClickNum(1);
		data.setAdValue(1);

		SetStr(1);

		IvSubTimer.setText("갱신시간 : ");
	}
	
	
	public void SetButtonEnable()
	{
		
		JobBuyBut[1].setEnabled(true);
		JobBuyTxt[1].setText(MoneyStr(job[1].getPrice())+"");

		/*IvBuyBut[0].setEnabled(true);
		IvBuyBut[1].setEnabled(true);
		IvBuyBut[2].setEnabled(true);*/
	}
	public String DoubleStr(double value)
	{
		String str = new DecimalFormat("###.#####").format(value);
		return str;
	}
	public String MoneyStr(double money)
	{
		money = Math.floor(money);
		String[] unit = {"원", "만", "억", "조", "경", "해", "자", "양", "구", "간", "정", "제", "극", "항하사", "아승기", "나유타", "불가사의", "무량대수", "그만해1", "그만해2", "그만해3", "그만해주세요"};
		
		String moneyStr = new DecimalFormat("###.#####").format(money);
		String firstChar="";
		String secondChar="";

		int subCnt;
		
		moneyStr = moneyStr.substring(0, moneyStr.length());
		int strCnt = moneyStr.length();

		subCnt = (int) Math.floor((strCnt-1)/4);
		if(strCnt<=4)
		{
			firstChar = moneyStr.substring(0);
			moneyStr = firstChar + unit[subCnt];
		}
		else
		{
			strCnt -= 4*subCnt;
			firstChar = moneyStr.substring(0, strCnt);
			secondChar = moneyStr.substring(strCnt, strCnt+4);
			
			for(int i=0 ; i<=secondChar.length() ; i++)
			{
				if(secondChar.charAt(i) == '0')
					secondChar = secondChar.substring(i+1);
				else
					break;
			}
			if(secondChar.charAt(0)!='0')
				moneyStr = firstChar + unit[subCnt] + secondChar + unit[subCnt-1];
			else
				moneyStr = firstChar + unit[subCnt] + "원";
		}
		return moneyStr;
	}
	
	public void Bound(double wid, double hei)
	{
		ui.Resize(Screen, wid, hei);
		ui.Resize(ClickBut, wid, hei);
		ui.Resize(MenuLayout, wid, hei);
		
		ui.Resize(OptionWindow, wid, hei);
		ui.Resize(OptionBG, wid, hei);
		ui.Resize(OptionExitBut, wid, hei);
		ui.Resize(SaveBut, wid, hei);
		ui.Resize(SaveCloseBut, wid, hei);
		ui.Resize(BGMVolumeSlider, wid, hei);
		ui.Resize(EffVolumeSlider, wid, hei);
		ui.Resize(WindowScrBut, wid, hei);
		ui.Resize(FullScrBut, wid, hei);
		ui.Resize(OptionLayoutBut, wid, hei);
		
		ui.Resize(BGImg, wid, hei);
		ui.Resize(MenuBG, wid, hei);
		ui.Resize(MenuBarPan, wid, hei);
		ui.Resize(BuildMenu, wid, hei);
		ui.Resize(JobMenu, wid, hei);
		ui.Resize(HireMenu, wid, hei);
		ui.Resize(IvMenu, wid, hei);
		ui.Resize(BuildMenuBut, wid, hei);
		ui.Resize(JobMenuBut, wid, hei);
		ui.Resize(HireMenuBut, wid, hei);
		ui.Resize(IvMenuBut, wid, hei);
		
		ui.Resize(IvSubMenu, wid, hei);
		
		ui.Resize(IvSubName, wid, hei);
		ui.Resize(IvSubMoney, wid, hei);
		ui.Resize(IvSubTimer, wid, hei);

		ui.Resize(IvSubInfo, wid, hei);
		ui.Resize(IvSubInfoWinLateStr, wid, hei);
		ui.Resize(IvSubInfoProbaTxt, wid, hei);
		ui.Resize(IvSubInfoWinValueTxt, wid, hei);
		ui.Resize(IvSubInfoLoseValueTxt, wid, hei);
		ui.Resize(IvSubInfoWinMoneyTxt, wid, hei);
		ui.Resize(IvSubInfoLoseMoneyTxt, wid, hei);
		
		ui.Resize(IvSubBuyPan, wid, hei);
		ui.Resize(IvSubSellPan, wid, hei);
		ui.Resize(IvSubExitBut, wid, hei);
		ui.Resize(IvSubBuyBut, wid, hei);
		ui.Resize(IvSubSellBut, wid, hei);
		ui.Resize(IvSubBuyTxt, wid, hei);
		ui.Resize(IvSubSellTxt, wid, hei);
		ui.Resize(IvSubLayoutBut, wid, hei);
		
		ui.Resize(CashInfoLab, wid, hei);
		ui.Resize(OptionBut, wid, hei);
		ui.Resize(MoneyTxt, wid, hei);
		ui.Resize(MoneyIncomeTxt, wid, hei);
		ui.Resize(MoneyTxtBG, wid, hei);
		ui.Resize(ErrTxt, wid, hei);
		ui.Resize(InfoTxt, wid, hei);
		
		ui.Resize(GroundBuildField, wid, hei);
		ui.Resize(GroundField, wid, hei);
		
		
		for(int i=STARTBUILDLINE ; i<MAXBUILD ; i++)
			ui.ResizeMenuUI(BuildDataPan[i], BuildIcon[i], BuildName[i], BuildValue[i], BuildBuyPan[i], BuildBuyBut[i], BuildBuyTxt[i], wid, hei);
		for(int i=STARTJOBLINE ; i<MAXJOB ; i++)
			ui.ResizeMenuUI(JobDataPan[i], JobIcon[i], JobName[i], JobValue[i], JobBuyPan[i], JobBuyBut[i], JobBuyTxt[i], wid, hei);
		for(int i=0 ; i<MAXHIRE ; i++)
			ui.ResizeMenuUI(HireDataPan[i], HireIcon[i], HireName[i], HireValue[i], HireBuyPan[i], HireBuyBut[i], HireBuyTxt[i], wid, hei);
		for(int i=0 ; i<MAXINVEST ; i++)
			ui.Resize(IvSubBut[i], wid, hei);
			//ui.ResizeDrawMenuUI(IvDataPan[i], IvIcon[i], IvName[i], IvValue[i], IvBuyPan[i], IvBuyBut[i], IvBuyTxt[i], wid, hei);*/

		boolean flag = false;
		for(int i=0 ; i<GROUNDHEIGHT ; i++)
		{
			ui.Resize(GroundBuildLine[i], wid, hei);
			ui.Resize(GroundLine[i], wid, hei);
			for(int j=0 ; j<GROUNDHEIGHT-(i+1) && flag ; j++)
			{
				ui.Resize(Ground[i][j], wid, hei);
				ui.Resize(GroundBuild[i][j], wid, hei);
			}
			for(int j=0 ; j<=i && !flag ; j++)
			{
				ui.Resize(Ground[i][j], wid, hei);
				ui.Resize(GroundBuild[i][j], wid, hei);
				
				if(j==GROUNDWIDTH-1)
					flag=true;
			}
		}
		ui.Resize(GroundBorder, wid, hei);
		ui.ResizeGroundInfoUI(GroundInfoMenu, GroundInfoIcon, GroundInfoName, GroundInfoIncomeTxt, GroundInfoBuildNTxt, GroundInfoLVTxt, GroundSellPan, GroundSellBut, GroundSellTxt, GroundIncomeIcon, GroundPrcIcon, GroundLvIcon, wid, hei);
		ui.ResizeJobInfoUI(JobInfoMenu, JobInfoIcon, JobInfoName, JobInfoValueTxt, JobInfoLvTxt, JobInfoValue, JobInfoLv, wid, hei);
		
		IvSubBuyTxt.setFont(new Font(font, fontType, (int)(IvSubBuyPan.getHeight()*0.20)));
		IvSubSellTxt.setFont(new Font(font, fontType, (int)(IvSubSellPan.getHeight()*0.28)));

		IvSubName.setFont(new Font(font, fontType, (int)(MenuBarPan.getHeight()*0.6)));
		IvSubMoney.setFont(new Font(font, fontType, (int)(MenuBarPan.getHeight()*0.4)));
		IvSubTimer.setFont(new Font(font, fontType, (int)(MenuBarPan.getHeight()*0.4)));

		IvSubInfoWinLateStr.setFont(new Font(font, fontType, (int)(IvSubInfo.getHeight()*0.1)));
		IvSubInfoProbaTxt.setFont(new Font(font, fontType, (int)(IvSubInfo.getHeight()*0.1)));
		IvSubInfoWinValueTxt.setFont(new Font(font, fontType, (int)(IvSubInfo.getHeight()*0.08)));
		IvSubInfoLoseValueTxt.setFont(new Font(font, fontType, (int)(IvSubInfo.getHeight()*0.08)));
		IvSubInfoWinMoneyTxt.setFont(new Font(font, fontType, (int)(IvSubInfo.getHeight()*0.1)));
		IvSubInfoLoseMoneyTxt.setFont(new Font(font, fontType, (int)(IvSubInfo.getHeight()*0.1)));
		
		InfoTxt.setFont(new Font(font, fontType, (int)(Screen.getHeight()*0.032)));
		ErrTxt.setFont(new Font(font, fontType, (int)(Screen.getHeight()*0.04)));
		ClickEffTxt.setFont(new Font(font, fontType, (int)(Screen.getHeight()*0.04)));

		MoneyTxt.setFont(new Font(font, fontType, (int)(MenuBarPan.getHeight()*0.3)));
		MoneyIncomeTxt.setFont(new Font(font, fontType, (int)(MenuBarPan.getHeight()*0.3)));
		repaint();
	}
	
	public void SetStr(int num)
	{
		switch(num)
		{
			case 0:
				IvSubMoney.setText(MoneyStr(data.getIvMoney(currIvIdx))+"");
				IvSubInfoProbaTxt.setText(iv[ivIdxMouse].getProba()+"%");
				IvSubInfoWinMoneyTxt.setText(MoneyStr(data.getIvMoney(ivIdxMouse)*iv[ivIdxMouse].getWinValue())+"");
				IvSubInfoLoseMoneyTxt.setText(MoneyStr(data.getIvMoney(ivIdxMouse)*iv[ivIdxMouse].getLoseValue())+"");
				break;
				
			case 1:
				MoneyIncomeTxt.setText(MoneyStr(Math.floor(data.getIncome()))+"("+DoubleStr(Math.floor((data.getAdValue()-1)*100))+"%+)");
				break;
		}
		
	}
}



