package cn.greatwebtech.service.impl;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.jdbc.Sql;

import net.sf.json.JSONArray;

//可以将输入控件条件随机组合进行测试



public class TestRecordServiceImplTest {
	//TestRecordServiceImpl TestRecordService = new TestRecordServiceImpl();
	//MockHttpServletRequest request= new MockHttpServletRequest();
	ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	MockHttpServletRequest request= new MockHttpServletRequest();
	TestRecordServiceImpl TRService=null;
	String SqlWithTable="SELECT Slot,Test_Station,Test_Require,Product_Model,SN,MAC,Record_Time,PC_Name,ATE_Version,Hardware_Version,Software_Version,Software_Number,Boot_Version,TestResult FROM ml_switch WHERE Test_Station='总检' AND SN='G1N40PP00214C'  ORDER BY Record_Time DESC limit 0,50";
	String SqlWithoutTable="SELECT Slot,Test_Station,Test_Require,Product_Model,SN,MAC,Record_Time,PC_Name,ATE_Version,Hardware_Version,Software_Version,Software_Number,Boot_Version,TestResult FROM ml_switch WHERE Test_Station='总检' AND SN='G1N40PP00214C'"
	+"  UNION SELECT Slot,Test_Station,Test_Require,Product_Model,SN,MAC,Record_Time,PC_Name,ATE_Version,Hardware_Version,Software_Version,Software_Number,Boot_Version,TestResult FROM securitypro WHERE Test_Station='总检' AND SN='G1N40PP00214C'"
	+"  ORDER BY Record_Time DESC limit 0,50";
	
	@Before
	public void init() 
	{
		request.setCharacterEncoding("UTF-8");
		TRService=(TestRecordServiceImpl)context.getBean("TestRecordService");
	}
	
	@Test
	public void testSQL() 
	{
		String a="ss";
		String b="ss";
		assertTrue(a==b);
	}
	
	@Test
	public void testSQLWithoutTable() 
	{
		request.setQueryString("searchMode=ProductInfo&Offset=0&Limit=50&SN=G1N40PP00214C&Test_Station=总检");
		/*request.setParameter("searchMode", "ProductInfo");
		
		request.setParameter("Product_Type", "ml_switch");
		request.setParameter("StartTime", "1572437686000");
		request.setParameter("EndTime", "1573820086000");
		request.setParameter("Offset", "0");
		request.setParameter("Limit", "50");
		request.setParameter("SN", "G1N40PP002143");*/
		try {
			System.out.println("===========测试不带表格SQL语句============");
			String SQL=TRService.generateSQL(request);
			System.out.println(SQL);
			assertTrue(SQL, SQL.equals(SqlWithoutTable));
		}catch(Exception e) {
			System.out.print(e.getMessage());
		}
		//System.out.print("TEST");
	}
	@Test
	public void testSQLWithTable() 
	{
		System.out.println("===========测试带表格SQL语句===============");
		request.setQueryString("searchMode=ProductInfo&Offset=0&Limit=50&SN=G1N40PP00214C&Product_Type=ml_switch&Test_Station=总检");
		try {
			String SQL=TRService.generateSQL(request);
			System.out.println(SQL);
			assertTrue(SQL, SQL.equals(SqlWithTable));
		}catch(Exception e) {
			System.out.print(e.getMessage());
		}
	}
	@Test
	public void testSQLLog() {
/* 		System.out.print("SQLWithTable: ");
		request.setParameter("searchMode", "Log");
		request.setParameter("Product_Type", "ml_switch");
		//request.setParameter("Offset", "0");
		request.setParameter("PC_Name", "AAABBBCCC");
		request.setParameter("Record_Time", "1572437686000");
		//request.setParameter("EndTime", "1573820086000");
		//request.setParameter("Test_Station", "�ܼ�");
		request.setParameter("Slot", "Chassis"); */
		request.setQueryString("searchMode=Log&SN=G1N40PP002696&PC_Name=BFEBFBFF000506E3&Record_Time=1558321157000&Slot=Slot+M1");
		//request.que
		try {
			System.out.println("===========测试查询LOG SQL语句===============");
			String SQL=TRService.generateSQL(request);
			System.out.println(SQL);
		}catch(Exception e) {
			System.out.print(e.getMessage());
		}
	}
	//测试AOP功能，看是否能够在执行查询语句前记录查询语句（前置通知），在执行语句报错时记录错误信息（异常通知）
	@Test
	public void testSearchRecord() 
	{
		try {
			System.out.println("===================查询测试记录=================");
			request.setQueryString("searchMode=ProductInfo&Offset=0&Limit=50&SN=G1N40PP00214C&Product_Type=ml_switch&Test_Station=总检");
			JSONArray result=TRService.searchData(TRService.generateSQL(request));
			//System.out.println(result.toString());
			assertTrue("查询测试记录失败，查询结果集为空", !result.isEmpty());
		}catch(Exception e){
			assertTrue("查询测试记录失败，异常信息:"+e.getMessage(), false);
			//System.out.println(e.getMessage());
		}
	}
	@Test
	public void testSearchLog()
	{
		try {
			System.out.println("===================查询Log测试=================");
			request.setQueryString("searchMode=Log&SN=G1N40PP002696&PC_Name=BFEBFBFF000506E3&Record_Time=1558321157000&Slot=Slot+M1");
			TRService.searchData(TRService.generateSQL(request));
			//System.out.println(result.toString());
		}catch(Exception e){
			//System.out.println(e.getMessage());
			assertTrue("查询LOG测试失败，异常信息:"+e.getMessage(), false);
		}
	}


}
