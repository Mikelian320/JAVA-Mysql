package cn.greatwebtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.greatwebtech.service.impl.DealQueryString;
import cn.greatwebtech.service.impl.PackageTestLogs;
import cn.greatwebtech.service.impl.SelectCountServiceImpl;
import cn.greatwebtech.service.impl.TestLogServiceImpl;
import cn.greatwebtech.service.impl.TestRecordServiceImpl;
import cn.greatwebtech.service.ISearchService;

import net.sf.json.JSONArray;

@SpringBootApplication
@EnableAutoConfiguration
@RestController
public class DemoApplication {

    private DealQueryString dealQS = new DealQueryString();
    private TestRecordServiceImpl TRService;
    private TestLogServiceImpl TLService;
    private SelectCountServiceImpl TCService;
    private PackageTestLogs PackService;
    private ISearchService ITService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(
                "SELECT sql_calc_found_rows SN,MAC FROM ml_switch WHERE Test_Station='总检' AND Test_Require='以管理板为主' AND Product_Model='S5750-24GT8SFP-P' ORDER BY Record_Time DESC limit 0,50");
        return result.toString();
    }

    @RequestMapping("/searchdata")
    public void searchData() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        TRService = (TestRecordServiceImpl) context.getBean("TestRecordService");
        TLService = (TestLogServiceImpl) context.getBean("TestLogService");
        TCService = (SelectCountServiceImpl) context.getBean("SelectCountService");
        PackService = (PackageTestLogs) context.getBean("PackageTestLogs");
        String queryStr = "";
        PrintWriter out = null;
        JSONArray result = new JSONArray();
        try {
            String searchMode = dealQS.getQueryParameter(request.getQueryString(), "searchMode");
            switch (searchMode) {
                case "ProductInfo":
                    ITService = TRService;
                    break;
                case "Log":
                    ITService = TLService;
                    break;
                case "TestCount":
                    ITService = TCService;
                    break;
                case "PackageTestLogs":
                    ITService = PackService;
                    break;
                default:
                    throw new Exception("No " + searchMode + " Available");
                // break;
            }
            queryStr = ITService.generateSQL(request);
            result = ITService.searchData(queryStr);
            if (!searchMode.equals("PackageTestLogs")) {
                response.setCharacterEncoding("utf-8");
                response.setContentType("text/javascript;charset=utf-8");
                response.setHeader("content-type", "application/json;charset=utf-8");
                out = response.getWriter();
                out.println(result.toString());
                response.setStatus(200);
                out.close();
            } else {
                // response.setContentType("application/zip");
                PackService.writeLogsInLocal(result);
                response.reset();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/x-msdownload");
                response.setHeader("Content-Disposition", "attachment; filename=" + PackService.getZipName());
                PackService.compressToZip(response.getOutputStream());
                PackService.deleteDirAndFile();
            }
        } catch (Exception e) {
            response.setStatus(500);
            response.sendError(500, e.getMessage());
            // out.println("Error:"+e.toString());
        } finally {
            // out.close();
        }

    }

}