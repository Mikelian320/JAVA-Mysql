package cn.greatwebtech.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.greatwebtech.dao.impl.TestCountDaoImpl;
import cn.greatwebtech.service.ISearchService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestCountServiceImpl implements ISearchService {
    private TestCountDaoImpl testDB;
	private DealQueryString dealQS;
	//private final String selectStr="SELECT sql_calc_found_rows SN";
	public void setTestDB(TestCountDaoImpl testDB) {
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
	public String generateSQL(HttpServletRequest request)throws Exception 
	{
		//boolean getLog=getQueryParameter(request.getQueryString(),"searchMode").contains("Log");
        String SQLString="";
        String selectStr="";
		try {
			JSONObject searchCon=dealQS.condition2Json(request);
			dealQS.validateSearchConditon(searchCon);
			//String select=selectStr;
			List<String> tables=dealQS.getTables(searchCon);
			String condition=dealQS.getWhereCondition(searchCon);
			String limit=dealQS.getLimitCondition(searchCon);
			if(tables.size()==1) 
			{
				if(!condition.isEmpty()) {
					SQLString="SELECT sql_calc_found_rows SN FROM "+tables.get(0)+" WHERE "+condition+" ORDER BY Record_Time DESC "+limit;
				}else {
					SQLString="SELECT sql_calc_found_rows SN FROM "+tables.get(0)+" ORDER BY Record_Time DESC "+limit;
				}

			}else {
				for(String table:tables) 
				{
					if(!SQLString.isEmpty()) 
					{
                        SQLString+=" UNION ";
                        selectStr="SELECT SN";
					}else{
                        selectStr="SELECT sql_calc_found_rows SN";
                    }
					if(!condition.isEmpty()) {
						SQLString+=selectStr+" FROM "+table+" WHERE "+condition;
					}else {
						SQLString+=selectStr+" FROM "+table;
					}
				}
				//非查询LOG时增加按时间降序排列，如果查询LOG不进行排列（由于时间为查询条件在UNION语句下会报错）
				SQLString+=" ORDER BY Record_Time DESC "+limit;

			}
		}catch(Exception e) {
			throw e;
		}
		return SQLString;
	}
}