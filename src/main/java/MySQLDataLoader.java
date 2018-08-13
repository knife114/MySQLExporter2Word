import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：MySQLDataLoader
 * 创建人：吕德奎
 * 创建时间：2018/8/13
 */
public class MySQLDataLoader implements IDataLoader {
    // 定义数据库的用户名
    private String USERNAME = "root";
    // 定义数据库的密码
    private String PASSWORD = "123456";
    // 定义数据库的驱动信息
    private final String DRIVER = "com.mysql.jdbc.Driver";
    // 定义访问数据库的地址
    private String URL = "jdbc:mysql://localhost:3306/mydb";

    private String TABLE_SCHEMA = "jdbc:mysql://localhost:3306/mydb";

    public MySQLDataLoader(String username, String password ,String url, String TABLE_SCHEMA) {
        try {
            Class.forName(DRIVER);
            this.USERNAME = username;
            this.PASSWORD = password;
            this.URL = url;
            this.TABLE_SCHEMA = TABLE_SCHEMA;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //
    public List<Map<String, Object>> load() {
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        Connection connection = null;
        Statement   statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.getConnection();
            List<String> listTables = this.fetchTables(connection);

            for (int i = 0; i < listTables.size(); i++) {
                Map<String,Object> listMap = new HashMap<String, Object>();
                List<Map<String,String>> list = new ArrayList<Map<String, String>>();
                statement = connection.createStatement();
                String sql = "select t.COLUMN_COMMENT,t.COLUMN_NAME,t.COLUMN_TYPE from information_schema.columns t where table_schema " +
                        "= '${TABLE_SCHEMA}' and table_name = '${TABLE_NAME}' ORDER BY t.ORDINAL_POSITION  ";
                sql = sql.replace("${TABLE_SCHEMA}", this.TABLE_SCHEMA);
                sql = sql.replace("${TABLE_NAME}", listTables.get(i));

                resultSet = statement.executeQuery(sql);


                int index = 1;
                while (resultSet.next()) {
                    Map map = new HashMap();
                    String name = resultSet.getString("COLUMN_NAME");
                    String type = resultSet.getString("COLUMN_TYPE");
                    String comment = resultSet.getString("COLUMN_COMMENT");
                    map.put("ID",index++ );
                    map.put("FIELDNAME",name );
                    map.put("FIELDTYPE",type );
                    map.put("DESCRIPTION",comment );
                    map.put("REMARK",comment );
                    list.add(map);

                }
                listMap.put("TABLENAME", listTables.get(i)+"");
                listMap.put("FIELDS", list);
                result.add(listMap);
                this.releaseConn(statement, resultSet);
            }
        } catch (Exception e) {
            System.out.println("Connection exception !");
        }
        finally {
            try{
                connection.close();
            }catch (Exception ex){}
        }
        return result;
    }
    public List<String> fetchTables(Connection connection){
        List<String> list = new ArrayList<String>();

        Statement   statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            String sql = "select * from information_schema.`TABLES` t where table_schema = '${TABLE_SCHEMA}'";
            sql = sql.replace("${TABLE_SCHEMA}", this.TABLE_SCHEMA);
            resultSet =  statement.executeQuery(sql);

            while (resultSet.next()){
                String tbName = resultSet.getString("TABLE_NAME");
                list.add(tbName);
            }
        } catch (Exception e) {
            System.out.println("Connection exception !");
        }
        finally {
            this.releaseConn(statement, resultSet);
        }
        return list;
    }
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection exception !");
        }
        return connection;
    }

    public void releaseConn(Statement statement,ResultSet resultSet) {
        try {
            if (resultSet!=null) {
                resultSet.close();
            }
            if (statement!=null) {
                statement.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
