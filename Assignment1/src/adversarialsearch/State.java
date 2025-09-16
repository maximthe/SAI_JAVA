package adversarialsearch;

import java.util.*;
import java.io.RandomAccessFile;

public class State {
    char[][] board; // The board as a 2D character array
    int[] agentX; // The X coordinates of the agents, example: agentX[0] -> x coordinate of agent 0
    int[] agentY; // The Y coordinates of the agents, example: agentY[1] -> y coordinate of agent 1
    int[] score; // Scores of both agents
    int turn; // Who's turn is it, agent 0 or agent 1
    int food; // Amount of food still available on the board
    
    Vector<String> moves = new Vector<String>(); // Variable for bookkeeping of the moves done so far
    

    public void read(String file_name) {

        // Initialize integer variables
        turn = 0;
        food = 0;

        // Initialize the Scores
        score = new int[2];
        score[0] = 0;
        score[1] = 0;

        try (RandomAccessFile reader = new RandomAccessFile(file_name, "r")) {

            // Define the variables
            String[] board_size;
            int board_height;
            int board_width;

            // Extract the size of the board
            board_size = reader.readLine().split(" ");
            board_width = Integer.valueOf(board_size[0]);
            board_height = Integer.valueOf(board_size[1]);

            // Initialize all the arrays
            board = new char[board_height][board_width];
            agentX = new int[2];
            agentY = new int[2];

            for (int row = 0; row < board_height; row++) {
                String line;
                line = reader.readLine();

                for (int col = 0; col < board_width; col++) {
                    char charecter;
                    charecter = line.charAt(col);

                    if (charecter == 'A') {
                        agentX[0] = col;
                        agentY[0] = row;
                    }

                    if (charecter == 'B') {
                        agentX[1] = col;
                        agentY[1] = row;
                    }

                    if (charecter == '*') {
                        food++;
                    }

                    board[row][col] = (charecter == 'A' || charecter == 'B') ? ' ' : charecter;
                }

            }

        } catch (Exception error) {
            System.out.println("Error while handling: " + error);
        }
    }

    public String toString() {
        StringBuilder S = new StringBuilder("");

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {

                char cellChar = board[row][col];

                // check if agent at that position
                if (col == agentX[0] && row == agentY[0]) {
                    cellChar = 'A';
                } else if (col == agentX[1] && row == agentY[1]) {
                    cellChar = 'B';
                }

                // append the character to the output string
                S.append(cellChar);
            }
            S.append("\n");
        }
        return S.toString();
    }

    public State copy() {
        State newState = new State();

        // deep copy the board
        int height = this.board.length;
        int width = this.board[0].length;
        newState.board = new char[height][width];

        // iterate through all of the squares
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                newState.board[row][col] = this.board[row][col];
            }
        }

        // this will be used for later deep copies
        int numAgents = 2;

        //deep copy the agent positions
        newState.agentX = new int[numAgents];
        newState.agentY = new int[numAgents];
        for (int i = 0; i < numAgents; i++) {
            newState.agentX[i] = this.agentX[i];
            newState.agentY[i] = this.agentY[i];
        }

        // deep copy the scores
        newState.score = new int[numAgents];
        for (int i = 0; i < numAgents; i++) {
            newState.score[i] = this.score[i];
        }

        // copy primitive types
        newState.turn = this.turn;
        newState.food = this.food;

        return newState;
    }

    public Vector<String> legalMoves(int agent) {
        Vector<String> output;
        output = new Vector<String>();

        // make a variable for position of the agents for convenience
        int agentX = this.agentX[agent];
        int agentY = this.agentY[agent];

        // examine if an agent can eat
        if (board[agentY][agentX] == '*') {
            output.add("eat");
        }

        // examine if an agent can drop a block
        if (board[agentY][agentX] == ' ') {
            output.add("block");
        }

        // check if we can go either of the 4 directions
        if (board[agentY + 1][agentX] != '#') {
            output.add("down");
        }
        if (board[agentY - 1][agentX] != '#') {
            output.add("up");
        }
        if (board[agentY][agentX + 1] != '#') {
            output.add("right");
        }
        if (board[agentY][agentX - 1] != '#') {
            output.add("left");
        }

        return output;

    }
    
    // method overloading
    public Vector<String> legalMoves() {
        return this.legalMoves(this.turn);
    }
    
    public void execute(String action) {
        Vector<String> possible_moves = this.legalMoves(this.turn);
        
        // exit the function early if the move is not legal
        if (possible_moves.contains(action) == false) {
            return;
        }
        
        switch (action) {
        
            // when moving we only care about updating the position of the agent
            case "up":
                this.agentY[this.turn] -= 1;
                break;
                
            case "down":
                this.agentY[this.turn] += 1;
                break;
                
            case "right":
                this.agentX[this.turn] += 1;
                break;
                
            case "left":
                this.agentX[this.turn] -= 1;
                break;
            
            // when eating we need to update the score and the total amount of food left
            case "eat":
                this.board[this.agentY[this.turn]][this.agentX[this.turn]] = ' ';
                this.score[this.turn] += 1;
                this.food -= 1;
                break;
            
            // simply place a block in the current position
            case "block":
                this.board[this.agentY[this.turn]][this.agentX[this.turn]] = '#';
                break;
                
        }
        
        // add the move to the history
        this.moves.add(action);
        
        // change whose turn it is
        this.turn = 1 - this.turn;
    }
    
    private static int absolute(int value) {
        if (value > 0) {
            return value;
        } else {
            return -1 * value;
        }
    }
    
    public boolean isLeaf() {
        boolean leaf_node = false;
        
        // this is so eclipse doesn't complain 
        absolute(-8);
        
        // check if any player is out of moves
        if (this.legalMoves(0).size() == 0 || this.legalMoves(1).size() == 0) {
            leaf_node = true;
        }
        
        // I wanted to use the Math.abs() but I don't think were allowed imports -> oh, turns out we stop when food is equal to 0
        if (this.food == 0) {
            leaf_node = true;
        }
        
        return leaf_node;
    }
    
    public double value(int agent) {
        // Game ended - determine why
        if (this.food == 0) {
            // All food collected, compare food counts
            return this.compareFood(agent);
        } else {
            // Game ended due to no legal moves
            if (this.legalMoves(agent).size() == 0) {
                if (this.legalMoves(1 - agent).size() == 0) {
                    // Neither can move, compare food
                    return this.compareFood(agent);
                } else {
                    // Only I can't move, I lose
                    return -1;
                }
            } else {
                // Only opponent can't move, I win
                return 1;
            }
        }
    }
    
    private double compareFood(int agent) {
        // This function returns if, based on food, the game is a tie, win or loss
        
        // agent has more food, win!
        if (this.score[agent] > this.score[1 - agent]) {
            return 1;
        
        // agent has less food, loss :(
        } else if (this.score[agent] < this.score[1 - agent]) {
            return -1;
        
        // equal food, tie :|
        } else {
            return 0;
        }
    }
}