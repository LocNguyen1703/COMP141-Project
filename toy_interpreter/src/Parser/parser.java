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
		
//		public void insertNodeLeftRecursive (TreeNode node, TreeNode left, TreeNode right, TreeNode mid) {
//			
//		}
		
		public Token consumeToken(List<Token> tokens) {
			if (index+1 < tokens.size()) {
				index ++; 
				next_token = tokens.get(index);
			}
			
			return next_token;
			
		}
		
		public Token findToken(List<Token> tokens, String value) {
			for (Token t : tokens){
				if (t.getValue() == value) return t;
			}
			return null;
		}
		
		// I guess we dont set parameter as List<List<Tokens>> but instead List<Tokens> --> this way the function 
		// is simpler - it's only trying to parse 1 line at a time
		
		//Addition
		public TreeNode parseExpr(List<Token> tokens, int numTabs, String outputFile) {
//			Token e = findToken(tokens, "+");
			
			TreeNode t = parseTerm(tokens, numTabs + 1, outputFile);
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
				if ((t.getLeftChild() == null || t.getRightChild() == null) && t.getDataToken().getType() == TokenType.SYMBOL) {
					try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true))) {
						bw.write("error");
						bw.newLine();
						break;
					}
					catch(IOException a) {
						a.printStackTrace();
					}
				}
				t = new TreeNode(temp, e, t, null, parseTerm(tokens, numTabs + 1, outputFile), numTabs + 1);
			}
			
			return t;
		}

		//Subtraction
		public TreeNode parseTerm(List<Token> tokens, int numTabs, String outputFile) {
			TreeNode t = parseFactor(tokens, numTabs + 1, outputFile);
			while (next_token.getValue().equals("-")) {
				Token temp = next_token;
				Token e = consumeToken(tokens);
				t = new TreeNode(temp, e, t, null, parseFactor(tokens, numTabs + 1, outputFile), numTabs + 1);
			}
			
			return t;
			
		}
		
		//Division
		public TreeNode parseFactor(List<Token> tokens, int numTabs, String outputFile) { 
			TreeNode t = parsePiece(tokens, numTabs + 1, outputFile);
			while (next_token.getValue().equals("/")) {
				Token temp = next_token;
				Token e = consumeToken(tokens);
				t = new TreeNode(temp, e, t, null, parsePiece(tokens, numTabs + 1, outputFile), numTabs + 1);
			}
			
			return t;
		}
		
		//Multiplication
		public TreeNode parsePiece(List<Token> tokens, int numTabs, String outputFile) {
			TreeNode t = parseElement(tokens, numTabs + 1, outputFile);
			while (next_token.getValue().equals("*")) {
				Token temp = next_token;
				Token e =consumeToken(tokens);
				t = new TreeNode(temp, e, t, null, parseElement(tokens, numTabs + 1, outputFile), numTabs + 1);
			}
			
			return t;
		}
		
		//parentheses or Number/Identifier
		public TreeNode parseElement(List<Token> tokens, int numTabs, String outputFile) {
			if (next_token.getValue().equals("(")) {
				consumeToken(tokens);
				TreeNode t = parseExpr(tokens, numTabs + 1, outputFile);
				if (next_token.getValue().equals(")")) {
					consumeToken(tokens);
					return t;
				}
				else {
					//i guess in execution we don't print error here,
					//but we print error when we're actually iterating and printing the tree
					System.out.println("erorr"); 
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

//	public class Tree extends TreeNode{
//		public TreeNode root;
//		public Token next_token;
//		public int index;
//		
//		public Tree(TreeNode root, Token next_token, int index) {
//			super(next_token); //must call superclass constructor first before keep running child class constructor
//			this.root = root;
//			this.next_token = next_token;
//			this.index = index;
//		}
//		
//		//we're doing left-recursive here so --> token is root node, t is left, tree is right
//		public Tree createTree(Token token, Tree t, TreeNode mid, Tree tree) {
//			return null;
//			/*
//			 idea: take a loook at list of tokens --> find the operators first
//			 each operator token should be in-between numbers/identifiers --> if not, throw error & exit 
//			 create nodes based on operators and left/right branches based on the numbers/identifiers they're in-between (and order of precedence)
//			 */	
//			
//			
//		}
//		
//		//in theory, moves pointer of currenct_token to next token in token list
//		//in here, we just assign next_token to item of whatever index "index" integer is in token list 
//		public void consumeToken(List<Token> tokens) {
//			next_token = tokens.get(index);
//			index ++; 
//		}
//		
//		public Token findToken(List<Token> tokens, String value) {
//			for (Token t : tokens){
//				if (t.getValue() == value) return t;
//			}
//			return null;
//		}
//		
//		// I guess we dont set parameter as List<List<Tokens>> but instead List<Tokens> --> this way the function 
//		// is simpler - it's only trying to parse 1 line at a time
//		
//		//Addition
//		public Tree parseExpr(List<Token> tokens) {
////			Token e = findToken(tokens, "+");
//			Tree t = parseTerm(tokens);
//			while (next_token.getValue() == "+") {
//				consumeToken(tokens);
//				t = createTree(next_token, t, null, parseTerm(tokens));
//			}
//			
//			return t;
//		}
//
//		//Subtraction
//		public Tree parseTerm(List<Token> tokens) {
//			return null;
//			
//		}
//		
//		//Division
//		public Tree parseFactor(List<Token> tokens) { 
//			return null;
//		}
//		
//		
//		//Multiplication
//		public Tree parsePiece(List<Token> tokens) {
//			return null;
//		}
//		
//		//parentheses or Number/Identifier
//		public Tree parseElement(List<Token> tokens) {
//			return null;
//		}
//		
//	}
	
	
	public static void writeAST(TreeNode node, String outputFile, int numTabs) throws IOException, NullPointerException {
//		System.out.println(node.getDataToken().getValue() + ": " + node.getDataToken().getType());
		try {
			try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true))) {
				
				if ((node.getLeftChild() == null || node.getRightChild() == null) && node.getDataToken().getType() == TokenType.SYMBOL) {
					bw.write(node.getDataToken().getValue() + ": " + node.getDataToken().getType());
					bw.newLine();
					bw.write("error");
					bw.newLine();
				}
				else {
					for (int i = 0; i < numTabs; i++) {
						bw.write("\t");
					}
					
					bw.write(node.getDataToken().getValue() + ": " + node.getDataToken().getType());
					bw.newLine();
				}
			}
			if (node.getLeftChild() != null) writeAST(node.getLeftChild(), outputFile, numTabs+1);
			if (node.getRightChild() != null) writeAST(node.getRightChild(), outputFile, numTabs+1);
		}
		catch(NullPointerException e) {
			e.getStackTrace();
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
//		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
		writeTokens(tokens, inputFile, outputFile);
		int numTab = 0;
		for (List<Token> i : tokens) {
			TreeNode node = new TreeNode(i.get(0), i.get(0));
//			System.out.println(node.getDataToken().getValue());
			node = node.parseExpr(i, 0, outputFile);
//			System.out.println(node.getDataToken().getValue());
//			System.out.println(node.getLeftChild().getDataToken().getValue());
//			System.out.println(node.getRightChild().getDataToken().getValue());
			writeAST(node, outputFile, numTab);
		}
	}
}
