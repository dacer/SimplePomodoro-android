package dacer.planefighter;

import java.util.Calendar;

import dacer.utils.MyUtils;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Author:dacer
 * Date  :Jul 28, 2013
 */
public class MiniCir {
	private float startPositionX;
	private float startPositionY;
	private float endPositionX;
	private float endPositionY;
	private Paint cirPaint;
	private float nowPositionX;
	private float nowPositionY;
	
	private float nextStepX;
	private float nextStepY;
	private int mRadius = 12;
	private int mColor = Color.parseColor("#FF4444");
	private double startMillis;
	private double nextMoveMillis;
	private final int MOVE_PER_MILLIS = 15;
	
	private boolean DEAD = false;
	private boolean autoDeadWhenReachGoal = false;
	private onDeadListener deadListener;
	
	public MiniCir(float[] position,float pixelPer20Millis,Context context){
		Calendar c = Calendar.getInstance();
		startMillis = c.getTimeInMillis();
		nextMoveMillis = startMillis + MOVE_PER_MILLIS;
		
		mRadius = (int)MyUtils.dipToPixels(context,6f);
		startPositionX = position[0];
		startPositionY = position[1];
		endPositionX = position[2];
		endPositionY = position[3];
		cirPaint = new Paint();
		cirPaint.setColor(mColor);
		cirPaint.setAntiAlias(true);
		//------fot inchPer20Millis----
//		double pixelPer20Millis = inchPer20Millis *(
//				Math.pow(MyUtils.getXdpi()*MyUtils.getXdpi()+
//						MyUtils.getYdpi()*MyUtils.getYdpi(), 2.0));
//		
//		
		//-----triangle calculate for pixPer20Millis-----
		float distanceX = endPositionX-startPositionX;
		float distanceY = endPositionY-startPositionY;
		double thirdBigLine = Math.pow(distanceX*distanceX+
				distanceY*distanceY,1/2.0);
		nextStepX = (float) (pixelPer20Millis/thirdBigLine*distanceX);
		nextStepY = (float) (pixelPer20Millis/thirdBigLine*distanceY);
		//-----triangle calculate over-----
		nowPositionX = startPositionX;
		nowPositionY = startPositionY;
	}
	
	public void draw(Canvas canvas){
		if(!DEAD){
			//move every 15 millis
			Calendar nowC = Calendar.getInstance();
			double nowMillis = nowC.getTimeInMillis();
			if(nowMillis - nextMoveMillis< MOVE_PER_MILLIS){
				
			}else{
				nowPositionX += nextStepX;
				nowPositionY += nextStepY;
				nextMoveMillis += MOVE_PER_MILLIS;
			}
			canvas.drawCircle(nowPositionX, nowPositionY, mRadius, cirPaint);
			if(autoDeadWhenReachGoal && arrivalEnd()){
				//the circle will dead when arrival goal place
				deadListener.onCirDead(this);
				DEAD = true;
			}else if(arrivalEnd() && outOfScreen()){
				deadListener.onCirDead(this);
				DEAD = true;
			}
		}	
	}
	
	public boolean arrivalEnd(){
		if(endPositionX>startPositionX){
			if(nowPositionX > endPositionX){
				return true;
			}
		}else{
			if(nowPositionX < endPositionX){
				return true;
			}
		}
		return false;
	}
	
	public void setAutoDeadAfterArrival(boolean auto){
		autoDeadWhenReachGoal = auto;
	}
	public boolean outOfScreen(){
		if(nowPositionX<0||nowPositionX>MyUtils.getScreenWidth()){
			return true;
		}
		return false;
	}
	public float getNowPositionX(){
		return nowPositionX;
	}
	
	public float getNowPositionY(){
		return nowPositionY;
	}
	
	public void setColor(int color){
		mColor = color;
		cirPaint.setColor(mColor);
	}
	
	public void setDead(){
		DEAD = true;
	}
	public boolean isDead(){
		return DEAD;
	}
	public boolean isKnocked(float x,float y, int radius){
		float distancePF = (nowPositionX-x)*(nowPositionX-x)+
				(nowPositionY-y)*(nowPositionY-y);
		int radiusSUMPF = (mRadius+radius)*(mRadius+radius);
		if(!DEAD && distancePF<radiusSUMPF){
			return true;
		}else{
			return false;
		}
	}
	
	public void setOnDeadListener(onDeadListener listener){
		deadListener = listener;
	}
}
