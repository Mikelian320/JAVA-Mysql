package cn.greatwebtech.service;

import net.sf.json.JSONArray;

public interface ITestRecordService {
	public JSONArray searchData(String queryString)throws Exception;
}
