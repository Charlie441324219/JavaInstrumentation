package com.javapapers.java.instrumentation;

public class TestInstrumentation {
	public static void main(String args[]) throws InterruptedException {
		Test d = new Test();
		System.out.println("TEST CASE 1: x = 100, y = 150 " );
		d.add(100,150);
		System.out.println("TEST CASE 2: x = 8, y = 150 " );
		d.add(8,150);
		System.out.println("TEST CASE 3: x = 20, y = 150 " );
		d.add(20,150);
	}
}
