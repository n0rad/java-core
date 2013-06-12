package net.awired.ajsl.core.lang.reflect;

import org.junit.Assert;
import org.junit.Test;

public class ReflectToolTest {

    public interface ToFindInterface {
    }

    public interface TotoInf extends ToFindInterface {
    }

    public interface NotFound {
    }

    public class ToFindClass {
    }

    public class Toto implements ToFindInterface {
    }

    public class Toto2 implements NotFound {
    }

    public class Toto3 extends ToFindClass {
    }

    public class Toto4 extends Toto {
    }

    public class Toto5 extends Toto4 {
    }

    public class Toto6 implements TotoInf {
    }

    public class Toto7 extends Toto6 {
    }

    public class Toto8 extends Toto7 {
    }

    @Test
    public void should_classImplement_find_direct_class() {
        Assert.assertTrue(ReflectTool.classImplement(ToFindInterface.class, ToFindInterface.class));
    }

    @Test
    public void should_classImplement_find_direct_implement() {
        Assert.assertTrue(ReflectTool.classImplement(Toto.class, ToFindInterface.class));
    }

    @Test
    public void should_not_classImplement_find_direct_implement() {
        Assert.assertFalse(ReflectTool.classImplement(Toto2.class, ToFindInterface.class));
    }

    @Test
    public void should_classImplement_find_direct_extend() {
        Assert.assertTrue(ReflectTool.classImplement(Toto3.class, ToFindClass.class));
    }

    @Test
    public void should_not_classImplement_find_direct_extend() {
        Assert.assertFalse(ReflectTool.classImplement(Toto3.class, ToFindInterface.class));
    }

    @Test
    public void should_not_classImplement_find_direct_extend2() {
        Assert.assertFalse(ReflectTool.classImplement(Toto3.class, Toto2.class));
    }

    @Test
    public void should_classImplement_find_extend_implement() {
        Assert.assertTrue(ReflectTool.classImplement(Toto4.class, ToFindInterface.class));
    }

    @Test
    public void should_not_classImplement_find_extend_implement() {
        Assert.assertFalse(ReflectTool.classImplement(Toto4.class, ToFindClass.class));
    }

    @Test
    public void should_classImplement_find_extend_extend() {
        Assert.assertTrue(ReflectTool.classImplement(Toto5.class, Toto.class));
    }

    @Test
    public void should_not_classImplement_find_extend_extend() {
        Assert.assertFalse(ReflectTool.classImplement(Toto5.class, Toto2.class));
    }

    @Test
    public void should_classImplement_find_extend_extend_implement() {
        Assert.assertTrue(ReflectTool.classImplement(Toto5.class, ToFindInterface.class));
    }

    @Test
    public void should_not_classImplement_find_extend_extend_implement() {
        Assert.assertFalse(ReflectTool.classImplement(Toto5.class, Toto2.class));
    }

    @Test
    public void should_classImplement_find_implement_implement() {
        Assert.assertTrue(ReflectTool.classImplement(TotoInf.class, ToFindInterface.class));
    }

    @Test
    public void should_not_classImplement_find_implement_implement() {
        Assert.assertFalse(ReflectTool.classImplement(TotoInf.class, NotFound.class));
    }

    @Test
    public void should_classImplement_find_direct_implement_implement() {
        Assert.assertTrue(ReflectTool.classImplement(Toto6.class, ToFindInterface.class));
    }

    @Test
    public void should_classImplement_find_extend_implement_implement() {
        Assert.assertTrue(ReflectTool.classImplement(Toto7.class, ToFindInterface.class));
    }

    @Test
    public void should_not_classImplement_find_extend_implement_implement() {
        Assert.assertFalse(ReflectTool.classImplement(Toto7.class, NotFound.class));
    }

    @Test
    public void should_classImplement_find_extend_extend_extend() {
        Assert.assertTrue(ReflectTool.classImplement(Toto8.class, Toto6.class));
    }

}
