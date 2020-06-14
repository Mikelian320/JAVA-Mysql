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

import cn.greatwebtech.service.ISearchService;
import cn.greatwebtech.service.impl.DealQueryString;
import cn.greatwebtech.service.impl.PackageTestLogs;
import cn.greatwebtech.service.impl.SelectCountServiceImpl;
import cn.greatwebtech.service.impl.SumTestRecordImpl;
import cn.greatwebtech.service.impl.TestLogServiceImpl;
import cn.greatwebtech.service.impl.TestRecordServiceImpl;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class QueryTestRecordServlet
 */
@WebServlet("/QueryTestRecordServlet")
public class QueryTestRecordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DealQueryString dealQS =new DealQueryString();
	private TestRecordServiceImpl TRService;
	private TestLogServiceImpl TLService;
	private SelectCountServiceImpl TCService;
	private PackageTestLogs PackService;
	private SumTestRecordImpl  STService;
	//private ISearchService ITService;
    
	@Override
	public void init() throws ServletException
	{
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		TRService=(TestRecordServiceImpl)context.getBean("TestRecordService");
		TLService=(TestLogServiceImpl)context.getBean("TestLogService");
		TCService=(SelectCountServiceImpl)context.getBean("SelectCountService");
		PackService=(PackageTestLogs)context.getBean("PackageTestLogs");
		STService=(SumTestRecordImpl)context.getBean("SumTestRecordService");
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
		PrintWriter out=null;
		//JSONArray result=new JSONArray();
		String result="";
		try 
		{
			String searchMode=dealQS.getQueryParameter(request.getQueryString(), "searchMode");
			switch (searchMode) {
				case "ProductInfo":
					result=TRService.searchData(TRService.generateSQL(request)).toString();
					break;
				case "Log":
					result=TLService.searchData(TLService.generateSQL(request)).toString();
				break;
				case "TestCount":
					result=TCService.searchData(TCService.generateSQL(request)).toString();
				break;
				case "PackageTestLogs":
					PackService.writeLogsInLocal(PackService.searchData(PackService.generateSQL(request)));
				break;
				case "SumTestRecord":
					result=STService.getTableSumTestRecord().toString();
				break;
				default:
					throw new Exception("No "+searchMode+" Available");
					//break;
			}
			if (!searchMode.equals("PackageTestLogs")) {
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/javascript;charset=utf-8");
				response.setHeader("content-type", "application/json;charset=utf-8");
				out=response.getWriter();
				out.println(result.toString());
				response.setStatus(200);
				out.close();
			}else{
				response.reset(); 
				response.setCharacterEncoding("UTF-8");
			  	response.setContentType("application/x-msdownload");
				response.setHeader("Content-Disposition", "attachment; filename="+PackService.getZipName());  
				PackService.compressToZip(response.getOutputStream());
				PackService.deleteDirAndFile();
			}
		}
		catch(Exception e)
		{
			response.setStatus(500);
			response.sendError(500, e.getMessage());
			//out.println("Error:"+e.toString());
		}
		finally 
		{
			//out.close();
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
