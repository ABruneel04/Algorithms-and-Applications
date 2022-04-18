// Created by: Andrew Bruneel

package main.pathfinder;

import java.util.*;

/**
 * Maze Pathfinding algorithm that implements A* graph search for the Muddy Maze
 * Pathfinding Problems with Locked Goals.
 */
public abstract class Pathfinder {
    
    /**
     * Given a MazeProblem, which specifies the actions and transitions available in the
     * search, returns a solution to the problem as a sequence of actions that leads from
     * the initial to a goal state.
     * 
     * @param problem A MazeProblem that specifies the maze, actions, transitions.
     * @return A List of Strings representing actions that lead from the initial to
     * the goal state, of the format: ["R", "R", "L", ...]
     */
    public static List<String> solve (MazeProblem problem) {
        ArrayList<String> actionList = new ArrayList<String>();
        //make the priority queue compare by cost using compareTo
        PriorityQueue<SearchTreeNode> frontier = new PriorityQueue<>();
        SearchTreeNode root = new SearchTreeNode(problem.getInitial(), null, null, 0);
        SearchTreeNode bestGoal = root;
        HashSet<MazeState> graveyard = new HashSet<MazeState>();
        int minCost = 9999;
        //need to check to see if there are any keys in the maze, if not it's unsolvable
    	if(problem.getKeys().isEmpty()) {
    		return null;
    	}        
    	for(MazeState key : problem.getKeys()) {
        	frontier.clear();
        	graveyard.clear();
    		frontier.add(root);
    		SearchTreeNode objective = nodeExplorer(key, problem, graveyard, frontier);
    		frontier.clear();
    		graveyard.clear();
    		if(!(objective == null)) {
        		frontier.add(objective);
    		}
        	SearchTreeNode goal = nodeExplorer(problem.getGoal(), problem, graveyard, frontier);
        	if (goal == null) {
        		return null;
        	}
        	if(minCost > goal.cost) {
        		minCost = goal.cost;
        		bestGoal = goal;
        	}
    	}
        //need to note past actions to find actionList
		while(!(bestGoal.equals(root))) {
			String action = bestGoal.action;
			actionList.add(0, action);
			SearchTreeNode parent = bestGoal.parent;
			bestGoal = parent;
		}
	return actionList;
    }
    
    /**
     * 
     * @param s MazeState s that we are trying to reach
     * @param p The given MazeProblem p
     * @param graveyard Our graveyard where we are storing explored states
     * @param frontier The priority queue frontier that allows us to expand optimally
     * @return the node that we are trying to find, whether it is the key node or goal node
     */
    
    public static SearchTreeNode nodeExplorer(MazeState s, MazeProblem p, HashSet<MazeState> graveyard, PriorityQueue<SearchTreeNode> frontier) {
    	
        while(!(frontier.size() == 0)) {
        	SearchTreeNode check = frontier.poll();
        	if(check.state.equals(s)) {
        		return check;
        	}
        	graveyard.add(check.state);
            Map<String, MazeState> children = p.getTransitions(check.state);
    		for(Map.Entry<String, MazeState> child : children.entrySet()) {
    			int newCost = p.getCost(child.getValue()) + manhattanDistance(child.getValue(), p.getGoal()) + check.cost;
    			SearchTreeNode childNode = new SearchTreeNode(child.getValue(), child.getKey(), check, newCost);
    			if(!(graveyard.contains(childNode.state))) {
        			frontier.add(childNode);
    			}
    		}
        	
        }
        return null;
    }
    
    /**
     * 
     * @param s_one the current state that applies to the agent
     * @param s_two the key state or goal state which the agent is attempting to reach
     * @return a future cost value of the distance from the current state to the key state/goal state
     */
    
    public static int manhattanDistance(MazeState s_one, MazeState s_two) {
    	int col_one = s_one.col;
    	int col_two = s_two.col;
    	int row_one = s_one.row;
    	int row_two = s_two.row;
  
    	return (Math.abs(row_two - row_one) + Math.abs(col_two - col_one));
    }
    
    
    /**
     * SearchTreeNode private static nested class that is used in the Search algorithm to 
     * construct the Search tree.
     */
    private static class SearchTreeNode implements Comparable<SearchTreeNode> {
        
        MazeState state;
        String action;
        SearchTreeNode parent;
        int cost;
        
        /**
         * Constructs a new SearchTreeNode to be used in the Search Tree.
         * 
         * @param state The MazeState (row, col) that this node represents.
         * @param action The action that *led to* this state / node.
         * @param parent Reference to parent SearchTreeNode in the Search Tree.
         * @param cost The cost to explore the SearchTreeNode
         */
        SearchTreeNode (MazeState state, String action, SearchTreeNode parent, int cost) {
            this.state = state;
            this.action = action;
            this.parent = parent;
            this.cost = cost;
        }
        
        /**
         * @param SearchTreeNode n that is being compared to a node already in the priority queue
         * @return an integer value that tells the priority queue how to rank nodes in the queue
         */
        public int compareTo(SearchTreeNode n) {
        	if(n.cost > this.cost) {
        		return -1;
        	}
        	else if(n.cost < this.cost) {
        		return 1;
        	}
        	else {
        		return 0;
        	}
        }
        
    }
    
}

