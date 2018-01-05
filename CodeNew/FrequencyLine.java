
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class FrequencyLine {
	public static double mersToNum(String str){
		double num=0;
		char[] strr=str.toCharArray();
		int length=strr.length;
		for(int i=0;i<length;i++){
			char temp=strr[length-1-i];
			switch(temp){
			case 'A':
				num=num+0*Math.pow(4, i);
				break;
			case 'C':
				num=num+1*Math.pow(4, i);
				break;
			case 'G':
				num=num+2*Math.pow(4, i);
				break;
			case 'T':
				num=num+3*Math.pow(4, i);
				break;
			default:
				break;
			}			
		}		
		return num;
	}

	public static int binarySearch(String str, ArrayList<Double> d){		
		double des=mersToNum(str);
		int low=0; int high=d.size()-1;
		while((low<=high)&&(low<d.size())&&(high<d.size())){
			int middle=low+(high-low)/2;
			if(d.get(middle)==des){
//				System.out.println("find out");
				return middle; 
			}
			else if(des<d.get(middle)){
				high=middle-1;
			}
			else{
				low=middle+1;
			}			
		}		
		return -1;
	}

	public static String reverse(String str){
		char[] strr=new char[str.length()];
		int length=strr.length;
		for(int i=0;i<length;i++){
			char temp=str.charAt(length-1-i);
			switch(temp){
			case 'A':
				strr[i]='T';
				break;
			case 'C':
				strr[i]='G';
				break;
			case 'T':
				strr[i]='A';
				break;
			case 'G':
				strr[i]='C';
				break;
			case 'N':
				strr[i]='N';
				break;
			default:
				break;
			}			
		}
		return String.valueOf(strr);
	}
	public static void readFreqKmer(ArrayList<Double> num, ArrayList<Byte> freq, String str){
		try{
			Scanner in =new Scanner(new File(str)); //"genome_25mer_2.sorted.txt"
			int kmerFreq; int p=0;
			while(in.hasNextLine()){
				String[] onePair= in.nextLine().trim().split("[\\p{Space}]+");
				num.add(mersToNum(onePair[0]));	
				kmerFreq=Integer.parseInt(onePair[1]);
    			if(kmerFreq>6){
    				freq.add((byte)6);
    			}
    			else{
    				freq.add((byte)kmerFreq);
    			}   
				if(p<20){
//					System.out.println(mersToNum(onePair[0])+"  "+onePair[0]+"  "+Integer.parseInt(onePair[1])+"  "+num.get(p)+"  "+freq.get(p));
				}
				p++;
			} 
//			System.out.println("end reading frequent kmers! "+p);
			in.close();
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}		
	}

	public static int numOfN(String str){
		int numN=0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)=='N'){
				numN++;
			}
		}
		return numN;
	}
   
	public static void main(String args[]){
		
		long startTime=System.currentTimeMillis();
		byte curFreq=0,freqBef=0; byte curSD=0, SDBef=0; int kmerLength=Integer.parseInt(args[3]);  // int lastFreq=0; // for N12,  HM process
		
		try{	
			 //reading frequent kmers
			ArrayList<Double> Kmers=new ArrayList<Double>();   ArrayList<Byte> freq= new ArrayList<Byte>(); //byte freqMer=3;
	        readFreqKmer(Kmers,freq, args[0]); 
			System.out.println("there are "+Kmers.size()+" repetitive k-mers in the input genome!");
			
	        Scanner inChr =new Scanner(new File(args[1])); //"genome.masked_all.fasta"
		    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[2])));  //"FrequencyLine.txt"
		    String tempChr; int line=0;
			
            //handling chromosome
            while(inChr.hasNextLine()){
				writer.write(inChr.nextLine()); writer.newLine();					
				tempChr=inChr.nextLine(); int length=tempChr.length();	line++;			
//	            System.out.println("begin handling chromosome "+n+" !  "+length);
								
				String tempKmer; int index; char tempChar; int nNum;  
				for(int i=0;i<=tempChr.length()-kmerLength;i++){
					tempKmer=tempChr.substring(i,i+kmerLength); 
					nNum=numOfN(tempKmer);
					if(nNum==0){
						index = binarySearch(tempKmer,Kmers);
        				if(index==-1){
        					index = binarySearch(reverse(tempKmer),Kmers);
        					if(index==-1){
        						writer.write("1"); 
        					}
        					else{
        						writer.write(freq.get(index)+"");
        					}
        				}
        				else{
        					writer.write(freq.get(index)+""); 
        				}

					}
				    else if(tempChr.charAt(i)!='N'){					
						writer.write("1");
					}
					else{
						writer.write("0"); 
					}					
				}
				writer.newLine();
//				if(line%1000==0){
//					System.out.println("end writing "+line);
//				}
								
			}
            inChr.close();  writer.close();
			
            long endTime=System.currentTimeMillis();
//			System.out.println("end writing frequency line !   "+(endTime-startTime));
			
			}catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
			catch(Exception e){
				
			}			
	}

}



