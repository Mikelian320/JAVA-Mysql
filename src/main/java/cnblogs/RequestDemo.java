package cnblogs;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ObjectInputStream.GetField;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
class  DealString{
    protected static String geturlKeyvalue(String key,String url){
        String Keyvalue;
        int len =key.length();
        if (url.indexOf(key)>=0) {
            Keyvalue=url.substring(url.indexOf(key)+len);//
            if (Keyvalue.indexOf("&")>=0) {
                Keyvalue=Keyvalue.substring(1, Keyvalue.indexOf("&"));
                //System.out.println(Keyvalue);
                return Keyvalue;
            } else {
                return Keyvalue;
            }
        } else {
                return null;
        }
    }
    protected static String dealCondition(String condition){
        String result;
        
        String Product_Type=condition.substring(condition.indexOf("Product_Type=")+13,condition.indexOf(","));
       // Product_Type=Product_Type.substring(0,Product_Type.indexOf(","));
        result=condition.substring(condition.indexOf(",")+1);
        result=result.replaceAll(",", " AND ");
        result = " FROM "+Product_Type+" WHERE "+result;
        return result;
    }
}
public class RequestDemo extends HttpServlet {
 
    private String message;
    private String [] Key1={"Slot","Test_Station","Test_Require","Product_Model","SN","MAC","Record_Time","PC_Name","ATE_Version","Hardware_Version","Software_Version","Software_Number","Boot_Version","TestResult"}; 
    ;
    private String [] Key2={"Test_Log"};
    public void init() throws ServletException
    {
        message = "Search Result";
    }
  
    public void doGet(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException
    {
        String queryString=request.getQueryString();
        String searchMode=DealString.geturlKeyvalue("searchMode",queryString);
        String searchCondition=DealString.geturlKeyvalue("searchCondition",queryString);
        searchCondition=DealString.dealCondition(searchCondition);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type", "text/html;charset=UTF-8");
        JSONObject Result = new JSONObject();
        if (searchMode=="ProductInfo") {
            Result=SearchData.searchData(searchCondition,Key1);
        } else {
            Result=SearchData.searchData(searchCondition,Key2);
        }
        PrintWriter out = response.getWriter();
        out.println("<h1>" + message + "</h1>");
        out.println(Result.toString());
    }
    
    public void destroy()
    {
    }
    public static void main(String[] args) {
        String queryString="searchMode=Productinfo&searchCondition=Product_Type=ml_switch,Test_Result='PASS',SN='123456789'";
        String searchMode=DealString.geturlKeyvalue("searchMode",queryString);
        String searchCondition=DealString.geturlKeyvalue("searchCondition",queryString);
        searchCondition=DealString.dealCondition(searchCondition);
        System.out.println(searchMode);
        System.out.println(searchCondition);
    }
  }