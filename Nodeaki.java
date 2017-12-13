package gr.auth.ee.dsproject.node1;
import gr.auth.ee.dsproject.pacman.*;
import java.util.ArrayList;
import java.util.HashMap;




public class Nodeaki{
	
  int nodeX;
  int nodeY;
  int nodeMove;
  double nodeEvaluation;
  int[][] currentGhostPos;
  int[][] flagPos;
 



boolean[] currentFlagStatus;
  
	// Constructor1
  	public Nodeaki (Room[][] Maze){
  		
  		nodeX=0;
	  	nodeY=0;
	  	nodeMove= -1;
	  	nodeEvaluation= 0;
	  	currentGhostPos=findGhosts(Maze);
	  	flagPos=findFlags(Maze);
	  	currentFlagStatus=checkFlags(Maze);
	  	
  	}
  	
  	// Constructor2
  	public Nodeaki (int x, int y, Room[][] Maze){
  		
  		nodeX=x;
	  	nodeY=y;
	  	nodeMove=-1;
	  	nodeEvaluation= 0;
	  	currentGhostPos=findGhosts(Maze);
	  	flagPos=findFlags(Maze);
	  	currentFlagStatus=checkFlags(Maze);
	  	
	 }
  	
  	// Constructor3
  	public Nodeaki (int x, int y, int k, Room[][] Maze) {

	  	nodeX=x;
	  	nodeY=y;
	  	nodeMove=k;
		currentGhostPos=findGhosts(Maze);
	  	flagPos=findFlags(Maze);
	  	currentFlagStatus=checkFlags(Maze);
	  	nodeEvaluation= evaluation();
  }
  	
  	
  	//The variables of the class that I will need to have access to. Generate some getters.
  	
  	//A getter for nodeMove (to return the direction of the best node to go)
  	public int getNodeMove() {
  		
  			return nodeMove;
  			
	}

	//A getter for nodeEvaluation (to compare the valid moves and find the best based on that)
	public double getNodeEvaluation() {
		
			return nodeEvaluation;
			
	}
	
	//A getter for the current position of ghosts.
  	public int[][] getCurrentGhostPos() {
  		
  			return currentGhostPos;
  			
  	}
  	 
  	//returns the position of each ghost with a 2-Dimensional array
	private int[][] findGhosts (Room[][] Maze){
		
		
			//Declarations
			
			//r stands for rows
			//c stands for columns
			//ctr stands for ghos
			int r,c, ctr=0;
			//We allocate memory to store the Postition of each ghost (4x2)
			int [][] curGhostPos= new int [PacmanUtilities.numberOfGhosts][2];
				
			// We scan the maze to find the ghosts.
			// If the counter becomes equal to the numberOfGhosts we don't need to search anymore
			// so we exit the for loops with a break;
			for ( r=0; r < PacmanUtilities.numberOfRows; r++) {
				
					for (c=0; c < PacmanUtilities.numberOfColumns; c++) {
						
							if (Maze[r][c].isGhost()) {
									
									curGhostPos[ctr][0]=r;
									curGhostPos[ctr][1]=c;
									
									ctr++;
									
							}
							
							if (ctr== PacmanUtilities.numberOfGhosts) {
									break;
							}
							
					}

					if (ctr== PacmanUtilities.numberOfGhosts) {
							
							break;
					}
					
			}
			
			return curGhostPos;  
			
  }
	
	
	private int[][] findFlags (Room[][] Maze){
		
			int [][] curFlagPos= new int [PacmanUtilities.numberOfFlags][2];
			int r,c, f=0;
			
			
			for ( r=0; r < PacmanUtilities.numberOfRows; r++) {
				
					for (c=0; c < PacmanUtilities.numberOfColumns; c++) {
								
									if (Maze[r][c].isFlag()) {
										
											curFlagPos[f][0] = r;
											curFlagPos[f][1] = c;
											f++;
											
									}
									
									if (f== PacmanUtilities.numberOfFlags) {break;}
									
							}
							
					if (f== PacmanUtilities.numberOfFlags) {break;}
							
					} 
			// PRINTS GOOD
			//			for (f=0; f<PacmanUtilities.numberOfFlags; f++) {
			//				System.out.print("Flag_"+f+" "+ curFlagPos[f][0]+" "+curFlagPos[f][1]);
			//			}
					return curFlagPos;  
					
  }
	
	private boolean[] checkFlags (Room[][] Maze){
			boolean[] flagStatus= new boolean[PacmanUtilities.numberOfFlags];
			int f;
			
			for (f=0; f<PacmanUtilities.numberOfFlags; f++) {
				
					flagStatus[f]= Maze[flagPos[f][0]][flagPos[f][1]].isCapturedFlag();
					
			}
			
			return flagStatus;
			
    }

	//Ma baby
	private double evaluation (){  
				
				double w1=1, w2=1, w3=1, w4=50, w5=0, evaluation = 0;
				int g,f, stepsAhead=0, takeableFlags=0;
				int[] nearestGhostToFlag=new int[PacmanUtilities.numberOfFlags];
				int[][] stepsGhostToFlag= new int[PacmanUtilities.numberOfGhosts][PacmanUtilities.numberOfFlags];
			
	
				
				int centerized=findDistToEdge(); //POSITIVE

	  			ArrayList<Integer> ghostsOfInterest= new ArrayList<Integer>(PacmanUtilities.numberOfGhosts);
		
	  			for (g=0; g<PacmanUtilities.numberOfGhosts; g++)  {
	  				
	  					if (nodeMove%2==1){
	  						
	  							if (relativePos(currentGhostPos, PacmanUtilities.numberOfGhosts)[g][0]==nodeMove) {
	  								
	  									ghostsOfInterest.add(taxicabDistance(nodeX, nodeY, currentGhostPos[g][0], currentGhostPos[g][1] ));
	  									
	  							}
	  							
	  					}
	  					
	  					else {
	  						
	  							if (relativePos(currentGhostPos, PacmanUtilities.numberOfGhosts)[g][1]==nodeMove) {
	  								
	  									ghostsOfInterest.add(taxicabDistance(nodeX, nodeY, currentGhostPos[g][0], currentGhostPos[g][1] ));
	  									
	  							}
	  							
	  					}
	  					
	  			}
				
				
	  			HashMap <Integer, Integer > flagsOfInterest= new HashMap<Integer, Integer>(PacmanUtilities.numberOfFlags);
	  			
	  			for (f=0; f<PacmanUtilities.numberOfFlags; f++) {
	  				
	  					if (!currentFlagStatus[f]) {
	  						
	  							if (nodeMove%2==1){
	  								
	  									if (relativePos(flagPos, PacmanUtilities.numberOfFlags)[f][0]==nodeMove) {
	  										
	  											flagsOfInterest.put(f, taxicabDistance(nodeX, nodeY, flagPos[f][0], flagPos[f][1] ));
	  											
	  									}
	  									
	  							}
	  							
	  							else {
	  								
	  									if (relativePos(flagPos, PacmanUtilities.numberOfFlags)[f][1]==nodeMove) {
	  										
	  											flagsOfInterest.put(f, taxicabDistance(nodeX, nodeY, flagPos[f][0], flagPos[f][1] ));
	  											
	  									}
	  									
	  							}
	  							
	  					}
	  				
	  			}
				
				int ghostsInThisDirection, flagsInThisDirection;
				ghostsInThisDirection=ghostsOfInterest.size();
				flagsInThisDirection=flagsOfInterest.size();
				
				/*
				int nearestGhostToPacman=0, nearestFlagToPacman=0;
				
				
				if (!ghostsOfInterest.isEmpty()) {
					nearestGhostToPacman=ghostsOfInterest.get(0);
					for (g=0; g<ghostsOfInterest.size(); g++) {
						if (nearestGhostToPacman> ghostsOfInterest.get(g)) {
							nearestGhostToPacman= ghostsOfInterest.get(g);
						}
					}
				}
				
				if (!flagsOfInterest.isEmpty()) {
					nearestFlagToPacman=flagsOfInterest.get(0);
					for (f=0; f<flagsOfInterest.size(); f++) {
						if (nearestFlagToPacman > flagsOfInterest.get(f)) {
							nearestFlagToPacman= flagsOfInterest.get(f);
						}
					}
				}
					*/
					
				//Distance each Flag to each Ghost (2-D)
				for ( g=0; g < PacmanUtilities.numberOfGhosts; g++) {	
					
						for ( f=0; f < PacmanUtilities.numberOfFlags; f++) {
								
							if (currentFlagStatus[f]) {
								stepsGhostToFlag[g][f]=-1;
							}
							else {
								stepsGhostToFlag[g][f]= taxicabDistance(flagPos[f][0], flagPos[f][1], currentGhostPos[g][0], currentGhostPos[g][1]);
							}
								
								
						}
						
				}

				//how many flags can he take in direction k? AND how far do i have ghosts?
				
				if (!flagsOfInterest.isEmpty()) {
					
						for (f=0; f < PacmanUtilities.numberOfFlags; f++) {
							
								nearestGhostToFlag[f]=-1; //DEN KSERW AN XREIAZETAI PREPEI KLP
								
								if (flagsOfInterest.containsKey(f)) {
									
									nearestGhostToFlag[f]=stepsGhostToFlag[0][f];
										
										for(g=0; g<PacmanUtilities.numberOfGhosts; g++) {
											
												if (stepsGhostToFlag[g][f]<nearestGhostToFlag[f]) {
													
													nearestGhostToFlag[f]=stepsGhostToFlag[g][f];
														
	  	    									}
												
	  	    							}
										
	  	    							if(nearestGhostToFlag[f]>flagsOfInterest.get(f)) {
	  	    								
	  	    											//POSITIVE
	  	    											takeableFlags++;
	  	    											
	  	    											//POSITIVE
	  	    											stepsAhead+= nearestGhostToFlag[f]-flagsOfInterest.get(f);
	  	    											
	  	    							}	
	  	    							
	  	    					}

						}
						
				}

				evaluation= 30*takeableFlags+10*stepsAhead+15*flagsInThisDirection-15*ghostsInThisDirection+10*centerized;
    
				return evaluation;

  }  
  
	// METHODS USED
	//Pacman Position in Direction nodeMove 
	
public int[] findTempPos(int x, int y, int k) {
		
			int[] nextMoveK= new int[2];	
			nextMoveK[0]=x;
			nextMoveK[1]=y;
			
			switch(k) {
			
					case 0: nextMoveK[1]--;
					
								break;
								
					case 1: nextMoveK[0]++;
					
								break;
								
					case 2:nextMoveK[1]++;
					
								break;
								
					case 3: nextMoveK[0]--;
					
								break;		
								
			}
			
			return nextMoveK;
			
    }

  //Distance to Wall (doesn't need abs)
	public int findDistToEdge() {
			
	  		int xToWall, yToWall;
	  		
	  		
	  		// theloume kai to ison giati nodeX, nodeY ksekinaei apo 0
	  		if (nodeX >= (int) PacmanUtilities.numberOfRows/2) {
	  			
	  				// den xreiazetai na kalesoume tin dist giati den xreiazomaste to apolyto
	  				xToWall =PacmanUtilities.numberOfRows -1- nodeX;
	  				
	  		}
	  		
	  		else {
	  			
	  				xToWall=nodeX;
	  				
	  		}
	  		
	  		if (nodeY >= (int) PacmanUtilities.numberOfColumns/2) {
	  			
	  				yToWall=PacmanUtilities.numberOfColumns -1- nodeY; // ksekinaei ap to 0 to nodeY
	  				
	  		}
	  		
	  		else {
	  			
	  				yToWall=nodeY;
	  				
	  		}
	  		//System.out.println("XDIST="+xToWall+"YDIST"+yToWall);
	  		/*den me noiazei toso i apostasi apo toixo oso i apostasi apo gwnia
	  		if	(xToWall<yToWall) {
	  			
	  				minDist=xToWall;
	  			
	  		}
	  		
	  		else {
	  			
	  				minDist=yToWall;
	  			
	  		}*/
	  		//System.out.println(minDist);
	  		return xToWall+yToWall;
	  		
	}
	
	
  //Math.abs(a-b)
	public int distance(int a,int b) {
		
			return Math.abs(a-b);
			
	}

	public int taxicabDistance(int x1, int y1, int x2, int y2) {
		
			return distance(x1, x2)+distance(y1,y2);
			
	}
	
	public int[][] relativePos(int[][] pos, int size){
	  
	  		int i;
	  		int[][] relPos= new int[size][2];
	  		
	  		for (i=0; i<size; i++) {
	  			
	  				if (pos[i][0]<nodeX) {
	  					//where is pos respective to Pacman (pos is norther than pacman)
	  						relPos[i][0]=Room.NORTH;
	  						
	  				}else if(pos[i][0]==nodeX){
	  					
	  					relPos[i][0]=-1;
	  						
	  				}else {
	  					relPos[i][0]=Room.SOUTH;
	  				}
	  				
	  				if (pos[i][1]<nodeY){
	  					
	  						relPos[i][1]=Room.WEST;
	  						
	  				}else if(pos[i][1]==nodeY){
	  						
	  						relPos[i][1]=-1;
	  						
	  				}else {
	  					
	  						relPos[i][1]=Room.EAST;
	  						
	  				}
	  				
	  		}
	  		
	  		return relPos;
	  		
}


}//end of class










