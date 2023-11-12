/*
names: Loc Nguyen, Phuoc Nguyen
Project Phase 2.1 (PR2.1): Parser for expressions
*/

package Evaluator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
// the way the Stack class works is it just extends from Vector - it just has some more 
// functions (like push, pop, peek) that make it operate somewhat like a Stack

import Parser.parser;
import scannerPhase2.ScannerPhase2;
import scannerPhase2.ScannerPhase2.Token;
import scannerPhase2.ScannerPhase2.TokenType;

public class evaluator extends parser{

	public evaluator() {
		// TODO Auto-generated constructor stub
	}
	
	public static Stack<Token> evaluateTop3Expr (Stack<Token> stack) {
		if (stack.size() < 3 || stack.peek().getType()!=TokenType.NUMBER || stack.elementAt(stack.size()-2).getType()!=TokenType.NUMBER || stack.elementAt(stack.size()-3).getType()!=TokenType.SYMBOL) {
			return stack;
		}
		
		else {
			Token token1 = stack.peek();
			Token token2 = stack.elementAt(stack.size()-2);
			Token token3 = stack.elementAt(stack.size()-3);
			String r = "";
	
			if (token3.getValue().equals("*")) r = String.valueOf(Integer.valueOf(token2.getValue())*Integer.valueOf(token1.getValue()));
			else if (token3.getValue().equals("/")) r = String.valueOf(Integer.valueOf(token2.getValue())/Integer.valueOf(token1.getValue()));
			else if (token3.getValue().equals("+")) r = String.valueOf(Integer.valueOf(token2.getValue())+Integer.valueOf(token1.getValue()));
			else if (token3.getValue().equals("-")) r = String.valueOf(Integer.valueOf(token2.getValue())-Integer.valueOf(token1.getValue()));			
			stack.pop();
			stack.pop();
			stack.pop();
			stack.push(new Token (TokenType.NUMBER, r));
			
			return stack;
		}		
	}
	
	public static void preOrder(TreeNode node, Stack<Token> stack) {
		if (node.getError() || (node.getLeftChild() != null && node.getLeftChild().getError()) || (node.getRightChild() != null && node.getRightChild().getError()))return;
		
		//should I create a stack of Tokens or of TreeNodes??
		stack.push(node.getDataToken());
		stack = evaluateTop3Expr (stack);
		
		preOrder(node.getLeftChild(), stack);
		stack = evaluateTop3Expr (stack);
		preOrder(node.getMidChild(), stack);
		stack = evaluateTop3Expr (stack);
		preOrder(node.getRightChild(), stack);
		stack = evaluateTop3Expr (stack);
	}
	
	public static void writeResult(Stack<Token> stack, TreeNode node, String outputFile) throws IOException {
		if (node.getError() || (node.getLeftChild() != null && node.getLeftChild().getError()) || (node.getRightChild() != null && node.getRightChild().getError())) return;
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true));
		bw.newLine();
		bw.write("result = " + stack.peek().getValue());
		bw.close();
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Usage: java FileEvaluator <input_file> <output_file>");
			System.exit(1);
		}

		String inputFile = args[0];
		String outputFile = args[1];
		
		List<List<Token>> tokens = ScannerPhase2.tokenizeFile(inputFile);
		writeTokens(tokens, inputFile, outputFile);
		
		int numTab = 0;
		List<Token> tokens1 = new ArrayList<>();
		for (List<Token> i : tokens) {
			for (Token j : i) {
				tokens1.add(j);
			}
		}
		
		TreeNode node = new TreeNode(tokens1.get(0), tokens1.get(0));
		node = node.parseExpr(tokens1, 0);
		writeAST(node, outputFile, numTab);
		
		Stack<Token> stack = new Stack<>();
		preOrder(node, stack);
		
		writeResult(stack, node, outputFile);
	}

}
