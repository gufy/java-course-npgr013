package cz.cuni.mff.ms.urtax.goophy.cvika.kalkulacka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;

import cz.cuni.mff.ms.urtax.goophy.cvika.kalkulacka.expression.Expression;

public class Calc {
	protected String lastResult;
	protected int precision;
	public Calc()
	{
	}
	public String compute(String input) {
		input = input.replaceAll("\\s+","");
		BigDecimal result;
		
		try
		{
			if ((result = Expression.Parse("("+input+")")) != null)
			{
				String strResult = (result.compareTo(BigDecimal.ZERO) == 0)?"0":result.stripTrailingZeros().toPlainString();
				//String strResult = result.stripTrailingZeros().toPlainString();
				return strResult;
			}
			else
			{
				return "";
			}
		} catch (Exception e)
		{
			Expression.clearLastResult();
			//System.out.println(e.getMessage());
			//e.printStackTrace();
			return "CHYBA";
		}
	}
	
	static public void main(String[] args) throws IOException
	{
		Calc calc = new Calc();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		String line;
		while ((line = in.readLine()) != null)
		{
			System.out.println(calc.compute(line));
		}
	}
}
