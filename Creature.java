package gr.auth.ee.dsproject.pacman;
import java.util.ArrayList;

import gr.auth.ee.dsproject.node1.*;

/**
 * <p>Title: DataStructures2011</p>
 *
 * <p>Description: Data Structures project: year 2011-2012</p>
 *
 * <p>Copyright: Copyright (c) 2011</p>
 *
 * <p>Company: A.U.Th.</p>
 *
 * @author Michael T. Tsapanos
 * @version 1.0
 */


public class Creature implements gr.auth.ee.dsproject.pacman.AbstractCreature
{

  public String getName (){
	  
	  	return "Mine";
    
  }

  		private int step = 1;
  		private boolean amPrey;

  public Creature (boolean isPrey){

	  
	  	amPrey = isPrey;

  }
  
  		
  		
  public int calculateNextPacmanPosition (Room[][] Maze, int[] currPosition){	
	  	
	  	//Declarations
	  
	  
	  	//k stands for the different directions
	  	//g stands for ghosts
	  	//l stands for levels of invalidity (for the neighbor nodes) 
	  	int d,g,l;	
	  	int pacmanX=currPosition[0], pacmanY=currPosition[1], moveToReturn = -1;
	  	
	  	
	  	//Position variables
	  	// By the word "Poss" I mean "possible", all of the 4 directions
	    int [][] possPac= new int[4][2];
	    int[][] currGhost= new int[PacmanUtilities.numberOfGhosts][2];
	    int[][] possGhost= new int[4*PacmanUtilities.numberOfGhosts][2];
	    
	    
	    // The node where Pacman is at the moment, without any specific direction
	    // I use this instance of the class Node878287825 to get some variables through the class
	    Nodeaki currPacNode= new Nodeaki(pacmanX, pacmanY, Maze);
	    
	    
	    //Each node is an intersection, we must be really careful to mind the traffic lights ;)
	    // Otherwise we might have a fatal accident with a ghost or a wall :O 
	  	boolean[][] trafficLight= new boolean[4][3];

	  	
	  	//What we need to implement, to find where Pacman is allowed to go
	  	ArrayList<Nodeaki> validMoves= new ArrayList<Nodeaki>(4);
	  	
	  	//They represent some characteristics of the "Chosen" Node, the "one" that we will move to:
	  	//the nodeMove and the nodeEvaluation, respectively.
		int maxNodeDir;
		double maxNodeEval;
	  	
	    //Now let's play:
		
		/*Our game has two phases:
		 *-Phase 1: We will check which moves are valid
		 *-Phase 2: We will choose the best of these moves, according to their evaluations.
		 *Let's follow them more specific:
		 */
		
		//Phase 1: VALID MOVES
		
		/*Our trafficLight array will be tha major variable. We will initialize its  values to true.
		 * Then we will check for 3 things in each direction:
		 * -->If there is wall in the node
		 * -->If there is a ghost in the node
		 * -->If a ghost might end up in the node with us
		 * So, we have 3 levels of invalidity for Pacman's next node:
		 * ---If it's a wall we CAN'T go, we 're not that strong to break the wall.
		 * ---If a ghost is already there, then it's like a suicide, it's like walking right into the wolf's mouth.
		 * This can't be an option unless all the other options are walls. Then suicide is a good option :P
		 * ---And last but no least, if a ghost is right next to the node we 're moving to. The ghost will catch us
		 * but it is still better than heading right to the wall, or commiting a suicide :P So we prefer this than
		 * the others, if there isn't a free node.
		 * After we look for these invalid nodes, all the nodes that haven't been switched to false are valid :clap
		 * To check all these we need some ingredients:
		 * -the current position of the ghosts;
		 * -the current position of Pacman(we have it);
		 * -the possible positions of Pacman;
		 * -the possible positions of each Ghost;
		 * -the position of the walls (we will call the function Maze[x][y].walls[direction]);
		 * Let's start!
		 */	
		
		// I use a getter to have access to the position of the ghosts which is private.
		//It is a 2-Dimensional table with rows as many as the ghosts, and columns the coordinates of each one.
	    currGhost=currPacNode.getCurrentGhostPos();
	    
	    //A loop to initialize some things for every direction 
	  	for (d=0; d<4; d++) { 
	  		
	  			//We initialize the trafficLight array which is 2-Dimensional: the outter loop expresses the 4 directions 
	  			//and the inner loop expresses the 3 levels of invalidity. (4x3)
	  			for (l=0; l <3; l++) {
	  				
	  					trafficLight[d][l]=true;
	  					
	  			}
	  			
	  			//We store all the 4 possible positions from the current Pacman-node, calling the findTempPos function
	  			// 2 Dimensions: the 1st one expresses the 4 directions and the other represents the coordinates (4x2)
	  			possPac[d] = currPacNode.findTempPos(pacmanX, pacmanY, d);
	  			
	  			/*Here comes the sun!
	  			 *We want ALL the possible positions that a ghost might be found.
	  			 *Each of the 4 ghosts can move to 4 different directions (I don't care if this move is invalid for the ghost, 
	  			 * I'm just gonna check if they lead to a possible Pacman node, so this will be invalid for Pacman
	  			 *We need the array to have 16 rows and 2 columns for the coordinates
	  			 *Therefore for the 1st ghost we will have the first 4 rows (0-->3)
	  			 *				  for the 2nd ghost we will have the next 4 rows (4-->7==4*1+0-->4*1+3)
	  			 *				  etc.
	  			 *As we see the values will be stored as the (4*d+g)th element in the array
	  			 *The coordinates given to the findTempPos function come from the currGhostPos Array.
	  			 */
	  			for (g=0; g<PacmanUtilities.numberOfGhosts; g++) {
	  				
	  					possGhost[4*d+g]=currPacNode.findTempPos(currGhost[g][0],currGhost[g][1], d);
	  					
	  			}
	  			
	  	}
	  	
	  	// Now that we' re done with initializations let's check the invalidity.
	  	//For each direction (outter loop):
		for (d=0; d<4; d++) { 
			
				//Wall: deep invalidity, affects all of the 3 columns of trafficLight and makes them false.
				if (Maze[pacmanX][pacmanY].walls[d]==0) {
				
						trafficLight[d][0]=false;
						trafficLight[d][1]=false;
						trafficLight[d][2]=false;
				
				}
				
				
				//If there isn't a wall we have to check the 2nd level, if there 's already a ghost in this direction
				//So we need a second loop for each ghost's position
				else {
					
						for (g=0; g<PacmanUtilities.numberOfGhosts; g++) {
								
								//the 2nd level makes the 1st 2 columns false and lets the 3rd true
								if((possPac[d][0]==currGhost[g][0]) && (possPac[d][1]==currGhost[g][1])) {
										
										trafficLight[d][0]=false;
										trafficLight[d][1]=false;
										
								}
								
								//If in this direction there isn't neither wall nor a ghost we check if it possibly a ghost will go
								//3rd level of invalidity makes false only the 1st column. (really soft nigga!)
								else if((possPac[d][0]==possGhost[g][0]) && (possPac[d][1]==possGhost[g][1])) {
										trafficLight[d][0]= false;
								}
						}
				}
		}
		
		
		/*After we calculated the trafficLights' values we need to fill the Arraylist with the valid moves.
		 * The Arraylist will hold the whole Node87828785 objects so as to have access both in the evaluation value
		 * and the nodeMove value.
		 * How do we determine if a move is valid?
		 * We check the 1st column to find a true element. This means that this node is of none level of invalidity.
		 * It's free, we can pass.
		 */
		for (d=0; d<4; d++) { 
			
				if (trafficLight[d][0]) {
					
						Nodeaki movedPacNode= new Nodeaki(pacmanX, pacmanY, d, Maze);
						validMoves.add(movedPacNode);
						
				}
			
		}
		
		
		//If this doesn't happen for none of our directions we need to reconsider and add a 3rd-level-invalidity-Node
		//to our moves ArrayList. Let's hope the ghosts are stupid.
		//If there is at least a free element all of the below procedure will be ignored.
		if (validMoves.isEmpty()) {
		
				for (d=0; d<4; d++) { 
					
						if (trafficLight[d][1]) {
							
								Nodeaki movedPacNode= new Nodeaki(pacmanX, pacmanY, d, Maze);
								validMoves.add(movedPacNode);
								
						}
						
				}
				
		}
		
		//If our Arraylist is still empty it means that our ghost is surrounded by walls and ghosts...
		//We reconsider even more and choose 2nd-level-invalidities as an option (there are at least 2)
		//Let's suicide boys...
		if (validMoves.isEmpty()) {
				for (d=0; d<4; d++) {
						if (trafficLight[d][2]) {
								Nodeaki movedPacNode= new Nodeaki(pacmanX, pacmanY, d, Maze);
								validMoves.add(movedPacNode);
						}
				}
		}
	 	
		// This is the second phase where we search in the validMoves ArrayList. Now follows a
		//common procedure for finding the max element of an arrayList based on each Node's evaluation
		//We keep the direction too, so as to be returned.
	  	maxNodeEval=validMoves.get(0).getNodeEvaluation();
	  	maxNodeDir=validMoves.get(0).getNodeMove();
	  	
	  	for ( d=1; d< validMoves.size(); d++) {
	  			
	  			if (maxNodeEval<validMoves.get(d).getNodeEvaluation()) {
	  				
	  					maxNodeEval=validMoves.get(d).getNodeEvaluation();
	  					maxNodeDir=validMoves.get(d).getNodeMove();
	  					
	  			}
	  			
	  	}
	  	
	  	
	  	//DADAAAAA!!!!
	  	moveToReturn= maxNodeDir;
	  	
	  	return moveToReturn;
  }

  public int[] calculateNextGhostPosition (Room[][] Maze, int[][] currentPos)
  {

    int[] moves = new int[4];
    int[] eval = new int[4];
    boolean[] ghostColision = new boolean[PacmanUtilities.numberOfGhosts];

    // System.out.println("Ghosts Current Positions ");
    for (int i = 0; i < PacmanUtilities.numberOfGhosts; i++) {
      // System.out.println("ghost x = "+currentPos[i][0]+" ghost y = "+ currentPos[i][1]);
    }
    // System.out.println(" ");

    int pacmanX = 0;
    int pacmanY = 0;

    for (int i = 0; i < PacmanUtilities.numberOfRows; i++)
      for (int j = 0; j < PacmanUtilities.numberOfColumns; j++) {
        if (Maze[i][j].isPacman()) {
          pacmanX = i;
          pacmanY = j;

        }
      }

    // pacmanX = 0;
    // pacmanY = 0;

    // System.out.println(pacmanX);
    // System.out.println(pacmanY);
    // System.out.println("");

    eval = newGhostDir(pacmanX, pacmanY, currentPos);
    ghostColision = checkCollision(eval, currentPos);

    for (int i = 0; i < PacmanUtilities.numberOfGhosts; i++) {
      while (!evaluateNewDirection(currentPos, i, eval, Maze) || ghostColision[i] || !checkFlag(currentPos, i, eval, Maze)) {
        eval[i] = (int) (4 * Math.random());
        ghostColision = checkCollision(eval, currentPos);
      }
    }

    moves = eval;

    // for (int i = 0; i < PacmanUtilities.numberOfGhosts; i++)
    // System.out.println("direction of ghost "+i+" = " +moves[i]);
    // System.out.println(" ");

    return moves;

  }

 
  public int[] newGhostDir (int pacX, int pacY, int[][] currentPos)
  {
    int[] tempDirections = new int[4];

    for (int k = 0; k < PacmanUtilities.numberOfGhosts; k++) {
      if (pacX < currentPos[k][0]) // pacman norther than ghost
      {
        tempDirections[k] = Room.NORTH;
      }

      if (pacX > currentPos[k][0]) // pacman souther than ghost
      {
        tempDirections[k] = Room.SOUTH;
      }

      if (pacY > currentPos[k][1]) // pacman easter than ghost
      {
        tempDirections[k] = Room.EAST;
      }

      if (pacY < currentPos[k][1]) // pacman wester than ghost
      {
        tempDirections[k] = Room.WEST;
      }
    }

    return tempDirections;
  }

  public boolean evaluateNewDirection (int[][] curPos, int i, int[] direction, Room[][] Maze)
  {
    boolean validChoice = true;
    if (Maze[curPos[i][0]][curPos[i][1]].walls[direction[i]] == 0)
      validChoice = false;

    return validChoice;

  }

  public boolean checkFlag (int[][] curPos, int i, int[] direction, Room[][] Maze)

  {
    boolean validChoice = true;

    if (direction[i] == Room.NORTH) {
      if (Maze[curPos[i][0] - 1][curPos[i][1]].isFlag()) {
        validChoice = false;
      }
    }

    if (direction[i] == Room.SOUTH) {
      if (Maze[curPos[i][0] + 1][curPos[i][1]].isFlag()) {
        validChoice = false;
      }
    }

    if (direction[i] == Room.EAST) {
      if (Maze[curPos[i][0]][curPos[i][1] + 1].isFlag()) {
        validChoice = false;
      }
    }

    if (direction[i] == Room.WEST) {
      if (Maze[curPos[i][0]][curPos[i][1] - 1].isFlag()) {
        validChoice = false;
      }
    }
    return validChoice;
  }

  public boolean[] checkCollision (int[] moves, int[][] currentPos)
  {
    boolean[] collision = new boolean[PacmanUtilities.numberOfGhosts];

    int[][] newPos = new int[4][2];

    for (int i = 0; i < PacmanUtilities.numberOfGhosts; i++) {

      if (moves[i] == 0) {
        newPos[i][0] = currentPos[i][0];
        newPos[i][1] = currentPos[i][1] - 1;
      } else if (moves[i] == 1) {
        newPos[i][0] = currentPos[i][0] + 1;
        newPos[i][1] = currentPos[i][1];
      } else if (moves[i] == 2) {
        newPos[i][0] = currentPos[i][0];
        newPos[i][1] = currentPos[i][1] + 1;
      } else {
        newPos[i][0] = currentPos[i][0] - 1;
        newPos[i][1] = currentPos[i][1];
      }

      collision[i] = false;
    }
   

    for (int k = 0; k < moves.length; k++) {
      // System.out.println("Ghost " + k + " new Position is (" + newPos[k][0] + "," + newPos[k][1] + ").");
    }

    for (int i = 0; i < PacmanUtilities.numberOfGhosts; i++) {
      for (int j = i + 1; j < PacmanUtilities.numberOfGhosts; j++) {
        if (newPos[i][0] == newPos[j][0] && newPos[i][1] == newPos[j][1]) {
          // System.out.println("Ghosts " + i + " and " + j + " are colliding");
          collision[j] = true;
        }

        if (newPos[i][0] == currentPos[j][0] && newPos[i][1] == currentPos[j][1] && newPos[j][0] == currentPos[i][0] && newPos[j][1] == currentPos[i][1]) {
          // System.out.println("Ghosts " + i + " and " + j + " are colliding");
          collision[j] = true;
        }

      }

    }
    return collision;
  }

  public int newPacmanDir (int pacX, int pacY, int[] currentPos)
  {
    int tempDirections = 5;

    if (pacX < currentPos[0]) // pacman norther than ghost
    {
      tempDirections = Room.NORTH;
    }

    if (pacX > currentPos[0]) // pacman souther than ghost
    {
      tempDirections = Room.SOUTH;
    }

    if (pacY > currentPos[1]) // pacman easter than ghost
    {
      tempDirections = Room.EAST;
    }

    if (pacY < currentPos[1]) // pacman wester than ghost
    {
      tempDirections = Room.WEST;
    }

    return tempDirections;
  }

}
