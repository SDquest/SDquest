
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class testLength {//
	
	public static void main(String args[]){
		try{		
			long startTime=System.currentTimeMillis();
			ArrayList<String> CRs=new ArrayList<String>();
			Scanner inChr = new Scanner(new File("genome.masked_all.fasta"));;
			while(inChr.hasNextLine()){
				inChr.nextLine(); CRs.add(inChr.nextLine());
			}
//			System.out.println("end reading! "); 
			inChr.close();
			
			Scanner in=new Scanner(new File("SCN_LastzResult_500NonCR_NewExtendPec50.txt"));//sort by number
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("length.fasta")));						
			in.nextLine(); int length1, length2, chr, start, end;
			String[] onepair; int index=0;
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+"); 
				length1=0; length2=0; 
				chr=Integer.parseInt(onepair[4]); start=Integer.parseInt(onepair[5]); end=Integer.parseInt(onepair[6]);
				if(!onepair[16].equalsIgnoreCase("-1")){
					start=Integer.parseInt(onepair[16]);
				}
				if(!onepair[17].equalsIgnoreCase("-1")){
					end=Integer.parseInt(onepair[17]);
				}
				for(int i=start;i<end;i++){
					if(CRs.get(chr).charAt(i)!='N'){
						length1++;
					}
				}
				
				chr=Integer.parseInt(onepair[7]); start=Integer.parseInt(onepair[8]); end=Integer.parseInt(onepair[9]);
				if(!onepair[18].equalsIgnoreCase("-1")){
					start=Integer.parseInt(onepair[18]);
				}
				if(!onepair[19].equalsIgnoreCase("-1")){
					end=Integer.parseInt(onepair[19]);
				}
				for(int i=start;i<end;i++){
					if(CRs.get(chr).charAt(i)!='N'){
						length2++;
					}
				}
				
				writer.write(index+"  "+length1+"  "+length2+"  "+Math.abs(length1-length2)); writer.newLine(); index++;
			}
			
			
            writer.close(); in.close(); long endTime=System.currentTimeMillis();
//            System.out.println("end testlength!  "+(endTime-startTime)); 
                        
            
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}

}
