package Parser;

import java.io.IOException;
import java.util.List;

import scannerPhase2.ScannerPhase2;
import scannerPhase2.ScannerPhase2.Token;

public class parser {
	
	public parser() {
		// TODO Auto-generated constructor stub
	}
	
	public class TreeNode {
		public TreeNode left; 
		public TreeNode mid;
		public TreeNode right; 
		Token token;
	
		public TreeNode(Token token) {
			this.token = token;
			this.left = null;
			this.right = null;
			this.mid = null;
		}
		
		public void setRightChild(TreeNode node) {
			this.right = node;
		}
		
		public void setLeftChild(TreeNode node)	{
			this.left = node;
		}
	}

	public class Tree {
		public TreeNode root;
		
		public void createTree(List<List<Token>> tokens) {
			/*
			 idea: take a loook at list of tokens --> find the operators first
			 each operator token should be in-between numbers/identifiers --> if not, throw error & exit 
			 create nodes based on operators and left/right branches based on the numbers/identifiers they're in-between (and order of precedence)
			 */
			
			
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
		public void parseExpr(List<Token> tokens) {
			Token e = findToken(tokens, "+");
		}
		
		//Subtraction
		public void parseTerm(List<Token> tokens) {
			
		}
		
		//Division
		public void parseFactor(List<Token> tokens) { 
			
		}
		
		
		//Multiplication
		public void parsePiece(List<Token> tokens) {
			
		}
		
		//parentheses or Number/Identifier
		public void parseElement(List<Token> tokens) {
			
		}
		
	}
	
	
	public static void AST(List<List<Token>> tokens) {
		
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String inputFile = args[0];
	
		List<List<Token>> tokens = ScannerPhase2.tokenizeFile(inputFile);
		
		
	}	

}
