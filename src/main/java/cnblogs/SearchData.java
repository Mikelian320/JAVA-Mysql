package cnblogs;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;
import net.sf.json.JSONArray;

public class SearchData{
    static String JDBC_DRIVER;
    static String DB_URL;
    static String USER;
    static String PASS;
    public static JSONArray searchData(String sql)
    throws Exception 
    {
        Connection conn = null;
        Statement stmt = null;
        JSONArray dataSet=new JSONArray();
        Integer index =0;
        try {
            Properties properties =new Properties();
            properties.load(new FileInputStream(System.getProperty("user.dir")+"/jdbc.properties"));
            JDBC_DRIVER=properties.getProperty("Driver");
            DB_URL=properties.getProperty("Url");
            USER=properties.getProperty("User");
            PASS=properties.getProperty("Password");
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            ResultSet data=stmt.executeQuery(sql);
            int columnCount= data.getMetaData().getColumnCount();
            while (data.next()){
                JSONArray singleDA=new JSONArray();
                for (int i=1;i<=columnCount;i++) {
                    singleDA.add(data.getString(i));
                }
                dataSet.add(singleDA);
                index++;
            }
            data.close();
            stmt.close();
            conn.close();
        }catch (SQLException se) {
           throw se;
        }catch(Exception e){
            throw e;
        }finally{
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se){
                throw se;
            }
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                throw se;
            }
        }
        return dataSet;
    }
}
