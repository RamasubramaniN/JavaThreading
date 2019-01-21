package com.psg.ramasubramani;

public class TryFinallyWithReturnValue
{

	//Finally clause is always executed this case
	//When jvm encounters return statement, it executes piece of code in finally clause.
	public static void main( String[] args )
	{
		System.out.println(add(5,10));
	}

	static int add( int a, int b )
	{
		int c = 0;
		try
		{
			c = a + b;
			return c;
		}
		finally
		{
			c = 100;
			System.out.println( "call is not yet returned. Value of c : " + c );
		}
	}

}
