package cn.greatwebtech.service.impl;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;

public class TestTestRecordServiceImpl {
	//TestRecordServiceImpl TestRecordService = new TestRecordServiceImpl();
	//MockHttpServletRequest request= new MockHttpServletRequest();
	ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
	MockHttpServletRequest request= new MockHttpServletRequest();
	TestRecordServiceImpl TRService=null;
	
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
		System.out.print("SQLWithoutTable: ");
		request.setQueryString("searchMode=ProductInfo&Offset=0&Limit=50&SN=G1N40PP00214C&Test_Station=ï¿½Ü¼ï¿½");
		/*request.setParameter("searchMode", "ProductInfo");
		
		request.setParameter("Product_Type", "ml_switch");
		request.setParameter("StartTime", "1572437686000");
		request.setParameter("EndTime", "1573820086000");
		request.setParameter("Offset", "0");
		request.setParameter("Limit", "50");
		request.setParameter("SN", "G1N40PP002143");*/
		try {
			String SQL=TRService.generateSQL(request);
			System.out.print(SQL);
		}catch(Exception e) {
			System.out.print(e.getMessage());
		}
		//System.out.print("TEST");
	}
	@Test
	public void testSQLWithTable() 
	{
		System.out.print("SQLWithTable: ");
		request.setQueryString("searchMode=ProductInfo&Offset=0&Limit=50&SN=G1N40PP00214C&Product_Type=ml_switch&Test_Station=×Ü¼ì");
		try {
			String SQL=TRService.generateSQL(request);
			System.out.print(SQL);
		}catch(Exception e) {
			System.out.print(e.getMessage());
		}
	}
	@Test
	public void testSQLLog() {
		System.out.print("SQLWithTable: ");
		request.setParameter("searchMode", "Log");
		request.setParameter("Product_Type", "ml_switch");
		//request.setParameter("Offset", "0");
		request.setParameter("PC_Name", "AAABBBCCC");
		request.setParameter("Record_Time", "1572437686000");
		//request.setParameter("EndTime", "1573820086000");
		//request.setParameter("Test_Station", "×Ü¼ì");
		request.setParameter("Slot", "Chassis");
		request.setRequestURI("searchMode=ProductInfo&Offset=0&Limit=50&SN=G1N40PP00214C");
		//request.que
		try {
			String SQL=TRService.generateSQL(request);
			System.out.print(SQL);
		}catch(Exception e) {
			System.out.print(e.getMessage());
		}
	}
	@Test
	public void testAOP() 
	{
		try {
			TRService.searchData("");
		}catch(Exception e){
			
		}
	}

}
