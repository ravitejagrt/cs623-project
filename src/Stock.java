
public class Stock {
	private String prod;
	private String dep;
	private int quantity;
	public Stock() {}
	public Stock(String prod, String dep, int quantity) {
		this.prod = prod;
		this.dep = dep;
		this.quantity = quantity;
	}
	public String getProd() {
		return prod;
	}
	public void setProd(String prod) {
		this.prod = prod;
	}
	public String getDep() {
		return dep;
	}
	public void setDep(String dep) {
		this.dep = dep;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
