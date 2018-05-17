
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ChangeFormat {

	public static void main(String args[]){
		try{
//			long startTime=System.currentTimeMillis();
			Scanner in = new Scanner(new File(args[0]));	//fa genome
			String outdir = args[2];
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outdir, "genome.txt")));
			BufferedWriter writerGenome = new BufferedWriter(new FileWriter(new File(outdir, "Genome_size_Indexes.txt")));
			double GenomeSize=0; String temp; int segNum=0;  int CRlength=0; int Nlength=0;
			
			temp=in.nextLine();
			if(temp.charAt(0)!='>') {
				System.out.println("The first line of fa genome data is not start with character > ");
			}
			else {
				writer.write(">"+segNum); writer.newLine();
				writerGenome.write(temp); writerGenome.newLine(); segNum++;
			}
			
			while(in.hasNextLine()) {
				temp=in.nextLine();
				if(temp.charAt(0)!='>') {
					writer.write(temp); GenomeSize=GenomeSize+temp.length();
					for(int i=0;i<temp.length();i++) {
						if(temp.charAt(i)=='a'||temp.charAt(i)=='c'||temp.charAt(i)=='g'||temp.charAt(i)=='t') {
							CRlength++;
						}
						else if(temp.charAt(i)=='n'||temp.charAt(i)=='N') {
							Nlength++;
						}
						
					}
				}
				else {
					writer.newLine();
					writer.write(">"+segNum); writer.newLine();
					writerGenome.write(temp); writerGenome.newLine(); segNum++;
				}
			}
			
			writer.newLine(); writer.close(); 
			writerGenome.write("GenomeSize:  "+GenomeSize+"    segNum: "+segNum);  writerGenome.newLine();
			writerGenome.write("CRlength:  "+CRlength+"    CR/Genome: "+(CRlength/GenomeSize));  writerGenome.newLine();
			writerGenome.write("Nlength:  "+Nlength+"    N/Genome: "+(Nlength/GenomeSize)); writerGenome.newLine();
			writerGenome.write("(CR+N)/Genome: "+((CRlength+Nlength)/GenomeSize)); writerGenome.newLine();
			writerGenome.close(); in.close();
//			System.out.println("end fa Genome writing! ");
			
			in = new Scanner(new File(args[1]));	// "genome.masked.fa"
			writer = new BufferedWriter(new FileWriter(new File(outdir, "genome.masked.txt"))); segNum=0;
			
			temp=in.nextLine();
			if(temp.charAt(0)!='>') {
				System.out.println("The first line of masked genome data is not start with character > ");
			}
			else {
				writer.write(">"+segNum); writer.newLine();
				segNum++;
			}
			
			while(in.hasNextLine()) {
				temp=in.nextLine();
				if(temp.charAt(0)=='>') {
					writer.newLine(); 
					writer.write(">"+segNum); writer.newLine(); segNum++;
				}
				else {
					writer.write(temp);
				}
			}
			
			writer.newLine(); writer.close(); in.close();
//			long endTime=System.currentTimeMillis();
			
			System.out.println("The Input Genome Size is: "+GenomeSize);
			
			
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		catch(Exception e){
			
		}
	}
}
