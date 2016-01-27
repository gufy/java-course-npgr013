package cz.cuni.mff.ms.urtax.goophy.cvika.cut;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Cut {
	static protected String DEFAULT_DELIMITER = "\t";
	
	static protected int STATE_BYCOLUMNS = 0;
	static protected int STATE_BYCHARS = 1;
	
	protected int state = 0;
	protected String delimiter;
	protected String params;
	
	public Cut(int state, String params, String delimiter) {
		this.state = state;
		this.params = params;
		this.delimiter = delimiter;
		
		
	}
	
	protected String[] getSeperateChars(String string) {
		String[] result = new String[string.length()];
		for (int i = 0; i < string.length(); i++) {
			result[i] = Character.toString(string.charAt(i));
		}
		return result;
	}
	
	public String parseLine(String line) {
		String output = "";
		String[] elements = null;
		
		if (state == STATE_BYCHARS) {
			elements = getSeperateChars(line);
		} else {
			elements = line.split(delimiter);
		}
		
		ArrayList<String> allSequence = new ArrayList<String>();
		
		for (String param : params.split(",")) {
			if (param.contains("-")) {
				String[] interval = param.split("-");
				int startIndex = (interval[0].length() == 0)?0:(Integer.parseInt(interval[0]) - 1);
				int endIndex = (interval.length < 2)?(elements.length-1):(Integer.parseInt(interval[1]) - 1);
				
				for (int i = startIndex; i <= endIndex; i++) {
					allSequence.add(elements[i]);
				}
			} else {
				int index = Integer.parseInt(param) - 1;
				allSequence.add(elements[index]);
			}
		}
		
		for (int i = 0; i < allSequence.size(); i++)
		{
			output += allSequence.get(i);
			
			if (state == STATE_BYCOLUMNS && i < allSequence.size() - 1) {
				output += delimiter;
			}
		}
		
		return output;
	}
	
	static public void main(String[] args) {
	
		String delimiter = DEFAULT_DELIMITER;
		String params = null;
		int state = -1;
		
		for (int i = 0; i < args.length; i = i + 2) {
			char param = args[i].charAt(1);
			switch (param) {
			case 'd':
				delimiter = args[i + 1];
				break;
			case 'f':
				state = STATE_BYCOLUMNS;
				params = args[i + 1];
				break;
			case 'c':
				state = STATE_BYCHARS;
				params = args[i + 1];
				break;
			}
		}
		
		if (state == -1) { 
			System.out.println("Chybi parametry.");
			System.exit(1);
		}
		
		Cut cut = new Cut(state, params, delimiter);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));		
		String line = null;
				
		try {
			while ((line = reader.readLine()) != null) {
				
				System.out.println(cut.parseLine(line));
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
