package dacer.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import dacer.interfaces.OnClickCircleListener;
import dacer.utils.MyUtils;
import dacer.views.CircleView.RunMode;

/**
 * Author:dacer
 * Date  :Feb 21, 2014
 */
public class CirLayout extends FrameLayout {
	private CircleView v;
	
	public CirLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        initView();
    }

    public CirLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initView();
    }
    
    public CirLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    
    

	public void init(Context context,float cirCenterX, float cirCenterY,
    		float bigCirRadius, String centerTextStr,
    		float sweepAngle, OnClickCircleListener listener,RunMode mode) {
		v = new CircleView(context, cirCenterX, cirCenterY, bigCirRadius, centerTextStr, sweepAngle, listener, mode);
        addView(v);
    }

	public CircleView getCircleView(){
	return v;	
	}

    
//  @Override  
//  public boolean onInterceptTouchEvent(MotionEvent ev) {  
//      int action = ev.getAction();  
//      float x = ev.getX();   
//      float y = ev.getY();  
//      switch (action) {  
//      case MotionEvent.ACTION_DOWN:  
//    	  if(MyUtils.isOnCirBorder(x, y,v.mCenterX,v.mCenterY,v.centerCirRadius, getContext())){
//        		return false;
//        	} 
//          return true;  
//      case MotionEvent.ACTION_MOVE: 
//    	  //Yes , there are all sh*t
//    	  if(MyUtils.isOnCirBorder(x, y,v.mCenterX,v.mCenterY,v.centerCirRadius, getContext())){
//      		return false;
//      	}
//          return true;  
//      case MotionEvent.ACTION_UP:  
//          return true;  
//      case MotionEvent.ACTION_CANCEL:    
//          return true;  
//      }  
//      return false;  
//  } 
}