package Parser;

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
	
		public TreeNode(Token token) {
			this.dataToken = token;
			this.left = null;
			this.right = null;
			this.mid = null;
		}
		
		public TreeNode(Token token, TreeNode left, TreeNode right, TreeNode mid) {
			this.dataToken = token;
			this.left = left;
			this.right = right;
			this.mid = mid;
		}
		
		public void setRightChild(TreeNode node) {
			this.right = node;
		}
		
		public void setLeftChild(TreeNode node)	{
			this.left = node;
		}
		
		public void consumeToken(List<Token> tokens) {
			if (index+1 < tokens.size()) {
				index ++; 
				next_token = tokens.get(index);
			}
			
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
		public TreeNode parseExpr(List<Token> tokens) {
//			Token e = findToken(tokens, "+");
			TreeNode t = parseTerm(tokens);
			while (next_token.getValue() == "//+") {
				consumeToken(tokens);
				t = new TreeNode(next_token, t, null, parseTerm(tokens));
			}
			
			return t;
		}

		//Subtraction
		public TreeNode parseTerm(List<Token> tokens) {
			TreeNode t = parseFactor(tokens);
			while (next_token.getValue() == "-") {
				consumeToken(tokens);
				t = new TreeNode(next_token, t, null, parseFactor(tokens));
			}
			
			return t;
			
		}
		
		//Division
		public TreeNode parseFactor(List<Token> tokens) { 
			TreeNode t = parsePiece(tokens);
			while (next_token.getValue() == "/") {
				consumeToken(tokens);
				t = new TreeNode(next_token, t, null, parseTerm(tokens));
			}
			
			return t;
		}
		
		
		//Multiplication
		public TreeNode parsePiece(List<Token> tokens) {
			TreeNode t = parseElement(tokens);
			while (next_token.getValue() == "/") {
				consumeToken(tokens);
				t = new TreeNode(next_token, t, null, parseElement(tokens));
			}
			
			return t;
		}
		
		//parentheses or Number/Identifier
		public TreeNode parseElement(List<Token> tokens) {
			if (next_token.getValue() == "(") {
				consumeToken(tokens);
				TreeNode t = parseExpr(tokens);
				if (next_token.getValue() == ")") {
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
				consumeToken(tokens);
				return new TreeNode (temp);
			}
			
			else if (next_token.getType() == TokenType.NUMBER) {
				Token temp = next_token; 
				consumeToken(tokens);
				return new TreeNode (temp);
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
	
	
//	public static void AST(List<List<Token>> tokens) {
//		
//	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String inputFile = args[0];
		String outputFile = args[1];
		
		List<List<Token>> tokens = ScannerPhase2.tokenizeFile(inputFile);
		for (List<Token> i : tokens) {
			TreeNode node = new TreeNode(i.get(0));
			node.parseExpr(i);
		}
		
	}	

}
