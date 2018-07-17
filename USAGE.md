# SDquest usage:

Download the "Code" folder. The executable script "SDquest.sh" is under the "Code" folder.

 ./SDquest.sh <output_dir> <genome.fa> <genome.masked.fa> <cpu_number>
- output_dir: a directory to store all result files (SDquest creates the folder if it does not exist)
- genome.fa: a path to the genome
- genome.masked.fa: a path to the masked genome
- cpu_number: an integer, indicating the number of CPUs used by SDquest

For example, “./SDquest.sh human/ human.fa human.masked.fa 20” means running SDquest with 20 CPUs using human.fa and human.masked.fa files,
all output files will be saved to the "human" folder.


# Download genome datasets as input:
Currently, SDquest has detected and analyzed segmental duplications on the following human, gorilla and mouse genomes downloaded from the UCSC Genome Browser.

- Human genome (GRCH37/hg19) : http://hgdownload.soe.ucsc.edu/goldenPath/hg19/bigZips/
- Human genome (GRCH38/hg38): http://hgdownload.soe.ucsc.edu/goldenPath/hg38/bigZips/
- Gorilla genome (GMSRT3/gorGor5): http://hgdownload.soe.ucsc.edu/goldenPath/gorGor5/bigZips/
- Mouse genome (NCBI36/mm8): http://hgdownload.soe.ucsc.edu/goldenPath/mm8/bigZips/
- Mouse genome (GRCm38/mm10): http://hgdownload.soe.ucsc.edu/goldenPath/mm10/bigZips/

To run SDquest on the 5 genomes shown above, please download both of the file “chromFa.tar.gz” and “chromFaMasked.tar.gz”
in one of the above links. Unzip these two files, you will find chromosome files in the "chromFa" folder and the corresponding chromosome
files with common repeats be masked by 'N' in the "chromFaMasked" folder. Concatenate all files from the "chromFa" folder to one FASTA file and
all files from the "chromFaMasked" folder to another FASTA file.

For human and mouse genomes, the pairwise SDs and mosaic SDs provided in this website are obtained by running SDquest on 22/19 autosomal chromosomals,
X chromosomal and Y chromosomal. The current version SDquest only allows you to handle one genome at a time.

Technically, SDquest can detect segmental duplications on any genome as long as the input genome sequence and the genome sequence with common repeats are masked by 'N' are given. 

# Output from SDquest:
There are two output files of SDquest “Pairwise_SDs.txt” and “MosaicSDs_SDblockIndexes.txt”. They can be found under the output folder.

(1) Pairwise_SDs.txt

This file contains all pairwise alignments identified by SDquest with similarities no less than 70%. Each line in the file represents a pairwise alignment. The format of each line is shown below.

“index  chrA  startA  endA  strandA  chrB  startB  endB  strandB  Match  Mismath  Indel  identity”
- index: the index of pairwise alignment
- chrA: the chromosome index of the first segment
- startA: the start position of the first segment
- endA: the end position of the first segment
- strandA: the strandness of the first segment
- chrB: the chromosome index of the second segment
- startB: the start position of the second segment
- endB: the end position of the second segment
- strandB: the strandness of the second segment
- Match: the number of matches in the alignment
- Mismatch: the number of mismatches in the alignment
- Indel: the number of insertions/deletions in the alignment
- identity: the overall similarity of the alignment 

(2) MosaicSDs_SDblockIndexes.txt

This file contains positions of all mosaic SDs in the genome. Each line represents position of one mosaic SD and the SDblocks in this mosaic SD. Each SDblock is represented by a distinct number as Bi. The format of each line is shown as follows.

“chr  start  end :  B1  B2 ...”
- chr: the chromosome index of the mosaic SD
- start: the start position of this mosaic SD
- end: the end position of this mosaic SD
- B1 B2 ...: the ordered list of SDblocks in this mosaic SD

# Test Case
A test case with 1 Mb simulated genome is attached inside the "Genome" and "maskedGenome" folder under the "Code" directory.

In this 1 Mb simulated genome, there exist 4 mosaic SDs \[A, B] (start at position 100,000), \[A] (start at position 300,000),  \[-B,-A] (start at position 600,000) and  \[-B] (start at position 850,000). A and B represent two SD-blocks with length 2000 bp and 3000 bp respectively. To see output of SDquest on this simulated genome, users can run command "./SDquest.sh 1".

# SD browser
An interactive SD browser for the human genome (hg19 and hg38 assemblies) is attached under the "SDbrowser" directory as "hg19.html" and "hg38.html".
