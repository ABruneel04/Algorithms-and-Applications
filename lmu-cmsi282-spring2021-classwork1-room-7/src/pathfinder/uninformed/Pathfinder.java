package pathfinder.uninformed;

import java.util.*;

/**
 * Maze Pathfinding algorithm that implements a basic, uninformed, breadth-first tree search.
 */
public class Pathfinder {
    
    /**
     * Given a MazeProblem, which specifies the actions and transitions available in the
     * search, returns a solution to the problem as a sequence of actions that leads from
     * the initial to a goal state.
     * 
     * @param problem A MazeProblem that specifies the maze, actions, transitions.
     * @return An ArrayList of Strings representing actions that lead from the initial to
     * the goal state, of the format: ["R", "R", "L", ...]
     */
    public static List<String> solve (MazeProblem problem) {
		ArrayList<String> actionList = new ArrayList<String>();
        Queue<SearchTreeNode> frontier = new LinkedList<>();
        SearchTreeNode root = new SearchTreeNode(problem.getInitial(), null, null);
        frontier.add(root);
        while(!(frontier.size()==0)) {
        	SearchTreeNode check = frontier.poll();
        	//need to return the path to this goal
        	if(check.state.equals(problem.getGoal())) {
        	//find the parent and corresponding actions until you get back to the root
        		while(!(check.equals(root))) {
        			String action = check.action;
        			actionList.add(0, action);
        			SearchTreeNode parent = check.parent;
        			check = parent;
        		}
        		break;
        	}
        	//find the children of the node and then add them to the frontier
        	else {
        		Map<String, MazeState> children = problem.getTransitions(check.state);
        		for(Map.Entry<String, MazeState> child : children.entrySet()) {
        			SearchTreeNode childNode = new SearchTreeNode(child.getValue(), child.getKey(), check);
        			frontier.add(childNode);
        		}
        	}
        }
        return actionList;
    }
    
    /**
     * SearchTreeNode private static nested class that is used in the Search algorithm to 
     * construct the Search tree.
     */
    private static class SearchTreeNode {
        
        MazeState state;
        String action;
        SearchTreeNode parent;
        
        /**
         * Constructs a new SearchTreeNode to be used in the Search Tree.
         * 
         * @param state The MazeState (col, row) that this node represents.
         * @param action The action that *led to* this state / node.
         * @param parent Reference to parent SearchTreeNode in the Search Tree.
         */
        SearchTreeNode (MazeState state, String action, SearchTreeNode parent) {
            this.state = state;
            this.action = action;
            this.parent = parent;
        }
        
    }
    
}
