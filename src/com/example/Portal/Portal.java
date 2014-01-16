package com.example.Portal;

import java.util.HashMap;
import java.util.Stack;

import android.graphics.Rect;

public class Portal {
	String grid [][][];
	int x1,x2, y1,y2;
	 int I,J;
	int portalCount=0;
	Stack <Integer> portalLocatorI = new Stack <Integer>();
	Stack <Integer> portalLocatorJ = new Stack <Integer>();
	int levelCount=0;
	Utilities ls;
	private int portalFinder;
	private boolean bool;
	
	public void levelSet(){ levelCount = ls.levelCount;}
	
	Portal (Wall w, Utilities les) {
		ls = les;
		grid = w.grid;
		levelSet();
		
	}
	String checkWall(int i, int j){
		return (grid[levelCount][i][j]);
	}

	boolean checkWall(Rect a,Rect b [][]){
		bool = false;

		nest : for (int i = -1; ++i < 22;){
			for (int j = -1; ++j < 14;){
				if (grid[levelCount][i][j].equals("X")){
					if (b[i][j].contains(a)){

						if(portalCount<2){
							
							bool = true;
							identifyWall(b,i,j);
							grid[levelCount][i][j] = "J";
							portalCount++;
							findPortal();
							break nest;
							
						}
						else {
							portalCount=0;
							for( int k = -1; ++k < 2;){
								grid[levelCount][portalLocatorI.pop()][portalLocatorJ.pop()]="X"; // reset to a normal wall because you shot two already
							}
							portalLocatorI.clear();
							portalLocatorJ.clear();
							x1=x2=y1=y2=0;
						}
					}}
			}}
		return bool;
	}

	void identifyWall (Rect b[][],int i, int j){
		portalLocatorI.push(i);
		portalLocatorJ.push(j);
	}
	
	
	void findPortal(){
		portalFinder = 0;
		nest : for (int i = -1; ++i < 22;){
			for (int j = -1; ++j < 14;){
				if(portalFinder == 0){
				if (grid[levelCount][i][j].equals("J")){portalFinder++;x1 = (int) i;y1=(int) j;}
			}
				else if (portalFinder == 1){
					if (grid[levelCount][i][j].equals("J")){portalFinder++;x2 = (int) i;y2=(int) j;}	
				}
			}}
		
	}
	
	
	void adjacentValue (int i , int j){ // bounce off wall logicY
		try{
			if (!grid[levelCount][i][j+1].equals("X") && !grid[levelCount][i][j+1].equals("J") ){I=i;J=j+1;}} //try/catch are used for j+1/i+1 > 22,14 respectively
		catch(ArrayIndexOutOfBoundsException e){}
		try{
			if (!grid[levelCount][i][j-1].equals("X") && !grid[levelCount][i][j-1].equals("J")){I=i;J=j-1;}}
		catch(ArrayIndexOutOfBoundsException e){}
		try{
			if (!grid[levelCount][i+1][j].equals("X") && !grid[levelCount][i+1][j].equals("J")){I=i+1;J=j;}}
		catch(ArrayIndexOutOfBoundsException e){}
		try{
			if (!grid[levelCount][i-1][j].equals("X") && !grid[levelCount][i-1][j].equals("J")){I=i-1;J=j;}}
		catch(ArrayIndexOutOfBoundsException e){}
	}
}
