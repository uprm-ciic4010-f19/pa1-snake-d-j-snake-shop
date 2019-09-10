package Game.Entities.Static;

import java.awt.*;
import Game.Entities.Dynamic.Player;
import Main.Handler;
import Worlds.WorldBase;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {

	private Handler handler;

	public int xCoord;
	public int yCoord;
//	public boolean appleIsGood = true;
//	public boolean appleIsBad = false; //New variable to test if the apple is a bad one Dilan Rios
	public double score;
	public double decrease;
	private Player player;

	private Graphics g;


	public Apple(Handler handler,int x, int y){
		this.handler=handler;
		this.xCoord=x;
		this.yCoord=y;
	}



	public boolean isGood() {
//		System.out.println((handler.getWorld().player.moveCounter));
		if (handler.getWorld().player.stepCounter > 55) {
			return false;
		}
		
		return true;

	}
}
