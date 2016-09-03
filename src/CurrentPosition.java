import java.util.List;
import java.util.ArrayList;
/**
 * 
 * 
 *
 * @author 0xcc
 * @version 1.0
 *  @createtime 2016-08-09 20:05:59
 *  
 *  
 */


public class CurrentPosition{
	
	public static inode curr; //Point to the current inode.
	public static List<String> routeRem = new ArrayList<String>();
	
	
	public static void addRoute(String currenpath){
		
		CurrentPosition.routeRem.add(currenpath);
	}
	public static void deleteRoute(String currenpath){
		
		CurrentPosition.routeRem.remove(currenpath);
	}
	
	public static String showRoute(){
		
		String res = "";
		for(int i = 0; i< CurrentPosition.routeRem.size(); i++)
		{
			if(i == 0 || i ==1){
				res += CurrentPosition.routeRem.get(i);
			}else{
				res += "/" + CurrentPosition.routeRem.get(i);
			}
		}
		return res;
	}
	
}