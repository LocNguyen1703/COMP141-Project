/*
names: Loc Nguyen, Phuoc Nguyen
Project Phase 2.1 (PR2.1): Parser for expressions
*/

package Evaluator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
// the way the Stack class works is it just extends from Vector - it just has some more 
// functions (like push, pop, peek) that make it operate somewhat like a Stack

import parserPhase2.ParserPhase2;
import scannerPhase2.ScannerPhase2.Token;
import scannerPhase2.ScannerPhase2.TokenType;

public class evaluator extends ParserPhase2{

	List<Token> tokens = new ArrayList<>();
	
	Stack<Token> stack = new Stack<Token>();
	
	
	public evaluator() {
		// TODO Auto-generated constructor stub
	}
	
	public Stack<Token> evaluateTop3Expr (Stack<Token> stack) {
		if (stack.peek().getType()!=TokenType.NUMBER || stack.elementAt(stack.size()-1).getType()!=TokenType.NUMBER || stack.elementAt(stack.size()-2).getType()!=TokenType.NUMBER) {
			return stack;
		}
		Token token1 = stack.peek();
		Token token2 = stack.elementAt(stack.size()-1);
		Token token3 = stack.elementAt(stack.size()-2);
		Token result = 
		
		stack.pop();
		stack.pop();
		stack.pop();
		
		return stack;
	}
	
	public void preOrder(TreeNode node, Stack<Token> stack) {
		if (node == null) return;
		
		//should I create a stack of Tokens or of TreeNodes??
		stack.push(node.getDataToken());
		
		
		preOrder(node.getLeftChild(), stack);
		preOrder(node.getMidChild(), stack);
		preOrder(node.getRightChild(), stack);
	}
	
	
	
	
	
	
	public static void main(String[] args) throws IOException {
		
	}

}
