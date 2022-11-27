package assignment3;

class hire_rotation1 implements Runnable{
    public void run(){
        // example in the pdf
        Cat B = new Cat("Buttercup", 45, 53, 5, 85.0);
        Cat JTO = new Cat("J. Thomas O'Malley", 21, 10, 9, 20.0);
        Cat C = new Cat("Chessur", 8, 23, 2, 250.0);


        CatCafe catCafe = new CatCafe();
        catCafe.hire(B);
        catCafe.hire(JTO);
        catCafe.hire(C);

        if (!(catCafe.root.catEmployee.equals(B) && catCafe.root.senior.catEmployee.equals(C) && catCafe.root.senior.junior.catEmployee.equals(JTO))){
            throw new AssertionError("Left rotation did not work." +
                    " Chessur should be Buttercup's senior, and JTO should be Chessur's junior.");
        }
        System.out.println("Test passed.");
    }
}

class hire_rotation2 implements Runnable {
    public void run() {
        Cat A = new Cat("A", 20, 40, 5, 85.0);
        Cat B = new Cat("B", 10, 50, 2, 250.0);

        CatCafe catCafe = new CatCafe();
        catCafe.hire(A);
        catCafe.hire(B);

        if (!(catCafe.root.catEmployee.equals(B) && catCafe.root.junior.catEmployee.equals(A))) {
            throw new AssertionError("Left rotation did not work." +
                    " Cat B should be root and Cat A should be  B's junior");
        }

        if (catCafe.root.junior.parent != catCafe.root || catCafe.root.parent != null) {
            throw new AssertionError("Parent pointers are not set correctly.");
        }

        System.out.println("Test passed.");
    }
}

class hire_rotation3 implements Runnable {
    public void run() {
        Cat A = new Cat("A", 20, 40, 5, 85.0);
        Cat B = new Cat("B", 30, 50, 2, 250.0);

        CatCafe catCafe = new CatCafe();
        catCafe.hire(A);
        catCafe.hire(B);

        if (!(catCafe.root.catEmployee.equals(B) && catCafe.root.senior.catEmployee.equals(A))) {
            throw new AssertionError("Right rotation did not work." +
                    " Cat B should be root and Cat A should be B's senior");
        }

        if (catCafe.root.senior.parent != catCafe.root) {
            throw new AssertionError("Parent pointers are not set correctly");
        }

        System.out.println("Test passed.");
    }
}

class hire_rotation4 implements Runnable {
    public void run() {
        Cat A = new Cat("A", 20, 40, 5, 85.0);
        Cat B = new Cat("B", 10, 50, 2, 250.0);
        Cat C = new Cat("C", 30, 60, 2, 250.0);

        CatCafe catCafe = new CatCafe();
        catCafe.hire(A);
        catCafe.hire(B);
        catCafe.hire(C);

        if (!(catCafe.root.catEmployee.equals(C) && catCafe.root.senior.catEmployee.equals(B) && catCafe.root.senior.junior.catEmployee.equals(A))) {
            throw new AssertionError("Left and right rotations did not work." +
                    " Cat C should be root, Cat B should be C's senior and Cat A should be B's junior");
        }

        if (catCafe.root.senior.parent != catCafe.root && catCafe.root.senior.junior.parent != catCafe.root.senior) {
            throw new AssertionError("Parent pointers are not set correctly.");
        }
        System.out.println("Test passed.");
    }
}

public class A3_Tester2 {
    static String[] tests = {
            "assignment3.hire_rotation1",
            "assignment3.hire_rotation2",
            "assignment3.hire_rotation3",
            "assignment3.hire_rotation4"
    };
    public static void main(String[] args) {
        int numPassed = 0;
        for(String className: tests)    {
            System.out.printf("%n======= %s =======%n", className);
            System.out.flush();
            try {
                Runnable testCase = (Runnable) Class.forName(className).getDeclaredConstructor().newInstance();
                testCase.run();
                numPassed++;
            } catch (AssertionError e) {
                System.out.println(e);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        System.out.printf("%n%n%d of %d tests passed.%n", numPassed, tests.length);
    }
}