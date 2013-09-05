package dacer.planefighter;

import java.util.Calendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import dacer.utils.GlobalContext;
import dacer.utils.MyUtils;

/**
 * Author:dacer
 * Date  :Jul 28, 2013
 */
public class CountdownNum {
	private float size=300;
	private Paint mPaint;
	private long startMillis;
	private int mSecond;
	private boolean gameOver = false;
	public CountdownNum() {
		// TODO Auto-generated constructor stub
		size = MyUtils.autoSetValue4DifferentScreen(380);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextAlign(Align.RIGHT);
        mPaint.setTypeface(Typeface.
        		createFromAsset(GlobalContext.getInstance().getAssets(),"fonts/Roboto-Thin.ttf"));
		mPaint.setTextSize(size);
		mPaint.setARGB(20, 0, 0, 0);
		Calendar c = Calendar.getInstance();
		startMillis = c.getTimeInMillis();
	}
	
	public void draw(Canvas canvas){
		Calendar nowC = Calendar.getInstance();
		if(!gameOver){
			mSecond = (int) ((nowC.getTimeInMillis() - startMillis)/1000);
		}
		canvas.drawText(String.valueOf(mSecond), MyUtils.getScreenWidth(), 
				MyUtils.autoSetValue4DifferentScreen(300), mPaint);
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
