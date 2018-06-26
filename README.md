# Bioinformatics
Implementation of various algorithms from bioinformatics

## Sequence Alignment
Genetic sequences can be represented as vectors of chars (e.g. AGCT for DNA). The alignment of sequences reveal sites with the same ancestry.   
```
GlobalSequenceAlignment.java
```
Implements a dynamic programming algorithm for global sequence alignment based on a scoring matrix.

## Phylogeny
Phylogeny is about taking multiple alignments of genome sequences and inferring an evolutionary relationship between them. These allow the creation of evolutionary trees that tell us about the common ancestors between species, where they diverged and what the relationships between them are.    

The Neighbour Joining algorithm is provided in:
```
NeighbourJoinMain.java
```
It always produces a tree if the input matrix is additive. Otherwise, it provides a heuristic for such a tree.

## Exact Pattern Matching
Suffix Trie
