//Created by: Andrew Bruneel
package main.csp;

import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.*;

/**
 * CSP: Calendar Satisfaction Problem Solver
 * Provides a solution for scheduling some n meetings in a given
 * period of time and according to some set of unary and binary 
 * constraints on the dates of each meeting.
 */
public class CSP {

    /**
     * Public interface for the CSP solver in which the number of meetings,
     * range of allowable dates for each meeting, and constraints on meeting
     * times are specified.
     * @param nMeetings The number of meetings that must be scheduled, indexed from 0 to n-1
     * @param rangeStart The start date (inclusive) of the domains of each of the n meeting-variables
     * @param rangeEnd The end date (inclusive) of the domains of each of the n meeting-variables
     * @param constraints Date constraints on the meeting times (unary and binary for this assignment)
     * @return A list of dates that satisfies each of the constraints for each of the n meetings,
     *         indexed by the variable they satisfy, or null if no solution exists.
     */
    public static List<LocalDate> solve (int nMeetings, LocalDate rangeStart, LocalDate rangeEnd, Set<DateConstraint> constraints) {
    	ArrayList<LocalDate> solution = new ArrayList<>();
    	ArrayList<Meeting> meetingList = new ArrayList<>();
    	for(int i = 0; i < nMeetings; i++) {
    		Meeting m = new Meeting();
    		for(LocalDate currentDate = rangeStart; !currentDate.isAfter(rangeEnd); currentDate = currentDate.plusDays(1)) {
    			m.domain.add(currentDate);
    		}
    		meetingList.add(m);
    	}
    	for(DateConstraint d: constraints) {
    		if(d.arity() == 1) {
    			meetingList.get(d.L_VAL).constraints.add(d);
    		}
    		else {
    			meetingList.get(d.L_VAL).constraints.add(d);
    			BinaryDateConstraint b = (BinaryDateConstraint)d;
    			meetingList.get(b.R_VAL).constraints.add(d);
    		}
    	}
    	for(int i = 0; i < nMeetings; i++) {
    		solution.add(null);
    	}
    	meetingList = nodeConsistency(meetingList);
    	meetingList = acThree(meetingList);
    	solution = recursiveBacktrack(solution, meetingList, nMeetings,  0);
    	return solution;
    }
    
    /**
     * One of our two pre-processing methods in our CSP class that allows us to limit
     * the domains of our meetings that we are scheduling by removing irrelevant values
     * that would never work because of unary constraints
     * @param meetingList The list of meeting objects that we are pre-processing against
     *                    our list of unary constraints
     * @return The edited version of our passed in meetingList, with a reduced domain
     */
    private static ArrayList<Meeting> nodeConsistency(ArrayList<Meeting> meetingList) {
    	for(int i = 0; i < meetingList.size(); i++) {
	    	ArrayList<LocalDate> toRemove = new ArrayList<>();
    		for(DateConstraint d: meetingList.get(i).constraints) {
    			for(LocalDate loc: meetingList.get(i).domain) {
    				String oper = d.OP;
    				if(d.arity() == 1) {
    					UnaryDateConstraint u = (UnaryDateConstraint)d;
    					LocalDate rVal = u.R_VAL;
    					if(!singleConstraintCheck(loc, rVal, oper)) {
    						toRemove.add(loc);
    					}
    				}
    			}
    		}
    		meetingList.get(i).domain.removeAll(toRemove);
    	}
    	return meetingList;
    }
    
    /**
     * One of our two pre-processing methods in our CSP class that allows us to limit
     * the domains of our meetings that we are scheduling by removing irrelevant values
     * that would never work because of binary constraints
     * @param meetingList The list of meeting objects that we are pre-processing against
     *                    our list of binary constraints
     * @return The edited version of our passed in meetingList, with a reduced domain
     */
    private static ArrayList<Meeting> acThree(ArrayList<Meeting> meetingList) {
    	Set<Arc> masterArcSet = new HashSet<Arc>();
    	Set<Arc> potentialReAdds = new HashSet<Arc>();
    	Set<Arc> reAddLater = new HashSet<Arc>();
    	Set<LocalDate> valsToRemove = new HashSet<LocalDate>();
    	for(int i = 0; i < meetingList.size(); i++) {
    		for(DateConstraint d: meetingList.get(i).constraints) {
    			if(d.arity() == 2) {
    				BinaryDateConstraint b = (BinaryDateConstraint)d;
    				Arc a = new Arc(d.L_VAL, b.R_VAL, d.OP);
    	    		masterArcSet.add(a);
    			}
    		}
    	}
    	while(!masterArcSet.isEmpty()) {
        	for(Iterator<Arc> iterator = masterArcSet.iterator(); iterator.hasNext();) {
        		Arc a = iterator.next();
        		for(LocalDate dateOne: meetingList.get(a.tail).domain) {
            	    boolean consistency = false;
        			for(LocalDate dateTwo: meetingList.get(a.head).domain) {
        				if(singleConstraintCheck(dateOne, dateTwo, a.op)) {
        					consistency = true;
        				}
        			}
        			if(!consistency) {
        				valsToRemove.add(dateOne);
        			}
        		}
        		if(!valsToRemove.isEmpty()) {
        			meetingList.get(a.tail).domain.removeAll(valsToRemove);
        			valsToRemove.clear();
        			potentialReAdds.add(a);
        			for(Arc reAdd: potentialReAdds) {
        				if(reAdd.head == a.tail) {
        					reAddLater.add(reAdd);	
        				}
        			}
        		}
            potentialReAdds.clear();
        	iterator.remove();
        	}
        	masterArcSet.addAll(reAddLater);
        	reAddLater.clear();
    	}
    	return meetingList;
    }

    /**
     * Our recursive method that does the bulk of the work in terms of actually solving
     * this assignment. Takes in an empty ArrayList of LocalDates and uses the passed in
     * constraints and domains of our meetings to schedule a date to each meeting and solve
     * the assignment
     * @param solution The list of LocalDate objects that we are recursively acting on,
     * 				   eventually turning into a solution for this CSP as an indexed list
     * 				   list of LocalDates that works given our specific meeting constraints
     * @param meetingList The ArrayList storing our Meeting objects, which contain each
     *                    the constraints and domain for each figurative "meeting" in our CSP
     * @return Will eventually return either a working solution of LocalDates for our CSP,
     *         or null if a solution is not possible
     */
    private static ArrayList<LocalDate> recursiveBacktrack(ArrayList<LocalDate> solution, ArrayList<Meeting> meetingList, int nMeetings, int index) {
    	ArrayList<LocalDate> result = null;
    	if(index == nMeetings) {
    		return solution;
    	}
    	Meeting unassigned = meetingList.get(index);
    	for(LocalDate loc: unassigned.domain) {
    	    solution.set(index, loc);
    		    if(consistencyCheck(unassigned, solution)) {
    			    result = recursiveBacktrack(solution, meetingList, nMeetings, index + 1);
        			if(result != null) {
        				return result;
        			}
    			}
    			solution.set(index, null);
    	}
    	return null;
    }
    
    /**
     * A method that checks the constraints of a meeting against the value that is trying
     * to be added to our solution set. It does this by also calling an additional method
     * that is able to check is a specific LocalDate object is consistent given a constraint
     * operator and R_VAL
     * @param unassigned An arbitrary "meeting" with an unassigned LocalDate that would be
     *                   returned as a solution in our recursive backtrack method
     * @param solution The list of LocalDate objects already part of our solution that is
     * 				   also referenced in our recursive backtrack method
     * @return A boolean telling us whether or not the LocalDate we assign to our solution
     *         will be consistent with our "meeting" given the CSP
     */
    private static boolean consistencyCheck(Meeting unassigned, List<LocalDate> solution) {
        boolean consistent = true;
    	for (DateConstraint d : unassigned.constraints) {
            LocalDate leftDate = solution.get(d.L_VAL),
                      rightDate = (d.arity() == 1) 
                          ? ((UnaryDateConstraint) d).R_VAL 
                          : solution.get(((BinaryDateConstraint) d).R_VAL);
            consistent = singleConstraintCheck(leftDate, rightDate, d.OP);
            if(!consistent) {
            	return consistent;
            }
        }
        return consistent;
    }
    
    /**
     * A method that checks if two passed in LocalDate objects are consistent
     * based on a constraint's operator
     * @param leftDate The LocalDate object on the left side of the comparison
     * @param rightDate The LocalDate object on the right side of the comparison
     * @param op The operator that is being used to compare the two LocalDate objects
     * @return A boolean telling us whether or not the single constraint being assessed is
     *         consistent with the two LocalDates being examined
     */
    private static boolean singleConstraintCheck(LocalDate leftDate, LocalDate rightDate, String op) {
        if(leftDate == null || rightDate == null) {
        	return true;
        }
        boolean sat = false;
        switch (op) {
        case "==": if (leftDate.isEqual(rightDate))  sat = true; break;
        case "!=": if (!leftDate.isEqual(rightDate)) sat = true; break;
        case ">":  if (leftDate.isAfter(rightDate))  sat = true; break;
        case "<":  if (leftDate.isBefore(rightDate)) sat = true; break;
        case ">=": if (leftDate.isAfter(rightDate) || leftDate.isEqual(rightDate))  sat = true; break;
        case "<=": if (leftDate.isBefore(rightDate) || leftDate.isEqual(rightDate)) sat = true; break;
        }
        return sat;
    }
    
    /**
     * CSP private nested class that creates an object
     * able to store domain and constraint information
     * for the meetings we are scheduled
     */
    private static class Meeting {
    	
    	//Private Fields
    	//--------------
    	private Set<DateConstraint> constraints = new HashSet<DateConstraint>();
    	private ArrayList<LocalDate> domain = new ArrayList<LocalDate>();
    	
    	/**
    	 * A domain toString() method for debugging purposes only
    	 */
    	public String toString() {
    		return domain.toString();
    	}
    }
    
    /**
     * CSP private nested class that allows us to create an
     * object storing the index value of the "head" and "tail"
     * of an Arc object we can use in our AC-3 algorithm, as
     * well as its accompanying operator
     */
    private static class Arc {
    	
    	//Private Fields
    	//--------------
    	private int tail;
    	private int head;
    	private String op;
    	
    	/**
    	 * Constructs an Arc object we can use in our AC-3 algorithm
    	 * @param tail The tail of our arc
    	 * @param head The head of our arc
    	 * @param op The operator that accompanies the binary constraint
    	 */
    	private Arc(int tail, int head, String op) {
    		this.tail = tail;
    		this.head = head;
    		this.op = op;
    	}
    }
    
}


