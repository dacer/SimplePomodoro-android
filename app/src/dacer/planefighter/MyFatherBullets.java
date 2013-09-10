package dacer.planefighter;

import android.graphics.Canvas;
import dacer.utils.MyUtils;

/**
 * Author:dacer
 * Date  :Jul 29, 2013
 */
public abstract class MyFatherBullets implements onDeadListener{
	protected onKnockListener knockListener;
	protected MiniCir[] miniCir;
	protected int bulletNUM = 20;
	protected double screenHeight;
	protected double screenWidth;
	protected float mSpeed;
	protected boolean DEAD = false;
	protected int cirDeadNUM = 0;
	protected MyBigCir mMainCir;
	protected onDeadListener deadListener;
	
	public MyFatherBullets(int bulletNUM,float speed) {
		// TODO Auto-generated constructor stub
		mSpeed = speed;
		screenHeight = MyUtils.getScreenHeight();
		screenWidth = MyUtils.getScreenWidth();
		this.bulletNUM = bulletNUM;
		miniCir = new MiniCir[bulletNUM];
	}
	
	public void draw(Canvas canvas){
		if(!DEAD){
			for(int i=0; i<bulletNUM; i++){
				miniCir[i].draw(canvas);
				if(miniCir[i].isKnocked(mMainCir.getX(), mMainCir.getY(), mMainCir.getRadius())){
					whenMyKnock(i);
				}
			}
			if(cirDeadNUM == bulletNUM){
				whenDead();
			}
		}
		
	}
	
	
	protected abstract void whenDead();
	
	protected abstract void whenMyKnock(int i);
	
	public boolean isDead(){
		return DEAD;
	}
	
	public void setSpeed(float speed){
		this.mSpeed = speed;
	}
	
	public void setBulletNum(int num){
		bulletNUM = num;
	}
	public void setKnockListener(onKnockListener listener,MyBigCir mainCir){
		knockListener = listener;
		mMainCir = mainCir;
	}

	public void setOnDeadListener(onDeadListener listener){
		deadListener = listener;
	}
	
	@Override
	public void onCirDead(MiniCir miniCir) {
		// TODO Auto-generated method stub
		cirDeadNUM++;
	}

	@Override
	public void onCirDead(MyFatherBullets bullets) {
		// TODO Auto-generated method stub
		
	}
}
