
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class LastzPost {

	public static int recover(String str, int index){
		int num=0; 
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)!='N'){
				num++;
			}
			if(num==index+1){
				return i;
			}
		}
//		if(num==index){
//			return str.length();
//		}
//		System.out.println("something wrong!");
		return -1;
	}
	public static int reverseRecover(String str, int index){
		int num=0;
		for(int i=str.length()-1;i>=0;i--){
			if(str.charAt(i)!='N'){
				num++;
			}
			if(num==index+1){
				return i;
			}
		}
//		System.out.println("something wrong in reverse case!");
		return -1;
	}
	public static int gapLength(String str){
		int length=0; int start=0;
		for(int i=1;i<str.length();i++){
			if(str.charAt(i)=='M'){
				start=i+1;
			}
			else if(str.charAt(i)=='I'||str.charAt(i)=='D'){
				length=length+Integer.parseInt(str.substring(start, i)); start=i+1;
			}
		}
		return length;		
	}
	public static boolean wrongGap(String str){
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)!='M'&&str.charAt(i)!='D'&&str.charAt(i)!='I'&&str.charAt(i)!='1'&&str.charAt(i)!='2'&&str.charAt(i)!='3'&&str.charAt(i)!='4'&&str.charAt(i)!='5'&&str.charAt(i)!='6'&&str.charAt(i)!='7'&&str.charAt(i)!='8'&&str.charAt(i)!='9'&&str.charAt(i)!='0'){
//				System.out.println(str.charAt(i));
				return true;
			}
		}
		return false;
	}
	public static void main (String args[]){
		try{
			long startTime=System.currentTimeMillis();
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[2])));	//"SCN_LastzResult.txt"
			writer.write("index  cigar seg1  seg2  chr1  start1  end1  chr2  start2  end2  Match  Mismatch  nGap  sGap  strand1  strand2"); writer.newLine();			
			
			ArrayList<String> Segs=new ArrayList<String>();   String[] onepair;  int num=0;
			ArrayList<int[]> SegsIndex=new ArrayList<int[]>();  int index=0; int seg1, seg2; int length=0;
			Scanner inSeg=new Scanner(new File(args[0]));  // "AllSegsOfSCN_CRMasked.fasta"
			while(inSeg.hasNextLine()){
				onepair=inSeg.nextLine().trim().split("[\\p{Space}]+"); 
				int[] temp={Integer.parseInt(onepair[1]), Integer.parseInt(onepair[2]), Integer.parseInt(onepair[3])};
			    SegsIndex.add(temp);
			    Segs.add(inSeg.nextLine());
			}
//			System.out.println("end reading segs!  "+Segs.size());
			
			Scanner in = new Scanner(new File(args[1]));  in.nextLine(); int p=1; // "AllSegsOfSCN_CRRemoved.info"
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+"); p++;//i++;
				if(onepair.length==20&&(!onepair[0].equalsIgnoreCase("#cigar"))){    
					if(wrongGap(onepair[0])){
//						System.out.println("wrong cigar line:    "+p); 
						num++;
					}
					else{
						seg1=Integer.parseInt(onepair[2]); seg2=Integer.parseInt(onepair[7]); length=gapLength(onepair[0])+Integer.parseInt(onepair[16])+Integer.parseInt(onepair[17]);
                        if(seg1!=seg2||Integer.parseInt(onepair[10])>Integer.parseInt(onepair[6])||Integer.parseInt(onepair[5])>Integer.parseInt(onepair[11])){
							writer.write(index+"  "+onepair[0]+"  "+seg1+"  "+seg2+"  ");
							if(onepair[3].equalsIgnoreCase("+")){
								writer.write(SegsIndex.get(seg1)[0]+"  "+(SegsIndex.get(seg1)[1]+recover(Segs.get(seg1),Integer.parseInt(onepair[5])))+"  "+(SegsIndex.get(seg1)[1]+recover(Segs.get(seg1),(Integer.parseInt(onepair[6])-1))+1)+"  ");
							}
							else{
								writer.write(SegsIndex.get(seg1)[0]+"  "+(SegsIndex.get(seg1)[1]+reverseRecover(Segs.get(seg1),(Integer.parseInt(onepair[6])-1)))+"  "+(SegsIndex.get(seg1)[1]+reverseRecover(Segs.get(seg1),Integer.parseInt(onepair[5]))+1)+"  ");
							}
							
							if(onepair[8].equalsIgnoreCase("+")){
								writer.write(SegsIndex.get(seg2)[0]+"  "+(SegsIndex.get(seg2)[1]+recover(Segs.get(seg2),Integer.parseInt(onepair[10])))+"  "+(SegsIndex.get(seg2)[1]+recover(Segs.get(seg2),(Integer.parseInt(onepair[11])-1))+1)+"  ");
							}
							else{
								writer.write(SegsIndex.get(seg2)[0]+"  "+(SegsIndex.get(seg2)[1]+reverseRecover(Segs.get(seg2),(Integer.parseInt(onepair[11])-1)))+"  "+(SegsIndex.get(seg2)[1]+reverseRecover(Segs.get(seg2),Integer.parseInt(onepair[10]))+1)+"  ");
							}
							writer.write(onepair[16]+"  "+onepair[17]+"  "+onepair[18]+"  "+gapLength(onepair[0])+"  "+onepair[3]+"  "+onepair[8]);								
							writer.newLine();  index++;
//							System.out.println(index);
//							if(index%10000000==0){
//								System.out.println(index+"  "+onepair[2]+"  "+onepair[7]);
//							}
						}
					}
					
				}
			}		
			writer.close(); in.close(); long endTime=System.currentTimeMillis();
//			System.out.println("end!  "+index+"   wrongGap: "+num+"  runTime: "+(endTime-startTime));
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
				
	}
}
