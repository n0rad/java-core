/**
 *
 *     Copyright (C) Awired.net
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package fr.norad.core.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericSignatureFormatError;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class ReflectTools {

    private ReflectTools() {
    }

    public static <T> T createInstance(final Class<T> clazz) throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        T instanceToReturn = null;
        Class<?> enclosingClass = clazz.getEnclosingClass();
        if (enclosingClass != null) {
            Object instanceOfEnclosingClass = createInstance(enclosingClass);
            Constructor<T> ctor = clazz.getConstructor(enclosingClass);
            if (ctor != null) {
                instanceToReturn = ctor.newInstance(instanceOfEnclosingClass);
            }
        } else {
            instanceToReturn = clazz.newInstance();
        }
        return instanceToReturn;
    }

    public static boolean classImplement(Class<?> clazz, Class<?> implement) {
        Class<?> current = clazz;
        while (current != null) {
            if (current.equals(implement)) {
                return true;
            }
            Class<?>[] interfaces = current.getInterfaces();
            for (Class<?> class1 : interfaces) {
                if (classImplement(class1, implement)) {
                    return true;
                }
            }
            current = current.getSuperclass();
        }
        return false;
    }

    public static Class<?> getClassDeclaredInSuperClassGeneric(Object extendedObjWithDeclaredGenerics, int genericPos)
            throws GenericSignatureFormatError, TypeNotPresentException, MalformedParameterizedTypeException,
            MalformedParameterizedTypeException {
        ParameterizedType parameterizedType = (ParameterizedType) extendedObjWithDeclaredGenerics.getClass()
                .getGenericSuperclass();
        return (Class<?>) parameterizedType.getActualTypeArguments()[genericPos];
    }

    public static Set<Annotation>[] findParametersAnnotation(Method method) {
        // Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method); // uses Spring
        if (method == null || method.getParameterTypes() == null) {
            return null;
        }

        // prepare res
        Set<Annotation>[] res = new Set[method.getParameterTypes().length];
        for (int i = 0; i < res.length; i++) {
            res[i] = new HashSet<Annotation>();
        }

        // current method annotations
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            res[i].addAll(Arrays.asList(parameterAnnotations[i]));
        }

        // inherited from extends
        Class<?> superclass = method.getDeclaringClass().getSuperclass();
        methodParameterFinderOnClass(method, res, superclass);

        // inherited from interfaces
        for (Class<?> class1 : method.getDeclaringClass().getInterfaces()) {
            methodParameterFinderOnClass(method, res, class1);
        }

        return res;
    }

    private static void methodParameterFinderOnClass(Method method, Set<Annotation>[] res, Class<?> clazz) {
        try {
            Method method2 = clazz.getMethod(method.getName(), method.getParameterTypes());
            if (method2 != null) {
                Annotation[][] parameterAnnotations2 = method2.getParameterAnnotations();
                methodParameterAdderIfNotSet(parameterAnnotations2, res);
            }
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        }
    }

    private static void methodParameterAdderIfNotSet(Annotation[][] parameterAnnotations2, Set<Annotation>[] res) {
        for (int i = 0; i < parameterAnnotations2.length; i++) { // loop on parameters
            Annotation[] annotations = parameterAnnotations2[i];

            for (Annotation annotation : annotations) { // loop on found param annotations
                boolean flag = false;
                for (Annotation alreadySaved : res[i]) { // loop on saved
                    if (alreadySaved.annotationType().equals(annotation.annotationType())) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) { // skip annotation if already found in more specific definition
                    res[i].add(annotation);
                }
            }

        }
    }

}
