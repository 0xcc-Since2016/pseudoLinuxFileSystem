import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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


public class Directory implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8013336672456952694L;
	public inode preinode;
	public String	directoryname;
	public List<inode> nextinodes;
	
	public Directory(String directoryname){
		
		this.directoryname  = directoryname;
		this.preinode            = null;
		this.nextinodes         = new ArrayList<inode>();
		
	}
	
	
	
	
	
}
