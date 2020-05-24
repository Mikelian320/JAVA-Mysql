package cn.greatwebtech.service.impl;

import org.junit.*;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;

import net.sf.json.JSONArray;

public class TestCountServiceImplTest {
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    MockHttpServletRequest request= new MockHttpServletRequest();
    TestCountServiceImpl TCService=null;
    TestCount2ServiceImpl TC2Service=null;
    @Before
	public void init() 
	{
		request.setCharacterEncoding("UTF-8");
        TCService=(TestCountServiceImpl)context.getBean("TestCountService");
        TC2Service=(TestCount2ServiceImpl)context.getBean("TestCount2Service");
	}
    @Test
	public void testCountSql_Calc_Found_Rows1()
	{
		//searchMode=TestCount&Offset=0&Limit=50&Product_Model=S5750-24GT8SFP-P&Product_Type=ml_switch&Test_Require=以管理板为主&Test_Station=总检
		try {
			System.out.println("===================sql_calc_found_rows查询测试记录总数1=================");
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
	public void testCountSql_Calc_Found_Rows2()
	{
		//searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================sql_calc_found_rows查询测试记录总数2=================");
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
	public void testCountSql_Calc_Found_Rows3()
	{
		//searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================sql_calc_found_rows查询测试记录总数3=================");
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
	public void testCountSql_Calc_Found_Rows4()
	{
		//searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================sql_calc_found_rows查询测试记录总数4=================");
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
	public void testCountSql_Calc_Found_Rows5()
	{
		//searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================sql_calc_found_rows查询测试记录总数5=================");
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

    @Test
	public void testCountSelect_Count1()
	{
		//searchMode=TestCount&Offset=0&Limit=50&Product_Model=S5750-24GT8SFP-P&Product_Type=ml_switch&Test_Require=以管理板为主&Test_Station=总检
		try {
			System.out.println("===================SELECT COUNT(*)查询测试记录总数1=================");
			request.setQueryString("searchMode=TestCount&Offset=0&Limit=50&Product_Model=S5750-24GT8SFP-P&Product_Type=ml_switch&Test_Require=以管理板为主");
			//TCService.generateSQL(request);
			System.out.println("SQL语句："+TC2Service.generateSQL(request));
			long startTime= System.currentTimeMillis();
			JSONArray result=TC2Service.searchData(TC2Service.generateSQL(request));
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
	public void testCountSelect_Count2()
	{
		//searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================SELECT COUNT(*)查询测试记录总数2=================");
			request.setQueryString("searchMode=TestCount&Offset=0&Limit=50&Product_Model=S5750-24GT8SFP-P&Product_Type=ml_switch&Test_Require=以管理板为主&Test_Station=总检");
			//TCService.generateSQL(request);
			System.out.println("SQL语句："+TC2Service.generateSQL(request));
			long startTime= System.currentTimeMillis();
			JSONArray result=TC2Service.searchData(TC2Service.generateSQL(request));
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
	public void testCountSelect_Count3()
	{
		//searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================SELECT COUNT(*)查询测试记录总数3=================");
			request.setQueryString("searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis");
			//TCService.generateSQL(request);
			System.out.println("SQL语句："+TC2Service.generateSQL(request));
			long startTime= System.currentTimeMillis();
			JSONArray result=TC2Service.searchData(TC2Service.generateSQL(request));
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
	public void testCountSelect_Count4()
	{
		//searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================SELECT COUNT(*)查询测试记录总数4=================");
			request.setQueryString("searchMode=TestCount&Offset=0&Limit=50&SN=1234567890123");
			//TCService.generateSQL(request);
			System.out.println("SQL语句："+TC2Service.generateSQL(request));
			long startTime= System.currentTimeMillis();
			JSONArray result=TC2Service.searchData(TC2Service.generateSQL(request));
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
    public void testCountSelect_Count5()
	{
		//searchMode=TestCount&SN=G1N408A000560&PC_Name=BFEBFBFF000506E3_CELL+3&Record_Time=1557571812000&Slot=Chassis
		try {
			System.out.println("===================SELECT COUNT(*)查询测试记录总数5=================");
			request.setQueryString("searchMode=TestCount");
			//TCService.generateSQL(request);
			System.out.println("SQL语句："+TC2Service.generateSQL(request));
			long startTime= System.currentTimeMillis();
			JSONArray result=TC2Service.searchData(TC2Service.generateSQL(request));
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