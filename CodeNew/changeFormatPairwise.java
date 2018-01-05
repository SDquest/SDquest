
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class changeFormatPairwise {
	
	public static int distance(String str, int start, int end){
		int dis=0;
		for(int i=start;i<end;i++){
			if(str.charAt(i)!='N'){
				dis++;
			}
		}
		return dis;
	}
	public static void main (String args[]){
		try{
			Scanner inChr=new Scanner(new File("genome.masked_all.fasta"));
			Scanner in=new Scanner(new File("Genome_size_Indexes.txt")); String str; 
			ArrayList<String> chrs=new ArrayList<String>();  boolean end=false;
			ArrayList<String> segs=new ArrayList<String>();
			while(in.hasNextLine()&&!end){
				str=in.nextLine(); 
				if(str.charAt(0)=='>'){
					chrs.add(str.substring(1));
					inChr.nextLine(); segs.add(inChr.nextLine());
				}
				else{
					end=true;
				}
			}
			in.close(); inChr.close();
//			System.out.println("end reading! "+chrs.size()+"  "+segs.size());
			
			in=new Scanner(new File("SCN_LastzResult_500NonCR_NewExtendPec50.txt"));
			BufferedWriter writer=new BufferedWriter (new FileWriter(new File("Pairwise_SDs.txt")));
			in.nextLine(); String[] onepair; int indexW=0; double identity; int length1, length2;
			writer.write("index  cigar  chrA  startA  endA  strandA  chrB  startB  endB  strandB  Match  Mismath  sGap  identity"); writer.newLine();
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+");
				if(Integer.parseInt(onepair[4])!=Integer.parseInt(onepair[7])||Integer.parseInt(onepair[5])>Integer.parseInt(onepair[9])||Integer.parseInt(onepair[8])>Integer.parseInt(onepair[6])){
					identity=Double.parseDouble(onepair[10])/(Integer.parseInt(onepair[10])+Integer.parseInt(onepair[11])+Integer.parseInt(onepair[13]));
					length1=distance(segs.get(Integer.parseInt(onepair[4])),Integer.parseInt(onepair[5]),Integer.parseInt(onepair[6]));
					length2=distance(segs.get(Integer.parseInt(onepair[7])),Integer.parseInt(onepair[8]),Integer.parseInt(onepair[9]));
					
					if(identity>=0.7&&length1>=500&&length2>=500){
						writer.write(indexW+"  "+onepair[1]+"  "+chrs.get(Integer.parseInt(onepair[4]))+"  "+onepair[5]+"  "+onepair[6]+"  "+onepair[14]+"  "+chrs.get(Integer.parseInt(onepair[7]))+"  "+onepair[8]+"  "+onepair[9]+"  "+onepair[15]+"  ");
				        writer.write(onepair[10]+"  "+onepair[11]+"  "+onepair[13]+"  "+identity);
				        writer.newLine(); indexW++;
					}
				}
			}
			in.close(); writer.close();
			System.out.println("Pairwise SDs can be found in Pairwise_SDs.txt!");
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
	
}
