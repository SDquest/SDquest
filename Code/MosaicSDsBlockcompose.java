
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MosaicSDsBlockcompose {

	
	public static void main(String args[]){
		try{
			Scanner in = new Scanner(new File("Genome_size_Indexes.txt")); int GenomeSize=0; String str; boolean isEnd=false;
			ArrayList<String> chrs=new ArrayList<String>();
			while(in.hasNextLine()&&!isEnd){
				str=in.nextLine();
				if(str.charAt(0)=='>'){
					GenomeSize++; chrs.add(str.substring(1));
				}
				else{
					isEnd=true;
				}
			}
			
			BufferedWriter writer=new BufferedWriter(new FileWriter(new File("MosaicSDs_SDblockIndexes.txt"))); 
			writer.write("chr   start   end   SDblocksIndexes"); writer.newLine(); 
			
			in = new Scanner(new File("ElementSDs_LengthAndMulti.fasta")); in.nextLine(); 
			ArrayList<ArrayList<int[]>> elements=new ArrayList<ArrayList<int[]>>();
			for(int i=0;i<GenomeSize;i++){
				ArrayList<int[]> temp=new ArrayList<int[]>(); elements.add(temp);
			}
			
			String[] onepair; int index=0;
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+"); index++;
				int[] temp={Integer.parseInt(onepair[2]), Integer.parseInt(onepair[3]), Integer.parseInt(onepair[0])};
				elements.get(Integer.parseInt(onepair[1])).add(temp);
			}
			in.close();
//			System.out.println("element SDs size!  "+index);
			
			int[] SDblock=new int[index]; 
			for(int i=0;i<SDblock.length;i++){
				SDblock[i]=0; 
			}
			in = new Scanner(new File("blocks.fasta")); String temp; index=0; int length; int elementSD;
			while(in.hasNextLine()){
				temp=in.nextLine();
				if(temp.length()>0){
					onepair=temp.trim().split("[\\p{Space}]+");
					index++;
					for(int i=0;i<onepair.length;i++){
						length=onepair[i].length(); elementSD=Integer.parseInt(onepair[i].substring(0, length-1));
						if(elementSD>0){
							SDblock[elementSD-1]=index;
						}
						else{
							SDblock[Math.abs(elementSD)-1]=-index;
						}
					}
				}
			}
			in.close();
//			System.out.println("SDblock size!  "+index); 
			int missSD=0;
			for(int i=0;i<SDblock.length;i++){
				if(SDblock[i]==0){
					missSD++;
				}
			}
//			System.out.println("element SD not be assigned to a SDblock:  "+missSD);
			
			
			ArrayList<ArrayList<Integer>> mosaicSDs=new ArrayList<ArrayList<Integer>>();
			ArrayList<String[]> mosaicSDsIndex=new ArrayList<String[]>();
			in=new Scanner(new File("BG_MosaicSDs.fasta")); in.nextLine();
			index=0; int chr, start, end;  missSD=0;
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+");
				chr=Integer.parseInt(onepair[0]); start=Integer.parseInt(onepair[1]);
				end=Integer.parseInt(onepair[2]); 
				ArrayList<Integer> tempSD=new ArrayList<Integer>();
				for(int i=0;i<elements.get(chr).size();i++){
					if(elements.get(chr).get(i)[0]>=start&&elements.get(chr).get(i)[1]<=end){
						if(SDblock[elements.get(chr).get(i)[2]]!=0){
							tempSD.add(SDblock[elements.get(chr).get(i)[2]]);
						}
						else{
							missSD++;
						}
					}
				}
				mosaicSDs.add(tempSD);
				String[] tempIndex={onepair[0], onepair[1], onepair[2]};
				mosaicSDsIndex.add(tempIndex);
			}
			
			int missMosaicSD=0;
			for(int i=0;i<mosaicSDs.size();i++){
				if(mosaicSDs.get(i).size()>0){
					writer.write(chrs.get(Integer.parseInt(mosaicSDsIndex.get(i)[0]))+"  "+mosaicSDsIndex.get(i)[1]+"  "+mosaicSDsIndex.get(i)[2]+" :  ");  
					for(int j=0;j<mosaicSDs.get(i).size();j++){
						writer.write(mosaicSDs.get(i).get(j)+"  ");
					}
					writer.newLine();
				}
				else{
					missMosaicSD++;
				}
			}
			writer.close();
			System.out.println("end mosaicSD analysis! SDblock indexes of Mosaic SDs can be found in MosaicSDs_SDblockIndexes.txt");
			
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
}
