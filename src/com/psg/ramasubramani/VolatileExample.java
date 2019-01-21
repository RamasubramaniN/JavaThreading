/**
 * 
 */
package com.psg.ramasubramani;

import java.util.concurrent.TimeUnit;

/**
 * @author RAMASUBRAMANIN
 *
 */
public class VolatileExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		Volatile volatileInstance = new Volatile();
		Thread thread = new Thread(volatileInstance);
		
		thread.start();

		thread.join();
		
		int a = 1;
		int b = 2;
		
		while(!volatileInstance.isReady()){
			a = 100;
			b = 200;
		}
		
		System.out.println(volatileInstance.isReady());
		System.out.println(a);
		System.out.println(b);
	}
	

}

class Volatile implements Runnable {
	
	private boolean ready = false;

	@Override
	public void run() {
		ready = true;
	}

	public boolean isReady() {
		return ready;
	}

}
