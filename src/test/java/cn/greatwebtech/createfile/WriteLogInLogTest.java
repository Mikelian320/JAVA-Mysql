package cn.greatwebtech.createfile;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;

import cn.greatwebtech.service.impl.PackageTestLogs;
import net.sf.json.JSONArray;

public class WriteLogInLogTest {

    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    MockHttpServletRequest request = new MockHttpServletRequest();
    PackageTestLogs PackService = null;
    WriteLogInLocal wLocal;

	
	@Before
	public void init() 
	{
		request.setCharacterEncoding("UTF-8");
        PackService=(PackageTestLogs)context.getBean("PackageTestLogs");
        wLocal=new WriteLogInLocal("D:/aaa/");

    }
    @Test
	public void writeSingleLog() 
	{
		System.out.println("===========在本地存储单个测试日志===============");
		request.setQueryString("searchMode=ProductInfo&Offset=0&Limit=50&SN=G1N40PP00214C&Product_Type=ml_switch&Test_Station=总检");
		try {
            String SQL=PackService.generateSQL(request);
            JSONArray result=PackService.searchData(SQL);
            PackService.writeLogsInLocal(result);
			//System.out.println(result.toString());
			//assertTrue(SQL, SQL.equals(SqlWithTable));
		}catch(Exception e) {
			System.out.print(e.getMessage());
		}
    }
    @Test
	public void writeMultiLog1() 
	{
		System.out.println("===========在本地存储多个测试日志1==============");
		request.setQueryString("searchMode=ProductInfo&Offset=0&Limit=50&SN=1234567890123&Product_Type=ml_switch&Test_Station=总检");
		try {
            String SQL=PackService.generateSQL(request);
            JSONArray result=PackService.searchData(SQL);
            System.out.println(result.size());
            PackService.writeLogsInLocal(result);
			//System.out.println(result.toString());
			//assertTrue(SQL, SQL.equals(SqlWithTable));
		}catch(Exception e) {
			System.out.print(e.getMessage());
		}
    }
    @Test
	public void PackageTestLogs() 
	{
		System.out.println("===========在本地存打包测试日志===============");
		request.setQueryString("searchMode=ProductInfo&Offset=0&Limit=50&SN=1234567890123&Product_Type=ml_switch");
		try {
            String SQL=PackService.generateSQL(request);
            JSONArray result=PackService.searchData(SQL);
            System.out.println(result.size());
			PackService.writeLogsInLocal(result);
			FileOutputStream fos1 = new FileOutputStream(new File(PackService.getFilePath()+"TestLog.zip"));
			PackService.compressToZip(fos1);
			PackService.deleteDirAndFile();
			//System.out.println(result.toString());
			//assertTrue(SQL, SQL.equals(SqlWithTable));
		}catch(Exception e) {
			System.out.print(e.getMessage());
		}
    }
}