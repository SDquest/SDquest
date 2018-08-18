
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class PutativeSDsSeparated {//

	public static void main(String args[]){
		try{
			int number=Integer.parseInt(args[0]);
			Scanner in=new Scanner(new File(args[1]));  // AllSegsOfSCN_CRRemoved.fasta
			Scanner inM=new Scanner(new File(args[2]));  // AllSegsOfSCN_CRMasked.fasta
			String outdir = args[3];
			BufferedWriter writer; BufferedWriter writerM;
			ArrayList<String> segs= new ArrayList<String>(); ArrayList<String> segsM= new ArrayList<String>();
			while(in.hasNextLine()){
				segs.add(in.nextLine()); segsM.add(inM.nextLine());
			}
			in.close(); inM.close();

			int size=segs.size()/(2*number); int index=0;
			for(int i=1;i<number;i++){
				writer = new BufferedWriter(new FileWriter(new File(outdir, "AllSegsOfSCN_CRRemoved"+i+".fasta")));
				writerM = new BufferedWriter(new FileWriter(new File(outdir, "AllSegsOfSCN_CRMasked"+i+".fasta")));
				for(int j=0;j<size;j++){
					writer.write(segs.get(index)); writer.newLine();
					writerM.write(segsM.get(index)); writerM.newLine(); index++;
					writer.write(segs.get(index)); writer.newLine();
					writerM.write(segsM.get(index)); writerM.newLine(); index++;
				}
				writer.close(); writerM.close();
			}

			writer = new BufferedWriter(new FileWriter(new File(outdir, "AllSegsOfSCN_CRRemoved"+number+".fasta")));
			writerM = new BufferedWriter(new FileWriter(new File(outdir, "AllSegsOfSCN_CRMasked"+number+".fasta")));
			while(index<segs.size()){
				writer.write(segs.get(index)); writer.newLine();
				writerM.write(segsM.get(index)); writerM.newLine(); index++;
				writer.write(segs.get(index)); writer.newLine();
				writerM.write(segsM.get(index)); writerM.newLine(); index++;
			}
			writer.close(); writerM.close();

//			System.out.println("end Seperated 2!   ");

		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){

		}
	}

}
