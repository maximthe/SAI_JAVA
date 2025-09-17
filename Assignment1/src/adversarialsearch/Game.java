package adversarialsearch;

import java.util.Vector;

public class Game {
	State b;
	public Game() {
		b=new State();
		b.read("data/almost_win.txt");
	}
	public void test() {
		
		System.out.println(b);
		
		State endState = minimax(b, b.turn, 13, 0);
		
		this.replayStepByStep(b, endState.moves);
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
	
	public void replayStepByStep(State initialState, Vector<String> moveHistory) {
	    // Copy the initial state to avoid modifying the original
	    State current = initialState.copy();
	    
	    printStep(current, -1, "Start", 0);  // Initial state, no player yet

	    // Iterate through all moves
	    for (int i = 0; i < moveHistory.size(); i++) {
	        String move = moveHistory.get(i);
	        int currentPlayer = current.turn;  // Player making the move
	        current.execute(move);
	        printStep(current, currentPlayer, move, i + 1);
	    }
	}

	// Helper method to print state info with current move
	private void printStep(State s, int player, String move, int stepNumber) {
	    if (player >= 0) {
	        System.out.println("Step " + stepNumber + ": Player " + (player == 0 ? "A" : "B") + " did " + move);
	    } else {
	        System.out.println("Initial state:");
	    }
	    System.out.println(s);  // Board
	    System.out.println("Score: A=" + s.score[0] + ", B=" + s.score[1]);
	    System.out.println("Food left: " + s.food);
	    System.out.println("Next turn: " + (s.turn == 0 ? "A" : "B"));
	    System.out.println("-------------------------\n");
	}

}
