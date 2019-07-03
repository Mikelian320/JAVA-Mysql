package cnblogs;
import java.sql.*;
import net.sf.json.JSONArray;
public class SearchData{
    static final String JDBC_DRIVER=Account.JDBC_DRIVER;
    static final String DB_URL=Account.DB_URL;
    static final String USER=Account.USER;
    static final String PASS=Account.PASS;
    public static JSONArray searchData(String sql)
    throws Exception 
    {
        Connection conn = null;
        Statement stmt = null;
        JSONArray dataSet=new JSONArray();
        Integer index =0;
        try {
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
