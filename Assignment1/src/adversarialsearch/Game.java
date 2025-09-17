package adversarialsearch;

import java.util.Vector;

public class Game {
	State b;
	public Game() {
		b=new State();
		b.read("data/board.txt");
	}
	public void test() {
		
		System.out.println(minimax(b, b.turn, 20, 0).value(b.turn));
		
		while (!b.isLeaf()){
			System.out.println(b.toString());
			System.out.println("Legal moves for agent with turn:"+b.legalMoves());
			b.execute(b.legalMoves().get((int)(Math.random()*b.legalMoves().size())));
		}
	}
	
	public State minimax(State s, int forAgent, int maxDepth, int depth) {
		// make a variable to keep track of the state we return
		State tempState = s;
		
		System.out.println(depth);
		
		// if we hit the depth limit, return the state without further checking
		if (depth >= maxDepth) {
			return s;
		}
		
		// if we are in a leaf node, return the state
		if (s.isLeaf()) {
			return s;
		}
		
		if (s.turn == forAgent) {
			// if it is our turn, go through our moves and select the max
			double temp = -999;
			
			for (String move: s.legalMoves()) {
				
				// make a new branch
				State nextState = s.copy();
				nextState.execute(move);
				
				// find if the branch leads to a better value for the agent
				double branchValue = minimax(nextState, 1 - forAgent, maxDepth, depth++).value(1 - forAgent);
				if (branchValue > temp) {
					
					// update the temporary best value and state
					temp = branchValue;
					tempState = nextState;
				}
			}
			
		} else {
			
			// if it is NOT our turn, go through our moves and select the max
			double temp = 999;
			
			for (String move: s.legalMoves()) {
				
				// make a new branch
				State nextState = s.copy();
				nextState.execute(move);
				
				// find if the branch leads to a better value for the opponent
				double branchValue = minimax(nextState, 1 - forAgent, maxDepth, depth++).value(1 - forAgent);
				if (branchValue < temp) {
					
					// update the temporary "worst" value and state
					temp = branchValue;
					tempState = nextState;
				}
			}
			
		}
		
		return tempState;
	}
}
