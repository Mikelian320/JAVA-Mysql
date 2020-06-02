package cn.greatwebtech.service.impl;

import static org.junit.Assert.assertTrue;

import org.junit.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;

import net.sf.json.JSONObject;

public class SumTestRecordImplTest {
    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    MockHttpServletRequest request= new MockHttpServletRequest();
    SumTestRecordImpl  STService=null;
    @Before
	public void init() 
	{
		request.setCharacterEncoding("UTF-8");
        STService=(SumTestRecordImpl)context.getBean("SumTestRecordService");
    }
    @Test
    public void sumTest(){
        try {
            JSONObject result= STService.getTableSumTestRecord();
            System.out.println(result.toString());            
        } catch (Exception e) {
            assertTrue("查询测试记录，异常信息:"+e.getMessage(), false);
            //TODO: handle exception
        }
    }
}