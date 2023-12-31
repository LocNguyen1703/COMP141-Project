package scanner;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class scanner {
	
	static class Token {
        TokenType type;
        String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }
    }
	
	enum TokenType {
		IDENTIFIER, NUMBER, SYMBOL, ERROR
	}
	
	public static List<List<Token>> tokenizeFile(String inputFile) throws IOException {
		List<List<Token>> result_tokens = new ArrayList<>();
		Pattern identifier = Pattern.compile("([a-zA-Z])([a-zA-Z0-9])*");
		Pattern num = Pattern.compile("[0-9]+");
		Pattern symbol = Pattern.compile("\\+|\\-|\\*|/|\\(|\\)");
		Pattern error = Pattern.compile("&|#|\\.|\\,|\\]|\\[");
		
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				List<Token> separated_result_tokens = new ArrayList<>();
		        for (String i : line.split(" ")) {
		            Matcher match_identifier = identifier.matcher(i);
		            Matcher match_num = num.matcher(i);
		            Matcher match_symbol = symbol.matcher(i);
		            Matcher match_error = error.matcher(i);
		            
		            if (match_identifier.matches()) separated_result_tokens.add(new Token(TokenType.IDENTIFIER,i));
		            else if (match_num.matches()) separated_result_tokens.add(new Token(TokenType.NUMBER, i));
		            else if (match_symbol.matches()) separated_result_tokens.add(new Token(TokenType.SYMBOL, i));
		            
		            else {
		            	while (match_identifier.find()) {
		            		separated_result_tokens.add(new Token(TokenType.IDENTIFIER, match_identifier.group()));
		            		//break;
		                }
		                while (match_num.find()) {
		                	separated_result_tokens.add(new Token(TokenType.NUMBER, match_num.group()));
		                	//break;
		                }
		                while (match_symbol.find()) {
		                	separated_result_tokens.add(new Token(TokenType.SYMBOL, match_symbol.group()));
		                	//break;
		                }
		                while (match_error.find()) {
		                	separated_result_tokens.add(new Token(TokenType.ERROR, match_error.group()));
		                	//break;
			            }	
		            }	            	
		        }
		        result_tokens.add(separated_result_tokens);
			}
		}
		return result_tokens;
	}
	
	public static void writeFile(List<List<Token>> tokens, String inputFile, String outputFile) throws IOException {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))){
			String line;
			// printing out the input line
			try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
				for (List<Token> list : tokens) {
					if ((line = br.readLine()) != null) bw.write("Line: " + line);
					bw.newLine();
					for (Token token : list) {
						bw.write(token.value + ": " + token.type);
						bw.newLine();
					}
					bw.newLine();
				}
            }
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//this was supposed to check if the command is in correct form, but I commented this out and test it with my pre-defined files
		if (args.length != 2) {
			System.err.println("Usage: java FileScanner <input_file> <output_file>");
			System.exit(1);
		}
		//this is for the program to take in input file and spit out an output file
//		String inputFile = "C:\\My most used folder on this lap\\Studyin\\Second year [2nd sem Aug - Nov]\\COMP 141\\Project\\toy_interpreter\\src\\scanner\\test_input.txt";
//		String outputFile = "C:\\My most used folder on this lap\\Studyin\\Second year [2nd sem Aug - Nov]\\COMP 141\\Project\\toy_interpreter\\src\\scanner\\test_output.txt";
//		
		String inputFile = args[0];
		String outputFile = args[1];
		try {
			List<List<Token>> tokens = tokenizeFile(inputFile);
			writeFile(tokens, inputFile, outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
