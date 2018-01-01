
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class CopyEndPoints {

	public static String reverse(String str){
		char[] temp=new char[str.length()];
		for(int i=str.length()-1;i>=0;i--){
			if(str.charAt(i)=='N'){
				temp[str.length()-1-i]='N';
			}
			else if(str.charAt(i)=='A'){
				temp[str.length()-1-i]='T';
			}
			else if(str.charAt(i)=='C'){
				temp[str.length()-1-i]='G';
			}
			else if(str.charAt(i)=='G'){
				temp[str.length()-1-i]='C';
			}
			else if(str.charAt(i)=='T'){
				temp[str.length()-1-i]='A';
			}
			else if(str.charAt(i)=='a'){
				temp[str.length()-1-i]='t';
			}
			else if(str.charAt(i)=='c'){
				temp[str.length()-1-i]='g';
			}
			else if(str.charAt(i)=='g'){
				temp[str.length()-1-i]='c';
			}
			else if(str.charAt(i)=='t'){
				temp[str.length()-1-i]='a';
			}
			else if(str.charAt(i)=='n'){
				temp[str.length()-1-i]='n';
			}
			else {
//				System.out.println("something wrong!");
			}
		}		
		return String.valueOf(temp);
	}
	
	public static String[] cigarAlignment(String seg1, String seg2, String cigar){		
		int start=0, length=0; int index1=0, index2=0, index=0, num;		
        for(int i=0;i<cigar.length();i++){
			if(cigar.charAt(i)=='M'||cigar.charAt(i)=='I'||cigar.charAt(i)=='D'){
				length=length+Integer.parseInt(cigar.substring(start, i)); start=i+1;
			}
		}
        char[] str1=new char[length]; char[] str2=new char[length]; start=0;
		for(int i=0;i<cigar.length();i++){
			if(cigar.charAt(i)=='M'){
				num=Integer.parseInt(cigar.substring(start, i)); start=i+1;
				while(num>0){
					num--;
					while(seg1.charAt(index1)=='N'){
						index1++;
					}
					str1[index]=seg1.charAt(index1); index1++;
					while(seg2.charAt(index2)=='N'){
						index2++;
					}
					str2[index]=seg2.charAt(index2); index2++; index++;
				}
			}
			if(cigar.charAt(i)=='D'){
				num=Integer.parseInt(cigar.substring(start, i)); start=i+1;
				while(num>0){
					num--;
					while(seg1.charAt(index1)=='N'){
						index1++;
					}
					str1[index]=seg1.charAt(index1); index1++;
					str2[index]='-'; index++;
				}
			}
			if(cigar.charAt(i)=='I'){
				num=Integer.parseInt(cigar.substring(start, i)); start=i+1;
				while(num>0){
					num--;
					str1[index]='-'; 
					while(seg2.charAt(index2)=='N'){
						index2++;
					}
					str2[index]=seg2.charAt(index2); index2++; index++;
				}
			}
		}
		String[] temp={String.valueOf(str1),String.valueOf(str2)}; 
		return temp;
	}
	public static void computeIndex(String[] alignment, int[] seg1To2, int[] seg2To1, String seg1, String seg2, String str1, String str2){
		int[][] temp=new int[2][alignment[0].length()]; int index;
		if(str1.equalsIgnoreCase("+")){
			index=0;
			while(seg1.charAt(index)=='N'){
				index++;
			}
			int p=0;
			while(alignment[0].charAt(p)=='-'){
				temp[0][p]=index; p++;
			}
			temp[0][p]=index; 
			for(int i=p+1;i<alignment[0].length();i++){
				if(alignment[0].charAt(i)!='-'){
					index++;
					while(seg1.charAt(index)=='N'){
						index++;
					}
				}
				temp[0][i]=index;				
			}
		}
		else{
			index=0;
			while(seg1.charAt(index)=='N'){
				index++;
			}
			int p=0;
			while(alignment[0].charAt(p)=='-'){
				temp[0][p]=seg1.length()-1-index; p++;
			}
			temp[0][p]=seg1.length()-1-index; 
			for(int i=p+1;i<alignment[0].length();i++){
				if(alignment[0].charAt(i)!='-'){
					index++;
					while(seg1.charAt(index)=='N'){
						index++;
					}
				}
				temp[0][i]=seg1.length()-1-index;				
			}
		}
		
		if(str2.equalsIgnoreCase("+")){
			index=0;
			while(seg2.charAt(index)=='N'){
				index++;
			}
			int p=0;
			while(alignment[1].charAt(p)=='-'){
				temp[1][p]=index; p++;
			}
			temp[1][p]=index; 
			for(int i=p+1;i<alignment[1].length();i++){
				if(alignment[1].charAt(i)!='-'){
					index++;
					while(seg2.charAt(index)=='N'){
						index++;
					}
				}
				temp[1][i]=index;				
			}
		}
		else{
			index=0;
			while(seg2.charAt(index)=='N'){
				index++;
			}
			int p=0;
			while(alignment[1].charAt(p)=='-'){
				temp[1][p]=seg2.length()-1-index; p++;
			}
			temp[1][p]=seg2.length()-1-index; 
			for(int i=p+1;i<alignment[1].length();i++){
				if(alignment[1].charAt(i)!='-'){
					index++;
					while(seg2.charAt(index)=='N'){
						index++;
					}
				}
				temp[1][i]=seg2.length()-1-index;				
			}
		}
		
		for(int i=0;i<alignment[0].length();i++){
			if(alignment[0].charAt(i)!='-'){
				seg1To2[temp[0][i]]=temp[1][i];
			}
			if(alignment[1].charAt(i)!='-'){
				seg2To1[temp[1][i]]=temp[0][i];
			}
		}
		
	}
	
	public static boolean copyEnds(boolean[] line1, boolean[] line2, int[] seg1to2, int[] seg2to1, int start1, int end1, int start2, int end2){
		int index1, index2; 
		boolean copy=false;
		for(int i=start1; i<end1;i++){
			if(line1[i]){
				index1=i-start1; index2=seg1to2[index1]; 
//				if(index2==-1){
//					System.out.println("something wrong!  1");
//				}
				if(!line2[start2+index2]){
					line2[start2+index2]=true; copy=true;
				}				
			}
		}
		for(int i=start2; i<end2;i++){
			if(line2[i]){
				index2=i-start2; index1=seg2to1[index2]; 
//				if(index1==-1){
//					System.out.println("something wrong!  2");
//				}
				if(!line1[start1+index1]){
					line1[start1+index1]=true; copy=true;
				}
			}
		}
		return copy;
	}
	public static void main(String args[]){
		try{
			long startTime=System.currentTimeMillis();
			ArrayList<boolean[]> lines=new ArrayList<boolean[]>(); 
			ArrayList<String> chroms=new ArrayList<String>();
			Scanner inChr = new Scanner(new File(args[0])); String temp; int chrNum=0; //"genome.masked_all.fasta"
			while(inChr.hasNextLine()){
				inChr.nextLine(); temp=inChr.nextLine();
				chroms.add(temp);
				boolean[] ends=new boolean[temp.length()];
				for(int j=0;j<ends.length;j++){
					ends[j]=false;
				}
				lines.add(ends); chrNum++;
			}
//			System.out.println("end reading chrs! "+chrNum);
			
			Scanner in = new Scanner(new File(args[1]));  String[] onepair;  //"BG_SDIndexes.fasta"
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+");
				lines.get(Integer.parseInt(onepair[0]))[Integer.parseInt(onepair[1])]=true;
				lines.get(Integer.parseInt(onepair[0]))[Integer.parseInt(onepair[2])-1]=true;
			}
			in.close();
			
			boolean copy=true; boolean tempCopy; int num=0;
			while(copy){
				copy=false; num++;
				in=new Scanner(new File(args[2])); in.nextLine();  //"SCN_LastzResult_500NonCR_FilterPair.txt"
				String cigar, seg1, seg2; int chr, start, end, otherChr, otherStart, otherEnd; String[] alignment=new String[2]; int index=0;
				while(in.hasNextLine()){
					onepair=in.nextLine().trim().split("[\\p{Space}]+"); index++;
					cigar=onepair[1]; chr=Integer.parseInt(onepair[4]); start=Integer.parseInt(onepair[5]); end=Integer.parseInt(onepair[6]);
					otherChr=Integer.parseInt(onepair[7]); otherStart=Integer.parseInt(onepair[8]); otherEnd=Integer.parseInt(onepair[9]);
					if(onepair[14].equalsIgnoreCase("+")){
						seg1=chroms.get(chr).substring(start, end);					
					}
					else{
						seg1=reverse(chroms.get(chr).substring(start, end));
					}
					if(onepair[15].equalsIgnoreCase("+")){
						seg2=chroms.get(otherChr).substring(otherStart, otherEnd);
					}
					else{
						seg2=reverse(chroms.get(otherChr).substring(otherStart, otherEnd));
					}
					alignment=cigarAlignment(seg1,seg2,cigar);
					
					int[] seg1to2=new int[seg1.length()]; int[] seg2to1=new int[seg2.length()];
					for(int i=0;i<seg1.length();i++){
						seg1to2[i]=-1;
					}
					for(int i=0;i<seg2.length();i++){
						seg2to1[i]=-1;
					}
					
					computeIndex(alignment, seg1to2, seg2to1, seg1, seg2, onepair[14], onepair[15]);
					tempCopy=copyEnds(lines.get(chr), lines.get(otherChr), seg1to2, seg2to1, start, end, otherStart, otherEnd);
					if(!copy&&tempCopy){
						copy=true;
					}
//					if(index%1000==0){
//						System.out.println(index);
//					}
				}
				in.close();
			}			
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[3]))); //"EndpointsLine.txt"	
			for(int n=0;n<lines.size();n++){
				writer.write(">"+n); writer.newLine();
				for(int i=0;i<lines.get(n).length;i++){
					if(lines.get(n)[i]){
						writer.write("1");
					}
					else{
						writer.write("0");
					}
				}
				writer.newLine(); 
			}
			writer.close(); long endTime=System.currentTimeMillis();
//			System.out.println("end copy endpoints! "+num+"   runTime: "+(endTime-startTime));
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
}
