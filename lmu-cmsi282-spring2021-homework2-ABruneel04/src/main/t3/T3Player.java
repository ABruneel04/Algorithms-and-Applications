// Created by: Andrew Bruneel

package main.t3;

import java.util.*;

/**
 * Artificial Intelligence responsible for playing the game of T3!
 * Implements the alpha-beta-pruning mini-max search algorithm
 */
public class T3Player {
	
	// Private Constants
	// -------------------------------
	private int a = Integer.MIN_VALUE;
	private int b = Integer.MAX_VALUE;
    
    /**
     * Workhorse of an AI T3Player's choice mechanics that, given a game state,
     * makes the optimal choice from that state as defined by the mechanics of
     * the game of Tic-Tac-Total.
     * Note: In the event that multiple moves have equivalently maximal minimax
     * scores, ties are broken by move col, then row, then move number in ascending
     * order (see spec and unit tests for more info). The agent will also always
     * take an immediately winning move over a delayed one (e.g., 2 moves in the future).
     * @param state The state from which the T3Player is making a move decision.
     * @return The T3Player's optimal action.
     */
    public T3Action choose (T3State state) {
    	T3Action choice = new T3Action(0, 0, 0);
    	choice = alphaBetaPrune(state, a, b, true).action;
    	return choice;
    }
    
    /**
     * Recursively utilizes alpha-beta pruning in order to return an optimal action as well as a minimax
     * score when given a state and told if the computer will be playing evens or odds
     * @param state The state of the T3 board before the method is called
     * @param a The minimum integer value we use for a-b pruning
     * @param b The maximum integer value we use for a-b pruning
     * @param isMaxPlayer The boolean value designed to determine which player's turn it is for calculations
     * @return The action that led to the optimal board state as well as the utility score
     */
    private T3ActionScore alphaBetaPrune(T3State state, int a, int b, boolean isMaxPlayer) {
    	Map<T3Action, T3State> children = state.getTransitions();
		T3ActionScore actionScore = new T3ActionScore(null, 0);
    	if(state.isWin()) {    		
    		if(isMaxPlayer) {
    			actionScore.action = null;
    			actionScore.miniMaxScore = -1;
    			return actionScore;
    		}
    		else {
    			actionScore.action = null;
    			actionScore.miniMaxScore = 1;
    			return actionScore;
    		}
    	}
    	else if(state.isTie()) {
			actionScore.action = null;
			actionScore.miniMaxScore = 0;
			return actionScore;
    	}
    	
    	if(isMaxPlayer) {
    		int v = Integer.MIN_VALUE;
    		for(Map.Entry<T3Action, T3State> child : children.entrySet()) {
    			T3ActionScore childActionScore = alphaBetaPrune(child.getValue(), a, b, false);
    			if(v < childActionScore.miniMaxScore) {
    				v = childActionScore.miniMaxScore;
    				actionScore.action = child.getKey();
    			}
    			a = Math.max(a, v);
    			actionScore.miniMaxScore = v;
    			if(child.getValue().isWin()) {
    				actionScore.action = child.getKey();
    				return actionScore;
    			}
    			if(b <= a) {
    				break;
    			}
    		}

        	return actionScore;
    	}

    	
    	else {
    		int v = Integer.MAX_VALUE;
    		for(Map.Entry<T3Action, T3State> child : children.entrySet()) {
    			T3ActionScore childActionScore = alphaBetaPrune(child.getValue(), a, b, true);
    			if(v > childActionScore.miniMaxScore) {
    				v = childActionScore.miniMaxScore;
    				actionScore.action = child.getKey();
    			}
    			b = Math.min(b, v);
    			actionScore.miniMaxScore = v;
    			if(child.getValue().isWin()) {
    				actionScore.action = child.getKey();
    				return actionScore;
    			}
    			if(b <= a) {
    				break;
    			}
    		}
        	return actionScore;
    	}

    }
    
    /**
     * T3ActionScore private nested class that is used in the recursive function to store
     * the utility score as well as the actions leading to that score
     */
    private class T3ActionScore {
    	
    	// Private Fields
    	// ----------------------
    	private T3Action action;
    	private int miniMaxScore;
    	
    	/**
    	 * Constructs a T3ActionScore object to be utilized in our pruning method
    	 * 
    	 * @param action A T3Action variable that stores the action involved with a T3state
    	 * @param miniMaxScore A utility score that is assigned to the player based on a win, loss, or tie
    	 */
    	private T3ActionScore(T3Action action, int miniMaxScore) {    	
    		this.action = action;
    		this.miniMaxScore = miniMaxScore;    		
    	}
    }	
}
