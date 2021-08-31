package jggames;

import java.awt.Color;
import java.awt.Graphics2D;	
import java.awt.Font;  

public class Circle{ 
	 
	private int radius;
	
	public boolean isDead;
	
	public Vector pos;
	public Vector center;
	public Vector vel;
	
	int id;
	int kills;
	 
	private Color color;
	
	public Circle(double x, double y, int radius){
		 
		this.pos = new Vector(x, y);
		this.radius = radius;
		this.center = new Vector(pos.x + radius, pos.y + radius);
		
		this.vel = new Vector(0, 0);
		this.color = new Color((int)(255 * Math.random()),(int)(255 * Math.random()),(int)(255 * Math.random()));
		this.isDead = false;
		
		System.out.println("Circle created");
	}
	public void drawCircle(Graphics2D g2){
		
		g2.setColor(color);
		g2.fillOval((int)center.x - radius, (int)center.y - radius, radius * 2, radius * 2);
		g2.setColor(Color.BLACK);
		
		if(!isDead){
			g2.setFont(new Font("Arial", Font.PLAIN, 20));
			g2.drawString(String.valueOf(id), (int)center.x - 5, (int)center.y + 10);
		}
		//g2.fillOval((int)center.x - radius/4, (int)center.y -radius/4, radius/2, radius/2);
	}
	public void updateCircle(){
		if(isDead){
			color = color.BLACK;
		}
		 
		checkBoundary();
		center.x += vel.x;
		center.y += vel.y;
	}
	public void checkBoundary(){
		if(center.x - radius <= 0){
			center.x = 0+radius;
			vel.x = vel.x * -1;
		}
		if(center.x + radius > 900){
			center.x = 900 - radius;
			vel.x = vel.x * -1;
		}
		if(center.y - radius <= 0){
			center.y = 0+radius;
			vel.y = vel.y * -1;
		}
		if(center.y + radius > 900){
			center.y = 900 - radius;
			vel.y = vel.y * -1;
		}
	}
	public int getRadius(){
		return radius;
	}
	public void setRadius(int radius){
		this.radius = radius;
	}
	public Color getColor(){
		return color;
	}
	public void setColor(Color color){
		this.color = color;
	}
}