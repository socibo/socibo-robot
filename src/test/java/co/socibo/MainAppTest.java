package co.socibo;
/*
 * @author Umashankar
 * <a href="http://j2eedev.org">http://j2eedev.org</a>
 * Beginer JUnit Tutorial
 * */

import co.socibo.MainApp;
import java.util.ArrayList;
import java.util.Arrays;
import junit.framework.TestCase;
import org.junit.Assert;

public class MainAppTest extends TestCase {


    public MainAppTest(String testName) {
	super(testName);
    }

    protected void setUp() throws Exception {
	super.setUp();
    }

    protected void tearDown() throws Exception {
	super.tearDown();
    }

    public void testParseSimple() {
	ArrayList<Object> ret = MainApp.parse("foo(1,2,3)");
	assertEquals(ret.get(0), "foo");
	assertTrue(Arrays.equals((String[])ret.get(1), new String[]{"1","2","3"}));
    }

    // public void testParseComplex() {
    // 	ArrayList<Object> ret = MainApp.parse("foo(1,2,3);boo(2,3)");
    // 	assertEquals(ret.get(0), "foo");
    // 	assertTrue(Arrays.equals((String[])ret.get(1), new String[]{"1","2","3"}));
    // }
    
}
