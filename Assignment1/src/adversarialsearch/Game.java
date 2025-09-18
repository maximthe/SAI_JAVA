package adversarialsearch;

import java.util.Vector;

public class Game {
	State b;
	public Game() {
		b=new State();
		b.read("data/big_board.txt");
	}
	public void test() {
		State endState = minimax(b, b.turn, 10, 0);
		this.replayStepByStep(b, endState.moves);
	}
	
	public State minimax(State s, int forAgent, int maxDepth, int depth) {

	    // Base case: depth limit or leaf node
	    if (depth >= maxDepth || s.isLeaf()) {
	        return s;
	    }

	    // Recursive step: check the resulting value of every possible next branch state
	    State bestState = null;
	    
	    // If it is our turn, take the moves and find the maximum
	    if (s.turn == forAgent) {
	    	double bestValue = Double.NEGATIVE_INFINITY;
	    	
	    	for (String move: s.legalMoves()) {
	    		// Make a copy of the state for after the move
	    		State nextState = s.copy();
	    		
	    		// Make the move
	    		nextState.execute(move);
	    		
	    		// For this branch, find the value of the resulting best state and find the maximum
	    		State candidate = minimax(nextState, forAgent, maxDepth, depth + 1);
	    		double branchValue = candidate.value(forAgent);
	    		
	    		// If the branch value better than what we found so far, set the bestValue and bestState
	    		if (branchValue > bestValue) {
	    			bestValue = branchValue;
	    			bestState = candidate.copy();
	    		}
	    		
	    		// Prefer solutions with less moves
	    		if (candidate.moves.size() < bestState.moves.size() && branchValue >= bestValue) {
	    			bestValue = branchValue;
	    			bestState = candidate.copy();
	    		}
	    	}
	    } else {
	    	// if it is not our move we look for the minimum with respect to our agent
	    	double bestValue = Double.POSITIVE_INFINITY;
	    	
	    	for (String move: s.legalMoves()) {
	    		// Make a copy of the state for after the move
	    		State nextState = s.copy();
	    		
	    		// Make the move
	    		nextState.execute(move);
	    		
	    		// For this branch, find the value of the resulting best state and find the minimum
	    		State candidate = minimax(nextState, forAgent, maxDepth, depth + 1);
	    		double branchValue = candidate.value(forAgent);
	    		
	    		// If the branch value better than what we found so far, set the bestValue and bestState
	    		if (branchValue < bestValue) {
	    			bestValue = branchValue;
	    			bestState = candidate.copy();
	    		}
	    		
	    		// Prefer solutions with less moves
	    		if (candidate.moves.size() < bestState.moves.size() && branchValue >= bestValue) {
	    			bestValue = branchValue;
	    			bestState = candidate.copy();
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

	        // If the game ended at this step, stop replaying further moves
	        if (current.isLeaf()) {
	            System.out.println("Game ended at step " + (i + 1) + ".\n");
	            break;
	        }
	    }

	    // After all moves (or early termination), determine and print the result
	    if (current.isLeaf()) {
	        double valA = current.value(0); // perspective of A
	        if (valA == 1.0) {
	            System.out.println("Result: A wins.");
	        } else if (valA == -1.0) {
	            System.out.println("Result: B wins.");
	        } else {
	            System.out.println("Result: Tie.");
	        }
	    } else {
	        System.out.println("Result: Game not finished (non-terminal).");
	        // optional: show current leader by score
	        if (current.score[0] > current.score[1]) {
	            System.out.println("Current leader: A (score " + current.score[0] + " - " + current.score[1] + ")");
	        } else if (current.score[0] < current.score[1]) {
	            System.out.println("Current leader: B (score " + current.score[1] + " - " + current.score[0] + ")");
	        } else {
	            System.out.println("Score tied: " + current.score[0] + " - " + current.score[1]);
	        }
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
