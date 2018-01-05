
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class masktandemRepeats {

	public static void main (String args[]){
		try{
//			long startTime=System.currentTimeMillis();
			Scanner in =new Scanner(new File(args[0]));
			Scanner inTR =new Scanner(new File(args[1]));
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[2])));
			String[] onepairTR; char[] seg; String temp; int TRLength=0;  boolean end=false; int TRnum=0; 
			
			if(inTR.hasNextLine()){
				String tempTR=inTR.nextLine();
				while(in.hasNextLine()) {
					temp=in.nextLine();
					writer.write(temp); writer.newLine();
//					System.out.println(temp);
					if(!end&&temp.substring(1).equalsIgnoreCase(tempTR.substring(1))) {
						seg=in.nextLine().toCharArray();
						tempTR=inTR.nextLine();//.trim().split("[\\p{Space}]+");
						while(tempTR.charAt(0)!='@'&&!end) {
							onepairTR=tempTR.trim().split("[\\p{Space}]+");
							
//							System.out.println(onepairTR[0]+"  "+onepairTR[1]+"  "+seg.length);
							
							for(int i=Integer.parseInt(onepairTR[0]); i<Integer.parseInt(onepairTR[1]); i++) {
								if(seg[i]!='N') {
									seg[i]='N'; TRLength++;
								}
							}
							if(inTR.hasNextLine()) {
								tempTR=inTR.nextLine();//.trim().split("[\\p{Space}]+");
							}
							else {
								end=true;
							}
							
						}
						for(int i=0;i<seg.length;i++) {
							writer.write(seg[i]);
						}
						writer.newLine(); TRnum++;
					}
					else {
						writer.write(in.nextLine()); writer.newLine();
					}
					
					
				}
			}
			else{
				while(in.hasNextLine()){
					writer.write(in.nextLine()); writer.newLine();
				}
			}
			
//			long endTime=System.currentTimeMillis();  
			writer.close(); in.close(); inTR.close();
			System.out.println("Tandem Repeat Finder Detected:  "+TRLength+" bp tandem repeats in the input genome. ");
			
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
	
}
