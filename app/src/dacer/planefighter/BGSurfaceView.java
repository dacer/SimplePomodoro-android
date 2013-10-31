package dacer.planefighter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import dacer.utils.MyUtils;

/**
 * Author:dacer
 * Date  :Jul 30, 2013
 */
public class BGSurfaceView extends SurfaceView implements SurfaceHolder.Callback,onKnockListener,onDeadListener{
	private float mPreviousX;
    private float mPreviousY;
    private CountdownNum countdownNum;
    private MyBigCir mainCir;
    private ArrayList<HugeWareBullets> hugeWare;
    private ArrayList<TrackedBullets> trackedBullets;
    private ArrayList<HugeWareBullets> hugeWareShouldDead;
    private ArrayList<TrackedBullets> trackedBulletsShouldDead;
    private ArrayList<GoodBullets> goodBullets;
    private int ADD_WARE_DURTION = 3;
    private int nowSecNumForAdd = 2;
    private int addWareNum = 1;
    private boolean isAddware = true;
    private float redSpeed = 8;
    private float orangeSpeed = 12;
    private int MAIN_RADIUS;
    private boolean running = false;

    private onGameOverListener gameOverListener;
	private SurfaceHolder mHolder;
	private MainThread _thread;
	
	private Context mContext;
	
	public BGSurfaceView(Context context) {
		super(context);
		mContext = context;
		setFocusable(true);
		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		_thread = new MainThread();
		_thread.setRunning(true);
		_thread.start();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		_thread.setRunning(false);
	}

	@Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                mainCir.setPosition(mainCir.getX() + dx,
                		mainCir.getY() + dy);
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }


	
	@Override
	public void onCirKnock(GameUtils.KnockFunction function) {
		if(countdownNum.getNowSecond() >1){
		switch (function) {
		case DEAD:
			if(running){
				countdownNum.gameOver();
				running = false;
				gameOverListener.onGameOver(countdownNum.getNowSecond());
			}
			break;
		case ADD_SIZE:
//			MAIN_RADIUS = mainCir.getRadius()+MyUtils.autoSetValue4DifferentScreen(5);
			elasticityChangeSize(mainCir.getRadius()+(int)MyUtils.dipToPixels(mContext,5));
			break;
		case REDUCE_SIZE:
			MAIN_RADIUS = mainCir.getRadius()-(int)MyUtils.dipToPixels(mContext,5);
//			elasticityChangeSize(mainCir.getRadius()-MyUtils.autoSetValue4DifferentScreen(5));
			break;
		default:
			break;
		}
			
		}
	}


	@Override
	public void onCirDead(MiniCir miniCir) {
		
	}

	private void elasticityChangeSize(final int goalSize){
		if(goalSize > MAIN_RADIUS){
			MAIN_RADIUS = goalSize+5;
		}else{
			MAIN_RADIUS = goalSize-5;
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean run = true;
				while(run){
					if(mainCir.getRadius() == MAIN_RADIUS){
						MAIN_RADIUS = goalSize;
						run = false;
					}
				}
			}
		}).start();
		
	}

	@Override
	public void onCirDead(MyFatherBullets bullets) {
		if(bullets instanceof HugeWareBullets){
			hugeWareShouldDead.add((HugeWareBullets)bullets);
		}else if(bullets instanceof TrackedBullets){
			trackedBulletsShouldDead.add((TrackedBullets)bullets);
		}
	}

	public void setOnGameOverListener(onGameOverListener listener){
		gameOverListener = listener;
	}
	
	private void addLevel(){
		if(isAddware){
			addWareNum++;
		}else{
			addSpeed();
		}
		addWare();
		isAddware = !isAddware;
	}
	
	private void addSpeed(){
		redSpeed+=0.15;
		orangeSpeed+=0.39;
	}
	
	private void addWare(){
		for(int i=0; i<addWareNum; i++){
			hugeWare.add(new HugeWareBullets(3,redSpeed,mContext));
			hugeWare.get(hugeWare.size()-1).setKnockListener(this, mainCir);
			hugeWare.get(hugeWare.size()-1).setOnDeadListener(this);
			trackedBullets.add(new TrackedBullets(1,mainCir,orangeSpeed,mContext));
			trackedBullets.get(trackedBullets.size()-1).setKnockListener(this, mainCir);
			trackedBullets.get(trackedBullets.size()-1).setOnDeadListener(this);
		}
		if(countdownNum.getNowSecond()>5&&countdownNum.getNowSecond()%2 == 0){
			goodBullets.add(new GoodBullets(1, mainCir, 6,mContext));
			goodBullets.get(goodBullets.size()-1).setKnockListener(this, mainCir);
		}	
	}
	
//	------------------THREAD--------------------
	class MainThread extends Thread {
        public MainThread() {
            initView(getContext());
        }
 
        public void setRunning(boolean run) {
            running = run;
        }
        
        @Override
        public void run() {
            Canvas c;
            while (running) {
                c = null;
                try {
                    c = mHolder.lockCanvas();
                    synchronized (mHolder) {
                    	if(c!= null){
                        	doDraw(c);
                    	}
                    }
                } finally {
                	if(c != null){
                        mHolder.unlockCanvasAndPost(c);
                	}
                }
            }
        }
        
        private void initView(Context context){
    		countdownNum = new CountdownNum(context);
    		mainCir = new MyBigCir(MyUtils.getScreenWidth()/2, MyUtils.getScreenHeight()/3);
    		MAIN_RADIUS = (int)MyUtils.dipToPixels(context,15);
    		hugeWare = new ArrayList<HugeWareBullets>();
    		trackedBullets = new ArrayList<TrackedBullets>();
    		hugeWareShouldDead = new ArrayList<HugeWareBullets>();
    		trackedBulletsShouldDead = new ArrayList<TrackedBullets>();
    		goodBullets = new ArrayList<GoodBullets>();
    		addWare();
    	}
        
        private void doDraw(Canvas canvas){
        	canvas.drawColor(Color.WHITE);
        	if(countdownNum.getNowSecond() == nowSecNumForAdd){
    			addLevel();
    			nowSecNumForAdd +=ADD_WARE_DURTION;
    		}
    		countdownNum.draw(canvas);
    		for (GoodBullets bullets:goodBullets){
    			bullets.draw(canvas);
    		}
    		for (HugeWareBullets bullets:hugeWare){
    			bullets.draw(canvas);
    		}
    		for (TrackedBullets bullets:trackedBullets){
    			bullets.draw(canvas);
    		}
    		//auto delete the bullets out of the screen && arrival goal place
    		if(!hugeWareShouldDead.isEmpty()){
    			for (HugeWareBullets bullets:hugeWareShouldDead){
    				hugeWare.remove(bullets);
    			}
    		}
    		if(!trackedBulletsShouldDead.isEmpty()){
    			for (TrackedBullets bullets:trackedBulletsShouldDead){
    				trackedBullets.remove(bullets);
    			}
    		}
    		
    		mainCir.draw(canvas);
    		//when knock change size bullets
    		if(mainCir.getRadius()<MAIN_RADIUS){
    			mainCir.setRadius(mainCir.getRadius()+1);
    			if (mainCir.getRadius()<MAIN_RADIUS+6) {
    				mainCir.setRadius(mainCir.getRadius()+2);
    			}
    		}else if(mainCir.getRadius()>MAIN_RADIUS){
    			mainCir.setRadius(mainCir.getRadius()-1);
    		}
        }
        
        
	}
	
	
}
