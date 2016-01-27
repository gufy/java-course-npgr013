package cz.cuni.mff.ms.urtax.goophy.cvika.kalkulacka.expression;

import java.math.BigDecimal;

public abstract class BinaryNode extends ExpressionNode {
	public BinaryNode()
	{
		
	}
	
	public BigDecimal getValue()
	{
		BigDecimal left = this.left.getValue();
		BigDecimal right = this.right.getValue();
		
		return evaluate(left, right);
	}
	
	protected abstract BigDecimal evaluate(BigDecimal left, BigDecimal right);
	
	static public BinaryNode factory(char c)
	{
		switch (c)
		{
		case '-':
			return new Minus();
		case '+':
			return new Plus();
		case '*':
			return new Multiply();
		case '/':
			return new Divide();
		}
		
		return null;
	}
}
