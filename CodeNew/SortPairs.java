
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class SortPairs {

	public static void main(String args[]){
		try{			   	
            Scanner in=new Scanner(new File(args[0])); 
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[1])));	
			String temp; String[] onepair; int index=0, indexR=0; writer.write(in.nextLine());  writer.newLine();
			
			while(in.hasNextLine()){
				temp=in.nextLine();
				onepair=temp.trim().split("[\\p{Space}]+"); index++;
				if(Integer.parseInt(onepair[4])<Integer.parseInt(onepair[7])){
					writer.write(temp+"  0"); writer.newLine();
				}
				else if(Integer.parseInt(onepair[4])>Integer.parseInt(onepair[7])){
					writer.write(onepair[0]+"  "+onepair[1]+"  "+onepair[2]+"  "+onepair[3]+"  ");  indexR++;
					writer.write(onepair[7]+"  "+onepair[8]+"  "+onepair[9]+"  "+onepair[4]+"  "+onepair[5]+"  "+onepair[6]+"  ");
					writer.write(onepair[10]+"  "+onepair[11]+"  "+onepair[12]+"  "+onepair[13]+"  "+onepair[14]+"  "+onepair[15]+"  1"); writer.newLine();
				}
				else if(Integer.parseInt(onepair[6])<Integer.parseInt(onepair[8])){
					writer.write(temp+"  0"); writer.newLine();
				}
				else if(Integer.parseInt(onepair[5])>Integer.parseInt(onepair[9])){
					writer.write(onepair[0]+"  "+onepair[1]+"  "+onepair[2]+"  "+onepair[3]+"  "); indexR++;
					writer.write(onepair[7]+"  "+onepair[8]+"  "+onepair[9]+"  "+onepair[4]+"  "+onepair[5]+"  "+onepair[6]+"  ");
					writer.write(onepair[10]+"  "+onepair[11]+"  "+onepair[12]+"  "+onepair[13]+"  "+onepair[14]+"  "+onepair[15]+"  1"); writer.newLine();
				}
			}
			
		    in.close(); writer.close();
//		    System.out.println("end! sortPair  "+index+"  "+indexR);
			
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
	
}
