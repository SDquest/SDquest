
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class NewElementSDsPair {

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
	
	public static boolean exist(String clusters2, String chr1, int[] seg1to2, int start, int end, int start1, int start2){
		boolean exist=true; int num=0;
		int index1=start; int index2=end;
		while(index1!=index2){
			index1++;
			while(chr1.charAt(index1)=='N'){
				index1++;
			}
			if(index1!=index2){
				index2--;
				while(chr1.charAt(index2)=='N'){
					index2--;
				}
			}
		}
		if(clusters2.charAt(start2+seg1to2[index1-start1])=='1'){
			return false;
		}
		else{
			return true;
		}
	}
	
	public static void main(String args[]){
		try{
			long startTime=System.currentTimeMillis();
			ArrayList<String> chroms=new ArrayList<String>(); ArrayList<String> clusters=new ArrayList<String>();
			Scanner inChr = new Scanner(new File("genome.masked_all.fasta"));
			Scanner inCluster = new Scanner(new File("ClustersLine.txt"));
			
			while(inChr.hasNextLine()){
				inChr.nextLine();
				chroms.add(inChr.nextLine()); 
				inCluster.nextLine();
				clusters.add(inCluster.nextLine()); 
			}
//			System.out.println("end reading "+clusters.size());
			inChr.close(); inCluster.close();
			
			ArrayList<ArrayList<int[]>> pairs=new ArrayList<ArrayList<int[]>>(); 
			for(int i=0;i<chroms.size();i++){
				ArrayList<int[]> temp=new ArrayList<int[]>(); pairs.add(temp);
			}
			Scanner in = new Scanner(new File("ElementSDs_100.fasta")); in.nextLine();
			String[] onepair; int index=1;
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+");
				int[] temp={Integer.parseInt(onepair[5]), Integer.parseInt(onepair[6]), index};//note to revise
				pairs.get(Integer.parseInt(onepair[1])).add(temp); index++;
				
			}
			in.close();
//			System.out.println(index);
	
			Scanner inL = new Scanner(new File("length.fasta"));
			in=new Scanner(new File("SCN_LastzResult_500NonCR_FilterPair.txt")); in.nextLine();
			BufferedWriter writerE = new BufferedWriter(new FileWriter(new File("ElementSDs_pairwiseEqual.fasta")));	
			BufferedWriter writerU = new BufferedWriter(new FileWriter(new File("ElementSDs_pairwiseUnequal.fasta")));	
			BufferedWriter writerLE = new BufferedWriter(new FileWriter(new File("ElementSDs_pairwiseLengthEqual.fasta")));
			BufferedWriter writerLU = new BufferedWriter(new FileWriter(new File("ElementSDs_pairwiseLengthUequal.fasta")));
			BufferedWriter writerDistri = new BufferedWriter(new FileWriter(new File("ElementSDs_pairwiseDistri.fasta")));	
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("ElementSDs_pairwiseSpecial_9.fasta")));	
			BufferedWriter writerDif = new BufferedWriter(new FileWriter(new File("ElementSDs_pairwiseDifference.fasta")));	
			
			int[][] distri=new int[12][12];
			for(int i=0;i<12;i++){
				for(int j=0;j<12;j++){
					distri[i][j]=0;
				}
			}
			
			int chr, start, end, otherChr, otherStart, otherEnd; int good=0, bad=0, wrong=0; index=-1; int index1,index2; String length; 
			ArrayList<Integer> num1=new ArrayList<Integer>();  ArrayList<Integer> num2=new ArrayList<Integer>(); 
			ArrayList<Integer> origin1=new ArrayList<Integer>(); ArrayList<Integer> origin2=new ArrayList<Integer>(); 
			String[] alignment; String seg1, seg2; int miss=0; 
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+"); index++; length=inL.nextLine();
				chr=Integer.parseInt(onepair[4]); start=Integer.parseInt(onepair[5]); end=Integer.parseInt(onepair[6]);
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
				alignment=cigarAlignment(seg1,seg2,onepair[1]);
				int[] seg1to2=new int[seg1.length()];  int[] seg2to1=new int[seg2.length()];
				for(int i=0;i<seg1.length();i++){
					seg1to2[i]=-1;
				}
				for(int i=0;i<seg2.length();i++){
					seg2to1[i]=-1;
				}
				
				computeIndex(alignment, seg1to2, seg2to1, seg1, seg2, onepair[14], onepair[15]);
//				System.out.println(index+"  test 1");
				
				num1.clear(); num2.clear(); origin1.clear(); origin2.clear();
				for(int i=0;i<pairs.get(chr).size();i++){
					if(pairs.get(chr).get(i)[0]>=start&&pairs.get(chr).get(i)[1]<=end){
						origin1.add(pairs.get(chr).get(i)[2]);
						if(exist(clusters.get(otherChr), chroms.get(chr), seg1to2, pairs.get(chr).get(i)[0], pairs.get(chr).get(i)[1], start, otherStart)){
							num1.add(pairs.get(chr).get(i)[2]);
						}
					}
				}
				for(int i=0;i<pairs.get(otherChr).size();i++){
					if(pairs.get(otherChr).get(i)[0]>=otherStart&&pairs.get(otherChr).get(i)[1]<=otherEnd){
						origin2.add(pairs.get(otherChr).get(i)[2]);
						if(exist(clusters.get(chr), chroms.get(otherChr), seg2to1, pairs.get(otherChr).get(i)[0], pairs.get(otherChr).get(i)[1], otherStart, start)){
							num2.add(pairs.get(otherChr).get(i)[2]);
						}
					}
				}
				
				if(origin1.size()==0||origin2.size()==0){
					miss++;
				}
				
//				System.out.println(index+"  "+num1+"  "+num2);
				if(num1.size()==0||num2.size()==0){
					wrong++;
					if(origin1.size()>0&&origin2.size()>0){
						writer.write(index+":  ");
						if(onepair[14].equalsIgnoreCase("+")){
							for(int i=0;i<origin1.size();i++){
								writer.write(origin1.get(i)+"  ");
							}
						}
						else{
							for(int i=origin1.size()-1;i>=0;i--){
								writer.write(origin1.get(i)+"  ");
							}
						}
						writer.write("--  ");
						if(onepair[15].equalsIgnoreCase("+")){
							for(int i=0;i<origin2.size();i++){
								writer.write(origin2.get(i)+"  ");
							}
						}
						else{
							for(int i=origin2.size()-1;i>=0;i--){
								writer.write(origin2.get(i)+"  ");
							}
						}
						writer.newLine(); writer.newLine();
					}
				}
				else{
					writerDif.write(index+"  "+origin1.size()+"  "+origin2.size()+"  "+num1.size()+"  "+num2.size()); writerDif.newLine();
					if(num1.size()==num2.size()){
						good++;
						writerE.write(index+":  ");						
						if(onepair[14].equalsIgnoreCase("+")){
							for(int i=0;i<num1.size();i++){
								writerE.write(num1.get(i)+"  ");
							}
						}
						else{
							for(int i=num1.size()-1;i>=0;i--){
								writerE.write("-"+num1.get(i)+"  ");
							}
						}
						writerE.write("--  ");
						if(onepair[15].equalsIgnoreCase("+")){
							for(int i=0;i<num2.size();i++){
								writerE.write(num2.get(i)+"  ");
							}
						}
						else{
							for(int i=num2.size()-1;i>=0;i--){
								writerE.write("-"+num2.get(i)+"  ");
							}
						}
						writerE.newLine(); writerE.newLine();
						writerLE.write(length); writerLE.newLine();
					}
					else{
						bad++;
						writerU.write(index+":  ");						
						if(onepair[14].equalsIgnoreCase("+")){
							for(int i=0;i<num1.size();i++){
								writerU.write(num1.get(i)+"  ");
							}
						}
						else{
							for(int i=num1.size()-1;i>=0;i--){
								writerU.write(num1.get(i)+"  ");
							}
						}
						writerU.write("--  ");
						if(onepair[15].equalsIgnoreCase("+")){
							for(int i=0;i<num2.size();i++){
								writerU.write(num2.get(i)+"  ");
							}
						}
						else{
							for(int i=num2.size()-1;i>=0;i--){
								writerU.write(num2.get(i)+"  ");
							}
						}
						writerU.newLine(); writerU.newLine();
						writerLU.write(length); writerLU.newLine();
					}
					
					if(num1.size()<=10){
						index1=num1.size()-1;
					}
					else if(num1.size()<=20){
						index1=10;
					}
					else{
						index1=11;
					}
					
					if(num2.size()<=10){
						index2=num2.size()-1;
					}
					else if(num2.size()<=20){
						index2=10;
					}
					else{
						index2=11;
					}
					distri[index1][index2]++;
				}
				
//				System.out.println(index);
			}
			for(int i=0;i<12;i++){
				for(int j=0;j<12;j++){
					writerDistri.write(distri[i][j]+"  ");
				}
				writerDistri.newLine();
			}
			long endTime=System.currentTimeMillis();
			writerE.close();writerU.close(); in.close(); writerDistri.close(); writerLE.close(); writerLU.close(); writer.close(); writerDif.close();
//			System.out.println("end! "+index+"  "+good+"  "+bad+"  "+wrong+"  "+miss);
//			System.out.println("runTime:  "+(endTime-startTime));
			
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
}
