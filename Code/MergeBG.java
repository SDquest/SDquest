
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class MergeBG {

	public static void main(String args[]){
		try{
			Scanner in = new Scanner(new File(args[0]));  //"BG_SDIndexes.sorted.fasta"	
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[1])));  //"BG_MosaicSDs.fasta"	
			String[] onepair;	int chr, start, end; int num=0;
			onepair=in.nextLine().trim().split("[\\p{Space}]+");
			chr=Integer.parseInt(onepair[0]); start=Integer.parseInt(onepair[1]); end=Integer.parseInt(onepair[2]);
			writer.write("chr  start  end"); writer.newLine();
			
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+");
				if(Integer.parseInt(onepair[1])!=(Integer.parseInt(onepair[2]))){
					if(Integer.parseInt(onepair[0])==chr){
						if(Integer.parseInt(onepair[1])>end){
							writer.write(chr+"  "+start+"  "+end); writer.newLine(); num++;
							start=Integer.parseInt(onepair[1]); 
							end=Integer.parseInt(onepair[2]);
						}
						else{
							if(Integer.parseInt(onepair[2])>end){
								end=Integer.parseInt(onepair[2]);
							}
						}
					}
					else{
						writer.write(chr+"  "+start+"  "+end); writer.newLine();  num++;
						chr=Integer.parseInt(onepair[0]); 
						start=Integer.parseInt(onepair[1]); 
						end=Integer.parseInt(onepair[2]);
					}	
				}				
			}
			writer.write(chr+"  "+start+"  "+end); writer.newLine(); num++;	
			writer.close(); in.close();
//			System.out.println("SDquest identifies  "+num+" BG Mosaic SDs! ");
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
}
