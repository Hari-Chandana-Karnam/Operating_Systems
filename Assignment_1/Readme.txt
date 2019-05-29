The producer program (parent) takes the following three command-line arguments (all integers):

The size of the bounded buffer (bufSize)-This is the number of items that can fit in the bounded buffer. This cannot exceed 1000. Your program should check if the value of this-command line argument is within the valid range (0 to 1000); if not, it should print an appropriate error message and terminate.

  
The number of items to produce/consume (itemCnt). In an interesting test case, itemCnt will be much larger than bufSize. However, your program must correctly handle test cases in which itemCnt is less than bufSize. Make sure that you test your program on such test cases.

A seed for the random number generator (randSeed). This will allow us to test the program using different data.

use the following commands to compile the two programs and build the two executables:
gcc producer.c -lrt -o producer
gcc consumer.c -lrt -o consumer
