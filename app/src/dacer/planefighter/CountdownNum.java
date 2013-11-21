package dacer.planefighter;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import dacer.utils.GlobalContext;
import dacer.utils.MyUtils;

/**
 * Author:dacer
 * Date  :Jul 28, 2013
 */
public class CountdownNum {
	private float size;
	private Paint mPaint;
	private long startMillis;
	private int mSecond;
	private boolean gameOver = false;
	private Context mContext;
	private Rect bound;
	
	public CountdownNum(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		size = MyUtils.spToPixels(context, 260f);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextAlign(Align.RIGHT);
        mPaint.setTypeface(Typeface.
        		createFromAsset(GlobalContext.getInstance().getAssets(),"fonts/Roboto-Thin.ttf"));
		mPaint.setTextSize(size);
		mPaint.setARGB(20, 0, 0, 0);
		Calendar c = Calendar.getInstance();
		startMillis = c.getTimeInMillis();
		bound = new Rect();
		mPaint.getTextBounds("9", 0, 1, bound);
	}
	
	public void draw(Canvas canvas){
		Calendar nowC = Calendar.getInstance();
		if(!gameOver){
			mSecond = (int) ((nowC.getTimeInMillis() - startMillis)/1000);
		}
		canvas.drawText(String.valueOf(mSecond), MyUtils.getScreenWidth(), 
				Math.abs(bound.top), mPaint);
	}
	
	public int getNowSecond(){
		Calendar nowC = Calendar.getInstance();
		int now = (int) ((nowC.getTimeInMillis() - startMillis)/1000);
		return now;
	}
	
	public void gameOver(){
		gameOver = true;
	}
}
