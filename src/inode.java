import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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


public class inode implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2454371087525650158L;
	private int nodeid;
	public String userid;  //Owner
	public String usergroup;
	public String filename;
	public String createtime;
	public String visitime;
	public String modifytime;
	public Directory nextDirectory;
	public Directory previousDirectory;
	public boolean isDirectorynode;
	public int filesize;
	public static int curr = 0;
	
	public String filetype;
	public int protectioncode;
	public Block blockhead;
	public Block datahead = null;
	public int startadd;
	
	public inode(boolean isdir){
		
		
		Block storage = new Block();
		
		//Head Block number, also known as physical address.
		this.nodeid        = storage.blocknumber;
		this.blockhead  = storage;
		this.usergroup  = "Administrator";
		this.createtime  = generalmethods.getCurrentTime();
		this.modifytime= generalmethods.getCurrentTime();
		this.visitime 		 = generalmethods.getCurrentTime();
		
		this.protectioncode = 666; //Default Privilege: rw-
		
		if (isdir){
			this.isDirectorynode = true;
			this.filesize   = 1;
		}
		else{
			this.isDirectorynode = false;
		}
		
	}
	
	
	public int getNodeid(){
		
		return this.nodeid;
		
	}
	
public void setNodeid(int nodeid){
		
		this.nodeid = nodeid;
		
	}
	
public static inode newrootnode(){
	
	inode RootInode 			   = new inode(true);
	//RootInode.modifytime 	   = generalmethods.getCurrentTime();
	//RootInode.visitime 		   = generalmethods.getCurrentTime();
	RootInode.protectioncode = 777;   //Grant Code 777 -- RWX FOR TRIPLE Sides.
	
	Directory RootDirectory= new Directory("/");
	//Make filename consistent with directory name and file name.
	RootInode.filename            = "/";
	RootInode.nextDirectory   = RootDirectory; //Point to "/" Directory.
	RootDirectory.preinode     = RootInode;
	RootInode.previousDirectory = null;
	CurrentPosition.curr           = RootInode;
	//RootInode.isDirectorynode= true; 
	return RootInode;
	
}

public inode createfilenode(){
	
	return null;
	
}

public static String dir(inode Curr){
	
	String res = "";
	//List: current inode.nextDirectory's inode array.
	if(!Curr.isDirectorynode){
			return "[*] " + Curr.filename + " isn't a directory.\n";
		}
		else{
			//Get Curr Dir.
			Directory currDir = Curr.nextDirectory;
			int number              = currDir.nextinodes.size();
			if(number==0)
			{
				return "[*] Current Directory is empty.";
			}
			else{
						//System.out.println("[*] directory number: " + number);
				res += "FileType    " + "Filename    " + "Filesize    " + "PhyAddress    " + "Privilege     " + "CreateDate\n\n";
				for(int i =0; i < number ; i++){
						inode temp       		= currDir.nextinodes.get(i);
						String isdir  			= temp.isDirectorynode?"<DIR>":"<FILE>";
						String filename   = temp.filename;
						//System.out.println("[*] filename: " + filename);
						String filesize 		= "";
						if(temp.datahead == null)
							filesize 				= "0KB";
						else
						filesize     		 	 	= temp.datahead.filesize+"B";
						String address		= temp.nodeid + "";
						//String seq 				= temp.nodeid+"";
						String protect 		= temp.protectioncode+"";
						String CreateDate = temp.createtime;
						
						//Require Format here!!
						//return "[*] Dir Information Collection.";
						
						res += isdir + "        " + filename + "        " + filesize + "	        " +address+"        "+ protect + "         " + CreateDate + "\n";	
					
					}
				
			}
			
			return res+"\n";
		}
	}
	
public boolean create(String dirname, boolean isdir, inode Curr){
	
	Directory currendir = Curr.nextDirectory;
	if(isdir){
		
		//Create -A - Dir.
		return generalmethods.createdir(currendir, dirname);
	}
	else{
		//Create -A -file-head.
		return generalmethods.createfile(currendir, dirname);
	}
}

public static boolean delete(inode Curr, String filename){
	//Remove Certain inode from Curr.nextDir
	Directory dir = Curr.nextDirectory;
	int position = inode.searchfile(filename, Curr);
	if(position == -1){
		return false;
	}else{
		dir.nextinodes.remove(position);
		return true;
	}
}

public static String read(inode Curr,String filename) throws UnsupportedEncodingException{
	
	Block datahead = inode.getDataHead(filename, Curr);
	if(datahead == null){
		System.out.println("[*] datahead Empty, Read Failed!");
		return null;
	}
	
	byte[] res   = datahead.readout(datahead, datahead.filesize);
	String temp = generalmethods.btoS(res);
	return temp;
}

public static boolean write(inode Curr,String filename,String source) throws UnsupportedEncodingException{
	
	int position = inode.searchfile(filename, Curr);
	if(position == -1){
		System.out.println("[*] Couldn't find target file.");
		return false;
	}else{
		
	byte[] target = generalmethods.stoB(source);
	Curr.nextDirectory.nextinodes.get(position).datahead = Block.storage(target);
	return true;
	}
}

public static boolean chmod(inode Curr,int privi){
	
	Curr.protectioncode = privi;
	return true;
}

//Crucial Part:
public static boolean cd(inode Curr, String filename){
	
	if(filename.equals("..")){
		if(Curr.filename.equals("/")){
			return false;
		}
		else{
			CurrentPosition.curr = Curr.previousDirectory.preinode;
			return true;
		}
		
	}else{
	
	int position = inode.searchfile(filename, Curr);
	
	if(position == -1){
		
		System.out.println("[*] Couldn't find dir " + filename);
		return false;
		
	}else{
		
		inode temp = Curr.nextDirectory.nextinodes.get(position);
		if(!temp.isDirectorynode){
			
			System.out.println("[*] Couldn't cd into a file (Not A Directory):  " + filename);
			return false;
		}else{
			//cd into target here.
			CurrentPosition.curr = temp;
			return true;
		}
	}
	
	}
}

public static int searchfile(String filename, inode Curr){
	
	if(!Curr.isDirectorynode){
		return -1;
	}
	Directory dir 	  = Curr.nextDirectory;
	boolean found = false;
	int position 	  = -1;
	for(int i = 0 ; i < dir.nextinodes.size(); i ++){
		
		if(filename.equals(dir.nextinodes.get(i).filename)){
			position = i;
			found = true;
		}
	}
	if(found){
		//Simplified Remove, ignoring resource take back.
		return position;
	}else{
		System.out.println("[*] File Not Found.");
		return -1;
	}
}

public static Block getDataHead(String filename, inode Curr){
	
	int position     = inode.searchfile(filename, Curr);
	Block datahead = null;
	if(position == -1){
		System.out.println("[*] No Such File Exist!");
		return null;
	}else{
			datahead = Curr.nextDirectory.nextinodes.get(position).datahead;
	}
	if(datahead == null){
		System.out.println("[*] datahead Empty, Read Failed!");
		return null;
	}else{
		return datahead;
	}
}



}