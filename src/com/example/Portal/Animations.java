package com.example.Portal;

import java.util.Arrays;

import android.graphics.Rect;

public class Animations {



	float y [] = {100,100,100,100,100,100};
	float x [] = {36,36,36,36,36,36};
	float originX[] = new float [x.length];
	float originY[] = new float [x.length];
	int count=0;
	float hypotInc = (float)2;
	double angle [] = {0.1,0.3,0.9,0.1,0.3,0.9};
	float returnX, returnY;
	public void setSparkShot () {

		for(int i = -1; ++i < x.length;)originX[i] = x[i];
		for(int i = -1; ++i < x.length;)originY[i] = y[i];

	}

	public void setSparkOrigin (float x1, float y1) {
		Arrays.fill(x, x1);
		Arrays.fill(y, y1);
	}
	
	public void laserWallSpark ( int i , String c) {

		if (c.equals("R")){
			y[i] += hypotInc*Math.cos(angle[i]);
			if (i < x.length/2)x[i] += hypotInc*Math.sin(angle[i]);
			else x[i] -= hypotInc*Math.sin(angle[i]);

		}
		else if (c.equals("L")){

			y[i] -= hypotInc*Math.cos(angle[i]);
			if (i < x.length/2)x[i] += hypotInc*Math.sin(angle[i]);
			else x[i] -= hypotInc*Math.sin(angle[i]);


		}
		else if (c.equals("D")){

			x[i] += hypotInc*Math.sin(angle[i]);
			if (i < x.length/2)y[i] += hypotInc*Math.cos(angle[i]);
			else y[i] -= hypotInc*Math.cos(angle[i]);


		}
		else if (c.equals("U")){

			x[i] -= hypotInc*Math.sin(angle[i]);
			if (i < x.length/2)y[i] += hypotInc*Math.cos(angle[i]);
			else y[i] -= hypotInc*Math.cos(angle[i]);


		}
		count++;

		if (count > 17){
			for(int k = -1; ++k < x.length;)  x[k] = originX[k];
			for(int k = -1; ++k < x.length;)  y[k] = originY[k];
			count = 0;
		}



	}
}
