# SDquest usage:

Download the "Code" folder. The excutable script "SDquest.sh" is under the "Code" folder.

 ./SDquest.sh CPU_NUMBER
- CPU_NUMBER: an integer, indicating the number of CPUs used by SDquest

For example, “./SDquest.sh 20” means running SDquest with 20 CPUs.


# Download genome datasets as input:
Currently, SDquest has detected and analyzed segmental duplications on the following human, gorilla and mouse genomes downloaded from the UCSC Genome Browser.

- Human genome (GRCH37/hg19) : http://hgdownload.soe.ucsc.edu/goldenPath/hg19/bigZips/
- Human genome (GRCH38/hg38): http://hgdownload.soe.ucsc.edu/goldenPath/hg38/bigZips/
- Gorilla genome (GMSRT3/gorGor5): http://hgdownload.soe.ucsc.edu/goldenPath/gorGor5/bigZips/
- Mouse genome (NCBI36/mm8): http://hgdownload.soe.ucsc.edu/goldenPath/mm8/bigZips/
- Mouse genome (GRCm38/mm10): http://hgdownload.soe.ucsc.edu/goldenPath/mm10/bigZips/

Technically, SDquest can detect segmental duplications on any genome as long as the input genome sequence and the genome sequence with common repeats are masked by 'N' are given. The input genome sequence can be given by a single or multiple fasta files, and should be named by "*.fa" (say "chr1.fa") and put in the "Genome" folder. The corresponding genome sequence with common repeats are masked by 'N' should also be give by fasta format file, and named by "*.fa.masked" (say "chr1.fa.masked") and put in the "maskedGenome" folder. An example is shown in the "Genome" folder. 

For the 5 genomes show above, the input genome sequence can be found in the file “chromFa.tar.gz” and “chromFaMasked.tar.gz” downloaded from one of the above links. Unzip these two files and copy all the chromosome files into the folder “Genome” and "maskeGenome" under the “Code” directory. Note that the current version SDquest only allows you to handle one genome (in the “GenomeData” folder) at a time.

# Output from SDquest:
There are two output files of SDquest “Pairwise_SDs.txt” and “MosaicSDs_SDblockIndexes.txt”. They can be found under the “Code” folder. 

(1) Pairwise_SDs.txt

This file contains all pairwise alignments identified by SDquest with similarities no less than 70%. Each line in the file represents a pairwise alignment. The format of each line is shown below.

“index  cigar  chrA  startA  endA  strandA  chrB  startB  endB  strandB  Match  Mismath  Indel  identity”
- index: the index of pairwise alignment
- cigar: alignments between unique sequences (non-common repeat and non-tandem repeats) in this pairwise alignment
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


