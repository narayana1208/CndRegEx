package com.reg.cmdregexanalyser.regex;

import com.reg.cmdregexanalyser.regex.Pattern;
import com.reg.cmdregexanalyser.regex.Statistics;
import com.reg.cmdregexanalyser.regex.Matcher;
import java.util.ArrayList;
import java.util.List;



public class Statistics 
{
	public Statistics(int from, int to, int pos, CharSequence seq) 
	{
		this.from = from;
		this.to = to;
		this.pos = pos;
		this.seq = seq;
		level = 0;
		trace = new ArrayList<MatchTest>();
		first = -1;
	}

	public int getLevel() 
	{
		return level;
	}

	public void incLevel() 
	{
		++level;
	}

	public void decLevel() 
	{
		--level;
	}

	public void setResult(int first, int last) 
	{
		this.first = first;
		this.last = last;
	}

	public int getFirst() 
	{
		return first;
	}

	public int getLast() 
	{
		return last;
	}

	public List<MatchTest> getTrace() 
	{
		return trace;
	}

	public static class Match 
	{
		public Match(int i, int j, int from, int to, CharSequence cs) 
		{
			this.i = i;
			this.j = j;
			this.from = from;
			this.to = to;
			this.cs = cs;
		}

		@Override
		public String toString() 
		{
			if (j == 0) 
			{
				return String.format("match %d", i);
			}
			return String.format("group %d", j);
		}

		public String getText() 
		{
			return cs.subSequence(from, to).toString();
		}

		public int getFrom() 
		{
			return from;
		}

		public int getTo() 
		{
			return to;
		}

		private final int i;
		private final int j;
		private final int from;
		private final int to;
		private final CharSequence cs;
	}

	/**
	 * Result of a find() or match() operation.
	 * Contains the tree of matched result, the text created from replacement
	 * and the list created from splitting.
	 */
	

	/**
	 * helper function to perform multiple find() operations with the supplied
	 * matcher and create a TreeResult.
	 */
	
	/**
	 * Helper function to perform a match() operation with the supplied matcher.
	 * The tree result will contain a match tree (with one match entry, if any),
	 * and a replacement text result, but no split result list.
	 * @return 
	 */
	public static  void matchAsTree(Matcher m) 
	{
		//System.out.println("Inside MAtch as Tree");
		StringBuffer repl = new StringBuffer();
		if (m.matches()) 
		{
			//Match m1=new Match(1, 0, m.start(), m.end(), m.text);
			//System.out.println(m1.i+"\t"+m1.j+"\t"+m1.from+"\t"+m1.to+"\t"+m1.cs);
			int j;
			for (j = 1; j <= m.groupCount(); ++j) 
			{
				//Match m2 = new Match(0, j, m.start(j), m.end(j), m.text);
				//System.out.println(m2.i+"\t"+m2.j+"\t"+m2.from+"\t"+m2.to+"\t"+m2.cs);
			}
			//m.appendReplacement(repl);
		} 
		else 
		{
			m.appendTail(repl);
			System.out.println("No Match");
		}
	}

	static void getPatternAsTree(Pattern.Node root)
	{
		
		System.out.println("Node CLass Name :-"+getNodeClassName(root.getClass()));
	
		for (Pattern.Node c : root.getChilds()) 
		{
			if (c != null) 
			{
				//System.out.println(getNodeClassName(c.getClass()));
				getPatternAsTree(c);
				
			}
		}
	}


	static String getNodeClassName(@SuppressWarnings("rawtypes") Class nc) 
	{
		return nc.getName().replace(
				Pattern.class.getName()+"$", "");
	}

	MatchTest newMatchTest(Pattern.Node node, int pos) 
	{
		MatchTest m = new MatchTest(node, getLevel(), pos);
		trace.add(m);
		return m;
	}

	public static class MatchTest 
	{
		private MatchTest(Pattern.Node node, int level, int pos) 
		{
			this.node = node;
			this.level = level;
			this.pos = pos;
		}

		public void setResult(boolean result) 
		{
			this.result = result;
		}

		public String getIndent() 
		{
			StringBuilder r = new StringBuilder();
			int i;
			for (i = 0; i < level; ++i) 
			{
				r.append("   ");
			}
			return r.toString();
		}

		@Override
		public String toString() 
		{
			return String.format("%8d%s%s = %s",
                pos,
                getIndent(),
                getNodeClassName(),
                (result)?("true"):("false"));
		}

		public String getNodeClassName() 
		{
			return Statistics.getNodeClassName(node.getClass());
		}

		public Object getNode() 
		{
			return node;
		}

		public int getLevel() 
		{
			return level;
		}

		public boolean getResult() 
		{
			return result;
		}	

		public int getPos() 
		{
			return pos;
		}

		private final Pattern.Node node;
		private final int level;
		private boolean result;
		private final int pos;
	}

	public static class TraceTableModel
	{
    
		public TraceTableModel(List<Statistics> statistics) 
		{
			this.statistics = statistics;
        
			rows = 0;
			offset = new int[statistics.size()+1];
			int i = 0;
			for (Statistics s : statistics) 
			{
				offset[i++] = rows;
				List<MatchTest> t = s.getTrace();
				rows += t.size();
			}
			offset[i] = rows;
		}

		public int getRowCount() 
		{
			return rows;
		}

		public int getColumnCount() 
		{
			// Trace | Op | Level | NodeClassName | Pos | Result
			return 6;
		}

		public String getColumnName(int columnIndex) 
		{
			switch (columnIndex) 
			{
				case 0:
					return "Trace";

				case 1:
					return "Op";

				case 2:
					return "Level";

				case 3:
					return "NodeClass";

				case 4:
					return "Pos";

				case 5:
					return "Res";
			}
			return "?";
		}

		public int getTraceIndex(int rowIndex) 
		{
			int i = 0;
			while ((i < offset.length) && (offset[i] <= rowIndex)) 
			{
				++i;
			}
			--i;
			return i;
		}

		public MatchTest getMatchTest(int rowIndex) 
		{
			int i = getTraceIndex(rowIndex);
			if ((i >= 0) && (i < statistics.size())) 
			{
				List<MatchTest> t = statistics.get(i).getTrace();
				return t.get(rowIndex-offset[i]);
			}
			return null;
		}

		public Object getValueAt(int rowIndex, int columnIndex) 
		{
			int i = getTraceIndex(rowIndex);
			MatchTest mt = getMatchTest(rowIndex);
			switch (columnIndex) 
			{
            	case 0:
            		return new Integer(i+1);

            	case 1:
            		return new Integer(rowIndex-offset[i]+1);

            	case 2:
            		return new Integer(mt.getLevel());

            	case 3:
            		return mt.getNodeClassName();

            	case 4:
            		return new Integer(mt.getPos());

            	case 5:
            		return mt.getResult()?"T":"F";
			}
			return "?";
		}

		private List<Statistics> statistics;
		private int[] offset;
		private int rows;
	}

	static TraceTableModel getTraceAsTable(List<Statistics> statistics) 
	{
		return new TraceTableModel(statistics);
	}



	private List<MatchTest> trace;
	private int level;
	@SuppressWarnings("unused")
	private final CharSequence seq;
	@SuppressWarnings("unused")
	private int from;
	@SuppressWarnings("unused")
	private int to;
	@SuppressWarnings("unused")
	private int pos;
	private int first;
	private int last;
}
