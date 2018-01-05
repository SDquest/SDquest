
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class GetIndexesBG {//
	public static void main(String args[]){
		try{	
			long startTime=System.currentTimeMillis();
			Scanner in=new Scanner(new File(args[0])); in.nextLine(); //"SCN_LastzResult_500NonCR_FilterPair.txt"
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[1])));  //"BG_SDIndexes.fasta"	
			String[] onepair;
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+");
				writer.write(onepair[4]+"  "+onepair[5]+"  "+onepair[6]); writer.newLine();
				writer.write(onepair[7]+"  "+onepair[8]+"  "+onepair[9]); writer.newLine();
			}
			in.close();  writer.close(); long endTime=System.currentTimeMillis();
//		    System.out.println("end reading BG SD indexes!   "+(endTime-startTime));
			
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}

}
