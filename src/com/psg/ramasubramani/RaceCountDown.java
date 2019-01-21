package com.psg.ramasubramani;

class RaceCountDown implements Runnable {
	String [] timeStr = { "One","Two","Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten" }; 
	
	public void run() {
		for(int i = 9; i >= 0; i--) {
			System.out.println(timeStr[i]); 
		}
	}
	
	public static void main(String []s) {
		RaceCountDown raceTimer = new RaceCountDown();
		Thread t = new Thread( raceTimer );
		t.start();
		System.out.println("Counting !!!");
		//t.join()
		System.out.println("****************** Race started ******************");
	}
}
