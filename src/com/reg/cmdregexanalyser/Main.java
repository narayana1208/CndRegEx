package com.reg.cmdregexanalyser;



import com.reg.cmdregexanalyser.MainFrame;

public class Main 
{
	public static final long sessionStart = System.currentTimeMillis();
	
	
	
	public static void main(String[] args) throws Exception 
	{
		// TODO Auto-generated method stub
		System.out.println(" Session Started \t"+sessionStart);
		MainFrame mainFrame = new MainFrame();
		
		mainFrame.startBackgroundTasks();

	}

}
