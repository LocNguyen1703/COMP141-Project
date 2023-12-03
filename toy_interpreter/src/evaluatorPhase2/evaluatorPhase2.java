package evaluatorPhase2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import parserPhase2.ParserPhase2;
import scannerPhase2.ScannerPhase2;
import scannerPhase2.ScannerPhase2.Token;
import scannerPhase2.ScannerPhase2.TokenType;

public class evaluatorPhase2 extends ParserPhase2{

	public evaluatorPhase2() {
		// TODO Auto-generated constructor stub
	}
	public static Stack<Token> evaluateTop3Expr (Stack<Token> stack, Map<String, Integer> memory) {
		if (stack.size() < 3 || (stack.peek().getType()!=TokenType.NUMBER && stack.peek().getType()!=TokenType.IDENTIFIER) || (stack.elementAt(stack.size()-2).getType()!=TokenType.NUMBER && stack.elementAt(stack.size()-2).getType()!=TokenType.IDENTIFIER) || stack.elementAt(stack.size()-3).getType()!=TokenType.SYMBOL) {
			return stack;
		}
		
		else {
			Token token1 = stack.peek();
			Token token2 = stack.elementAt(stack.size()-2);
			int value1 = 0; 
			int value2 = 0;
			Token token3 = stack.elementAt(stack.size()-3);
			String r = "";
			
			if (token1.getType()==TokenType.IDENTIFIER) {
//				for (Entry<String, Integer> entry : memory.entrySet()) {
//					if (entry.getKey().equals(token1.getValue())) value1 = entry.getValue();
//				}
				if(memory.containsKey(token1.getValue())) value1 = memory.get(token1.getValue());
			}
			else value1 = Integer.valueOf(token1.getValue());
			
			if (token2.getType()==TokenType.IDENTIFIER) {
//				for (Entry<String, Integer> entry : memory.entrySet()) {
//					if (entry.getKey().equals(token2.getValue())) value2 = entry.getValue();
//				}
				if(memory.containsKey(token2.getValue())) value2 = memory.get(token2.getValue());
			}
			else value2 = Integer.valueOf(token2.getValue());	
			
			if (token3.getValue().equals("*")) {
				r = String.valueOf(value2*value1);	
			}
			else if (token3.getValue().equals("/")) {
				r = String.valueOf(value2/value1);	
			}
			else if (token3.getValue().equals("+")) {
				r = String.valueOf(value2+value1);		
			}
			else if (token3.getValue().equals("-")) {
				r = String.valueOf(value2-value1);			
			}
			stack.pop();
			stack.pop();
			stack.pop();
			stack.push(new Token (TokenType.NUMBER, r));
			
			return stack;
		}		
	}
	
	// this preOrder function is now used to evaluate expression alone - we need a new function to traverse through AST and evaluate sequencing, if-statements & while statements
	public static void preOrder(TreeNode node, Stack<Token> stack, Map<String, Integer> memory) {
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
	
	// a helper function to delete node when traversing AST
	public static void deleteNode (TreeNode node) {
//		if (node.getLeftChild() == null && node.getMidChild() == null && node.getRightChild() == null) node = null;
//		else {
//			node.setLeftChild(null);
//			node.setRightChild(null);
//			node.setMidChild(null);
//		}
////		node = null;
//		deleteNode (node.getLeftChild());
//		deleteNode (node.getMidChild());
//		deleteNode (node.getRightChild());
//		node = null;
		
		// what happens when I nullify node's children and then itself? if its children had children what would happen to those children?
		if (node.getLeftChild()!= null) node.setLeftChild(null);
		if (node.getMidChild()!= null) node.setRightChild(null);
		if (node.getRightChild()!= null) node.setMidChild(null);
		if(node != null) node = null;
	}
	
	// thought: maybe we create separate helper functions to traverse semicolon, while subtree and if subtree individually?
	// thought: also, should I even do recursive for traverseAST or should I just do a while loop?
	public static TreeNode evaluateWhileStatement (TreeNode node, Stack<Token> stack, Map<String, Integer> memory) {
		if (node == null) return null;
		preOrder(node.getLeftChild(), stack, memory);
		System.out.println(stack.peek().getValue());
		if (Integer.valueOf(stack.peek().getValue())<=0) {
			deleteNode (node);
			return null;
		}
		TreeNode rightChild = new TreeNode(node.getRightChild().getDataToken(), node.getRightChild().getNextToken(), node.getRightChild().getLeftChild(), node.getRightChild().getMidChild(), node.getRightChild().getRightChild());
		TreeNode t = new TreeNode(new Token(TokenType.SYMBOL, ";"), null, rightChild, null, node);
//		evaluateSequencing(t, stack, memory);
//		evaluateWhileStatement(node, stack, memory);
		// don't know if this stack.clear() line will mess the result up 
		stack.clear();
		return t;
	}
	
	public static boolean evaluateIfStatement (TreeNode node, Stack<Token> stack, Map<String, Integer> memory) {
//		if (node == null) return;
		preOrder (node.getLeftChild(), stack, memory);
		stack.clear();
		if (Integer.valueOf(stack.peek().getValue()) > 0) {
//			TreeNode temp = node.getMidChild();
//			TreeNode temp = new TreeNode (node.getMidChild().getDataToken(), node.getMidChild().getNextToken(), node.getMidChild().getLeftChild(), node.getMidChild().getMidChild(), node.getMidChild().getRightChild());
//			deleteNode (node.getLeftChild());
//			deleteNode (node.getMidChild());
//			deleteNode (node.getRightChild());
//			node.setDataToken(node.getMidChild().getDataToken());
//			node.setNextToken(node.getMidChild().getNextToken());
//			node.setLeftChild(node.getMidChild().getLeftChild());
//			node.setRightChild(node.getMidChild().getRightChild());
//			node.setMidChild(node.getMidChild().getMidChild());
//			evaluateSequencing(node, stack, memory);
			return true;
		}
		else {
//			TreeNode temp = node.getRightChild();
//			TreeNode temp = new TreeNode (node.getRightChild().getDataToken(), node.getRightChild().getNextToken(), node.getRightChild().getLeftChild(), node.getRightChild().getMidChild(), node.getRightChild().getRightChild());
//			deleteNode (node.getLeftChild());
//			deleteNode (node.getMidChild());
//			deleteNode (node.getRightChild());
//			node = temp;
//			node.setDataToken(node.getRightChild().getDataToken());
//			node.setNextToken(node.getRightChild().getNextToken());
//			node.setLeftChild(node.getRightChild().getLeftChild());	
//			node.setMidChild(node.getRightChild().getMidChild());
//			node.setRightChild(node.getRightChild().getRightChild());
//			evaluateSequencing(node, stack, memory);
			return false;
		}
	}
	
	public static void evaluateSequencing (TreeNode node, Stack<Token> stack, Map<String, Integer> memory) {
		if (node.getLeftChild().getDataToken().getValue().equals(";")) {
			evaluateSequencing (node.getLeftChild(), stack, memory);
		}
		else if (node.getLeftChild().getDataToken().getValue().equals(":=")) {
			evaluateAssignment(node.getLeftChild(), stack, memory);
			// maybe I don't set temp to right child (temp might be come a REFERENCE to right child)
			// but create a new node so that it is a COPY of the right child
//			TreeNode temp = node.getRightChild();
//			TreeNode temp = new TreeNode(node.getRightChild().getDataToken(), node.getRightChild().getNextToken(),node.getRightChild().getLeftChild(), node.getRightChild().getMidChild(), node.getRightChild().getRightChild());
//			node.setLeftChild(null);
//			node.setRightChild(null);
//			node = temp;
			node.setDataToken(node.getRightChild().getDataToken());
			node.setNextToken(node.getRightChild().getNextToken());
			node.setLeftChild(node.getRightChild().getLeftChild());
			node.setMidChild(node.getRightChild().getMidChild());
			node.setRightChild(node.getRightChild().getRightChild());
		}
		else if (node.getLeftChild().getDataToken().getValue().equals("if")) {
			boolean ans = evaluateIfStatement(node.getLeftChild(), stack, memory);
			if (ans) {
				node.setLeftChild(node.getLeftChild().getMidChild());
			}
			else {
				node.setLeftChild(node.getLeftChild().getRightChild());	
			}
		}

		else if (node.getLeftChild().getDataToken().getValue().equals("while")) {
			evaluateWhileStatement(node.getLeftChild(), stack, memory);
		}
	}
	
	public static void evaluateAssignment (TreeNode node, Stack<Token> stack, Map<String, Integer> memory) {
		if (node == null) return;
		preOrder(node.getRightChild(), stack, memory);
		//stack.peek should return a single integer
		if (memory.containsKey(node.getLeftChild().getDataToken().getValue())==false) memory.put(node.getLeftChild().getDataToken().getValue(), Integer.valueOf(stack.peek().getValue()));
//		for (Entry<String, Integer> entry : memory.entrySet()) {
//			if (entry.getKey().equals(node.getLeftChild().getDataToken().getValue())) memory.replace(entry.getKey(), Integer.valueOf(stack.peek().getValue()));
//		}
		else memory.replace(node.getLeftChild().getDataToken().getValue(), Integer.valueOf(stack.peek().getValue()));
		
//		deleteNode(node); apparently deleteNode() doesn't work right here
//		TreeNode temp = node.getRightChild();
//		deleteNode(node.getLeftChild());
//		deleteNode(node.getRightChild());
//		node = temp;
//		deleteNode(temp);
		/*
		 problem: the node accessed here is the 1st ":=" node, and so this logic of deletion doesn't make 
		 sense (I want to delete the last ";" node's left child, which should be this ":=" node, and then replace 
		 the ";" node with its right child
		 solution: should I create a link to parent for every node?
		 */
		stack.clear();
	}
	
	// new function to traverse through AST and evaluate sequencing, if-statements & while statements
	public static void traverseAST (TreeNode node, Stack<Token> stack, Map<String, Integer> memory ) {
		// idk if this is correct but my guess is it's gonna keep traversing until AST empty i.e. node itself is null? (maybe add a check for its children too?)
		// so maybe when u use this function outside use it in a loop?
		
		//also this if condition might make traverseAST exit before it can traverse through the whole AST
//		if (node == null || node.getError() || (node.getLeftChild() != null && node.getLeftChild().getError()) || (node.getRightChild() != null && node.getRightChild().getError()))return;
//		if (node.getDataToken().getValue().equals(";")) {
//			if (node.getLeftChild().getDataToken().getValue().equals(";")) traverseAST(node.getLeftChild(), stack, memory);
//			else if (node.getLeftChild().getDataToken().getValue().equals(":=")){
////				preOrder(node.getLeftChild().getRightChild(), stack, memory);
////				//stack.peek should return a single integer
////				memory.put(node.getLeftChild().getLeftChild().getDataToken(), Integer.valueOf(stack.peek().getValue()));
////				TreeNode temp = node.getRightChild();
////				deleteNode(node.getLeftChild());
////				deleteNode(node.getRightChild());
////				node = temp;
//				evaluateAssignment (node.getLeftChild(), stack, memory);
//			}	
//		}
		while (node != null) {
			if (node.getDataToken().getValue().equals(";")) evaluateSequencing(node, stack, memory);
			else if (node.getDataToken().getValue().equals(":=")) {
				evaluateAssignment (node, stack, memory);
				node = null;
			}
			else if (node.getDataToken().getValue().equals("while")) {
				TreeNode t = evaluateWhileStatement (node, stack, memory);
				node = t;
			}
			else if (node.getDataToken().getValue().equals("if")) evaluateIfStatement (node, stack, memory);	
		}
	}
	
//	public static void writeResult(Stack<Token> stack, TreeNode node, String outputFile) throws IOException {
//		if (node.getError() || (node.getLeftChild() != null && node.getLeftChild().getError()) || (node.getRightChild() != null && node.getRightChild().getError())) return;
//		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true));
//		bw.newLine();
//		bw.write("result = " + stack.peek().getValue());
//		bw.close();
//	}
	
	public static void writeResult(Map<String, Integer> memory, TreeNode node, String outputFile) throws IOException {
		if (node.getError() || (node.getLeftChild() != null && node.getLeftChild().getError()) || (node.getRightChild() != null && node.getRightChild().getError())) return;
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true));
		for (Entry<String, Integer> entry : memory.entrySet()) {
			bw.newLine();
			bw.write(entry.getKey() + " = " + String.valueOf(entry.getValue()));
		}
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
		node = node.parseStatement(tokens1);
		writeAST(node, outputFile, numTab);
		
		Stack<Token> stack = new Stack<>();
		Map<String, Integer> memory = new HashMap<String, Integer>();
		traverseAST(node, stack, memory);
		
		writeResult(memory, node, outputFile);
	}
}
