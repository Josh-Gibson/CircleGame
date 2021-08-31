package jggames;

import javax.swing.JFrame; 


public class CircleGame{
	public static void main(String args[]){
		System.out.println("fugger");
		  
		JFrame frame = new JFrame("Circle Game");
		frame.setContentPane(new Game());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
}