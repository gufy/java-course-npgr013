package cz.cuni.mff.ms.urtax.goophy.cvika.kalkulacka.expression;

import java.math.BigDecimal;

public abstract class ExpressionNode {
	protected ExpressionNode left = null;
	protected ExpressionNode right = null;
	
	public ExpressionNode()
	{
		
	}
	
	public void setLeft(ExpressionNode node)
	{
		this.left = node;
	}
	
	public void setRight(ExpressionNode node)
	{
		this.right = node;
	}
	
	public abstract BigDecimal getValue();
}
