package cnblogs;
import java.sql.*;
//import com.mysql.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class SearchData{
    static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://10.18.255.116:3306/zzblogo";
    static final String USER="zzbl";
    static final String PASS="#Sx4BXya";
    public static JSONObject searchData(String condition,String [] keys){
        Connection conn = null;
        Statement stmt = null;
        JSONObject dataSet=new JSONObject();
        Integer index =0;
        //String [] keys={"Slot","Test_Station","Test_Require","Product_Model","SN","MAC","Record_Time","PC_Name","ATE_Version","Hardware_Version","Software_Version","Software_Number","Boot_Version","TestResult"}; 
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
           // sql="SELECT Slot,Test_Station,Test_Require,Product_Model,SN,MAC,Record_Time,PC_Name,ATE_Version,Hardware_Version,Software_Version,Software_Number,Boot_Version,TestResult FROM zzblogo.ml_switch where SN='G1MR13G00005B'";
            ResultSet data=stmt.executeQuery(sql);
            while (data.next()){
                JSONArray singleDA=new JSONArray();
                for (String value : keys) {
                    singleDA.add(data.getString(value));
                }
                dataSet.put(index.toString(),singleDA);
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