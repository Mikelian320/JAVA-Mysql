package cnblogs;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
class  DealString{
    protected static String geturlKeyvalue(String key,String url){
        String Keyvalue;
        int len =key.length();
        if (url.indexOf(key)>=0) {
            Keyvalue=url.substring(url.indexOf(key)+len);
            if (Keyvalue.indexOf("&")>=0) {
                Keyvalue=Keyvalue.substring(1, Keyvalue.indexOf("&"));
                return Keyvalue;
            } else {
                return Keyvalue;
            }
        } else {
                return null;
        }
    }
    /*
        根据输入的查询条件生成mysql语句
        注意时间的处理
    */
    protected static JSONObject condition2Json(String condition){
        JSONObject searchCon=new JSONObject();
        String [] searchArr={"Product_Type","Test_Station","Product_Model","SN","MAC","TestResult","StartTime","EndTime"};
        for (String var : searchArr) {
            if (condition.indexOf(var)>=0) {
                String temp;
                temp=condition.substring(condition.indexOf(var)+var.length()+1);
                if (temp.indexOf(",")>=0) {
                    temp=temp.substring(0, temp.indexOf(","));
                }
                searchCon.put(var, temp);
            }
        }
        return searchCon;
    }
    /**20190522优化：直接用for循环遍历json数据**/
    protected static String dealCondition(JSONObject searchCon){
     // String [] searchArr={"Product_Type","Test_Station","Product_Model","SN","MAC","TestResult","StartTime","EndTime"};
        String sqlCondition="";
        if (searchCon.getString("Product_Type").isEmpty()) {
            sqlCondition="FROM zzblogo.*";
        } else {
            sqlCondition="FROM zzblogo."+searchCon.getString("Product_Type");
            searchCon.remove("Product_Type");
        }
        Iterator iterator = searchCon.keys();
        int i=0;
        while(iterator.hasNext()) {
            String key=(String) iterator.next();
            if (i==0){
                sqlCondition=sqlCondition +" WHERE ";
            }else{
                sqlCondition=sqlCondition +" AND ";
            }
            i++;
             switch (key) {
                 case "StartTime":
                 {
                     sqlCondition=sqlCondition +"Record_Time>="+"'"+searchCon.getString(key)+"'";
                 } 
                     break;
                 case "EndTime":
                 {
                    sqlCondition=sqlCondition +"Record_Time<="+"'"+searchCon.getString(key)+"'";
                 } 
                     break;
                 default:
                {
                    sqlCondition=sqlCondition+key+"="+"'"+searchCon.getString(key)+"'";
                } 
                     break;
             }
        }
        return sqlCondition;
    }
}
public class RequestDemo extends HttpServlet {
 
    private String message;
    private String [] Key1={"Slot","Test_Station","Test_Require","Product_Model","SN","MAC","Record_Time","PC_Name","ATE_Version","Hardware_Version","Software_Version","Software_Number","Boot_Version","TestResult"}; 
    private String [] Key2={"Log"};
    public void init() throws ServletException
    {
        message = "Search Result";
    }
  
    public void doGet(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException
    {
        String queryString=request.getQueryString();
        String searchMode=DealString.geturlKeyvalue("searchMode",queryString);
        String searchCondition=DealString.dealCondition(DealString.condition2Json(queryString));
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type", "application/json;charset=UTF-8");
        JSONArray Result = new JSONArray();
        if (searchMode.indexOf("ProductInfo")>=0) {
            Result=SearchData.searchData(searchCondition,Key1);
        } else {
            Result=SearchData.searchData(searchCondition,Key2);
        }
        PrintWriter out = response.getWriter();
        //out.println("<h1>" + message + "</h1>");
        out.println(Result.toString());
    }
    
    public void destroy()
    {
    }
    public static void main(String[] args) {
        String queryString="searchMode=ProductInfo&searchCondition=Product_Type=ml_switch,StartTime=2018/8/10 0:00,EndTime=2019/5/1 0:00,Test_Result=PASS,SN=G1MR13G00005B";
        String sqlCondition="";
        String searchMode=DealString.geturlKeyvalue("searchMode",queryString);
        JSONObject searchCon=new JSONObject();
        searchCon=DealString.condition2Json(queryString);
        sqlCondition=DealString.dealCondition(searchCon);
        System.out.println(searchCon);
        System.out.println(sqlCondition);
        System.out.println(searchMode);
        if (searchMode.indexOf("ProductInfo")>=0) {
            System.out.println(0);
        } else {
            System.out.println(1);
        }
    }
  }
