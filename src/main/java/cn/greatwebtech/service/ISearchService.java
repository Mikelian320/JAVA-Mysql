package cn.greatwebtech.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

public interface ISearchService {
	public JSONArray searchData(String queryString)throws Exception;
	public String generateSQL(HttpServletRequest request)throws Exception ;

}
