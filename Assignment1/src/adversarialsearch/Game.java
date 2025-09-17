package adversarialsearch;

import java.util.Vector;

public class Game {
	State b;
	public Game() {
		b=new State();
		b.read("data/board.txt");
	}
	public void test() {
		
		System.out.println(minimax(b, b.turn, 13, 0).value(b.turn));
		System.out.println(minimax(b, b.turn, 13, 0));
		System.out.println(minimax(b, b.turn, 13, 0).score[0]);
		System.out.println(minimax(b, b.turn, 13, 0).score[1]);
	}
	
	public State minimax(State s, int forAgent, int maxDepth, int depth) {
		
	    // if we hit the depth limit or leaf node, return the state
	    if (depth >= maxDepth || s.isLeaf()) {
	        return s;
	    }

	    State bestState = null;

	    if (s.turn == forAgent) {
	        double bestValue = Double.NEGATIVE_INFINITY;

	        for (String move : s.legalMoves()) {
	            State nextState = s.copy();
	            nextState.execute(move);

	            State candidate = minimax(nextState, forAgent, maxDepth, depth + 1);
	            double branchValue = candidate.value(forAgent);

	            if (branchValue > bestValue) {
	                bestValue = branchValue;
	                bestState = candidate;
	            }
	        }
	    } else {
	        double bestValue = Double.POSITIVE_INFINITY;

	        for (String move : s.legalMoves()) {
	            State nextState = s.copy();
	            nextState.execute(move);

	            State candidate = minimax(nextState, forAgent, maxDepth, depth + 1);
	            double branchValue = candidate.value(forAgent);

	            if (branchValue < bestValue) {
	                bestValue = branchValue;
	                bestState = candidate;
	            }
	        }
	    }

	    return bestState;
	}

}
