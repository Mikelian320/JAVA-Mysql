package cn.greatwebtech.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.codec.json.Jackson2CodecSupport;

import cn.greatwebtech.dao.impl.TestRecordDaoImpl;
import cn.greatwebtech.service.ISearchService;
import cn.greatwebtech.createfile.WriteLogInLocal;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//RG-IS2712G_总检_以管理板为主_G1N40PP002696_80058845DBE2_PASS_2019-05-20 10_59_17.0.txt
public class PackageTestLogs implements ISearchService{
    private TestRecordDaoImpl testDB;
	private DealQueryString dealQS;
    private final String selectStr="SELECT Product_Model,Test_Station,Test_Require,SN,MAC,TestResult,Record_Time,Log";
	private String dirPath=null;
	private String dirName=null;
	WriteLogInLocal writeLog= null;
	public void setTestDB(TestRecordDaoImpl testDB) {
		this.testDB = testDB;
	}
	public void setDealQS(DealQueryString dealQS) {
		this.dealQS = dealQS;
	}
	public String getFilePath(){
		return this.dirPath;
	}
	public String getZipName(){
		return this.dirName+"TestLog.zip";
	}
    public PackageTestLogs()
    {
		this.writeLog=new WriteLogInLocal();
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
					SQLString=selectStr+" FROM "+tables.get(0)+" WHERE "+condition+" ORDER BY Record_Time DESC "+limit;
				}else {
					SQLString=selectStr+" FROM "+tables.get(0)+" ORDER BY Record_Time DESC "+limit;
				}

			}else {
				for(String table:tables) 
				{
					if(SQLString!="") 
					{
						SQLString+=" UNION ";
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
    //yyyy/MM/dd HH:mm:ss
    private  String getTime(String format){
        SimpleDateFormat dateFormat=new SimpleDateFormat(format);
        String currentTime=dateFormat.format(new Date());
        return currentTime;
	}
	
	private File createDirectory()
    {
		this.dirName=getTime("yyyy-MM-dd-HH-mm-ss");
		this.dirPath=System.getProperty("user.dir")+"/"+dirName;
        File file = new File(this.dirPath);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
	}
	
    public void writeLogsInLocal(JSONArray searchData) throws Exception
    {
        try {
			createDirectory();
            for (Object result : searchData) {
                JSONArray testResult= (JSONArray)result;
                String fileName=String.format("%s_%s_%s_%s_%s_%s_%s.txt", testResult.get(0),testResult.get(1),testResult.get(2), testResult.get(3),testResult.get(4),testResult.get(5),testResult.get(6).toString().replace(' ', '_').replace(':', '_'));
                writeLog.writeDataInLocal(dirPath,fileName, testResult.getString(7),false);
			}
        } catch (Exception e) {
            throw e;
            //TODO: handle exception
        }
	}

	public void deleteDirAndFile() throws Exception
	{
		try {
			writeLog.deleteDirAndFile(dirPath);
		} catch (Exception e) {
			throw e;
			//TODO: handle exception
		}
	}
	public void compressToZip(OutputStream out)throws RuntimeException
	{
		try {
			writeLog.compressToZip(dirPath, out, false);
		} catch (Exception e) {
			throw e;
			//TODO: handle exception
		}
	}
}