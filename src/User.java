import java.io.Serializable;
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


public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5835007458881138196L;
	public String id;
	public String username;
	private String password;
	
	public User(String id, String username, String password){
		
		this.id = id;
		this.username = username;
		this.password = password;
	}
	
	public void setPasswd(String passwd){
		
		this.password = passwd;
		
	}
	
	public String getPasswd(){
		
		return this.password;
		
	}
	
	public String getUsername(){
		
		return this.username;
		
	}
	
public void setUsername(String username){
		
		this.username = username;
		
	}
	
}