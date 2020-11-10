
/*
 * @author Ravi Teja Gajarla 
 */

public class Depot {
	private String dep;
	private String addr;
	private int volume;	
	public Depot() {}
	public Depot(String dep, String addr, int volume) {
		this.dep = dep;
		this.addr = addr;
		this.volume = volume;
	}
	public String getDep() {
		return dep;
	}
	public void setDep(String dep) {
		this.dep = dep;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}	
}
