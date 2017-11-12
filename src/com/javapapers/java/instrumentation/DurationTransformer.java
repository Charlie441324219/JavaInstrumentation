package com.javapapers.java.instrumentation;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

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

//				count total line number in class
				String classString = ctClass.toString();
                int counter = 0;
                for( int i=0; i<classString.length(); i++ ) {
                    if( classString.charAt(i) == ';' ) {
                        counter++;
                    }
                }

				CtMethod[] methods = ctClass.getDeclaredMethods();
				for (CtMethod method : methods) {
                    method.addLocalVariable("tempLineNumber", CtClass.intType);
				    method.insertBefore("tempLineNumber = 0;System.out.println(\"========\");");
				    for(int i =1; i <=16 ; i++) {
                        method.insertAt(i, "if(Thread.currentThread().getStackTrace()[1].getLineNumber() != tempLineNumber)" +
                                "{" +
                                "System.out.println(\"execute code in line number :  \" + Thread.currentThread().getStackTrace()[1].getLineNumber());" +
                                "tempLineNumber = Thread.currentThread().getStackTrace()[1].getLineNumber();" +
                                "}");
                    }
					//method.insertAt(11,"System.out.println(\"when x <= 10 execute line \" + Thread.currentThread().getStackTrace()[1].getLineNumber());");;
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