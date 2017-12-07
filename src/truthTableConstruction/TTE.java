package truthTableConstruction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

import TabularFunctions.TabularEvaluation;

public class TTE {
	
	public static void main(String[] args) {
		
		boolean invalid = false;
		while(true) {
			EvaluatorImpl test = new EvaluatorImpl();
			Scanner scan = new Scanner(System.in);
			List<Boolean> truthTable = null ;
			String expression = null;
			try {
			clearScreen();
			System.out.println("\n\n\t\t\t************ "+"************ "+" ********");
			System.out.println("\t\t\t **********  "+" **********  "+"******** ");
			System.out.println("\t\t\t    ****     "+"    ****     "+"***");
			System.out.println("\t\t\t    ****     "+"    ****     "+"***");
			System.out.println("\t\t\t    ****     "+"    ****     "+"*********");
			System.out.println("\t\t\t    ****     "+"    ****     "+"*********");
			System.out.println("\t\t\t    ****     "+"    ****     "+"***");
			System.out.println("\t\t\t    ****     "+"    ****     "+"********");
			System.out.println("\t\t\t     **      "+"     **      "+" ********\n\n");
	
			if (!invalid) {
				System.out.println("\t\t\t Please enter an expression:");
				System.out.print("\t\t\t   ");
			} else {
				invalid = false;
				System.out.println("\t\t\t Invalid expression or a contradiction, Enter again:");
				System.out.print("\t\t\t   ");
			}
			
			expression = (scan.nextLine());
			String postFix = test.infixToPostfix(expression);
	//		System.out.println("Postfix expression:");
	//		System.out.println(postFix);
			List<Character> variables = test.getVariables(postFix);
			truthTable = test.getTruthTable(variables, postFix);
			System.out.println("\n\t\t\t Truthtable:");
			for (int i = 0; i < truthTable.size(); i++) {
				System.out.println("\t\t\t  " + i + ": " + truthTable.get(i));
			}
			test.minimizeTruthTable(variables.size());
			} catch (Exception e) {
				invalid = true;
				continue;
			}
			System.out.println("\n\t\t\t Exit program ?\n\t\t\t type y to exit\n\t\t\t type s to save the result\n\t\t\t or any thing else to solve another one:");
			System.out.print("\t\t\t   ");
			String exit = scan.nextLine();
			if (exit.equals("y")) {
				return;
			} else if (exit.equals("s")) {
				PrintWriter writer = null;
				try {
					writer = new PrintWriter("saved_truthtable_result.txt", "UTF-8");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				writer.println("Expression: " + expression);
				for (int i = 0; i < truthTable.size(); i++) {
					writer.println(i + ": " + truthTable.get(i));
				}
				writer.close();
			}
			
		}
		
	}
	public static void clearScreen() {
		try {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor(); 
		} catch (Exception e) {
			//TODO
		}
	}

}
