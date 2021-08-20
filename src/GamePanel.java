import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600; //setting screen size
	static final int SCREEN_HEIGHT = 600; //setting screen size
	static final int UNIT_SIZE = 25; //size of the objects in game
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75;
	final int x[] = new int[GAME_UNITS]; //the snake will be no bigger than the game itself, so set max to game units
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6; //start the game with 6 body parts
	int applesEaten; //initially 0
	int appleX; //basically where the apple appears on the screen, completely random 
	int appleY;
	char direction = 'R'; //have the snake begin the game by going to the right
	boolean running = false;
	Timer timer; //setting a timer
	Random random; //creating an instance of the Random class
	
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		newApple();
		running = true;
		timer =  new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running) {
//			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) { //making a grid across our game panel
//				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
//				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
//			}
			g.setColor(Color.red); //setting the color of the apple
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); //making sure the apple fills the entire grid marker
		
			for(int i = 0; i < bodyParts; i++) {
				if(i==0) {
					g.setColor(Color.green); //setting the color of the head of the snake to green
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); //filling the rectangle of the snake
				}
				else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 25));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " +applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " +applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
		
	}
	
	public void newApple() { //populate the game with a new apple 
		
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	
	public void move() {
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
			
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
			
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
			
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)){
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	
	public void checkCollisions() { //checks to see if the snake has collided with itself or the wall
		//this checks if head collides with body
		for(int i = bodyParts; i > 0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])){
				running = false;
			}
		}
		
		//checks if head touches left border
		if(x[0] < 0) {
			running = false;
		}
		
		//checks if head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		
		//checks if head touches top border
		if(y[0] < 0) {
			running = false;
		}
				
		//checks if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		//g.drawString("Score: " +applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " +applesEaten))/2, g.getFont().getSize());
		if(applesEaten < 20) {
			g.drawString("Score: " +applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " +applesEaten))/2, g.getFont().getSize());
			g.drawString("Noob!", (SCREEN_WIDTH - metrics1.stringWidth("Score: " +applesEaten))/2, SCREEN_HEIGHT/4);
		}
		if(applesEaten >= 20 && applesEaten < 50) {
			g.drawString("Score: " +applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " +applesEaten))/2, g.getFont().getSize());
			g.drawString("Pretty good!!", (SCREEN_WIDTH - metrics1.stringWidth("Score: " +applesEaten))/2, SCREEN_HEIGHT/4);
		}
		if(applesEaten > 50) {
			g.drawString("Score: " +applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " +applesEaten))/2, g.getFont().getSize());
			g.drawString("Snake God", (SCREEN_WIDTH - metrics1.stringWidth("Score: " +applesEaten))/2, SCREEN_HEIGHT/4);
		}
		
		//Game Over text
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
		
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}

}
