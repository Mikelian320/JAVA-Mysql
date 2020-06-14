package cn.greatwebtech.dao.impl;

import java.sql.*;

import cn.greatwebtech.dao.AuthorityData;
import cn.greatwebtech.dao.ITestRecordDao;
//import cn.greatwebtech.dao.ITestRecordDao;
//import ITestRecordDao;
import net.sf.json.JSONArray;


public class TestRecordDaoImpl implements ITestRecordDao {
	private AuthorityData  authorityData=null;
	

	@Override
	public JSONArray getDataFromDB(final String SQL) throws Exception {
		Connection conn = null;
		Statement stmt = null;
		final JSONArray dataSet = new JSONArray();
		try {
			Class.forName(authorityData.driver);
			conn = DriverManager.getConnection(authorityData.dburl, authorityData.user, authorityData.password);
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

	public AuthorityData getAuthorityData() {
		return authorityData;
	}

	public void setAuthorityData(AuthorityData authorityData) {
		this.authorityData = authorityData;
	}

	public String getTables(){
		return authorityData.tables;
	}
}
