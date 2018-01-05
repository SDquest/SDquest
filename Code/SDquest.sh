#! /bin/csh
javac ChangeFormat.java
java ChangeFormat
./TRFinder/trf409.linux64 genome.masked.txt 2 7 7 80 10 50 500 -f -d -h -ngs > genome.masked.info
javac masktandemRepeats.java
java masktandemRepeats genome.masked.txt genome.masked.info genome.masked_all.fasta 
./dsk/dsk -verbose 0 -file genome.masked_all.fasta -kmer-size 25 -abundance-min 2 -out genome_Kmer_2.h5
./dsk/dsk2ascii -verbose 0 -file genome_Kmer_2.h5 -out genome_Kmer_2.txt
sort -k 1 genome_Kmer_2.txt > genome_Kmer_2.sorted.txt
javac FrequencyLine.java
java FrequencyLine genome_Kmer_2.sorted.txt genome.masked_all.fasta FrequencyLine.txt 25
javac CombineLine.java
java CombineLine FrequencyLine.txt CombineLine.txt
javac GetAllSCSegs.java
java GetAllSCSegs genome.masked_all.fasta CombineLine.txt AllSegsOfSCN_CRMasked.fasta AllSegsOfSCN_CRRemoved.fasta 25
javac PutativeSDsSeperated.java
java PutativeSDsSeperated $argv[1]
chmod 744 Lastz.sh
./Lastz.sh $argv[1]
wait
cat AllSegsOfSCN_CRRemoved*.info > AllSegsOfSCN.info
javac LastzPost.java
java LastzPost AllSegsOfSCN_CRMasked.fasta AllSegsOfSCN.info SCN_LastzResult.txt 
javac FilterAlignmentsWith500NonCR.java
java FilterAlignmentsWith500NonCR SCN_LastzResult.txt SCN_LastzResult_500NonCR.txt 
javac SortPairs.java
java SortPairs SCN_LastzResult_500NonCR.txt SCN_LastzResult_500NonCR_sortPair.txt 
sort -n -k 5 -k 6 -k 7 -k 8 -k 9 -k 10 -k 11 -k 12 -k 14 SCN_LastzResult_500NonCR_sortPair.txt > SCN_LastzResult_500NonCR_sortPair.sorted.txt
javac FilterRepetitivePairs.java
java FilterRepetitivePairs SCN_LastzResult_500NonCR_sortPair.sorted.txt SCN_LastzResult_500NonCRfiltered.txt 
javac Recover.java
java Recover SCN_LastzResult_500NonCRfiltered.txt SCN_LastzResult_500NonCR_FilterPair.txt 
javac NewExtendPec50.java
java NewExtendPec50 genome.masked_all.fasta genome.txt SCN_LastzResult_500NonCR_FilterPair.txt SCN_LastzResult_500NonCR_NewExtendPec50.txt SCN_LastzResult_500NonCR_BG.txt
rm *.info
rm SCN_LastzResult.txt
rm SCN_LastzResult_500NonCR.txt
rm SCN_LastzResult_500NonCR_sortPair.txt
rm SCN_LastzResult_500NonCR_sortPair.sorted.txt
rm SCN_LastzResult_500NonCRfiltered.txt
rm SCN_LastzResult_500NonCR_FilterPair.txt
javac changeFormatPairwise.java
java changeFormatPairwise
echo End pairwise SDs detection. Begin analyze mosaic SDs.
javac GetIndexesBG.java
java GetIndexesBG SCN_LastzResult_500NonCR_BG.txt BG_SDIndexes.fasta
javac CopyEndPoints.java
java CopyEndPoints genome.masked_all.fasta BG_SDIndexes.fasta SCN_LastzResult_500NonCR_BG.txt EndpointsLine.txt
sort -n -k 1 -k 2 -k 3 BG_SDIndexes.fasta > BG_SDIndexes.sorted.fasta
javac MergeBG.java
java MergeBG BG_SDIndexes.sorted.fasta BG_MosaicSDs.fasta
javac ElementSDs.java
java ElementSDs
javac testLength.java
java testLength
javac NewElementSDsPair.java
java NewElementSDsPair
javac SDblock.java
java SDblock
javac ElementSDsMulti.java
java ElementSDsMulti
javac MosaicSDsBlockcompose.java
java MosaicSDsBlockcompose
rm ElementSDs*.fasta
rm length.fasta
rm Clusters_100.fasta
rm BadMosaicSD_100.fasta
rm BG*.fasta
rm AllSegsOfSCN*.fasta
rm *.class
echo end SDquest analysis
