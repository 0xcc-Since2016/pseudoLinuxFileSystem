import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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


public class Console{
	
	private static boolean access      = false;
	private static int userseq 		     = 3;
	public static String defaultdate    = "1970-01-01 00:00:00";
	public static boolean firstInitial = false;
	public static inode Serio                = null;
	
	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException{
		
		List<User> userlist = new ArrayList<User>();
		String Com;
		//Default User, Claim Two:
		User root    			  = new User("0001","root","root");
		User admin 			  = new User("0002","admin","admin");
		String loginname ="";
		inode RootNode   = null; 
		userlist.add(root);
		userlist.add(admin);
		//FileSystem - Initialization:
		File file = new File("root.seq");
		if(!file.exists()){
			
			System.out.println("[*] System First Initialization..");
			file.createNewFile();
			Console.firstInitial = true;
			//System.out.println(generalmethods.getCurrentTime());
			//Build new root-node.
			RootNode = inode.newrootnode();
			Console.Serio = RootNode;
			//RootNode Remain unserilized.
			//RootNode points to RootDirectory - > "/"
			
		}else{
			//DeSerilize to old "/" node info.
			//Loading in RootNode from file root.seq
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("root.seq"));
			RootNode 					 	= (inode)in.readObject();
			Console.Serio 				= RootNode;
			CurrentPosition.curr    = RootNode;
			//Simulations for RootNode after Loading from root.seq.
			
			//Create File Folder with user's loginname.
			//System.out.println(RootNode.nextDirectory);
			
		}
		
		//Read in Root-Inode From Current-System.
		
		System.out.println("[*] Loading 0xcc File System...\n");
		Thread.sleep(500);
		System.out.println("[*] Please Wait...\n");
		Thread.sleep(1500);
		System.out.println("[*] Use Login Or Reg to Initialize the Sytem.");
		Scanner reader = new Scanner(System.in);
		System.out.print("0xcc_FileSys$:");
		while(reader.hasNext()){
			Com = reader.nextLine();
			//System.out.println(Com);
			if(Com.equals("Login")&&access==true){
				System.out.println("[*] Already Logined!");
				System.out.print(loginname+"_FileSys$:");
			}
			else if(Com.equals("Login") && access == false){
				System.out.print("$User:");
				String Username = reader.nextLine();
				if(checkusername(Username,userlist)){
					System.out.print("$Password:");
			        String Passwd = reader.nextLine();
					if(checkuserpasswd(Username,Passwd,userlist)){
						System.out.println("[*] Login Success!\n");
						System.out.print(Username+"_FileSys$:");
						access = true;
						loginname = Username;
					}
					else{
						System.out.println("[*] Login Failed!\n");
						System.out.print("0xcc_FileSys$:");
					}
				}
				else{
					System.out.println("[*] Login Failed!\n");
					System.out.print("0xcc_FileSys$:");
				}
			}
			else if(Com.equals("Reg")){
				System.out.print("$Username:");
				String regname = reader.nextLine();
				System.out.print("$Passwd:");
				String regpasswd= reader.nextLine();
				System.out.print("$Passwd-Twice:");
				String regpasswdagain= reader.nextLine();
				if(regpasswd.equals(regpasswdagain)){
					User Buddy = new User("000"+userseq,regname,regpasswd);
					userseq+=1;
					userlist.add(Buddy);
					System.out.println("[*]Register Success!\n");
					if(access==false)
					System.out.print("0xcc_FileSys$:");
					else
					{
						System.out.println(loginname+"_FileSys$:");
					}
				}
				else{
					System.out.println("[*] Twice Input not Consistent! Failed!\n");
					if(access == true)
					System.out.print(loginname+"_FileSys$:");
					else{
						System.out.print("0xcc_FileSys$:");
					}
				}
			}
			else{
				System.out.println("[*] Your Input is not  a recognizable Command.");
				if(access == true)
				System.out.print(loginname+"_FileSys$:");
			else{
				System.out.print("0xcc_FileSys$:");
			}
			}
			
			if(access==true){
				break;
				//Jump to Command Execution part.
			}
			//System.out.println(Com);
		}
		//reader.close();
		//Scanner Commander = new Scanner(System.in);
		//System.out.println(loginname+"_FileSys$:");
		generalmethods.initialize(RootNode.nextDirectory, loginname);
		CurrentPosition.addRoute("/");
		
		
		while(reader.hasNext()){
			
			String Commands = reader.nextLine();
			String Command	   = "";
			String filename  		   = null;
			if(Commands.equals("ls") || Commands.equals("exit") || Commands.equals("cd")){
					Command = Commands;
			}else{
					Command = Commands.split("\\s+")[0];
					try{
					filename    = Commands.split("\\s+")[1];
					}catch(Exception e){
						System.out.println("[*] Your Input is not  in a Correct Command Format.\n");
						System.out.print(loginname+"_FileSys$" + CurrentPosition.curr.filename + ":");
						continue;
					}
			}
			
			if(generalmethods.judgeComIn(Command,generalmethods. commandlist)){
				
				switch(Command){
				
				case "exit":
					System.out.println("[*] Shutting Down System..Good-Bye:  " + loginname);
					reader.close();
					//Take back resources and update the file tree.
					//Store The whole tree into Real File System.
					//Serialize and store the whole System here.
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("root.seq"));
					out.writeObject(Console.Serio);
					
					System.exit(0);
					
					break;
				case "ls":
					
					System.out.println(inode.dir(CurrentPosition.curr));
					break;
				case "mkdir":
				
					if(generalmethods.createdir(CurrentPosition.curr.nextDirectory, filename))
					{
						System.out.println("[*] Create Dir Success.");
					}else{
						System.out.println("[*] Create Dir Error!");
					}
					break;
				
				case "touch":
					
					if(generalmethods.createfile(CurrentPosition.curr.nextDirectory, filename))
					{
						System.out.println("[*] Create File Success.");
					}else{
						
						System.out.println("[*] Create File Error!");
					}
					break;
				case "rm":
					
					if(inode.delete(CurrentPosition.curr, filename)){
						
						System.out.println("[*] Delete " + filename + " Succeed.");
					}else{
						
						System.out.println("[*] Delete " + filename + " Failed.");
					}
					break;
				case "cd":
					
					if (filename == null){
						System.out.println("[*] Target absense.");
						break;
					}
					
					boolean indicator = false;
					if(filename.equals("..")){
						indicator = false;
					}else{
						indicator = true;
					}
					if(inode.cd(CurrentPosition.curr, filename)){
						if(indicator){
							CurrentPosition.addRoute(filename);
						}
						else{
							CurrentPosition.deleteRoute(CurrentPosition.routeRem.get(CurrentPosition.routeRem.size()-1));
						}
					}
					
					break;
				case "cat":
					
					String res = inode.read(CurrentPosition.curr, filename);
					System.out.println("[*] " + filename + " :\n");
					System.out.println(res);
					break;
				case "write":
					
					System.out.println("[*] Content to write-in : \n");
					//Test if Input-Format Could Modify here:
					String source = reader.nextLine();
					inode.write(CurrentPosition.curr, filename, source);
					break;
				case "chmod":
					
					System.out.println("[*] Input new mod:");
					int privi = reader.nextInt();
					inode.chmod(CurrentPosition.curr, privi);
					
					break;
					default:
						break;
				}
				
				System.out.print(loginname+"_FileSys$" + CurrentPosition.showRoute()+ ":");
				
			}else{
				System.out.println("[*] Your Input is not  a recognizable Command.\n");
				System.out.print(loginname+"_FileSys$" + CurrentPosition.showRoute() + ":");
			}
			
		}
	}
	
	public static boolean checkusername(String userinput, List<User>userlist)
	{
		for(int i =0 ; i < userlist.size();i++)
		{
			if(userinput.equals(userlist.get(i).username)){
				return true;
			}
		}
		return false;
	}
	public static boolean checkuserpasswd(String username, String userinput,List<User>userlist){
		
		for(int i = 0; i< userlist.size();i++)
		{
			if(username.equals(userlist.get(i).username)){
				if(userinput.equals(userlist.get(i).getPasswd())){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return false;
	}
}