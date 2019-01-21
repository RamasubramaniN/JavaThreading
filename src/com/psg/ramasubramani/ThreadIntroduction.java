package com.psg.ramasubramani;

import java.util.concurrent.TimeUnit;

public class ThreadIntroduction {

	public static void main(String[] args) throws Exception{
		
		System.out.println("Preparing Rocket Launch");
		
		RocketLaunch rocketLaunch = new RocketLaunch();
		Thread thread = new Thread(rocketLaunch);
		//thread.setDaemon(true);
		thread.start();
		thread.join();
		
		System.out.println("Rocket Launched");
	}

}

class RocketLaunch implements Runnable{
	
	@Override
	public void run() {
		for(int i=10; i>0; i--){
			try {
				System.out.println("***\t" + i + "\t***");
				TimeUnit.SECONDS.sleep(1L);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
