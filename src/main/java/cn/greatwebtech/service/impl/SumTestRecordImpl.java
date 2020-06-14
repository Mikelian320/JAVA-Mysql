package cn.greatwebtech.service.impl;

import cn.greatwebtech.dao.impl.TestRecordDaoImpl;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SumTestRecordImpl {
    private TestRecordDaoImpl testDB;
    public void setTestDB(TestRecordDaoImpl testDB) {
		this.testDB = testDB;
    }
    
    public JSONObject getTableSumTestRecord()throws Exception{
        String [] tables=testDB.getTables().split(",");
        JSONObject result=new JSONObject();
        JSONArray count=new JSONArray();
        String queryStr="";
        try {
            for (String table : tables) {
                queryStr=String.format("SELECT COUNT(*) FROM %s", table);
                count=testDB.getDataFromDB(queryStr);
                result.put(table,count.getJSONArray(0).get(0));
            }
            return result;
        } catch (Exception e) {
            //TODO: handle exception
            throw e;
        }


    }
}