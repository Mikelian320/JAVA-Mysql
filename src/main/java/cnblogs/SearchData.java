package cnblogs;
import java.sql.*;
//import com.mysql.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class SearchData{
    static final String JDBC_DRIVER=Account.JDBC_DRIVER;
    static final String DB_URL=Account.DB_URL;
    static final String USER=Account.USER;
    static final String PASS=Account.PASS;
    public static JSONArray searchData(String condition,String [] keys){
        Connection conn = null;
        Statement stmt = null;
        JSONArray dataSet=new JSONArray();
        Integer index =0; 
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("connecting...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            System.out.println(" Initial Statement...");
            stmt = conn.createStatement();
            String sql="";
            for(String value:keys){
                if (sql=="") {
                    sql=value;
                } else {
                    sql=sql+","+value;
                }
            }
            sql="SELECT "+sql+" "+condition;
            System.out.println(sql);
            ResultSet data=stmt.executeQuery(sql);
            while (data.next()){
                JSONArray singleDA=new JSONArray();
                for (String value : keys) {
                    singleDA.add(data.getString(value));
                }
                dataSet.add(singleDA);
                index++;
            }
            data.close();
            stmt.close();
            conn.close();
        }catch (SQLException se) {
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
        return dataSet;
    }
}