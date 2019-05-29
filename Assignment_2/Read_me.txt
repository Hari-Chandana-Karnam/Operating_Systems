Your program will take the following three command-line arguments:
1.	The array size (a positive integer between 1 and 100 000 000)
2.	The number of threads  (a positive integer between 1 and 16)
3.	The index at which a zero must be placed in the input array. If this index is negative, don’t put a zero in the array.   


Use the following command to compile your code:
g++ -O3 MTFfindMin.c -lpthread -o MTFindMin
Note that we are using the –O3 option to enable a high level of compiler optimization. 
