Devin Durham
d1durham
TCSS435
6/9/17

README

Implementations Notes:
My tree has a depth of only 2. my computer counldnt handle such a massive tree and I didnt have time to design the program in a way that allows to change depth. i did however comment out code in the makeTreeHuman() mothod that will expand to a deoths of 3.
The program is currently using the MinMax with pruning. Simply comment out code (neaqr Line 80) to change to regular MinMax

MinMax:
   # of nodes expanded: 96705
   Depth: 2
   Time: O(b^n) where b is the branching factor and n is the max depth
   Space: O(b^n) where b is the branching factor and n is the max depth

MinMax with Pruning:
   # of nodes expanded: 8248
   Depth: 2
   Time: O(b^n) where b is the branching factor and n is the max depth
   Space: O(b^n) where b is the branching factor and n is the max depth