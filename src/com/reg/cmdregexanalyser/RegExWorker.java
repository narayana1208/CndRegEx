package com.reg.cmdregexanalyser;

import java.util.concurrent.BlockingDeque;

import com.reg.cmdregexanalyser.regex.*;
import com.reg.cmdregexanalyser.regex.Statistics.*;
import com.reg.cmdregexanalyser.regex.Statistics.TraceTableModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

@SuppressWarnings("unused")
public class RegExWorker implements Runnable
{

	private final BlockingDeque<Task> taskQueue;
	private Task t;
	public RegExWorker(BlockingDeque<Task> taskQueue) 
    {
		//System.out.println("Inside Reg Ex Worker Constructor");
        this.taskQueue = taskQueue;
       // System.out.println("taskqueue size is"+taskQueue.size()+taskQueue.getFirst());
        t=taskQueue.getFirst();
        process(t);
    }

	
	 
	 public void run() {
	        while (true) {
	            try {
	            	//System.out.println("Inside Thread Run");
	                //Task t = taskQueue.getFirst();
	               // System.out.println("After task in thread run");
	               /** if (t.mode.equals(Task.SHUTDOWN.mode)) 
	                {
	                	System.out.println("Inside Task Shutdown mode");
	                    return;
	                }**/
	                //System.out.println("in run task details "+t.mode+t.p);
	                 process(t);
	                // System.out.println("Inside Process");
	                 Pattern p1=t.p;
	                 String text=t.text;
	                 Matcher m = new Matcher(p1,text);
	     	        //Matcher m = t.p.matcher(t.text);
	     	       // System.out.println("After MAtcher");
	     	        
	     	        if (t.mode.equals("match")) 
	     	        {
	     	             Statistics.matchAsTree(m);
	     	        }
	     	    

	     	        Date stop = new Date();
	            } catch (Exception e) {
	                System.out.println("warning: other unexpected exception ("+e.getMessage()+")");
	            }
	        }
	    }


	    public static void process(Task t) 
	    {
	        Date start = new Date();
	        
	        // match the test text
	        //System.out.println("Inside Process original");
	        Matcher m = t.p.matcher(t.text);

	        
	       
	        if (t.mode.equals("match")) 
	        {
	             Statistics.matchAsTree(m);
	        }
	        TraceTableModel ttm =m.getTraceAsTable();
	        
	        display(ttm);
	        Date stop = new Date();
	      
	    }

	    public static void display(TraceTableModel tt)
	    {
	    	int cc=tt.getColumnCount();
	    	int rc=tt.getRowCount();
	    	System.out.println("The number of operations trace took\t"+rc);
	    	for(int i=0;i<cc;i++)
	    	{
	    		System.out.print("\t"+tt.getColumnName(i));
	    	}
	    	System.out.println("\n");
	    	
	    	for(int j=0;j<rc;j++)
	    	{
	    		for(int l=0;l<cc;l++)
	    		{
	    			System.out.print("\t"+tt.getValueAt(j, l));
	    		}
	    		System.out.println("\n");
	    	}
	    	
	    	
	    }
	    public final static class Task 
	    {
	        
	        public final static Task SHUTDOWN = new Task("shutdown");
	        
	        public Task(String mode) 
	        {
	            this.p = null;
	            this.text = null;
	            this.mode=mode;
	        }
	        
	        public Task(Pattern p, String text,String mode) 
	        {
	        	//System.out.println("Inside Task Con");
	            this.p = p;
	            this.text = text;
	            this.mode = mode;
	        }

	        public final Pattern p;
	        public final String text;
	        public final String mode;
	  
	    }

	   
	  
	
}
