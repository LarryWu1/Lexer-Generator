package Lexer.Automaton;

import java.util.ArrayList;

public class FA {
    State start;
    State accept;
    ArrayList<State> current;

    public FA() {
        start = new State();
        accept = start;
    }

    private void fillEpsilonClosure() {
        ArrayList<State> epsilons = new ArrayList<State>();
        epsilons.addAll(current);
        while (epsilons.size() > 0) {
            ArrayList<State> nextEpsilons = new ArrayList<State>();
            for (int i = 0; i < epsilons.size(); i += 1) {
                State node = epsilons.get(i);
                ArrayList<State> next = node.next('~');

                if (next != null) {
                    for (int j = 0; j < next.size(); j += 1) {
                        State nextNode = next.get(j);
                        if (!nextNode.visited) {
                            current.add(nextNode);
                            nextEpsilons.add(nextNode);
                            nextNode.visited = true;
                        }
                    }
                }
            }
            epsilons = nextEpsilons;
        }
    }

    private void initializeTraversal() {
        current = new ArrayList<State>();
        start.visited = true;
        current.add(start);
        fillEpsilonClosure();
    }

    // 1: accept, 0: has states, -1: has no more states
    public int read(char c) {
        if (current == null) {
            initializeTraversal();
        }

        if (current.size() == 0) {
            return -1;
        }

        for (int i = 0; i < current.size(); i += 1) {
            current.get(i).visited = false;
        }

        ArrayList<State> nextCurrent = new ArrayList<State>();
        for (int i = 0; i < current.size(); i += 1) {
            State node = current.get(i);
            if (node.next(c) != null) {
                ArrayList<State> next = node.next(c);
                for (int j = 0; j < next.size(); j += 1) {
                    State nextNode = next.get(j);
                    if (!nextNode.visited) {
                        nextCurrent.add(nextNode);
                        nextNode.visited = true;
                    }
                }
            }
        }
        current = nextCurrent;
        fillEpsilonClosure();

        boolean hasAccept = false;
        for (int i = 0; i < current.size(); i += 1) {
            if (accept.equals(current.get(i))) {
                hasAccept = true;
            }
        }

        if (current.size() == 0) {
            return -1;
        }
        return hasAccept ? 1 : 0;
    }

    public void reset() {
        current = null;
    }

    public static void printfa(FA nfa) {
        ArrayList<State> lvl = new ArrayList<State>();
        lvl.add(nfa.start);
        nfa.start.visited = true;

        while (lvl.size() > 0) {
            ArrayList<State> next = new ArrayList<State>();
            for (int i = 0; i < lvl.size(); i += 1) {
                State node = lvl.get(i);
                
                for (Character c : node.transitions.keySet()) {
                    ArrayList<State> transitions = node.transitions.get(c);
                    for (int j = 0; j < transitions.size(); j += 1) {
                        if (!transitions.get(j).visited) {
                            next.add(transitions.get(j));
                            transitions.get(j).visited = true;
                        }
                    }
                }

                String isStart = node.hashCode() == nfa.start.hashCode() ? " -> START STATE" : "";
                String isAccept = node.hashCode() == nfa.accept.hashCode() ? " -> ACCEPT STATE" : "";
                System.out.println(Integer.toHexString(node.hashCode()) + ", " + node.transitions + isStart + isAccept);
            }
            lvl = next;
        }
        clearVisited(nfa);
    }

    private static void clearVisited(FA nfa) {
        ArrayList<State> lvl = new ArrayList<State>();
        lvl.add(nfa.start);
        nfa.start.visited = false;

        while (lvl.size() > 0) {
            ArrayList<State> next = new ArrayList<State>();
            for (int i = 0; i < lvl.size(); i += 1) {
                State node = lvl.get(i);
                
                for (Character c : node.transitions.keySet()) {
                    ArrayList<State> transitions = node.transitions.get(c);
                    for (int j = 0; j < transitions.size(); j += 1) {
                        if (transitions.get(j).visited) {
                            next.add(transitions.get(j));
                            transitions.get(j).visited = false;
                        }
                    }
                }
            }
            lvl = next;
        }
    }

    public static FA construct(String regex) {
        int[] start = { 0 };
        FA fa = constructFA(regex, start);
        return fa;
    }

    private static FA constructFA(String regex, int[] start) {
        ArrayList<FA> fas = new ArrayList<FA>();

        FA curr = new FA();
        while (start[0] < regex.length()) {
            if (regex.charAt(start[0]) == ')') {
                fas.add(curr);
                start[0] += 1;
                break;
            } else if (regex.charAt(start[0]) == '(') {
                start[0] += 1;
                FA subfa = constructFA(regex, start);
                if (start[0] < regex.length() && regex.charAt(start[0]) == '*') {
                    start[0] += 1;
                    star(subfa);
                }
                curr.concat(subfa);
                if (start[0] == regex.length()) {
                    fas.add(curr);
                }
                continue;
            }

            if (regex.charAt(start[0]) == '|') {
                fas.add(curr);
                curr = new FA();
                start[0] += 1;
                continue;
            }

            FA nextFA = symbol(regex.charAt(start[0]));
            if (start[0] + 1 < regex.length() && regex.charAt(start[0] + 1) == '*') {
                start[0] += 1;
                star(nextFA);
            }
            curr.concat(nextFA);

            start[0] += 1;
            if (start[0] == regex.length()) {
                fas.add(curr);
            }
        }

        if (fas.size() == 1) {
            return fas.get(0);
        }
        return union(fas);
    }

    private void concat(FA a) {
        this.accept.transitions = a.start.transitions;
        this.accept = a.accept;
    }

    private static FA symbol(char c) {
        FA fa = new FA();
        fa.accept = new State();
        fa.start.addTransition(Character.valueOf(c), fa.accept);
        return fa;
    }

    private static FA union(ArrayList<FA> fas) {
        FA newFA = new FA();
        newFA.accept = new State();

        for (int i = 0; i < fas.size(); i += 1) {
            FA fa = fas.get(i);
            newFA.start.addTransition('~', fa.start);
            fa.accept.addTransition('~', newFA.accept);
        }
        return newFA;
    }

    private static void star(FA a) {
        State newStart = new State();
        State newEnd = new State();

        a.accept.addTransition('~', a.start);
        a.accept.addTransition('~', newEnd);
        newStart.addTransition('~', a.start);
        newStart.addTransition('~', newEnd);

        a.start = newStart;
        a.accept = newEnd;
    }
}
