package assignment3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CatCafe implements Iterable<Cat> {
	public CatNode root;

	public CatCafe() {
	}


	public CatCafe(CatNode dNode) {
		this.root = dNode;
	}

	// Constructor that makes a shallow copy of a CatCafe
	// New CatNode objects, but same Cat objects
	public CatCafe(CatCafe cafe) {

		//root = new CatNode(cafe.root.catEmployee);
		root =shallowcopy(cafe.root);
		
		
	}


	private CatNode shallowcopy(CatNode root) {
		
		if(root == null) {
			return null;
		}
		
		CatNode node = new CatNode(root.catEmployee);  
		node.parent = root.parent;
		
		node.senior = shallowcopy(root.senior);
		
		if(node.senior != null) {
		node.senior.parent=node;
		}
		node.junior = shallowcopy(root.junior);
		if(node.junior != null) {
		node.junior.parent=node;
		}

		
		
		return node;
	}

	
	// add a cat to the cafe database
	public void hire(Cat c) {
		if (root == null) 
			root = new CatNode(c);
		else
			root = root.hire(c);
	}

	// removes a specific cat from the cafe database
	public void retire(Cat c) {
		if (root != null)
			root = root.retire(c);
	}

	// get the oldest hire in the cafe
	public Cat findMostSenior() {
		if (root == null)
			return null;

		return root.findMostSenior();
	}

	// get the newest hire in the cafe
	public Cat findMostJunior() {
		if (root == null)
			return null;

		return root.findMostJunior();
	}

	// returns a list of cats containing the top numOfCatsToHonor cats 
	// in the cafe with the thickest fur. Cats are sorted in descending 
	// order based on their fur thickness. 
	public ArrayList<Cat> buildHallOfFame(int numOfCatsToHonor) {

		if(numOfCatsToHonor == 0) {
			return new ArrayList<Cat>();
		}
		
		ArrayList<Cat> honorlist_long = new ArrayList<Cat>();
		
		insert(root, honorlist_long);
	
		System.out.println("long version:"+honorlist_long.toString());
		ArrayList<Cat> finalhonorlist = new ArrayList<Cat>();
		for(int i=0; i < numOfCatsToHonor; i++) {
			
			finalhonorlist.add(honorlist_long.get(i));
		}
		System.out.println("Cutdown version:"+finalhonorlist.toString());
		
		return finalhonorlist;
	}
	
	
	private void insert(CatNode root, ArrayList<Cat> honorlist) {

		if(root == null) {
			return;
		}
		int i=0;
		do {

			if(honorlist.size() == 0) {
				honorlist.add(root.catEmployee);
				break;
			} else if(honorlist.get(i).getFurThickness() < root.catEmployee.getFurThickness()) {
				honorlist.add(i, root.catEmployee);
				break;
			} else if (i == honorlist.size()-1) {
				honorlist.add(root.catEmployee);
				break;
			}
			i++;
		} while(i<honorlist.size());

		insert(root.senior, honorlist);

		insert(root.junior, honorlist);
	}
	
	
	
	
	// Returns the expected grooming cost the cafe has to incur in the next numDays days
	public double budgetGroomingExpenses(int numDays) {
		
		double cost=0;
		if(root.catEmployee.getDaysToNextGrooming() <= numDays) {
		cost += root.catEmployee.getExpectedGroomingCost();
		}
		cost += CalcGroomingExpenses(root.senior, numDays);		
		cost += CalcGroomingExpenses(root.junior, numDays);	
	
		return cost;
	}

	// WORKS
	private double CalcGroomingExpenses(CatNode root, int numdays) {
		double cost=0;
		if(root == null) {
			return 0;
		}
		if ( root.catEmployee.getDaysToNextGrooming() <= numdays ) {
			cost += root.catEmployee.getExpectedGroomingCost();
		}	
		return cost + CalcGroomingExpenses(root.senior, numdays) + CalcGroomingExpenses(root.junior, numdays);
	}
	
	
	// returns a list of list of Cats. 
	// The cats in the list at index 0 need be groomed in the next week. 
	// The cats in the list at index i need to be groomed in i weeks. 
	// Cats in each sublist are listed in from most senior to most junior. 
	public ArrayList<ArrayList<Cat>> getGroomingSchedule() {
		/*
		 * TODO: ADD YOUR CODE HERE
		 */
		return null;
	}


	public Iterator<Cat> iterator() {
		return new CatCafeIterator();
	}


	public class CatNode {

		public Cat catEmployee;
		public CatNode junior;
		public CatNode senior;
		public CatNode parent;

		public CatNode(Cat c) {
			this.catEmployee = c;
			this.junior = null;
			this.senior = null;
			this.parent = null;
		}

		// add the c to the tree rooted at this and returns the root of the resulting tree
		public CatNode hire (Cat c) {

			CatNode newNode = findCatNode(add(root, c), c);

			//do tree rotation
			while(newNode.parent != null && newNode.parent.catEmployee.getFurThickness() < newNode.catEmployee.getFurThickness()) {
				if(newNode == newNode.parent.junior) {
					newNode=rightRotate(newNode);
				} else if(newNode== newNode.parent.senior) {
					newNode=leftRotate(newNode);;
				}
			}
			return root;
		}


		//WORKS
		private CatNode rightRotate(CatNode newnode) {

			if (newnode == null) {
				return null;
			}
			CatNode root=newnode.parent;

			//egde case for when the parent of the newnode is the root
			if(newnode.parent == CatCafe.this.root) {
				CatCafe.this.root.parent=newnode;

				CatNode tmp = newnode.senior; 

				newnode.senior=CatCafe.this.root;
				CatCafe.this.root.junior=tmp;

				CatCafe.this.root=newnode;
				newnode.parent=null;
				return newnode;
			}

			//fixes the parent
			if(root.parent.junior == root) {
				root.parent.junior=newnode;
			} else if(root.parent.senior == root) {
				root.parent.senior=newnode;
			}
			newnode.parent=root.parent;
			////

			CatNode tmp=newnode.senior;

			newnode.senior=root;
			root.parent=newnode;

			root.junior=tmp;
			if(tmp != null) {
				tmp.parent=root;
			}


			return newnode;

		}

		//WORKS
		private CatNode leftRotate(CatNode newnode) {

			if(newnode ==null) {
				return null;
			}
			CatNode root=newnode.parent;

			//egde case if newnode is root
			if(newnode.parent == CatCafe.this.root) {
				CatCafe.this.root.parent=newnode;

				CatNode tmp = newnode.junior; 

				newnode.junior=CatCafe.this.root;
				CatCafe.this.root.senior=tmp;

				CatCafe.this.root=newnode;
				newnode.parent=null;
				return newnode;
			}

			if(root.parent.junior == root) {
				root.parent.junior=newnode;
			} else if(root.parent.senior == root) {
				root.parent.senior=newnode;
			}
			newnode.parent=root.parent;

			CatNode tmp=newnode.junior;

			newnode.junior=root;
			root.parent=newnode;

			root.senior=tmp;

			if(tmp != null) {
				tmp.parent=root;
			}

			return newnode;
		}

		//used to locate a specific cat in tree
		private CatNode findCatNode(CatNode root, Cat c) {
			if (root ==null) {
				return null;
			}

			if (root.catEmployee.getMonthHired() == c.getMonthHired()) {
				return root;
			}

			CatNode tmp = findCatNode(root.junior,c);
			if(tmp != null) {
				return tmp;
			}

			return findCatNode(root.senior,c);

		}

		//this method returns the cat that was added
		private CatNode add(CatNode root, Cat c) {

			if (root == null) {
				root = new CatNode(c);
			} else if(root.catEmployee.getMonthHired() < c.getMonthHired()) {
				root.junior = add(root.junior, c);
				root.junior.parent=root;

			} else if (root.catEmployee.getMonthHired() > c.getMonthHired()) {
				root.senior = add(root.senior, c);
				root.senior.parent=root;

			}
			return root;
		}
		// remove c from the tree rooted at this and returns the root of the resulting tree
		public CatNode retire(Cat c) {
			
			
			if(root.catEmployee.equals(c) && root.senior ==null && root.junior == null) {
				return null;
			}
			
			CatNode node=remove(root, c);
			

			while(node != null && node.junior.catEmployee.getFurThickness() < node.catEmployee.getFurThickness()) {
				if(node == node.parent.junior) {
					node=rightRotate(node.junior);
				} else if(node == node.parent.senior) {
					node=leftRotate(node.junior);
				}
			}
			return root;
		}
		
		
		public CatNode remove(CatNode root, Cat c) {
			
			
			if(root ==CatCafe.this.root) { //check if we are trying to remove the head
				return null; 
				
			} else if(root.catEmployee.equals(c)) {
				
			CatNode mostsenior;	
			
			if(root.junior == null) {
				mostsenior=root;
			} else if(root.junior.senior == null) {
				mostsenior=root.junior;
			} else {
				mostsenior = findCatNode(root, root.junior.findMostSenior());
			}
			mostsenior.parent.senior=null;
			
			mostsenior.parent=root.parent;
			if(root.parent.senior == root) {
				root.parent.senior=mostsenior;
			} else if(root.parent.junior == root) {
				root.parent.junior=mostsenior;
			}
			mostsenior.junior=root.junior;
			mostsenior.junior.parent=mostsenior;
			
			mostsenior.senior=root.senior;
			mostsenior.senior.parent=mostsenior;
			return mostsenior;
			} else if(root.catEmployee.getMonthHired() < c.getMonthHired()) {
				return remove(root.senior, c);
				
			} else if(root.catEmployee.getMonthHired() > c.getMonthHired()) {
				return remove(root.junior, c);
			}
			return root;
		}
		// find the cat with highest seniority in the tree rooted at this
		public Cat findMostSenior() {
			if (senior == null) {
				return catEmployee;
			} else {
				return senior.findMostSenior();
			}
		}

		//
		// find the cat with lowest seniority in the tree rooted at this
		public Cat findMostJunior() {
			if(junior == null) {
				return catEmployee;
			} else {
				return junior.findMostJunior();
			}
		}

		// Feel free to modify the toString() method if you'd like to see something else displayed.
		public String toString() {
			String result = this.catEmployee.toString() + "\n";
			if (this.junior != null) {
				result += "junior than " + this.catEmployee.toString() + " :\n";
				result += this.junior.toString();
			}
			if (this.senior != null) {
				result += "senior than " + this.catEmployee.toString() + " :\n";
				result += this.senior.toString();
			} 
			if (this.parent != null) {
				result += "parent of " + this.catEmployee.toString() + " :\n";
				result += this.parent.catEmployee.toString() +"\n";
			}
			
			return result;
		}
	}


	private class CatCafeIterator implements Iterator<Cat> {
		// HERE YOU CAN ADD THE FIELDS YOU NEED

		private CatCafeIterator() {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */
		}

		public Cat next(){
			/*
			 * TODO: ADD YOUR CODE HERE
			 */
			return null;
		}

		public boolean hasNext() {
			/*
			 * TODO: ADD YOUR CODE HERE
			 */
			return false;
		}

	}
	
	private static void printTree(CatNode root, int spaceCount)
    {
        if(root==null) {
            return;
        }
        int spacing = spaceCount+24;
        
        printTree(root.senior, spacing);
        
        System.out.println();
        for(int index=0; index < spacing+root.catEmployee.toString().length()+1; index++) {
            System.out.print(" ");
        }
        if (root.senior != null) {
            System.out.print("/");
            System.out.println();
            for(int index=0; index < spacing+root.catEmployee.toString().length(); index++) {
                System.out.print(" ");
            }
            if (root.senior != null) {
                System.out.print("/");
            }
        }

        System.out.println();
        for(int index=0; index < spacing; index++) {
            System.out.print(" ");
        }
        System.out.println(root.catEmployee);

        for(int index=0; index < spacing+root.catEmployee.toString().length(); index++) {
            System.out.print(" ");
        }
        if (root.junior != null) {
            System.out.print("\\");
            System.out.println();
            for(int index=0; index < spacing+root.catEmployee.toString().length()+1; index++) {
                System.out.print(" ");
            }
            if (root.junior != null) {
                System.out.print("\\");
            }
        }
        printTree(root.junior, spacing);
    }

	//for testing 
	public static void main(String[] args) {
		
		Cat B = new Cat("Buttercup", 45, 53, 5, 85.0);
		Cat C = new Cat("Chessur", 8, 23, 2, 250.0);
		Cat J = new Cat("Jonesy", 0, 21, 12, 30.0);	
		Cat JJ = new Cat("JIJI", 156, 17, 1, 30.0);
		Cat JTO = new Cat("J. Thomas O'Malley", 21, 10, 9, 20.0);
		Cat MrB = new Cat("Mr. Bigglesworth", 71, 0, 31, 55.0);
		Cat MrsN = new Cat("Mrs. Norris", 100, 68, 15, 115.0);
		Cat T = new Cat("Toulouse", 180, 37, 14, 25.0);


		Cat BC = new Cat("Blofeld's cat", 6, 72, 18, 120.0);
		Cat L = new Cat("Lucifer", 10, 44, 20, 50.0);
		
		CatCafe Cafe = new CatCafe();
		
		 
		Cafe.hire(B);
		printTree(Cafe.root, 0);
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------");
		Cafe.hire(C);
		printTree(Cafe.root, 0);
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------");
		Cafe.hire(J);
		printTree(Cafe.root, 0);
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------");
		Cafe.hire(JJ);
		printTree(Cafe.root, 0);
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------");
		Cafe.hire(JTO);
		printTree(Cafe.root, 0);
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------");
		Cafe.hire(MrB);
		printTree(Cafe.root, 0);
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------");
		Cafe.hire(MrsN);
		printTree(Cafe.root, 0);
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------");
		Cafe.hire(T);
		printTree(Cafe.root, 0);
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------");
		Cafe.hire(BC);
		printTree(Cafe.root, 0);
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------");
		Cafe.hire(L);
		printTree(Cafe.root, 0);
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------");
		
		
		Cafe.buildHallOfFame(5);
		
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------");
		
		CatCafe caf = new CatCafe(Cafe);
		printTree(caf.root, 0);
		
		System.out.println();
	    System.out.println("The most junior cat is: "+Cafe.findMostJunior().toString()+" hired at month: "+Cafe.findMostJunior().getMonthHired());
	    System.out.println("The most senior cat is: "+Cafe.findMostSenior().toString()+" hired at month: "+Cafe.findMostSenior().getMonthHired());
		
	}
}




