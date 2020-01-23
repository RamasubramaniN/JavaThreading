/**
 * 
 */
package com.psg.ramasubramani;

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

/**
 * Volatile solves following problems.
 * 1. Visibility - Not reading from cached registers. Read from main memory.
 * Sometimes program never end due to visibility of the variables.
 * 2. Atomicity - Read Double values atomically. - c = a + b --> Add lower order byte
 * and then higher order bytes in 32 bit machine. There is a high chance some other
 * instruction executes before the second step and try to read c -> Atomic issue.
 * Volatile solves atomic issue.
 * 3. Prevents processor reordering instructions.
 *
 */
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
