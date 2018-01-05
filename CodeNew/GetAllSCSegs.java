
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class GetAllSCSegs {
	public static void main(String args[]){
		try{			
			long startTime=System.currentTimeMillis();
			int kmerLength=Integer.parseInt(args[4]);  int extendSpan=500;
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[2])));	int indexW=0; //"AllSegsOfSCN_CRMasked.fasta"
			BufferedWriter writerRe = new BufferedWriter(new FileWriter(new File(args[3])));  //"AllSegsOfSCN_CRRemoved.fasta"	
			Scanner inChr = new Scanner(new File(args[0])); //"genome.masked_all.fasta" 
			Scanner inSCN = new Scanner(new File(args[1])); //"CombineLine.txt" 
			String chr; String strSCN; int segIndex=-1;
			double SCNLength=0;  int numN=0; double GenomeLength=0;  int start, end=0, index;  char cur; int extendL=0;
			
			while(inChr.hasNextLine()) {
				inChr.nextLine();  chr=inChr.nextLine();
	            inSCN.nextLine();  strSCN=inSCN.nextLine(); segIndex++;
//				System.out.println("end reading chromosome and SCN line!");
				
				start=0; end=0;  index=0;  
				int length=strSCN.length(); GenomeLength=GenomeLength+chr.length();
//				System.out.println(segIndex+"  "+(length+(kmerLength-1))+"  "+chr.length());//test
      			while(index<length){
      				if(strSCN.charAt(index)=='1'){
						start=index;  index++;
						while((index<length)&&(strSCN.charAt(index)=='1')){	
							index++;
						}
						end=index+(kmerLength-1);//count kmer length
						
						extendL=0;
						while(extendL<extendSpan&&start>0){
							start--;
							while(start>0&&chr.charAt(start)=='N'){
								start--;
							}
							if(start>0){
								extendL++;
							}
						}
						extendL=0;
						while(extendL<extendSpan&&end<chr.length()){
							while(end<chr.length()&&chr.charAt(end)=='N'){
								end++;
							}
							if(end<chr.length()){
								end++; extendL++;
							}
						}
						
						writer.write(">"+indexW+"  "+segIndex+"  "+start+"  "+end); writer.newLine();  
						writerRe.write(">"+indexW+"  "+segIndex+"  "+start+"  "+end); writerRe.newLine();  indexW++;
						SCNLength=SCNLength+(end-start);
						for(int i=start;i<end;i++){
							cur=chr.charAt(i);
							writer.write(cur); 
							if(cur!='N'){
								writerRe.write(cur);
							}
							else{
								numN++;
							}
						}
						writer.newLine(); writerRe.newLine();	
//						if(indexW%1000==0){
//							System.out.println(indexW+"  "+index);
//						}
					}
					index++;					
				}      			
			}
			writer.close(); writerRe.close(); inChr.close(); inSCN.close(); long endTime=System.currentTimeMillis();
			System.out.println("SDquest identified "+indexW+" putative SDs!");
//			System.out.println("CR/SCNlength= "+numN/SCNLength+"   runTime: "+(endTime-startTime));
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}

}



