
package com.example.Portal;

public class RenderPerson {
	double r=2;
	float a,b,c;
	private float y2;
	private float x2;
	private double cos;
	private double retVal;
	private double sin;
	private double retVal2;
	RenderPerson(float leftX, float leftY, float rightY,int p){ // Sets all of the positions for the two analog sticks.
		r=p;
	
	 a = leftX;//246
	 b = leftY;//350
	 c = rightY;//75}
	}
	 RenderPerson(float leftX, float leftY, float rightY){ // This was for the right joy stick, because it doesn't need a radius to be sent to draw anything.
		 a = leftX;//246
		 b = leftY;//350
		 c = rightY;//75
	}
	public double xPos (double currentPos,double theta, int Q) { // Returns the x position that the character (you) should be moved to.
		
		retVal2 = currentPos;
		sin = Math.sin(theta);
		sin = sin == Double.NaN ? 1:sin; // If sin is NaN set it to 1
		switch (Q){                      // Based on the quadrant, you either go up or down
		case 1: {
			retVal2 -= r*sin;         
			break;
		}
		case 2:{
			retVal2 -= r*sin;
			break;
		}
		case 3:{
			retVal2 += r*sin;	
			break;
		}
		case 4:{
			retVal2 += r*sin;
			break;
		}
		}
		return retVal2;
	}
	public double yPos (double currentPos,double theta, int Q) { // Returns the y position that the character (you) should be moved to.
	
		retVal = currentPos;
		cos = Math.cos(theta);
		cos = cos == Double.NaN ? 1 : cos; 
		switch (Q){ // Based on the quadrant, you either left or right
		case 1: {
			retVal -= r*cos;
			break;
		}
		case 2:{
			retVal += r*cos;
			break;
		}
		case 3:{
			retVal += r*cos;	
			break;
		}
		case 4:{
			retVal -= r*cos;
			break;
		}
		}
		return retVal;
	}
	public int getQuadrant(float x, float y, int stick){// c and b denote joystick Y value distances (c away from origin, b away from origin)
														// Finds the quadrant of the x/y values, this combined with the angle gives a perfect direction.
		x2 = x;   
		y2 = y;
		int quadrant = 0;
		if (stick == 2){ // Right Joy Stick
			if (y2 < c & x2 < a)//Q1
			{
				quadrant = 1;
			}
			else if (y2 > c & x2 < a)//Q2
			{
				quadrant = 2;
			}
			else if (y2 > c & x2 > a)//Q3
			{
				quadrant = 3;
			}
			else if (y2 < c & x2 > a)//Q4
			{
				quadrant = 4;
			}
		}
		else if (stick == 1){ // Left Joy Stick
			if (y2 < b & x2 < a)//Q1
			{
				quadrant = 1;
			}
			else if (y2 > b & x2 < a)//Q2
			{
				quadrant = 2;
			}
			else if (y2 > b & x2 > a)//Q3
			{
				quadrant = 3;
			}
			else if (y2 < b & x2 > a)//Q4
			{
				quadrant = 4;
			}
		}
		return quadrant;
	}
}
