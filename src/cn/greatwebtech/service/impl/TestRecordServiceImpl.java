package cn.greatwebtech.service.impl;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import cn.greatwebtech.dao.impl.TestRecordDaoImpl;
import cn.greatwebtech.service.ITestRecordService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.mock.web.MockHttpServletRequest;
/*service锟姐，锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷转锟斤拷锟斤拷锟斤拷锟斤拷
 **锟斤拷URL转锟斤拷为锟斤拷询锟斤拷锟�
 **锟斤拷DAO锟斤拷锟截碉拷锟斤拷锟斤拷转锟斤拷为前锟斤拷锟斤拷要锟侥革拷式
 * 
 * */
public class TestRecordServiceImpl implements ITestRecordService{

	private TestRecordDaoImpl testDB;
	public void setTestDB(TestRecordDaoImpl testDB) {
		this.testDB = testDB;
	}
	@Override
	public JSONArray searchData(String queryString)throws Exception {
		// CALL DAO get database data
		//可在该函数增加前置通知和异常通知,分别记录查询语句和异常消息
		try {
			return null;
			//return testDB.getDataFromDB(queryString);
		}catch(Exception e) {
			throw e;
		}
	}
	/*	 * use this method to generate SQL
	 * */
	public String generateSQL(HttpServletRequest request)throws Exception 
	{
		boolean getLog=getQueryParameter(request.getQueryString(),"searchMode").contains("Log");
		String SQLString="";
		try {
			JSONObject searchCon=condition2Json(request);
			validateSearchConditon(searchCon);
			String select=getSelectStr(getLog);
			List<String> tables=getTables(searchCon);
			String condition=getWhereCondition(searchCon);
			String limit= getLimitCondition(searchCon);
			if(tables.size()==1) 
			{
				SQLString=select+" FROM "+tables.get(0)+" WHERE "+condition+" ORDER BY Record_Time DESC "+limit;
			}else {
				for(String table:tables) 
				{
					if(SQLString!="") 
					{
						SQLString+=" UNION ";
					}
					SQLString+=select+" FROM "+table+" WHERE "+condition;
				}
				SQLString+=" ORDER BY Record_Time DESC "+limit;
			}
		}catch(Exception e) {
			throw e;
		}
		return SQLString;
	}
	private String getLimitCondition(JSONObject searchCon) 
	{
		String offset="";
		if(searchCon.containsKey("Offset")) {
			offset=searchCon.getString("Offset");
		}else {
			offset="0";
		}
		String limit="";
		if(searchCon.containsKey("Limit")) {
			limit=searchCon.getString("Limit");
		}else {
			limit="50";
		}
		return "limit "+offset+","+limit;
	}
	private String getSelectStr(boolean selectLog) 
	{
		if(selectLog) 
		{
			return "SELECT LOG";
		}else {
			return "SELECT Slot,Test_Station,Test_Require,Product_Model,SN,MAC,Record_Time,PC_Name,ATE_Version,Hardware_Version,Software_Version,Software_Number,Boot_Version,TestResult";
		}
	}
	private String getWhereCondition(JSONObject searchCon) 
	{
		Iterator<String> iterator = searchCon.keys();
		String whereCon="";
		while(iterator.hasNext()) 
		{
			String key=iterator.next();
			String value=searchCon.getString(key);
			if(key!="Limit"&&key!="Offset"&&key!="Product_Type") 
			{
				if(whereCon!="") 
				{
					whereCon+="AND ";
				}
				switch(key) 
				{
				case "StartTime":
					whereCon+="Record_Time>="+"'"+value+"'"+" ";
					break;
				case "EndTime":
					whereCon+="Record_Time<="+"'"+value+"'"+" ";
					break;
				default:
					whereCon+=key+"="+"'"+value+"'"+" ";
					break;
				}
			}
		}
		return whereCon;
	}
	private List<String> getTables(JSONObject searchCon) 
	{
		List<String> result=new ArrayList<String>();
		if(searchCon.containsKey("Product_Type")) 
		{
			result.add(searchCon.getString("Product_Type"));
		}else {
			String [] tableArray=testDB.tables.split(",");
			for(String var : tableArray) 
			{
				result.add(var);
			}
		}
		return result;
	}
	private JSONObject condition2Json(HttpServletRequest request)throws Exception 
	{
		JSONObject searchCon= new JSONObject();//为什么锟结报锟届常锟斤拷
		String [] searchArr={"Slot","Product_Type","Test_Station","Test_Required","Product_Model","SN","MAC","PC_Name","TestResult","StartTime","EndTime","Record_Time","Offset","Limit"};
		for(String key : searchArr) 
		{
			String value = getQueryParameter(request.getQueryString(),key);
			if(value!=null) 
			{
				/*
				 * /Key which needs transfer
				 * 1锟斤拷slot(SLOT+M1-->SLOT M1)
				 * 2锟斤拷Test_Station锟斤拷Test_Required(UTF-8)
				 * 3锟斤拷StartTime锟斤拷EndTime锟斤拷RecordTime(TimeStamp-->String)
				 */
				try 
				{
					switch(key) 
					{
						case "Slot":
							value.replace("+", " ");
							break;
						case "Test_Station":
						case "Test_Required":
							value=getURLDecode(value);
							break;
						case "StartTime":
						case "EndTime":
						case "Record_Time":
							value=timeStamp2Date(value);
							break;
						case "MAC":
						case "SN":
						case "Product_Model":
						case "TestResult":
						case "PC_Name":
						case "Product_Type":
						case "Limit":
						case "Offset":
							break;
						default:
							throw new Exception(key+" is invalid condition!");
							//break;
					}
					searchCon.put(key, value);	
				}catch(Exception e) {
					throw e;
				}
			}
		}
		return searchCon;
	}
	
	//校锟斤拷锟斤拷锟斤拷前锟剿碉拷锟斤拷锟斤拷锟斤拷锟斤拷锟角凤拷锟斤拷锟斤拷,锟斤拷锟斤拷锟斤拷锟斤拷锟阶筹拷锟届常
	private void validateSearchConditon(JSONObject searchCon) throws Exception
	{
		Iterator<String> iterator = searchCon.keys();
		while(iterator.hasNext()) 
		{
			String key=iterator.next();
			String value=searchCon.getString(key);
			switch(key) 
			{
			case "Slot":
				break;
			case "Product_Type":
				String [] typeArray=testDB.tables.split(",");
				boolean valid=false;
				for(String var : typeArray) 
				{
					if(value.equals(var)) 
					{
						valid=true;
						break;
					}
				}
				if(!valid) 
				{
					throw new Exception("Product_Type is not in Range");
				}
				break;
			case "Test_Station":
				break;
			case "Test_Required":
				break;
			case "SN":
				if(value.length()!=13) 
				{
					throw new Exception("SN Length is not 13");
				}
				break;
			case "MAC":
				break;
			case "Product_Model":
				break;
			case "PC_Name":
				break;
			case "TestResult":
				if(value.toUpperCase()!="PASS"||value.toUpperCase()!="FAIL") 
				{
					throw new Exception("TestResult is not PASS or FAIL");
				}
				break;
			case "StartTime":
			case "EndTime":
			case "Record_Time":
				break;
			default:
				//throw new Exception(key+" is invalid condition!");
				break;
			}
		}
	}
	//锟斤拷时锟斤拷锟阶拷锟轿憋拷锟斤拷址锟斤拷锟�
	private String timeStamp2Date(String timeStampStr) 
	{
		String formats="yyyy-MM-dd HH:mm:ss";
		Long timestamp=Long.parseLong(timeStampStr);
		String date=new SimpleDateFormat(formats,Locale.CHINA).format(new Date(timestamp));
		return date;
	}
	//锟斤拷URL转锟斤拷为UTF-8锟斤拷式
	private String getURLDecode(String str)throws Exception 
	{
		try{
			return URLDecoder.decode(str,"UTF-8");
		}catch(Exception e){
			throw e;
		}
	}
	private String getQueryParameter(String queryStr,String key){
		String [] parameterValue=queryStr.split("&");
		Map<String,String> para=new HashMap<String,String>();
		for(String var:parameterValue) 
		{
			para.put(var.split("=")[0], var.split("=")[1]);
		}
		if(para.containsKey(key)) {
			return para.get(key);
		}else {
			return null;
		}	
	}
	@Test
	public void testSQL() 
	{
		//HttpServletRequest request =new HttpServletRequest();
		MockHttpServletRequest request= new MockHttpServletRequest();
		//request.;
		request.setCharacterEncoding("UTF-8");
		request.setRequestURI("/searchdata");
		request.setQueryString("searchMode=ProductInfo&Offset=0&Limit=50&SN=G1N40PP00214C");
		String searchMode=request.getParameter("Offset");
		//String searchMode=request.getRequestURL().toString();
		try {
			String SQL=generateSQL(request);
			System.out.print(request.getMethod());
		}catch(Exception e) {
			System.out.print(e.getMessage());
		}
		//System.out.print("TEST");
	}
}
