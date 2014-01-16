
/*TODO:
 * 
 * - Fix "Float" "Float" nonsense later![x]
 * - Figure out how to make the thing move even when the analog's at a standstill outside of the centre point[x]
 * - Create Queue to store last angle that's non 0, so the "gun" stays in the last position[x]
 * - Enable Shooting [x]
 * - Change Up Functions with the >/< 250 to something narrower, because it hanges if u go to the right and let go
 *   in the right joy sticks "area". Thinks it's the right joy stick, so it doesn't set the LJS back to centre.
 * - Fix multitouch crashing (4 fingers + )
 * - Add lasers
 * */


package com.example.Portal;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;

import com.example.portal_v1.R;

import android.R.color;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class RenderGame extends View {
	private Utilities ls; // Used to figure out the level count
	private Paint paint = new Paint();
	private double r;
	private float leftX,leftY;
	private float rightX, rightY ;
	private double x,y;
	private double x1,y1;
	private double RenderX, RenderY;
	private float AngleX, AngleY,endX, endY;
	private double theta;
	private int action;
	private float xpos,ypos;
	private float joyStickRadius;
	public RenderPerson RP;
	public RenderPerson rightJoyStick;
	private static int InvalidPointerID;
	private static int ActivePointerID, SecondActivePointerID;
	private float tempTheta, tempRightX, tempRightY; // Used to make gun ALWAYS appear even when RightAnalogStick is inactive
	private boolean testBullet, firstTime ;
	private float bulletDistanceCount, bulletX, bulletY, shootTheta;
	private int screenHeight, screenWidth;
	private float ratioY,  ratioX, ratioXR ; 
	private Drawable homeScreen, Instructions;
	private float oldX, oldY;
	private static float MIN_ZOOM ; //Starts out n times zoomed in.
	private static float MAX_ZOOM ;
	private float scaleFactor;
	private ScaleGestureDetector detector;
	private static int NONE;
	private static int DRAG;
	private static int ZOOM;
	private int mode;
	private float startX;
	private float startY;
	private float translateX;
	private float translateY;
	private float previousTranslateX ;
	private float previousTranslateY ; 
	private boolean dragged;
	private float displayWidth;
	private float displayHeight;
	private Wall wall;
	private Portal portal;
	Rect currentMap [][] ;
	Rect you;
	private AI ai;
	int xx [] ;
	int yy [] ;
	int counter [] ;
	Rect enemies []  ;
	int enemyiGoal [];
	int enemyjGoal [];
	int enemyiStart [] ;
	int enemyjStart [] ;
	LinkedList <Integer> iDirection [];
	LinkedList<Integer> jDirection [];
	int size [] ;
	Rect bullet = new Rect();
	Rect enemyBullet [];
	boolean death,aiDeath,levelComplete;
	Rect stageWinPortal = new Rect();
	private int pointerIndex;
	private double distance;
	Path mPath = new Path();
	Paint    _paintSimple = new Paint();
	Paint    _paintBlur = new Paint();
	float shootx, shooty;
	int frameCount=0;
	Animations animation = new Animations ();
	Rect laserBeam = new Rect();
	int walkCount, wallCount, portalCount;
	private boolean boo, instructions;

	RenderGame(Context context, int height, int width, Utilities instanceofLS){
		super(context);	
		ls = instanceofLS;
		displayWidth = width;
		displayHeight = height;
		screenWidth = height;//480
		screenHeight = width;//320

		ratioY = (float) (350.0/480.0);  // Ratios used to adjust position's to be dynamic for different phone sizes.
		ratioX = (float) (246.0/320.0);
		ratioXR = (float) (75.0/320.0);



		leftX = rightX = (ratioX*screenHeight); //246
		leftY = (ratioY*screenWidth); //350
		rightY = (ratioXR*screenHeight); //75
		RP = new RenderPerson(leftX, leftY, rightY);
		rightJoyStick = new RenderPerson(leftX, leftY, rightY,10); // Only need to be instantiated once for the game, because the right/left analog stick do not change.
		joyStickRadius =(float) (screenWidth*70)/510;
		
		
			// Set up the blue/glow effects for the character, this is the same throughout the levels.
		
		
		    _paintSimple.setAntiAlias(true);
		    _paintSimple.setDither(true);
		    _paintSimple.setColor(Color.argb(255, 13, 217, 228));
		    _paintSimple.setStrokeWidth(1);
		    _paintSimple.setStyle(Paint.Style.STROKE);
		    _paintSimple.setStrokeJoin(Paint.Join.ROUND);
		    _paintSimple.setStrokeCap(Paint.Cap.ROUND);

		    
		    _paintBlur.set(_paintSimple);
		    _paintBlur.setColor(0xff3DC2CC);
		    _paintBlur.setStrokeWidth(8);
		    _paintBlur.setMaskFilter(new BlurMaskFilter(3, BlurMaskFilter.Blur.SOLID));
		    
		    
		    // All of the above are variables that are not ALTERED by the program, therefore they can be set once
		    
		variableInit(); // Anything that IS ALTERED by the program needs to be re-initiliazed.
		
		animation.setSparkOrigin(currentMap[3][3].left+(currentMap[3][3].width())/2,currentMap[3][3].top+(currentMap[3][3].height())/2); // Set the location of the sparks
		animation.setSparkShot(); // Start the sparks
		
		        Resources res = context.getResources(); // Access the resources folder
				homeScreen = res.getDrawable(R.drawable.androidgameadea_portal); // Set the designated image
				Instructions = res.getDrawable(R.drawable.instructionpage); // Set the designated image
				instructions = false;
				this.setBackgroundDrawable(homeScreen);
	}

	//Bitmap board2 = Bitmap.createScaledBitmap(board, 50, 75, false);

	public boolean onTouchEvent (MotionEvent e){



		action = e.getAction(); 
		
		if (ls.homeScreen){
			if (e.getX() < screenWidth/2){
			ls.homeScreen = false;              // Sets homescreen to false, allowing for the game to start (if homescreen false, start game)
			}
			
			else{ instructions = true; // Go to instruction page
			ls.homeScreen = false;}
			
			
		}
		else if (instructions){instructions = false; // Go back to the homescreen (this should really be a back button)
		ls.homeScreen = true;}
		
		else if (death){ 						// Collision between AI + you, or AI Bullet + you


			if (action == MotionEvent.ACTION_DOWN)variableInit(); // Retry

		}
		else if (levelComplete){ 				// Walking into the "blue portal" located at the end of the map

			if (e.getY() > screenWidth/2){
				ls.levelCount++;				// Increases the level count
				wall.levelSet();
				portal.levelSet();              // Next Level
				variableInit();               
			}
			else{		             
				variableInit();                 // Retry
			}


		}
		else
		{
			
			
			/*
			 The following is multiple touching logic. This is varied on 
			 
			 a) The number of fingers (pointers) on the screen
			 b) The location of those touch locations (alternating between pointers based on finger switching for analog sticks)
			 c) Determining if zoom/pan is applicable in those areas (can't zoom/pan near the analog controls)
			 
			 */
			
			
			
			
			switch(action & MotionEvent.ACTION_MASK){  
			case MotionEvent.ACTION_DOWN:{	


				if (e.getX() > screenHeight*1/4){                                        // If the FIRST finger down (ACTION_DOWN) is below the top of the controls, that means the user
																						 // is using the analog sticks, therefore we set that location to an analog stick.

					if (e.getY(e.findPointerIndex(e.getActionIndex())) > 250){           // Which analog stick is set? That is based on whether or not the finger is on the left
						ActivePointerID = e.getPointerId(e.getActionIndex());            // or right size of the screen.
					}
					else if (e.getY(e.findPointerIndex(e.getActionIndex()))<250){
						SecondActivePointerID = e.getPointerId(e.getActionIndex());
					}
				}

				else {

					mode = DRAG;                                                         // If the first finger down is above the top of the controls, that means the user
																						 // is trying to pan/zoom, this being the case, we want to store this location for
																						 // our x1 value, in order to later determine the ΔX and ΔY for the panning option. 
					startX = e.getX() - previousTranslateX;
					startY = e.getY() - previousTranslateY;

				}


				break;
			}
			case (MotionEvent.ACTION_POINTER_DOWN):{									 // If the SECOND finger down (ACTION_POINTER_DOWN) is below the top of the controls, that means the user
																						 // is using the other analog stick, whereby we switch based on the previous
				if (e.getX() > screenHeight*1/4){
					if (e.getY(e.findPointerIndex(e.getActionIndex())) > 250){
						ActivePointerID = e.getPointerId(e.getActionIndex());
					}
					else if (e.getY(e.findPointerIndex(e.getActionIndex()))<250){
						SecondActivePointerID = e.getPointerId(e.getActionIndex());
					}
				}
				else {
					mode = ZOOM;													     // If the above is false, then two fingers above the analog sticks indicate zooming (panning with)
																					     // two fingers does not make sense, therefore the user is trying to zoom.

				}
				break;
			}
			case (MotionEvent.ACTION_MOVE):{                                             // If we are using the analog sticks to move, that means we are literally moving one of the
																						 // actual analog sticks, so within this if statement, we alternate based on which analog stick
																						 // is being configured.
				if (e.getX() > screenHeight*1/4) {
					if (ActivePointerID != InvalidPointerID){
						pointerIndex = e.findPointerIndex(ActivePointerID);
						if (pointerIndex != -1){
							RenderX = Math.abs(e.getX(pointerIndex)-(ratioX*screenHeight));
							RenderY = Math.abs(e.getY(pointerIndex)-(ratioY*screenWidth));

							
																						 // The below line is to compensate for the user using the analog stick outside of it's designated "range".
																						 // To compute this, we calculate the distance from the users finger location to the center of the analog stick
																						 // which is accomplished by using the pythagorean triangle to compute the hypotenuse (distance).
							
																						 // This value is then compared with the joyStickRadius, to see if it is outside the range of the analog stick.
																						 // If true, we have to compute the angle of the finger with respect to the analog centre, while displaying the
																						 // analog stick along the same line with respect to the analog centre and the users finger.
							
							
							if ((Math.sqrt(Math.pow((Math.abs(e.getX(pointerIndex)-(ratioX*screenHeight))),2) + Math.pow((Math.abs(e.getY(pointerIndex)-(ratioY*screenWidth))),2)) > joyStickRadius)){ // radius of User triangle > radius of Borderline

								x = e.getX(pointerIndex)-(ratioX*screenHeight); 		 // The x distance between the users finger and origin.
								y = e.getY(pointerIndex)-(ratioY*screenWidth);           // The y distance between the users finger and origin.
								theta = Math.atan(Math.abs(x/y));                        // The angle at which the users finger is with respect to the centre of the analog stick (tan = x/y)
								if (y < 0 & x < 0)//Q1                                   // The absolute value is given because all we care about is the angle with respect to the first quadrant (in the general mathematical sense).
								{
									leftY = (float) ((ratioY*screenWidth)-(r*Math.cos(theta)));         // The following code figures out where to place the analog stick's circlular picture within it's range
																										// with respect to the users finger location, utilizing the angle solved for above.
									leftX = (float) ((ratioX*screenHeight)-(r*Math.sin(theta)));
								}

								else if (y > 0 & x < 0)//Q2
								{
									leftY = (float) ((ratioY*screenWidth)+(r*Math.cos(theta)));
									leftX = (float) ((ratioX*screenHeight)-(r*Math.sin(theta)));
								}

								else if (y >0 & x > 0)//Q3
								{
									leftY = (float) ((ratioY*screenWidth)+(r*Math.cos(theta)));
									leftX = (float) ((ratioX*screenHeight)+(r*Math.sin(theta)));

								}

								else if (y < 0 & x > 0)//Q4
								{
									leftY = (float) ((ratioY*screenWidth)-(r*Math.cos(theta)));
									leftX = (float) ((ratioX*screenHeight)+(r*Math.sin(theta)));
								}
							}
							else 
							{
								leftX = e.getX(pointerIndex);                                      // If the finger was within the range to begin with, there is no need
																								   // to reposition the analog stick location.
								leftY = e.getY(pointerIndex);
							}
						}

					}
					
					/*
					 * 
					 * The follow code repeats the same process as above, except for the RIGHT analog stick, therefore it uses a variation of the variables used above, but offer
					 * the same functionality. 
					 * 
					 */
					
					
					if (SecondActivePointerID != InvalidPointerID){
						final int pointerIndex = e.findPointerIndex(SecondActivePointerID);
						if (pointerIndex != -1){
							AngleX = Math.abs(e.getX(pointerIndex)-(ratioX*screenHeight));
							AngleY = Math.abs(e.getY(pointerIndex)-(ratioXR*screenHeight));
							if ((Math.sqrt(Math.pow((Math.abs(e.getX(pointerIndex)-(ratioX*screenHeight))),2) + Math.pow((Math.abs(e.getY(pointerIndex)-(ratioXR*screenHeight))),2)) > joyStickRadius)){ // radius of User triangle > radius of Borderline

								x1 = e.getX(pointerIndex)-(ratioX*screenHeight);
								y1 = e.getY(pointerIndex)-(ratioXR*screenHeight);
								theta = Math.atan(Math.abs(x1/y1));
								if (y1 < 0 & x1 < 0)//Q1
								{
									rightY = (float) ((ratioXR*screenHeight)-(r*Math.cos(theta)));
									rightX = (float) ((ratioX*screenHeight)-(r*Math.sin(theta)));
								}

								else if (y1 >0 & x1 <0)//Q2
								{
									rightY = (float) ((ratioXR*screenHeight)+(r*Math.cos(theta)));
									rightX = (float) ((ratioX*screenHeight)-(r*Math.sin(theta)));
								}

								else if (y1 > 0 & x1 > 0)//Q3
								{
									rightY = (float) ((ratioXR*screenHeight)+(r*Math.cos(theta)));
									rightX = (float) ((ratioX*screenHeight)+(r*Math.sin(theta)));

								}

								else if (y1 < 0 & x1 >0)//Q4
								{
									rightY = (float) ((ratioXR*screenHeight)-(r*Math.cos(theta)));
									rightX = (float) ((ratioX*screenHeight)+(r*Math.sin(theta)));
								}
							}
							else 
							{
								rightX = e.getX(pointerIndex);
								rightY = e.getY(pointerIndex);
							}
						}

					}

				}
				else {                                                                   // Translate X represents the ΔX
																						 // Translate Y represents the ΔY	
					translateX = e.getX() - startX;
					translateY = e.getY() - startY;

					distance = Math.sqrt(Math.pow(e.getX() - (startX + previousTranslateX), 2) + // The hypotenuse of each Δ represents the overall distance spread apart from pointer 1 to 2

							Math.pow(e.getY() - (startY + previousTranslateY), 2)

							);

					if(distance > 0) {
						dragged = true;
					}              



				}
				break;
			}
			case(MotionEvent.ACTION_UP):{                                                // When the first pointer goes up, that means that it must be re-assigned to the opposite analog stick 
																						 // If there were a second pointer location there. This is because the second pointer only exists if 
																						 // there is a first pointer, if the first pointer goes up without the second, then the second will automatically go up
																						 // which would render an analog stick that was in use, inoperable. To resist this, the first pointer takes place of the second.
				if(e.getX() > screenHeight*1/4){
					if (e.getY( e.getActionIndex()) > 250){
						leftX = (ratioX*screenHeight);
						leftY = (ratioY*screenWidth);
						ActivePointerID = InvalidPointerID;
					}
					else if (e.getY( e.getActionIndex()) < 250){
						rightX = (ratioX*screenHeight);
						rightY = (ratioXR*screenHeight);
						SecondActivePointerID = InvalidPointerID;
						testBullet = true;
						firstTime = true;
					}

				}
				else {

					mode = NONE;                                                         // If the user is not touching the analog sticks, and the pointers are up, then they are using nothing, meaning the mode = nothing.
					dragged = false;
					previousTranslateX = translateX;
					previousTranslateY = translateY;

				}
				break;
			}

			case (MotionEvent.ACTION_POINTER_UP):{                                       // If the second pointer goes up, then we have to set the variable "SecondActivePointerID" to false.
				if (e.getX() > screenHeight*1/4){
					if (e.getY( e.findPointerIndex(e.getActionIndex())) > 250){
						leftX = (ratioX*screenHeight);
						leftY = (ratioY*screenWidth);
						ActivePointerID = InvalidPointerID;
					}
					else if (e.getY( e.findPointerIndex(e.getActionIndex())) < 250){     // In addition, firing portals are accomplished through releasing the second pointer, therefore testbullet/firsttimeshot must be true.
						rightX = (ratioX*screenHeight);
						rightY = (ratioXR*screenHeight);
						SecondActivePointerID = InvalidPointerID;
						testBullet = true;
						firstTime = true;
					}

				}
				else{
					mode = DRAG;
					previousTranslateX = translateX;                                     // If a second pointer went up, then two must have been down initially, meaning the user has spread their fingers apart.
																						 // this indicates that a drag has been accomplished.
					previousTranslateY = translateY;

				}
				break;
			}

			}
			if (mode == DRAG ||mode == ZOOM) 											 // Solves lots and lots of problems to do with analog zooming
																						 // Only call detector if we are panning/zooming
				detector.onTouchEvent(e);

			if ((mode == DRAG && scaleFactor != 1f && dragged) || mode == ZOOM) {        // This should be tested for practicality later**

				invalidate();
			}



		}
		return true;	
	}
	public void onDraw (Canvas canvas){
		
		
		
		if (ls.homeScreen)
			this.setBackgroundDrawable(homeScreen); 														     // Don't do anything but display the graphic
		else if (instructions)this.setBackgroundDrawable(Instructions);                                          // Display the instructions
		else if (death){
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(1);
			paint.setTextSize(80);
			canvas.drawText("REPLAY",70, 200, paint);									// Display replay screen because the user is dead, therefore they have not earned the privalage to move onto the next level.
		}
		else if (levelComplete){
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(1);
			paint.setTextSize(50);
			canvas.drawText("REPLAY", 50, 100, paint);									// The user has earned the privalage to move onto the next level, or replay the current level.
			canvas.drawText("CONTINUE",85, 300, paint);
		}
		else
		{	
			super.onDraw(canvas);														// If the user has not won anything or died, then the current level must still be rendered
			canvas.save();
			canvas.scale(scaleFactor, scaleFactor);                                     // Zoom/Pan logic
			if((translateX * -1) < 0) {
				translateX = 0;
			}
			else if((translateX * -1) > (scaleFactor - 1) * displayWidth) {
				translateX = (1 - scaleFactor) * displayWidth;
			}
			if(translateY * -1 < 0) {
				translateY = 0;
			}
			if((translateY * -1) > (scaleFactor - 1) * displayHeight) {
				translateY = (1 - scaleFactor) * displayHeight;
			}
			if (ls.Locked)canvas.translate((float)(-xpos+80 / scaleFactor),(float)( -ypos+200 / scaleFactor)); // Changes between follow or unlocked.
																											   // Follow = Camera is locked on the character by adjusting the canvas around the position of the user.
																											   // Unlocked = Camera is stationary and can be moved around by the users panning capabilities. 
			else canvas.translate(translateX / scaleFactor, translateY / scaleFactor); // changes between follow or pan;
			

			/* The rest of the drawing code to render the environment, character, AI, animations, etc. */

			//************************************************************************



			//System.out.println(translateY);
			
			/*
			  
			  
			 The way the following works, is the for loop runs through every single AI for the specific level. So each level has a certain number of AI's (current capped at 5), and the loop
			 iterates between each of them. The rest of the code uses the iterating variable to indicate which AI is being manipulated (everything is an array). The positioning system
			 is basically static, setting the AI to walk to a specific destination LINEARLY, and then retracing its steps. The way this was accomplished was through using a stack to store the initial positions,
			 popping those values off into a queue. The queue then held the reverse order, which then removed each element from the head in order to tell the AI where to move to next. As these positions are 
			 removed from the queue, they are sequentially passed back into the stack, whereby the process repeats itself. This allowed us to have every item removed from the queue coming to us in the direction to go one way, 
			 then go back.
			 
			 Example:
			 
			          AI RUNNING TO THEIR END GOAL        ||              AI RUNNING BACK TO THEIR START  
			                                              ||
			 Stack's Current State            Queue       ||       Stack's Current State               Queue
			                                              ||
			                                  4321        ||                                           1234 
			         1                                    ||                4
			         2 									  ||			    3
			         3                                    ||                2
			         4						     		  ||			    1	
			  
			  
			  
			 
			 */
			
			
			
			
			
			
			for (int i = -1 ; ++ i < enemies.length;) {           						 // Set the "goals" for the AI to reach / compute tracing their steps.

				if (ai.iDirections[ls.levelCount].get(i) != null){                       // This is the ai walking logic
					if (counter[i] == size[i]){
						counter[i] = 0;
					}
					if (counter[i] == 0 ){
						if (iDirection[i].size() == 0)refill(size[i], i);
						enemyiStart[i] = iDirection[i].removeFirst();
						enemyiGoal[i] = iDirection[i].removeFirst();
						enemyjStart[i] =jDirection[i].removeFirst(); 
						enemyjGoal[i] = jDirection[i].removeFirst();
						xx[i] = (int)currentMap[enemyiStart[i]][enemyjStart[i]].exactCenterX();
						yy[i] = (int)currentMap[enemyiStart[i]][enemyjStart[i]].exactCenterY();
						counter[i]++;
					}

					try{


						if ((currentMap[enemyiGoal[i]][enemyjGoal[i]]).contains(enemies[i])){
							counter[i]++;

							if (counter[i] == size[i]){

								ai.iDirections[ls.levelCount].get(i).push(enemyiStart[i]);
								ai.jDirections[ls.levelCount].get(i).push(enemyjStart[i]);
								ai.iDirections[ls.levelCount].get(i).push(enemyiGoal[i]);
								ai.jDirections[ls.levelCount].get(i).push(enemyjGoal[i]);

							}
							else{
								ai.iDirections[ls.levelCount].get(i).push(enemyiStart[i]);
								enemyiStart[i] = enemyiGoal[i];
								enemyiGoal[i] = iDirection[i].removeFirst();
								ai.jDirections[ls.levelCount].get(i).push(enemyjStart[i]);
								enemyjStart[i] = enemyjGoal[i];
								enemyjGoal[i] = jDirection[i].removeFirst();
								xx[i] = (int)currentMap[enemyiStart[i]][enemyjStart[i]].exactCenterX();
								yy[i] = (int)currentMap[enemyiStart[i]][enemyjStart[i]].exactCenterY();
							}	
						}
					}catch(NullPointerException e){}
				}
			}


			if (leftX != (ratioX*screenHeight) & leftY != (ratioY*screenWidth)){         // Check's if the analog stick is not at its centre (meaning it is not being used)
				xpos = (float)RP.xPos(xpos, getAngle(), RP.getQuadrant(leftX,leftY,1));  // Sets the position of the AI (moving it)
				ypos = (float)RP.yPos(ypos, getAngle(),RP.getQuadrant(leftX,leftY,1));
			}

			if (SecondActivePointerID != InvalidPointerID){                     
				tempTheta = (float) Math.atan(AngleX/AngleY);
				tempRightX = rightX;
				tempRightY = rightY;
			}




			paint.setAntiAlias(true);													 // Smooth's out the line ridge's

			boo = wall.checkWall(you, currentMap);                                       // Checks if the users (you) collided with a wall
			if (boo){
				xpos = wall.retX;			                                             // Sets your position to "bounce" off the wall
				ypos = wall.retY;
			}


			if (portal.portalLocatorI.size() == 2){                                      // If you've shot two portals, you can now teleport!
				if (currentMap[portal.x1][portal.y1].contains(you)){                     // If the first portal contains you (walked into)
					portal.adjacentValue( portal.x2, portal.y2);
					xpos = currentMap[portal.I][portal.J].exactCenterX();
					ypos = currentMap[portal.I][portal.J].exactCenterY();}
				else if (currentMap[portal.x2][portal.y2].contains(you)){				 // If the second portal contains you (walked into)
					portal.adjacentValue( portal.x1, portal.y1);     
					xpos = currentMap[portal.I][portal.J].exactCenterX();
					ypos = currentMap[portal.I][portal.J].exactCenterY();}
			}



			endX = (float)rightJoyStick.xPos(xpos,tempTheta, rightJoyStick.getQuadrant(tempRightX, tempRightY,2));  // Determines where the bullet starts from (tip of the retangular gun)
			endY = (float)rightJoyStick.yPos(ypos, tempTheta, rightJoyStick.getQuadrant(tempRightX, tempRightY,2));

			

									//drawing the environment
			canvas.drawColor(Color.BLACK);	
			
				paint.setARGB(255,0,0,0); // crevaces between walking tiles match the background (black)
				paint.setStyle(Style.STROKE);
				paint.setStrokeWidth(1);
				for (int i = -1; ++i < walkCount;){
					canvas.drawRect(currentMap[wall.walkingPositionsi[i]][wall.walkingPositionsj[i]], paint); // draw walking positions
				}
				paint.setColor(0xff5D2496);
				paint.setStyle(Style.STROKE);
				paint.setStrokeWidth(1);
				paint.setShadowLayer(2,0,0,0xff7B27B0 );
				for (int i = -1; ++i < wallCount;){
					canvas.drawRect(currentMap[wall.wallPositionsi[i]][wall.wallPositionsj[i]], paint);  // draw wall positions
				}
				paint.setShadowLayer(0, 0, 0, color.background_dark);
				paint.setColor(0xffEB7C21);
				paint.setStyle(Style.FILL);
				paint.setStrokeWidth(1);
				for (int i = -1; ++i < 22;){						
					for (int j = -1; ++j < 14;){
						if (portal.checkWall(i, j).equals("J"))canvas.drawRect(currentMap[i][j], paint); // draw portal's
					}}
				
									//drawing the environment
				
				// The above was done in this way to layer tiles, as well as reduce calls to the Wall class

			paint.setARGB(255, 34, 62, 138);
			canvas.drawRect(stageWinPortal, paint); // draw the winning portal (blue)


			paint.setStyle(Paint.Style.STROKE);	
			paint.setARGB(255, 13, 217, 228);

			
			//Laser Sparks
			
			// These animations were made for lasers, but that's in process.
			
			paint.setColor(0xffE62E00);
		//	animation.laserWallSpark(currentMap[3][2].exactCenterX(), currentMap[3][2].exactCenterY(), frameCount, "left");
			for (int i = -1 ; ++ i < 6;){
			float s = animation.x[i];
			float c = animation.y[i];
			animation.laserWallSpark(i,"U");
			
			canvas.drawLine(s, c, animation.x[i], animation.y[i], paint);
			}
			frameCount += frameCount < 24 ? 1 : -frameCount;
			
			
			
			//Laser Sparks
			
			
			
			
			
			// GLOW
			
		   
		     
			_paintBlur.setStrokeWidth(7);
		    mPath.addCircle(xpos,ypos, 1, Path.Direction.CCW);      // You
		    canvas.drawPath(mPath, _paintBlur);
		    canvas.drawPath(mPath, _paintSimple);
			mPath.reset();                                          // After storing stuff in the path and drawing it, it must be emptied for rendering other things with similar characteristics. 
			
			
			// GLOW 
			
			paint.setARGB(255, 13, 217, 228);
			canvas.drawCircle(xpos, ypos, 5, paint);
			
			
			 
			
			you.set((int)(xpos-(5)),(int)(ypos-(5)),(int)(xpos+(5)),(int)(ypos+(5)));     // Setting your poisition (the rectangle around you)
																						  // **NOTE: The below line is commented, because the rectangle represents your border for collisions, not for graphics!

			//canvas.drawRect(you, paint);


			levelComplete = (you.intersect(stageWinPortal)) ? true : false;               // Checks to see if you've collided with the winning portal whereby the level is "complete"

			



			// DRAW ALL OF THE AI AND CHECK FOR COLLISIONS AS WELL AS SHOOTING BULLETS

			for (int i = -1 ; ++ i < enemies.length;) {

				if (ai.iDirections[ls.levelCount].get(i) != null){ // There is a maximum of 5 ai's, but that doesn't mean there are 5 ai's, this avoids nullpointer errors


					//canvas.drawRect(currentMap[enemyiGoal[i]][enemyjGoal[i]], paint);           Highlights the tile where the AI will travel to next

					if ( enemyjStart[i] == enemyjGoal[i] &&(enemyiGoal[i] - enemyiStart[i]) > 0 )yy[i]+=2;             // 2 is the speed of the AI's, they all have the same speed (for looped)
					else if (enemyjStart[i] == enemyjGoal[i] &&(enemyiGoal[i] - enemyiStart[i]) < 0 )yy[i]-=2;         // Since they are drawn linearly, either the x value or y value of the start/temporary "end" position will be the same, meaning
																													   // the values that are not the same must either be increased or decreased depending on the value being > or < than 0.

					if ( enemyiStart[i] == enemyiGoal[i] &&    (enemyjGoal[i] - enemyjStart[i]) > 0 )xx[i]+=2;
					else if (enemyiStart[i] == enemyiGoal[i] && (enemyjGoal[i] - enemyjStart[i]) < 0 )xx[i]-=2;

					//System.out.println(xx);
					enemies[i].set(xx[i]-5, yy[i]-5, xx[i]+5, yy[i]+5); // The 5's set how large the enemies are in terms of pixels
					paint.setARGB(255, 2, 245, 19);
					canvas.drawRect(enemies[i],paint); // Draw the enemies


					if (Math.sqrt(((enemies[i].centerX() - you.centerX())*(enemies[i].centerX() - you.centerX())) + (enemies[i].centerY() - you.centerY())*(enemies[i].centerY() - you.centerY())) < screenWidth*(0.19)){ // 90 pixels represents the range, if the user walks within this range (it's circular) then they will be shot at!
						ai.testBullet[i] = true;
					}
					if (ai.testBullet[i] && !wall.checkWall(enemyBullet[i], currentMap)){   // If the bullet hits a wall, then do not draw it
						ai.active[i] = true;
						enemyBullet[i].set((int)ai.bulletX(i,this)-1, (int)ai.bulletY(i,this)-1, (int)ai.bulletX(i,this)+1, (int)ai.bulletY(i,this)+1); // Sets the location of the bullets border (for collisions)
						paint.setARGB(255, 255, 0, 17);
						canvas.drawCircle(enemyBullet[i].centerX(),enemyBullet[i].centerY() , 1, paint);                                                // Draws the circular bullet


						//canvas.drawRect(enemyBullet[i],paint);       **Draws the boundary around the circle which hits you


					} 
					else if (wall.checkWall(enemyBullet[i], currentMap)){                                                                               // If the bullet hit a wall, stop drawing it
						enemyBullet[i].set(enemies[i].centerX(),enemies[i].centerY() , enemies[i].centerX(), enemies[i].centerY());
						ai.testBullet[i] = false;
						ai.bulletDistanceCount[i] = 0;
						ai.firstTime[i] = true;
						ai.active[i] = false;
					}
					if (you.intersect(enemyBullet[i])   || you.intersect(enemies[i]))if( ai.active[i])death = true;                                     // If you walk into the bullet/enemy, then you die.

				}
			}

			paint.setARGB(255, 13, 217, 228);
			oldX = xpos;
			oldY = ypos;

			paint.setStrokeWidth(1);
			mPath.moveTo(xpos, ypos);
			mPath.lineTo( endX, endY);
			_paintBlur.setStrokeWidth(5);
			canvas.drawPath(mPath, _paintBlur);
			canvas.drawPath(mPath, _paintSimple);
			canvas.drawLine(xpos, ypos, endX, endY, paint); // Your gun
			mPath.reset();
			paint.setShadowLayer(0,3,3,2);
			








			if (testBullet){ // This is the logic for how you shoot

				if (firstTime){ // Sets the starting position and angle ONCE,considering those are static variables in this equation.
					bulletX = endX;
					bulletY = endY;
					shootTheta = tempTheta;
					firstTime = false;
					shootx = tempRightX;
					shooty = tempRightY;
				}
				bulletDistanceCount ++;
				if (bulletDistanceCount % 1 == 0 & bulletDistanceCount < 100){ // A counter of 100 is what sets the distance the bullets can travel relative to the screen size. 

					bulletX = (float) (((rightJoyStick.xPos(bulletX,shootTheta, rightJoyStick.getQuadrant(shootx, shooty,2)) - bulletX)/10)*3)+bulletX; // Bullet x is returned as an increased value everytime in order for it to move.
																																						// The math around it is used to vary the speed (specifically the multiplying by 3 part)
					bulletY = (float)(((rightJoyStick.yPos(bulletY, shootTheta, rightJoyStick.getQuadrant(shootx, shooty,2)) - bulletY)/10)*3)+bulletY;
					bullet.set((int)(bulletX+2), (int)(bulletY+2), (int)(bulletX-2), (int)(bulletY-2));
					if (portal.checkWall(bullet, currentMap)){bulletDistanceCount = 0;testBullet  = false;}
				}
				else if (bulletDistanceCount >= 100){          // If the counter has gone past 100, stop drawing the screen
					testBullet = false;
					bulletDistanceCount = 0;
				}
				paint.setStyle(Style.FILL);
				paint.setColor(0xffF08811);
				canvas.drawCircle((float)(bulletX), (float)(bulletY), 2, paint);
				//canvas.drawRect(bullet, paint);
			}
			
			// The following draws the analogs stick
			// It is placed here because the analog sticks must be drawn ontop of everything.
			
			canvas.restore();	
			paint.setARGB(100, 255, 0, 208);	
			paint.setStrokeWidth(2);     // Left Joy Stick	
			paint.setStyle(Style.STROKE);
			canvas.drawCircle((ratioX*screenHeight),(ratioY*screenWidth),joyStickRadius,paint);
			paint.setStrokeWidth(40);
			paint.setStyle(Style.FILL);
			canvas.drawCircle(leftX,leftY,(float)(0.5*joyStickRadius),paint);


			paint.setStrokeWidth(2);                    // Right Joy Stick
			paint.setStyle(Style.STROKE);
			canvas.drawCircle((ratioX*screenHeight),(ratioXR*screenHeight),joyStickRadius,paint);
			paint.setStrokeWidth(40);
			paint.setStyle(Style.FILL);
			canvas.drawCircle(rightX,rightY,(float)(0.5*joyStickRadius),paint);
			paint.setAntiAlias(false);
		}
		invalidate();


	}
	public double getAngle (){ // This calculates the angle for the analog stick
		float retTheta;
		retTheta = (float) Math.atan((RenderX/RenderY));
		retTheta = (float) (retTheta == Double.NaN  && RenderX > (ratioX*screenHeight) ? 1.57 :retTheta);
		retTheta = (float) (retTheta == Double.NaN  && RenderX < (ratioX*screenHeight) ? 4.71 :retTheta);
		return retTheta;
	}
	public void refill(int size, int i){ // Refill is used to "fill" the direction queue for the AI
		for (int k = -1;++k<size;){

			iDirection[i].add(ai.iDirections[ls.levelCount].get(i).pop());
			jDirection[i].add(ai.jDirections[ls.levelCount].get(i).pop());
		}
	}
	public double leftX(){

		return leftX;
	}
	public double leftY (){

		return leftY; 
	}
	public double rightY(){
		return rightY;
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener { // Used for the panning/zooming
																							// We did not come up with this ourselves
																							// This is DIRECTLY copied from this site:
																							// http://android-developers.blogspot.ca/2010/06/making-sense-of-multitouch.html
		

		@Override

		public boolean onScale(ScaleGestureDetector detector) {

			scaleFactor *= detector.getScaleFactor();

			scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
			return true;
		}




	}

	public void variableInit (){ // Initializes all of the variables 
								 // Put into a method because of the level concept
		
		detector = new ScaleGestureDetector(getContext(), new ScaleListener());
		death =false;
		levelComplete = false;
		r =joyStickRadius;
		endX=2;
		endY=2;
		InvalidPointerID = -1;
		ActivePointerID=InvalidPointerID; 
		SecondActivePointerID=InvalidPointerID;
		testBullet = false;
		firstTime = false;
		bulletDistanceCount=0; 
		bulletX = endX;
		bulletY =  endY;
		MIN_ZOOM = 2.3f; //Starts out n times zoomed in.
		MAX_ZOOM = 5f;
		scaleFactor = 1.f;
		NONE = 0;
		DRAG = 1;
		ZOOM = 2;
		startX = 0f;
		startY = 0f;
		translateX = 0f;
		translateY = 0f;
		previousTranslateX = 0f;
		previousTranslateY = 0f; 
		dragged = false;
		currentMap = new Rect [22][14];
		xx  = new int [5];
		yy  = new int [5];
		counter = new int [5];
		enemies = new Rect [5];
		enemyiGoal = new int [5];
		enemyjGoal = new int [5];
		enemyiStart  = new int [5];
		enemyjStart = new int [5];
		iDirection  = new LinkedList [5];
		jDirection =  new LinkedList [5];
		size = new int [enemies.length];
		enemyBullet = new Rect [enemies.length];
		wall = new Wall(screenWidth,screenHeight,ls);
		portal = new Portal(wall,ls);
		
		
currentMap = wall.gridSet(currentMap); // Defines each rectangle size/location for the current Map
wall.setWallComponents(portal);        // Sets the arrays up for the walls, walking positions, and portal positions in order for them to be drawn in a layered fashion.
walkCount=0;                          // Set all the counts to zero every new level
wallCount=0;
portalCount=0;
for (int i : wall.walkingPositionsi) // Fill them all with a number that can be identified as "end of array"
{
if (i != 999)walkCount++;
else break;
	
}
for (int i : wall.wallPositionsi){
	if (i != 999)wallCount++;
	else break;
		
	}
for (int i : wall.portalPositionsi){
	if (i != 999)portalCount++;
	else break;
		
	}
		//set up AI positioning's
		for (int i = -1 ; ++i < enemies.length;){ // Instantiate all needed objects
		enemies[i] = new Rect();
		enemyBullet[i] = new Rect();
		iDirection[i] = new LinkedList<Integer>();
		jDirection[i] = new LinkedList<Integer>();}
		Arrays.fill(enemyiGoal, -1); // Fill them all with a number that can be identified as "end of array"
		Arrays.fill(enemyjGoal, -1);
		Arrays.fill(enemyiStart, -1);
		Arrays.fill(enemyjStart, -1);

		ai = new AI(enemies.length, ls); 


		if (ls.levelCount == 0){ // Draw level 1's ai's

			xpos = currentMap[1][1].exactCenterX();
			ypos = currentMap[1][1].exactCenterY();
			you = new Rect((int)(xpos-(5)),(int)(ypos-(5)),(int)(xpos+(5)),(int)(ypos+(5)));
			stageWinPortal.set(currentMap[1][12]);
			ai.setupAI(3);    // ai.setupAI(Number of AI'S to be made);


			ai.initI(0,0,6);
			ai.initI(0,0,6);    // ai.initI(AI ID number (level ,index # in array) , "i" positioning on the wall map in Wall);
			ai.initJ(0,0,1); 
			ai.initJ(0,0,5);    // ai.initJ(AI ID number (Level ,index # in array) , "j" positioning on the wall map in Wall);

			ai.initI(0,1,6);    // AI 2
			ai.initI(0,1,6);
			ai.initJ(0,1,8);
			ai.initJ(0,1,12);

			ai.initI(0,2,17);
			ai.initI(0,2,17);   // AI 3
			ai.initJ(0,2,12);
			ai.initJ(0,2,8);
	
		}

		else if (ls.levelCount == 1){ // Draw level 2's ai's

			// The following is the same for the rest of this if statement
			// Set the starting position for the person
			// Set the winning portal position
			// Define how many AI's are in the level
			// Initialize each AI's I'th and J'th position (They are drawn with respect to the currentMap rect[][] object)
			
			
			
			xpos = currentMap[1][1].exactCenterX(); 
			ypos = currentMap[1][1].exactCenterY();
			you = new Rect((int)(xpos-(5)),(int)(ypos-(5)),(int)(xpos+(5)),(int)(ypos+(5)));
			stageWinPortal.set(currentMap[1][11]); // winning portal tile

			ai.setupAI(5);

			ai.initI(1,0,1);
			ai.initI(1,0,1);
			ai.initI(1,0,3);
			ai.initJ(1,0,5);
			ai.initJ(1,0,7);
			ai.initJ(1,0,7);


			ai.initI(1,1,6);
			ai.initI(1,1,6);
			ai.initJ(1,1,6);
			ai.initJ(1,1,1);


			ai.initI(1,2,11);
			ai.initI(1,2,11);
			ai.initJ(1,2,6);
			ai.initJ(1,2,1);


			ai.initI(1,3,11);
			ai.initI(1,3,11);
			ai.initJ(1,3,10);
			ai.initJ(1,3,12);


			ai.initI(1,4,20);
			ai.initI(1,4,18);
			ai.initI(1,4,18);
			ai.initI(1,4,20);
			ai.initJ(1,4,1);
			ai.initJ(1,4,1);
			ai.initJ(1,4,3);
			ai.initJ(1,4,3);


		}
		else if (ls.levelCount == 2){ // Draw level 3's ai's

			xpos = currentMap[1][11].exactCenterX();
			ypos = currentMap[1][11].exactCenterY();
			you = new Rect((int)(xpos-(5)),(int)(ypos-(5)),(int)(xpos+(5)),(int)(ypos+(5)));
			stageWinPortal.set(currentMap[20][12]); // winning portal tile

			ai.setupAI(3);

			ai.initI(2,0,3);
			ai.initI(2,0,4);
			ai.initI(2,0,4);
			ai.initI(2,0,3);
			ai.initJ(2,0,3);
			ai.initJ(2,0,3);
			ai.initJ(2,0,4);
			ai.initJ(2,0,4);


			ai.initI(2,1,5);
			ai.initI(2,1,7);
			ai.initI(2,1,7);
			ai.initI(2,1,6);		
			ai.initJ(2,1,12);
			ai.initJ(2,1,12);
			ai.initJ(2,1,11);
			ai.initJ(2,1,11);	

			ai.initI(2,2,9);
			ai.initI(2,2,17);
			ai.initI(2,2,17);
			ai.initI(2,2,9);
			ai.initI(2,2,9);
			ai.initJ(2,2,2);
			ai.initJ(2,2,2);
			ai.initJ(2,2,7);
			ai.initJ(2,2,7);
			ai.initJ(2,2,2);


		}

		
		else if (ls.levelCount == 3){ // Draw level 4's ai's
			
			
			xpos = currentMap[1][1].exactCenterX();
			ypos = currentMap[1][1].exactCenterY();
			you = new Rect((int)(xpos-(5)),(int)(ypos-(5)),(int)(xpos+(5)),(int)(ypos+(5)));
			stageWinPortal.set(currentMap[9][12]);
			
			 ai.setupAI(3);    // ai.setupAI(Number of AI'S to be made);

			 ai.initI(3,0,5);
			 ai.initJ(3,0,3);
			 ai.initI(3,0,5);
			 ai.initJ(3,0,7);
			 
			 ai.initI(3,1,17);
			 ai.initJ(3,1,3);
			 ai.initI(3,1,17);
			 ai.initJ(3,1,7);
			 
			 ai.initI(3,2,13);
			 ai.initJ(3,2,10);
			 ai.initI(3,2,13);
			 ai.initJ(3,2,10);
			 
			 
		}


		//set up sizes 
		for (int i = -1; ++i < enemies.length;){
			if (ai.iDirections[ls.levelCount].get(i) != null)
				size[i] = ai.iDirections[ls.levelCount].get(i).size();
		}
scaleFactor = (float) 2.3; // Set the scaleFactor to 2.3 such that when the game starts, it starts zoomed in at this amount.
		
	}

}


