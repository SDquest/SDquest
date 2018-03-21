
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class CombineLine {

	public static void main (String args[]){
		try{
			long startTime=System.currentTimeMillis();
			double denseBound=0.01;  int combineLength=500;  double lengthBound=500;
			Scanner in =new Scanner(new File(args[0])); //"FrequencyLine.txt"
			Scanner inGenome =new Scanner(new File(args[1])); //"genome.txt"
		    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[2])));  //"CombineLine.txt"	
		    String freq, genome; int start, end, dis, kmerNum, length=0, index=0; double totalDis; boolean find, findN; char curFreq, curGenome;
		    int lastIndex, nextIndex; int line=0;
		    
			while(in.hasNextLine()) {
			    writer.write(in.nextLine()); writer.newLine();  line++;
			    freq=in.nextLine(); length=freq.length(); 
				inGenome.nextLine(); genome=inGenome.nextLine();
				
			    start=0; end=0;   totalDis=0; kmerNum=0; index=0; find=false;
				while(index<length&&(!find)){
					curFreq=freq.charAt(index); curGenome=genome.charAt(index);
					if((curFreq!='0')&&(curFreq!='1')&&(curGenome!='N')&&(curGenome!='n')){
						find=true; start=index; end=start;
						totalDis=1; kmerNum=1;
					}
					else{
						writer.write("0");;					
					}
					index++;
				}
				nextIndex=-1; lastIndex=-1;
				
				while(index<length){
					
					find=false; findN=false;  dis=1;
					while(index<length&&(!find)&&(!findN)){
						curFreq=freq.charAt(index); 
						curGenome=genome.charAt(index);
						index++;
						if((curFreq!='0')&&(curFreq!='1')){
							find=true;
						}
						else if(curGenome=='N'||curGenome=='n'){
							findN=true;
						}
						else{
							if(curFreq=='1'){
								dis++;
							}
						}
					}
					if(findN==true){
						if(totalDis>=lengthBound){
							if(lastIndex!=-1){
								for(int i=start;i<lastIndex;i++){
									writer.write("1");
								}
								index=lastIndex;
								while((index<length)&&(freq.charAt(index)=='0'||freq.charAt(index)=='1')){
									index++; writer.write("0");
								}
								start=index; end=start; index++;
							}
							else {
								for(int i=start;i<nextIndex;i++){
									writer.write("0");
								}
								start=nextIndex; end=start; index=nextIndex+1;
								
							}
						}
						else{
							for(int i=start;i<index;i++){
								writer.write("0");
							}
							while((index<length)&&(freq.charAt(index)=='0'||freq.charAt(index)=='1')){
								index++; writer.write("0");
							}
							start=index; end=start; index++;
						}
						totalDis=1; kmerNum=1; lastIndex=-1; nextIndex=-1;
					}
					if(find==true){
						if(dis<=combineLength){
							end=index; totalDis=totalDis+dis; kmerNum++;
							if(kmerNum==2){
								nextIndex=index-1;
							}
							if(totalDis>=lengthBound&&kmerNum/totalDis>=denseBound){
								lastIndex=index;
							}
						}
						else{
							if(totalDis>=lengthBound){
								if(lastIndex!=-1){
									for(int i=start;i<lastIndex;i++){
										writer.write("1");
									}
									index=lastIndex;
									while((index<length)&&(freq.charAt(index)=='0'||freq.charAt(index)=='1')){
										index++; writer.write("0");
									}
									start=index; end=start; index++;
								}
								else {
									for(int i=start;i<nextIndex;i++){
										writer.write("0");
									}
									start=nextIndex; end=start; index=nextIndex+1;
								}
							}
							else{
								for(int i=start;i<index-1;i++){
									writer.write("0");
								}
								start=index-1; end=start;
							}
							totalDis=1; kmerNum=1; lastIndex=-1; nextIndex=-1;
						}						
					}
					if(index>=length){
						if(totalDis>=lengthBound){
							if(lastIndex>0){
								for(int i=start;i<lastIndex;i++){
									writer.write("1");
								}
								index=lastIndex;
								while((index<length)&&(freq.charAt(index)=='0'||freq.charAt(index)=='1')){
									index++; writer.write("0");
								}
								start=index; end=start; index++;
							}
							else {
								for(int i=start;i<nextIndex;i++){
									writer.write("0");
								}
								start=nextIndex; end=start; index=nextIndex+1;
							}
							totalDis=1; kmerNum=1; lastIndex=-1; nextIndex=-1;
						}
						else{
							for(int i=start;i<length;i++){
								writer.write("0");
							}
						}
					}					
										
				}
				writer.newLine();
				
			}
			writer.close(); in.close(); long endTime=System.currentTimeMillis();
//			System.out.println(" end Combine Line! ");
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
	
}

