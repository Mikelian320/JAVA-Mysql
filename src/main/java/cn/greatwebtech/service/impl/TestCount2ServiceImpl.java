package cn.greatwebtech.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

//import cn.greatwebtech.dao.impl.TestCountDaoImpl;
import cn.greatwebtech.dao.impl.TestRecordDaoImpl;
import cn.greatwebtech.service.ISearchService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestCount2ServiceImpl implements ISearchService {
    private TestRecordDaoImpl testDB;
	private DealQueryString dealQS;
	//private final String selectStr="SELECT sql_calc_found_rows SN";
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
	public String generateSQL(HttpServletRequest request)throws Exception 
	{
		//boolean getLog=getQueryParameter(request.getQueryString(),"searchMode").contains("Log");
        String SQLString="";
        //String selectStr="";
		try {
			JSONObject searchCon=dealQS.condition2Json(request);
			dealQS.validateSearchConditon(searchCon);
			//String select=selectStr;
			List<String> tables=dealQS.getTables(searchCon);
			String condition=dealQS.getWhereCondition(searchCon);
			if(tables.size()==1) 
			{
				if(!condition.isEmpty()) {
					SQLString="SELECT COUNT(*) FROM "+tables.get(0)+" WHERE "+condition;
				}else {
					SQLString="SELECT COUNT(*) FROM "+tables.get(0);
				}

			}else {
				//for(String table:tables) 
				for(int i=0;i<tables.size();i++)
				{
					//selectStr="SELECT sql_calc_found_rows Slot,Record_Time,PC_Name";
					if(!SQLString.isEmpty()){

					}
					if(!condition.isEmpty()) {
						SQLString+="(SELECT COUNT(*) FROM "+tables.get(i)+" WHERE "+condition+")";
					}else {
						SQLString+="(SELECT COUNT(*) FROM "+tables.get(i)+")";
					}
					if (i!=tables.size()-1) {
						SQLString+="+";
					}
				}
				SQLString="SELECT("+SQLString+") AS sum_count";
			}
		}catch(Exception e) {
			throw e;
		}
		return SQLString;
	}
}