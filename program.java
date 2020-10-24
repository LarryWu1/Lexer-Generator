import Lexer.Lexer;
import Lexer.Token;
import Lexer.Lexeme;

import java.util.ArrayList;

public class program {
    public static void main(String []args) {
        Token[] tokens = {
            new Token("WHITESPACE", " *", true),
            new Token("OPERATOR", "=|-|+|/"), // *
            new Token("KEYWORD", "let|;"),
            new Token("LITERAL", "(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)(0|1|2|3|4|5|6|7|8|9|a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)*"),
            new Token("NUMBER", "(0|1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9)*"),
        };

        Lexer lexer = new Lexer(tokens);

        ArrayList<Lexeme> lexemes = lexer.lex("let a = 10 + 15;");
        print(lexemes);
    }

    public static void print(ArrayList<Lexeme> lexemes) {
        for (int i = 0; i < lexemes.size(); i += 1) {
            System.out.println(lexemes.get(i).type + " => \"" + lexemes.get(i).value + "\"");
        }
    }
}