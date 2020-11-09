
public class Product {
	private String prod;
	private String pName;
	private float price;
	public Product() {}
	public Product(String prod, String pName, float price) {
		this.prod = prod;
		this.pName = pName;
		this.price = price;
	}
	public String getProd() {
		return prod;
	}
	public void setProd(String prod) {
		this.prod = prod;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
}
