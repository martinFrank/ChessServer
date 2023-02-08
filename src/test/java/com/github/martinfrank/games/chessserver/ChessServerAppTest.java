package com.github.martinfrank.games.chessserver;

import com.github.martinfrank.tcpclientserver.TcpServer;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static org.mockito.Mockito.mock;

/**
 * Unit test for simple App.
 */
public class ChessServerAppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ChessServerAppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ChessServerAppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
        ChessServerApp app = new ChessServerApp();
        TcpServer mock = mock(TcpServer.class);
    }
}
