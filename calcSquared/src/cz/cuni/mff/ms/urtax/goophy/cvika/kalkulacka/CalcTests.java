package cz.cuni.mff.ms.urtax.goophy.cvika.kalkulacka;

import java.util.ArrayList;

public class CalcTests {
	protected Calc calc;
	protected ArrayList<String> inputs;
	protected ArrayList<String> outputs;
	protected ArrayList<String> fails;
	protected ArrayList<String> success;
	protected boolean lastResult;
	public CalcTests(Calc calc)
	{
		this.calc = calc;
		this.inputs = new ArrayList<String>();
		this.outputs = new ArrayList<String>();
	}
	public void addValue(String input, String output)
	{
		inputs.add(input);
		outputs.add(output);
	}
	public boolean proceedTest()
	{
		fails = new ArrayList<String>();
		success = new ArrayList<String>();
		lastResult = true;
		
		for (int i = 0; i < inputs.size(); i++)
		{
			String output = calc.compute(inputs.get(i));
			
			if (output.equals(outputs.get(i)))
			{
				success.add(inputs.get(i));
			}
			else
			{
				fails.add(inputs.get(i) + " expected " + outputs.get(i) + " but got " + output);
				lastResult = false;
			}
		}
		
		return lastResult;
	}
	public void printFails()
	{
		System.out.println("Failed at:");
		for (String line : fails)
		{
			System.out.println(line);
		}
	}
	public void printSuccess()
	{
		System.out.println("Succeeded at:");
		for (String line : success)
		{
			System.out.println(line);
		}
	}
	static public void main(String[] args) {
		Calc calc = new Calc();
		CalcTests test = new CalcTests(calc);
	
		test.addValue("3 + 2", "5");
		test.addValue("2 * ( 3 + 3 )", "12");
		test.addValue("2 - 3 * ( 1 + 2 )", "-7");
		test.addValue("2 * 3", "6");
		test.addValue("last - 3", "3");
		test.addValue("10 * last", "30");
		test.addValue("1.2 + 4.3", "5.5");
		test.addValue("1 / 3", "0.33333333333333333333");
		test.addValue("1000000000 * 1000000000", "1000000000000000000");
		test.addValue("precision(30)", "");
		test.addValue("1 / 3", "0.333333333333333333333333333333");
		test.addValue("3 ** 5", "CHYBA");
		test.addValue("last * 3", "0");
		test.addValue("* * 3", "CHYBA");
		test.addValue("((3", "CHYBA");
		test.addValue("3", "3");
		test.addValue("-3+1", "-2");
		test.addValue("-3", "-3");
		test.addValue("-3+1*-2", "-5");
		test.addValue("2+x", "2");
		test.addValue("y*x", "0");
		test.addValue("x = (2+1)", "3");
		test.addValue("last", "3");
		test.addValue("y = 2", "2");
		test.addValue("last", "2");
		test.addValue("y*x", "6");
		test.addValue("DEF sqr(x) x*x", "");
		test.addValue("DEF neg(x) -x", "");
		test.addValue("sqr(3)", "9");
		test.addValue("sqr(sqr(2)+1)", "25");
		test.addValue("neg(4)", "-4");
		test.addValue("DEF prumer(x, y, z) (x+y+z)/3", "");
		test.addValue("prumer(1,2,3)", "2");
		test.addValue("precision(2)", "");
		test.addValue("prumer(sqr(1),sqr(2),sqr(3))", "4.66");
		test.addValue("precision(100)", "");
		test.addValue("1/3", "0.3333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
		test.addValue("precision(20)", "");
		test.addValue("last*3", "0.99999999999999999999");
		
		/*
		*/

		System.out.println("===================================");
		System.out.println("Test starts.\n");
		
		if (test.proceedTest())
		{			
			System.out.println("Test SUCCEEDED.\n");
		}
		else
		{
			test.printSuccess();
			test.printFails();
			
			System.out.println("\nTest FAILED.\n");	
		}

		System.out.println("End of test.");
		System.out.println("===================================");
	}
}
