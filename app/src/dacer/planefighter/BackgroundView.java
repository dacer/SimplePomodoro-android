//package dacer.planefighter;
//
//import java.util.ArrayList;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Canvas;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//
//import com.dacer.simplepomodoro.R;
//
//import dacer.utils.GlobalContext;
//import dacer.utils.MyUtils;
//
///**
// * Author:dacer
// * Date  :Jul 27, 2013
// */
//public class BackgroundView extends View implements onKnockListener,onDeadListener{
//	private float mPreviousX;
//    private float mPreviousY;
//    private CountdownNum countdownNum;
//    private MyBigCir mainCir;
//    private ArrayList<HugeWareBullets> hugeWare;
//    private ArrayList<TrackedBullets> trackedBullets;
//    private ArrayList<HugeWareBullets> hugeWareShouldDead;
//    private ArrayList<TrackedBullets> trackedBulletsShouldDead;
//    private ArrayList<GoodBullets> goodBullets;
//    private boolean running = true;
//    private int ADD_WARE_DURTION = 3;
//    private int nowSecNumForAdd = 2;
//    private int addWareNum = 1;
//    private boolean isAddware = true;
//    private float redSpeed = 9;
//    private float orangeSpeed = 15;
//    private int MAIN_RADIUS;
//    	
//    private onGameOverListener gameOverListener;
//    
//    public BackgroundView(Context context){
//    	super(context);
//    	initView(context);
//    }
//	public BackgroundView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		initView(context);
//	}
//
//	private void initView(Context context){
//		countdownNum = new CountdownNum();
//		mainCir = new MyBigCir(MyUtils.getScreenWidth()/2, MyUtils.getScreenHeight()/3);
//		MAIN_RADIUS = MyUtils.autoSetValue4DifferentScreen(30);
//		hugeWare = new ArrayList<HugeWareBullets>();
//		trackedBullets = new ArrayList<TrackedBullets>();
//		hugeWareShouldDead = new ArrayList<HugeWareBullets>();
//		trackedBulletsShouldDead = new ArrayList<TrackedBullets>();
//		goodBullets = new ArrayList<GoodBullets>();
//		addWare();
//	}
//
//	@Override 
//	protected void onDraw(Canvas canvas) {
//		if(countdownNum.getNowSecond() == nowSecNumForAdd){
//			addLevel();
//			nowSecNumForAdd +=ADD_WARE_DURTION;
//		}
//		countdownNum.draw(canvas);
//		for (GoodBullets bullets:goodBullets){
//			bullets.draw(canvas);
//		}
//		for (HugeWareBullets bullets:hugeWare){
//			bullets.draw(canvas);
//		}
//		for (TrackedBullets bullets:trackedBullets){
//			bullets.draw(canvas);
//		}
//		//auto delete the bullets out of the screen && arrival goal place
//		if(!hugeWareShouldDead.isEmpty()){
//			for (HugeWareBullets bullets:hugeWareShouldDead){
//				hugeWare.remove(bullets);
//			}
//		}
//		if(!trackedBulletsShouldDead.isEmpty()){
//			for (TrackedBullets bullets:trackedBulletsShouldDead){
//				trackedBullets.remove(bullets);
//			}
//		}
//		
//		mainCir.draw(canvas);
//		if(running){
//			postInvalidate();
//		}
//		//when knock change size bullets
//		if(mainCir.getRadius()<MAIN_RADIUS){
//			mainCir.setRadius(mainCir.getRadius()+1);
//			if (mainCir.getRadius()<MAIN_RADIUS+6) {
//				mainCir.setRadius(mainCir.getRadius()+2);
//			}
//		}else if(mainCir.getRadius()>MAIN_RADIUS){
//			mainCir.setRadius(mainCir.getRadius()-1);
//		}
//
//	}
//
//	private void addLevel(){
//		if(isAddware){
//			addWareNum++;
//		}else{
//			addSpeed();
//		}
//		addWare();
//		isAddware = !isAddware;
//	}
//	
//	private void addSpeed(){
//		redSpeed+=0.15;
//		orangeSpeed+=1.0;
//	}
//	
//	private void addWare(){
//		for(int i=0; i<addWareNum; i++){
//			hugeWare.add(new HugeWareBullets(3,redSpeed));
//			hugeWare.get(hugeWare.size()-1).setKnockListener(this, mainCir);
//			hugeWare.get(hugeWare.size()-1).setOnDeadListener(this);
//			trackedBullets.add(new TrackedBullets(1,mainCir,orangeSpeed));
//			trackedBullets.get(trackedBullets.size()-1).setKnockListener(this, mainCir);
//			trackedBullets.get(trackedBullets.size()-1).setOnDeadListener(this);
//		}
//		if(countdownNum.getNowSecond()>5&&countdownNum.getNowSecond()%2 == 0){
//			goodBullets.add(new GoodBullets(1, mainCir, 6));
//			goodBullets.get(goodBullets.size()-1).setKnockListener(this, mainCir);
//		}
//		
//	}
//	
//	@Override
//    public boolean onTouchEvent(MotionEvent e) {
//        float x = e.getX();
//        float y = e.getY();
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                float dx = x - mPreviousX;
//                float dy = y - mPreviousY;
//                mainCir.setPosition(mainCir.getX() + dx,
//                		mainCir.getY() + dy);
//                if(running){
//                	postInvalidate();
//                }
//                
//        }
//
//        mPreviousX = x;
//        mPreviousY = y;
//        return true;
//    }
//
//
//	public void setOnGameOverListener(onGameOverListener listener){
//		gameOverListener = listener;
//	}
//	@Override
//	public void onCirKnock(GameUtils.KnockFunction function) {
//		// TODO Auto-generated method stub
//		if(countdownNum.getNowSecond() >1){
//		switch (function) {
//		case DEAD:
//			if(running){
//				countdownNum.gameOver();
//				running = false;
//				gameOverListener.onGameOver(countdownNum.getNowSecond());
//			}
//			break;
//		case ADD_SIZE:
//			MAIN_RADIUS = mainCir.getRadius()+MyUtils.autoSetValue4DifferentScreen(5);
//			break;
//		case REDUCE_SIZE:
//			MAIN_RADIUS = mainCir.getRadius()-MyUtils.autoSetValue4DifferentScreen(5);
//			break;
//		default:
//			break;
//		}
//			
//		}
//	}
//
//
//	@Override
//	public void onCirDead(MiniCir miniCir) {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//	@Override
//	public void onCirDead(MyFatherBullets bullets) {
//		// TODO Auto-generated method stub
//		if(bullets instanceof HugeWareBullets){
//			hugeWareShouldDead.add((HugeWareBullets)bullets);
//		}else if(bullets instanceof TrackedBullets){
//			trackedBulletsShouldDead.add((TrackedBullets)bullets);
//		}
//	}
//
//
//
//}
