package jggames;

public class Position{
	
	private double x;
	private double y;
	
	public Position(double x, double y){
		this.x = x;
		this.y = y;
	}
	public Position getPosition(){
		return this;
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public void setX(double x){
		this.x = x;
	}
	public void setY(double y){
		this.y = y;
	}
	public void addX(double x){
		this.x += x;
	}
	public void addY(double y){
		this.y += y;
	}
}