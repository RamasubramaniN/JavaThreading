package com.psg.ramasubramani;

/**
 * @author rn5
 *
 */
public class Volatile implements Runnable{
	
	private static int someValue = 0;
	private static boolean flag = false;
	
	//Different results on different processors.
	//jcstress library.
	public static void main(String args[]) {
		new Thread(new VolatileUsage()).start();
		someValue = 100;
		flag = true;
	}

	@Override
	public void run() {
		while(!flag)
			System.out.println("I am in a loop");
		System.out.println(someValue);
	}
}
