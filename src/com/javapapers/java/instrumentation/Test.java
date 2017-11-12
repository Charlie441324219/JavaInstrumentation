package com.javapapers.java.instrumentation;

public class Test {
	public int ans;

	public void add(int x, int y) throws InterruptedException {
		if (x > 10) {
			this.ans = x / 10 + y;
		}
		else {
			this.ans = x + y;
		}
		System.out.println("ans = " + ans);
		Thread.sleep(2000L);
	}
}
