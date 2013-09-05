package dacer.views;

import java.util.ArrayList;

import dacer.utils.MyUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author:dacer
 * Date  :Jul 18, 2013
 */
public class WeekCirViewMode2 extends View {
	private boolean enableMode2 = false;
	private float[] cirCenterX;
	private float cirCenterY;
	private Paint frontCirPaint;
    private RectF[] frontCirOval;
    private int[] sweepAngle;
	
	public WeekCirViewMode2(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		int cirRadius = WeekCirView.getRadius();
		int cirSpacing = WeekCirView.getSpacing();
		int cirLeftAlign = WeekCirView.getLeftAlign();
		
		frontCirPaint = new Paint();
		frontCirPaint.setAntiAlias(true);
		frontCirPaint.setColor(Color.parseColor("#FF4444"));
		cirCenterY = cirRadius;
		cirCenterX = new float[7];
		frontCirOval = new RectF[7];
		sweepAngle = new int[7];
		for(int i=0; i<7; i++){
			cirCenterX[i] = cirLeftAlign+(2*i+1)*cirRadius+i*cirSpacing;
			frontCirOval[i] = new RectF(cirCenterX[i] - cirRadius,
					cirCenterY - cirRadius, 
					cirCenterX[i] + cirRadius, 
					cirCenterY + cirRadius);
			sweepAngle[i] = 0;
		}
	}

	@Override 
	protected void onDraw(Canvas canvas) {
		if(enableMode2){
			for(int i=0; i<7; i++){
				canvas.drawArc(frontCirOval[i], 270, sweepAngle[i], true, frontCirPaint);
			}	
		}
	}
	
	public void refreshView(){
		this.postInvalidate();
	}

	public void setEveryPercent(float[] percent){
		for(int i=0; i<7; i++){
			this.sweepAngle[i] = (int) (percent[i] * 360);
		}
	}
	
	public void setCirColor(int color){
		frontCirPaint.setColor(color);
		postInvalidate();
	}
	
	public void enabelMode(){
		enableMode2 = true;
		postInvalidate();
	}

}
