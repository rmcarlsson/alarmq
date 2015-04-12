import java.util.Date;


public class Tuple implements Comparable<Tuple> {
	

	private int id;
	private Date tm;
	private String mo;
	private String at;
	private Date ev_tm;
	private String sev;
	private String ah;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTm() {
		return tm;
	}

	public void setTm(Date tm) {
		this.tm = tm;
	}

	public String getMo() {
		return mo;
	}

	public void setMo(String mo) {
		this.mo = mo;
	}

	public String getAt() {
		return at;
	}

	public void setAt(String at) {
		this.at = at;
	}

	public Date getEv_tm() {
		return ev_tm;
	}

	public void setEv_tm(Date ev_tm) {
		this.ev_tm = ev_tm;
	}

	public String getSev() {
		return sev;
	}

	public void setSev(String sev) {
		this.sev = sev;
	}

	public String getAh() {
		return ah;
	}

	public void setAh(String ah) {
		this.ah = ah;
	}

	@Override
	public int compareTo(Tuple arg0) {
		
		int res = tm.compareTo(arg0.getTm());
		
		// TODO Auto-generated method stub
		return res;
	}
	
}
