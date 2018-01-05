
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class FilterRepetitivePairs {

	public static void main(String args[]){
		try{			   	
            Scanner in=new Scanner(new File(args[0])); 
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[1])));	
			String temp; String[] onepair; int index=0, indexR=0; writer.write(in.nextLine());  writer.newLine();
			String[] bef=new String[9];
			
			temp=in.nextLine();
			onepair=temp.trim().split("[\\p{Space}]+"); writer.write(temp); writer.newLine(); index++;
			bef[0]=onepair[4]; bef[1]=onepair[5]; bef[2]=onepair[6]; bef[3]=onepair[7]; bef[4]=onepair[8]; bef[5]=onepair[9];
			bef[6]=onepair[10]; bef[7]=onepair[11]; bef[8]=onepair[13]; 
			while(in.hasNextLine()){
				temp=in.nextLine();
				onepair=temp.trim().split("[\\p{Space}]+"); index++;
				if(onepair[4].equalsIgnoreCase(bef[0])&&onepair[5].equalsIgnoreCase(bef[1])&&onepair[6].equalsIgnoreCase(bef[2])&&onepair[7].equalsIgnoreCase(bef[3])&&onepair[8].equalsIgnoreCase(bef[4])&&onepair[9].equalsIgnoreCase(bef[5])&&onepair[10].equalsIgnoreCase(bef[6])&&onepair[11].equalsIgnoreCase(bef[7])&&onepair[13].equalsIgnoreCase(bef[8])){
					indexR++;
				}
				else{
					writer.write(temp); writer.newLine();
					bef[0]=onepair[4]; bef[1]=onepair[5]; bef[2]=onepair[6]; bef[3]=onepair[7]; bef[4]=onepair[8]; bef[5]=onepair[9];
					bef[6]=onepair[10]; bef[7]=onepair[11]; bef[8]=onepair[13]; 
				}
			}
			
		    in.close(); writer.close();
//		    System.out.println("end! FilterRepetitivePairs   "+index+"  "+indexR);
			
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
	
}
