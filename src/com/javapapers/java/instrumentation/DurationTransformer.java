package com.javapapers.java.instrumentation;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

//this class will be registered with instrumentation agent
// reference: http://javapapers.com/core-java/java-instrumentation/
public class DurationTransformer implements ClassFileTransformer {
	public byte[] transform(ClassLoader loader, String className,
							Class classBeingRedefined, ProtectionDomain protectionDomain,
							byte[] classfileBuffer) throws IllegalClassFormatException {
		byte[] byteCode = classfileBuffer;

		if (className.equals("com/javapapers/java/instrumentation/Test")) {
			System.out.println("Instrumenting......");
			try {
				ClassPool classPool = ClassPool.getDefault();
				CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(
						classfileBuffer));
				CtMethod[] methods = ctClass.getDeclaredMethods();

				for (CtMethod method : methods) {
				    //insert variable
                    method.addLocalVariable("tempLineNumber", CtClass.intType);
                    //insert code before origin code
				    method.insertBefore("tempLineNumber = 0;System.out.println(\"========\");");
				    //insert code in each line of origin code
				    for(int i =1; i <=byteCode.length ; i++) {
                        method.insertAt(i, "if(Thread.currentThread().getStackTrace()[1].getLineNumber() != tempLineNumber)" +
                                "{" +
                                "System.out.println(\"execute code in line number :  \" + Thread.currentThread().getStackTrace()[1].getLineNumber());" +
                                "tempLineNumber = Thread.currentThread().getStackTrace()[1].getLineNumber();" +
                                "}");
                    }
                    //insert code after origin code
                    method.insertAfter("System.out.println(\"========\" + \"\\n\");");
				}
				byteCode = ctClass.toBytecode();
				ctClass.detach();
				System.out.println("Instrumentation complete.");
			} catch (Throwable ex) {
				System.out.println("Exception: " + ex);
				ex.printStackTrace();
			}
		}
		return byteCode;
	}
}