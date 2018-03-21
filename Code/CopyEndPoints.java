
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
	public static String[] endsCigarAlignment(String seg1, String seg2, String cigar){		
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
					str1[index]=seg1.charAt(index1); index1++;
					str2[index]=seg2.charAt(index2); index2++; index++;
				}
			}
			if(cigar.charAt(i)=='D'){
				num=Integer.parseInt(cigar.substring(start, i)); start=i+1;
				while(num>0){
					num--;
					str1[index]=seg1.charAt(index1); index1++;
					str2[index]='-'; index++;
				}
			}
			if(cigar.charAt(i)=='I'){
				num=Integer.parseInt(cigar.substring(start, i)); start=i+1;
				while(num>0){
					num--;
					str1[index]='-'; 
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
			while(alignment[0].charAt(p)=='-'||alignment[0].charAt(p)=='N'){
				temp[0][p]=index; p++;
			}
			temp[0][p]=index; 
			for(int i=p+1;i<alignment[0].length();i++){
				if(alignment[0].charAt(i)!='-'&&alignment[0].charAt(i)!='N'){
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
			while(alignment[0].charAt(p)=='-'||alignment[0].charAt(p)=='N'){
				temp[0][p]=seg1.length()-1-index; p++;
			}
			temp[0][p]=seg1.length()-1-index; 
			for(int i=p+1;i<alignment[0].length();i++){
				if(alignment[0].charAt(i)!='-'&&alignment[0].charAt(i)!='N'){
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
			while(alignment[1].charAt(p)=='-'||alignment[1].charAt(p)=='N'){
				temp[1][p]=index; p++;
			}
			temp[1][p]=index; 
			for(int i=p+1;i<alignment[1].length();i++){
				if(alignment[1].charAt(i)!='-'&&alignment[1].charAt(i)!='N'){
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
			while(alignment[1].charAt(p)=='-'||alignment[1].charAt(p)=='N'){
				temp[1][p]=seg2.length()-1-index; p++;
			}
			temp[1][p]=seg2.length()-1-index; 
			for(int i=p+1;i<alignment[1].length();i++){
				if(alignment[1].charAt(i)!='-'&&alignment[1].charAt(i)!='N'){
					index++;
					while(seg2.charAt(index)=='N'){
						index++;
					}
				}
				temp[1][i]=seg2.length()-1-index;				
			}
		}
		
		for(int i=0;i<alignment[0].length();i++){
			if(alignment[0].charAt(i)!='-'&&alignment[0].charAt(i)!='N'){
				seg1To2[temp[0][i]]=temp[1][i];
			}
			if(alignment[1].charAt(i)!='-'&&alignment[1].charAt(i)!='N'){
				seg2To1[temp[1][i]]=temp[0][i];
			}
		}
		
		index=0; int start=index;
		while(index<seg1To2.length&&seg1To2[index]==-1){
			index++;
		}
		for(int i=start;i<index;i++){
			seg1To2[i]=seg1To2[index];
		}
		index++; start=index;
		while(index<seg1To2.length){
			while(index<seg1To2.length&&seg1To2[index]==-1){
				index++;
			}
			for(int i=start;i<index;i++){
				seg1To2[i]=seg1To2[start-1];
			}
			index++; start=index;
		}
		
		index=0;  start=index;
		while(index<seg2To1.length&&seg2To1[index]==-1){
			index++;
		}
		for(int i=start;i<index;i++){
			seg2To1[i]=seg2To1[index];
		}
		index++; start=index;
		while(index<seg2To1.length){
			while(index<seg2To1.length&&seg2To1[index]==-1){
				index++;
			}
			for(int i=start;i<index;i++){
				seg2To1[i]=seg2To1[start-1];
			}
			index++; start=index;
		}
		
	/*	for(int i=0;i<seg1To2.length;i++){//test
			if(str2.equalsIgnoreCase("+")){
				if(seg2.charAt(seg1To2[i])=='N'){
					System.out.println(i+"  wrong seg1to2  "+seg1To2[i]);
				}
			}
			else{
				if(seg2.charAt(seg2.length()-1-seg1To2[i])=='N'){
					System.out.println(i+"  wrong seg1to2  "+(seg2.length()-1-seg1To2[i]));
				}
			}
			
		}
		for(int i=0;i<seg2To1.length;i++){
			if(str1.equalsIgnoreCase("+")){
				if(seg1.charAt(seg2To1[i])=='N'){
					System.out.println(i+"  wrong seg2To1  "+seg2To1[i]);
				}
			}
			else{
				if(seg1.charAt(seg1.length()-1-seg2To1[i])=='N'){
					System.out.println(i+"  wrong seg2To1  "+(seg1.length()-1-seg2To1[i]));
				}
			}
		}*/
		
	}
	
	public static boolean copyEnds(boolean[] line1, boolean[] line2, int[] seg1to2, int[] seg2to1, int start1, int end1, int start2, int end2){
		int index1, index2;  
		boolean copy=false;
		for(int i=start1; i<end1;i++){
			if(line1[i]){
				index1=i-start1; index2=seg1to2[index1]; 
				if(index2==-1){
//					System.out.println("something wrong!  seg1 array   "+i);
				}
				if(!line2[start2+index2]){
					line2[start2+index2]=true; copy=true;
//					if(chr2.charAt(start2+index2)=='N'){
//						System.out.println("wrong start2+index2! ");
//					}
				}				
			}
		}
		for(int i=start2; i<end2;i++){
			if(line2[i]){
				index2=i-start2; index1=seg2to1[index2]; 
				if(index1==-1){
//					System.out.println("something wrong!  seg2 array  "+i);
				}
				if(!line1[start1+index1]){
					line1[start1+index1]=true; copy=true;
//					if(chr1.charAt(start1+index1)=='N'){
//						System.out.println("wrong start1+index1! "+i+"  "+index2+"  "+index1);
//					}
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
//			ArrayList<String> chromsN=new ArrayList<String>();
			Scanner inChr = new Scanner(new File(args[0])); String temp; int chrNum=0; //"genome.masked_all.fasta"
//			Scanner inChrN = new Scanner(new File("genome.txt"));
			while(inChr.hasNextLine()){
//				inChrN.nextLine(); chromsN.add(inChrN.nextLine());
				inChr.nextLine(); temp=inChr.nextLine();
				chroms.add(temp);
				boolean[] ends=new boolean[temp.length()];
				for(int j=0;j<ends.length;j++){
					ends[j]=false;
				}
				lines.add(ends); chrNum++;
			}
//			System.out.println("end reading chrs! "+chrNum);
			
			Scanner in = new Scanner(new File(args[1]));  String[] onepair;  //BG_SDIndexes.fasta
			int chr, start, end, otherChr, otherStart, otherEnd; 
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+");
				chr=Integer.parseInt(onepair[0]); start=Integer.parseInt(onepair[1]); end=Integer.parseInt(onepair[2])-1;
				while(start<end&&chroms.get(chr).charAt(start)=='N'){
					start++;
				}
				while(start<end&&chroms.get(chr).charAt(end)=='N'){
					end--;
				}
//				if(start>=end){
//					System.out.println("endpoint wrong!");
//				}
				lines.get(chr)[start]=true;
				lines.get(chr)[end]=true;
			}
			in.close();
//			System.out.println("end reading");
			
//			BufferedWriter writerTest = new BufferedWriter(new FileWriter(new File("EndpointsTest.txt")));
			boolean copy=true; boolean tempCopy; int num=0;
			String cigar, cigarS, cigarE, seg1, seg2, segS1="", segS2="", segE1="", segE2=""; int index; int extendS1, extendS2, extendE1, extendE2; 
			String[] alignment=new String[2]; String[] alignS=new String[2]; String[] alignE=new String[2];
			while(copy){
				copy=false; num++;
				in=new Scanner(new File(args[2])); in.nextLine();  //"SCN_LastzResult_500NonCR_NewExtendPec50.txt"
				index=0;
				while(in.hasNextLine()){
					onepair=in.nextLine().trim().split("[\\p{Space}]+"); index++;
					cigar=onepair[1]; cigarS=onepair[20]; cigarE=onepair[21];
					chr=Integer.parseInt(onepair[4]); start=Integer.parseInt(onepair[5]); end=Integer.parseInt(onepair[6]);
					otherChr=Integer.parseInt(onepair[7]); otherStart=Integer.parseInt(onepair[8]); otherEnd=Integer.parseInt(onepair[9]);
					extendS1=Integer.parseInt(onepair[16]); extendE1=Integer.parseInt(onepair[17]); 
					extendS2=Integer.parseInt(onepair[18]); extendE2=Integer.parseInt(onepair[19]);
					
					if(onepair[14].equalsIgnoreCase("+")){
						seg1=chroms.get(chr).substring(start, end);	
						if(!cigarS.equalsIgnoreCase("-1")){
							segS1=chroms.get(chr).substring(extendS1, start);
							start=extendS1;
						}
						if(!cigarE.equalsIgnoreCase("-1")){
							segE1=chroms.get(chr).substring(end, extendE1);
							end=extendE1;
						}
					}
					else{
						seg1=reverse(chroms.get(chr).substring(start, end));
						if(!cigarS.equalsIgnoreCase("-1")){
							segS1=reverse(chroms.get(chr).substring(end, extendE1));
							end=extendE1;
						}
						if(!cigarE.equalsIgnoreCase("-1")){
							segE1=reverse(chroms.get(chr).substring(extendS1, start));
							start=extendS1;
						}
					}
					if(onepair[15].equalsIgnoreCase("+")){
						seg2=chroms.get(otherChr).substring(otherStart, otherEnd);
						if(!cigarS.equalsIgnoreCase("-1")){
							segS2=chroms.get(otherChr).substring(extendS2, otherStart);
							otherStart=extendS2;
						}
						if(!cigarE.equalsIgnoreCase("-1")){
							segE2=chroms.get(otherChr).substring(otherEnd, extendE2);
							otherEnd=extendE2;
						}
					}
					else{
						seg2=reverse(chroms.get(otherChr).substring(otherStart, otherEnd));
						if(!cigarS.equalsIgnoreCase("-1")){
							segS2=reverse(chroms.get(otherChr).substring(otherEnd, extendE2));
							otherEnd=extendE2;
						}
						if(!cigarE.equalsIgnoreCase("-1")){
							segE2=reverse(chroms.get(otherChr).substring(extendS2, otherStart));
							otherStart=extendS2;
						}
					}
					
					alignment=cigarAlignment(seg1,seg2,cigar);
					if(!cigarS.equalsIgnoreCase("-1")){
						alignS=endsCigarAlignment(segS1,segS2,cigarS);
						alignment[0]=alignS[0]+alignment[0];
						alignment[1]=alignS[1]+alignment[1];
						seg1=segS1+seg1;  seg2=segS2+seg2;
					}
					if(!cigarE.equalsIgnoreCase("-1")){
						alignE=endsCigarAlignment(segE1,segE2,cigarE);
						alignment[0]=alignment[0]+alignE[0];
						alignment[1]=alignment[1]+alignE[1];
						seg1=seg1+segE1;  seg2=seg2+segE2;
					}
					
					
					int[] seg1to2=new int[seg1.length()]; int[] seg2to1=new int[seg2.length()];
					for(int i=0;i<seg1.length();i++){
						seg1to2[i]=-1;
					}
					for(int i=0;i<seg2.length();i++){
						seg2to1[i]=-1;
					}
					//test
				/*	for(int i=0;i<alignment[0].length();i=i+50){
						if(i+50<alignment[0].length()){
							writerTest.write(alignment[0].substring(i, i+50)); writerTest.newLine();
							writerTest.write(alignment[1].substring(i, i+50)); writerTest.newLine(); writerTest.newLine();
						}
						else{
							writerTest.write(alignment[0].substring(i)); writerTest.newLine();
							writerTest.write(alignment[1].substring(i)); writerTest.newLine(); writerTest.newLine();
						}	
					}
					writerTest.newLine(); writerTest.newLine();*/
					
//					System.out.println(index+"  "+extendS1+"  "+extendE1+"  "+seg1.length()+"  "+seg2.length());
//					System.out.println(seg1); System.out.println();
//					System.out.println(seg2); System.out.println();
					
					computeIndex(alignment, seg1to2, seg2to1, seg1, seg2, onepair[14], onepair[15]);
					
				/*	for(int i=0;i<seg2to1.length;i=i+20){
						for(int j=i;j<i+20&&j<seg2to1.length;j++){
							writerTest.write(seg2to1[j]+" ");
						}
						writerTest.newLine();
					}*/
					
					tempCopy=copyEnds(lines.get(chr), lines.get(otherChr), seg1to2, seg2to1, start, end, otherStart, otherEnd);
					if(!copy&&tempCopy){
						copy=true;
					}
//					if(index%10000==0){
//						System.out.println(num+"  "+index);
//					}
				}
				in.close();
			}			
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[3]))); //	"EndpointsLine.txt"
			for(int n=0;n<lines.size();n++){
				writer.write(">"+n); writer.newLine();
				for(int i=0;i<lines.get(n).length;i++){
					if(lines.get(n)[i]){
						writer.write("1");
//						if(chroms.get(n).charAt(i)=='N'){
//							System.out.println("N in ends: "+n+"  "+i);
//						}
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
