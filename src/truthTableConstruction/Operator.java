package truthTableConstruction;

public class Operator {
	char operator;
	int periority;
	public Operator(char operator) {
		this.operator = operator;
		if (operator ==  '-') {
			periority = 5;
		} else if (operator ==  '*') {
			periority = 4;
		} else if (operator ==  '+') {
			periority = 3;
		} else if (operator ==  '>') {
			periority = 2;
		} else if (operator ==  '/') {
			periority = 1;
		}
	}
	
}
