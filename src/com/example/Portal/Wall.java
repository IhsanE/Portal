package com.example.Portal;



import java.util.Arrays;

import android.graphics.Rect;

/*
   String x [][] = {

  {"V","X","V"},
  {"V","X","V"}
  };

  for (int i  = -1;++i<2;){
  for (int j  = -1;++j<3;){
  System.out.println(x[i][j]);
  }
  }
 */

public class Wall {
	double x,y,x1,y1;
	float retX, retY;
	int currentLevel;
	Utilities ls;
	public void levelSet() {currentLevel = ls.levelCount;}
	int walkingPositionsi [] = new int [(22*14)];
	int walkingPositionsj [] = new int [(22*14)];
	int walkingCount=-1;
	int wallPositionsi [] = new int [(22*14)];
	int wallPositionsj [] = new int [(22*14)];
	int wallCount = -1;
	int portalPositionsi [] = new int [(22*14)];
	int portalPositionsj [] = new int [(22*14)];
	int portalCount = -1;

	String 
	grid[][][]={

			//LEVEL ONE


			{{"X","X","X","X","X","X","X","X","X","X","X","X","X","X"},
				{"X","","","","","","X","X","","","","","","X"},
				{"X","","","","","","X","X","X","","","","","X"},
				{"X","","","","","","X","X","X","","","","","X"},
				{"X","","","","","","X","X","X","","","","","X"},
				{"X","","","","","","X","X","","","","","","X"},
				{"X","","","","","","X","X","","","","","","X"},
				{"X","","","X","X","X","X","X","X","X","X","","","X"},
				{"X","","","","","","X","X","","","","","","X"},
				{"X","X","X","X","","","X","X","","","X","X","X","X"},
				{"X","","","","","","X","X","X","","","","","X"},
				{"X","","","X","X","X","X","X","X","X","X","","","X"},
				{"X","","","","","","X","X","X","","","","","X"},
				{"X","","X","X","X","X","X","X","X","X","X","X","","X"},
				{"X","","X","X","X","X","X","X","X","X","X","X","","X"},
				{"X","","X","X","X","X","X","X","X","X","X","X","","X"},
				{"X","","X","X","X","X","X","X","","","","","","X"},
				{"X","","","","","","X","X","","","","","","X"},
				{"X","","X","X","X","X","X","X","","","","","","X"},
				{"X","","","","","","X","X","","","","","","X"},
				{"X","","","","","","","","","","","","","X"},
				{"X","X","X","X","X","X","X","X","X","X","X","X","X","X"}},


				//LEVEL TWO


				{{"X","X","X","X","X","X","X","X","X","X","X","X","X","X"},
					{"X","","","X","","","","","X","X","","","","X"},
					{"X","","","","","","","","X","X","X","","","X"},
					{"X","X","X","","","","","","X","X","X","","","X"},
					{"X","","","","","","","X","X","X","","","","X"},
					{"X","","","","","","","X","X","X","","","","X"},
					{"X","","","","","","","X","X","X","","","","X"},
					{"X","","","X","X","X","","X","X","X","","","","X"},
					{"X","","","X","","","","X","X","X","","","","X"},
					{"X","","","X","","","","X","X","X","","","","X"},
					{"X","","","X","X","X","X","X","X","X","","","","X"},
					{"X","","","","","","","X","X","X","","","","X"},
					{"X","","","","","","","X","X","X","","","","X"},
					{"X","X","","X","X","","","X","X","X","","","","X"},
					{"X","","","","","","","X","X","X","","","","X"},
					{"X","","","","","","","X","X","X","","","","X"},
					{"X","","","","","","","X","X","X","","","","X"},
					{"X","","","","","","","X","X","X","","","","X"},
					{"X","","","","","","","X","X","X","","","","X"},
					{"X","","","","","","","X","X","X","","","","X"},
					{"X","","","","","","","","","","","","","X"},
					{"X","X","X","X","X","X","X","X","X","X","X","X","X","X"}},


					//LEVEL THREE


					{{"X","X","X","X","X","X","X","X","X","X","X","X","X","X"},
						{"X","","","","","","","","","","X","","","X"},
						{"X","","X","X","X","","","","","","X","X","","X"},
						{"X","","","","","","","","","","","","","X"},
						{"X","","","","","","","","","","","","","X"},
						{"X","","X","X","X","X","X","X","X","X","X","","","X"},
						{"X","","X","X","X","X","X","X","X","X","X","","","X"},
						{"X","","X","X","X","X","X","X","X","X","X","","","X"},
						{"X","","","","","","","","","","X","X","X","X"},
						{"X","","","","","","","","","","","","","X"},
						{"X","","","X","X","X","X","","","","","","","X"},
						{"X","","","X","X","X","X","","","","","","","X"},
						{"X","","","X","X","X","X","","","","","","","X"},
						{"X","","","X","X","X","X","","","","","","","X"},
						{"X","","","X","X","X","X","","","","","","","X"},
						{"X","","","X","X","X","X","","","","","","","X"},
						{"X","","","X","X","X","X","","","","","","","X"},
						{"X","","","","","","","","","","","","","X"},
						{"X","","","","","","","","","","","","","X"},
						{"X","","","","","","","","","","","","","X"},
						{"X","","","","","","","","","","","","","X"},
						{"X","X","X","X","X","X","X","X","X","X","X","X","X","X"}},


						//LEVEL FOUR


						{{"X","X","X","X","X","X","X","X","X","X","X","X","X","X"},
							{"X","","","","","","","","","","","","","X"},
							{"X","X","X","X","X","X","X","X","X","X","X","X","","X"},
							{"X","","","","","","","","","","","X","","X"},
							{"X","","","","","","","","","","","","","X"},
							{"X","","","","","","","","","","","X","X","X"},
							{"X","","","","","","","","","","","X","X","X"},
							{"X","","X","X","X","X","X","X","X","X","X","X","","X"},
							{"X","","","","","","X","X","","","","","","X"},
							{"X","X","X","X","","X","X","X","","","X","X","X","X"},
							{"X","","","","","X","X","X","","","","","","X"},
							{"X","","","X","X","X","X","X","X","X","X","","","X"},
							{"X","","","","","","X","X","","","","","","X"},
							{"X","","X","X","","X","X","X","","","","","","X"},
							{"X","","X","X","","X","X","X","","","","","","X"},
							{"X","","","","","X","X","X","X","X","X","X","","X"},
							{"X","","X","X","X","X","","X","","","","","","X"},
							{"X","","","","","","","","X","","","","","X"},
							{"X","","X","X","X","X","","X","","","","","","X"},
							{"X","","","","","X","","X","","","","","","X"},
							{"X","","","","","","","","","","","","","X"},
							{"X","X","X","X","X","X","X","X","X","X","X","X","X","X"}}   









	}; // END OF MAPS
	private float retX1;
	private float retX2;
	private float rety1;
	private float rety2;





	Wall(int height, int width, Utilities les){
		x1 = width;
		y1 = height;
		ls = les;

		x=x1/14;
		y=y1/22;
		levelSet();	
		Arrays.fill(walkingPositionsi, 999);
		Arrays.fill(wallPositionsi, 999);
		Arrays.fill(portalPositionsi, 999);
	}


	boolean calcBoundary (float charXpos, float charYpos){
		boolean bool = false;
		//i = row
		//j  = column
		nest : for (int i = -1; ++i < 16;){
			for (int j = -1; ++j < 14;){
				if (grid[currentLevel][i][j].equals("X")){
					if(charXpos > ((x*(j+1))-x) && charXpos < (x*(j+1)) && charYpos > ((y*(i+1))-y)  && charYpos < ((y*(i+1)))){bool = true;break nest;}
				}
			}
		}


		return bool;
	}

	float x1Draw (int i, int j){
		retX1 = 0;

		if (grid[currentLevel][i][j].equals("X"))retX1 = (float) (x*(j+1)-x);
		return  retX1;
	}
	float x2Draw (int i, int j){
		retX2 = 0;

		if (grid[currentLevel][i][j].equals("X"))retX2 = (float) (x*(j+1));
		return  retX2;
	}
	float y1Draw (int i, int j){
		rety1 = 0;

		if (grid[currentLevel][i][j].equals("X"))rety1 = (float) (y*(j+1)-y);
		return  rety1;
	}
	float y2Draw (int i, int j){
		rety2 = 0;

		if (grid[currentLevel][i][j].equals("X"))rety2 = (float) (y*(j+1));
		return  rety2;
	}


	Rect[][] gridSet (Rect grid[][]){
		for (int i = -1; ++i < 22;){
			for (int j = -1; ++j < 14;){
				grid[i][j] = new Rect((int)((x*(j+1))-x),(int)((y*(i+1))-y),(int)((x*(j+1))),(int)((y*(i+1))));
			}}
		return grid;
	}

	String checkWall(int i, int j){
		return (grid[currentLevel][i][j]);
	}

	boolean checkWall(Rect a,Rect b [][]){
		boolean bool = false;

		nest : for (int i = -1; ++i < 22;){
			for (int j = -1; ++j < 14;){
				if (grid[currentLevel][i][j].equals("X")){
					if (Rect.intersects(a, b[i][j])){bool = true;adjacentValue(a,b,i,j);break nest;}}
				else if (jCount() == 1 && grid[currentLevel][i][j].equals("J"))if (Rect.intersects(a, b[i][j])){bool = true;adjacentValue(a,b,i,j);break nest;}

			}}
		return bool;
	}

	void adjacentValue (Rect a, Rect b[][],int i , int j){ // bounce off wall logicY
		try{
			if (Rect.intersects(a, b[i][j+1]) && !grid[currentLevel][i][j+1].equals("X")){retX = b[i][j+1].centerX();retY = b[i][j+1].centerY();} //try/catch are used for j+1/i+1 > 22,14 respectively
		}
		catch(ArrayIndexOutOfBoundsException e){}
		try{
			if (Rect.intersects(a, b[i][j-1])&& !grid[currentLevel][i][j-1].equals("X")){retX= b[i][j-1].centerX();retY= b[i][j-1].centerY();}}
		catch(ArrayIndexOutOfBoundsException e){}
		try{
			if (Rect.intersects(a, b[i+1][j])&& !grid[currentLevel][i+1][j].equals("X")){retX= b[i+1][j].centerX();retY= b[i+1][j].centerY();}}
		catch(ArrayIndexOutOfBoundsException e){}
		try{
			if (Rect.intersects(a, b[i-1][j])&& !grid[currentLevel][i-1][j].equals("X")){retX= b[i-1][j].centerX();retY= b[i-1][j].centerY();}
		}
		catch(ArrayIndexOutOfBoundsException e){}
	}

	int jCount () {
		int jCount = 0;
		for (int i = -1; ++i < 22;){
			for (int j = -1; ++j < 14;){
				jCount += grid[currentLevel][i][j].equals("J") ? 1 : 0;
			}}
		return jCount;
	}

	public void setWallComponents (Portal portal){
		for (int i = -1; ++i < 22;){						//drawing the environment
			for (int j = -1; ++j < 14;){
				if((checkWall(i,j)).equals("")){
					walkingPositionsi[++walkingCount]=i;
					walkingPositionsj[walkingCount]=j;
				}
				else if((checkWall(i,j)).equals("X")){
					wallPositionsi[++wallCount]=i;
					wallPositionsj[wallCount]=j;
				}
				
			}}
	}
	
}
