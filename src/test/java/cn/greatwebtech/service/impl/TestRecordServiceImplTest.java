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
	TestLogServiceImpl TLService=null;
	TestCountServiceImpl TCService=null;
	String SqlWithTable="SELECT Slot,Test_Station,Test_Require,Product_Model,SN,MAC,Record_Time,PC_Name,ATE_Version,Hardware_Version,Software_Version,Software_Number,Boot_Version,TestResult FROM ml_switch WHERE Test_Station='总检' AND SN='G1N40PP00214C'  ORDER BY Record_Time DESC limit 0,50";
	String SqlWithoutTable="SELECT Slot,Test_Station,Test_Require,Product_Model,SN,MAC,Record_Time,PC_Name,ATE_Version,Hardware_Version,Software_Version,Software_Number,Boot_Version,TestResult FROM ml_switch WHERE Test_Station='总检' AND SN='G1N40PP00214C'"
	+"  UNION SELECT Slot,Test_Station,Test_Require,Product_Model,SN,MAC,Record_Time,PC_Name,ATE_Version,Hardware_Version,Software_Version,Software_Number,Boot_Version,TestResult FROM securitypro WHERE Test_Station='总检' AND SN='G1N40PP00214C'"
	+"  ORDER BY Record_Time DESC limit 0,50";
	
	@Before
	public void init() 
	{
		request.setCharacterEncoding("UTF-8");
		TRService=(TestRecordServiceImpl)context.getBean("TestRecordService");
		TLService=(TestLogServiceImpl)context.getBean("TestLogService");
		TCService=(TestCountServiceImpl)context.getBean("TestCountService");
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
			String SQL=TLService.generateSQL(request);
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
	public void testSearchLog1()
	{
		//http://table.greatwebtech.cn/search/searchdata?searchMode=Log&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================查询Log测试1=================");
			request.setQueryString("searchMode=Log&SN=G1N40PP002696&PC_Name=BFEBFBFF000506E3&Record_Time=1558321157000&Slot=Slot+M1");
			JSONArray result=TLService.searchData(TLService.generateSQL(request));
			//System.out.println(result.toString());
		}catch(Exception e){
			//System.out.println(e.getMessage());
			assertTrue("查询LOG测试失败，异常信息:"+e.getMessage(), false);
		}
	}

/* 	@Test
	public void testSearchLog2()
	{
		//http://table.greatwebtech.cn/search/searchdata?searchMode=Log&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================查询Log测试2=================");
			request.setQueryString("searchMode=Log&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis");
			System.out.println("查询LOG的SQL语句："+TLService.generateSQL(request));
			JSONArray result=TLService.searchData(TLService.generateSQL(request));
			assertTrue("查询测试记录失败，查询结果集为空", !result.isEmpty());
			//System.out.println(result.toString());
		}catch(Exception e){
			//System.out.println(e.getMessage());
			assertTrue("查询LOG测试失败，异常信息:"+e.getMessage(), false);
		}
	} */

	@Test
	public void testSearchByTesrRequire()
	{
		try {
			System.out.println("===================按测试需求查询测试记录=================");
			request.setQueryString("searchMode=ProductInfo&Offset=0&Limit=50&SN=G1N40PP00214C&Product_Type=ml_switch&Test_Require=以管理板为主&Test_Station=总检");
			JSONArray result=TRService.searchData(TRService.generateSQL(request));
			System.out.println(result.toString());
			assertTrue("查询测试记录失败，查询结果集为空", !result.isEmpty());
		}catch(Exception e){
			assertTrue("查询测试记录失败，异常信息:"+e.getMessage(), false);
			//System.out.println(e.getMessage());
		}
	}

	@Test
	public void testSearchByTestResultFail()
	{
		try {
			System.out.println("===================按测试结果(FAIL)查询测试记录=================");
			request.setQueryString("searchMode=ProductInfo&Offset=0&Limit=50&TestResult=FAIL");
			JSONArray result=TRService.searchData(TRService.generateSQL(request));
			assertTrue("查询测试记录失败，查询结果集为空", !result.isEmpty());
		}catch(Exception e){
			assertTrue("查询测试记录失败，异常信息:"+e.getMessage(), false);
			//System.out.println(e.getMessage());
		}
	}

	@Test
	public void testSearchByTestResultPass()
	{
		try {
			System.out.println("===================按测试结果(PASS)查询测试记录=================");
			request.setQueryString("searchMode=ProductInfo&Offset=0&Limit=50&TestResult=PASS");
			JSONArray result=TRService.searchData(TRService.generateSQL(request));
			assertTrue("查询测试记录失败，查询结果集为空", !result.isEmpty());
		}catch(Exception e){
			assertTrue("查询测试记录失败，异常信息:"+e.getMessage(), false);
			//System.out.println(e.getMessage());
		}
	}


	@Test
	public void testCount1()
	{
		//searchMode=TestCount&Offset=0&Limit=50&Product_Model=S5750-24GT8SFP-P&Product_Type=ml_switch&Test_Require=以管理板为主&Test_Station=总检
		try {
			System.out.println("===================查询测试记录总数1=================");
			request.setQueryString("searchMode=TestCount&Offset=0&Limit=50&Product_Model=S5750-24GT8SFP-P&Product_Type=ml_switch&Test_Require=以管理板为主");
			//TCService.generateSQL(request);
			System.out.println("SQL语句："+TCService.generateSQL(request));
			long startTime= System.currentTimeMillis();
			JSONArray result=TCService.searchData(TCService.generateSQL(request));
			long endTime =System.currentTimeMillis();
			System.out.println("返回结果："+result.toString());
			System.out.printf("查询时间：%dms\n", (endTime-startTime));
			assertTrue("查询测试记录失败，查询结果集为空", !result.isEmpty());
			assertTrue("返回值异常", result.size()==1);
		}catch(Exception e){
			assertTrue("查询测试记录失败，异常信息:"+e.getMessage(), false);
			//System.out.println(e.getMessage());
		}
	}
	@Test
	public void testCount2()
	{
		//searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================查询测试记录总数2=================");
			request.setQueryString("searchMode=TestCount&Offset=0&Limit=50&Product_Model=S5750-24GT8SFP-P&Product_Type=ml_switch&Test_Require=以管理板为主&Test_Station=总检");
			//TCService.generateSQL(request);
			System.out.println("SQL语句："+TCService.generateSQL(request));
			long startTime= System.currentTimeMillis();
			JSONArray result=TCService.searchData(TCService.generateSQL(request));
			long endTime =System.currentTimeMillis();
			System.out.println("返回结果："+result.toString());
			System.out.printf("查询时间：%dms\n", (endTime-startTime));
			assertTrue("查询测试记录失败，查询结果集为空", !result.isEmpty());
			assertTrue("返回值异常", result.size()==1);
		}catch(Exception e){
			assertTrue("查询测试记录失败，异常信息:"+e.getMessage(), false);
			//System.out.println(e.getMessage());
		}
	}

	@Test
	public void testCount3()
	{
		//searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================查询测试记录总数3=================");
			request.setQueryString("searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis");
			//TCService.generateSQL(request);
			System.out.println("SQL语句："+TCService.generateSQL(request));
			long startTime= System.currentTimeMillis();
			JSONArray result=TCService.searchData(TCService.generateSQL(request));
			long endTime =System.currentTimeMillis();
			System.out.println("返回结果："+result.toString());
			System.out.printf("查询时间：%dms\n", (endTime-startTime));
			assertTrue("查询测试记录失败，查询结果集为空", !result.isEmpty());
			assertTrue("返回值异常", result.size()==1);
		}catch(Exception e){
			assertTrue("查询测试记录失败，异常信息:"+e.getMessage(), false);
			//System.out.println(e.getMessage());
		}
	}
	@Test
	public void testCount4()
	{
		//searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================查询测试记录总数4=================");
			request.setQueryString("searchMode=TestCount&Offset=0&Limit=50&SN=1234567890123");
			//TCService.generateSQL(request);
			System.out.println("SQL语句："+TCService.generateSQL(request));
			long startTime= System.currentTimeMillis();
			JSONArray result=TCService.searchData(TCService.generateSQL(request));
			long endTime =System.currentTimeMillis();
			System.out.printf("查询时间：%dms\n", (endTime-startTime));
			System.out.println("返回结果："+result.toString());
			assertTrue("查询测试记录失败，查询结果集为空", !result.isEmpty());
			assertTrue("返回值异常", result.size()==1);
		}catch(Exception e){
			assertTrue("查询测试记录失败，异常信息:"+e.getMessage(), false);
			//System.out.println(e.getMessage());
		}
	}
	@Test
	public void testCount5()
	{
		//searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================查询测试记录总数5=================");
			request.setQueryString("searchMode=TestCount");
			//TCService.generateSQL(request);
			System.out.println("SQL语句："+TCService.generateSQL(request));
			long startTime= System.currentTimeMillis();
			JSONArray result=TCService.searchData(TCService.generateSQL(request));
			long endTime =System.currentTimeMillis();
			System.out.printf("查询时间：%dms\n", (endTime-startTime));
			System.out.println("返回结果："+result.toString());
			assertTrue("查询测试记录失败，查询结果集为空", !result.isEmpty());
			assertTrue("返回值异常", result.size()==1);
		}catch(Exception e){
			assertTrue("查询测试记录失败，异常信息:"+e.getMessage(), false);
			//System.out.println(e.getMessage());
		}
	}


}
