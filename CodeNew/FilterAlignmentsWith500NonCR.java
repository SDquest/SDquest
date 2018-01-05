
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FilterAlignmentsWith500NonCR {
	public static int insertLength(String str){
		int length=0; int start=0;
		for(int i=1;i<str.length();i++){
			if(str.charAt(i)=='M'||str.charAt(i)=='D'){
				start=i+1;
			}
			else if(str.charAt(i)=='I'){
				length=length+Integer.parseInt(str.substring(start, i)); start=i+1;
			}
		}
		return length;		
	}
	public static int deleteLength(String str){
		int length=0; int start=0;
		for(int i=1;i<str.length();i++){
			if(str.charAt(i)=='M'||str.charAt(i)=='I'){
				start=i+1;
			}
			else if(str.charAt(i)=='D'){
				length=length+Integer.parseInt(str.substring(start, i)); start=i+1;
			}
		}
		return length;		
	}
	public static void main (String args[]){
		try{
			long startTime=System.currentTimeMillis();
			Scanner in = new Scanner(new File(args[0])); 	
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[1])));	
			String[] onepair; int length; double identity; String temp=""; writer.write(in.nextLine()); writer.newLine(); int num=0;
			
			
			while(in.hasNextLine()){
				temp=in.nextLine();
				onepair=temp.trim().split("[\\p{Space}]+");
				if(num<3){
//					System.out.println(insertLength(onepair[1])+"   "+deleteLength(onepair[1])+"  "+onepair[13]);
				}
				length=Integer.parseInt(onepair[10])+Integer.parseInt(onepair[11])+insertLength(onepair[1]); 
				if(length>(Integer.parseInt(onepair[10])+Integer.parseInt(onepair[11])+deleteLength(onepair[1]))){
					length=Integer.parseInt(onepair[10])+Integer.parseInt(onepair[11])+deleteLength(onepair[1]);
				}
				if(length>=500&&Double.parseDouble(onepair[10])/(Integer.parseInt(onepair[10])+Integer.parseInt(onepair[11])+Integer.parseInt(onepair[13]))>=0.7){
					writer.write(temp); writer.newLine(); num++;
				}				
			}
			writer.close();
//			System.out.println("end!  "+num+"   startTime: "+startTime);
//			System.out.println(temp); 
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}

}

