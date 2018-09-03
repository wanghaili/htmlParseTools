package htmlParseTools;

import cn.edu.bupt.rsx.textdeal.fileutil.DocExtractLibray;
import com.sun.jna.NativeLong;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DocExtractTest {
	
	 public static void main(String [] args){
//		 //testImportUserDict();
//		 testExtratContent();
		 List<String> lists = new ArrayList<>();
		 System.out.println(lists.contains("ddd"));
	 }
	/**
	 * 测试导入用户自定义词
	 */
	@Test
	public static void testImportUserDict() {
		if ( DocExtractLibray.Instance.DE_Init("", 1, "") == 0 ) {
			System.out.println("DocExtractor初始化失败：" + DocExtractLibray.Instance.DE_GetLastErrMsg());
			System.exit(1);
		} 
		//System.out.println("DocExtractor初始化成功");
		//System.out.println("成功导入的商品名称：" + DocExtractLibray.Instance.DE_ImportUserDict("dict/shop_name.txt",true));
//		System.out.println("成功导入的商品品牌：" + DocExtractLibray.Instance.DE_ImportUserDict("dict/brand.txt",false));
//		System.out.println("成功导入的自定义词：" + DocExtractLibray.Instance.DE_ImportUserDict("dict/userdic.txt",false));
		
		System.out.println("是否安全退出-->"+DocExtractLibray.Instance.DE_Exit());
	}
	
	/**
	 *测试文章实体抽取 
	 */
	@Test
	public static void testExtratContent() {
		if (DocExtractLibray.Instance.DE_Init("", 1, "") == 0) {
			System.out.println("初始化失败："
					+ DocExtractLibray.Instance.DE_GetLastErrMsg());
			System.exit(1);
		}
		System.out.println("初始化成功");
		
//	  String content = FileOperateUtils.getFileContent("test.txt", "utf-8");
	  String content = "UYUK 短袖T恤男2016夏装男士条纹纯色韩版圆领印花修身休闲大码男装T081 白色 XL";
	 // int score=DocExtractLibray.Instance.DE_ComputeSentimentDoc(content);
	  NativeLong handle = DocExtractLibray.Instance.DE_ParseDocE(content, "shop_name#brand#key",
				true, DocExtractLibray.ALL_REQUIRED);
		System.out.println("商品名称-->"
				+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_USER_DEFINED+1));
		System.out.println("商品品牌-->"
				+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_USER_DEFINED + 2));
		System.out.println("自定义词-->"
				+ DocExtractLibray.Instance.DE_GetResult(handle, DocExtractLibray.DOC_EXTRACT_TYPE_USER_DEFINED + 3));
		DocExtractLibray.Instance.DE_ReleaseHandle(handle);
		
		System.out.println("是否安全退出-->"+DocExtractLibray.Instance.DE_Exit());
	}

}
