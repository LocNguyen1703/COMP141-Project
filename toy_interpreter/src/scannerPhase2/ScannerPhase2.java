package scannerPhase2;

import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class ScannerPhase2 {

	public static class Token {
        TokenType type;
        String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }
    }
	
	enum TokenType {
		//for project phase 1.2, we have to add in KEYWORD type
		IDENTIFIER, NUMBER, SYMBOL, KEYWORD, ERROR
	}
	
	public static List<List<Token>> tokenizeFile(String inputFile) throws IOException {
		List<List<Token>> result_tokens = new ArrayList<>();
//		Pattern identifier = Pattern.compile("([a-zA-Z])([a-zA-Z0-9])*");
//		Pattern num = Pattern.compile("[0-9]+");
//		Pattern symbol = Pattern.compile("\\+|\\-|\\*|/|\\(|\\)|\\:=|\\;");
//		Pattern keyword = Pattern.compile("\\b(if|then|else|endif|while|do|endwhile|skip)\\b");
//		Pattern error = Pattern.compile("&|#|\\.|\\,|\\]|\\[");
		
		String identifier = "([a-zA-Z])([a-zA-Z0-9])*";
		String num = "[0-9]+";
		String symbol = "\\+|\\-|\\*|/|\\(|\\)|\\:=|\\;";
		String keyword = "\\b(if|then|else|endif|while|do|endwhile|skip)\\b";
		String error = "&|#|\\.|\\,|\\]|\\[";
		
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				List<Token> separated_result_tokens = new ArrayList<>();
//		        for (String i : line.split(" ")) {
//		            Matcher match_identifier = identifier.matcher(i);
//		            Matcher match_num = num.matcher(i);
//		            Matcher match_symbol = symbol.matcher(i);
//		            Matcher match_keyword = keyword.matcher(i);
//		            Matcher match_error = error.matcher(i);
//		            
//		            if (match_keyword.matches()) separated_result_tokens.add(new Token(TokenType.KEYWORD, i));
//		            else if (match_num.matches()) separated_result_tokens.add(new Token(TokenType.NUMBER, i));
//		            else if (match_symbol.matches()) separated_result_tokens.add(new Token(TokenType.SYMBOL, i));
//		            else if (match_identifier.matches()) separated_result_tokens.add(new Token(TokenType.IDENTIFIER,i));
//		            //
//		            else {
//		            	while (match_identifier.find()) {
//		            		separated_result_tokens.add(new Token(TokenType.IDENTIFIER, match_identifier.group()));
//		                }
//		                while (match_num.find()) {
//		                	separated_result_tokens.add(new Token(TokenType.NUMBER, match_num.group()));
//		                }
//		                while (match_symbol.find()) {
//		                	separated_result_tokens.add(new Token(TokenType.SYMBOL, match_symbol.group()));
//		                }
//		                while (match_keyword.find()) {
//		                	separated_result_tokens.add(new Token(TokenType.KEYWORD, match_keyword.group()));
//		                }
//		                while (match_error.find()) {
//		                	separated_result_tokens.add(new Token(TokenType.ERROR, match_error.group()));
//			            }	
//		            }	            	
//		        }
				String longest_substring = "";
				int iter = 0;
				
				while (iter < line.length()) {
					longest_substring += line.charAt(iter);
					if (longest_substring.matches(num)) {
						while (iter + 1 < line.length() && (longest_substring + line.charAt(iter+1)).matches(num)) {
							iter ++;
							longest_substring += line.charAt(iter);
						}
//						System.out.println(longest_substring + ": NUMBER");
						separated_result_tokens.add(new Token(TokenType.NUMBER, longest_substring));
						longest_substring = ""; 
					}
					
					else if (longest_substring.matches(keyword)) {
						while (iter + 1 < line.length() && (longest_substring + line.charAt(iter+1)).matches(keyword)) {
							iter ++;
							longest_substring += line.charAt(iter);
						}
//						System.out.println(longest_substring + ": ERROR");
						separated_result_tokens.add(new Token(TokenType.KEYWORD, longest_substring));
						longest_substring = ""; 
					}
					
					else if (longest_substring.matches(identifier)) {
						while (iter + 1 < line.length() && (longest_substring + line.charAt(iter+1)).matches(identifier)) {
							iter ++;
							longest_substring += line.charAt(iter);
						}
						if (longest_substring.matches(keyword)) {
							separated_result_tokens.add(new Token(TokenType.KEYWORD, longest_substring));
						}
//						System.out.println(longest_substring + ": IDENTIFIER");
						else separated_result_tokens.add(new Token(TokenType.IDENTIFIER, longest_substring));
						longest_substring = ""; 
					}
					
					else if (longest_substring.matches(symbol)) {
						while (iter + 1 < line.length() && (longest_substring + line.charAt(iter+1)).matches(symbol)) {
							iter ++;
							longest_substring += line.charAt(iter);
						}
//						System.out.println(longest_substring + ": SYMBOL");
						separated_result_tokens.add(new Token(TokenType.SYMBOL, longest_substring));
						longest_substring = ""; 
					}
					
					else if (longest_substring.matches(error)) {
						while (iter + 1 < line.length() && (longest_substring + line.charAt(iter+1)).matches(error)) {
							iter ++;
							longest_substring += line.charAt(iter);
						}
//						System.out.println(longest_substring + ": ERROR");
						separated_result_tokens.add(new Token(TokenType.ERROR, longest_substring));
						longest_substring = ""; 
					}
					
					longest_substring = "";
					iter++;
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
		if (args.length != 2) {
			System.err.println("Usage: java FileScanner <input_file> <output_file>");
			System.exit(1);
		}

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
