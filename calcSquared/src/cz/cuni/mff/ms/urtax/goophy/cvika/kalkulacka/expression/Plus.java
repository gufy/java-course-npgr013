package cz.cuni.mff.ms.urtax.goophy.cvika.kalkulacka.expression;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Plus extends BinaryNode {
	public Plus() {
		super();
	}
	protected BigDecimal evaluate(BigDecimal left, BigDecimal right)
	{
		return left.add(right);
	}
}
