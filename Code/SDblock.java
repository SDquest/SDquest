
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SDblock {

	public static boolean exist(int a, ArrayList<Integer> list){
		for(int i=0;i<list.size();i++){
			if(list.get(i)==a){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String args[]){
		try{
			long startTime=System.currentTimeMillis();
			Scanner in = new Scanner(new File(args[0]));	in.nextLine();
			String[] onepair; ArrayList<Integer> length=new ArrayList<Integer>(); 
			
			while(in.hasNextLine()){
				onepair=in.nextLine().trim().split("[\\p{Space}]+");
				length.add(Integer.parseInt(onepair[4]));
			}
			in.close();
			
			in = new Scanner(new File(args[1]));
			ArrayList<ArrayList<Integer>> firstE=new ArrayList<ArrayList<Integer>>(); ArrayList<ArrayList<Integer>> secondE=new ArrayList<ArrayList<Integer>>();
			String temp; 
			
			while(in.hasNextLine()){
				temp=in.nextLine();
				if(temp.length()>0){
					onepair=temp.trim().split("[\\p{Space}]+");
					int index=1; ArrayList<Integer> t1=new ArrayList<Integer>();
					while(!onepair[index].equalsIgnoreCase("--")){
						t1.add(Integer.parseInt(onepair[index])); index++;
					}
					
					index++;  ArrayList<Integer> t2=new ArrayList<Integer>();
					while(index<onepair.length){
						t2.add(Integer.parseInt(onepair[index])); index++;
					}
					firstE.add(t1); secondE.add(t2);					
				}
			}
			in.close();
//			System.out.println(firstE.size());
			
			boolean[][] Equal=new boolean[length.size()+1][length.size()+1];
			byte[][] sign=new byte[length.size()+1][length.size()+1];
			for(int i=0;i<Equal.length;i++){
				for(int j=0;j<Equal[i].length;j++){
					Equal[i][j]=false; sign[i][j]=0;
				}
			}
			
			int index1, index2;
			for(int i=0;i<firstE.size();i++){
				for(int j=0;j<firstE.get(i).size();j++){
					index1=Math.abs(firstE.get(i).get(j)); index2=Math.abs(secondE.get(i).get(j));
					Equal[index1][index2]=true;
					Equal[index2][index1]=true;
					
					if(firstE.get(i).get(j)>0&&secondE.get(i).get(j)>0){
						if(sign[index1][index2]==0){
							sign[index1][index2]=1;
						}
						else if(sign[index1][index2]!=1){
//							System.out.println("wrong! "+index1+"  "+index2+"  is:"+sign[index1][index2]+"  1");
						}
						
						if(sign[index2][index1]==0){
							sign[index2][index1]=1;
						}
						else if(sign[index2][index1]!=1){
//							System.out.println("wrong! "+index2+"  "+index1+"  is:"+sign[index2][index1]+"  1");
						}
					}
					else if(firstE.get(i).get(j)>0&&secondE.get(i).get(j)<0){
						if(sign[index1][index2]==0){
							sign[index1][index2]=2;
						}
						else if(sign[index1][index2]!=2){
//							System.out.println("wrong! "+index1+"  "+index2+"  is:"+sign[index1][index2]+"  2");
						}
						
						if(sign[index2][index1]==0){
							sign[index2][index1]=2;
						}
						else if(sign[index2][index1]!=2){
//							System.out.println("wrong! "+index2+"  "+index1+"  is:"+sign[index2][index1]+"  2");
						}
					}
					else if(firstE.get(i).get(j)<0&&secondE.get(i).get(j)>0){
						if(sign[index1][index2]==0){
							sign[index1][index2]=2;
						}
						else if(sign[index1][index2]!=2){
//							System.out.println("wrong! "+index1+"  "+index2+"  is:"+sign[index1][index2]+"  2");
						}
						
						if(sign[index2][index1]==0){
							sign[index2][index1]=2;
						}
						else if(sign[index2][index1]!=2){
//							System.out.println("wrong! "+index2+"  "+index1+"  is:"+sign[index2][index1]+"  2");
						}
					}
					else if(firstE.get(i).get(j)<0&&secondE.get(i).get(j)<0){
						if(sign[index1][index2]==0){
							sign[index1][index2]=1;
						}
						else if(sign[index1][index2]!=1){
//							System.out.println("wrong! "+index1+"  "+index2+"  is:"+sign[index1][index2]+"  1");
						}
						
						if(sign[index2][index1]==0){
							sign[index2][index1]=1;
						}
						else if(sign[index2][index1]!=1){
//							System.out.println("wrong! "+index2+"  "+index1+"  is:"+sign[index2][index1]+"  1");
						}
					}
					
				}
			}
			
			ArrayList<ArrayList<Integer>> blocks=new ArrayList<ArrayList<Integer>>();
			for(int i=0;i<Equal.length;i++){
				ArrayList<Integer> block=new ArrayList<Integer>(); block.add(i);
				for(int j=0;j<Equal[i].length;j++){
					if(Equal[i][j]==true){
						if(sign[i][j]==1){
							block.add(j);
						}
						else if(sign[i][j]==2){
							block.add(0-j);
						}
						else{
//							System.out.println("wrong2! "+i+"  "+j);
						}
						Equal[i][j]=false; Equal[j][i]=false; sign[i][j]=0; sign[j][i]=0;
					}
				}
				if(block.size()>1){
					for(int k=1;k<block.size();k++){
						int tempE=Math.abs(block.get(k));
						for(int j=0;j<Equal[tempE].length;j++){
							if(Equal[tempE][j]==true){
								if(sign[tempE][j]==1){
									if(block.get(k)>0&&!exist(j,block)){
										block.add(j);
									}
									if(block.get(k)<0&&!exist((0-j),block)){
										block.add(0-j);
									}
								}
								else if(sign[tempE][j]==2){
									if(block.get(k)>0&&!exist((0-j),block)){
										block.add(0-j);
									}
									if(block.get(k)<0&&!exist(j,block)){
										block.add(j);
									}
								}
								sign[tempE][j]=0; sign[j][tempE]=0;
								Equal[tempE][j]=false; Equal[j][tempE]=false;
								
							}
						}
					}
					blocks.add(block);
				}
			}
			
			int[] ElementSDs=new int[length.size()];
			for(int i=0;i<ElementSDs.length;i++){
				ElementSDs[i]=-1;
			}
			String outdir = args[2];
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outdir, "blocks.fasta")));
			BufferedWriter writerS = new BufferedWriter(new FileWriter(new File(outdir, "blocks_Size.fasta")));
			writerS.write("index   size   averageL   minL   maxL"); writerS.newLine();
			
			for(int i=0;i<blocks.size();i++){
				int sum=0; int min=length.get(Math.abs(blocks.get(i).get(0))-1); int max=min;
				for(int j=0;j<blocks.get(i).size();j++){
					writer.write(blocks.get(i).get(j)+", ");
					sum=sum+length.get(Math.abs(blocks.get(i).get(j))-1);
					if(min>length.get(Math.abs(blocks.get(i).get(j))-1)){
						min=length.get(Math.abs(blocks.get(i).get(j))-1);
					}
					if(max<length.get(Math.abs(blocks.get(i).get(j))-1)){
						max=length.get(Math.abs(blocks.get(i).get(j))-1);
					}
//					ElementSDs[blocks.get(i).get(j)]=blocks.get(i).get(0);
				}
				writer.newLine(); writer.newLine();
				writerS.write(i+"  "+blocks.get(i).size()+"  "+sum/blocks.get(i).size()+"  "+min+"  "+max); writerS.newLine();
			}
			writer.close(); writerS.close();
			
			long endTime=System.currentTimeMillis();
//			System.out.println("end sdblock! "+blocks.size()+"   runTime: "+(endTime-startTime));
			
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
}
