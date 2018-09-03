package cn.edu.bupt.rsx.htmlparser.tools;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * 计算类
 * @author wanghl
 *
 */
public class CalculateTools {
	/**
	 * 除法
	 * @param divisor
	 * @param dividend
	 * @return
	 */
	
	public static double division(double divisor,double dividend){
		DecimalFormat df = new DecimalFormat("######0.0000"); 
		double result = 0;
		if(dividend!=0){
			result = divisor/dividend;
		}
		result = Double.parseDouble(df.format(result));
		return result;
	}
	
	public static double calculateCos(double[] arg1,double[] arg2) {
		double result = -1;
		int size = 0;
		double numerator = 0;
		double denominator1 = 0;
		double denominator2 = 0;
		if(arg1.length == arg2.length){
			size = arg1.length;
			for(int i=0;i<size;i++){
				numerator += arg1[i]*arg2[i];
				denominator1 += Math.pow(arg1[i], 2);
				denominator2 += Math.pow(arg2[i], 2);
			}
		}
		
		if(denominator1!=0&&denominator2!=0){
			double denominator = Math.sqrt(denominator1)*Math.sqrt(denominator2);
			result = numerator/denominator;
		}
		return result;
	}
	
	
	public static List<Map.Entry<String , Integer>> sortInteger (Map<String , Integer> map) {
		List<Map.Entry<String, Integer>> list = new LinkedList<>( map.entrySet());
		Collections.sort( list, (o1, o2) -> (o1.getValue()).compareTo( o2.getValue()));
		return list;
	}

	public static List<Map.Entry<String , Float>> sortFloat (Map<String , Float> map) {
		List<Map.Entry<String,Float>> list = new LinkedList<>( map.entrySet());
		Collections.sort( list, (o1, o2) -> (o1.getValue()).compareTo( o2.getValue()));
		return list;
	}
}
