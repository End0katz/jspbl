package com.end0katz.jspbl;

import org.junit.*;

public class TestStringMarcher {

    @Test
    public void reTest0() {
        Assert.assertEquals(
                "Hello",
                new StringMarcher("Hello!")
                        .re(
                                "..(.)\\1o",
                                StringMarcher.ExpansionType.START_SMALL_AND_GROW
                        )
                        .result()
        );
    }

    @Test
    public void reTest1() {
        Assert.assertEquals(
                "010001",
                new StringMarcher("0100010")
                        .re(
                                ".*1",
                                StringMarcher.ExpansionType.START_LARGE_AND_SHRINK
                        )
                        .result()
        );
    }

    @Test
    public void constCharTest() {
        Assert.assertEquals(
                true,
                new StringMarcher("char")
                        .str('c').success()
        );
    }

    @Test
    public void constStrEquals() {
        Assert.assertEquals(
                null,
                new StringMarcher("Helo, World!")
                        .str("Hello").result()
        );
    }
}
