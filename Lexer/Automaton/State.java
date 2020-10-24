package Lexer.Automaton;

import java.util.ArrayList;
import java.util.HashMap;

public class State {
    HashMap<Character, ArrayList<State>> transitions;
    boolean visited;

    public State() {
        transitions = new HashMap<Character, ArrayList<State>>();
        visited = false;
    }

    public void addTransition(char v, State s) {
        if (transitions.get(Character.valueOf(v)) == null) {
            transitions.put(Character.valueOf(v), new ArrayList<State>());
        }

        transitions.get(Character.valueOf(v)).add(s);
    }

    public ArrayList<State> next(char v) {
        return transitions.get(Character.valueOf(v));
    }
}
