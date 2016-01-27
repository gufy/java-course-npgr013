package cz.cuni.mff.ms.urtax.goophy.cvika.kalkulacka.expression;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Multiply extends BinaryNode {
	public Multiply() {
		super();
	}
	
	protected BigDecimal evaluate(BigDecimal left, BigDecimal right) {
		return left.multiply(right);
	}

}
