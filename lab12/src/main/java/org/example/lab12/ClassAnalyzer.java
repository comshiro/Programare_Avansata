package org.example.lab12;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class ClassAnalyzer {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java org.example.lab12.ClassAnalyzer <fully-qualified-class-name>");
            return;
        }
        String className = args[0];
        Class<?> clazz = Class.forName(className);
        System.out.println("Class: " + clazz.getName());
        System.out.println("Methods:");
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println("  " + Modifier.toString(method.getModifiers()) + " " + method.getReturnType().getSimpleName() + " " + method.getName() + Arrays.toString(method.getParameterTypes()));
        }
        System.out.println("\nInvoking static @Test methods with no arguments:");
        for (Method method : clazz.getDeclaredMethods()) {
            boolean hasTestAnnotation = Arrays.stream(method.getDeclaredAnnotations())
                .anyMatch(a -> a.annotationType().getName().equals("org.junit.jupiter.api.Test"));
            if (Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0 && hasTestAnnotation) {
                System.out.println("Invoking: " + method.getName());
                method.setAccessible(true);
                method.invoke(null);
            }
        }
    }
}
