package service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LifecycleDemoTest {

    @BeforeClass
    public static void beforeAll() {
        System.out.println(">> BeforeClass");
    }

    @Before
    public void beforeEach() {
        System.out.println(">> Before");
    }

    @Test
    public void test1() {
        System.out.println(">> Test 1");
    }

    @Test
    public void test2() {
        System.out.println(">> Test 2");
    }

    @After
    public void afterEach() {
        System.out.println(">> After");
    }

    @AfterClass
    public static void afterAll() {
        System.out.println(">> AfterClass");
    }
}
