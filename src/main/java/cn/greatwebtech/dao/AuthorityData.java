package cn.greatwebtech.dao;

public class AuthorityData {
    public String driver;
	public String dburl;
	public String user;
	public String password;
	public String tables;
	public void setDriver(final String driver) {
		this.driver = driver;
	}

	public void setDburl(final String dburl) {
		this.dburl = dburl;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setTables(final String tables) {
		this.tables = tables;
	}
    
}