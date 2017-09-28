# SDquest usage:
 ./SDquest.sh type n
- type: human or mouse, indicating which genome will be analyzed by SDquest
- n: an integer indicating the number of CPUs used by SDquest

For example, “./SDquest.sh human 20” means running SDquest on the human genome with 20 CPUs. 

# Download genome datesets as input
Currently, SDquest can handle the following human and mouse genomes downloaded from the UCSC Genome Browser.

- Human genome (GRCH37/hg19) : http://hgdownload.soe.ucsc.edu/goldenPath/hg19/bigZips/
- Human genome (GRCH38/hg38): http://hgdownload.soe.ucsc.edu/goldenPath/hg38/bigZips/
- Mouse genome (NCBI36/mm8): http://hgdownload.soe.ucsc.edu/goldenPath/mm8/bigZips/
- Mouse genome (GRCm38/mm10): http://hgdownload.soe.ucsc.edu/goldenPath/mm10/bigZips/

Please download both the file “chromFa.tar.gz” and “chromFaMasked.tar.gz” in one of the above links. Unzip these two files and copy all the chromosome files into the directory “GenomeData” under the “SDquest” main directory. Note that the current version SDquest only allows you to handle one genome (in the “GenomeData” folder) at a time.

# Output from SDquest:
The output file of SDquest can be found in the “SDquestResult” folder under the “SDquest” main directory. There are 2 output files of SDquest.

(1) Pairwise_SDs.txt

This file contains all pairwise alignments identified by SDquest with similarities no less than 70%. Each line in the file represents a pairwise alignment. The format of each line is shown below.

“index  cigar  segA  segB  chrA  startA  endA  chrB  startB  endB  Match  Mismath  sGap  strand1  strand2  identity”
- index: the index of pairwise alignment
- cigar: alignments between non-common repeats sequences in this pairwise alignment
- segA: index of the first segment
- segB: index of the second segment
- chrA: the chromosome index of the first segment
- startA: the start position of the first segment
- endA: the end position of the first segment
- chrB: the chromosome index of the second segment
- startB: the start position of the second segment
- endB: the end position of the second segment
- Match: the number of matches in the alignment
- Mismatch: the number of mismatches in the alignment
- sGap: the number of gaps in the alignment
- strandA: the strandness of the first segment
- strandB: the strandness of the second segment
- identity: the overall similarity of the alignment 

(2) Mosaic_SDs.txt

This file contains positions of all mosaic SDs in the genome. Each line represents position of one mosaic SD and the SDblocks in this mosaic SD. Each SDblock is represented by a distinct number as Bi. The format of each line is shown as follows.

“chr  start  end  B1  B2 ...”
- chr: the chromosome index of the mosaic SD
- start: the start position of this mosaic SD
- end: the end position of this mosaic SD
- B1 B2 ...: the linearly ordered list of SDblocks in this mosaic SD


