package cnblogs;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.logging.*;
import java.util.logging.SimpleFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
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
                return Keyvalue.substring(1);
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
                String value;
                value=geturlKeyvalue(var, condition);
                searchCon.put(var, value);
            }
        }
        return searchCon;
    }
    /**20190522优化：直接用for循环遍历json数据**/
    protected static String dealCondition(JSONObject searchCon,String [] Key){
        String sqlCondition="";
        String select="SELECT ";
        String result="";
        boolean hasTable=searchCon.containsKey("Product_Type");
        final String gettablesSQL="select table_name from information_schema.tables where table_schema='zzblogo'";
        JSONArray tables=new JSONArray();
        for (int i=0;i<Key.length;i++) {
            if (i<(Key.length-1)) {
                select+=Key[i]+",";
            } else {
                select+=Key[i];
            }
        }
        if (hasTable) {
            result=select+" From "+searchCon.getString("Product_Type");
            searchCon.remove("Product_Type");
        }else{
            try {
                tables=SearchData.searchData(gettablesSQL);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        if (hasTable) {
            result+=sqlCondition;
        } else {
            int size=tables.size();
            for (int j=0;j<size;j++) {
                if (j<size-1) {
                    result+=select+" From "+tables.getJSONArray(j).getString(0)+sqlCondition+" Union ";
                } else {
                    result+=select+" From "+tables.getJSONArray(j).getString(0)+sqlCondition;
                }

            }
        }
        return result;
    }
}
class MyLogHandler extends Formatter{
    private static String getTime(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String currentTime=dateFormat.format(new Date());
        return currentTime;
    }
    @Override
    public String format(LogRecord record) { 
        return getTime() + ":" + record.getMessage()+"\n"; 
    } 
}
public class RequestDemo extends HttpServlet {
    private String [] Key1={"Slot","Test_Station","Test_Require","Product_Model","SN","MAC","Record_Time","PC_Name","ATE_Version","Hardware_Version","Software_Version","Software_Number","Boot_Version","TestResult"}; 
    private String [] Key2={"Log"};
    Logger errlog=Logger.getLogger("ErrLog");
    Logger searchrecord=Logger.getLogger("SearchRecord");
    public void init() throws ServletException
    {
        //message = "Search Result";
    }
    public void doGet(HttpServletRequest request,HttpServletResponse response)
    throws ServletException, IOException
    {
        String queryString=request.getQueryString();
        String searchMode=DealString.geturlKeyvalue("searchMode",queryString);
        String searchSQL="";
        if (searchMode.indexOf("ProductInfo")>=0) {
            searchSQL =DealString.dealCondition(DealString.condition2Json(queryString),Key1);
        } else {
            searchSQL =DealString.dealCondition(DealString.condition2Json(queryString),Key2);
        }  
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/javascript");
        response.setHeader("content-type", "application/json;charset=UTF-8");
        JSONArray Result = new JSONArray();
        PrintWriter out = response.getWriter();
        try {
            FileHandler sfHandler = new FileHandler("./SearchRecord.txt");
            sfHandler.setFormatter(new MyLogHandler());
            searchrecord.addHandler(sfHandler);
            Result=SearchData.searchData(searchSQL);
            searchrecord.info(searchSQL);
            out.println(Result.toString());
        } catch (Exception se) {
            try {
                out.println("Error:"+se.toString());
                FileHandler efHandler = new FileHandler("./Errlog.txt");
                errlog.addHandler(efHandler);
                errlog.warning(se.toString());
            } catch (Exception e) {
                out.println("Error:"+e.toString());
            }
        }
    }
    
    public void destroy()
    {
    }
    public static void main(String[] args) {
        String [] Key1={"Slot","Test_Station","Test_Require","Product_Model","SN","MAC","Record_Time","PC_Name","ATE_Version","Hardware_Version","Software_Version","Software_Number","Boot_Version","TestResult"}; 
        String queryString="searchMode=ProductInfo&Product_Type=ml_switch&SN=G1MR13G00005B";
        //String sqlCondition="";
       // Logger searchrecord=Logger.getLogger("SearchRecord");
        JSONObject searchCon=new JSONObject();
        JSONArray Result=new JSONArray();
        try {
           searchCon=DealString.condition2Json(queryString);
           String searchCondition=DealString.dealCondition(searchCon,Key1);
           Result=SearchData.searchData(searchCondition);
           System.out.println(Result);
           //System.out.println(searchCondition);
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }

    }
  }
