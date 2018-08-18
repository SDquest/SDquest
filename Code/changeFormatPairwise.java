
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class changeFormatPairwise {
	
	public static int distance(String str, int start, int end){
		int dis=0;
		for(int i=start;i<end;i++){
			if(str.charAt(i)!='N'&&str.charAt(i)!='n'){
				dis++;
			}
		}
		return dis;
	}
	public static int existN(String str){
		int dis=0;
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)=='N'||str.charAt(i)=='n'){
				dis++;;
			}
		}
		return dis;
	}
	public static void main (String args[]){
		try{
//			Scanner inChrN=new Scanner(new File("genome.txt"));
			Scanner inChr=new Scanner(new File(args[0]));  // genome.masked_all.fasta
			Scanner in=new Scanner(new File(args[1])); String str;  // Genome_size_Indexes.txt
			ArrayList<String> chrs=new ArrayList<String>();  boolean end=false;
			ArrayList<String> segs=new ArrayList<String>();
//			ArrayList<String> segsN=new ArrayList<String>();
			while(in.hasNextLine()&&!end){
				str=in.nextLine(); 
				if(str.charAt(0)=='>'){
					chrs.add(str.substring(1));
					inChr.nextLine(); segs.add(inChr.nextLine());
//					inChrN.nextLine(); segsN.add(inChrN.nextLine());
				}
				else{
					end=true;
				}
			}
			in.close(); inChr.close();
//			System.out.println("end reading! "+chrs.size()+"  "+segs.size());
			
			in=new Scanner(new File(args[2])); // SCN_LastzResult_500NonCR_NewExtendPec50.txt
			String outdir = args[3];
			BufferedWriter writer=new BufferedWriter (new FileWriter(new File(outdir, "Pairwise_SDs.txt")));
			BufferedWriter writerBG=new BufferedWriter (new FileWriter(new File(outdir, "BG_SDIndexes.fasta")));
			in.nextLine(); String[] onepair; int indexW=0; int length1, length2; int start1,end1, start2,end2,chr, otherChr; int line=0, tempN1, tempN2;
			writer.write("index  chrA  startA  endA  strandA  chrB  startB  endB  strandB  Match  Mismath  Indel  identity"); writer.newLine();
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+"); line++;
				if(onepair[16].equalsIgnoreCase("-1")){
					start1=Integer.parseInt(onepair[5]);
				}
				else{
					start1=Integer.parseInt(onepair[16]);
				}
				if(onepair[17].equalsIgnoreCase("-1")){
					end1=Integer.parseInt(onepair[6]);
				}
				else{
					end1=Integer.parseInt(onepair[17]);
				}
				
				if(onepair[18].equalsIgnoreCase("-1")){
					start2=Integer.parseInt(onepair[8]);
				}
				else{
					start2=Integer.parseInt(onepair[18]);
				}
				if(onepair[19].equalsIgnoreCase("-1")){
					end2=Integer.parseInt(onepair[9]);
				}
				else{
					end2=Integer.parseInt(onepair[19]);
				}
				chr=Integer.parseInt(onepair[4]); otherChr=Integer.parseInt(onepair[7]);
				
//				tempN1=existN(segsN.get(chr).substring(start1, end1)); tempN2=existN(segsN.get(otherChr).substring(start2, end2));
//				if(tempN1>0||tempN2>0){
//					System.out.println(line+"  "+tempN1+"  "+tempN2);
//				}
				
				if(chr!=otherChr||start1>end2||start2>end1){
					length1=distance(segs.get(chr),start1,end1);
					length2=distance(segs.get(otherChr),start2,end2);
					
					if(Double.parseDouble(onepair[22])>=0.7&&length1>=500&&length2>=500){
						writer.write(indexW+"  "+chrs.get(chr)+"  "+start1+"  "+end1+"  "+onepair[14]+"  "+chrs.get(otherChr)+"  "+start2+"  "+end2+"  "+onepair[15]+"  ");
				        writer.write(onepair[10]+"  "+onepair[11]+"  "+onepair[13]+"  "+onepair[22]);
				        writer.newLine(); indexW++;
				        writerBG.write(chr+"  "+start1+"  "+end1); writerBG.newLine();
				        writerBG.write(otherChr+"  "+start2+"  "+end2); writerBG.newLine();
					}
					else{
//						System.out.println("length and identity  "+line);
					}
				}
				else{
//					System.out.println("overlap  "+line);
				}
			}
			in.close(); writer.close(); writerBG.close();
			System.out.println("Pairwise SDs can be found in " + outdir + "/Pairwise_SDs.txt!");
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
	
}
