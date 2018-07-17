#! /bin/csh
set outdir = $argv[1]
set genome = $argv[2]
set maskedgenome = $argv[3]
set threads = $argv[4]
echo Creating $outdir
mkdir $outdir
cp $genome $outdir/genome.fa
cp $maskedgenome $outdir/genome.masked.fa
echo Processing genome files
javac ChangeFormat.java
java ChangeFormat $outdir/genome.fa $outdir/genome.masked.fa $outdir
echo Running TRFinder
chmod +x TRFinder/trf409.linux64
./TRFinder/trf409.linux64 $outdir/genome.masked.txt 2 7 7 80 10 50 500 -f -d -h -ngs > $outdir/genome.masked.info
javac masktandemRepeats.java
java masktandemRepeats $outdir/genome.masked.txt $outdir/genome.masked.info $outdir/genome.masked_all.fasta
echo Running k-mer counting
chmod +x dsk/dsk
chmod +x dsk/dsk2ascii
./dsk/dsk -verbose 0 -file $outdir/genome.masked_all.fasta -kmer-size 25 -abundance-min 2 -out $outdir/genome_Kmer_2.h5
./dsk/dsk2ascii -verbose 0 -file $outdir/genome_Kmer_2.h5 -out $outdir/genome_Kmer_2.txt
sort -k 1 $outdir/genome_Kmer_2.txt > $outdir/genome_Kmer_2.sorted.txt
javac FrequencyLine.java
java FrequencyLine $outdir/genome_Kmer_2.sorted.txt $outdir/genome.masked_all.fasta $outdir/FrequencyLine.txt 25
javac CombineLine.java
java CombineLine $outdir/FrequencyLine.txt $outdir/genome.txt $outdir/CombineLine.txt
javac GetAllSCSegs.java
java GetAllSCSegs $outdir/genome.masked_all.fasta $outdir/CombineLine.txt $outdir/genome.txt $outdir/AllSegsOfSCN_CRMasked.fasta $outdir/AllSegsOfSCN_CRRemoved.fasta 25
javac PutativeSDsSeparated.java
java PutativeSDsSeparated $threads $outdir/AllSegsOfSCN_CRRemoved.fasta $outdir/AllSegsOfSCN_CRMasked.fasta $outdir
chmod +x Lastz.sh
./Lastz.sh $threads
wait
cat $outdir/AllSegsOfSCN_CRRemoved*.info > $outdir/AllSegsOfSCN.info
javac MinimapPost.java
java MinimapPost $outdir/AllSegsOfSCN_CRMasked.fasta $outdir/AllSegsOfSCN.info $outdir/SCN_MinimapResult.txt
javac FilterAlignmentsWith500NonCR.java
java FilterAlignmentsWith500NonCR $outdir/SCN_MinimapResult.txt $outdir/SCN_MinimapResult_500NonCR.txt 
javac SortPairs.java
java SortPairs $outdir/SCN_MinimapResult_500NonCR.txt $outdir/SCN_MinimapResult_500NonCR_sortPair.txt 
sort -n -k 5 -k 6 -k 7 -k 8 -k 9 -k 10 -k 11 -k 12 -k 14 $outdir/SCN_MinimapResult_500NonCR_sortPair.txt > $outdir/SCN_MinimapResult_500NonCR_sortPair.sorted.txt
javac FilterRepetitivePairs.java
java FilterRepetitivePairs $outdir/SCN_MinimapResult_500NonCR_sortPair.sorted.txt $outdir/SCN_MinimapResult_500NonCRfiltered.txt 
javac Recover.java
java Recover $outdir/SCN_MinimapResult_500NonCRfiltered.txt $outdir/SCN_MinimapResult_500NonCR_FilterPair.txt 
javac NewExtendPec50.java
java NewExtendPec50 $outdir/genome.masked_all.fasta $outdir/genome.txt $outdir/SCN_MinimapResult_500NonCR_FilterPair.txt $outdir/SCN_MinimapResult_500NonCR_NewExtendPec50.txt
javac changeFormatPairwise.java
java changeFormatPairwise $outdir/genome.masked_all.fasta $outdir/genome_size_Indexes.txt $outdir/SCN_MinimapResult_500NonCR_NewExtendPec50.txt
echo End pairwise SDs detection. Begin analyze mosaic SDs.
javac CopyEndPoints.java
java CopyEndPoints $outdir/genome.masked_all.fasta $outdir/BG_SDIndexes.fasta $outdir/SCN_MinimapResult_500NonCR_NewExtendPec50.txt $outdir/EndpointsLine.txt
sort -n -k 1 -k 2 -k 3 $outdir/BG_SDIndexes.fasta > BG_SDIndexes.sorted.fasta
javac MergeBG.java
java MergeBG BG_SDIndexes.sorted.fasta $outdir/BG_MosaicSDs.fasta
javac ElementSDs.java
java ElementSDs $outdir/genome.masked_all.fasta $outdir/EndpointsLine.txt $outdir/BG_MosaicSDs.fasta
javac testLength.java
java testLength $outdir/genome.masked_all.fasta $outdir/SCN_MinimapResult_500NonCR_NewExtendPec50.txt
javac NewElementSDsPair.java
java NewElementSDsPair $outdir/genome.masked_all.fasta $outdir/genome.txt $outdir/ClustersLine.txt $outdir/ElementSDs_100.fasta $outdir/SCN_MinimapResult_500NonCR_NewExtendPec50.txt $outdir
javac SDblock.java
java SDblock $outdir/ElementSDs_100.fasta $outdir/ElementSDs_pairwiseEqual.fasta $outdir
javac ElementSDsMulti.java
java ElementSDsMulti $outdir/genome_size_Indexes.txt $outdir/ElementSDs_100.fasta $outdir/SCN_MinimapResult_500NonCR_NewExtendPec50.txt $outdir/ElementSDs_LengthAndMulti.fasta
javac MosaicSDsBlockcompose.java
java MosaicSDsBlockcompose $outdir/genome_size_Indexes.txt $outdir/MosaicSDs_SDblockIndexes.txt $outdir/ElementSDs_LengthAndMulti.fasta $outdir/blocks.fasta $outdir/BG_MosaicSDs.fasta
mkdir $outdir/tmp
mv $outdir/ElementSDs*.fasta $outdir/tmp/
mv $outdir/length.fasta $outdir/tmp/
mv $outdir/Clusters_100.fasta $outdir/tmp/
mv $outdir/BadMosaicSD_100.fasta $outdir/tmp/
mv $outdir/BG*.fasta $outdir/tmp/
mv $outdir/AllSegsOfSCN*.fasta $outdir/tmp/
mv $outdir/*.fasta $outdir/tmp/
mv $outdir/genome*.txt $outdir/tmp/
mv $outdir/Genome_size_Indexes.txt $outdir/tmp/
mv $outdir/*.h5 $outdir/tmp/
mv $outdir/FrequencyLine.txt $outdir/tmp/
mv $outdir/CombineLine.txt $outdir/tmp/
mv $outdir/EndpointsLine.txt $outdir/tmp/
mv $outdir/SCN_LastzResult_500NonCR_NewExtendPec50.txt $outdir/tmp/
mv $outdir/ClustersLine.txt $outdir/tmp/
rm -r $outdir/tmp
echo end SDquest analysis