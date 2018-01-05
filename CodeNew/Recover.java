
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class Recover {//
	
	public static void main(String args[]){
		try{			   	
            Scanner in=new Scanner(new File(args[0])); 
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[1])));	
			writer.write(in.nextLine());  writer.newLine(); String[] onepair; 
            
            while(in.hasNextLine()){
            	onepair=in.nextLine().trim().split("[\\p{Space}]+"); 
            	if(Integer.parseInt(onepair[16])==1){
            		writer.write(onepair[0]+"  "+onepair[1]+"  "+onepair[2]+"  "+onepair[3]+"  "); 
					writer.write(onepair[7]+"  "+onepair[8]+"  "+onepair[9]+"  "+onepair[4]+"  "+onepair[5]+"  "+onepair[6]+"  ");
					writer.write(onepair[10]+"  "+onepair[11]+"  "+onepair[12]+"  "+onepair[13]+"  "+onepair[14]+"  "+onepair[15]); 
            	}
            	else{
            		for(int i=0;i<16;i++){
            			writer.write(onepair[i]+"  ");
            		}
            	}
            	writer.newLine();
            }
			
            writer.close(); in.close(); long endTime=System.currentTimeMillis();
//            System.out.println("end recover!   endTime: "+endTime);
				
			
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}

}
