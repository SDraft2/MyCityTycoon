/*
 * name		이름
 * value	초당 수입
 * price	건물 한채당 가격
 * number	건물의 수
 * index	몇번째 버튼인지 확인하는 용도
 * */

public class Data_Building extends Data
{
	String[] name = new String[3];
	Data_Building(String name1, String name2, String name3, int value ,int price)
	{
		this.name[0] = name1;
		this.name[1] = name2;
		this.name[2] = name3;
		this.value = value;
		this.price = price;
	}

	public String getName(int idx) {
		return name[idx];
	}

	public void setName(String name, int idx) {
		this.name[idx] = name;
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
	public void addPrice(int price) {
		this.price += price;
	}
}
