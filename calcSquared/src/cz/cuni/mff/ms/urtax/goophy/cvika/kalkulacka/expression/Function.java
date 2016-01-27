package cz.cuni.mff.ms.urtax.goophy.cvika.kalkulacka.expression;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Function {
	protected String rule;
	protected String[] params;
	public Function(String rule, String[] params) {
		this.rule = rule;
		this.params = params;
	}
	
	public BigDecimal run(String valuesInString, ArrayList<Integer> commaIndexes, int precision, BigDecimal last) throws Exception {
		String[] values = new String[params.length];
		
		if (commaIndexes.size() > params.length) {
			throw new Exception("Chyba. Pocet parametru neodpovida.");
		} else {
			int prevIndex = 0;
			for (int i = 0; i < commaIndexes.size(); i++) {
				int currentIndex = commaIndexes.get(i);
				values[i] = valuesInString.substring(prevIndex, currentIndex);
				prevIndex = currentIndex + 1;
			}
		}
		
		return run(values, precision, last);
	}
	
	public BigDecimal run(String[] values, int precision, BigDecimal last) throws Exception {
		BigDecimal[] realValues = new BigDecimal[values.length];
		int irv = 0;
		for (String value : values) {
			
			Expression expr = new Expression();
			ExpressionParser parser = new ExpressionParser(expr, "("+value+")", last);
			BigDecimal parsedValue = null;
			
			if (parser.run()) {
				
				ExpressionNode node = parser.getExpressionTree();
				parsedValue = node.getValue();
				parsedValue.setScale(precision, RoundingMode.FLOOR);

				realValues[irv++] = parsedValue;
								
			} else {
				throw new Exception("Chyba.");
			}
			
		}
		
		String expression = rule;
		for (int i = 0; i < realValues.length; i++) {
			expression = expression.replace(params[i], realValues[i].toPlainString());
		}
		
		Expression expr = new Expression();
		ExpressionParser parser = new ExpressionParser(expr, "("+expression+")", last);
		BigDecimal parsedValue = null;
		
		if (parser.run()) {
			
			ExpressionNode node = parser.getExpressionTree();
			parsedValue = node.getValue();
			parsedValue.setScale(precision, RoundingMode.FLOOR);
							
		} else {
			throw new Exception("Chyba.");
		}		
		
		return parsedValue;
	}
}
