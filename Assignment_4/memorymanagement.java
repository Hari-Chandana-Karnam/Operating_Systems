/*
CSC139 
Spring 2019
Fourth Assignment
Karnam Rajendraprasad, Hari Chandana
Language: JAVA
Section 01#
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
public class memorymanagement {
	private Scanner scan;
	 private static PrintWriter toFile;
	 private int pages,total_frames,total_page_requests;
	 ArrayList<Integer> page_requests = new ArrayList<Integer>();
	 public static void main(String[] args) {
	        memorymanagement mem = new memorymanagement();
	        String file_name=args[0];
	        try {//Create a Scanner object from input file, throws a exception when file not found
	            mem.scan = new Scanner(new File(file_name));
	        } catch (Exception e) {
	            toFile.println("file not found");
	        }
	        try{
	            mem.toFile=new PrintWriter("output.txt","UTF-8");
	        }
	        catch(Exception e) {
	            toFile.println("file not created");
	        }
	         mem.Read_Input();
	         mem.toFile.close();
	       }
 @SuppressWarnings("resource")
	private void Read_Input() {
     Scanner firstLine = new Scanner(scan.nextLine());
     while (firstLine.hasNext()) {
     	pages = firstLine.nextInt();
     	total_frames = firstLine.nextInt();
     	total_page_requests = firstLine.nextInt();
     }
     for (int i = 0; i < total_page_requests; i++) {
         Scanner thisLine = new Scanner(scan.nextLine());
         page_requests.add(thisLine.nextInt());
     }
     	FIFO(page_requests,pages,total_frames,total_page_requests);
		Optimal(page_requests,pages,total_frames,total_page_requests);
		LRU(page_requests,pages,total_frames,total_page_requests);
     }

	public static void FIFO(ArrayList<Integer> page_requests, int pages, int total_frames, int total_page_requests) {
		int[] frames = new int[total_frames];
    	int flag=0,count=0,frame_to_unload=0,page_fault=0,current_page_request=0;
    	toFile.println("FIFO");
    	for(int i=0;i<total_page_requests;i++){
    	flag=0;
    	current_page_request=page_requests.get(i);
    	for(int k=0;k<total_frames;k++)
    		if(frames[k]==current_page_request){
    			toFile.println("Page "+current_page_request+" already in Frame "+k);
    			flag=1;
    		}
    		if(flag==0){//start loading pages into frames
    			if(count==total_frames){//wait until all frames are used to start unloading
    				toFile.printf("Page %d unloaded from Frame %d, ",frames[frame_to_unload],frame_to_unload);
    				count--;
    			}
    			frames[frame_to_unload]=current_page_request;
    			toFile.printf("Page %d loaded into Frame %d\n",current_page_request,frame_to_unload);
    			frame_to_unload=(frame_to_unload+1) % total_frames;
    			page_fault++;
    			count++;
    		}
    	}
    	toFile.println(page_fault+" page faults\n");
	}
	
	public static void LRU(ArrayList<Integer> page_requests, int pages, int total_frames, int total_page_requests) {
		ArrayList<Integer> frames = new ArrayList<Integer>();
		ArrayList<Integer> last_used_frame = new ArrayList<Integer>();
		int page_faults = 0,available_frames = total_frames;
		int frame_to_unload = 0,least_recently_used_frame = total_page_requests;
		toFile.println("LRU");
		for (int i = 0; i < total_frames; i++) {
			frames.add(i, -1);
			last_used_frame.add(i, total_page_requests-1);
		}
		for (int i = 0; i < total_page_requests; i++) {
			int current_page_request = page_requests.get(i);
			if (frames.contains(current_page_request)) {
				toFile.println("Page " + current_page_request + " already in Frame " + frames.indexOf(current_page_request));
			}
			else if (available_frames > 0) {
				frame_to_unload = frames.indexOf(-1);
				frames.set(frame_to_unload, current_page_request);
				page_faults++;
				available_frames--;
				toFile.println("Page " + current_page_request + " loaded into Frame " + frame_to_unload);
			} else {
				least_recently_used_frame = total_page_requests;
				for (int j = 0; j < total_frames; j++) {
					for (int k = i; k >= 0; k--) {
						if (frames.get(j) == page_requests.get(k)) {
							last_used_frame.set(j, k); 
							break;
						}
					}
				}
				for (int j = 0; j < total_frames; j++) {
					if (last_used_frame.get(j) < least_recently_used_frame) {
						least_recently_used_frame = last_used_frame.get(j);
					}
				} 
				frame_to_unload = frames.indexOf(page_requests.get(least_recently_used_frame));
				toFile.print("Page " + frames.get(frame_to_unload) + " unloaded from Frame " + frame_to_unload + ", ");
				frames.set(frame_to_unload, current_page_request);
				page_faults++;
				toFile.println("Page " + current_page_request + " loaded into Frame " + frames.indexOf(current_page_request));
			}
		}
		toFile.println(page_faults + " page faults\n");
	}
	
	public static void Optimal(ArrayList<Integer> page_requests, int pages, int total_frames, int total_page_requests) {
		ArrayList<Integer> frames = new ArrayList<Integer>();
		ArrayList<Integer> future_use_frame = new ArrayList<Integer>();
		final int INFINITY = total_frames+1;
		int page_fault = 0,available_frames = total_frames,frame_to_unload = 0,least_future_use_frame = total_page_requests;
		toFile.println("Optimal");
		for (int i = 0; i < total_frames; i++) {
			frames.add(i, -1);
			future_use_frame.add(i, 0);
		}
		for (int i = 0; i < total_page_requests; i++) {
			int currentPage = page_requests.get(i);
			if (frames.contains(currentPage)) {
				toFile.println("Page " + currentPage + " already in Frame " + frames.indexOf(currentPage));
			}
			else if (available_frames > 0) {
				frame_to_unload = frames.indexOf(-1);//gets the first available frame
				frames.set(frame_to_unload, currentPage);
				page_fault++;
				available_frames--;
				toFile.println("Page " + currentPage + " loaded into Frame " + frame_to_unload);
			} 
			else {
				for (int j = 0; j < total_frames; j++) {
					for (int k = i; k < total_page_requests; k++) {
						if (frames.get(j) == page_requests.get(k)) {
							future_use_frame.set(j, k); //if there is an occurrence set the index of the frame for future use 
							break;
						} else {
							future_use_frame.set(j, INFINITY);//if not set, future use as highest+1
						}
					}
				}
				least_future_use_frame = 0;
				for (int j = 0; j < total_frames; j++) { 
					if (future_use_frame.get(j) == INFINITY) {
						frame_to_unload = j;
						least_future_use_frame = INFINITY;
						break;
					} else if (future_use_frame.get(j) > least_future_use_frame) {
						least_future_use_frame = future_use_frame.get(j);
					}
				}
				if (least_future_use_frame != INFINITY) {
					frame_to_unload = frames.indexOf(page_requests.get(least_future_use_frame));
				}
				toFile.print("Page " + frames.get(frame_to_unload) + " unloaded from Frame " + frame_to_unload + ", ");
				frames.set(frame_to_unload, currentPage);
				page_fault++;
				toFile.println("Page " + currentPage + " loaded into Frame " + frames.indexOf(currentPage));
			}
		}
		toFile.println(page_fault + " page faults\n");
	}
}