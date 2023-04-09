import java.io.Serializable;

/*
 * money	돈
 * clickLv	알바의 레벨
 * buildLv	건물 해금 레벨
 * */

public class Info implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1828649991440709239L;
	
	private double money;
	private int clickLv;
	private int clickNum;
	private double clickPrice;
	private int buildLv;
	private int arbiteLv;
	private double arbitePrice;
	private double arbiteValue;
	private double adValue;
	private double[] adPrice;
	private int[] adNum;
	private double[] ivMoney;
	private double income;
	
	
	Info(double money, int clickLv, int buildLv, int maxBuildLv, int maxHire, int maxiv)
	{
		this.money = money;
		this.clickLv = clickLv;
		this.buildLv = buildLv;
		adPrice = new double[maxHire];
		adNum = new int[maxHire];
		ivMoney = new double[maxiv];
	}


	public double getMoney() {
		return Math.floor(money);
	}


	public void setMoney(double money) {
		this.money = money;
	}
	
	public void addMoney(double money) {
		this.money += money;
	}
	
	public void subMoney(double money) {
		this.money -= money;
	}

	public int getClickLv() {
		return clickLv;
	}


	public void setClickLv(int clickLv) {
		this.clickLv = clickLv;
	}

	public void addClickLv(int clickLv) {
		this.clickLv += clickLv;
	}

	public int getBuildLv() {
		return buildLv;
	}
	public void setBuildLv(int buildLv) {
		this.buildLv = buildLv;
	}
	public void addBuildLv(int buildLv) {
		this.buildLv += buildLv;
	}
	
	public int getClickNum() {
		return clickNum;
	}
	public void setClickNum(int clickNum) {
		this.clickNum = clickNum;
	}
	public void addClickNum(int num) {
		this.clickNum += num;
	}
	
	public double getClickPrice() {
		return Math.floor(clickPrice);
	}
	public void setClickPrice(double clickPrice) {
		this.clickPrice = clickPrice;
	}
	public void addClickPrice(double price) {
		this.clickPrice += price;
	}
	
	public int getArbiteLv() {
		return arbiteLv;
	}
	public void setArbiteLv(int arbiteLv) {
		this.arbiteLv = arbiteLv;
	}
	public void addArbiteLv(int num) {
		this.arbiteLv += num;
	}
	
	public double getArbitePrice() {
		return arbitePrice * Math.pow(7, arbiteLv);
	}
	public void setArbitePrice(double ArbitePrice) {
		this.arbitePrice = ArbitePrice;
	}

	public double getArbiteValue() {
		return arbiteValue;
	}
	public void setArbiteValue(double arbiteValue) {
		this.arbiteValue = arbiteValue;
	}
	
	public double getAdValue() {
		return adValue;
	}
	public void setAdValue(double adValue) {
		this.adValue = adValue;
	}
	public void AddAdValue(double value) {
		this.adValue += value;
	}
	
	public double getAdPrice(int idx) {
		return adPrice[idx];
	}
	public void setAdPrice(double price, int idx) {
		this.adPrice[idx] = price;
	}
	
	public int getAdNum(int idx) {
		return adNum[idx];
	}
	public void setAdNum(int num, int idx) {
		this.adNum[idx] = num;
	}
	public void AddAdNum(int idx) {
		this.adNum[idx]++;
	}
	
	public double getIvMoney(int idx) {
		return ivMoney[idx];
	}
	public void setIvMoney(double ivValue, int idx) {
		if(ivMoney[idx] < 1 && ivMoney[idx] != 0)
			ivValue = 0;
		
		this.ivMoney[idx] = ivValue;
	}
	
	public double getIncome() {
		return income*adValue;
	}

	public void setIncome(double income) {
		this.income = income;
	}
	public void addIncome(double income) {
		this.income += income;
	}
	public void subIncome(double income) {
		this.income -= income;
	}
	

	
}
