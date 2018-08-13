import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：WordExporter
 * 创建人：吕德奎
 * 创建时间：2018/8/13
 */
public class WordExporter {
    private Configuration configuration = null;

    private static WordExporter ourInstance = new WordExporter();

    public static WordExporter getInstance() {
        return ourInstance;
    }

    private WordExporter() {
        configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
    }

    public void execute(String databaseName,  List<Map<String, Object>> mapList) {
        configuration.setClassForTemplateLoading(this.getClass(), "");//模板文件所在路径
        Template template = null;
        try {
            template = configuration.getTemplate("template.ftl"); //获取模板文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        File outFile = new File("D:/outFile" + Math.random() * 10000 + ".doc"); //导出文件
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            Map map = new HashMap();
            map.put("DatabaseName",databaseName);
            map.put("RESULT",mapList);
            template.process(map, out); //将填充数据填入模板文件并输出到目标文件
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readTemplate() {
        String content = "";
        File file = new File("template.xml");
        if (file.exists() == false)
            file = new File(WordExporter.class.getResource("template.xml").getFile());


        return content;
    }
}
