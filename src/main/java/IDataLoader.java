import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * 说明：IDataLoader
 * 创建人：吕德奎
 * 创建时间：2018/8/13
 */
public interface IDataLoader {
    public List<Map<String,Object>> load();
    public Connection getConnection();
    public void releaseConn(Statement statement, ResultSet resultSet);
}
