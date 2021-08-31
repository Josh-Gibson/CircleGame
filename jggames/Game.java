package jggames;

//imports
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener; 
import java.awt.event.KeyEvent;  
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;
 

public class Game extends JPanel{

//gameloop stuff
static final int WIDTH = 900;
static final int HEIGHT = 900;
static final int FPS = 60;
static final int TOTAL_LEVELS = 30;
static final long UPDATES_NSEC = 1000000000L / FPS;
static enum GameState{MENU, PLAYING, PAUSED, WIN, GAMEOVER}	//control gamestate 

//interface stuff
static GameState state; 
public GamePanel canvas;
public JLabel outputLabel;
public JScrollPane scroll;

String output;

//timer stuff
public Timer timer;
public TimerTask task;
public long time = 1000L * 60;
 
 //options
public String GAMEMODE = "elimination";
public boolean ENABLE_COLLISIONS = true;
public int CIRCLE_AMOUNT = 20;
public int CIRCLE_RADIUS = 1;
public double CIRCLE_SPACING = 1;
public double SPEED = 1;
public boolean TIMER_ENABLED = false;
public int TIME_MINUTES = 3;

private FileLoader fileLoader = new FileLoader();

//circle resources
public Circle circleTrack[];
public Collisions collisionManager = new Collisions();

public Circle winner;

private boolean firstLaunch;

	public Game(){
		init();
	}
	public void buildUI(){
		canvas = new GamePanel();
      	canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		add(canvas);
		
		JPanel wrap = new JPanel();
		wrap.setPreferredSize(new Dimension(200, 450));
		wrap.setBackground(Color.WHITE);
		
		//thank you Oracle for the helpful BoxLayout tutorial
		//https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html
		
		wrap.setLayout(new BoxLayout(wrap, BoxLayout.PAGE_AXIS));
		wrap.setOpaque(true);
		
		outputLabel = new JLabel();
		outputLabel.setText("default");
		
		scroll = new JScrollPane(outputLabel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		wrap.add(scroll);
		add(wrap);
	}
	public void init(){	//Create Game and set options 
	
		buildUI();
		
		System.out.println("Game Launched");
		fileLoader.readFile("options.txt"); 
		
		GAMEMODE = fileLoader.gamemode;
		ENABLE_COLLISIONS = fileLoader.enablePhysicsCollisions;
		CIRCLE_AMOUNT = fileLoader.circleAmount;
		CIRCLE_RADIUS = fileLoader.circleRadius;
		CIRCLE_SPACING = CIRCLE_RADIUS * fileLoader.circleSpacing;
		SPEED = fileLoader.speed;
		TIMER_ENABLED = fileLoader.timerEnabled;
		TIME_MINUTES = fileLoader.timeMinutes;
		 
		circleTrack = new Circle[CIRCLE_AMOUNT];
		 
		collisionManager.gamemode = GAMEMODE;
		
		timer = new Timer();
		task = new TimerTask(){
			public void run(){
				if(TIMER_ENABLED){
					gameEnd();
				}
			}
		};
		
		time *= TIME_MINUTES;
		timer.schedule(task, time);
		
			
		int row = 0;
		int column = 0; 
		 
		for(int i = 0; i < CIRCLE_AMOUNT; i++){
		   
			if((CIRCLE_RADIUS + CIRCLE_SPACING)*row > 900 - CIRCLE_RADIUS){
				row = 0;
				column ++;
			}
		   
			Circle ccc = new Circle((CIRCLE_RADIUS + CIRCLE_SPACING) * row, (CIRCLE_RADIUS + CIRCLE_SPACING) * column, CIRCLE_RADIUS);
			 
			circleTrack[i] = ccc;
			circleTrack[i]. id = i;
			circleTrack[i].vel.x = Math.random() * SPEED;
			circleTrack[i].vel.y =  Math.random() * SPEED;
			
			row++;
		}
		gameStart(); 
	 
	}  
	public void gameStart(){	//start gameloop
		
		state = GameState.PLAYING;
			
		Thread thread = new Thread(){
			@Override
			
			public void run(){
				gameLoop();
			} 
		};
		thread.start(); 
	}
	public void gameLoop(){	//60FPS loop 
		long beginTime, elapsedTime, timeDifference;
		while(state != GameState.GAMEOVER){
			beginTime = System.nanoTime();
			if(state == GameState.PLAYING){
				gameUpdate(); 
			}
			repaint();	
  
			elapsedTime = System.nanoTime() - beginTime; 
			timeDifference = (UPDATES_NSEC - elapsedTime) / 1000000;  
			if(timeDifference < 10){ 
				timeDifference = 10;
			} 
			try{ 
				Thread.sleep(timeDifference); 
			}
			catch(InterruptedException e){}
		} 
	}
	public void gameUpdate(){
		
		output = "";
		
		switch(state){
			case PLAYING:
				break;
			case GAMEOVER: 
				return;
		}
		
		int count = 0;
		int count2 = 0;
		
		for(int i = 0; i < CIRCLE_AMOUNT; i++){

			if(GAMEMODE.equals("extinction")){
				if(circleTrack[i].id == circleTrack[0].id && circleTrack[i] != circleTrack[0]){
				
					count++;
					
					if(count == CIRCLE_AMOUNT -1){
						gameEnd();
					}
				}
				else{
					winner = circleTrack[i];	
				}
				
				for(int x = 0; x < circleTrack.length; x++){
					
					if(i == circleTrack[x].id){
						count2++;
						
						if(count2 > 0){
							output += " ALIVE : Team " + i + "\n";
							break;
						}
					}
				}
			}
			else if(GAMEMODE.equals("elimination")){
				
				
				if(circleTrack[i].isDead){
				count++;
						
					if(count == CIRCLE_AMOUNT -3){
						collisionManager.suddenDeath = true;
					}			
					
					if(count == CIRCLE_AMOUNT -1){
						gameEnd();
					}
				}
				else{
					winner = circleTrack[i];	
				}
			output = collisionManager.sendMessage();
			}
			 
			
			for(int j = 0; j < CIRCLE_AMOUNT; j++){
				if(collisionManager.checkCollision(circleTrack[i],circleTrack[j]) && ENABLE_COLLISIONS){
					
					
					//courtesy of javidx9
					double distance = Math.sqrt((circleTrack[i].center.x - circleTrack[j].center.x) * (circleTrack[i].center.x - circleTrack[j].center.x) + (circleTrack[i].center.y - circleTrack[j].center.y) * (circleTrack[i].center.y - circleTrack[j].center.y));
					double overlap = .5 * (distance - circleTrack[i].getRadius() - circleTrack[j].getRadius());
					
					circleTrack[i].center.x -= overlap * (circleTrack[i].center.x - circleTrack[j].center.x) / distance;
					circleTrack[i].center.y -= overlap * (circleTrack[i].center.y - circleTrack[j].center.y) / distance;
					
					circleTrack[j].center.x += overlap * (circleTrack[i].center.x - circleTrack[j].center.x) / distance;
					circleTrack[j].center.y += overlap * (circleTrack[i].center.y - circleTrack[j].center.y) / distance;
				}
			}
			circleTrack[i].updateCircle();
			
		}
		if(GAMEMODE.equals("extinction")){
			output += "\n Teams Left: " + count2;	
		//BIG credit to TheSola10 for this awesomeness!
		//https://stackoverflow.com/questions/1090098/newline-in-jlabel
		
		}
		if(state !=GameState.GAMEOVER){
			outputLabel.setText("<html><p>Output</p><br/>" + output.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
		}
	}
	public void gameDraw(Graphics2D g2){	//draw everything within here. 
		setBackground(new Color(77, 0, 0)); 
		
		g2.setColor(Color.BLACK);
		g2.drawRect(0,0,899,899);
		
		for(int i = 0; i < CIRCLE_AMOUNT; i++){
			circleTrack[i].drawCircle(g2);
		}
		
		switch(state){
			case PAUSED:
			
				g2.setColor(Color.WHITE);
				g2.setFont(new Font("Arial", Font.PLAIN, 50));
				g2.drawString("PAUSED", 400, 400);
				
				break;
			case GAMEOVER:
				
				g2.setColor(Color.WHITE);
				g2.setFont(new Font("Arial", Font.PLAIN, 50));
				g2.drawString("GAME OVER", 400, 400);
				
				break;
		}
		
	}

	public void gameEnd(){
		
		state = GameState.GAMEOVER;
		
		if(GAMEMODE.equals("elimination")){
		
			String status;
			
			System.out.println("\n___________________");
			System.out.println("\n GAME OVER! \n");
			System.out.println(winner.id + " is the winner! \n");
			System.out.println("KILLS:");
			System.out.println("___________________");
			
			
			output += "\n___________________ \n GAME OVER! \n " + winner.id + " is the winner! \n KILLS: \n ___________________ \n ";
			
			for(int i = 0; i < CIRCLE_AMOUNT; i++){
				if(circleTrack[i].isDead){
					status = "KILLED";
				}
				else{
					status = "SURVIVED";
				}
				System.out.println(circleTrack[i].id + " Kills: " + circleTrack[i].kills + " | Status: " + status);
				output += circleTrack[i].id + " Kills: " + circleTrack[i].kills + " | Status: " + status + "\n";
			}
		}
		if(GAMEMODE.equals("extinction")){
			
			String status;
			
			System.out.println("\n___________________");
			System.out.println("\n GAME OVER! \n");
			System.out.println("Team " + winner.id + " is has destroyed their competition! \n");
			System.out.println("RESULTS:");
			System.out.println("___________________");
			
			
			output += "\n___________________ \n GAME OVER! \n Team " + winner.id + " is the winner! \n RESULTS: \n ___________________ \n ";
			
			for(int i = 0; i < CIRCLE_AMOUNT; i++){
				if(i != winner.id){
					status = "EXTINCT";
				}
				else{
					status = "DOMINATED";
				}
				System.out.println("Team " + i + " | Status: " + status);	
				output += "Team " + i + " | Status: " + status + "\n";
			}
		}
		
		//and thank you to camickr for providing this method, minor changes by me
		//https://stackoverflow.com/questions/5147768/scroll-jscrollpane-to-bottom/5150437
		
		
		outputLabel.setText("<html><p>Output</p><br/>" + output.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
			
	}
	
	class GamePanel extends JPanel implements KeyListener{	//Key Listener
		public GamePanel(){
			setFocusable(true);
			requestFocus();
			addKeyListener(this);
		}
		@Override
		public void paintComponent(Graphics g){

			Graphics2D g2 = (Graphics2D) g;
			gameDraw(g2);
		}
		@Override
		public void keyPressed(KeyEvent e){
			
			if(e.getKeyCode() == KeyEvent.VK_Q){
				if(state != GameState.GAMEOVER){
					gameEnd();	
					return;
				}
				System.exit(1);
			}
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				if(state == GameState.PAUSED){
					state = GameState.PLAYING;
					return;
				}
				state = GameState.PAUSED;
			}
		}
		@Override
		public void keyReleased(KeyEvent e){
		}
		@Override
		public void keyTyped(KeyEvent e){}
	}
}
