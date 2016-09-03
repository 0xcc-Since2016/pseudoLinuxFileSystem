import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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


public class generalmethods{
	
	public static String[] commandlist = {"exit","ls","mkdir","touch","rm","cd","cat","write","chmod",""};
	public static boolean DebugMode = true;
	public static boolean judgeComIn(String Command, String[] commandlist){
		
		for(String comm: commandlist){
			if (Command.equals(comm))
			{
				return true;
			}
		}
		return false;
	}
	
	
	
	public static String getCurrentTime(){
		
		String currentime = "";
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		currentime = format.format(date);
		
		return currentime;
	}
	
	
	public static void initialize(Directory rootDir, String loginname){
		
		boolean has = false;
		
		for(int i =0 ; i < rootDir.nextinodes.size(); i++){
			
			if(rootDir.nextinodes.get(i).filename.equals(loginname) && rootDir.nextinodes.get(i).isDirectorynode)
			{
				
				has = true;
				break;
			}
		}
		if(has){
			
			return;
		}
		else{
			
			generalmethods.createdir(rootDir, loginname);
		}
		
	}
	
	
	public static boolean createfile(Directory dir, String filename){
		
		//Create a file -> create an empty file.
		//Use write to write in .
		int dirlen = dir.nextinodes.size();
		inode tempfnode 			= new inode(false);
		tempfnode.filename 			= filename;
		tempfnode.previousDirectory = dir;
		dir.nextinodes.add(tempfnode);
		int newdirlen = dir.nextinodes.size();
		if(dirlen == newdirlen-1)
		{
			return true;	
		}		
		else{
			return false;
		}
	}
	
	
	public static boolean createdir(Directory dir, String dirname){
	
		//Create a new directory under this directory.
		int dirlen = dir.nextinodes.size();
		//System.out.println(dirlen);
		inode tempdnode   = new inode(true);
		
		tempdnode.filename = dirname;
		Directory  newdir    = new Directory(dirname);
		newdir.preinode		 = tempdnode;
		tempdnode.nextDirectory = newdir;
		tempdnode.previousDirectory = dir;
		
		
		dir.nextinodes.add(tempdnode);
		int newdirlen = dir.nextinodes.size();
		
		if(dirlen == newdirlen-1){
			
			return true;
		}else{
			
		return false;
		}
	}
	
	public static String btoS(byte[] source) throws UnsupportedEncodingException{
		
		String res = new String(source, "UTF-8");
		return res;
		
	}
	
	public static byte[] stoB(String source) throws UnsupportedEncodingException{
		
		byte[] res = source.getBytes("UTF8");
		 return res;
		
	}
	
	
}