import Lexer.Lexer;
import Lexer.Token;
import Lexer.Lexeme;

import java.util.ArrayList;

public class program {
    public static void main(String []args) {
        Token[] tokens = {
            // new Token("WHITESPACE", "( |\n|\t)*"),
            // new Token("OPERATOR", "=|-|+|*|/"),
            new Token("KEYWORD", "a*b*c*d"),
            // new Token("TEST", "el"),
            // new Token("LITERAL", "(0|1|2|3|4|5|6|7|8|9)*"),
        };

        Lexer lexer = new Lexer(tokens);

        ArrayList<Lexeme> lexemes = lexer.lex("aaaacbdd");
        print(lexemes);
    }

    public static void print(ArrayList<Lexeme> lexemes) {
        for (int i = 0; i < lexemes.size(); i += 1) {
            System.out.println(lexemes.get(i).type + ", " + lexemes.get(i).value);
        }
    }
}