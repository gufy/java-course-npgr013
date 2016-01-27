package cz.cuni.mff.ms.urtax.goophy.cvika.kalkulacka.expression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Divide extends BinaryNode {
	public Divide() {
		super();
	}
	
	protected BigDecimal evaluate(BigDecimal left, BigDecimal right) {
		return left.divide(right, ExpressionParser.precision, RoundingMode.FLOOR);
	}

}
