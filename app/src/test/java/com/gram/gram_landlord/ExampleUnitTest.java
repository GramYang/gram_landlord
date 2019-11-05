package com.gram.gram_landlord;

import org.junit.Test;

import java.security.KeyStore;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void test1() throws Exception{
        assertNotNull(KeyStore.getInstance("jks"));
    }
}