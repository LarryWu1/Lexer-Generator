package Lexer;

import Lexer.Automaton.FA;

public class Token {
    public String type;
    FA fa;
    boolean ignore;

    public Token(String type, String regex) {
        this.type = type;
        fa = FA.construct(regex);
    }

    public Token(String type, String regex, boolean ignore) {
        this.type = type;
        fa = FA.construct(regex);
        this.ignore = ignore;
    }

    public int read(char c) { // true if accept state, false if not
        return fa.read(c);
    }

    public void reset() { // reset NFA
        fa.reset();
    }
}
