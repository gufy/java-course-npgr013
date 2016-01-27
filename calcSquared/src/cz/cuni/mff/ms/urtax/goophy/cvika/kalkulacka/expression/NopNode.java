package cz.cuni.mff.ms.urtax.goophy.cvika.kalkulacka.expression;

import java.math.BigDecimal;

public abstract class NopNode extends ExpressionNode {
	protected BigDecimal value;
	
	public NopNode(BigDecimal value) {
		super();
		this.value = value;
	}
	
	public BigDecimal getValue()
	{
		return value;
	}
}
