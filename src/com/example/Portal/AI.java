package com.example.Portal;

import android.annotation.SuppressLint;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;



public class AI {
	//HashMap <Integer ,HashMap <Integer, Stack <Integer >>> iDirections = new HashMap <Integer ,HashMap <Integer, Stack <Integer >>> ();  // Stack was used to be able to reverse the movements, enabling walking forward/backward
	//HashMap <Integer ,HashMap <Integer, Stack <Integer >>>  jDirections = new HashMap <Integer ,HashMap <Integer, Stack <Integer >>> (); // Stack was used to be able to reverse the movements, enabling walking forward/backward
	HashMap<Integer, Stack<Integer>> iDirections [] = new  HashMap[10];
	HashMap<Integer, Stack<Integer>> jDirections [] = new  HashMap[10];
	boolean  testBullet [],firstTime [],active[];
	int bulletDistanceCount[];
	float bulletX [], bulletY[];
	double shootTheta[];
	float personPosX[], personPosY[];
	Utilities util;
	AI (int size, Utilities u){
		util = u;
		bulletX = new float [size];
		bulletY = new float [size];
		testBullet = new boolean [size];
		firstTime = new boolean [size];
		bulletDistanceCount = new int [size];
		shootTheta = new double [size];
		personPosX = new float [size];
		personPosY = new float [size];
		active = new boolean[size];

		Arrays.fill(testBullet, false);
		Arrays.fill(bulletX, 0);
		Arrays.fill(bulletY, 0);
		Arrays.fill(bulletDistanceCount, 0);
		Arrays.fill(firstTime, true);
		Arrays.fill(shootTheta, 0);
		Arrays.fill(active, false);

		
	}

	
	public void setupAI (int AInum){
		//iDirections.put(util.levelCount, new HashMap <Integer, Stack<Integer>>());
		//jDirections.put(util.levelCount, new HashMap <Integer, Stack<Integer>>());
		
		iDirections[util.levelCount] = new HashMap<Integer, Stack<Integer>>();
		jDirections[util.levelCount] = new HashMap<Integer, Stack<Integer>>();
		
		
		for (int i = -1; ++i < AInum;){
		//	iDirections.get(util.levelCount).put(i, new Stack());
		//	jDirections.get(util.levelCount).put(i, new Stack());
		
		    iDirections[util.levelCount].put(i, new Stack());
		    jDirections[util.levelCount].put(i, new Stack());
		}
	}

	public void initI(int level,int i, int fromEnd){

		//iDirections.get(level).get(i).add(fromEnd);
		
		iDirections[util.levelCount].get(i).add(fromEnd);

	}
	public void initJ(int level, int i, int fromEnd){

		//jDirections.get(level).get(i).add(fromEnd);
		
		jDirections[util.levelCount].get(i).add(fromEnd);

	}

	public float bulletX (int i, RenderGame ljs){
		float retBullet=0;



		int r = 2;



		if (firstTime[i]){
			bulletX[i] = ljs.enemies[i].exactCenterX();
			bulletY[i] = ljs.enemies[i].exactCenterY();
			personPosX[i] = ljs.you.centerX();
			personPosY[i] = ljs.you.centerY();
			
			
			shootTheta[i] = (float)Math.atan(Math.abs(Math.abs(bulletX[i]  - personPosX[i]) / Math.abs(bulletY[i]  - personPosY[i] )));
			firstTime[i] = false;
			
		}
		bulletDistanceCount[i] ++;

		if ( bulletDistanceCount[i] < 100){


			switch(getQuadrant(bulletX[i],bulletY[i],(int)personPosX[i], (int)personPosY[i])) {
			case 1:{
				bulletX[i] += r*Math.sin(shootTheta[i]);
				break;

			}
			case 2:{
				bulletX[i] += r*Math.sin(shootTheta[i]);
				break;
			}
			case 3:{
				bulletX[i] -= r*Math.sin(shootTheta[i]);
				break;
			}
			case 4:{
				bulletX[i] -= r*Math.sin(shootTheta[i]);
				break;
			}
			}




			// should put wall logic here
		}
		else if (bulletDistanceCount[i] >= 100){
			testBullet[i] = false;
			bulletDistanceCount[i] = 0;
			firstTime[i] = true;
			active[i] = false;
		}


		retBullet = bulletX[i];
		return retBullet;
	}

	public float bulletY (int i, RenderGame ljs){
		float retBullet=0;
		int r = 2;
		

		if ( bulletDistanceCount[i] < 100){


			switch(getQuadrant(bulletX[i],bulletY[i],(int)personPosX[i], (int)personPosY[i])) {
			case 1:{
				bulletY[i] += r*Math.cos(shootTheta[i]);
				break;

			}
			case 2:{
				bulletY[i] -= r*Math.cos(shootTheta[i]);
				break;
			}
			case 3:{
				bulletY[i] -= r*Math.cos(shootTheta[i]);
				break;
			}
			case 4:{
				bulletY[i] += r*Math.cos(shootTheta[i]);
				break;
			}
			}




			// should put wall logic here
		}
		


		retBullet = bulletY[i];

		return retBullet;
	}


	public int getQuadrant (float originX, float originY, float subjectX, float subjectY){
		int Q=0;
		if (subjectY-originY > 0 && subjectX - originX > 0)Q = 1;
		else if (subjectY-originY < 0 && subjectX - originX > 0)Q = 2	;
		else if (subjectY-originY < 0 && subjectX - originX < 0)Q = 3;
		else if (subjectY-originY > 0 && subjectX - originX < 0)Q = 4;
		return Q;
	}



}
