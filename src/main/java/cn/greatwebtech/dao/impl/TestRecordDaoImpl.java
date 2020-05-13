package cn.greatwebtech.dao.impl;

import java.sql.*;

import cn.greatwebtech.dao.ITestRecordDao;
//import cn.greatwebtech.dao.ITestRecordDao;
//import ITestRecordDao;
import net.sf.json.JSONArray;


public class TestRecordDaoImpl implements ITestRecordDao {
	public String driver;
	private String dburl;
	private String user;
	private String password;
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

	@Override
	public JSONArray getDataFromDB(final String SQL) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		final JSONArray dataSet = new JSONArray();
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(dburl, user, password);
			stmt = conn.createStatement();
			final ResultSet data = stmt.executeQuery(SQL);
			final int columnCount = data.getMetaData().getColumnCount();
			while (data.next()) {
				final JSONArray singleDA = new JSONArray();
				for (int i = 1; i <= columnCount; i++) {
					singleDA.add(data.getString(i));
				}
				dataSet.add(singleDA);
			}
			data.close();
			stmt.close();
			conn.close();
			return dataSet;
		} catch (final Exception e) {
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (final Exception e) {
				throw e;
			}
		}
	}

}
