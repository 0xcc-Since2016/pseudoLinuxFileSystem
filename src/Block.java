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


public class Block implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5852448156807489428L;
	//1KB blocks times 10000, represents space occupies 10MB space.
	public static int[] bitmap = new int[10000];
	public int blocknum = 0;
	public byte[] block = new byte[1024] ;   //Block Simulations, 1block occupies 1024 bytes.
	public Block nextBlock;
	public int blocknumber = (int)(Math.random()*10000); //0-9999
	public int filesize = 0;
	
	public Block(){
		this.nextBlock = null;
		//System.out.println(this.blocknumber);
		allocateblocknum();
		
	}
	
	
	public void allocateblocknum(){
		
		if (Block.bitmap[this.blocknumber] == 0){
			//Undeployed.
			Block.bitmap[this.blocknumber] = 1;
		}
		else{
			do {
				this.blocknumber = (int)(Math.random()*10000);
			}while(Block.bitmap[this.blocknumber]==1);
			
			Block.bitmap[this.blocknumber] = 1; //Allocated.
		}
		//System.out.println("[*] Allocated address: " + this.blocknumber);
	}
	
	public static Block storage(byte[] store){
		//Filesize represents bytes num of the file.
		
		Block head       = new Block();
		head.filesize      = store.length;
		Block temp       = head;
		int blocknum = store.length/1024;
		int leftbytes    = store.length%1024; //left bytes.
		int content      = 0;
		
		for(int x = 0 ; x < blocknum; x++)
		{
			//Construct the data block chain table.
			temp.nextBlock = Block.writein(store,content);
			content+=1024;
			temp = temp.nextBlock;
		}
		
		if(leftbytes != 0){
		Block e = new Block();
		int k = 0;
		for(int i = content; i < content+leftbytes ; i++)
		{
			e.block[k++] = store[i];
		}
		
		temp.nextBlock = e;
		head.blocknum = blocknum+2;
		}else{
			head.blocknum = blocknum+1; 
		}
		
		//With head and insufficient.
		if(content+leftbytes != store.length){
			System.out.println("[*] Create File Error: ");
			System.out.println("[*]Details: 1024ContentNum: " + content);
			System.out.println("[*] leftbytesNum: " + leftbytes);
			System.out.println("[*] storelength: " + store.length);
			System.out.println("[*] Minus: " + (store.length-content-leftbytes));
		}
		
		else{
			System.out.println("[*] Write-in File Succeed!");
		}
		
		return head;
	}
	
	
	public static Block writein(byte[] store, int contentnum){
		
		Block next = new Block();
		int k = 0;
		for(int i = contentnum; i < contentnum+1024 ; i++)
			{
				next.block[k++] = store[i];
			}
		return next;
	}
	
	
	public byte[] readout(Block head, int filesize){
		
		byte[] file    = new byte[filesize];
		Block start   = head.nextBlock;
		int content = 0;
		while(start.nextBlock != null){
			
			int count =0;
			for(int i = content; i < content+1024; i++){
				
				file[i] = start.block[count++];
				
			}
			
			content += 1024;
			start = start.nextBlock;
			
		}
		
		byte[] last = start.block;
		int cr    = 0;
		for(int i = content; i<filesize; i++){
			
			file[i] = last[cr++];
			
		}
		
		if(file.length == filesize){
			return file;
		}
		
		else{
			System.out.println("[*] Error: ");
			System.out.println("[*] FileSize: " + filesize);
			System.out.println("[*] Transport FileSize:  " + file.length);
			return null;
		}
		
	}
	
}