package cz.cuni.mff.ms.urtax.goophy.cvika.kalkulacka.expression;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ExpressionParser {
	protected Expression expr;
	protected char[] input;
	protected String inputString;
	
	static protected final int EXPECTING_EXPRESSION = 0;
	static protected final int EXPECTING_OPERAND = 1;
	
	protected int currentlyExpecting = EXPECTING_EXPRESSION;
	
	protected ArrayDeque<Character> operatorBuffer;
	protected ArrayDeque<ExpressionNode> operandBuffer;
	
	protected boolean nextNegative = false;

	protected BigDecimal last;
	static public int precision = 20;
	
	protected boolean unaryop = true;

	static protected HashMap<String, BigDecimal> variables;
	static protected HashMap<String, Function> functions;
	
	public ExpressionParser(Expression expr, String input, BigDecimal last) {
		this.expr = expr;
		this.inputString = input;
		this.input = input.toCharArray();
		this.operandBuffer = new ArrayDeque<ExpressionNode>();
		this.operatorBuffer = new ArrayDeque<Character>();
		this.last = last;
		if (variables == null) variables = new HashMap<String, BigDecimal>();
		if (functions == null) functions = new HashMap<String, Function>();
	}
	public ExpressionParser(Expression expr, String input) {
		this(expr, input, new BigDecimal("0"));
	}
	public boolean run() throws Exception {
		for (int i = 0; i < input.length; i++) {
			char c = input[i];
			
			switch (c)
			{
			default:
			{
				// try to load entire word
				StringBuilder wordBuilder = new StringBuilder();
				int j = 0;
				while ((('a' <= c) && (c <= 'z')) || (('A' <= c) && (c <= 'Z'))) {
					wordBuilder.append(c);
					j++;
					c = input[i+j];
				}
				
				String word = wordBuilder.toString();
				
				if (word.length() > 0)
				{
					i += word.length() - 1;

					if (word.equals("precision")) {
						i += 2;
						
						String value = "";
						while (input[i] >= '0' && input[i] <= '9' && i < input.length - 1)
						{
							value += input[i];
							i++;
						}
						
						precision = Integer.parseInt(value);
						return false;
					} else
					if (word.equals("last")) {
						BigDecimal big = last;
						big = big.setScale(precision, RoundingMode.FLOOR);
						
						// check unary minus
						if (nextNegative)
						{
							big = big.negate();
							nextNegative = false;
						}
						
						ExpressionNode value = new Value(big);
						operandBuffer.add(value);
						unaryop = false;
					} else
					if (word.length() > 2 && word.substring(0, 3).equals("DEF")) {
						i += 2;
						String functionName = word.substring(3);
						
						int closingBracketIndex = inputString.indexOf(')', i);
						String params[] = inputString.substring(i, closingBracketIndex).split(",");
						String rule = inputString.substring(closingBracketIndex + 1, inputString.length()-1);
						
						Function fce = new Function(rule, params);
						
						functions.put(functionName, fce);
						return false;
					} else
					if (variables.containsKey(word)) {
						// existuje jako promenna
						BigDecimal big = variables.get(word);
						big = big.setScale(precision, RoundingMode.FLOOR);
						
						// check unary minus
						if (nextNegative)
						{
							big = big.negate();
							nextNegative = false;
						}
						
						ExpressionNode value = new Value(big);
						operandBuffer.add(value);
						unaryop = false;						
					} else {
						if (input[i+1] == '=') {
							
							// defining new variable
							if (!functions.containsKey(word) && !word.equals("last") && !word.equals("DEF")) {
								
								// should not be declared as function
								Expression expr = new Expression();
								ExpressionParser parser = new ExpressionParser(expr, "("+inputString.substring(i+2,inputString.length()-1)+")", last);
								BigDecimal value = null;
								
								if (parser.run()) {
									
									ExpressionNode node = parser.getExpressionTree();
									value = node.getValue();
									value = value.setScale(precision, RoundingMode.FLOOR);
									
									// check unary minus
									if (nextNegative)
									{
										value = value.negate();
										nextNegative = false;
									}
									
									variables.put(word, value);
									
									ExpressionNode valueNode = new Value(value);
									operandBuffer.add(valueNode);
									unaryop = false;
									i = inputString.length()-2;
									
								} else {
									throw new Exception("Chyba.");
								}
								
							} else {
								throw new Exception("Chyba. Uz to je funkce.");
							}
						} else
						if (functions.containsKey(word)) {
							Function fce = functions.get(word);
									
							// split into values.length params
							ArrayList<Integer> commaIndexes = new ArrayList<Integer>();
							int numOpenedBrackets = 1;
							int in = 0;
							for (; numOpenedBrackets > 0; in++) {
								char cc = inputString.charAt(i+in+2);
								switch (cc) {
								case '(': numOpenedBrackets++; break;
								case ')': numOpenedBrackets--; break;
								case ',': 
								{
									if (numOpenedBrackets == 1) {
										commaIndexes.add(in);
									}
								}
								}
							}
							commaIndexes.add(in-1);

							String paramString = inputString.substring(i+2, i+1+in);
							BigDecimal value = fce.run(paramString, commaIndexes, precision, last);
							value = value.setScale(precision, RoundingMode.FLOOR);
							
							// check unary minus
							if (nextNegative)
							{
								value = value.negate();
								nextNegative = false;
							}
							
							ExpressionNode valueNode = new Value(value);
							operandBuffer.add(valueNode);
							unaryop = false;
							i += 1+in;
							
						} else {
							// implicit zero
							BigDecimal big = new BigDecimal(0);
							big = big.setScale(precision, RoundingMode.FLOOR);
							
							// check unary minus
							if (nextNegative)
							{
								big = big.negate();
								nextNegative = false;
							}
							
							ExpressionNode value = new Value(big);
							operandBuffer.add(value);
							unaryop = false;
						}
					}
				}
				else 
				{
					throw new Exception("Chyba. Neocekavany znak.");
				}
				
				// problem
				break;
			}
			/*
			case 'p':
			{
				String word = "";
				for (int j = 0; j < 9; j++)
				{
					word += input[j+i];
				}
				
				if (word.equals("precision"))
				{
					i += 9;
					
					String value = "";
					while (input[i] >= '0' && input[i] <= '9' && i < input.length)
					{
						value += input[i];
						i++;
					}
					
					precision = Integer.parseInt(value);
					return false;
				}
				else
				{
					// ERROR
				}
				break;
			}
			case 'l':
			{
				if (input[i+1] == 'a' && input[i+2] == 's' && input[i+3] == 't')
				{
					i += 3;
					BigDecimal big = last;
					big.setScale(precision, RoundingMode.FLOOR);
					
					// check unary minus
					if (nextNegative)
					{
						big = big.negate();
						nextNegative = false;
					}
					
					ExpressionNode value = new Value(big);
					operandBuffer.add(value);
					unaryop = false;
				}
				else
				{
					// ERROR
				}
				break;
			}
			*/
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '.':
				// nacteme cislo
				String number = "";
				
				while (i < input.length)
				{
					if ((input[i] >= '0' && input[i] <= '9') || input[i] == '.')
					{
						number += input[i];
						i++;
					}
					else
					{
						break;
					}
				}
				
				i--;
				
				BigDecimal big = new BigDecimal(number);
				big = big.setScale(precision, RoundingMode.FLOOR);
				
				// check unary minus
				if (nextNegative)
				{
					big = big.negate();
					nextNegative = false;
				}
				
				ExpressionNode value = new Value(big);
				operandBuffer.add(value);
				unaryop = false;
				break;
			case '(':
			case '+':
			case '*':
			case '-':
			case '/':
			case ')':
				if (unaryop && c == '-')
				{
					nextNegative = true;
					unaryop = false;
					break;
				}
				
				if (operatorBuffer.size() == 0)
				{
					operatorBuffer.add(c);
					unaryop = true;
				}
				else
				{
					if (c == '(')
					{
						operatorBuffer.add('(');
						unaryop = true;
					}
					else if (c == ')')
					{
						// evaluate everything till (
						while (operatorBuffer.size() > 0 && operatorBuffer.getLast() != '(')
						{
							BinaryNode node = BinaryNode.factory(operatorBuffer.pollLast());
						
							if (node != null)
							{
								ExpressionNode right = operandBuffer.pollLast();
								ExpressionNode left = operandBuffer.pollLast();
								
								node.setLeft(left);
								node.setRight(right);
								
								operatorBuffer.add(c);
								operandBuffer.add(node);
							}
						}
						
						// remove bracket
						operatorBuffer.removeLast();
						unaryop = false;
					}
					else 
					{
						if (operatorLevel(c) > operatorLevel(operatorBuffer.getLast()))
						{
							operatorBuffer.add(c);
						}
						else
						{
							BinaryNode node = BinaryNode.factory(operatorBuffer.pollLast());
							
							if (node != null)
							{
								ExpressionNode right = operandBuffer.pollLast();
								ExpressionNode left = operandBuffer.pollLast();
								
								node.setLeft(left);
								node.setRight(right);
								
								operatorBuffer.add(c);
								operandBuffer.add(node);
							}
							else
							{
								// CHYBA
							}
						}
						unaryop = true;
					}
				}
				break;
			}			
		}
		
		//System.out.println("Parsed: "+input.toString());
		
		//BigInteger n = operandBuffer.getLast().getValue();
		//System.out.println("Result: "+n);
		
		if (operatorBuffer.size() != 0)
		{
			throw new Exception("Chybny vyraz. Buffer operatoru se nevyprazdnil.");
		}
		
		return true;
	}
	public ExpressionNode getExpressionTree() {
		return operandBuffer.getLast();
	}
	protected int operatorLevel(char c)
	{
		switch (c)
		{
		case '*':
		case '/':
			return 1;
		case '-':
		case '+':
			return 0;
		default: 
			return -1;
		}
	}
}
