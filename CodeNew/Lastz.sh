#! /bin/bash
number="$1"
#b=$(number)
#echo $b
for ((i=1; i<=number; i++)); do
	./lastz-distrib/bin/lastz AllSegsOfSCN_CRRemoved.fasta[multiple] AllSegsOfSCN_CRRemoved$i.fasta --format=general:cigar,score,name1,strand1,size1,zstart1,end1,name2,strand2,size2,zstart2,end2,identity,coverage,nmatch,nmismatch,ngap,cgap > AllSegsOfSCN_CRRemoved$i.info &
done
wait
echo end lastz alignments of putative SDs
