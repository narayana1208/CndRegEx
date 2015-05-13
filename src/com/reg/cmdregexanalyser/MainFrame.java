package com.reg.cmdregexanalyser;


import com.reg.cmdregexanalyser.RegExWorker;
import com.reg.cmdregexanalyser.regex.Pattern;
import com.reg.cmdregexanalyser.regex.PatternSyntaxException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;



public class MainFrame 
{
    // session-unique window instance id
    //private static int nextInstanceId = 0;
    //private final int instanceId;
    
    // recent test text files
  

    private String RegEx;
    private String TestText;
    //private RegExWorker.Result lastResult;
    
    private LinkedBlockingDeque<RegExWorker.Task> backgroundMatching;
    private Thread backgroundMatcher;
  
	public MainFrame() throws FileNotFoundException 
    {
     
        //instanceId = ++nextInstanceId;

        
        backgroundMatching = new LinkedBlockingDeque<RegExWorker.Task>();
        
        RegEx =getRegEx();
        TestText=importTestText();

        triggerMatching(RegEx,TestText);

        // prepare background matcher
        backgroundMatcher = new Thread(new RegExWorker(backgroundMatching));
        backgroundMatcher.setDaemon(true);
    }

   public String getRegEx() throws FileNotFoundException 
   {
	   Scanner input=new Scanner(new BufferedReader(new FileReader("/home/narayanar/workspace/CmdRegExp/src/stockprices.txt")));
		//String st=new String();
		StringBuilder sb=new StringBuilder();
		//st=input.nextLine();
		//System.out.println(" The line from file is "+st);
		sb=sb.append(input.nextLine());
		while(input.hasNextLine())
		{
			
			sb=sb.append(input.nextLine()+System.lineSeparator());
			//sb.append('\n');
		}
		input.close();
		String regex=sb.toString();
		if(regex.contains("\n"))
		{
			System.out.println("\nRegex contains new line character\n");
		}
	       
	        System.out.println("\nRegular Expression String is:  \n"+regex);
	        return (regex == null)?(""):(regex);
    }
   
   private String importTestText() throws FileNotFoundException 
   {
	   Scanner TestText=new Scanner(new BufferedReader(new FileReader("/home/narayanar/workspace/CmdRegExp/src/stockprices1.txt")));
		//String st=new String();
		StringBuilder sb=new StringBuilder();
		//st=input.nextLine();
		//System.out.println(" The line from file is "+st);
		while(TestText.hasNextLine())
		{
			
			sb=sb.append(TestText.nextLine()+System.lineSeparator());
			//sb.append('\n');
		}
		TestText.close();
		System.out.println("\nThe File Contents are\n ");
		System.out.println(sb);
		return sb.toString();
	
   }
   
	private void triggerMatching(String RegEx,String TestText) 
	{
		// TODO Auto-generated method stub
		//System.out.println("Inside Trigger Matching");
		 Pattern p = null;
		 try 
	        {
	            p = compilePattern(RegEx);
	        } 
		    catch (PatternSyntaxException e) 
	        {
	          System.out.println("Pattern Syntax exception \t"+e.getMessage());
	         
	        }
		 
		 RegExWorker.Task t = new RegExWorker.Task(p,TestText,"match");
		 
		 
		 try {
	            backgroundMatching.clear();
	            backgroundMatching.putFirst(t);
	           // System.out.println("Inside background matching thread task ");
	        } catch (InterruptedException e) {
	            System.out.println("warning: unexpected interrupt in enqueue");
	        }
		 
		 
	}   
	private Pattern compilePattern(String regEx) throws PatternSyntaxException 
    {
    	//System.out.println(" Inside Compile Pattern \nThe passed RegEx is :\n"+regEx);
    	if(regEx.contains("\n"))
    	{
    		//System.out.println("Inside Multi Line");
    		return Pattern.compile(regEx,Pattern.MULTILINE);
    	}
    	else
    	{
    		//System.out.println("Inside Single Line");
    		return Pattern.compile(regEx);
    	}
    }
	public void startBackgroundTasks() 
	{
		// TODO Auto-generated method stub
		//System.out.println("Inside Back Ground Tasks");
		backgroundMatching.clear();
        backgroundMatcher.start();
      
	}

	public void dispose() 
    {
        
        // enqueue background matcher shutdown task
        try 
        {
            backgroundMatching.clear();
            backgroundMatching.putFirst(RegExWorker.Task.SHUTDOWN);
        }
        catch (InterruptedException e) 
        {
            System.out.println("warning: unexpected interrupt in enqueue");
        }
        
      
    }
	
	
	
	
}
