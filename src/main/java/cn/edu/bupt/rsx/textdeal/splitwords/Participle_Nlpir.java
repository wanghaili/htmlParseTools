package cn.edu.bupt.rsx.textdeal.splitwords;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;

public class Participle_Nlpir {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String sentence = "   1：融合代码 " +
	"将修改的代码融合到最新版本的代码中。打包本地测试通过后再放入测试环境" +
   "2：数据准备" +
   	"将正式运行的数据库和测试系统的数据分别导入出来到本地中，2个库做下对比，然后将正式运营的数据导入到测试数据库中。若是有修改字段或表请自行添加" +
   "3：文件备份" +
   	"上诉工作完成以后，停止服务，然后做好数据库备份和ROOT文件备份" +
   "4：正式系统准备" +
   	"修改系统关键配置文件，将原正式环境下ROOT/WEB-INFO/下的图片和pagehtml里面的html页面迁移到新的ROOT中（若是嫌麻烦可以在放到服务器上去之前用对比工具来做文件对比。对比结果一目了然）" +
   "5：部署" +
   	"将新的ROOT文件放到正式环境下的TOMCAT/webapp/目录下。将新的数据库结构部署到数据库服务器。启动tomcat";
		try {			System.out.println(splitWords(sentence).get(10).toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List splitWords(String sentence){
		List<Term> termList = ToAnalysis.parse(sentence);
		return termList;
	}
	
}
