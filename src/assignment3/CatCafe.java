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
	
	//TODO MAKE THIS METHOD RUN IN O(n) TIME  <---------IMPORTANT
	public ArrayList<Cat> buildHallOfFame(int numOfCatsToHonor) {

		if(numOfCatsToHonor == 0) {
			return new ArrayList<Cat>(0);
		}
		
		ArrayList<Cat> honorlist_long = new ArrayList<Cat>(numOfCatsToHonor);
		
		insert(root, honorlist_long);
	
		System.out.println("long version:"+honorlist_long.toString());
		ArrayList<Cat> finalhonorlist = new ArrayList<Cat>();
		for(int i=0; i < numOfCatsToHonor; i++) {
			
			if(i < honorlist_long.size()) {
			finalhonorlist.add(honorlist_long.get(i));
			} else {
				break;
			}
			
		}
		System.out.println("Cutdown version:"+finalhonorlist.toString());
		
		return finalhonorlist;
	}
	
	//TODO FIX STACKOVERFLOW ERROR IN THE TESTER
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

			//CatNode newNode = findCatNode(add(root, c), c); //findCatNode return the CatNode of the following cat 
			CatNode newNode = add(root, c,null);
			
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

			if(root == null) {
				return newnode;
			}
			//edge case for when the parent of the newnode is the root
			if(newnode.parent == CatCafe.this.root) {
				//CatCafe.this.root.parent=newnode;

				CatNode tmp = newnode.senior; 

				newnode.senior=CatCafe.this.root;
				CatCafe.this.root.parent=newnode;
				
				CatCafe.this.root.junior=tmp;
				if(tmp != null) {
				tmp.parent=CatCafe.this.root;
				}
				CatCafe.this.root=newnode;
				newnode.parent=null;
				return newnode;
			} else if (root.parent != null) {
				if(root.parent.junior == root) {
					root.parent.junior=newnode;
				} else if(root.parent.senior == root) {
					root.parent.senior=newnode;
				}
			}

			//fixes the parent
			newnode.parent=root.parent;
			CatNode tmp=newnode.senior;

			newnode.senior=root;
			root.parent=newnode;

			root.junior=tmp;
			if(tmp != null) {
				tmp.parent=root;
			}


			return newnode;

		}

		//TODO works
		private CatNode leftRotate(CatNode newnode) {

			if(newnode ==null) {
				return null;
			}
			CatNode root=newnode.parent;

			if(root == null) {
				return newnode;
			}
			//edge case if newnode parent is root
			if(newnode.parent == CatCafe.this.root) {
				

				CatNode tmp = newnode.junior; 
				
				newnode.junior=CatCafe.this.root;
				CatCafe.this.root.parent=newnode;
				
				CatCafe.this.root.senior=tmp;
				
				
				if(tmp !=null) {
					tmp.parent=CatCafe.this.root;
				}
				CatCafe.this.root=newnode;
				newnode.parent=null;
				return newnode;
			} else if (root.parent != null) {
				
				if(root.parent.junior == root) {
					root.parent.junior=newnode;
				} else if(root.parent.senior == root) {
					root.parent.senior=newnode;
				}
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
			} else if (root.catEmployee.getMonthHired() == c.getMonthHired()) {
				return root;
			} else if(root.catEmployee.getMonthHired() < c.getMonthHired()) {
				return findCatNode(root.junior,c);
			} else if (root.catEmployee.getMonthHired() > c.getMonthHired()) {
				return findCatNode(root.senior,c);
			}
			return null;
		}

		//this method returns the catNode that was added
		private CatNode add(CatNode root, Cat c, CatNode parentRoot) {

			CatNode tmp=null;
			
			if (root == null) {
				root = new CatNode(c);
				root.parent=parentRoot;
				if(parentRoot.catEmployee.getMonthHired() < c.getMonthHired()) {
					parentRoot.junior=root;
				} else {
					parentRoot.senior=root;
				}
				return root;
			} else if(root.catEmployee.getMonthHired() < c.getMonthHired()) {
				tmp = add(root.junior, c, root);
			
			} else if (root.catEmployee.getMonthHired() > c.getMonthHired()) {
				tmp = add(root.senior,c, root);
			
			}
			return tmp;
		}
		
		// remove c from the tree rooted at this and returns the root of the resulting tree
		
		//TODO WORKS
		public CatNode retire(Cat c) {

			
			if(root.catEmployee.equals(c) && root.senior ==null && root.junior == null) {
				return null;
			} 

			CatNode node=remove(this, c);  
			
			if(this == node || node == null) {
				return this;
			} 
			
			CatNode tmp=null;



			if(node.junior != null && node.junior.catEmployee.getFurThickness() > node.catEmployee.getFurThickness()) {
				if(node.senior == null) {
					tmp=node.junior;	
				} else if(node.junior.catEmployee.getFurThickness() >  node.senior.catEmployee.getFurThickness()) {
					tmp = node.junior;
				} else {
					tmp = node.senior;					
				}
			} else if(node.senior != null && node.senior.catEmployee.getFurThickness() > node.catEmployee.getFurThickness()){				
				if(node.junior == null) {			
					tmp = node.senior;				
				} else if(node.junior.catEmployee.getFurThickness() <  node.senior.catEmployee.getFurThickness()) {
					tmp = node.senior;				
				} else {
					tmp = node.junior;					
				}
			} else if(this == root){
				tmp=root;
			} else {
				tmp=node;
			}
			
			
			while(node.senior != null || node.junior !=null) {
				if(node.junior != null && node.junior.catEmployee.getFurThickness() > node.catEmployee.getFurThickness()) {
					if(node.senior == null) {
						rightRotate(node.junior);	
					} else if(node.junior.catEmployee.getFurThickness() >  node.senior.catEmployee.getFurThickness()) {
						rightRotate(node.junior);
					} else {
						leftRotate(node.senior);					
					}
				} else if(node.senior != null && node.senior.catEmployee.getFurThickness() > node.catEmployee.getFurThickness()){				
					if(node.junior == null) {			
						leftRotate(node.senior);				
					} else if(node.junior.catEmployee.getFurThickness() <  node.senior.catEmployee.getFurThickness()) {
						leftRotate(node.senior);				
					} else {
						rightRotate(node.junior);					
					}
				} else {
					break;
				}
			}
			return tmp; //TODO make it so that it returns the instance of the new head of the node 
		}

		//WORKS (FOR NOW)
		//this method returns the node that the removed node is replaced by
		private CatNode remove(CatNode root, Cat c) {
			
			
			if(root == null) {
				return this;
		
			}
			
			//edge case for when we must remove the head
			if(root.catEmployee.equals(c) && root.parent == null) {
				
				//edge case for when the head as no junior child 
				if(root.junior==null) {
					root.senior.parent=null;
					CatCafe.this.root=root.senior;
					return CatCafe.this.root;
				} 
				
				CatNode newNode = findCatNode(root, root.junior.findMostSenior());
				//edge case for when the node to replace the head is its junior child
				if(root.junior == newNode) {
					newNode.senior=root.senior;
					if(newNode.senior != null) {
						newNode.senior.parent=newNode;
					}
					newNode.parent=null;
					CatCafe.this.root=newNode;
					
					return newNode;
				}
				
				
				if(newNode.junior != null) {
					newNode.junior.parent=newNode.parent;
					newNode.parent.senior=newNode.junior;
				} else {
					newNode.parent.senior=null;
				}
				
				newNode.senior=root.senior;
				newNode.senior.parent=newNode;
				
				newNode.junior=root.junior;
				newNode.junior.parent=newNode;
				
				newNode.parent=null;
				CatCafe.this.root=newNode;
				return CatCafe.this.root;
				
			} else if(root.catEmployee.equals(c)) {
				//edge case for when the node to remove as no children
				if(root.junior == null && root.senior == null) {
					if(root.parent.junior == root) {
						root.parent.junior=null;
					} else if(root.parent.senior == root) {
						root.parent.senior=null;
					}
					return null;
					//edge case for when the node to remove as no junior child
				} else if(root.junior == null) {
					root.senior.parent=root.parent;
					if(root.parent.junior == root) {
						root.parent.junior=root.senior;
					} else if(root.parent.senior == root) {
						root.parent.senior=root.senior;
					}
					return root.senior;
					//egde case for when the node to remove as no senior child
				} else if(root.senior == null) {
					root.junior.parent=root.parent;
					if(root.parent.junior == root) {
						root.parent.junior=root.junior;
					} else if(root.parent.senior == root) {
						root.parent.senior=root.junior;
					}
					return root.senior;
				}
				
				CatNode newNode = findCatNode(root, root.junior.findMostSenior());
				
				
				if(newNode==root) {
					newNode.junior.parent=newNode.parent;
					newNode.parent.senior=newNode.junior;
					return newNode.junior;
				}
				
				if(newNode.junior != null) {
					newNode.junior.parent=newNode.parent;
					newNode.parent.senior=newNode.junior;
				} else {
					newNode.parent.senior=null;
				}
				
				newNode.parent=root.parent;
				newNode.senior=root.senior;
				newNode.junior=root.junior;
				return newNode;
				
			} else if(root.catEmployee.getMonthHired() < c.getMonthHired()) {
				return remove(root.junior, c);
				
			} else if(root.catEmployee.getMonthHired() > c.getMonthHired()) {
				return remove(root.senior, c);
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
//			String result = this.catEmployee.toString() + "\n";
//			if (this.junior != null) {
//				result += "junior than " + this.catEmployee.toString() + " :\n";
//				result += this.junior.toString();
//			}
//			if (this.senior != null) {
//				result += "senior than " + this.catEmployee.toString() + " :\n";
//				result += this.senior.toString();
//			} 
//			if (this.parent != null) {
//				result += "parent of " + this.catEmployee.toString() + " :\n";
//				result += this.parent.catEmployee.toString() +"\n";
//			}
				String result = this.catEmployee.toString();
			return result;
		}
	}


	//REVISE THIS
	private class CatCafeIterator implements Iterator<Cat> {
		// HERE YOU CAN ADD THE FIELDS YOU NEED
		private ArrayList<Cat> CatArray = new ArrayList<Cat>(); //will be used to store the cats in ascending order of seniority
		private int currentindex;
		
		
		//sets the arraylist in a depthfirst postorder traversal
		private CatCafeIterator() {
			InorderInsertion(CatCafe.this.root, CatArray);
			currentindex=0;
		}

		void InorderInsertion(CatNode root, ArrayList<Cat> arr) {
			if(root != null) {
				InorderInsertion(root.junior, arr);
				arr.add(root.catEmployee);
				InorderInsertion(root.senior, arr);
			} 
			return;
		}
		
		//gets next element in arraylist
		public Cat next(){
			
			Cat c = CatArray.get(currentindex);
			currentindex++;
			return c;
		}

		//checks if the next element in the array exists
		public boolean hasNext() {	
			 if(CatArray.size() == 0) {
				 return false;
			 } else if(currentindex <= CatArray.size()-1 && CatArray.get(currentindex) != null) {
				 return true;
			 } else {
				 return false;
			 }
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
		

		
		//Cafe.buildHallOfFame(5);
		//Cafe.retire(J);
		System.out.println(Cafe.root.junior);
		System.out.println("\n---------------------------------------------------------------------------------------------------------------------------");
		
		CatCafe caf = new CatCafe(Cafe);
		printTree(caf.root, 0);
		

		System.out.println();
	    System.out.println("The most junior cat is: "+Cafe.findMostJunior().toString()+" hired at month: "+Cafe.findMostJunior().getMonthHired());
	    System.out.println("The most senior cat is: "+Cafe.findMostSenior().toString()+" hired at month: "+Cafe.findMostSenior().getMonthHired());
		
	}
}




