package dacer.planefighter;

import android.content.Context;
import dacer.planefighter.GameUtils.KnockFunction;


/**
 * Author:dacer
 * Date  :Jul 28, 2013
 */
public class HugeWareBullets extends MyFatherBullets{
    
	public HugeWareBullets(int bulletNUM,float speed,Context c) {
		// TODO Auto-generated constructor stub
		super(bulletNUM,speed);
		for(int i=0; i<bulletNUM; i++){
			double random1 = Math.random();
			double random2 = Math.random();
			double randomSpeed = speed*(Math.random()*0.3+0.75);//From 85% - 115%
			boolean fromLeft = Math.random()<0.5;
			float fromX = (float) (fromLeft?0:screenWidth);
			float fromY = (float) (random1*(screenHeight+2*screenWidth)-screenWidth);
			float toX = (float) (fromLeft?screenWidth+20:-20);
			float toY;
			if(fromY<0){
				toY = (float) (random2*(screenHeight+screenWidth));
			}else if(fromY>screenHeight){
				toY = (float) (random2*(screenHeight+screenWidth)-screenWidth);
			}else{
				toY = (float) (random2*(screenHeight+2*screenWidth)-screenWidth);
				fromX = (float) (fromLeft?-200:screenWidth+200);
			}
			
			miniCir[i]=new MiniCir(
					new float[]{fromX,
								fromY,
								toX,
								toY}, 
					(float)randomSpeed,c);
			miniCir[i].setOnDeadListener(this);
			miniCir[i].setAutoDeadAfterArrival(true);
		}
	}
	
	@Override
	protected void whenMyKnock(int i){
		knockListener.onCirKnock(KnockFunction.DEAD);
	}
	@Override
	protected void whenDead(){
		DEAD = true;
		deadListener.onCirDead(this);
	}

}
