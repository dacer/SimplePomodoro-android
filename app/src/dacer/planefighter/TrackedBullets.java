package dacer.planefighter;

import dacer.planefighter.GameUtils.KnockFunction;
import dacer.utils.MyUtils;
import android.graphics.Color;

/**
 * Author:dacer
 * Date  :Jul 28, 2013
 */
public class TrackedBullets extends MyFatherBullets{
    
	public TrackedBullets(int bulletNUM,MyBigCir mainCir,float speed) {
		// TODO Auto-generated constructor stub
		super(bulletNUM,speed);
		for(int i=0; i<bulletNUM; i++){
			double random1 = Math.random();
			double random2 = Math.random();
			double random6 = Math.random();
			double randomSpeed = speed*(Math.random()*0.3+0.75);//From 85% - 115%
			boolean fromLeft = Math.random()<0.5;
			float fromX = (float) (fromLeft?-300:screenWidth+300);
			float fromY = (float) (random1*(screenHeight+2*screenWidth)-screenWidth);
			miniCir[i]=new MiniCir(
					new float[]{fromX,
							fromY,
							(float) (mainCir.getX()+MyUtils.getScreenWidth()/4*random2),
							(float) (mainCir.getY()+MyUtils.getScreenHeight()/4*random6)}, 
					(float)randomSpeed);
			miniCir[i].setColor(Color.parseColor("#AA66CC"));
			miniCir[i].setOnDeadListener(this);
		}
	}
	
	@Override
	protected void whenMyKnock(int i){
		knockListener.onCirKnock(KnockFunction.ADD_SIZE);
		miniCir[i].setDead();
	}
	@Override
	protected void whenDead(){
		DEAD = true;
		deadListener.onCirDead(this);
	}

}
