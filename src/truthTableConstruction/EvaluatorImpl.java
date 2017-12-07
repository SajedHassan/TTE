package truthTableConstruction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import LinkedList.SLinkedList;
import TabularFunctions.TabularEvaluation;
import TabularFunctions.petrikGroup;

/**
 * @author sajed
 *
 */
public class EvaluatorImpl implements Evaluator {
	StringBuilder minU = new StringBuilder();
	List<Character> variables;

	@Override
	public String infixToPostfix(String expression) {
		expression = expression.toLowerCase();
		expression = expression.replaceAll("-", "1-");
		Stack<Character> operations = new Stack<Character>();

		boolean validOperators = true;
		int bracesCount = 0;

		// TODO Auto-generated method stub
		if (expression.length() == 0
				|| expression.charAt(0) == '*'
				|| expression.charAt(0) == '+'
				|| expression.charAt(0) == '/'
				|| expression.charAt(0) == '>'
				|| expression.charAt(0) == ')') {
			throw null;
		}

		// String postfixExpression = new String();
		StringBuilder postFixExpression
		= new StringBuilder(expression.length());
		int index = 0;
		while (true) {

			if (index < expression.length()
					&& expression.charAt(index) == ' ') {
				index++;
				continue;
			}

			while (index < expression.length()
					&& expression.charAt(index) != '*'
					&& expression.charAt(index) != '-'
					&& expression.charAt(index) != '+'
					&& expression.charAt(index) != '/'
					&& expression.charAt(index) != '>'
					&& expression.charAt(index) != '('
					&& expression.charAt(index) != ')'
					&& expression.charAt(index) != ' ') {

	postFixExpression.append(expression.charAt(index) + " ");
				index++;
				validOperators = true;
			}

			if (index < expression.length()
					&& expression.charAt(index) == ' ') {
				index++;
				continue;
			}

			if (index < expression.length()
					&& expression.charAt(index) != ')') {

				if (expression.charAt(index) == '(') {
				operations.push(expression.charAt(index));
					bracesCount++;
					index++;
				} else if (operations.isEmpty()
					|| operations.peek().equals('(')) {
				operations.push(expression.charAt(index));
					validOperators = false;
					index++;

			} else  {
				Operator operator = new Operator(expression.charAt(index));
				if (validOperators) {

					while (!(operations.isEmpty())
				&& !operations.peek().equals('(')
				&& operator.periority <= (new Operator(operations.peek()).periority)) {
	postFixExpression.append(operations.pop() + " ");
						}
			operations.push(expression.charAt(index));
						validOperators = false;

						index++;
					} else {
						throw null;
					}
			}

			} else if (index < expression.length()) {
				while (!(operations.isEmpty())
				&& !(operations.peek().equals('('))) {
	postFixExpression.append(operations.pop() + " ");
				}
				try {
					operations.pop();
				} catch (Exception e) {
					throw null;
				}
				bracesCount--;
				index++;
			}

	if (index >= expression.length() && validOperators
					&& bracesCount == 0) {
				break;
			} else if (index >= expression.length()
			&& (!validOperators || bracesCount != 0)) {
				throw null;
			}

		}

		while (!(operations.isEmpty())) {
	postFixExpression.append(operations.pop() + " ");
		}

	return postFixExpression.toString().substring(0,
				postFixExpression.length() - 1);

	}

	@Override
	public int evaluate(final String expression) {
		int index = 0;
		Stack evaluator = new Stack();
		while (index < expression.length()) {
			if (expression.charAt(index) == ' ') {
				index++;
				continue;
			} else if (expression.charAt(index) == '+'
				|| expression.charAt(index) == '*'
				|| expression.charAt(index) == '/'
				|| expression.charAt(index) == '>'
			|| expression.charAt(index) == '-') {

			if (expression.charAt(index) == '+') {
					try {
						int second = (int) evaluator.pop();
						int first = (int) evaluator.pop();
						int res = first + second;
						if (res == 0) {
								evaluator.push(res);
						} else {
							evaluator.push(1);
						}
					} catch (Exception e) {
						throw null;
					}
		} else if (expression.charAt(index) == '-') {
					try {
			evaluator.push(-1 * ((int) evaluator.pop()
						- (int) evaluator.pop()));
					} catch (Exception e) {
						throw null;
					}
		} else if (expression.charAt(index) == '/') {
					try {
				int second = (int) evaluator.pop();
				int first = (int) evaluator.pop();
				int res = 1 - (first ^ second);
						evaluator.push(res);
					} catch (Exception e) {
						throw e;
					}
		} else if (expression.charAt(index) == '*') {
					try {
				evaluator.push((int) evaluator.pop()
						* (int) evaluator.pop());
					} catch (Exception e) {
						throw null;
					}
				} else if (expression.charAt(index) == '>') {
					try {
						int second = (int) evaluator.pop();
						int first = (int) evaluator.pop();
						int res = first ^ second;
						if (res == 1 && first == 1) {
								evaluator.push(0);
						} else {
							evaluator.push(1);
						}
					} catch (Exception e) {
						throw null;
					}
				}
				index++;

			} else {
				String temp = new String();
				while (index < expression.length()
				&& expression.charAt(index) != ' '
				&& expression.charAt(index) != '+'
				&& expression.charAt(index) != '-'
				&& expression.charAt(index) != '>'
				&& expression.charAt(index) != '*'
			&& expression.charAt(index) != '/') {
				temp += expression.charAt(index);
					index++;
				}

				int foo = 0;
				try {
					foo = Integer.parseInt(temp);
				} catch (Exception e) {
					throw null;
				}
				evaluator.push(foo);
			}
		}
		int res;
		try {
			res = (int) evaluator.pop();
		} catch (Exception e) {
			throw null;
		}
		if (evaluator.isEmpty()) {
			return res;
		} else {
			return 0;
		}

	}

	public List<Character> getVariables(String expression) {
		StringBuilder count = new StringBuilder();
		for (int i = 0; i < expression.length(); i++) {
			
			if (Character.isLetter(expression.charAt(i))) {
				count.append(expression.charAt(i));
			} else {
				if (expression.charAt(i) != ' ' &&
					expression.charAt(i) != '-' &&
					expression.charAt(i) != '*' &&
					expression.charAt(i) != '+' &&
					expression.charAt(i) != '>' &&
					expression.charAt(i) != '/' &&
					expression.charAt(i) != '1' &&
					expression.charAt(i) != '0') {
						throw new RuntimeException("Invalid Expression");
					}
			}
		}
		
		char[] chars = count.toString().toCharArray();
		Set<Character> charSet = new LinkedHashSet<Character>();
		for (char c : chars) {
		    charSet.add(c);
		}

		List<Character> countWithoutDublicates = new ArrayList<Character>();
		for (Character character : charSet) {
			countWithoutDublicates.add(character);
		}
		
		variables = new ArrayList<Character>(countWithoutDublicates);
		
		Collections.reverse(countWithoutDublicates);
		return countWithoutDublicates;
	}

	public List<Boolean> getTruthTable(List<Character> variables, String postFix) {
		String expressionToBeEvaluated;
		//BitSet truthTable = new BitSet((int)Math.pow(2, variables.size()));
		List<Boolean> truthTable = new ArrayList<Boolean>();
		for (int i = 0; i <  Math.pow(2, variables.size()); i++) {
			expressionToBeEvaluated = new String(postFix);
			for (int j = 0; j < variables.size(); j++) {
				int substitution = ((int)Math.pow(2, j) & i);
				if (substitution > 1) {
					substitution = 1;
				}
				expressionToBeEvaluated = expressionToBeEvaluated.replaceAll(variables.get(j).toString(), "" + substitution);
			}
			//System.out.println(expressionToBeEvaluated);
			if (evaluate(expressionToBeEvaluated) == 1) {
				truthTable.add(true);
				this.minU.append(i + ",");
			} else {
				truthTable.add(false);
			}
		}
		return truthTable;
	}

	public void minimizeTruthTable(int numOfVariables) {
		TabularEvaluation test = new TabularEvaluation();
		int[] minterm = test.setMinterms(this.minU.toString(), numOfVariables);
		test.setRealMinterms(minterm);
		int[] dont;
		try{
		dont = test.setDontCares("4,18,21", numOfVariables);
		} catch (Exception e) {
			dont = null;
		}
		if (test.isValidInput(minterm, null)) {

			int[] minterms;
			
			minterms = minterm;
			
			test.setParameters(minterms, numOfVariables, dont);
	
			SLinkedList res = test.findPrimeImplicants();
			//test.printTest(res);
			String mapped = test.mapPrimeImplicants(res);
			//System.out.println(mapped);
	
			String[] allPrimeImplicants = mapped.split("\\s*,\\s*");
	

			SLinkedList pet = test.rowColumnChart(res);
			//System.out.println(((petrikGroup)pet.get(0)).primeImplicant.size());

//			for (int i = 0; i < ((petrikGroup)pet.get(0)).primeImplicant.size() ; i++) {
//				System.out.println(((petrikGroup)pet.get(0)).primeImplicant.get(i));
//			}
			
			
			String[] allSolutions = test.mapAllSolutions(pet, allPrimeImplicants);
			SLinkedList min = test.getMinSolutions(pet);
			String[] minSolutions = test.mapAllSolutions(min, allPrimeImplicants);
			
			
			String[] reservedMinSolutions = new String[minSolutions.length];
			for (int i = 0; i < minSolutions.length ; i++) {
				reservedMinSolutions[i] = minSolutions[i];
			}
			
			SLinkedList minCost = new SLinkedList();
			minSolutions[0] = minSolutions[0].replace("'", "");
			minSolutions[0] = minSolutions[0].replace("+", "");
			minSolutions[0] = minSolutions[0].replace(" ", "");
			minCost.add(new Point(minSolutions[0].length(), 0));
			for (int i = 1; i < minSolutions.length ; i++) {
				minSolutions[i] = minSolutions[i].replace("'", "");
				minSolutions[i] = minSolutions[i].replace("+", "");
				minSolutions[i] = minSolutions[i].replace(" ", "");
				if (minSolutions[i].length() == ((Point)minCost.get(0)).x) {
					minCost.add(new Point(minSolutions[i].length(), i));
				} else if (minSolutions[i].length() < ((Point)minCost.get(0)).x) {
					minCost.clear();
					minCost.add(new Point(minSolutions[i].length(), i));
				}
			}


			System.out.println("\n\t\t\t All solutions:");

			for (int i = 0 ; i < allSolutions.length ; i++) {
				String sol = new String(allSolutions[i]);
				for (int j = 0; j < variables.size(); j++) {
					sol = sol.replaceAll(((Character)test.literals[j]).toString(), this.variables.get(j).toString());
				}
				System.out.println("\t\t\t " + sol);
			}
			System.out.println("\n\t\t\t Minimum solution : ");

			for (int i = 0 ; i < minCost.size() ; i++) {
				String sol = new String(reservedMinSolutions[((Point)minCost.get(i)).y]);
				for (int j = 0; j < variables.size(); j++) {
					sol = sol.replaceAll(((Character)test.literals[j]).toString(), this.variables.get(j).toString());
				}
				System.out.println("\t\t\t " + sol);
			}
//			System.out.println(test.getSteps());
		}
	}


}
