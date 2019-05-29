/*
CSC139 
Spring 2019
Third Assignment
Karnam Rajendraprasad, Hari Chandana
Language: JAVA
Section 01#
*/
import java.io.File;
import java.io.PrintWriter;
import java.util.*;
public class Cpuscheduling {
	private Scanner scan;
	 private PrintWriter toFile;
	 private int Scheduling_order;//using three types of ordering   
	 private static final int order_by_priority=3,order_by_burst_time=2,order_by_arrival_time=1;
	 private int no_of_processes;
     private data processes[];
	 public static void main(String[] args) {
	        Cpuscheduling cpu = new Cpuscheduling();
	        String file_name=args[0];
	        try {//Create a Scanner object from input file, throws a exception when file not found
	            cpu.scan = new Scanner(new File(file_name));
	        } catch (Exception e) {
	            System.out.println("file not found");
	        }
	        try{
	            cpu.toFile=new PrintWriter("output.txt","UTF-8");
	        }
	        catch(Exception e) {
	            System.out.println("file not created");
	        }
	        cpu.Read_Input();
	        cpu.toFile.close();
	       }
    @SuppressWarnings("resource")
	private void Read_Input() {
    	String scheduler=null;
    	int time_quanta = 0;
        Scanner firstLine = new Scanner(scan.nextLine());
        Scanner secondLine = new Scanner(scan.nextLine());
        while (firstLine.hasNext()) {
            scheduler = firstLine.next();
            if (scheduler.equals("RR")) {
                time_quanta = firstLine.nextInt();
            }
        }
        no_of_processes = secondLine.nextInt();
        processes = new data[no_of_processes];
        for (int i = 0; i < no_of_processes; i++) {
            Scanner thisLine = new Scanner(scan.nextLine());
            processes[i] = new data(thisLine.nextInt(), thisLine.nextInt(), thisLine.nextInt(), thisLine.nextInt());
        }
        if(scheduler.equals("RR")) {
        	 toFile.println("RR"+" "+time_quanta);
        	scheduler_RR(time_quanta);
        }
        else if(scheduler.equals("SJF")){
            toFile.println("SJF");
            scheduler_SJF();
        }
        else if(scheduler.equals("PR_noPREMP")){
            toFile.println("PR_noPREMP");
            implementPR_noPREMP();
        }
        else if(scheduler.equals("PR_withPREMP")){
            toFile.println("PR_withPREMP");
            implementPR_withPREMP();
        }
        }
    private void average_waiting_time(){
        int total_waiting_time=0;
        for(int i=0;i<no_of_processes;i++)
        {
            total_waiting_time+=processes[i].waitingTime;//add waiting time of each process
            processes[i].reset();
        }
        toFile.println("AVG Waiting Time: "+(double)total_waiting_time/(double)no_of_processes);
    }
    private void scheduler_RR(int time_quanta){
        Queue<data> ready_queue=new LinkedList<data>();//A ready queue has been declared of type data
        Scheduling_order=order_by_arrival_time;//Ordering process list according to arrival time
        Arrays.sort(processes);//sort the arrays according to the order_mode
        int last=-1,time=0,process_done=0;//variables to hold time, previous states and job status
        data running_in_cpu=null;//initially process in cpu is null
        while(process_done<no_of_processes){//Run until all processes are done
            for(int i=0;i<processes.length;i++)//loading processes into ready queue based on arrival time
            {
                if(last<processes[i].arrival_time && processes[i].arrival_time<=time)
                    ready_queue.add(processes[i]);//since processes has been sorted according to their arrival time
            }
            if(running_in_cpu!=null && running_in_cpu.completed!=0)//loading process in cpu at the end of ready queue based upon its completion status
            {
                ready_queue.add(running_in_cpu);
            }
            running_in_cpu=ready_queue.remove();//Giving CPU to process at the front of queue
            last=time;//storing current status into variables for calculation purposes
            toFile.println(time+" "+running_in_cpu.pid);// printing cpu status at current time
            time+=running_in_cpu.work(time_quanta,time);//updating time variable
            if(running_in_cpu.completed==0) {//updating number of process completed
                process_done++;
                running_in_cpu.waiting_time(time-running_in_cpu.burst_time-running_in_cpu.arrival_time);//update waiting time for each process
            }
        }
        average_waiting_time();// calculating average waiting time
    }
    @SuppressWarnings("unchecked")
	private void scheduler_SJF(){
        List<data> ready_queue =new ArrayList<data>();
        Scheduling_order=order_by_burst_time;//sort by burst time because shortest job should has priority
        Arrays.sort(processes);
        data running_in_cpu=null;
        int process_done=0,time=0,last=-1;
        while(process_done<no_of_processes){
            for(int i=0;i<no_of_processes;i++)
            {
                if(last<processes[i].arrival_time && processes[i].arrival_time<=time)
                    ready_queue.add(processes[i]);//Load process according to the arrival time and burst time
            }
            Collections.sort(ready_queue);
            running_in_cpu=ready_queue.remove(0);
            last=time;
            toFile.println(time+" "+running_in_cpu.pid);
            time+=running_in_cpu.work(0,time);
            if(running_in_cpu.completed==0) {
                process_done+=1;
                running_in_cpu.waiting_time(time-running_in_cpu.burst_time-running_in_cpu.arrival_time);
            }
        }
        average_waiting_time();
    }
    @SuppressWarnings("unchecked")
	private void implementPR_noPREMP(){
        List<data> ready_queue =new ArrayList<data>();
        Scheduling_order=order_by_arrival_time;
        Arrays.sort(processes);
        data running_in_cpu=null;
        int process_done=0,time=0,last=-1;
        while(process_done<no_of_processes){
            for(int i=0;i<no_of_processes;i++)
            {
                if(last<processes[i].arrival_time && processes[i].arrival_time<=time)
                    ready_queue.add(processes[i]);
            }
            Scheduling_order=order_by_priority;
            Collections.sort(ready_queue);
            running_in_cpu=ready_queue.remove(0);
            last=time;
            toFile.println(time+" "+running_in_cpu.pid);
            time+=running_in_cpu.work(0,time);
            if(running_in_cpu.completed==0) {
                process_done+=1;
                running_in_cpu.waiting_time(time-running_in_cpu.burst_time-running_in_cpu.arrival_time);
            }
        }
        average_waiting_time();
    }
    @SuppressWarnings("unchecked")
	private void implementPR_withPREMP(){
        List<data> ready_queue =new ArrayList<data>();
        Scheduling_order=order_by_arrival_time;
        Arrays.sort(processes);
        data running_in_cpu=null;
        int process_done=0,time=0,last=-1;
        while(process_done<no_of_processes){
            for(int i=0;i<no_of_processes;i++)
            {
                if(last<processes[i].arrival_time && processes[i].arrival_time<=time)
                    ready_queue.add(processes[i]);
            }
            if(running_in_cpu!=null && running_in_cpu.completed!=0)
            {
                ready_queue.add(running_in_cpu);
            }
            Scheduling_order=order_by_priority;
            Collections.sort(ready_queue);
            running_in_cpu=ready_queue.remove(0);
            int timeTaken=time+running_in_cpu.completed;
            int interruptTime=0;
            for(int i=0;i<no_of_processes;i++)
            {
                if(last<processes[i].arrival_time && processes[i].arrival_time<timeTaken && processes[i].priority<running_in_cpu.priority)
                {
                    interruptTime=processes[i].arrival_time-time;
                    break;
                }
            }
            last=time;
            toFile.println(time+" "+running_in_cpu.pid);
            time+=running_in_cpu.work(interruptTime,time);
            if(running_in_cpu.completed==0) {
                process_done+=1;
                running_in_cpu.waiting_time(time-running_in_cpu.burst_time-running_in_cpu.arrival_time);
            }
        }
        average_waiting_time();
    }
    @SuppressWarnings("rawtypes")
	private class data implements Comparable {
        int pid;
        int arrival_time;
        int burst_time;
        int priority;
        int completed;
        int waitingTime;

        data(int pid, int arrival_time, int burst_time, int priority) {
            this.pid = pid;
            this.arrival_time = arrival_time;
            this.burst_time = burst_time;
            this.priority = priority;
            this.completed=burst_time;
            this.waitingTime=0;
        }
        public void reset()
        {
            waitingTime=0;
            completed=burst_time;
        }
        public int work(int time_quanta,int time)
        {
            int retTime=0;
            if(time_quanta>0) {
                if(time_quanta<=completed) {
                    completed -= time_quanta;
                    retTime=time_quanta;
                }
                else
                {
                    retTime=completed;
                    completed=0;
                }
                return retTime;
            }
            else
            {
                retTime=completed;
                completed=0;
            }
            return retTime;
        }
        public void waiting_time(int waitingTime){
            this.waitingTime=waitingTime;
        }
        public int compareTo(Object p) {
            switch (Scheduling_order) {
                case 1:
                    if (this.arrival_time > ((data) p).arrival_time)
                        return 1;
                    else if (this.arrival_time < ((data) p).arrival_time)
                        return -1;
                    else
                        return 0;
                case 2:
                        if (this.burst_time > ((data) p).burst_time)
                            return 1;
                        else if (this.burst_time < ((data) p).burst_time)
                            return -1;
                        else
                            return 0;
                case 3:
                    if (this.priority > ((data) p).priority)
                        return 1;
                    else if (this.priority < ((data) p).priority)
                        return -1;
                    else
                        return 0;
                default:
                    return 0;
            }
        }
    }
}