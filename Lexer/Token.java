package Lexer;

import Lexer.Automaton.FA;

public class Token {
    public String type;
    FA fa;

    public Token(String type, String regex) {
        this.type = type;
        fa = FA.construct(regex);
    }

    public int read(char c) { // true if accept state, false if not
        return fa.read(c);
    }

    public void reset() { // reset NFA
        fa.reset();
    }
}
