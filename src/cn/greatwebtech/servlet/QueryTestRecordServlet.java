package cn.greatwebtech.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.greatwebtech.service.ITestRecordService;
import cn.greatwebtech.service.impl.TestRecordServiceImpl;

/**
 * Servlet implementation class QueryTestRecordServlet
 */
@WebServlet("/QueryTestRecordServlet")
public class QueryTestRecordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ITestRecordService TRService;
    
	@Override
	public void init() throws ServletException
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		TRService=(ITestRecordService)context.getBean("TestRecordService");
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
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String queryString=request.getQueryString();
		//String searchMode=req
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
