package dacer.views;

import com.dacer.simplepomodoro.R;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import dacer.interfaces.OnClickCircleListener;
import dacer.utils.MyUtils;

public class CircleView extends View {
	//used to draw two circle
	Context mContext;
	private boolean isLightTheme = true;
	private int mAllAlpha = 0;
	private Boolean enterAnimSign = true;
	private int backgroundColor = Color.BLACK;
	private int bigCirColor = Color.parseColor("#CC0000");
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
    private SharedPreferences sp;
    
    public CircleView(Context context,float cirCenterX, float cirCenterY,
    		float bigCirRadius, float ringThickness, String centerTextStr,
    		int centerTextSize, float sweepAngle, OnClickCircleListener listener,RunMode mode) {
        super(context);
        mContext = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        isLightTheme = (sp.getString("pref_theme_type", "black").equals("white"));
        if(isLightTheme){
        	backgroundColor = Color.WHITE;
        	bigCirColor = Color.parseColor("#FF4444");
        	mtShader = new SweepGradient(cirCenterX, cirCenterY, new int[] { 
        			Color.parseColor("#FFBB33"),
    				Color.parseColor("#FF4444"),
    				Color.parseColor("#FF4444"),
    				Color.parseColor("#FF4444"),
    				Color.parseColor("#FF4444"),
    				Color.parseColor("#FF4444"),
    				Color.parseColor("#FFBB33") }, null);
        }else{
        	mtShader = new SweepGradient(cirCenterX, cirCenterY, new int[] { 
        			Color.parseColor("#90d7ec"),
    				Color.parseColor("#145b7d"),
    				Color.parseColor("#145b7d"),
    				Color.parseColor("#145b7d"),
    				Color.parseColor("#145b7d"),
    				Color.parseColor("#145b7d"),
    				Color.parseColor("#90d7ec") }, null);
        }
        mStatusBarHeight = MyUtils.getStatusBarHeight(context);
        CENTER_TEXT_SIZE = centerTextSize;
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
        centerCirRadius = bigCirRadius - ringThickness;
        
        textPaint = new Paint();
        textPaint.setARGB(allAlpha, 255, 255, 255);//RGB-->White
        if(isLightTheme){
        	textPaint.setARGB(allAlpha, 0, 0, 0);//RGB-->BLACK
        }
//        textPaint.setColor(Color.WHITE);
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
        mButtonTextPaint.setTextSize(CENTER_TEXT_SIZE - 20);
        mButtonTextPaint.setColor(Color.WHITE);
        BUTTON_TEXT = context.getString(R.string.time_up);
        Rect rect=new Rect();
        mButtonTextPaint.getTextBounds(BUTTON_TEXT, 0, BUTTON_TEXT.length(), rect);
        //text width = rect.width()
        mtPaint.setShader(mtShader);
        mRunmode = mode;
        mBigCirRadius = bigCirRadius;
        buttonOval = new RectF(cirCenterX - rect.width()/2-30,
        		cirCenterY*2-mStatusBarHeight + fm.top, 
				cirCenterX + rect.width()/2+30, 
				cirCenterY*2-mStatusBarHeight+20);
        
        mButtonPaint.setColor(Color.parseColor(isLightTheme ? "#FF4444":"#145b7d"));
        
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
        private final static int MIN_TEXT_SIZE = 78;
        private final static int MAX_TEXT_SIZE = 98;
        
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
        // 获取点击屏幕时的点的坐标   
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
            		Boolean isVibrator = sp.getBoolean("pref_enable_vibrations", false);
            		if(isVibrator){
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
    	if(isLightTheme){
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
}
