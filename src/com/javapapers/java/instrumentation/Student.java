package com.javapapers.java.instrumentation;

public class Student {
	private String name = "Dian Li";
	private String program = "software engineering";
	String level = "master";
	private int mathGrade = 98;
	private int algGrade = 96;
	private int softwareTestingGrade = 100;

	public void display() throws InterruptedException {
		System.out.println("My name is " + this.name);
		System.out.println("I am a " + this.program + "student");
		System.out.println("I am in " + this.level + "program");
		System.out.println("My avarage grades is "+ (this.mathGrade + this.algGrade + this.softwareTestingGrade) / 3);
		Thread.sleep(2000L);
	}

}
