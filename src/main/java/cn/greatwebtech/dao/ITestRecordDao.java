package cn.greatwebtech.dao;
import net.sf.json.JSONArray;
public interface ITestRecordDao {
		//public String getLog(String SQL);
		public JSONArray getDataFromDB(String SQL)throws Exception;
}
