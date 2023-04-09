/*
 * name			이름
 * value		클릭당 수입
 * nextLvPrice	다음 레벨가격
 * */

public class Data_Job extends Data
{
	double price;
	Data_Job(String name, int value ,double price)
	{
		this.name = name;
		this.value = value;
		this.price = price;
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

	public void setPrice(double price) {
		this.price = price;
	}
	
	
}
