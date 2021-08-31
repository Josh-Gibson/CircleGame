package jggames;

import java.awt.Color; 
 
public class Collisions{

public String gamemode;
public boolean collideWithDead = true;
public boolean suddenDeath;
public String message = "";

	//courtesy of javidx9
	public boolean checkCollision(Circle c1, Circle c2){
		
		double x = (c1.center.x - c2.center.x) * (c1.center.x - c2.center.x);
		double y = (c1.center.y - c2.center.y) * (c1.center.y - c2.center.y);
		double r = (c1.getRadius() + c2.getRadius()) * (c1.getRadius() + c2.getRadius());
		
		Vector nullVec = new Vector(0,0);
		
		if(Math.abs(x + y) <= r){
			if(c1 == c2 || c1.vel == nullVec && c2.vel == nullVec){
				return false;
			}
			
			if(gamemode.equals("extinction")){
				
				c2.setColor(c1.getColor());
				c2.id = c1.id;
				return true;
			}
			
			if(gamemode.equals("elimination")){
				
				if(c1.isDead || c2.isDead){
					/* if(collideWithDead){
						return true;	
					}
					else{
						return false;
					} */
					return true;
				}
				
				c2.isDead = true;
				c2.vel = nullVec;
				
				if(suddenDeath){
					c1.vel.x = 20;
				}
				
				c1.kills ++;
				
				
				message += "-" + c2.id + " was killed by " + c1.id + "\n";
				sendMessage();
				return true;
				
			}
			
			return true;
		}
		else{
			return false;
		}
	}
	public String sendMessage(){
		return message;
	}
}