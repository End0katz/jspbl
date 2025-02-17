package com.end0katz.jspbl;

import org.junit.*;

public class BackupTest {

    StringMarcher s;

    public void test(String x) {
        s = new StringMarcher(x);
        try {
            while (!s.data().isEmpty()) {
                parseF();
            }

        } catch (Exception e) {

        }
    }

    public void parseX() throws Backup {
        s.str('X').expect();
    }

    public void parseAFA() throws Backup {
        s.str('{').expect();
        parseF();
        s.str('}').expect();
    }

    public void parseF() {
        s.enterScope();
        try {
            parseX();
        } catch (Backup b) {
            try {
                s.resetScope();
                parseAFA();
            } catch (Backup b2) {
                Assert.fail("Parse error");
            }
        }
        s.discardScope();
    }

    @Test
    public void test0() {
        test("XX{XX}X{X{X}XX{{X}}}{}{{}{}}");
    }

    @Test
    public void test1() {
        test("{{{{{{{{}}}}}}}}");
    }

    @Test
    public void test2() {
        test("{XXXXXXX{}}");
    }

    @Test
    public void test3() {
        test("");
    }

    @Test
    public void test4() {
        test("{X}");
    }

    @Test
    public void test5() {
        test("{XX}");
    }
}
