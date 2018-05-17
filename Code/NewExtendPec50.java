
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class NewExtendPec50 {

	public static int firstCR(String str){
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)=='N'){
				return i;
			}
		}
		return str.length();
	}
	public static int lastCR(String str){
		for(int i=str.length()-1;i>=0;i--){
			if(str.charAt(i)=='N'){
				return str.length()-1-i;
			}
		}
		return str.length();
	}
	
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
			else {
				System.out.println("something wrong!  "+str.charAt(i));
			}
		}		
		return String.valueOf(temp);
	}
	public static int startIndex(int length, String str){		
		int num=0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)!='N'){
				num++;
			}
			if(num==length+1){
				return i;
			}
		}
//		System.out.println("no non-CR sequence anymore start!"+"  "+length);
		return str.length();
	}
	
	public static int endIndex(int length, String str){		
		int num=0;
		for(int i=str.length()-1;i>=0;i--){
			if(str.charAt(i)!='N'){
				num++;
			}
			if(num==length+1){
				return str.length()-1-i;
			}
		}
//		System.out.println("no non-CR sequence anymore end!"+"  "+length);
		return str.length();
	}
	
	
	public static int[] cutStart(int length, String seg, String strand, int start, int end){
		int[] temp=new int[2];
		if(strand.equalsIgnoreCase("+")){
			temp[0]=start+startIndex(length,seg); temp[1]=end;
			
		//	System.out.println("+  "+start+"  "+temp[0]+"  "+length);//test
		}
		else{
			temp[0]=start; temp[1]=end-startIndex(length,seg);
		//	System.out.println("-");
		}		
		return temp;
	}
	
	public static int[] cutEnd(int length, String seg, String strand, int start, int end){
		int[] temp=new int[2];
		if(strand.equalsIgnoreCase("+")){
			temp[0]=start; temp[1]=end-endIndex(length,seg);
		}
		else{
			temp[0]=start+endIndex(length,seg); temp[1]=end;
		}		
		return temp;
	}
	public static int[] cutCigarStart(int index, int length, String[] alignment, String seg1, String strand1, int start1, int end1, String seg2, String strand2, int start2, int end2){
		int num=0;
		if(index==0){//segment1(0) or segment2(1)
			while(length>0){
				if(alignment[0].charAt(num)!='-'){
					length--;
				}
				num++;
			}
		}
		else{
			while(length>0){
				if(alignment[1].charAt(num)!='-'){
					length--;
				}
				num++;
			}
		}
			
		while(num<alignment[0].length()&&alignment[0].charAt(num)!=alignment[1].charAt(num)){
			num++;
		}
		int num1=0, num2=0;
		for(int i=0;i<num;i++){
			if(alignment[0].charAt(i)!='-'){
				num1++;
			}
			if(alignment[1].charAt(i)!='-'){
				num2++;
			}
		}
		alignment[0]=alignment[0].substring(num);
		alignment[1]=alignment[1].substring(num); 
		
		int[] temp1=cutStart(num1,seg1,strand1,start1,end1);
		int[] temp2=cutStart(num2,seg2,strand2,start2,end2);
		int[] newIndex=new int[4];  newIndex[0]=temp1[0]; newIndex[1]=temp1[1];  newIndex[2]=temp2[0]; newIndex[3]=temp2[1];
	//	System.out.println("test:  "+strand1+"  "+strand2+"  "+temp1[0]+"  "+temp1[1]+"  "+temp2[0]+"  "+temp2[1]);//test
		return newIndex;
	}
	public static int[] cutCigarEnd(int index, int length, String[] alignment, String seg1, String strand1, int start1, int end1, String seg2, String strand2, int start2, int end2){
		int num=alignment[0].length()-1;
		if(index==0){//segment1(0) or segment2(1)
			while(length>0){
				if(alignment[0].charAt(num)!='-'){
					length--;
				}
				num--;
			}
		}
		else{
			while(length>0){
				if(alignment[1].charAt(num)!='-'){
					length--;
				}
				num--;
			}
		}
			
		while(num>=0&&alignment[0].charAt(num)!=alignment[1].charAt(num)){
			num--;
		}
		int num1=0, num2=0;
		for(int i=num+1;i<alignment[0].length();i++){
			if(alignment[0].charAt(i)!='-'){
				num1++;
			}
			if(alignment[1].charAt(i)!='-'){
				num2++;
			}
		}
		alignment[0]=alignment[0].substring(0,num+1);
		alignment[1]=alignment[1].substring(0,num+1); 
		
		int[] temp1=cutEnd(num1,seg1,strand1,start1,end1);
		int[] temp2=cutEnd(num2,seg2,strand2,start2,end2);
		int[] newIndex=new int[4];  newIndex[0]=temp1[0]; newIndex[1]=temp1[1];  newIndex[2]=temp2[0]; newIndex[3]=temp2[1];
		return newIndex;
	}
	
	public static String[] globalAlignment(String str1, String str2, int[][][] sAndP){
        String[] align=new String[4]; int matchNum=0;
		int m=str1.length(); int n=str2.length();
//		int[][][] sAndP=new int[m+1][n+1][2];
		sAndP[0][0][0]=0; sAndP[0][0][1]=2;
		for(int j=1;j<=n;j++){
			sAndP[0][j][0]=sAndP[0][j-1][0]-1; sAndP[0][j][1]=-1;
		}
		for(int i=1;i<=m;i++){
			sAndP[i][0][0]=sAndP[i-1][0][0]-1; sAndP[i][0][1]=1;
		}
		for(int i=1; i<=m; i++){
			for(int j=1;j<=n;j++){
				if(str1.charAt(i-1)==str2.charAt(j-1)){// match weight 1
					sAndP[i][j][0]=sAndP[i-1][j-1][0]+1;
					sAndP[i][j][1]=0;
				}
				else{// mismatch punish 1
					sAndP[i][j][0]=sAndP[i-1][j-1][0]-1;
					sAndP[i][j][1]=0;
				}
				if(sAndP[i][j][0]<(sAndP[i-1][j][0]-1)){//insert punish 1
					sAndP[i][j][0]=(sAndP[i-1][j][0]-1);
					sAndP[i][j][1]=1;					
				}
				if(sAndP[i][j][0]<(sAndP[i][j-1][0]-1)){// delete punish 1
					sAndP[i][j][0]=(sAndP[i][j-1][0]-1);
					sAndP[i][j][1]=-1;
				}
			}			
		}
		
		String t1=""; String t2="";
		int i=m; int j=n;
		while(sAndP[i][j][1]!=2){
			if(sAndP[i][j][1]==0){
				i--; j--;
                t1=str1.charAt(i)+t1;
				t2=str2.charAt(j)+t2;

				if(str1.charAt(i)==str2.charAt(j)){
					matchNum++;
				}
			}
			if(sAndP[i][j][1]==-1){
				j--;
				t1="-"+t1;
				t2=str2.charAt(j)+t2;
			}
			if(sAndP[i][j][1]==1){
				i--;
				t1=str1.charAt(i)+t1;
				t2="-"+t2;
			}			
		}
//		System.out.println("end alignment! "+i+"  "+j);
		align[0]=t1; align[1]=t2;
		align[2]=String.valueOf(sAndP[m][n][0]);  align[3]=String.valueOf(matchNum);
		return align;  
	}
	
	public static double computeIdentity(int start, int length, String str1, String str2){
		double identity=-1; int match=0, tempI=0;
		if(start==0){
			while(length>0){
				if(str1.charAt(tempI)!='-'){
					length--;
					if(str1.charAt(tempI)==str2.charAt(tempI)){
						match++;
					}								
				}
				tempI++;
			}
			identity=match/tempI;
		}
		else{
			tempI=str1.length()-1;
			while(length>0){
				if(str1.charAt(tempI)!='-'){
					length--;
					if(str1.charAt(tempI)==str2.charAt(tempI)){
						match++;
					}								
				}
				tempI--;
			}
			identity=match/(str1.length()-1-tempI);
		}
		
		return identity;
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
	public static String[] computeCigar(String[] alignment){
		String[] temp=new String[5]; temp[0]="";
		int match=0; int mismatch=0; int sgap=0; int ngap=0; int num; int i=0;
		while(i<alignment[0].length()){
			if(alignment[0].charAt(i)!='-'&&alignment[1].charAt(i)!='-'){
				if(alignment[0].charAt(i)==alignment[1].charAt(i)){
					match++;
				}
				else{
					mismatch++;
				}
				num=1;i++;
				while(i<alignment[0].length()&&alignment[0].charAt(i)!='-'&&alignment[1].charAt(i)!='-'){
					if(alignment[0].charAt(i)==alignment[1].charAt(i)){
						match++;
					}
					else{
						mismatch++;
					}
					num++; i++;
				}
				temp[0]=temp[0]+(num+"M");
			}
			else if(alignment[0].charAt(i)=='-'&&alignment[1].charAt(i)!='-'){
				sgap++; ngap++; num=1;i++;
				while(i<alignment[0].length()&&alignment[0].charAt(i)=='-'&&alignment[1].charAt(i)!='-'){
					i++; sgap++; num++;
				}
				temp[0]=temp[0]+(num+"I");
			}
			else if(alignment[0].charAt(i)!='-'&&alignment[1].charAt(i)=='-'){
				sgap++; ngap++; num=1;i++;
				while(i<alignment[0].length()&&alignment[0].charAt(i)!='-'&&alignment[1].charAt(i)=='-'){
					i++; sgap++; num++;
				}
				temp[0]=temp[0]+(num+"D");
			}
//			else{
//				System.out.println("wrong in compute cigar");
//			}
		}
		temp[1]=match+""; temp[2]=mismatch+""; temp[3]=ngap+""; temp[4]=sgap+"";
		return temp;
	}
	public static String[] updateAlign(int start, ArrayList<String> strA, ArrayList<String> strB){
		String temp1="",temp2="";
		if(start==0){
			for(int i=strA.size()-1;i>=0;i--){
				temp1=temp1+strA.get(i);
				temp2=temp2+strB.get(i);
			}
		}
		else{
			for(int i=0;i<strA.size();i++){
				temp1=temp1+strA.get(i);
				temp2=temp2+strB.get(i);
			}
		}
		String[] alignment={temp1,temp2};
		return computeCigar(alignment);
	}
	public static int Nlength(String str){
		int dis=0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)=='N'||str.charAt(i)=='n'){
				dis++;
			}
		}
		return dis;
	}
	public static boolean existGapN(String str){
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)=='N'||str.charAt(i)=='n'){
				return true;
			}
		}
		return false;
	}

	public static void main (String args[]){
		try{
			long startTime=System.currentTimeMillis(); int maxN=1000000;
			ArrayList<String> segs=new ArrayList<String>(); ArrayList<String> segsCR=new ArrayList<String>(); int p=0;
			Scanner inSeg = new Scanner (new File(args[0])); //CR TR masked "genome.masked_all.fasta"
			while(inSeg.hasNextLine()) {
				inSeg.nextLine(); segs.add(inSeg.nextLine()); p++;
			}
//			System.out.println("end reading 1 "+p);
			inSeg.close(); p=0;

			inSeg = new Scanner (new File(args[1])); //CR TR not masked  "Hg38Genome.txt"
			while(inSeg.hasNextLine()) {
				inSeg.nextLine(); segsCR.add(inSeg.nextLine()); p++;
			}
			inSeg.close();
//			System.out.println("end reading 2 "+p+"  "+segsCR.get(0).length());
			
			Scanner in = new Scanner (new File(args[2]));  String temp=in.nextLine(); String[] onepair;  //"SCN_LastzResult_500NonCR_FilterPair.txt"
			String otherChr="", chr="", otherChrCR="", chrCR=""; int curChr, curOtherChr;
			int start1,end1,start2,end2,s1,e1,s2,e2; int extendS1, extendE1, extendS2, extendE2; String[] cigarS=new String[5]; String[] cigarE=new String[5]; int CR1, CR2;
			String strand1, strand2;
			double identity1,identity2; int indexE=0, indexT=0, indexTE=0, index=0; int time1=0, time2=0, time3=0; int line=0;
			
			String seg1,seg2, segCR1, segCR2; boolean start,end, change; String[] align=new String[4]; int[] newIndex=new int[4]; int extendLength=50; int[][][] sAndP=new int[51][51][2];
			ArrayList<String> align1=new ArrayList<String>(); ArrayList<String> align2=new ArrayList<String>(); ArrayList<Double> Pec=new ArrayList<Double>(); double sumPec; int extendNum;
			String finalSeg1="", finalSeg2=""; int numCR1=0, numCR2=0, finalM, finalMis, finalSgap, finalNgap; double identity; int newStart1, newEnd1, newStart2, newEnd2;
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[3])));  //"SCN_LastzResult_500NonCR_NewExtendPec50.txt"
			writer.write(temp+"  extendS1  extendE1  extendS2  extendE2  cigarS  cigarE  Identity"); writer.newLine();  int indexW=0;
//			BufferedWriter writerBG = new BufferedWriter(new FileWriter(new File(args[4])));  //"SCN_LastzResult_500NonCR_BG.txt"
//			writerBG.write(temp); writerBG.newLine(); 
			String[] alignment=new String[2]; String[] newCigar=new String[5]; int length=chr.length(), otherLength=otherChr.length(); String str1,str2;// strCR1="", strCR2=""; 
			
			while(in.hasNextLine()){
				temp=in.nextLine(); index++;
				onepair=temp.trim().split("[\\p{Space}]+"); line++;
//				System.out.println(onepair[0]+"  "+line);
				if(Integer.parseInt(onepair[4])!=Integer.parseInt(onepair[7])||(Integer.parseInt(onepair[5])>Integer.parseInt(onepair[9]))||(Integer.parseInt(onepair[8])>Integer.parseInt(onepair[6]))){
					curChr=Integer.parseInt(onepair[4]); curOtherChr=Integer.parseInt(onepair[7]);
//					System.out.println(curChr+"   "+curOtherChr);

					chr=segs.get(curChr); chrCR=segsCR.get(curChr); length=chr.length();
					start1=Integer.parseInt(onepair[5]); end1=Integer.parseInt(onepair[6]);
					start2=Integer.parseInt(onepair[8]); end2=Integer.parseInt(onepair[9]);
					strand1=onepair[14]; strand2=onepair[15];
					if(strand1.equalsIgnoreCase("+")){
						seg1=chr.substring(start1, end1);  segCR1=chrCR.substring(start1, end1).toUpperCase();
					}
					else{
						seg1=reverse(chr.substring(start1, end1));  segCR1=reverse(chrCR.substring(start1, end1).toUpperCase());
					}
//					System.out.println("end seg1  "+length);
					
					otherChr=segs.get(curOtherChr); otherChrCR=segsCR.get(curOtherChr); otherLength=otherChr.length();
					if(strand2.equalsIgnoreCase("+")){
						seg2=otherChr.substring(start2, end2); 	segCR2=otherChrCR.substring(start2, end2).toUpperCase();				
					}
					else{
						seg2=reverse(otherChr.substring(start2, end2));  segCR2=reverse(otherChrCR.substring(start2, end2).toUpperCase());					
					}
//					System.out.println("test 0  "+line);//test
					alignment=cigarAlignment(seg1,seg2,onepair[1]);
//					System.out.println("test 1  "+line);//test
					
					cigarS[0]="-1"; cigarS[1]="-1"; cigarS[2]="-1"; cigarS[3]="-1"; cigarS[4]="-1";
					cigarE[0]="-1"; cigarE[1]="-1"; cigarE[2]="-1"; cigarE[3]="-1"; cigarE[4]="-1";
					s1=start1; e1=end1; s2=start2; e2=end2; extendS1=-1; extendE1=-1; extendS2=-1; extendE2=-1;//back up
					align1.clear();align2.clear(); Pec.clear(); 
					start=true; change=true; time1=0; //extend1=false;
					while(start){
						start=false; str1=""; str2=""; 
						if(strand1.equalsIgnoreCase("+")){//get str1
							if((start1-extendLength)>=0){
								str1=chrCR.substring(start1-extendLength, start1).toUpperCase(); //strCR1=chrCR.substring(start1-extendLength, start1);
								if(existGapN(str1)){
									change=false;
								}
							}
							else{
								change=false;
							}
						}
						else{
							if((end1+extendLength)<=length){
								str1=reverse(chrCR.substring(end1, end1+extendLength).toUpperCase()); // strCR1=reverse(chrCR.substring(end1, end1+extendLength));
								if(existGapN(str1)){
									change=false;
								}
							}
							else{
								change=false;
							}
						}
						
						if(change){
							if(strand2.equalsIgnoreCase("+")){
								if((start2-extendLength)>=0){
									str2=otherChrCR.substring(start2-extendLength, start2).toUpperCase(); // strCR2=otherChrCR.substring(start2-extendLength, start2);
									if(existGapN(str2)){
										change=false;
									}
								}
								else{
									change=false;
								}
							}
							else{
								if((end2+extendLength)<otherLength){
									str2=reverse(otherChrCR.substring(end2, end2+extendLength).toUpperCase()); // strCR2=reverse(otherChrCR.substring(end2, end2+extendLength));
									if(existGapN(str2)){
										change=false;
									}
								}
								else{
									change=false;
								}
							}
						}
						
						if(change){
							align=globalAlignment(str1,str2,sAndP);
							if(Double.parseDouble(align[3])/align[0].length()>0.5){
								start=true; //extend1=true;
								if(strand1.equalsIgnoreCase("+")){
									start1=start1-extendLength;
								}
								else{
									end1=end1+extendLength;
								}
								if(strand2.equalsIgnoreCase("+")){
									start2=start2-extendLength;
								}
								else{
									end2=end2+extendLength;
								}
								
								if(curChr!=curOtherChr||start1>end2||start2>end1){
									align1.add(align[0]); align2.add(align[1]); Pec.add(Double.parseDouble(align[3])/align[0].length()); 
									//alignment=updateAlign(0,alignment[0],align[0],alignment[1],align[1]);//alignment[0]=align[0]+alignment[0]; alignment[1]=align[1]+alignment[1]; time1++;
								}
								else{
									start=false;
								}
							}
						}					
					}
					if(align1.size()>0){
						for(int i=align1.size()-1;i>=0&&(time1==0);i--){
							if(Pec.get(i)>=0.7){
								sumPec=0;
								for(int j=0;j<=i;j++){
									sumPec=sumPec+Pec.get(j);
								}
								if(sumPec/(i+1)>=0.7){
									time1=i+1;
								}
							}
						}
					}
					start1=s1; end1=e1; start2=s2; end2=e2;
//					System.out.println("test extend start times:  "+time1);//test

					if(time1==0){//start can't extend
//						start1=s1; end1=e1; start2=s2; end2=e2;
						change=true; CR1=firstCR(seg1); CR2=firstCR(seg2);
						while((CR1<extendLength||CR2<extendLength)&&change){
							if(seg1.length()>extendLength&&seg2.length()>extendLength){
								align=globalAlignment(segCR1.substring(0, extendLength),segCR2.substring(0, extendLength),sAndP);
								
								if(Double.parseDouble(align[3])/align[0].length()<0.7){// if the similarity is low, thus need to cut the start
									if(CR1<extendLength&&CR2<extendLength){
										if(CR1<CR2){
											newIndex=cutCigarStart(1,CR2,alignment,seg1,strand1,start1,end1,seg2,strand2,start2,end2);										
										}
										else{
											newIndex=cutCigarStart(0,CR1,alignment,seg1,strand1,start1,end1,seg2,strand2,start2,end2);
										}
									}
									else if(CR1<extendLength){
										newIndex=cutCigarStart(0,CR1,alignment,seg1,strand1,start1,end1,seg2,strand2,start2,end2);
									}
									else{
										newIndex=cutCigarStart(1,CR2,alignment,seg1,strand1,start1,end1,seg2,strand2,start2,end2);		
									}
									
									start1=newIndex[0]; end1=newIndex[1]; start2=newIndex[2]; end2=newIndex[3];
									if(strand1.equalsIgnoreCase("+")){
										seg1=chr.substring(start1, end1);  segCR1=chrCR.substring(start1, end1).toUpperCase();
									}
									else{
										seg1=reverse(chr.substring(start1, end1));  segCR1=reverse(chrCR.substring(start1, end1).toUpperCase());
									}
									if(strand2.equalsIgnoreCase("+")){
										seg2=otherChr.substring(start2, end2); 	segCR2=otherChrCR.substring(start2, end2).toUpperCase();				
									}
									else{
										seg2=reverse(otherChr.substring(start2, end2));   	segCR2=reverse(otherChrCR.substring(start2, end2).toUpperCase());					
									}
									CR1=firstCR(seg1); CR2=firstCR(seg2); time1++;
								}
								else{
									change=false;
								}							
							}
							else{
								change=false;
							}
						}
						if(time1>0){//if start be cutted
							
							//extend start after trim
							s1=start1; e1=end1; s2=start2; e2=end2;
							align1.clear();align2.clear(); Pec.clear(); 
							start=true; change=true; time3=0;
							while(start){
								start=false; str1=""; str2=""; 
								if(strand1.equalsIgnoreCase("+")){//get str1
									if((start1-extendLength)>=0){
										str1=chrCR.substring(start1-extendLength, start1).toUpperCase(); //strCR1=chrCR.substring(start1-extendLength, start1);
										if(existGapN(str1)){
											change=false;
										}
									}
									else{
										change=false;
									}
								}
								else{
									if((end1+extendLength)<=length){
										str1=reverse(chrCR.substring(end1, end1+extendLength).toUpperCase()); // strCR1=reverse(chrCR.substring(end1, end1+extendLength));
										if(existGapN(str1)){
											change=false;
										}
									}
									else{
										change=false;
									}
								}
								
								if(strand2.equalsIgnoreCase("+")){
									if((start2-extendLength)>=0){
										str2=otherChrCR.substring(start2-extendLength, start2).toUpperCase(); // strCR2=otherChrCR.substring(start2-extendLength, start2);
										if(existGapN(str2)){
											change=false;
										}
									}
									else{
										change=false;
									}
								}
								else{
									if((end2+extendLength)<otherLength){
										str2=reverse(otherChrCR.substring(end2, end2+extendLength).toUpperCase());  //strCR2=reverse(otherChrCR.substring(end2, end2+extendLength));
										if(existGapN(str2)){
											change=false;
										}
									}
									else{
										change=false;
									}
								}
								
								if(change){
									align=globalAlignment(str1,str2,sAndP);
									if(Double.parseDouble(align[3])/align[0].length()>0.5){
										start=true; //extend1=true;
										if(strand1.equalsIgnoreCase("+")){
											start1=start1-extendLength;
										}
										else{
											end1=end1+extendLength;
										}
										if(strand2.equalsIgnoreCase("+")){
											start2=start2-extendLength;
										}
										else{
											end2=end2+extendLength;
										}
										
										if(curChr!=curOtherChr||start1>end2||start2>end1){
											align1.add(align[0]); align2.add(align[1]); Pec.add(Double.parseDouble(align[3])/align[0].length()); 
											//alignment=updateAlign(0,alignment[0],align[0],alignment[1],align[1]);//alignment[0]=align[0]+alignment[0]; alignment[1]=align[1]+alignment[1]; time1++;
										}
										else{
											start=false;
										}
										
									}
								}					
							}
							if(align1.size()>0){
								for(int i=align1.size()-1;i>=0&&(time3==0);i--){
									if(Pec.get(i)>=0.7){
										sumPec=0;
										for(int j=0;j<=i;j++){
											sumPec=sumPec+Pec.get(j);
										}
										if(sumPec/(i+1)>=0.7){
											time3=i+1;
										}
									}
								}
							}
							
							start1=s1; end1=e1; start2=s2; end2=e2;
							if(time3>0){
								while(align1.size()>time3){
									align1.remove(align1.size()-1); align2.remove(align2.size()-1); 
								}
								if(strand1.equalsIgnoreCase("+")){
									extendS1=s1-extendLength*time3;
								}
								else{
									extendE1=e1+extendLength*time3;
								}
								if(strand2.equalsIgnoreCase("+")){
									extendS2=s2-extendLength*time3;
								}
								else{
									extendE2=e2+extendLength*time3;
								}
								cigarS=updateAlign(0,align1,align2);
							}
						}
					}
					else{//extend start
						
						while(align1.size()>time1){
							align1.remove(align1.size()-1); align2.remove(align2.size()-1); 
						}
						if(strand1.equalsIgnoreCase("+")){
							extendS1=s1-extendLength*time1;
						}
						else{
							extendE1=e1+extendLength*time1;
						}
						if(strand2.equalsIgnoreCase("+")){
							extendS2=s2-extendLength*time1;
						}
						else{
							extendE2=e2+extendLength*time1;
						}
						cigarS=updateAlign(0,align1,align2);
					}
//					System.out.println("end start extend and trim  "+time1+"  "+time3);//test
					
					//extend end
					s1=start1; e1=end1; s2=start2; e2=end2; 
					align1.clear(); align2.clear(); Pec.clear();
					end=true; change=true; time2=0; //extend2=false;
					if(extendS1!=-1){
						start1=extendS1;
					}
					if(extendS2!=-1){
						start2=extendS2;
					}
					if(extendE1!=-1){
						end1=extendE1;
					}
					if(extendE2!=-1){
						end2=extendE2;
					}
					
					while(end){
						end=false; str1=""; str2="";  
						if(strand1.equalsIgnoreCase("+")){//get str1
							if((end1+extendLength)<=length){
								str1=chrCR.substring(end1, end1+extendLength).toUpperCase(); // strCR1=chrCR.substring(end1, end1+extendLength);
								if(existGapN(str1)){
									change=false;
								}
							}
							else{
								change=false;
							}
						}
						else{
							if((start1-extendLength)>=0 ){
								str1=reverse(chrCR.substring(start1-extendLength, start1).toUpperCase()); // strCR1=reverse(chrCR.substring(start1-extendLength, start1));
								if(existGapN(str1)){
									change=false;
								}
							}
							else{
								change=false;
							}
						}
						
						if(strand2.equalsIgnoreCase("+")){
							if((end2+extendLength)<otherLength){
								str2=otherChrCR.substring(end2, end2+extendLength).toUpperCase(); // strCR2=otherChrCR.substring(end2, end2+extendLength);
								if(existGapN(str2)){
									change=false;
								}
							}
							else{
								change=false;
							}
						}
						else{
							if((start2-extendLength)>=0){
								str2=reverse(otherChrCR.substring(start2-extendLength, start2).toUpperCase()); // strCR2=reverse(otherChrCR.substring(start2-extendLength, start2));
								if(existGapN(str2)){
									change=false;
								}
							}
							else{
								change=false;
							}
						}
						
						if(change){
							align=globalAlignment(str1,str2,sAndP);
							if(Double.parseDouble(align[3])/align[0].length()>0.5){
								end=true;  //extend2=true;
								if(strand1.equalsIgnoreCase("+")){
									end1=end1+extendLength;
								}
								else{
									start1=start1-extendLength;
								}
								if(strand2.equalsIgnoreCase("+")){
									end2=end2+extendLength;
								}
								else{
									start2=start2-extendLength;
								}
								if(curChr!=curOtherChr||start1>end2||start2>end1){
									align1.add(align[0]); align2.add(align[1]); Pec.add(Double.parseDouble(align[3])/align[0].length()); 
									//alignment=updateAlign(1,alignment[0],align[0],alignment[1],align[1]); time1++;
								}
								else{
									end=false;
								}															
							}
						}	
					}
					
					//System.out.println(align1.size());//test	
					if(align1.size()>0){
						for(int i=align1.size()-1;i>=0&&(time2==0);i--){
							if(Pec.get(i)>=0.7){
								sumPec=0;
								for(int j=0;j<=i;j++){
									sumPec=sumPec+Pec.get(j);
								}
								if(sumPec/(i+1)>=0.7){
									time2=i+1;
								}
							}
						}
					}
					start1=s1; end1=e1; start2=s2; end2=e2;
//					System.out.println("test extend end  "+time2);//test
					
					if(time2==0){
						CR1=lastCR(seg1);  CR2=lastCR(seg2); change=true; 
						while((CR1<extendLength||CR2<extendLength)&&change){
							if(seg1.length()>extendLength&&seg2.length()>extendLength){
								align=globalAlignment(segCR1.substring(segCR1.length()-extendLength),segCR2.substring(segCR2.length()-extendLength),sAndP);
								
								if(Double.parseDouble(align[3])/align[0].length()<0.7){//if the end similarity is low, thus need to be cutted
									if(CR1<extendLength&&CR2<extendLength){
										if(CR1<CR2){
											newIndex=cutCigarEnd(1,CR2,alignment,seg1,strand1,start1,end1,seg2,strand2,start2,end2);
										}
										else{
											newIndex=cutCigarEnd(0,CR1,alignment,seg1,strand1,start1,end1,seg2,strand2,start2,end2);
										}
									}
									else if(CR1<extendLength){
										newIndex=cutCigarEnd(0,CR1,alignment,seg1,strand1,start1,end1,seg2,strand2,start2,end2);
									}
									else{
										newIndex=cutCigarEnd(1,CR2,alignment,seg1,strand1,start1,end1,seg2,strand2,start2,end2);
									}
									
									start1=newIndex[0]; end1=newIndex[1]; start2=newIndex[2]; end2=newIndex[3];
									if(strand1.equalsIgnoreCase("+")){
										seg1=chr.substring(start1, end1);  segCR1=chrCR.substring(start1, end1).toUpperCase();
									}
									else{
										seg1=reverse(chr.substring(start1, end1));  segCR1=reverse(chrCR.substring(start1, end1).toUpperCase());
									}
									if(strand2.equalsIgnoreCase("+")){
										seg2=otherChr.substring(start2, end2); 	segCR2=otherChrCR.substring(start2, end2).toUpperCase();				
									}
									else{
										seg2=reverse(otherChr.substring(start2, end2));   	segCR2=reverse(otherChrCR.substring(start2, end2).toUpperCase());					
									}
									CR1=lastCR(seg1); CR2=lastCR(seg2); time2++;
								}
								else{
									change=false;
								}
							}
							else{
								change=false;
							}
						}
						if(time2>0){
							//extend end after trim
							s1=start1; e1=end1; s2=start2; e2=end2; 
							align1.clear(); align2.clear(); Pec.clear();
							end=true; change=true; time3=0; 
							if(extendS1!=-1){
								start1=extendS1;
							}
							if(extendS2!=-1){
								start2=extendS2;
							}
							if(extendE1!=-1){
								end1=extendE1;
							}
							if(extendE2!=-1){
								end2=extendE2;
							}
							
							while(end){
								end=false; str1=""; str2="";  
								if(strand1.equalsIgnoreCase("+")){//get str1
									if((end1+extendLength)<=length){
										str1=chrCR.substring(end1, end1+extendLength).toUpperCase(); // strCR1=chrCR.substring(end1, end1+extendLength);
										if(existGapN(str1)){
											change=false;
										}
									}
									else{
										change=false;
									}
								}
								else{
									if((start1-extendLength)>=0 ){
										str1=reverse(chrCR.substring(start1-extendLength, start1).toUpperCase()); // strCR1=reverse(chrCR.substring(start1-extendLength, start1));
										if(existGapN(str1)){
											change=false;
										}
									}
									else{
										change=false;
									}
								}
								
								if(strand2.equalsIgnoreCase("+")){
									if((end2+extendLength)<otherLength){
										str2=otherChrCR.substring(end2, end2+extendLength).toUpperCase(); // strCR2=otherChrCR.substring(end2, end2+extendLength);
										if(existGapN(str2)){
											change=false;
										}
									}
									else{
										change=false;
									}
								}
								else{
									if((start2-extendLength)>=0){
										str2=reverse(otherChrCR.substring(start2-extendLength, start2).toUpperCase()); // strCR2=reverse(otherChrCR.substring(start2-extendLength, start2));
										if(existGapN(str2)){
											change=false;
										}
									}
									else{
										change=false;
									}
								}
								
								if(change){
									align=globalAlignment(str1,str2,sAndP);
									if(Double.parseDouble(align[3])/align[0].length()>0.5){
										end=true;  //extend2=true;
										if(strand1.equalsIgnoreCase("+")){
											end1=end1+extendLength;
										}
										else{
											start1=start1-extendLength;
										}
										if(strand2.equalsIgnoreCase("+")){
											end2=end2+extendLength;
										}
										else{
											start2=start2-extendLength;
										}
										
										if(curChr!=curOtherChr||start1>end2||start2>end1){
											align1.add(align[0]); align2.add(align[1]); Pec.add(Double.parseDouble(align[3])/align[0].length()); 
											//alignment=updateAlign(1,alignment[0],align[0],alignment[1],align[1]); time1++;
											
										}
										else{
											end=false;
										}
									}
								}	
							}
							if(align1.size()>0){
								for(int i=align1.size()-1;i>=0&&(time3==0);i--){
									if(Pec.get(i)>=0.7){
										sumPec=0;
										for(int j=0;j<=i;j++){
											sumPec=sumPec+Pec.get(j);
										}
										if(sumPec/(i+1)>=0.7){
											time3=i+1;
										}
									}
								}
							}
							start1=s1; end1=e1; start2=s2; end2=e2;
							
							if(time3>0){
								while(align1.size()>time3){
									align1.remove(align1.size()-1); align2.remove(align2.size()-1); 
								}
								if(strand1.equalsIgnoreCase("+")){
									extendE1=e1+extendLength*time3;
								}
								else{
									extendS1=s1-extendLength*time3;
								}
								if(strand2.equalsIgnoreCase("+")){
									extendE2=e2+extendLength*time3;
								}
								else{
									extendS2=s2-extendLength*time3;
								}
								cigarE=updateAlign(1,align1,align2);
							}
						}
					}
					else{
						while(align1.size()>time2){
							align1.remove(align1.size()-1); align2.remove(align2.size()-1); 
						}
						if(strand1.equalsIgnoreCase("+")){
							extendE1=e1+extendLength*time2;
						}
						else{
							extendS1=s1-extendLength*time2;
						}
						if(strand2.equalsIgnoreCase("+")){
							extendE2=e2+extendLength*time2;
						}
						else{
							extendS2=s2-extendLength*time2;
						}
						cigarE=updateAlign(1,align1,align2);
					}
					
//					System.out.println("end extend and trim end  "+time2+"  "+time3); //test
					newCigar=computeCigar(alignment);
					if(extendS1==-1&&extendE1==-1){
						finalSeg1=segs.get(curChr).substring(start1, end1);
						finalM=Integer.parseInt(newCigar[1]);
						finalMis=Integer.parseInt(newCigar[2]);
						finalNgap=Integer.parseInt(newCigar[3]);
						finalSgap=Integer.parseInt(newCigar[4]);
					}
					else if(extendS1==-1&&extendE1!=-1){
						finalSeg1=segs.get(curChr).substring(start1, extendE1);
						finalM=Integer.parseInt(newCigar[1])+Integer.parseInt(cigarE[1]);
						finalMis=Integer.parseInt(newCigar[2])+Integer.parseInt(cigarE[2]);
						finalNgap=Integer.parseInt(newCigar[3])+Integer.parseInt(cigarE[3]);
						finalSgap=Integer.parseInt(newCigar[4])+Integer.parseInt(cigarE[4]);
					}
					else if(extendS1!=-1&&extendE1==-1){
						finalSeg1=segs.get(curChr).substring(extendS1, end1);
						finalM=Integer.parseInt(newCigar[1])+Integer.parseInt(cigarS[1]);
						finalMis=Integer.parseInt(newCigar[2])+Integer.parseInt(cigarS[2]);
						finalNgap=Integer.parseInt(newCigar[3])+Integer.parseInt(cigarS[3]);
						finalSgap=Integer.parseInt(newCigar[4])+Integer.parseInt(cigarS[4]);
					}
					else{
						finalSeg1=segs.get(curChr).substring(extendS1, extendE1);
						finalM=Integer.parseInt(newCigar[1])+Integer.parseInt(cigarS[1])+Integer.parseInt(cigarE[1]);
						finalMis=Integer.parseInt(newCigar[2])+Integer.parseInt(cigarS[2])+Integer.parseInt(cigarE[2]);
						finalNgap=Integer.parseInt(newCigar[3])+Integer.parseInt(cigarS[3])+Integer.parseInt(cigarE[3]);
						finalSgap=Integer.parseInt(newCigar[4])+Integer.parseInt(cigarS[4])+Integer.parseInt(cigarE[4]);
					}
					identity=((double)finalM)/(finalM+finalMis+finalSgap);
					
					if(extendS2==-1&&extendE2==-1){
						finalSeg2=segs.get(curOtherChr).substring(start2, end2);
					}
					else if(extendS2==-1&&extendE2!=-1){
						finalSeg2=segs.get(curOtherChr).substring(start2, extendE2);
					}
					else if(extendS2!=-1&&extendE2==-1){
						finalSeg2=segs.get(curOtherChr).substring(extendS2, end2);
					}
					else{
						finalSeg2=segs.get(curOtherChr).substring(extendS2, extendE2);
					}
					numCR1=Nlength(finalSeg1); numCR2=Nlength(finalSeg2);
					
					if(finalSeg1.length()-numCR1>=500&&finalSeg2.length()-numCR2>=500&&identity>=0.7&&Math.abs(finalSeg1.length()-finalSeg2.length())<maxN){//
						writer.write(onepair[0]+"  "+newCigar[0]+"  "+onepair[2]+"  "+onepair[3]+"  "+onepair[4]+"  ");
						writer.write(start1+"  "+end1+"  "+onepair[7]+"  "+start2+"  "+end2+"  ");
						writer.write(finalM+"  "+finalMis+"  "+finalNgap+"  "+finalSgap+"  "+onepair[14]+"  "+onepair[15]+"  ");					
						writer.write(extendS1+"  "+extendE1+"  "+extendS2+"  "+extendE2+"  "+cigarS[0]+"  "+cigarE[0]+"  "+identity);//extendS1  extendE1  extendS2  extendE2  cigarS  cigarE
						writer.newLine(); indexW++;
					}
					
				}
				
//				if(line%10000==0) {
//					System.out.println("end  "+line+"  "+indexW);
//				}
			}
			writer.close();  in.close(); 
//			System.out.println("end!  totalNum: "+index+"   "+indexW);	
//			long endTime=System.currentTimeMillis();
//			System.out.println("runTime: "+(endTime-startTime));
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}

