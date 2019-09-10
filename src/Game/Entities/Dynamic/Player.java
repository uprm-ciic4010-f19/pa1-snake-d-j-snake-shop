package Game.Entities.Dynamic;

import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

import Game.GameStates.State;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

	public int lenght;
	public boolean justAte;
	private Handler handler;

	public int xCoord;
	public int yCoord;
	public  double score; //JM

	public int moveCounter;
	public int stepCounter;

	public int i; //JM

	public boolean appleIsGood; //JM

	public String direction;//is your first name one?
	private Tail block;

	public Player(Handler handler){
		this.handler = handler;
		xCoord = 0;
		yCoord = 0;
		moveCounter = 0;
		direction= "Right";
		justAte = false;
		lenght= 1;
		i= 10; //JM
		score = 0;
		appleIsGood = true; //JM

	}

	public void tick(){

		moveCounter++;
		if(moveCounter >= i) {
			checkCollisionAndMove();
			stepCounter++;
			moveCounter=0;

		}
		//Phase 3: Added the direction condition to prevent backtracking. JM
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) && direction !="Down"){
			direction="Up";
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) && direction != "Up"){
			direction="Down";
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) && direction != "Right"){
			direction="Left";
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) && direction != "Left"){
			direction="Right";
		}

		//Phase 2: Adds a piece of tail when you press 'N'. Javier M.
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)) {
			Tail tail=null;
			tail=new Tail(xCoord, yCoord, handler);
			handler.getWorld().body.addLast(tail);
			handler.getWorld().playerLocation[tail.x][tail.y] = true;
		}
		//Phase 2: Increases speed when you press '+' and decreases when '-' is pressed. JM
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)) {
			i--;
		}

		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)) {
			i++;
		}

		//Phase 3: Pause State. JM
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)){
			State.setState(handler.getGame().pauseState);

		}







	}

	public void checkCollisionAndMove(){
		handler.getWorld().playerLocation[xCoord][yCoord]=false;
		int x = xCoord;
		int y = yCoord;
		switch (direction){
		case "Left":
			if(xCoord==0){
				kill();
				xCoord=handler.getWorld().GridWidthHeightPixelCount-1; //P3: Snake teleports to the opposite side. JM
			}else{
				xCoord--;
			}
			break;
		case "Right":
			if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				kill();
				xCoord=0; //P3: Snake teleports to the opposite side. JM
			}else{
				xCoord++;
			}
			break;
		case "Up":
			if(yCoord==0){
				kill();
				yCoord=handler.getWorld().GridWidthHeightPixelCount-1; //P3: Snake teleports to the opposite side. JM
			}else{
				yCoord--;
			}
			break;
		case "Down":
			if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				kill();
				yCoord=0; //P3: Snake teleports to the opposite side. JM
			}else{
				yCoord++;
			}
			break;
		}
		handler.getWorld().playerLocation[xCoord][yCoord]=true;


		if(handler.getWorld().appleLocation[xCoord][yCoord]){ 
			Eat();

			System.out.println("Score: " + (int) score);
			i -= 1; //My student number ends with 4270, 0 is boring so I used 7 instead. JM 


		}

		if(!handler.getWorld().body.isEmpty()) {
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
			handler.getWorld().body.removeLast();
			handler.getWorld().body.addFirst(new Tail(x, y,handler));

			//Sets Game Over when snake collides with itself. JM

			for (int i = 0; i <handler.getWorld().body.size(); i++) {
				if((xCoord == handler.getWorld().body.get(i).x) && (yCoord == handler.getWorld().body.get(i).y)) {
					kill();
					State.setState(handler.getGame().gameOverState);
				}
			}


		}


	}

	public void render(Graphics g,Boolean[][] playeLocation){
		Random r = new Random();


		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				g.setColor(Color.GREEN);


				if(playeLocation[i][j]){
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);
				}
				if (handler.getWorld().appleLocation[i][j]) {
					if(handler.getWorld().apple.isGood()) {

						g.setColor(Color.green);

					} else {
						g.setColor(Color.red);
					}	

					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);


				}



			}

		}

		g.setFont(new Font("ComicSans", Font.ROMAN_BASELINE, 40)); //implemented the score board on screen. JM
		g.setColor(Color.YELLOW);                                
		g.drawString("Score: "+ (int) handler.getWorld().player.score, 70, 30);



	}

	public void Eat(){
		lenght++;
		Tail tail= null;
		handler.getWorld().appleLocation[xCoord][yCoord]=false;
		handler.getWorld().appleOnBoard=false;

		switch (direction){
		case "Left":
			if(handler.getWorld().body.isEmpty()){
				if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail = new Tail(this.xCoord+1,this.yCoord,handler);
				}else{
					if(this.yCoord!=0){
						tail = new Tail(this.xCoord,this.yCoord-1,handler);
					}else{
						tail =new Tail(this.xCoord,this.yCoord+1,handler);
					}
				}
			}else{
				if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
				}else{
					if(handler.getWorld().body.getLast().y!=0){
						tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
					}else{
						tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

					}
				}

			}
			break;
		case "Right":
			if( handler.getWorld().body.isEmpty()){
				if(this.xCoord!=0){
					tail=new Tail(this.xCoord-1,this.yCoord,handler);

				}else{
					if(this.yCoord!=0){
						tail=new Tail(this.xCoord,this.yCoord-1,handler);
					}else{
						tail=new Tail(this.xCoord,this.yCoord+1,handler);
					}
				}
			}else{
				if(handler.getWorld().body.getLast().x!=0){
					tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
				}else{
					if(handler.getWorld().body.getLast().y!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
					}
				}

			}
			break;
		case "Up":
			if( handler.getWorld().body.isEmpty()){
				if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=(new Tail(this.xCoord,this.yCoord+1,handler));
				}else{
					if(this.xCoord!=0){
						tail=(new Tail(this.xCoord-1,this.yCoord,handler));
					}else{
						tail=(new Tail(this.xCoord+1,this.yCoord,handler));
					}
				}
			}else{
				if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
					tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
				}else{
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
					}
				}

			}
			break;
		case "Down":
			if( handler.getWorld().body.isEmpty()){
				if(this.yCoord!=0){
					tail=(new Tail(this.xCoord,this.yCoord-1,handler));
				}else{
					if(this.xCoord!=0){
						tail=(new Tail(this.xCoord-1,this.yCoord,handler));
					}else{
						tail=(new Tail(this.xCoord+1,this.yCoord,handler));
					} System.out.println("Tu biscochito");
				}
			}else{
				if(handler.getWorld().body.getLast().y!=0){
					tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
				}else{
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}else{
						tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
					}
				}

			}

			break;
		}
		
		if (handler.getWorld().apple.isGood()) {
			handler.getWorld().body.addLast(tail);
			score += Math.sqrt(2 * score + 1);
		}else {
			if (handler.getWorld().body.size() < 1) {
				State.setState(handler.getGame().gameOverState);
			} else {
				handler.getWorld().body.removeLast();
				score -= Math.sqrt(2 * score + 1);
				if (score < 0) {
					State.setState(handler.getGame().gameOverState);
					System.out.println("Score Went Negative! Try Again!");
				}
			}
		}
		handler.getWorld().playerLocation[tail.x][tail.y] = true;
		stepCounter = 0;


	}



	public void kill(){
		lenght = 0;
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

				handler.getWorld().playerLocation[i][j]=false;

			}
		}
	}

	public boolean getJustAte() {
		return justAte;
	}

	public void setJustAte(boolean justAte) {
		this.justAte = justAte;
	}
}
