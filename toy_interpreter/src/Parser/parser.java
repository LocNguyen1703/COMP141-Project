/*
names: Loc Nguyen, Phuoc Nguyen
Project Phase 2.1 (PR2.1): Parser for expressions
*/
package Parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import scannerPhase2.ScannerPhase2;
import scannerPhase2.ScannerPhase2.Token;
import scannerPhase2.ScannerPhase2.TokenType;

public class parser {
	
	public parser() {
		// TODO Auto-generated constructor stub
	}
	
	public static class TreeNode {
		public TreeNode left; 
		public TreeNode mid;
		public TreeNode right; 
		Token dataToken;
		Token next_token; 
		int index;
		int numTabs;
		Boolean Error = false;
	
		public TreeNode(Token token, Token next_token) {
			this.dataToken = token;
			this.next_token = next_token;
			this.left = null;
			this.right = null;
			this.mid = null;
		}
		
		public TreeNode(Token token, Token next_token, TreeNode left, TreeNode mid, TreeNode right, int numTabs) {
			this.dataToken = token;
			this.next_token = next_token;
			this.left = left;
			this.right = right;
			this.mid = mid;
			this.numTabs = numTabs;
		}
		
		public void setDataToken(Token token) {
			this.dataToken = token;
		}
		
		public void setRightChild(TreeNode node) {
			this.right = node;
		}
		
		public void setLeftChild(TreeNode node)	{
			this.left = node;
		}
		
		public void setMidChild (TreeNode node) {
			this.mid = node;
		}
		
		public void setNextToken (Token token) {
			this.next_token = token;
		}
		
		public void setNumTabs (int numTabs) {
			this.numTabs = numTabs;
		}
		
		public void setErrorDetection (Boolean error) {
			this.Error = error;
		}
		
		public TreeNode getRightChild() {
			return this.right;
		}
		
		public TreeNode getLeftChild() {
			return this.left;
		}
		
		public TreeNode getMidChild() {
			return this.mid;
		}
		
		public Token getDataToken() {
			return this.dataToken;
		}
		
		public Boolean getError() {
			return this.Error;
		}
		
		public Token consumeToken(List<Token> tokens) {
			if (index+1 < tokens.size()) {
				index ++; 
				next_token = tokens.get(index);
			}
			
			return next_token;
			
		}
		
		//we dont set parameter as List<List<Tokens>> but instead List<Tokens> --> this way the function 
		// is simpler - it's only trying to parse 1 line at a time
		
		//Addition
		public TreeNode parseExpr(List<Token> tokens, int numTabs) {
			TreeNode t = parseTerm(tokens, numTabs + 1);
			//problem (idk if it is a problem): there might be smthin wrong w/ getValue (I checked in debug
			//console and next_token's value IS "+", but it still didn't match the while loop's condition and 
			//it jumped out of while loop and returned...
			
			while (next_token.getValue().equals("+")) {
				Token temp = next_token;
				Token e = consumeToken(tokens);
				//I'm bothered by the new TreeNode thing - do I create a void method to insert Node instead of using constructor?
				//problem - since there's no setting for next_token in constructor, the next_token automatically gets assigned to null
				//fix: i tried setting next_token to the same value as token in constructor --> problem: next_token is supposed to be 
				//1 index ahead of token, but now it's not --> it's not detecting any operators I think
				
				//this if statement is to check whether consumeToken returns the same next_token
				//if (temp == e) return new TreeNode(temp, e, t, null, null, numTabs + 1);
				
//				if ((t.getLeftChild() == null || t.getRightChild() == null) && t.getDataToken().getType() == TokenType.SYMBOL) {
//					try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true))) {
//						bw.write("error");
//						bw.newLine();
//						break;
//					}
//					catch(IOException a) {
//						a.printStackTrace();
//					}
//				}
				
				t = new TreeNode(temp, e, t, null, parseTerm(tokens, numTabs + 1), numTabs + 1);
				
				if (t.getLeftChild() == null || t.getRightChild() == null) {
					t.setErrorDetection(true);
					return t;
				}
			}
			return t;
		}

		//Subtraction
		public TreeNode parseTerm(List<Token> tokens, int numTabs) {
			TreeNode t = parseFactor(tokens, numTabs + 1);
			while (next_token.getValue().equals("-")) {
				Token temp = next_token;
				Token e = consumeToken(tokens);
				t = new TreeNode(temp, e, t, null, parseFactor(tokens, numTabs + 1), numTabs + 1);
				
				if (t.getLeftChild() == null || t.getRightChild() == null) {
					t.setErrorDetection(true);
					return t;
				}
			}
			return t;
		}
		
		//Division
		public TreeNode parseFactor(List<Token> tokens, int numTabs) { 
			TreeNode t = parsePiece(tokens, numTabs + 1);
			while (next_token.getValue().equals("/")) {
				Token temp = next_token;
				Token e = consumeToken(tokens);
				t = new TreeNode(temp, e, t, null, parsePiece(tokens, numTabs + 1), numTabs + 1);
				
				if (t.getLeftChild() == null || t.getRightChild() == null) {
					t.setErrorDetection(true);
					return t;
				}
			}
			return t;
		}
		
		//Multiplication
		public TreeNode parsePiece(List<Token> tokens, int numTabs) {
			TreeNode t = parseElement(tokens, numTabs + 1);
			while (next_token.getValue().equals("*")) {
				Token temp = next_token;
				Token e =consumeToken(tokens);
				t = new TreeNode(temp, e, t, null, parseElement(tokens, numTabs + 1), numTabs + 1);
				
				if (t.getLeftChild() == null || t.getRightChild() == null) {
					t.setErrorDetection(true);
					return t;
				}
			}
			return t;
		}
		
		//parentheses or Number/Identifier
		public TreeNode parseElement(List<Token> tokens, int numTabs) {
			if (next_token.getValue().equals("(")) {
				consumeToken(tokens);
				TreeNode t = parseExpr(tokens, numTabs + 1);
				if (next_token.getValue().equals(")")) {
					consumeToken(tokens);
					return t;
				}
				else {
					return null;
				}
			}
			
			else if (next_token.getType() == TokenType.IDENTIFIER) {
				Token temp = next_token; 
				Token e = consumeToken(tokens);
				return new TreeNode (temp, e);
			}
			
			else if (next_token.getType() == TokenType.NUMBER) {
				Token temp = next_token; 
				Token e =consumeToken(tokens);
				return new TreeNode (temp, e);
			}
			
			//if none of the if-statements work --> we return null --> when we iterate and print Tree we print error there and stop
			return null;
		}
	}
	
	public static void checkError(TreeNode node) {
		if (node.getError() == true) {
			if (node.getLeftChild() != null) node.getLeftChild().setErrorDetection(true);
			if (node.getRightChild() != null) node.getRightChild().setErrorDetection(true);
		}
		else if ((node.getLeftChild() != null || node.getRightChild() != null) && (node.getLeftChild().getError() == true || node.getRightChild().getError() == true)) {
			node.setErrorDetection(true);
		}
		
		if (node.getLeftChild() != null) checkError(node.getLeftChild());
		if (node.getRightChild() != null) checkError(node.getRightChild());
	}
	
	public static void writeAST(TreeNode node, String outputFile, int numTabs) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true));
		checkError(node);
		if (node.getError() || (node.getLeftChild() != null && node.getLeftChild().getError()) || (node.getRightChild() != null && node.getRightChild().getError())) {
			bw.write("error: incorrect syntax");
//			if (node.getLeftChild() != null) node.getLeftChild().setErrorDetection(true);
//			if (node.getRightChild() != null) node.getRightChild().setErrorDetection(true);
			//this would set every other node's error to true, which is cool, but
			//for problems where the error node is not at the root node (like 3 + 4 - + 5), it doesn't work
			// --> how can I access the node before?? 
			// maybe create a separate void recursive function, keep calling it recursively and checking
			// to see if a node's children are error - if reach a error child node, set the node to error
			bw.close();
		}
		else {
			for (int i = 0; i < numTabs; i++) {
				bw.write("\t");
			}
				
			bw.write(node.getDataToken().getValue() + ": " + node.getDataToken().getType());
			bw.newLine();
			bw.close();
			if (node.getLeftChild() != null) writeAST(node.getLeftChild(), outputFile, numTabs+1);
			if (node.getRightChild() != null) writeAST(node.getRightChild(), outputFile, numTabs+1);
		}
	}
	
	public static void writeTokens(List<List<Token>> tokens, String inputFile, String outputFile) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))){
			String line;
			// printing out the input line
			try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
				for (List<Token> list : tokens) {
					if ((line = br.readLine()) != null) bw.write("Line: " + line);
					bw.newLine();
					for (Token token : list) {
						bw.write(token.getValue() + ": " + token.getType());
						bw.newLine();
					}
					bw.newLine();
				}
				bw.write("AST:");
				bw.newLine();
            }
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String inputFile = args[0];
		String outputFile = args[1];
		
		List<List<Token>> tokens = ScannerPhase2.tokenizeFile(inputFile);
		writeTokens(tokens, inputFile, outputFile);
		int numTab = 0;
		for (List<Token> i : tokens) {
			TreeNode node = new TreeNode(i.get(0), i.get(0));
			node = node.parseExpr(i, 0);
			writeAST(node, outputFile, numTab);
		}
	}
}
