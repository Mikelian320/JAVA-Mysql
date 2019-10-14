package cnblogs;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.*;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;
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
        if (key.equals("MAC")) {
            url=url.replace("SETMAC","");//排除SETMAC干扰
        }
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
        String [] searchArr={"Slot","Product_Type","Test_Station","Product_Model","SN","MAC","PC_Name","TestResult","StartTime","EndTime","Record_Time","Offset","Limit"};
        for (String var : searchArr) {
            if (condition.indexOf(var)>=0) {
                String value;
                value=geturlKeyvalue(var, condition);
                searchCon.put(var, value);
            }
        }
        return searchCon;
    }
    protected static String timeStamp2Date(String timestampString){
        String formats="yyyy-MM-dd HH:mm:ss";
        Long timestamp=Long.parseLong(timestampString);
        String date=new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }
    protected static boolean isInteger(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
    protected static JSONArray getTables(){
        JSONArray tables=new JSONArray();
        final String gettablesSQL="select table_name from information_schema.tables where table_schema='zzblogo'";
        try {
            tables=SearchData.searchData(gettablesSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tables;
    }
    protected static String limit(JSONObject searchCon){
        String offset="";
        String limit="";
        String result="";
        if (searchCon.containsKey("Offset")&&searchCon.containsKey("Limit")) {
            offset=searchCon.getString("Offset");
            limit=searchCon.getString("Limit");
            if (isInteger(offset)&&isInteger(limit)) {
                result="limit "+offset+","+limit;
            }
        } else {
            if (searchCon.containsKey("Offset")) {
                offset=searchCon.getString("Offset");
                if (isInteger(offset)) {
                    result="limit "+offset;
                }
            }else{
                if (searchCon.containsKey("Limit")) {
                    limit=searchCon.getString("Limit");
                    if (isInteger(limit)) {
                        result="limit "+limit;
                    }
                }else{
                    result="limit 1000";
                }
            }
        }
        return result;
    }
    protected static String getURLDecode(String str){
        String result="";
        if(str==null){
            return result;
        }
        try {
            result=URLDecoder.decode(str,"UTF-8");
        } catch(Exception e) {
            e.printStackTrace();
            //TODO: handle exception
        }
        return result;
    }
    /**20190522优化：直接用for循环遍历json数据**/
    protected static String dealCondition(JSONObject searchCon,String [] Key){
        String sqlCondition="";
        String select="SELECT ";
        String result="";
        String time="";
        String limit="";
        String teststation="";
        boolean hasTable=searchCon.containsKey("Product_Type");
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
            tables=getTables();
        }
        limit=limit(searchCon);
        if (searchCon.containsKey("Offset")) {
            searchCon.remove("Offset");
        }
        if (searchCon.containsKey("Limit")) {
            searchCon.remove("Limit");
        }
        Iterator iterator = searchCon.keys();
        int i=0;
        while(iterator.hasNext()) {
            String key=(String) iterator.next();
            if (i==0){
                sqlCondition=sqlCondition +" WHERE ";//如果只有Offset和Limit则不能够+ WHERE
            }else{
                sqlCondition=sqlCondition +" AND ";
            }
            i++;
             switch (key) {
                 case "StartTime":
                 {
                     time=timeStamp2Date(searchCon.getString(key));
                     sqlCondition=sqlCondition +"Record_Time>="+"'"+time+"'";
                 } 
                     break;
                 case "EndTime":
                 {
                    time=timeStamp2Date(searchCon.getString(key));
                    sqlCondition=sqlCondition +"Record_Time<="+"'"+time+"'";
                 } 
                     break;
                 case "Test_Station":
                 {
                    teststation=searchCon.getString(key);
                    sqlCondition=sqlCondition +"Test_Station="+"'"+DealString.getURLDecode(teststation)+"'";
                 }
                    break;
                 case "Record_Time":
                 {
                    time=timeStamp2Date(searchCon.getString(key));
                    sqlCondition=sqlCondition +"Record_Time="+"'"+time+"'";
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
        result=result+" "+limit;
        result=result.replace("+", " ");
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
        return getTime() + ":" + record.getMessage()+"\r\n"; 
    } 
}
public class RequestDemo extends HttpServlet {
    private String [] Key1={"Slot","Test_Station","Test_Require","Product_Model","SN","MAC","Record_Time","PC_Name","ATE_Version","Hardware_Version","Software_Version","Software_Number","Boot_Version","TestResult"}; 
    private String [] Key2={"Log"};
    Logger searchrecord=Logger.getLogger("SearchRecord");
    Logger errlog=Logger.getLogger("ErrLog");
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
        System.out.println(searchMode);
        if (searchMode.indexOf("Log")>=0) {
            searchSQL =DealString.dealCondition(DealString.condition2Json(queryString),Key2);
        } else {
            searchSQL =DealString.dealCondition(DealString.condition2Json(queryString),Key1);
        }  
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/javascript");
        response.setHeader("content-type", "application/json;charset=UTF-8");
        JSONArray Result = new JSONArray();
        PrintWriter out = response.getWriter();
        try {
            FileHandler sfHandler = new FileHandler("./SearchRecord%g.txt",100000,5,true); 
            sfHandler.setFormatter(new MyLogHandler());
            searchrecord.addHandler(sfHandler);
            searchrecord.info(searchSQL);//先记录查询语句，后查询，避免出错没有记录查询语句
            sfHandler.close();
            Result=SearchData.searchData(searchSQL);
            response.setStatus(200);
            out.println(Result.toString());
        } catch (Exception se) {
            try {
                response.setStatus(500);
                out.println("Error:"+se.toString());
                FileHandler efHandler = new FileHandler("./Errlog%g.txt",100000,5,true);//句柄初始化，入参：路径、日志大小上限（Byte）、日志保存数量、追加写入 
                errlog.addHandler(efHandler);
                errlog.warning(se.toString());
                efHandler.close();
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
        String [] Key2={"Log"};
        String queryString="http://www.greatwebtech.cn/search/searchdata?searchMode=ProductInfo&Test_Station=SETMAC&Limit=10";
        Logger searchrecord=Logger.getLogger("SearchRecord");
        JSONObject searchCon=new JSONObject();
        JSONArray Result=new JSONArray();
        try {
          // FileHandler sfHandler = new FileHandler("./SearchRecord.txt");
          //String a=DealString.geturlKeyvalue("Test_Station", queryString);
         // System.out.println(a);
           FileHandler sfHandler = new FileHandler("./SearchRecord%g.txt",100000,5,true);
           sfHandler.setFormatter(new MyLogHandler());       
           searchrecord.addHandler(sfHandler);
           searchCon=DealString.condition2Json(queryString);
           String searchCondition=DealString.dealCondition(searchCon,Key1);
           searchrecord.info(searchCondition);
           Result=SearchData.searchData(searchCondition);
           System.out.println(Result);
           //System.out.println(searchCondition);
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }
  }
