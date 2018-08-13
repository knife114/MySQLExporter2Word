import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 说明：Bootstrap
 * 创建人：吕德奎
 * 创建时间：2018/8/13
 */
public class Bootstrap {
    public static void main(String[] args) {
        System.out.println("开始转换");
        IDataLoader dataLoader = new MySQLDataLoader("root","xxx.com",
                //"jdbc:mysql://192.168.60.200:3306/ds2000?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8",
                "jdbc:mysql://192.168.60.200:3306/ds2000",
                "ds2000");
        List<Map<String, Object>> mapList = dataLoader.load();
        WordExporter.getInstance().execute("ds2000",mapList);
    }


}
