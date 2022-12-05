package assignment3;

import java.util.ArrayList;

import assignment3.CatCafe.CatNode;

public class Tester3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		 CatCafe school = new CatCafe();
	        ArrayList<Cat> angels = new ArrayList<>();
	        Cat Giulia = new Cat("GIULIA :DDDD",40,Integer.MAX_VALUE-1,7,35);
	        Cat Kavosh = new Cat("KAVOSH <3",45,Integer.MAX_VALUE,2,15);
	        Cat Justine = new Cat("JUSTINE:D",37,100,15,40);

	        Cat Anthony = new Cat("ANTHONY:D",20,3,5,10);
	        Cat Austin = new Cat("AUSTIN:D",39,4,5,10);
	        Cat Elise = new Cat("ÉLISE:D",49,32,9,45);
	        Cat Emma = new Cat("EMMA:D",47,33,1,45);

	        Cat Jack = new Cat("JACK:D",32,6,0,10);
	        Cat Japmann = new Cat("JAPMANN:D",48,41,3,50);
	        Cat Kaixiang = new Cat("KAIXIANG:D",21,2,3,5);

	        Cat Lancer = new Cat("LANCER:D",52,8,11,15);
	        Cat Liam = new Cat("LIAM:D",7, 11,7,25);
	        Cat Mayank = new Cat("MAYANK:D",3,12,27,15);
	        Cat Nikhil = new Cat("NIKHIL:D",17,9,17,15);
	        Cat Nina = new Cat("NINA:D",9,40,12,10);

	        Cat Rohan = new Cat("ROHAN:D",0,5,2,65);
	        Cat Sasha = new Cat("SASHA:D",4,17,8,70);
	        Cat Tianyu = new Cat("TIANYU:D",67,26,15,15);
	        Cat Tudor = new Cat("TUDOR:D",59,10,30,100);
	        Cat Victor = new Cat("VICTOR:D",65,1,1,10);

	        Cat Wenqi = new Cat("WENQI:D",22,31,10,40);
	        Cat Zhihao = new Cat("ZHIIHAO:D",70,0,7,5);


	        angels.add(Giulia); angels.add(Kavosh); angels.add(Justine);
	        angels.add(Anthony); 
	        angels.add(Austin); angels.add(Elise); angels.add(Emma);
	        angels.add(Jack); angels.add(Japmann); angels.add(Kaixiang);
	        angels.add(Lancer); angels.add(Liam); angels.add(Mayank); angels.add(Nikhil); angels.add(Nina);
	        angels.add(Rohan); angels.add(Sasha); angels.add(Tianyu); angels.add(Tudor); angels.add(Victor);
	        angels.add(Wenqi); angels.add(Zhihao);

	        CatNode tmp;
	        for (Cat angel: angels)
	        {
	            school.hire(angel);
	            printTree(school.root,0);
	            System.out.println("\n----------------------------------------------------------------------------------------------------------------------------------------");
	           
	        }
	        
	        System.out.println(school.buildHallOfFame(3));
	        System.out.println("\n\n\n\n\n");
	        System.out.println(school.buildHallOfFame(50));
	        System.out.println();
	        
	        ArrayList<ArrayList<Cat>> groomingSchedule = school.getGroomingSchedule();

	        for(ArrayList<Cat> week: groomingSchedule)
	        {
	            for(Cat angel: week)
	                System.out.print(angel +"\t");
	            System.out.println();
	        }
	        
	        school.retire(Lancer); 
	        school.retire(Nikhil); 
	        school.retire(Liam);
	        printTree(school.root,0);
	        System.out.println("\n----------------------------------------------------------------------------------------------------------------------------------------");
	        school.retire(Nina);
	        printTree(school.root,0);
//	        
//	        school.retire(school.root.catEmployee);
//	        printTree(school.root,0);
//	        
	        
	        
	        
	        CatCafe copy = new CatCafe(school);
	        
	        for(Cat cat: copy)
	            for(Cat angel: school)
	                if(cat==angel)
	                    System.out.println("COPY IS GOOD");
	}
	
	

	public static void printTree(CatNode root, int spaceCount)
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

}
