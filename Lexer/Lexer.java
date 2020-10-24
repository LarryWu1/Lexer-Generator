package Lexer;

import java.util.ArrayList;

public class Lexer {
    Token[] tokens;

    public Lexer(Token tokens[]) {
        this.tokens = tokens;
    }

    public ArrayList<Lexeme> lex(String program) {
        ArrayList<Lexeme> lexemes = new ArrayList<Lexeme>();

        int start = 0;

        while (start < program.length()) {
            int acceptIdx = -1;
            int acceptTokenIdx = -1;

            for (int i = start; i < program.length(); i += 1) {
                boolean hasMore = false;
                for (int j = this.tokens.length - 1; j >= 0; j -= 1) {
                    Token t = this.tokens[j];
                    int state = t.read(program.charAt(i));

                    if (state == -1) {
                        continue;
                    }
                    hasMore = true;

                    if (state == 1) {
                        acceptIdx = i;
                        acceptTokenIdx = j;
                    } 
                }

                if (!hasMore) {
                    break;
                }
            }
            System.out.println(program);
            System.out.println(start + ", " + acceptIdx + ", " + acceptTokenIdx);
            if (acceptIdx == -1) {
                lexemes.add(new Lexeme("ERROR => CHARACTER: '" + program.charAt(start) +  "' AT INDEX: " + Integer.toString(start), null));
                return lexemes;
            } else {
                lexemes.add(new Lexeme(this.tokens[acceptTokenIdx].type, program.substring(start, acceptIdx + 1)));
                start = acceptIdx + 1;
                for (int i = 0; i < this.tokens.length; i += 1) {
                    this.tokens[i].reset();
                }
            }
        }

        return lexemes;
    }
};
