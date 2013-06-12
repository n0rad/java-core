package net.awired.ajsl.core.lang.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import org.junit.Test;

public class ReflectToolParameterAnnotationsTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface ParamTest {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface ParamTest2 {
        String value();
    }

    public class Genre {
        public void style() {
        }
    }

    public class Genre2 {
        public void style2(String ouda) {
        }
    }

    public class Genre3 {
        public void style3(@ParamTest("myval") String ouda) {
        }
    }

    public class Genre4 extends Genre3 {
        @Override
        public void style3(String ouda) {
        }
    }

    public interface Genre6 {
        void style3(@ParamTest("myval") String ouda);
    }

    public class Genre5 implements Genre6 {
        @Override
        public void style3(String ouda) {
        }
    }

    public class Genre7 implements Genre6 {
        @Override
        public void style3(@ParamTest2("gg") String ouda) {
        }
    }

    public class Genre8 implements Genre6 {
        @Override
        public void style3(@ParamTest("myVal2") String ouda) {
        }
    }

    public interface Genre10 {
        void style4(@ParamTest("myval") String ouda, @ParamTest("myval2") String ouda2);
    }

    public class Genre9 implements Genre10 {
        @Override
        public void style4(String ouda, String ouda2) {
        }
    }

    public class Genre11 implements Genre10 {
        @Override
        public void style4(String ouda, @ParamTest("myval42") String ouda2) {
        }
    }

    @SuppressWarnings("unused")
    public class Genre12 extends Genre11 implements Genre10 {
        @Override
        public void style4(String ouda, String ouda2) {
        }
    }

    @SuppressWarnings("unused")
    public class Genre13 extends Genre11 implements Genre10 {
        @Override
        public void style4(String ouda, @ParamTest("myval43") String ouda2) {
        }
    }

    @Test
    public void should_not_found_annotations_for_null() {
        Set<Annotation>[] findParametersAnnotation = ReflectTool.findParametersAnnotation(null);

        assertNull(findParametersAnnotation);
    }

    @Test
    public void should_not_found_annotations_for_no_parameters() {
        Method method = Genre.class.getMethods()[0];
        Set<Annotation>[] findParametersAnnotation = ReflectTool.findParametersAnnotation(method);

        assertNotNull(findParametersAnnotation);
        assertEquals(0, findParametersAnnotation.length);
    }

    @Test
    public void should_not_found_annotations_for_param_without_annotations() {
        Method method = Genre2.class.getMethods()[0];
        Set<Annotation>[] findParametersAnnotation = ReflectTool.findParametersAnnotation(method);

        assertNotNull(findParametersAnnotation);
        assertEquals(1, findParametersAnnotation.length);
        assertEquals(0, findParametersAnnotation[0].size());
    }

    @Test
    public void should_find_annotation_on_one_param() {
        Method method = Genre3.class.getMethods()[0];
        Set<Annotation>[] findParametersAnnotation = ReflectTool.findParametersAnnotation(method);

        assertNotNull(findParametersAnnotation);
        assertEquals(1, findParametersAnnotation.length);
        assertEquals(1, findParametersAnnotation[0].size());
        assertEquals("myval", ((ParamTest) findParametersAnnotation[0].iterator().next()).value());
    }

    @Test
    public void should_find_annotation_on_one_param_extend() throws Exception {
        Method method = Genre4.class.getMethods()[0];
        Set<Annotation>[] findParametersAnnotation = ReflectTool.findParametersAnnotation(method);

        assertNotNull(findParametersAnnotation);
        assertEquals(1, findParametersAnnotation.length);
        assertEquals(1, findParametersAnnotation[0].size());
        assertEquals("myval", ((ParamTest) findParametersAnnotation[0].iterator().next()).value());
    }

    @Test
    public void should_find_annotation_on_one_param_implements() throws Exception {
        Method method = Genre5.class.getMethods()[0];
        Set<Annotation>[] findParametersAnnotation = ReflectTool.findParametersAnnotation(method);

        assertNotNull(findParametersAnnotation);
        assertEquals(1, findParametersAnnotation.length);
        assertEquals(1, findParametersAnnotation[0].size());
        assertEquals("myval", ((ParamTest) findParametersAnnotation[0].iterator().next()).value());
    }

    @Test
    public void should_find_annotation_on_one_param_with_2_annotations() throws Exception {
        Method method = Genre7.class.getMethods()[0];
        Set<Annotation>[] findParametersAnnotation = ReflectTool.findParametersAnnotation(method);

        assertNotNull(findParametersAnnotation);
        assertEquals(1, findParametersAnnotation.length);
        assertEquals(2, findParametersAnnotation[0].size());
        Iterator<Annotation> iterator = findParametersAnnotation[0].iterator();
        assertEquals("gg", ((ParamTest2) iterator.next()).value());
        assertEquals("myval", ((ParamTest) iterator.next()).value());
    }

    @Test
    public void should_find_annotation_on_one_param_with_2_same_annotations() throws Exception {
        Method method = Genre8.class.getMethods()[0];
        Set<Annotation>[] findParametersAnnotation = ReflectTool.findParametersAnnotation(method);

        assertNotNull(findParametersAnnotation);
        assertEquals(1, findParametersAnnotation.length);
        assertEquals(1, findParametersAnnotation[0].size());
        Iterator<Annotation> iterator = findParametersAnnotation[0].iterator();
        assertEquals("myVal2", ((ParamTest) iterator.next()).value());
    }

    @Test
    public void should_find_annotation_on_two_params_in_interfaces() throws Exception {
        Method method = Genre9.class.getMethods()[0];
        Set<Annotation>[] findParametersAnnotation = ReflectTool.findParametersAnnotation(method);

        assertNotNull(findParametersAnnotation);
        assertEquals(2, findParametersAnnotation.length);
        assertEquals(1, findParametersAnnotation[0].size());
        assertEquals(1, findParametersAnnotation[1].size());
        assertEquals("myval", ((ParamTest) findParametersAnnotation[0].iterator().next()).value());
        assertEquals("myval2", ((ParamTest) findParametersAnnotation[1].iterator().next()).value());
    }

    @Test
    public void should_find_annotation_with_priority_on_class() throws Exception {
        Method method = Genre11.class.getMethods()[0];
        Set<Annotation>[] findParametersAnnotation = ReflectTool.findParametersAnnotation(method);

        assertNotNull(findParametersAnnotation);
        assertEquals(2, findParametersAnnotation.length);
        assertEquals(1, findParametersAnnotation[0].size());
        assertEquals(1, findParametersAnnotation[1].size());
        assertEquals("myval", ((ParamTest) findParametersAnnotation[0].iterator().next()).value());
        assertEquals("myval42", ((ParamTest) findParametersAnnotation[1].iterator().next()).value());
    }

    @Test
    public void should_find_annotation_with_priority_on_extends() throws Exception {
        Method method = Genre12.class.getMethods()[0];
        Set<Annotation>[] findParametersAnnotation = ReflectTool.findParametersAnnotation(method);

        assertNotNull(findParametersAnnotation);
        assertEquals(2, findParametersAnnotation.length);
        assertEquals(1, findParametersAnnotation[0].size());
        assertEquals(1, findParametersAnnotation[1].size());
        assertEquals("myval", ((ParamTest) findParametersAnnotation[0].iterator().next()).value());
        assertEquals("myval42", ((ParamTest) findParametersAnnotation[1].iterator().next()).value());
    }

    @Test
    public void should_find_annotation_with_priority_on_class_when_extends_and_implements() throws Exception {
        Method method = Genre13.class.getMethods()[0];
        Set<Annotation>[] findParametersAnnotation = ReflectTool.findParametersAnnotation(method);

        assertNotNull(findParametersAnnotation);
        assertEquals(2, findParametersAnnotation.length);
        assertEquals(1, findParametersAnnotation[0].size());
        assertEquals(1, findParametersAnnotation[1].size());
        assertEquals("myval", ((ParamTest) findParametersAnnotation[0].iterator().next()).value());
        assertEquals("myval43", ((ParamTest) findParametersAnnotation[1].iterator().next()).value());
    }
}
