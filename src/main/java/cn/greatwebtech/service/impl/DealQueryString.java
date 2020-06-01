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

import net.sf.json.JSONObject;

public class DealQueryString {
	private final String [] searchArr={"Slot","Product_Type","Test_Station","Test_Require","Product_Model","SN","MAC","PC_Name","TestResult","StartTime","EndTime","Record_Time","Offset","Limit"};
	//private final String [] productTypeArray={"ac","ap","ml_switch","ml_router","h_switch","h_router","securitypro"};
	private String tables;
    public JSONObject condition2Json(HttpServletRequest request)throws Exception 
	{
		JSONObject searchCon= new JSONObject();
		//String [] searchArr={"Slot","Product_Type","Test_Station","Test_Require","Product_Model","SN","MAC","PC_Name","TestResult","StartTime","EndTime","Record_Time","Offset","Limit"};
		for(String key : searchArr) 
		{
			String value = getQueryParameter(request.getQueryString(),key);
			if(value!=null) 
			{
				/*
				 * /Key which needs transfer
				 * 1Slot(SLOT+M1-->SLOT M1)
				 * 2Test_Station转化为UTF-8编码(UTF-8)
				 * 3时间戳转换为字符串(TimeStamp-->String)
				 */
				try 
				{
					switch(key) 
					{
						case "Slot":
						case "PC_Name":
							value=value.replace("+", " ");
							break;
						case "Test_Station":
						case "Test_Require":
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

    private String timeStamp2Date(String timeStampStr) 
	{
		String formats="yyyy-MM-dd HH:mm:ss";
		Long timestamp=Long.parseLong(timeStampStr);
		String date=new SimpleDateFormat(formats,Locale.CHINA).format(new Date(timestamp));
		return date;
    }
    
    private String getURLDecode(String str)throws Exception 
	{
		try{
			return URLDecoder.decode(str,"UTF-8");
		}catch(Exception e){
			throw e;
		}
    }
    public String getQueryParameter(String queryStr,String key){
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
    public String getLimitCondition(JSONObject searchCon) 
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
    
    public void validateSearchConditon(JSONObject searchCon) throws Exception
	{
		Iterator<String> iterator = searchCon.keys();
		String []productTypeArray=tables.split(",");
		while(iterator.hasNext()) 
		{
			String key=iterator.next();
			String value=searchCon.getString(key);
			switch(key) 
			{
			case "Slot":
				break;
			case "Product_Type":
				boolean valid=false;
				for(String var : productTypeArray) 
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
			case "Test_Require":
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
				if(!value.toUpperCase().equals("PASS")&&!value.toUpperCase().equals("FAIL")) 
				{
					throw new Exception("TestResult is not PASS or FAIL");
				}
				break;
			case "StartTime":
			case "EndTime":
			case "Record_Time":
			case "Offset":
			case "Limit":
				break;
			default:
				throw new Exception(key+" is invalid condition!");
				//break;
			}
		}
    }
    public String getWhereCondition(JSONObject searchCon) 
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
	public List<String> getTables(JSONObject searchCon) 
	{
		List<String> result=new ArrayList<String>();
		if(searchCon.containsKey("Product_Type")) 
		{
			result.add(searchCon.getString("Product_Type"));
		}else {
			String [] tableArray=tables.split(",");
			for(String var : tableArray) 
			{
				result.add(var);
			}
		}
		return result;
	}

	public String getTables() {
		return tables;
	}

	public void setTables(String tables) {
		this.tables = tables;
	}
}