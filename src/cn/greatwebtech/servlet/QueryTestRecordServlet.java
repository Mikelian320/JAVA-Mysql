package cn.greatwebtech.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.greatwebtech.service.ITestRecordService;
import cn.greatwebtech.service.impl.TestRecordServiceImpl;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class QueryTestRecordServlet
 */
@WebServlet("/QueryTestRecordServlet")
public class QueryTestRecordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TestRecordServiceImpl TRService;
    
	@Override
	public void init() throws ServletException
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		TRService=(TestRecordServiceImpl)context.getBean("TestRecordService");
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryTestRecordServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String queryStr="";
		JSONArray result=new JSONArray();
		response.setCharacterEncoding("utf-8");
	    response.setContentType("text/javascript;charset=utf-8");
	    response.setHeader("content-type", "application/json;charset=utf-8");
		PrintWriter out=response.getWriter();
		try 
		{
			queryStr=TRService.generateSQL(request);
			result=TRService.searchData(queryStr);
			response.setStatus(200);
			out.println(result.toString());
		}
		catch(Exception e)
		{
			response.setStatus(500);
			out.println("Error:"+e.toString());
		}
		finally 
		{
			
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
