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
 * Date  :Jul 17, 2013
 */
public class WeekCirView extends View {
	private static int cirRadius;
	private static int cirSpacing;
	private static int cirLeftAlign;
	private ArrayList<Integer> colorSet;
	private Paint bgCirPaint;
	private float[] cirCenterX;
	private float cirCenterY;

	private Paint frontCirPaint;
    private RectF[] frontCirOval;
    private int[] sweepAngle;
	
	public WeekCirView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		colorSet = new ArrayList<Integer>();
		bgCirPaint = new Paint();
		bgCirPaint.setAntiAlias(true);
		cirRadius = (int)MyUtils.dipToPixels(context,12);
		cirSpacing = (int)MyUtils.dipToPixels(context,12);
		cirLeftAlign = (int)MyUtils.dipToPixels(context,5);
		
		
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
	
	public void setEveryPercent(float[] percent){
		for(int i=0; i<7; i++){
			this.sweepAngle[i] = (int) (percent[i] * 360);
		}
	}
	
	public void setCirColor(int color){
		frontCirPaint.setColor(color);
		postInvalidate();
	}

	@Override 
	protected void onDraw(Canvas canvas) {
		cirCenterY = cirRadius;
		for(int i=0; i<7; i++){
			if(colorSet.isEmpty()||colorSet.size()<i+1){
				colorSet.add(Color.parseColor("#cccccc"));
			}
			bgCirPaint.setColor(colorSet.get(i));
			canvas.drawCircle(cirCenterX[i], cirCenterY, cirRadius, bgCirPaint);
			
			canvas.drawArc(frontCirOval[i], 270, sweepAngle[i], true, frontCirPaint);
		}
	}

//	FOR custom view height
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
		measureHeight(heightMeasureSpec));
	}     
	
	private int measureWidth(int measureSpec){
		int preferred = cirLeftAlign+14*cirRadius+6*cirSpacing;
		return getMeasurement(measureSpec, preferred);
	}
	
	private int measureHeight(int measureSpec){
		int preferred = 2*cirRadius;
		return getMeasurement(measureSpec, preferred);
	}
	
	private int getMeasurement(int measureSpec, int preferred)
    {
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement = 0;
       
        switch(MeasureSpec.getMode(measureSpec))
        {
            case MeasureSpec.EXACTLY:
                // This means the width of this view has been given.
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                // Take the minimum of the preferred size and what
                // we were told to be.
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
   
        return measurement;
    }
	
	public void refreshView(){
		this.postInvalidate();
	}
	
	public void setCirColor(ArrayList<Integer> colorSet){
		this.colorSet = colorSet;
	}
	
	public void setAllColor(int color){
		for(int i=0; i<7; i++){
			colorSet.add(i, color);
		}
		postInvalidate();
	}
	
	public void setIndexColor(ArrayList<Integer> index, int color){
		if(!index.isEmpty()){
			for(int i=0; i<index.size(); i++){
				colorSet.set(index.get(i), color);
			}
		}
		
		postInvalidate();
	}
	
	public static int getRadius(){
		return cirRadius;
	}
	
	public static int getSpacing(){
		return cirSpacing;
	}
	
	public static int getLeftAlign(){
		return cirLeftAlign;
	}
}
