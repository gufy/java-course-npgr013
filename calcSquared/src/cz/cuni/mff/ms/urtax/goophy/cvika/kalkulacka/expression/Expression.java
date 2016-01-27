package cz.cuni.mff.ms.urtax.goophy.cvika.kalkulacka.expression;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Expression {
	static protected BigDecimal last = null;
	
	static public void clearLastResult()
	{
		last = new BigDecimal("0");
	}
	
	static public BigDecimal Parse(String input) throws Exception
	{
		Expression expr = new Expression();
		ExpressionParser parser = null;
		
		if (last != null)
		{
			parser = new ExpressionParser(expr, input, last);
		}
		else
		{
			parser = new ExpressionParser(expr, input);
		}
		
		if (parser.run())
		{
			ExpressionNode node = parser.getExpressionTree();
			
			last = node.getValue();
			last = last.setScale(ExpressionParser.precision, RoundingMode.FLOOR);
			
			return last;
		}
		else
		{
			return null;
		}
	}
}
