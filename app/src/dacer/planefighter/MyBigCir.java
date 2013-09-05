package dacer.planefighter;

import dacer.utils.MyUtils;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Author:dacer
 * Date  :Jul 28, 2013
 */
public class MyBigCir {
	private Paint cirPaint;
	private float nowX;
	private float nowY;
	private int mRadius = 0;
	
	public MyBigCir(float x,float y){
//		mRadius = MyUtils.autoSetValue4DifferentScreen(30);
		cirPaint = new Paint();
		cirPaint.setAntiAlias(true);
		cirPaint.setColor(Color.parseColor("#33B5E5"));
		nowX = x;
		nowY = y;
	}
	
	public void draw(Canvas canvas){
		canvas.drawCircle(nowX, nowY, mRadius, cirPaint);
	}
	
	public void setPosition(float x, float y){
		if((x-mRadius)>=0 && (y-mRadius)>=0 &&
				(x+mRadius)<=MyUtils.getScreenWidth() &&
				(y+mRadius)<=MyUtils.getScreenHeight()){
			nowX = x;
			nowY = y;
		}
	}

	public float getX(){
		return nowX;
	}
	
	public float getY(){
		return nowY;
	}
	public int getRadius(){
		return mRadius;
	}
	public void setRadius(int radius){
		mRadius = radius;
	}
}
