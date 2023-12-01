package evaluatorPhase2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import Evaluator.evaluator;
import Parser.parser.TreeNode;
import parserPhase2.ParserPhase2;
import scannerPhase2.ScannerPhase2;
import scannerPhase2.ScannerPhase2.Token;
import scannerPhase2.ScannerPhase2.TokenType;

public class evaluatorPhase2 extends ParserPhase2{

	public evaluatorPhase2() {
		// TODO Auto-generated constructor stub
	}
	public static Stack<Token> evaluateTop3Expr (Stack<Token> stack, Map<Token, Integer> memory) {
		if (stack.size() < 3 || stack.peek().getType()!=TokenType.NUMBER || stack.elementAt(stack.size()-2).getType()!=TokenType.NUMBER || stack.elementAt(stack.size()-3).getType()!=TokenType.SYMBOL) {
			return stack;
		}
		
		else {
			Token token1 = stack.peek();
			Token token2 = stack.elementAt(stack.size()-2);
			Token token3 = stack.elementAt(stack.size()-3);
			String r = "";
			
			if (token3.getValue().equals("*")) r = String.valueOf(memory.get(token2)*memory.get(token1));
			else if (token3.getValue().equals("/")) r = String.valueOf(memory.get(token2)/memory.get(token1));
			else if (token3.getValue().equals("+")) r = String.valueOf(memory.get(token2)+memory.get(token1));
			else if (token3.getValue().equals("-")) r = String.valueOf(memory.get(token2)-memory.get(token1));			
			stack.pop();
			stack.pop();
			stack.pop();
			stack.push(new Token (TokenType.NUMBER, r));
			
			return stack;
		}		
	}
	
	// this preOrder function is now used to evaluate expression alone - we need a new function to traverse through AST and evaluate sequencing, if-statements & while statements
	public static void preOrder(TreeNode node, Stack<Token> stack, Map<Token, Integer> memory) {
		if (node == null || node.getError() || (node.getLeftChild() != null && node.getLeftChild().getError()) || (node.getRightChild() != null && node.getRightChild().getError()))return;
		
		//should I create a stack of Tokens or of TreeNodes??
		stack.push(node.getDataToken());
		stack = evaluateTop3Expr (stack, memory);
		
		preOrder(node.getLeftChild(), stack, memory);
		stack = evaluateTop3Expr (stack, memory);
		preOrder(node.getMidChild(), stack, memory);
		stack = evaluateTop3Expr (stack, memory);
		preOrder(node.getRightChild(), stack, memory);
		stack = evaluateTop3Expr (stack, memory);
	}
	
	// I don't think this would work... but the idea is to have a helper function to delete node when traversing AST
	public static void deleteNode (TreeNode node) {
//		if (node.getLeftChild() == null && node.getMidChild() == null && node.getRightChild() == null) node = null;
//		else {
//			node.setLeftChild(null);
//			node.setRightChild(null);
//			node.setMidChild(null);
//			
//		}
		node = null;
		deleteNode (node.getLeftChild());
		deleteNode (node.getMidChild());
		deleteNode (node.getRightChild());
	}
	
	// thought: maybe we create separate helper functions to traverse semicolon, while subtree and if subtree individually?
	// thought: also, should I even do recursive for traverseAST or should I just do a while loop?
	public static void evaluateWhileStatement (TreeNode node, Stack<Token> stack, Map<Token, Integer> memory) {
		preOrder(node, stack, memory);
		if (Integer.valueOf(stack.peek().getValue())<=0) {
			deleteNode (node);
			return;
		}
		TreeNode t = new TreeNode(new Token(TokenType.SYMBOL, ";"), null, node.getRightChild(), null, node);
		evaluateSequencing(t, stack, memory);
		evaluateWhileStatement(node, stack, memory);
	}
	
	public static void evaluateIfStatement (TreeNode node, Stack<Token> stack, Map<Token, Integer> memory) {
		
	}
	
	public static void evaluateSequencing (TreeNode node, Stack<Token> stack, Map<Token, Integer> memory) {
		if (node.getLeftChild().getDataToken().getValue().equals(";")) {
			evaluateSequencing (node.getLeftChild(), stack, memory);
		}
		else if (node.getLeftChild().getDataToken().getValue().equals(":=")) {
			evaluateAssignment(node.getLeftChild(), stack, memory);
		}
		else if (node.getLeftChild().getDataToken().getValue().equals("if")) {
			evaluateIfStatement(node.getLeftChild(), stack, memory);
		}

		else if (node.getLeftChild().getDataToken().getValue().equals("while")) {
			evaluateWhileStatement(node.getLeftChild(), stack, memory);
		}
	}
	
	public static void evaluateAssignment (TreeNode node, Stack<Token> stack, Map<Token, Integer> memory) {
		preOrder(node.getRightChild(), stack, memory);
		//stack.peek should return a single integer
		memory.put(node.getLeftChild().getDataToken(), Integer.valueOf(stack.peek().getValue()));
		TreeNode temp = node.getRightChild();
		deleteNode(node.getLeftChild());
		deleteNode(node.getRightChild());
		node = temp;
	}
	
	// new function to traverse through AST and evaluate sequencing, if-statements & while statements
	public static void traverseAST (TreeNode node, Stack<Token> stack, Map<Token, Integer> memory ) {
		// idk if this is correct but my guess is it's gonna keep traversing until AST empty i.e. node itself is null? (maybe add a check for its children too?)
		// so maybe when u use this function outside use it in a loop?
		
		//also this if condition might make traverseAST exit before it can traverse through the whole AST
		if (node == null || node.getError() || (node.getLeftChild() != null && node.getLeftChild().getError()) || (node.getRightChild() != null && node.getRightChild().getError()))return;
		if (node.getDataToken().getValue().equals(";")) {
			if (node.getLeftChild().getDataToken().getValue().equals(";")) traverseAST(node.getLeftChild(), stack, memory);
			else if (node.getLeftChild().getDataToken().getValue().equals(":=")){
//				preOrder(node.getLeftChild().getRightChild(), stack, memory);
//				//stack.peek should return a single integer
//				memory.put(node.getLeftChild().getLeftChild().getDataToken(), Integer.valueOf(stack.peek().getValue()));
//				TreeNode temp = node.getRightChild();
//				deleteNode(node.getLeftChild());
//				deleteNode(node.getRightChild());
//				node = temp;
				evaluateAssignment (node.getLeftChild(), stack, memory);
			}
			
		}
		
		
		
		
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
		node = node.parseExpr(tokens1);
		writeAST(node, outputFile, numTab);
		
		Stack<Token> stack = new Stack<>();
		Map<Token, Integer> memory = new HashMap<Token, Integer>();
		preOrder(node, stack, memory);
		
		writeResult(stack, node, outputFile);
	}
	
	
	
}
