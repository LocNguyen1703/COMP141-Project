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
		}
	}

	public class Tree {
		public TreeNode root;
	}
	
	public void createTree(List<List<Token>> tokens) {
		/*
		 idea: take a loook at list of tokens --> find the operators first
		 each operator token should be in-between numbers/identifiers --> if not, throw error & exit 
		 create nodes based on operators and left/right branches based on the numbers/identifiers they're in-between (and order of precedence)
		 */
	}
	
	
	public static void AST(List<List<Token>> tokens) {
		
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String inputFile = args[0];
	
		List<List<Token>> tokens = ScannerPhase2.tokenizeFile(inputFile);
		
		
	}	

}
