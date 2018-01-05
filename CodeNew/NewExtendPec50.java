
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
	
	public static String[] globalAlignment(String str1, String str2, String strCR1, String strCR2, int[][][] sAndP){
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
                t1=strCR1.charAt(i)+t1;
				t2=strCR2.charAt(j)+t2;

				if(str1.charAt(i)==str2.charAt(j)){
					matchNum++;
				}
			}
			if(sAndP[i][j][1]==-1){
				j--;
				t1="-"+t1;
				t2=strCR2.charAt(j)+t2;
			}
			if(sAndP[i][j][1]==1){
				i--;
				t1=strCR1.charAt(i)+t1;
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
			else{
				sgap++; ngap++; num=1;i++;
				while(i<alignment[0].length()&&alignment[0].charAt(i)!='-'&&alignment[1].charAt(i)=='-'){
					i++; sgap++; num++;
				}
				temp[0]=temp[0]+(num+"D");
			}
		}
		temp[1]=match+""; temp[2]=mismatch+""; temp[3]=ngap+""; temp[4]=sgap+"";
		return temp;
	}
	public static String[] updateAlign(int start, String str1, ArrayList<String> strA, String str2, ArrayList<String> strB){
		String[] alignment=new String[2];
		alignment[0]=str1; alignment[1]=str2; String temp1="",temp2="";
		
		if(start==0){
			for(int i=strA.size()-1;i>=0;i--){
				for(int j=0;j<strA.get(i).length();j++){
					if(strA.get(i).charAt(j)=='A'||strA.get(i).charAt(j)=='T'||strA.get(i).charAt(j)=='C'||strA.get(i).charAt(j)=='G'||strA.get(i).charAt(j)=='-'){
						if(strB.get(i).charAt(j)=='A'||strB.get(i).charAt(j)=='T'||strB.get(i).charAt(j)=='C'||strB.get(i).charAt(j)=='G'||strB.get(i).charAt(j)=='-'){
							temp1=temp1+strA.get(i).charAt(j);
							temp2=temp2+strB.get(i).charAt(j);
						}
					}
				}
			}
			alignment[0]=temp1+alignment[0];
			alignment[1]=temp2+alignment[1];
		}
		else{
			for(int i=0;i<strA.size();i++){
				for(int j=0;j<strA.get(i).length();j++){
					if(strA.get(i).charAt(j)=='A'||strA.get(i).charAt(j)=='T'||strA.get(i).charAt(j)=='C'||strA.get(i).charAt(j)=='G'||strA.get(i).charAt(j)=='-'){
						if(strB.get(i).charAt(j)=='A'||strB.get(i).charAt(j)=='T'||strB.get(i).charAt(j)=='C'||strB.get(i).charAt(j)=='G'||strB.get(i).charAt(j)=='-'){
							temp1=temp1+strA.get(i).charAt(j);
							temp2=temp2+strB.get(i).charAt(j);
						}
					}
				}
			}
			alignment[0]=alignment[0]+temp1;
			alignment[1]=alignment[1]+temp2;
		}
		
		return alignment;
	}
	public static int Nlength(String str, int start, int end){
		int dis=0;
		for(int i=start;i<end;i++){
			if(str.charAt(i)=='N'){
				dis++;
			}
		}
		return dis;
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
			int start1,end1,start2,end2,s1,e1,s2,e2; int CRs1, CRe1, CRs2, CRe2; double identity1,identity2; int indexE=0, indexT=0, indexTE=0, index=0; int time1=0, time2=0, time3=0; int line=0;
			
			String seg1,seg2, segCR1, segCR2; boolean start,end, change; String[] align=new String[4]; int[] newIndex=new int[4]; int extendLength=50; int[][][] sAndP=new int[51][51][2];
			ArrayList<String> align1=new ArrayList<String>(); ArrayList<String> align2=new ArrayList<String>(); ArrayList<Double> Pec=new ArrayList<Double>(); double sumPec; int extendNum;
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[3])));  //"SCN_LastzResult_500NonCR_NewExtendPec50.txt"
			writer.write(temp+"  CRs1  CRe1  CRs2  CRe2"); writer.newLine(); 
			BufferedWriter writerBG = new BufferedWriter(new FileWriter(new File(args[4])));  //"SCN_LastzResult_500NonCR_NewExtendPec50.txt"
			writerBG.write(temp); writerBG.newLine(); 
			String[] alignment=new String[2]; String[] newCigar=new String[5]; int length=chr.length(), otherLength=otherChr.length(); String str1,str2, strCR1="", strCR2=""; 
			
			while(in.hasNextLine()){
				temp=in.nextLine(); index++;
				onepair=temp.trim().split("[\\p{Space}]+"); line++;
//				System.out.println(onepair[0]+"  "+line);
				if(Integer.parseInt(onepair[4])!=Integer.parseInt(onepair[7])||(Integer.parseInt(onepair[5])>Integer.parseInt(onepair[9]))||(Integer.parseInt(onepair[8])>Integer.parseInt(onepair[6]))){
					curChr=Integer.parseInt(onepair[4]); curOtherChr=Integer.parseInt(onepair[7]);
//					System.out.println(curChr+"   "+curOtherChr);
					
					chr=segs.get(curChr); chrCR=segsCR.get(curChr); length=chr.length();
					start1=Integer.parseInt(onepair[5]); end1=Integer.parseInt(onepair[6]);
					if(onepair[14].equalsIgnoreCase("+")){
						seg1=chr.substring(start1, end1);  segCR1=chrCR.substring(start1, end1).toUpperCase();
					}
					else{
						seg1=reverse(chr.substring(start1, end1));  segCR1=reverse(chrCR.substring(start1, end1).toUpperCase());
					}
//					System.out.println("end seg1  "+length);
					
					otherChr=segs.get(curOtherChr); otherChrCR=segsCR.get(curOtherChr); otherLength=otherChr.length();
					start2=Integer.parseInt(onepair[8]); end2=Integer.parseInt(onepair[9]);
					
//					System.out.println("end  "+otherLength+"  "+onepair[14]+"  "+onepair[15]);
					if(onepair[15].equalsIgnoreCase("+")){
//						System.out.println("if");
						seg2=otherChr.substring(start2, end2); 	segCR2=otherChrCR.substring(start2, end2).toUpperCase();				
					}
					else{
//						System.out.println("else  "+otherChr.length()+"  "+otherChrCR.length()+"  "+start2+"  "+end2);
						seg2=reverse(otherChr.substring(start2, end2));  segCR2=reverse(otherChrCR.substring(start2, end2).toUpperCase());					
					}
//					System.out.println("test 0  "+line);//test
					alignment=cigarAlignment(seg1,seg2,onepair[1]);		
					
//					System.out.println("test 1  "+line);//test
					
					s1=start1; e1=end1; s2=start2; e2=end2;
					align1.clear();align2.clear(); Pec.clear();
					start=true; change=true; time1=0; //extend1=false;
					while(start){
						start=false; str1=""; str2=""; 
						if(onepair[14].equalsIgnoreCase("+")){//get str1
							if((start1-extendLength)>=0){
								str1=chrCR.substring(start1-extendLength, start1).toUpperCase(); strCR1=chrCR.substring(start1-extendLength, start1);
							}
							else{
								change=false;
							}
						}
						else{
							if((end1+extendLength)<=length){
								str1=reverse(chrCR.substring(end1, end1+extendLength).toUpperCase());  strCR1=reverse(chrCR.substring(end1, end1+extendLength));
							}
							else{
								change=false;
							}
						}
						
						if(onepair[15].equalsIgnoreCase("+")){
							if((start2-extendLength)>=0){
								str2=otherChrCR.substring(start2-extendLength, start2).toUpperCase();  strCR2=otherChrCR.substring(start2-extendLength, start2);
							}
							else{
								change=false;
							}
						}
						else{
							if((end2+extendLength)<otherLength){
								str2=reverse(otherChrCR.substring(end2, end2+extendLength).toUpperCase());  strCR2=reverse(otherChrCR.substring(end2, end2+extendLength));
							}
							else{
								change=false;
							}
						}
						
						if(change){
							align=globalAlignment(str1,str2,strCR1,strCR2,sAndP);
							if(Double.parseDouble(align[3])/align[0].length()>0.5){
								start=true; //extend1=true;
								if(onepair[14].equalsIgnoreCase("+")){
									start1=start1-extendLength;
								}
								else{
									end1=end1+extendLength;
								}
								if(onepair[15].equalsIgnoreCase("+")){
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
					
//					System.out.println("test 2  "+line+"  "+time1);//test

					if(time1==0){
						start1=s1; end1=e1; start2=s2; end2=e2;
						CRs1=firstCR(seg1); CRs2=firstCR(seg2); change=true;
						while((CRs1<extendLength||CRs2<extendLength)&&change){
							if(seg1.length()>extendLength&&seg2.length()>extendLength){
								align=globalAlignment(segCR1.substring(0, extendLength),segCR2.substring(0, extendLength),segCR1.substring(0, extendLength),segCR2.substring(0, extendLength),sAndP);
								
								if(Double.parseDouble(align[3])/align[0].length()<0.7){// if the similarity is low, thus need to cut the start
									if(CRs1<extendLength&&CRs2<extendLength){
										if(CRs1<CRs2){
											newIndex=cutCigarStart(1,CRs2,alignment,seg1,onepair[14],start1,end1,seg2,onepair[15],start2,end2);										
										}
										else{
											newIndex=cutCigarStart(0,CRs1,alignment,seg1,onepair[14],start1,end1,seg2,onepair[15],start2,end2);
										}
									}
									else if(CRs1<extendLength){
										newIndex=cutCigarStart(0,CRs1,alignment,seg1,onepair[14],start1,end1,seg2,onepair[15],start2,end2);
									}
									else{
										newIndex=cutCigarStart(1,CRs2,alignment,seg1,onepair[14],start1,end1,seg2,onepair[15],start2,end2);		
									}
									
									start1=newIndex[0]; end1=newIndex[1]; start2=newIndex[2]; end2=newIndex[3];
									if(onepair[14].equalsIgnoreCase("+")){
										seg1=chr.substring(start1, end1);  segCR1=chrCR.substring(start1, end1).toUpperCase();
									}
									else{
										seg1=reverse(chr.substring(start1, end1));  segCR1=reverse(chrCR.substring(start1, end1).toUpperCase());
									}
									if(onepair[15].equalsIgnoreCase("+")){
										seg2=otherChr.substring(start2, end2); 	segCR2=otherChrCR.substring(start2, end2).toUpperCase();				
									}
									else{
										seg2=reverse(otherChr.substring(start2, end2));   	segCR2=reverse(otherChrCR.substring(start2, end2).toUpperCase());					
									}
									CRs1=firstCR(seg1); CRs2=firstCR(seg2); time1++;
								}
								else{
									change=false;
								}							
							}
							else{
								change=false;
							}
						}
						if(time1>0){
							indexT++;
							if(indexT%100==0){
//								System.out.println("trim start: "+indexT+"  "+time1);
							}
							//extend start after trim
							s1=start1; e1=end1; s2=start2; e2=end2;
							align1.clear();align2.clear(); Pec.clear(); 
							start=true; change=true; time3=0;
							while(start){
								start=false; str1=""; str2=""; 
								if(onepair[14].equalsIgnoreCase("+")){//get str1
									if((start1-extendLength)>=0){
										str1=chrCR.substring(start1-extendLength, start1).toUpperCase(); strCR1=chrCR.substring(start1-extendLength, start1);
									}
									else{
										change=false;
									}
								}
								else{
									if((end1+extendLength)<=length){
										str1=reverse(chrCR.substring(end1, end1+extendLength).toUpperCase());  strCR1=reverse(chrCR.substring(end1, end1+extendLength));
									}
									else{
										change=false;
									}
								}
								
								if(onepair[15].equalsIgnoreCase("+")){
									if((start2-extendLength)>=0){
										str2=otherChrCR.substring(start2-extendLength, start2).toUpperCase();  strCR2=otherChrCR.substring(start2-extendLength, start2);
									}
									else{
										change=false;
									}
								}
								else{
									if((end2+extendLength)<otherLength){
										str2=reverse(otherChrCR.substring(end2, end2+extendLength).toUpperCase());  strCR2=reverse(otherChrCR.substring(end2, end2+extendLength));
									}
									else{
										change=false;
									}
								}
								
								if(change){
									align=globalAlignment(str1,str2,strCR1,strCR2,sAndP);
									if(Double.parseDouble(align[3])/align[0].length()>0.5){
										start=true; //extend1=true;
										if(onepair[14].equalsIgnoreCase("+")){
											start1=start1-extendLength;
										}
										else{
											end1=end1+extendLength;
										}
										if(onepair[15].equalsIgnoreCase("+")){
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
							
							if(time3>0){
								CRs1=-1; CRs2=-1; indexTE++;
								if(indexTE%100==0){
//									System.out.println("trim and extend start: "+indexTE+"  "+time3);
								}
								
								while(align1.size()>time3){
									align1.remove(align1.size()-1); align2.remove(align2.size()-1); 
								}
								if(onepair[14].equalsIgnoreCase("+")){
									start1=s1-extendLength*time3;
								}
								else{
									end1=e1+extendLength*time3;
								}
								if(onepair[15].equalsIgnoreCase("+")){
									start2=s2-extendLength*time3;
								}
								else{
									end2=e2+extendLength*time3;
								}
								alignment=updateAlign(0,alignment[0],align1,alignment[1],align2);
							}
							else{
								start1=s1; end1=e1; start2=s2; end2=e2;
							}
						}
					}
					else{
						CRs1=-1; CRs2=-1; indexE++;
						if(indexE%100==0){
//							System.out.println("extend start: "+indexE+"  "+time1);
						}
						
						while(align1.size()>time1){
							align1.remove(align1.size()-1); align2.remove(align2.size()-1); 
						}
						if(onepair[14].equalsIgnoreCase("+")){
							start1=s1-extendLength*time1;
						}
						else{
							end1=e1+extendLength*time1;
						}
						if(onepair[15].equalsIgnoreCase("+")){
							start2=s2-extendLength*time1;
						}
						else{
							end2=e2+extendLength*time1;
						}
						alignment=updateAlign(0,alignment[0],align1,alignment[1],align2);
						
					}
					
//					System.out.println("test2  "+time1+"  "+time3);//test
					
					end=true; change=true; time2=0; //extend2=false;
					s1=start1; e1=end1; s2=start2; e2=end2; 
					align1.clear(); align2.clear(); Pec.clear();
					while(end){
						end=false; str1=""; str2="";  
						if(onepair[14].equalsIgnoreCase("+")){//get str1
							if((end1+extendLength)<=length){
								str1=chrCR.substring(end1, end1+extendLength).toUpperCase();  strCR1=chrCR.substring(end1, end1+extendLength);
							}
							else{
								change=false;
							}
						}
						else{
							if((start1-extendLength)>=0 ){
								str1=reverse(chrCR.substring(start1-extendLength, start1).toUpperCase());  strCR1=reverse(chrCR.substring(start1-extendLength, start1));
							}
							else{
								change=false;
							}
						}
						
						if(onepair[15].equalsIgnoreCase("+")){
							if((end2+extendLength)<otherLength){
								str2=otherChrCR.substring(end2, end2+extendLength).toUpperCase();  strCR2=otherChrCR.substring(end2, end2+extendLength);
							}
							else{
								change=false;
							}
						}
						else{
							if((start2-extendLength)>=0){
								str2=reverse(otherChrCR.substring(start2-extendLength, start2).toUpperCase());  strCR2=reverse(otherChrCR.substring(start2-extendLength, start2));
							}
							else{
								change=false;
							}
						}
						
						if(change){
							align=globalAlignment(str1,str2,strCR1,strCR2,sAndP);
							if(Double.parseDouble(align[3])/align[0].length()>0.5){
								end=true;  //extend2=true;
								if(onepair[14].equalsIgnoreCase("+")){
									end1=end1+extendLength;
								}
								else{
									start1=start1-extendLength;
								}
								if(onepair[15].equalsIgnoreCase("+")){
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
					//System.out.println(time2);	//test
					
//					System.out.println("test3  "+time2);//test
					
					if(time2==0){
						CRe1=lastCR(seg1);  CRe2=lastCR(seg2); change=true; 
						start1=s1; end1=e1; start2=s2; end2=e2;
					/*	if((CRe1<10&&CRe2>extendLength)||(CRe2<10&&CRe1>extendLength)){
							if(time3==0&&CRs1==-1&&CRs2==-1){
								System.out.println(CRe1+"  "+CRe2);
								System.out.println(temp);
							}
						}*/
						
						while((CRe1<extendLength||CRe2<extendLength)&&change){
							if(seg1.length()>extendLength&&seg2.length()>extendLength){
								align=globalAlignment(segCR1.substring(segCR1.length()-extendLength),segCR2.substring(segCR2.length()-extendLength),segCR1.substring(segCR1.length()-extendLength),segCR2.substring(segCR2.length()-extendLength),sAndP);
								
								if(Double.parseDouble(align[3])/align[0].length()<0.7){//if the end similarity is low, thus need to be cutted
									if(CRe1<extendLength&&CRe2<extendLength){
										if(CRe1<CRe2){
											newIndex=cutCigarEnd(1,CRe2,alignment,seg1,onepair[14],start1,end1,seg2,onepair[15],start2,end2);
										}
										else{
											newIndex=cutCigarEnd(0,CRe1,alignment,seg1,onepair[14],start1,end1,seg2,onepair[15],start2,end2);
										}
									}
									else if(CRe1<extendLength){
										newIndex=cutCigarEnd(0,CRe1,alignment,seg1,onepair[14],start1,end1,seg2,onepair[15],start2,end2);
									}
									else{
										newIndex=cutCigarEnd(1,CRe2,alignment,seg1,onepair[14],start1,end1,seg2,onepair[15],start2,end2);
									}
									
									start1=newIndex[0]; end1=newIndex[1]; start2=newIndex[2]; end2=newIndex[3];
									if(onepair[14].equalsIgnoreCase("+")){
										seg1=chr.substring(start1, end1);  segCR1=chrCR.substring(start1, end1).toUpperCase();
									}
									else{
										seg1=reverse(chr.substring(start1, end1));  segCR1=reverse(chrCR.substring(start1, end1).toUpperCase());
									}
									if(onepair[15].equalsIgnoreCase("+")){
										seg2=otherChr.substring(start2, end2); 	segCR2=otherChrCR.substring(start2, end2).toUpperCase();				
									}
									else{
										seg2=reverse(otherChr.substring(start2, end2));   	segCR2=reverse(otherChrCR.substring(start2, end2).toUpperCase());					
									}
									CRe1=lastCR(seg1); CRe2=lastCR(seg2); time2++;
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
							indexT++;
//							if(indexT%100==0){
//								System.out.println("trim end: "+indexT+"  "+time2);
//							}
							//extend end after trim
							end=true; change=true; time3=0; //extend2=false;
							s1=start1; e1=end1; s2=start2; e2=end2; 
							align1.clear(); align2.clear(); Pec.clear();
							while(end){
								end=false; str1=""; str2="";  
								if(onepair[14].equalsIgnoreCase("+")){//get str1
									if((end1+extendLength)<=length){
										str1=chrCR.substring(end1, end1+extendLength).toUpperCase();  strCR1=chrCR.substring(end1, end1+extendLength);
									}
									else{
										change=false;
									}
								}
								else{
									if((start1-extendLength)>=0 ){
										str1=reverse(chrCR.substring(start1-extendLength, start1).toUpperCase());  strCR1=reverse(chrCR.substring(start1-extendLength, start1));
									}
									else{
										change=false;
									}
								}
								
								if(onepair[15].equalsIgnoreCase("+")){
									if((end2+extendLength)<otherLength){
										str2=otherChrCR.substring(end2, end2+extendLength).toUpperCase();  strCR2=otherChrCR.substring(end2, end2+extendLength);
									}
									else{
										change=false;
									}
								}
								else{
									if((start2-extendLength)>=0){
										str2=reverse(otherChrCR.substring(start2-extendLength, start2).toUpperCase());  strCR2=reverse(otherChrCR.substring(start2-extendLength, start2));
									}
									else{
										change=false;
									}
								}
								
								if(change){
									align=globalAlignment(str1,str2,strCR1,strCR2,sAndP);
									if(Double.parseDouble(align[3])/align[0].length()>0.5){
										end=true;  //extend2=true;
										if(onepair[14].equalsIgnoreCase("+")){
											end1=end1+extendLength;
										}
										else{
											start1=start1-extendLength;
										}
										if(onepair[15].equalsIgnoreCase("+")){
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
							if(time3>0){
								CRe1=-1; CRe2=-1; indexTE++;
								if(indexTE%100==0){
//									System.out.println("trim and extend end: "+indexTE+"  "+time3);
								}
								
								while(align1.size()>time3){
									align1.remove(align1.size()-1); align2.remove(align2.size()-1); 
								}
								if(onepair[14].equalsIgnoreCase("+")){
									end1=e1+extendLength*time3;
								}
								else{
									start1=s1-extendLength*time3;
								}
								if(onepair[15].equalsIgnoreCase("+")){
									end2=e2+extendLength*time3;
								}
								else{
									start2=s2-extendLength*time3;
								}
								alignment=updateAlign(1,alignment[0],align1,alignment[1],align2);
							}
							else{
								start1=s1; end1=e1; start2=s2; end2=e2;
							}
							
						}
					}
					else{
						CRe1=-1; CRe2=-1; indexE++;
//						if(indexE%100==0){
//							System.out.println("extend end: "+indexE+"  "+time2);
//						}
						
						while(align1.size()>time2){
							align1.remove(align1.size()-1); align2.remove(align2.size()-1); 
						}
						if(onepair[14].equalsIgnoreCase("+")){
							end1=e1+extendLength*time2;
						}
						else{
							start1=s1-extendLength*time2;
						}
						if(onepair[15].equalsIgnoreCase("+")){
							end2=e2+extendLength*time2;
						}
						else{
							start2=s2-extendLength*time2;
						}
						alignment=updateAlign(1,alignment[0],align1,alignment[1],align2);
					}
					
//					System.out.println("test4  "+time2+"  "+time3); //test
					
					if(time1>0||time2>0){
						if(Math.abs(end1-start1-(end2-start2))<maxN){
							newCigar=computeCigar(alignment);
							writer.write(onepair[0]+"  "+newCigar[0]+"  "+onepair[2]+"  "+onepair[3]+"  "+onepair[4]+"  ");
							writer.write(start1+"  "+end1+"  "+onepair[7]+"  "+start2+"  "+end2+"  ");
							writer.write(newCigar[1]+"  "+newCigar[2]+"  "+newCigar[3]+"  "+newCigar[4]+"  "+onepair[14]+"  "+onepair[15]+"  ");					
							writer.write(CRs1+"  "+CRe1+"  "+CRs2+"  "+CRe2);
							writer.newLine();
							writerBG.write(temp); writerBG.newLine();
						}
						
					}
					else{	
						if(Math.abs(Integer.parseInt(onepair[6])-Integer.parseInt(onepair[5])-(Integer.parseInt(onepair[9])-Integer.parseInt(onepair[8])))<maxN){
							writer.write(temp+"  "+CRs1+"  "+CRe1+"  "+CRs2+"  "+CRe2); writer.newLine();
							writerBG.write(temp); writerBG.newLine();
						}
										
					}
					
				}
				
//				if(line%10000==0) {
//					System.out.println("end  "+line);
//				}
			}
			writer.close(); writerBG.close(); in.close(); 
			System.out.println("end!  totalNum:"+index+"  ExtendNum:"+indexE+"  TrimNum:"+indexT+"  TrimEndNum:"+indexTE);	
			long endTime=System.currentTimeMillis();
//			System.out.println("runTime: "+(endTime-startTime));
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
	
}

