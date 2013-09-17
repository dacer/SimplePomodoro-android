package dacer.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

import com.dacer.simplepomodoro.R;

import dacer.interfaces.OnClickCircleListener;
import dacer.settinghelper.SettingUtility;
import dacer.utils.MyUtils;

/**
 * Author:dacer
 * Date  :Jul 17, 2013
 */
public class CircleView extends View {
	//used to draw two circles
	Context mContext;
	private int mAllAlpha = 0;
	private Boolean enterAnimSign = true;
	private int backgroundColor;
	private int bigCirColor;
	private Paint bigCirPaint;
    private Paint centerCirPaint;
    private Paint textPaint;
    private RectF bigCirOval;
    private float mCenterX;
    private float mCenterY;
    private float textTrueCenterY;
    private float centerCirRadius;
    private String mCenterTextStr;
    private float mStartAngle;
    private float mSweepAngle;
    private OnClickCircleListener mListener;
    private boolean moveSign = false;
    private boolean downSign = false;
    
    private enum AnimType {
		DOWN,UP
	}
    private static int CENTER_TEXT_SIZE;
    private final static boolean USE_CENTER = true;
    
    //Mode 2
    private Paint mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mButtonTextPaint;
    private Paint mtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mtRotate;
    private Matrix mtMatrix = new Matrix();
    private Shader mtShader;
    public enum RunMode{
    	MODE_ONE,MODE_TWO
    }
    private RectF buttonOval;
    private RunMode mRunmode;
    private float mBigCirRadius;
    private float mStatusBarHeight;
    private String BUTTON_TEXT;
    
    public CircleView(Context context,float cirCenterX, float cirCenterY,
    		float bigCirRadius, String centerTextStr,
    		float sweepAngle, OnClickCircleListener listener,RunMode mode) {
        super(context);
        mContext = context;
        backgroundColor = SettingUtility.isLightTheme()? Color.WHITE:Color.BLACK;
        bigCirColor = SettingUtility.getBigCirColor();
        mtShader = new SweepGradient(cirCenterX, cirCenterY,
        		SettingUtility.getWaitShaderColor(), null);

        mStatusBarHeight = MyUtils.getStatusBarHeight(context);
        CENTER_TEXT_SIZE = MyUtils.autoSetValue4DifferentScreen(88);
        float startAngle = 270;
        int allAlpha = 255;
        mListener = listener;
        mCenterTextStr = centerTextStr;
        mStartAngle = startAngle;
        mSweepAngle = sweepAngle;
        mCenterX = cirCenterX;
        mCenterY = cirCenterY;
        
        bigCirPaint = new Paint();
        bigCirPaint.setAntiAlias(true);
        bigCirPaint.setColor(bigCirColor);
        bigCirPaint.setAlpha(allAlpha);
        
        bigCirOval = new RectF(cirCenterX - bigCirRadius,
        				cirCenterY - bigCirRadius, 
        				cirCenterX + bigCirRadius, 
        				cirCenterY + bigCirRadius);
        
        centerCirPaint = new Paint(bigCirPaint);
        centerCirPaint.setColor(backgroundColor);
        centerCirRadius = bigCirRadius - autoSetRingThickness();
        
        textPaint = new Paint();
        textPaint.setARGB(allAlpha, 255, 255, 255);//RGB-->White
        if(SettingUtility.isLightTheme()){
        	textPaint.setARGB(allAlpha, 0, 0, 0);//RGB-->BLACK
        }
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(CENTER_TEXT_SIZE);
        textPaint.setTypeface(Typeface.
        		createFromAsset(context.getAssets(),"fonts/Roboto-Thin.ttf"));
        //!Get the text true center Y location 
        //more info: http://sd4886656.iteye.com/blog/1200890
        FontMetrics fm = textPaint.getFontMetrics();  
        textTrueCenterY = mCenterY - (fm.descent + fm.ascent)/2; 
//        Log.e("CirView",centerTextStr);
        
      //MODE 2
        //get Button Text Width
        mButtonTextPaint = new Paint(textPaint);
        mButtonTextPaint.setTextSize(MyUtils.autoSetValue4DifferentScreen(68));
        mButtonTextPaint.setColor(Color.WHITE);
        BUTTON_TEXT = context.getString(R.string.time_up);
        Rect rect=new Rect();
        mButtonTextPaint.getTextBounds(BUTTON_TEXT, 0, BUTTON_TEXT.length(), rect);
        //text width = rect.width()
        mtPaint.setShader(mtShader);
        mRunmode = mode;
        mBigCirRadius = bigCirRadius;
        buttonOval = new RectF(cirCenterX - rect.width()/2-MyUtils.autoSetValue4DifferentScreen(30),
        		cirCenterY*2-mStatusBarHeight + fm.top-12, 
				cirCenterX + rect.width()/2+MyUtils.autoSetValue4DifferentScreen(30), 
				cirCenterY*2-mStatusBarHeight+20);
        
        mButtonPaint.setColor(SettingUtility.getButtonTextColor());
        
        //MODE 2
    }
    

    
    @Override 
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(backgroundColor);
        if(enterAnimSign){
        	setMyAlpha(mAllAlpha);
        }
       
        if(mRunmode.equals(RunMode.MODE_ONE)){
        	canvas.drawArc(bigCirOval,mStartAngle,mSweepAngle, USE_CENTER, bigCirPaint);
            //Draw Center Circle
            canvas.drawCircle(mCenterX, mCenterY, centerCirRadius, centerCirPaint);
            //Draw Center Text
            canvas.drawText(mCenterTextStr,mCenterX,textTrueCenterY, textPaint);
            
        }else if(mRunmode.equals(RunMode.MODE_TWO)){
        	//enterANIM
        	setMode2ButtonAlpha(mAllAlpha);
        	//enterANIM
        	mtMatrix.setRotate(mtRotate, mCenterX, mCenterY);
            mtShader.setLocalMatrix(mtMatrix);
            mtRotate += 3;
            if (mtRotate >= 360) {
                mtRotate = 0;
            }
            canvas.drawCircle(mCenterX, mCenterY, mBigCirRadius, mtPaint);
            canvas.drawCircle(mCenterX, mCenterY, centerCirRadius, centerCirPaint);
//            canvas.drawRect(buttonOval, mButtonPaint);
            canvas.drawRoundRect(buttonOval, 22, 22, mButtonPaint);
            canvas.drawText(mCenterTextStr,mCenterX,textTrueCenterY, textPaint);
            canvas.drawText(mContext.getString(R.string.time_up),
            		mCenterX,mCenterY*2-mStatusBarHeight-10, mButtonTextPaint);
            invalidate();
        }
        //enter animation
        if(mAllAlpha<246 && enterAnimSign){
        	mAllAlpha+=7;
        	invalidate();
        }else{
        	enterAnimSign = false;
        }
    }

    
    class myAnimThread implements Runnable { 
    	private AnimType mAnimType;
		private Boolean run= true;
		private int animSign = 1;
        private float mSize = textPaint.getTextSize();
        private int MIN_TEXT_SIZE = CENTER_TEXT_SIZE - 10;
        private int MAX_TEXT_SIZE = CENTER_TEXT_SIZE + 10;
        
        myAnimThread(AnimType at){
    		mAnimType = at;
    	}
		@Override
		public void run() {     
            while (!Thread.currentThread().isInterrupted()&& run) {      
                 try {  
                	 switch (mAnimType) {
					case DOWN:
						mSize--;
	                	setMyTextSize(mSize);
	                	postInvalidate();
	                	if(mSize < MIN_TEXT_SIZE){
	                		 run = false;
	                	}
						break;

					case UP:
						if(animSign == 1){
							mSize++;
							setMyTextSize(mSize);
		                	postInvalidate();
		                	if(mSize > MAX_TEXT_SIZE){
		                		 animSign = 2;
		                	}
						}else if(animSign == 2){
							mSize--;
							setMyTextSize(mSize);
		                	postInvalidate();
		                	if(mSize < CENTER_TEXT_SIZE){
		                		 run = false;
		                	}
						}
						break;
					default:
						break;
					}
                	 Thread.sleep(8);       
                 } catch (InterruptedException e) {      
                	 Thread.currentThread().interrupt();      
                 }  
             }      
        }     
	}
    @Override  
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();   
        float y = event.getY();  
        int action = MotionEventCompat.getActionMasked(event);
        
        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
            	if(MyUtils.isInCir(x, y,mCenterX,mCenterY,centerCirRadius)){
                	moveSign = true;
                	if(!downSign){
                		new Thread(new myAnimThread(AnimType.DOWN)).start();
                		downSign = true;
                	}
                }
            	
            	
                return true;
            case (MotionEvent.ACTION_MOVE) :
                return true;
            case (MotionEvent.ACTION_UP) :
            	if(MyUtils.isInCir(x, y,mCenterX,mCenterY,centerCirRadius) && moveSign){
            		if(SettingUtility.isVibrator()){
            			Vibrator mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            			mVibrator.vibrate(10);
            		}
            		mListener.onClickCircle();
            	}
            	if(moveSign){
            		new Thread(new myAnimThread(AnimType.UP)).start();
            	}
            	moveSign = false;
            	downSign = false;
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                return true;      
            default : 
                return super.onTouchEvent(event);
        }
    } 
    
    
    /*
     *
     * 
     */
    public void setMyAlpha(int alpha){
    	bigCirPaint.setAlpha(alpha);
    	mtPaint.setAlpha(alpha);
    	setTextAlpha(alpha);
    }

    public void setTextAlpha(int alpha){
    	if(SettingUtility.isLightTheme()){
        	textPaint.setARGB(alpha, 0, 0, 0);//RGB-->BLACK
        }else{
        	textPaint.setARGB(alpha, 255, 255, 255);
        }
    }
    
    public void setMode2ButtonAlpha(int alpha){
    	mButtonPaint.setAlpha(alpha);
    	mButtonTextPaint.setARGB(alpha, 255, 255, 255);
    }
    
    public void setMySweep(float sweep){
    	mSweepAngle = sweep;
    	
    }
    
    public void setMyText(String str){
//    	Log.e("CirView",str);
    	mCenterTextStr = str;
    }
    
    public void setMyTextSize(float size){
    	textPaint.setTextSize(size);
    }
    
    private float autoSetRingThickness(){
    	float value = 10;
    	int screenWidth = MyUtils.getScreenWidth();
    	int screenHeight = MyUtils.getScreenHeight();
    	if(screenWidth > screenHeight){
    		int temp = screenWidth;
    		screenWidth = screenHeight;
    		screenHeight = temp;
    	}
    	if(screenHeight<854 || screenWidth<480){
    		value = 5;
    	}
    	return value;
    }
    
    
}
