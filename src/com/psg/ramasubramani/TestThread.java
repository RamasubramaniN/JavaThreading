package com.psg.ramasubramani;

public class TestThread implements Runnable
{
	A a = new A();
	public static void main( String[] args )
	{
		Thread thread = new Thread(new TestThread());
		thread.start();
	}

	@Override
	public void run()
	{
		a.method1();
	}
}

class A{
	public synchronized void  method1(){
		System.out.println("Method1");
		method2();
	}
	public  synchronized void method2(){
		System.out.println("Method2");
	}
}