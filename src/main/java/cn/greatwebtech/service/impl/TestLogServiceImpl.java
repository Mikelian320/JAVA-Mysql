package cn.greatwebtech.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.greatwebtech.dao.impl.TestRecordDaoImpl;
import cn.greatwebtech.service.ISearchService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestLogServiceImpl implements ISearchService{
    private TestRecordDaoImpl testDB;
	private DealQueryString dealQS;
	private final String selectStr="SELECT Log";
	public void setTestDB(TestRecordDaoImpl testDB) {
		this.testDB = testDB;
	}
	public void setDealQS(DealQueryString dealQS) {
		this.dealQS = dealQS;
	}
	@Override
	public JSONArray searchData(String queryString)throws Exception {
		// CALL DAO get database data
		//
		try {
			JSONArray result=new JSONArray();
			//return null;
			result=testDB.getDataFromDB(queryString);
			//System.out.print(result.toString());
			return result;
			//throw new Exception("Test Exception");
		}catch(Exception e) {
			throw e;
		}
	}
	/*	 * use this method to generate SQL
	 * */
    @Override
	public String generateSQL(HttpServletRequest request)throws Exception 
	{
		//boolean getLog=getQueryParameter(request.getQueryString(),"searchMode").contains("Log");
		String SQLString="";
		try {
			JSONObject searchCon=dealQS.condition2Json(request);
			dealQS.validateSearchConditon(searchCon);
			//String select=selectStr;
			List<String> tables=dealQS.getTables(searchCon);
            String condition=dealQS.getWhereCondition(searchCon);
            if (condition.isEmpty()) {
                throw new Exception("Search Log Condition Can't be None!");
            }
			if(tables.size()==1) 
			{
				SQLString=selectStr+" FROM "+tables.get(0)+" WHERE "+condition;
			}else {
				for(String table:tables) 
				{
					if(SQLString!="") 
					{
						SQLString+=" UNION ";
					}
					SQLString+=selectStr+" FROM "+table+" WHERE "+condition;

				}
			}
		}catch(Exception e) {
			throw e;
		}
		return SQLString;
	}
    
}