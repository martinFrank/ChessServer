package com.github.martinfrank.games.chessserver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static org.mockito.Mockito.mock;

/**
 * Unit test for simple App.
 */
public class ChessServerTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ChessServerTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ChessServerTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
        ChessServer app = new ChessServer();
//        TcpServer mock = mock(TcpServer.class);
    }
}
