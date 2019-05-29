This program reads an input file named “input.txt” and write the results into an output file named “output.txt”. The formats of these files are as follows:
Input File
The first line has the name of the scheduling algorithm to run(RR, SJF, Priority_withpreemption and Priority_no_preemption).  
The second line has a single integer representing the number of processes in the file. 
In the rest of the file, there is one line per process with the following information:
Process number	Arrival Time	CPU burst time	Priority

Output File
output file will show the scheduling results for each of the algorithms listed in the input file. The first line in the output file has the name of the scheduling algorithm. The file then shows the schedule using a simple text format in which there is one line for each CPU assignment (each line corresponds to a vertical line in the Gantt chart). Each line has two numbers: one indicating the time point and one indicating the process number that got the CPU at that point. The last line in the output file shows the average waiting time.

javac Cpuscheduling.java  /// To compile the java code.
java Cpuscheduling input1.txt   //// takes one text file as input 
vim output.txt     //// to view the output