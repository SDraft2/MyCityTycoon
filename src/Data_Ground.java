import java.io.Serializable;

/*
 * name			이름
 * value		클릭당 수입
 * nextLvPrice	다음 레벨가격
 * */

public class Data_Ground implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1828649991440709239L;
	
	int name;
	double value;
	double price;
	int buildLv;
	int buildNum;
	int hireNum;
	
	Data_Ground(int level ,int num)
	{
		name = 0;
		buildNum = level;
		hireNum = num;
	}
	public void SetDataGround(int name, int level ,int num)
	{
		this.name = name;
		buildNum = level;
		hireNum = num;
	}
	
	public int getName() {
		return name;
	}
	public void setName(int name) {
		this.name = name;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getBuildLv() {
		return buildLv;
	}
	public void setBuildLv(int buildLv) {
		this.buildLv = buildLv;
	}
	public int getBuildNum() {
		return buildNum;
	}
	public void setBuildNum(int buildNum) {
		this.buildNum = buildNum;
	}
	public void addBuildNum(int num)
	{
		buildNum += num;
	}
	
	public int getHireNum() {
		return hireNum;
	}
	public void setHireNum(int hireNum) {
		this.hireNum = hireNum;
	}
	public void addPrice(int price) {
		this.price += price;
	}
	
	public double getIncome()
	{
		return value*buildNum;
	}
	
	public double SellPrice()
	{
		return getPrice()/10;
	}
}
