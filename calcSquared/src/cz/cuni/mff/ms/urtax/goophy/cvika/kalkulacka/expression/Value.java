package cz.cuni.mff.ms.urtax.goophy.cvika.kalkulacka.expression;

import java.math.BigDecimal;

public class Value extends NopNode {
	public Value(BigDecimal value) {
		super(value);
	}
}
