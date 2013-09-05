/*
 * Copyright 2008-2009 the original ������(zyc@hasor.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.context.matcher;
import java.lang.annotation.Annotation;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
/**
 * 
 * @version : 2013-8-20
 * @author ������(zyc@hasor.net)
 */
public class AopMatchers {
    private AopMatchers() {}
    /** ƥ���������루���ͻ򷽷���*/
    public static Matcher<Object> any() {
        return Matchers.any();
    }
    /** �ڣ����͡���������ƥ��ע�� */
    public static Matcher<Object> annotatedWith(final Class<? extends Annotation> annotationType) {
        return new MatcherAnnotationType(annotationType);
    }
    /** Returns a matcher which matches subclasses of the given type (as well as the given type). */
    public static Matcher<Class> subclassesOf(final Class<?> superclass) {
        return Matchers.subclassesOf(superclass);
    }
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //    /**
    //    * Returns a matcher which matches elements (methods, classes, etc.)
    //    * with a given annotation.
    //    */
    //    public static Matcher<AnnotatedElement> annotatedWith(final Annotation annotation) {
    //        return Matchers.annotatedWith(annotation);
    //    }
    //    /** Returns a matcher which matches objects equal to the given object. */
    //    public static Matcher<Object> only(Object value) {
    //        return Matchers.only(value);
    //    }
    //    /**
    //    * Returns a matcher which matches only the given object.
    //    */
    //    public static Matcher<Object> identicalTo(final Object value) {
    //        return Matchers.identicalTo(value);
    //    }
    //    /**
    //    * Returns a matcher which matches classes in the given package. Packages are specific to their
    //    * classloader, so classes with the same package name may not have the same package at runtime.
    //    */
    //    public static Matcher<Class> inPackage(final Package targetPackage) {
    //        return Matchers.inPackage(targetPackage);
    //    }
    //    /**
    //    * Returns a matcher which matches classes in the given package and its subpackages. Unlike
    //    * {@link #inPackage(Package) inPackage()}, this matches classes from any classloader.
    //    * @since 2.0
    //    */
    //    public static Matcher<Class> inSubpackage(final String targetPackageName) {
    //        return Matchers.inSubpackage(targetPackageName);
    //    }
    //    /** Returns a matcher which matches methods with matching return types. */
    //    public static Matcher<Method> returns(final Matcher<? super Class<?>> returnType) {
    //        return Matchers.returns(returnType);
    //    }
}