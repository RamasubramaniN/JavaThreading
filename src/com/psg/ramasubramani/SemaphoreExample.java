package com.psg.ramasubramani;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author nramasubramani
 * Semaphore - Max of n threads can access the shared resource at a time. 
 * Solve below problem Using semaphore.
 * One ATM center has 3 ATM instances and 14 ppl are waiting. Simultaneously 3 ppl can access ATM.
 */
public class SemaphoreExample {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		//Only one bowling court is available. One team can play at a time.
		//System.out.println("***** There is only one bowling court *****");
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		try {
			Lock bowlingCourt = new ReentrantLock();
			// Only one bowling court is available. One team can play at a time.
			playSingleCourtPresent(bowlingCourt, executorService);
			Thread.sleep(15000);
		} finally {
			executorService.shutdownNow();
		}

		System.out.println("\n\n");
		System.out.println("***** There are multiple bowling courts *****");
		
		//Max of 2 threads can access the shared resource at a time.
		Semaphore multiBowlingCourt = new Semaphore(2); 
		try {
			playMultiCourtPresent(multiBowlingCourt);
			Thread.sleep(15000);
		} finally {
			executorService.shutdownNow();
		}
	}

	//Dont use static methods. This is created just to explain the concept.
	private static void playSingleCourtPresent(Lock bowlingCourt, ExecutorService executorService) {
		
		Group group1 = new Group("Group1", bowlingCourt);
		Group group2 = new Group("Group2", bowlingCourt);
		Group group3 = new Group("Group3", bowlingCourt);
		Group group4 = new Group("Group4", bowlingCourt);
		Group group5 = new Group("Group5", bowlingCourt);
		
		executorService.submit(group1);
		executorService.submit(group2);
		executorService.submit(group3);
		executorService.submit(group4);
		executorService.submit(group5);
	}
	
	private static void playMultiCourtPresent(Semaphore bowlingCourt) {
		
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		
		Team group1 = new Team("Team1", bowlingCourt);
		Team group2 = new Team("Team2", bowlingCourt);
		Team group3 = new Team("Team3", bowlingCourt);
		Team group4 = new Team("Team4", bowlingCourt);
		Team group5 = new Team("Team5", bowlingCourt);
		
		executorService.submit(group1);
		executorService.submit(group2);
		executorService.submit(group3);
		executorService.submit(group4);
		executorService.submit(group5);
	}

}

class Group implements Callable<Void> {

	private String groupName;
	private Lock bowlingCourt;
	
	//Each callable is a task. Same lock is passed for all tasks.
	public Group(String groupName, Lock bowlingCourt){
		this.groupName = groupName;
		this.bowlingCourt = bowlingCourt;
	}

	@Override
	public Void call() {

		bowlingCourt.lock(); //equivalent to synchronized(this). Start of synchronization block.
		System.out.println(groupName + " acquired the lock");
		try {
			play();
		} catch (InterruptedException e) {
			System.out.println("Thread is interrupted");
		} finally {
			System.out.println(groupName + " releasing the lock");
			bowlingCourt.unlock();//end of synchronization block.
		}
		
		return null;
		
	}

	private void play() throws InterruptedException {
		
		System.out.println(groupName + " is currently using bowling court");
		Thread.sleep(1000); //Play for 1 seconds
		
	}

}


class Team implements Callable<Void> {

	private String teamName;
	private Semaphore bowlingCourt;
	
	public Team(String teamName, Semaphore bowlingCourt){
		this.teamName = teamName;
		this.bowlingCourt = bowlingCourt;
	}

	@Override
	public Void call() {

		try {
			bowlingCourt.acquire();
			System.out.println(teamName + " acquired the lock");
			play();
		} catch (InterruptedException e) {
			System.out.println("Thread is interrupted");
		} finally {
			bowlingCourt.release();
			System.out.println(teamName + " released the lock");
		}
		
		return null;
		
	}

	private void play() throws InterruptedException {
		
		System.out.println(teamName + " is currently using bowling court");
		Thread.sleep(5000); //Play for 5 seconds
		
	}

}
