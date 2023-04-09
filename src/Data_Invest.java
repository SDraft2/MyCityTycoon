/*
 * name			이름
 * value		클릭당 수입
 * nextLvPrice	다음 레벨가격
 * */

public class Data_Invest extends Data
{
	private int timer;
	private int proba;
	private double decre;
	private double winValue;
	private double loseValue;
	
	Data_Invest(String name, int timer, int probability, double decrease, double winValue, double loseValue)
	{
		this.name = name;
		this.timer = timer;
		proba = probability;
		decre = decrease;
		this.winValue = winValue;
		this.loseValue = loseValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	public int getTimer() {
		return timer;
	}
	public void setTimer(int timer) {
		this.timer = timer;
	}
	
	public double getDecre() {
		return decre/100;
	}
	public void setDecre(double decrease) {
		decre = decrease;
	}
	
	public int getProba() {
		return proba;
	}
	public void setProba(int proba) {
		this.proba = proba;
	}
	

	public double getWinValue() {
		return winValue;
	}
	public void setWinValue(double winValue) {
		this.winValue = winValue;
	}
	
	public double getLoseValue() {
		return loseValue;
	}
	public void setloseValue(double loseValue) {
		this.loseValue = loseValue;
	}
}
