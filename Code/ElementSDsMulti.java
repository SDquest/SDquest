
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ElementSDsMulti {

	public static void main(String args[]){
		try{
			long startTime=System.currentTimeMillis();
			Scanner in = new Scanner(new File(args[0]));  // Genome_size_Indexes.txt
			int GenomeSize=0; String str; boolean isEnd=false;
			while(in.hasNextLine()&&!isEnd){
				str=in.nextLine();
				if(str.charAt(0)=='>'){
					GenomeSize++;
				}
				else{
					isEnd=true;
				}
			}
			
			ArrayList<ArrayList<int[]>> pairs=new ArrayList<ArrayList<int[]>>(); 
			for(int i=0;i<GenomeSize;i++){
				ArrayList<int[]> temp=new ArrayList<int[]>(); pairs.add(temp);
			}
			in = new Scanner(new File(args[1])); in.nextLine();  // ElementSDs_100.fasta
			String[] onepair; int index=0;
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+");
				int[] temp={Integer.parseInt(onepair[5]), Integer.parseInt(onepair[6]), Integer.parseInt(onepair[4]), index, 0};//0 is the multiplicity
				pairs.get(Integer.parseInt(onepair[1])).add(temp); index++;
				
			}
			in.close();
//			System.out.println(index);
	
			in=new Scanner(new File(args[2])); in.nextLine();  // SCN_LastzResult_500NonCR_NewExtendPec50.txt
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[3])));  // ElementSDs_LengthAndMulti.fasta
			writer.write("index   chr   start   end   midLength   multi"); writer.newLine();
			int chr, start, end; 
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+");
				chr=Integer.parseInt(onepair[4]); start=Integer.parseInt(onepair[5]); end=Integer.parseInt(onepair[6]);
				if(!onepair[16].equalsIgnoreCase("-1")){
					start=Integer.parseInt(onepair[16]);
				}
				if(!onepair[17].equalsIgnoreCase("-1")){
					end=Integer.parseInt(onepair[17]);
				}
				for(int i=0;i<pairs.get(chr).size();i++){
					if(pairs.get(chr).get(i)[0]>=start&&pairs.get(chr).get(i)[1]<=end){
						pairs.get(chr).get(i)[4]++;
					}
				}
				chr=Integer.parseInt(onepair[7]); start=Integer.parseInt(onepair[8]); end=Integer.parseInt(onepair[9]);
				if(!onepair[18].equalsIgnoreCase("-1")){
					start=Integer.parseInt(onepair[18]);
				}
				if(!onepair[19].equalsIgnoreCase("-1")){
					end=Integer.parseInt(onepair[19]);
				}
				for(int i=0;i<pairs.get(chr).size();i++){
					if(pairs.get(chr).get(i)[0]>=start&&pairs.get(chr).get(i)[1]<=end){
						pairs.get(chr).get(i)[4]++;
					}
				}
			}
			
			for(int i=0;i<pairs.size();i++){
				for(int j=0;j<pairs.get(i).size();j++){
					writer.write(pairs.get(i).get(j)[3]+"  "+i+"  "+pairs.get(i).get(j)[0]+"  "+pairs.get(i).get(j)[1]+"  "+pairs.get(i).get(j)[2]+"  "+pairs.get(i).get(j)[4]); writer.newLine();
				}
			}
			writer.close(); long endTime=System.currentTimeMillis();
//			System.out.println("end elementarySD multi!   runTime: "+(endTime-startTime));
			
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
}
