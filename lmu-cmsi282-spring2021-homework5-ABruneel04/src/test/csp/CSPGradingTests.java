package test.csp;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.rules.Timeout;
import org.junit.runner.Description;

import java.time.LocalDate;
import java.util.*;
import main.csp.*;

public class CSPGradingTests {
    
    // =================================================
    // Test Configuration
    // =================================================
    
    // Global timeout to prevent infinite loops from
    // crashing the test suite + to test that your
    // constraint propagation is working...
    // If they are, 3 seconds should be more than enough
    // for any test
    @Rule
    public Timeout globalTimeout = Timeout.seconds(3);
    
    // Grade record-keeping
    static int possible = 0, passed = 0;
    
    // the @Before method is run before every @Test
    @Before
    public void init () {
        possible++;
    }
    
    // Each time you pass a test, you get a point! Yay!
    // [!] Requires JUnit 4+ to run
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            passed++;
        }
    };
    
    // Used for grading, reports the total number of tests
    // passed over the total possible
    @AfterClass
    public static void gradeReport () {
        System.out.println("============================");
        System.out.println("Tests Complete");
        System.out.println(passed + " / " + possible + " passed!");
        if ((1.0 * passed / possible) >= 0.9) {
            System.out.println("[!] Nice job!"); // Automated acclaim!
        }
        System.out.println("============================");
    }
    
    /**
     * Tests whether a given solution to a CSP satisfies all constraints or not
     * @param soln Full instantiation of variables to assigned values, indexed by variable
     * @param constraints The set of constraints the solution must satisfy
     */
    public static void testSolution (List<LocalDate> soln, Set<DateConstraint> constraints) {
        for (DateConstraint d : constraints) {
            LocalDate leftDate = soln.get(d.L_VAL),
                      rightDate = (d.arity() == 1) 
                          ? ((UnaryDateConstraint) d).R_VAL 
                          : soln.get(((BinaryDateConstraint) d).R_VAL);
            
            boolean sat = false;
            switch (d.OP) {
            case "==": if (leftDate.isEqual(rightDate))  sat = true; break;
            case "!=": if (!leftDate.isEqual(rightDate)) sat = true; break;
            case ">":  if (leftDate.isAfter(rightDate))  sat = true; break;
            case "<":  if (leftDate.isBefore(rightDate)) sat = true; break;
            case ">=": if (leftDate.isAfter(rightDate) || leftDate.isEqual(rightDate))  sat = true; break;
            case "<=": if (leftDate.isBefore(rightDate) || leftDate.isEqual(rightDate)) sat = true; break;
            }
            if (!sat) {
                fail("[X] Constraint Failed: " + d);
            }
        }
    }
    
    
    // =================================================
    // Unit Tests
    // =================================================
    
    @Test
    public void CSP_t0() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new UnaryDateConstraint(0, "==", LocalDate.of(2020, 1, 3))
            )
        );
        
        // Date range of 2020-1-1 to 2020-1-5 in which the only meeting date
        // for 1 meeting can be on 2020-1-3
        List<LocalDate> solution = CSP.solve(
            1,                          // Number of meetings to schedule
            LocalDate.of(2020, 1, 1),   // Domain start date
            LocalDate.of(2020, 1, 5),   // Domain end date
            constraints                 // Constraints all meetings must satisfy
        );
        
        // Example Solution:
        // [2020-01-03]
        testSolution(solution, constraints);
    }
    
    @Test
    public void CSP_t1() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new UnaryDateConstraint(0, "==", LocalDate.of(2020, 1, 6))
            )
        );
        
        // Date range of 2020-1-1 to 2020-1-5 in which the only meeting date
        // for 1 meeting can be on 2020-1-6, which is outside of the allowable
        // range, so no solution here!
        List<LocalDate> solution = CSP.solve(
            1,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 5),
            constraints
        );
        
        assertNull(solution);
    }
    
    @Test
    public void CSP_t2() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new UnaryDateConstraint(0, ">", LocalDate.of(2020, 1, 3))
            )
        );
        
        // Date range of 2020-1-1 to 2020-1-5 in which the only meeting date
        // for 1 meeting can be AFTER 2020-1-3
        List<LocalDate> solution = CSP.solve(
            1,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 5),
            constraints
        );
        
        // Example Solution:
        // [2020-01-05]
        testSolution(solution, constraints);
    }
    
    @Test
    public void CSP_t3() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new UnaryDateConstraint(0, ">", LocalDate.of(2020, 1, 3)),
                new UnaryDateConstraint(1, ">", LocalDate.of(2020, 1, 3))
            )
        );
        
        // Date range of 2020-1-1 to 2020-1-5 in which the only meeting date
        // for 2 meetings can be AFTER 2020-1-3 (nothing here saying that they
        // can't be on the same day!)
        List<LocalDate> solution = CSP.solve(
            2,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 5),
            constraints
        );
        
        // Example Solution:
        // [2020-01-05, 2020-01-05]
        testSolution(solution, constraints);
    }
    
    @Test
    public void CSP_t4() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new UnaryDateConstraint(0, "<=", LocalDate.of(2020, 1, 2)),
                new UnaryDateConstraint(1, "<=", LocalDate.of(2020, 1, 2)),
                new BinaryDateConstraint(0, "!=", 1)
            )
        );
        
        // Date range of 2020-1-1 to 2020-1-5 in which the only meeting date
        // for 2 meetings can be BEFORE or ON 2020-1-2 but NOW they can't be on the
        // same date!
        List<LocalDate> solution = CSP.solve(
            2,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 5),
            constraints
        );
        
        // Example Solution:
        // [2020-01-02, 2020-01-01]
        testSolution(solution, constraints);
    }
    
    @Test
    public void CSP_t5() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new BinaryDateConstraint(0, "!=", 1),
                new BinaryDateConstraint(0, "!=", 2),
                new BinaryDateConstraint(1, "!=", 2)
            )
        );
        
        // Date range of 2020-1-1 to 2020-1-2 in which the only meeting date
        // for 3 meetings in a narrow time window that can't have the same
        // date! (impossible)
        List<LocalDate> solution = CSP.solve(
            3,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 2),
            constraints
        );
        
        assertNull(solution);
    }
    
    @Test
    public void CSP_t6() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new BinaryDateConstraint(0, "!=", 1),
                new BinaryDateConstraint(0, "!=", 2),
                new BinaryDateConstraint(1, "!=", 2)
            )
        );
        
        // Date range of 2020-1-1 to 2020-1-2 in which the only meeting date
        // for 3 meetings in a less narrow time window that can't have the same
        // date! (impossible)
        List<LocalDate> solution = CSP.solve(
            3,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 3),
            constraints
        );
        
        // Example Solution:
        // [2020-01-03, 2020-01-02, 2020-01-01]
        testSolution(solution, constraints);
    }
    
    @Test
    public void CSP_t7() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new BinaryDateConstraint(0, "!=", 1),
                new BinaryDateConstraint(1, "==", 2),
                new BinaryDateConstraint(2, "!=", 3),
                new BinaryDateConstraint(3, "==", 4),
                new BinaryDateConstraint(4, "<", 0),
                new BinaryDateConstraint(3, ">", 2)
            )
        );
        
        // Here's a puzzle for you...
        List<LocalDate> solution = CSP.solve(
            5,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 3),
            constraints
        );
        
        // Example Solution:
        // [2020-01-03, 2020-01-01, 2020-01-01, 2020-01-02, 2020-01-02]
        testSolution(solution, constraints);
    }
    
    @Test
    public void CSP_t8() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new UnaryDateConstraint(0, ">", LocalDate.of(2020, 1, 1)),
                new UnaryDateConstraint(1, ">", LocalDate.of(2020, 2, 1)),
                new UnaryDateConstraint(2, ">", LocalDate.of(2020, 3, 1)),
                new UnaryDateConstraint(3, ">", LocalDate.of(2020, 4, 1)),
                new UnaryDateConstraint(4, ">", LocalDate.of(2020, 5, 1)),
                new BinaryDateConstraint(0, ">", 4),
                new BinaryDateConstraint(1, ">", 3),
                new BinaryDateConstraint(2, "!=", 3),
                new BinaryDateConstraint(4, "!=", 0),
                new BinaryDateConstraint(3, ">", 2)
            )
        );
        
        // This one's simple, but requires some NODE consistency
        // preprocessing to solve in a tractable amount of time
        List<LocalDate> solution = CSP.solve(
            5,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 5, 15),
            constraints
        );
        
        // Example Solution:
        // [2020-05-15, 2020-05-15, 2020-04-30, 2020-05-14, 2020-05-14]
        testSolution(solution, constraints);
    }
    
    @Test
    public void CSP_t9() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new UnaryDateConstraint(0, ">", LocalDate.of(2020, 1, 1)),
                new UnaryDateConstraint(1, ">", LocalDate.of(2020, 2, 1)),
                new UnaryDateConstraint(2, ">", LocalDate.of(2020, 3, 1)),
                new UnaryDateConstraint(3, ">", LocalDate.of(2020, 4, 1)),
                new UnaryDateConstraint(4, ">", LocalDate.of(2020, 5, 1)),
                new BinaryDateConstraint(0, ">", 4),
                new BinaryDateConstraint(1, ">", 3),
                new BinaryDateConstraint(2, "!=", 3),
                new BinaryDateConstraint(4, "!=", 0),
                new BinaryDateConstraint(3, ">", 2)
            )
        );
        
        // This one's simple, but requires some NODE + ARC consistency
        // preprocessing to solve in a tractable amount of time
        List<LocalDate> solution = CSP.solve(
            5,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 6, 30),
            constraints
        );
        
        // Example Solution:
        // [2020-05-31, 2020-04-30, 2020-04-28, 2020-04-29, 2020-05-30]
        testSolution(solution, constraints);
    }
    
    @Test
    public void CSP_t10() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new BinaryDateConstraint(0, ">", 1),
                new BinaryDateConstraint(1, ">", 2),
                new BinaryDateConstraint(0, ">", 3),
                new BinaryDateConstraint(2, "<", 3)
            )
        );
        
        List<LocalDate> solution = CSP.solve(
            4,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 3),
            constraints
        );
        
        // Example Solution:
        // [2020-01-03, 2020-01-02, 2020-01-01, 2020-01-02]
        testSolution(solution, constraints);
    }
    
    @Test
    public void CSP_t11() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new BinaryDateConstraint(0, ">", 1),
                new BinaryDateConstraint(1, ">", 0)
            )
        );
        
        List<LocalDate> solution = CSP.solve(
            2,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 3),
            constraints
        );
        
        assertNull(solution);
    }
    
    @Test
    public void CSP_t12() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new UnaryDateConstraint(4, "!=", LocalDate.of(2020, 1, 1)),
                new BinaryDateConstraint(0, ">", 1),
                new BinaryDateConstraint(1, ">", 2),
                new BinaryDateConstraint(2, ">", 3),
                new BinaryDateConstraint(3, ">", 4)
            )
        );
        
        List<LocalDate> solution = CSP.solve(
            5,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 5),
            constraints
        );
        
        assertNull(solution);
    }
    
    @Test
    public void CSP_t13() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new UnaryDateConstraint(4, "!=", LocalDate.of(2020, 1, 1)),
                new UnaryDateConstraint(4, "==", LocalDate.of(2020, 1, 1)),
                new BinaryDateConstraint(3, ">", 1),
                new BinaryDateConstraint(1, ">", 3)
            )
        );
        
        List<LocalDate> solution = CSP.solve(
            5,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 5),
            constraints
        );
        
        assertNull(solution);
    }
    
    @Test
    public void CSP_t14() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new UnaryDateConstraint(0, ">", LocalDate.of(2020, 1, 2)),
                new UnaryDateConstraint(0, "<", LocalDate.of(2020, 1, 3)),
                new UnaryDateConstraint(1, "<", LocalDate.of(2020, 1, 3)),
                new BinaryDateConstraint(0, ">", 1)
            )
        );
        
        List<LocalDate> solution = CSP.solve(
            2,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 5),
            constraints
        );
        
        assertNull(solution);
    }
    
    @Test
    public void CSP_t15() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new UnaryDateConstraint(0, ">", LocalDate.of(2020, 1, 2)),
                new UnaryDateConstraint(0, "<", LocalDate.of(2020, 1, 4)),
                new UnaryDateConstraint(1, "<", LocalDate.of(2020, 1, 4)),
                new BinaryDateConstraint(0, ">", 1)
            )
        );
        
        List<LocalDate> solution = CSP.solve(
            2,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 5),
            constraints
        );
        
        // Example Solution:
        // [2020-01-03, 2020-01-02]
        testSolution(solution, constraints);
    }
    
    @Test
    public void CSP_t16() {
        Set<DateConstraint> constraints = new HashSet<>(
            Arrays.asList(
                new BinaryDateConstraint(0, ">=", 1),
                new BinaryDateConstraint(0, "<=", 1),
                new BinaryDateConstraint(1, "==", 0),
                new BinaryDateConstraint(3, "<", 0),
                new BinaryDateConstraint(2, ">", 1)
            )
        );
        
        List<LocalDate> solution = CSP.solve(
            4,
            LocalDate.of(1989, 11, 9),
            LocalDate.of(1989, 11, 12),
            constraints
        );
        
        // Example Solution:
        // [1989-11-10, 1989-11-10, 1989-11-12, 1989-11-09]
        testSolution(solution, constraints);
    }
    
    @Test
    public void CSP_t17() {
        final int N_CONS = 10;
        Set<DateConstraint> constraints = new HashSet<>();
        
        for (int i = 1; i < N_CONS; i++) {
            constraints.add(new BinaryDateConstraint(i, (i % 2 == 0) ? ">" : "<", i - 1));
        }
        
        List<LocalDate> solution = CSP.solve(
            N_CONS,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 1, 2),
            constraints
        );
        
        // Example Solution:
        // [2020-01-02, 2020-01-01, 2020-01-02, 2020-01-01, 2020-01-02, 2020-01-01, 2020-01-02, 2020-01-01, 2020-01-02, 2020-01-01]
        testSolution(solution, constraints);
    }
    
    @Test
    public void CSP_t18() {
        final int N_CONS = 35;
        Set<DateConstraint> constraints = new HashSet<>();
        
        for (int i = 1; i < N_CONS; i++) {
            for (int j = 0; j < N_CONS; j++) {
                if (i == j) { continue; }
                constraints.add(new BinaryDateConstraint(i, "<", j));
            }
            constraints.add(new UnaryDateConstraint(i, ">", LocalDate.of(2020, 3, 1).plusDays(i)));
        }
        
        List<LocalDate> solution = CSP.solve(
            N_CONS,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 5, 30),
            constraints
        );
        
        assertNull(solution);
    }
    
    @Test
    public void CSP_t19() {
        final int N_CONS = 100;
        Set<DateConstraint> constraints = new HashSet<>();
        
        for (int i = 1; i < N_CONS; i++) {
            for (int j = 0; j < N_CONS; j++) {
                if (i == j) { continue; }
                constraints.add(new BinaryDateConstraint(i, (i % 2 == 0) ? ">" : "<", j));
            }
            constraints.add(new UnaryDateConstraint(i, (i % 2 == 0) ? ">" : "<", LocalDate.of(2020, 3, 15)));
        }
        
        List<LocalDate> solution = CSP.solve(
            N_CONS,
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2020, 5, 30),
            constraints
        );
        
        assertNull(solution);
    }
    
}
