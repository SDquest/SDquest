
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ElementSDs {

	public static int getDistance(int s, int e, String CR){
		int dis=0;
		for(int i=s;i<=e;i++){
			if(CR.charAt(i)!='N'){
				dis++;
			}
		}
		return dis;
	}
	public static void main(String args[]){
		try{
			long startTime=System.currentTimeMillis();
			ArrayList<String> CRs=new ArrayList<String>(); ArrayList<String> ends=new ArrayList<String>(); 
			ArrayList<boolean[]> lines=new ArrayList<boolean[]>();
			Scanner inChr = new Scanner(new File("genome.masked_all.fasta")); String tempChr;
			Scanner inEnds = new Scanner(new File("EndpointsLine.txt"));
			while(inChr.hasNextLine()){
				inChr.nextLine(); tempChr=inChr.nextLine();
				CRs.add(tempChr); 
				boolean[] temp=new boolean[tempChr.length()];
				for(int j=0;j<temp.length;j++){
					temp[j]=false;
				}
				lines.add(temp);
				inEnds.nextLine(); tempChr=inEnds.nextLine();
				ends.add(tempChr); 
			}
			inChr.close(); inEnds.close();
//			System.out.println("end reading chrs and endlines!  "+ends.size());
			
			int clusterLength=100; int badNum=0; int badInterval=0;
			Scanner in = new Scanner(new File("BG_MosaicSDs.fasta"));	in.nextLine();
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("ElementSDs_100.fasta")));	
			BufferedWriter writerC = new BufferedWriter(new FileWriter(new File("Clusters_100.fasta")));
			BufferedWriter writerB = new BufferedWriter(new FileWriter(new File("BadMosaicSD_100.fasta")));
			writerB.write("chr  start  end  points  cluster  length  compactL"); writerB.newLine();
			writer.write("index  chr  midStart  midEnd  midLength  startC  endC  lengthC"); writer.newLine();
			writerC.write("index   chr   start   end   size   length  compactLength"); writerC.newLine();
			
			String[] onepair;	int chr, start, end, length=0; int segNum; ArrayList<Integer> point=new ArrayList<Integer>();
			int index=0, indexC=0;
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+"); point.clear();
				chr=Integer.parseInt(onepair[0]); start=Integer.parseInt(onepair[1]); end=Integer.parseInt(onepair[2]);
				for(int i=start;i<end;i++){
					if(ends.get(chr).charAt(i)=='1'){
						point.add(i);
					}
				}
//				for(int i=0;i<point.size();i++){//test
//					if(CRs.get(chr).charAt(point.get(i))=='N'){
//						System.out.println("wrong  "+i);
//					}
//				}
				
				ArrayList<Integer> cluster=new ArrayList<Integer>();
				int cur=point.get(0); cluster.add(cur); int lastPoint=0, mPoint=-1; 
				for(int i=1;i<point.size();i++){
					if(getDistance(cur, point.get(i), CRs.get(chr))>clusterLength){
						writerC.write(indexC+"   "+chr+"   "+cluster.get(0)+"   "+cur+"   "+cluster.size()+"   "+(cur-cluster.get(0)+1)+"  "+getDistance(cluster.get(0), cur, CRs.get(chr))); writerC.newLine(); indexC++;
						for(int k=cluster.get(0);k<=cur;k++){
							lines.get(chr)[k]=true;
						}
						
						if(mPoint!=-1){
							writer.write(index+"  "+chr+"  "+mPoint+"  "+(cluster.get(cluster.size()/2)+1)+"  "+getDistance(mPoint,cluster.get(cluster.size()/2),CRs.get(chr))+"  "+lastPoint+"  "+(cluster.get(0)+1)+"  "+getDistance(lastPoint,cluster.get(0),CRs.get(chr))); 
							writer.newLine(); index++;
//							if(CRs.get(chr).charAt(lastPoint)=='N'||CRs.get(chr).charAt(cluster.get(0))=='N'){//test
//								System.out.println(chr+"  "+start+"  "+end+"  "+CRs.get(chr).charAt(lastPoint)+"  "+CRs.get(chr).charAt(cluster.get(0)));
//							}
						}
						lastPoint=cur; mPoint=cluster.get(cluster.size()/2); cluster.clear(); 							
					}
					cur=point.get(i); cluster.add(cur);
				}
				writerC.write(indexC+"   "+chr+"   "+cluster.get(0)+"   "+cur+"   "+cluster.size()+"   "+(cur-cluster.get(0)+1)+"  "+getDistance(cluster.get(0), cur, CRs.get(chr))); writerC.newLine(); indexC++;
				for(int k=cluster.get(0);k<=cur;k++){
					lines.get(chr)[k]=true;
				}
				
				if(mPoint!=-1){
					writer.write(index+"  "+chr+"  "+mPoint+"  "+(cluster.get(cluster.size()/2)+1)+"  "+getDistance(mPoint,cluster.get(cluster.size()/2),CRs.get(chr))+"  "+lastPoint+"  "+(cluster.get(0)+1)+"  "+getDistance(lastPoint,cluster.get(0),CRs.get(chr))); 
					writer.newLine(); index++;
//					if(CRs.get(chr).charAt(lastPoint)=='N'||CRs.get(chr).charAt(cluster.get(0))=='N'){//test
//						System.out.println(chr+"  "+start+"  "+end+"  "+CRs.get(chr).charAt(lastPoint)+"  "+CRs.get(chr).charAt(cluster.get(0)));
//					}
				}
				else{
					badNum++; badInterval=badInterval+point.size()/2;
					writerB.write(chr+"  "+start+"  "+end+"  "+point.size()+"  "+(indexC-1)+"  "+(end-start)+"  "+getDistance(start, end-1, CRs.get(chr))); writerB.newLine();
				}
			}
			writer.close(); writerC.close(); writerB.close(); in.close();
			
			writer = new BufferedWriter(new FileWriter(new File("ClustersLine.txt")));	
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
//			System.out.println("end! "+indexC+"  "+index+"  "+badNum+"  "+badInterval);
//			System.out.println("runTime: "+(endTime-startTime));
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
}
