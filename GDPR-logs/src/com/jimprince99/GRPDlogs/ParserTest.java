package com.jimprince99.GRPDlogs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;
import java.util.Set;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParserTest {
	private String SEARCH_PREFIX_NATIONAL = "0";
	private String SEARCH_PREFIX_INTERNATIONAL = "44";
	private String PREFIX = "7700";
	
	// Test numbers
	private String TEST_MSISDN0 = "7802000000";
	private String TEST_MSISDN1 = "7802000001";
	private String TEST_MSISDN2 = "7802000002";
	private String TEST_MSISDN3 = "7802000003";
	
	private String TEST_MSISDN0_NATIONAL = "07802000000";
	private String TEST_MSISDN1_NATIONAL = "07802000001";
	private String TEST_MSISDN2_NATIONAL = "07802000002";
	private String TEST_MSISDN3_NATIONAL = "07802000003";
	
	
	// Replacement numbers
	private String REPLACEMENT_MSISDN0 = "7700000000";
	private String REPLACEMENT_MSISDN1 = "7700000001";
	private String REPLACEMENT_MSISDN2 = "7700000002";
	private String REPLACEMENT_MSISDN3 = "7700000003";
	
	private String REPLACEMENT_MSISDN0_NATIONAL = "07700000000";
	private String REPLACEMENT_MSISDN1_NATIONAL = "07700000001";
	private String REPLACEMENT_MSISDN2_NATIONAL = "07700000002";
	private String REPLACEMENT_MSISDN3_NATIONAL = "07700000003";
	
	private String TEST_LINE1 = TEST_MSISDN0_NATIONAL;
	private String TEST_LINE2 = TEST_MSISDN0;
	private String TEST_LINE3 = TEST_MSISDN0_NATIONAL + "  " + TEST_MSISDN0_NATIONAL;
	private String TEST_LINE4 = TEST_MSISDN0_NATIONAL + "  " + TEST_MSISDN0 + "  " + TEST_MSISDN1_NATIONAL;

	private String TEST_LINE5 = "+" + SEARCH_PREFIX_INTERNATIONAL + TEST_MSISDN0;
	private String TEST_LINE6 = TEST_MSISDN0;
	private String TEST_LINE7 = "+" + SEARCH_PREFIX_INTERNATIONAL + TEST_MSISDN0 + "  " + TEST_MSISDN0_NATIONAL;
	private String TEST_LINE8 = "+" + SEARCH_PREFIX_INTERNATIONAL + TEST_MSISDN0 + "  " + TEST_MSISDN0 + "  " + TEST_MSISDN1_NATIONAL;

	private String TEST_LINE9  = "00" + SEARCH_PREFIX_INTERNATIONAL + TEST_MSISDN0;
	private String TEST_LINE10 = TEST_MSISDN0;
	private String TEST_LINE11 = "00" + SEARCH_PREFIX_INTERNATIONAL + TEST_MSISDN0 + "  " + TEST_MSISDN0_NATIONAL;
	private String TEST_LINE12 = "00" + SEARCH_PREFIX_INTERNATIONAL + TEST_MSISDN0 + "  " + TEST_MSISDN0 + "  " + TEST_MSISDN1_NATIONAL;

	private String TEST_RESULT_LINE1 = REPLACEMENT_MSISDN0_NATIONAL;
	private String TEST_RESULT_LINE2 = TEST_MSISDN0;
	private String TEST_RESULT_LINE3 = REPLACEMENT_MSISDN0_NATIONAL + "  " + REPLACEMENT_MSISDN0_NATIONAL;
	private String TEST_RESULT_LINE4 = REPLACEMENT_MSISDN0_NATIONAL + "  " + TEST_MSISDN0 + "  " + REPLACEMENT_MSISDN1_NATIONAL;

	private String TEST_RESULT_LINE5 = "+" + SEARCH_PREFIX_INTERNATIONAL + REPLACEMENT_MSISDN0;
	private String TEST_RESULT_LINE6 = TEST_MSISDN0;
	private String TEST_RESULT_LINE7 = "+" + SEARCH_PREFIX_INTERNATIONAL + REPLACEMENT_MSISDN0 + "  " + REPLACEMENT_MSISDN0_NATIONAL;
	private String TEST_RESULT_LINE8 = "+" + SEARCH_PREFIX_INTERNATIONAL + REPLACEMENT_MSISDN0 + "  " + TEST_MSISDN0 + "  "  + REPLACEMENT_MSISDN1_NATIONAL;

	private String TEST_RESULT_LINE9 = "00" + SEARCH_PREFIX_INTERNATIONAL + REPLACEMENT_MSISDN0;
	private String TEST_RESULT_LINE10 = TEST_MSISDN0;
	private String TEST_RESULT_LINE11 = "00" + SEARCH_PREFIX_INTERNATIONAL + REPLACEMENT_MSISDN0 + "  " + REPLACEMENT_MSISDN0_NATIONAL;
	private String TEST_RESULT_LINE12 = "00" + SEARCH_PREFIX_INTERNATIONAL + REPLACEMENT_MSISDN0 + "  " + TEST_MSISDN0 + "  " + REPLACEMENT_MSISDN1_NATIONAL;



	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void testIsRedacted() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
				
		assertEquals("7700000000", p.getNextMsisdn());
		assertEquals("7700000001", p.getNextMsisdn());
		assertEquals("7700000002", p.getNextMsisdn());
		assertEquals("7700000003", p.getNextMsisdn());
		assertEquals("7700000004", p.getNextMsisdn());
	}

	@Test
	void testGetNextMsisdn() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		assertEquals("7700000000", p.getNextMsisdn());
		assertEquals("7700000001", p.getNextMsisdn());
		assertEquals("7700000002", p.getNextMsisdn());
		assertEquals("7700000003", p.getNextMsisdn());
		assertEquals("7700000004", p.getNextMsisdn());
	}
	
	@Test
	void testGetNextMsisdnFormats() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		assertEquals("07700000000", p.getNextMsisdnLocalFormat());
		assertEquals("07700000001", p.getNextMsisdnLocalFormat());
		assertEquals("+447700000002", p.getNextMsisdnInternationalFormat1());
		assertEquals("+447700000003", p.getNextMsisdnInternationalFormat1());
		assertEquals("+447700000004", p.getNextMsisdnInternationalFormat1());
		assertEquals("07700000005", p.getNextMsisdnLocalFormat());
		assertEquals("+447700000006", p.getNextMsisdnInternationalFormat1());

		assertEquals("00447700000007", p.getNextMsisdnInternationalFormat2());
		assertEquals("00447700000008", p.getNextMsisdnInternationalFormat2());
		assertEquals("00447700000009", p.getNextMsisdnInternationalFormat2());
		assertEquals("00447700000010", p.getNextMsisdnInternationalFormat2());

	}
	
	/**
	 * start by finding national format MSISDNs from Strings
	 */
	@Test
	void testFindNationalMsisdns1() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		Set<String> results = p.findMsisdnsNational(TEST_MSISDN1_NATIONAL);
		assertEquals(1, results.size());
		
		Iterator<String> i = results.iterator();
		while (i.hasNext()) {
			assertEquals(TEST_MSISDN1, i.next());
		}
	}
	
	/**
	 * start by finding national format MSISDNs from Strings
	 */
	@Test
	void testFindNationalMsisdns2() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		Set<String> results = p.findMsisdnsNational("0" + TEST_MSISDN1 + "xxx");
		assertEquals(1, results.size());
		
		Iterator<String> i = results.iterator();
		while (i.hasNext()) {
			assertEquals(TEST_MSISDN1, i.next());
		}
	}
	
	/**
	 * start by finding national format MSISDNs from Strings
	 */
	@Test
	void testFindNationalMsisdns3() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		Set<String> results = p.findMsisdnsNational("0" + TEST_MSISDN1 + "xxx" + TEST_MSISDN1);
		assertEquals(1, results.size());
		
		Iterator<String> i = results.iterator();
		while (i.hasNext()) {
			assertEquals(TEST_MSISDN1, i.next());
		}
	}
	
	/**
	 * start by finding national format MSISDNs from Strings
	 */
	@Test
	void testFindNationalMsisdns4() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		Set<String> results = p.findMsisdnsNational("0" + TEST_MSISDN1 + "xxx" + TEST_MSISDN1 + "xxx" + TEST_MSISDN1);
		assertEquals(1, results.size());
		
		Iterator<String> i = results.iterator();
		while (i.hasNext()) {
			assertEquals(TEST_MSISDN1, i.next());
		}
	}
	
	/**
	 * start by finding national format MSISDNs from Strings
	 */
	@Test
	void testFindNationalMsisdns5() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		Set<String> results = p.findMsisdnsNational("0" + TEST_MSISDN1 + "xxx" + "0" + TEST_MSISDN2 + "xxx" + "0" + TEST_MSISDN1);
		assertEquals(2, results.size());
		
		Iterator<String> i = results.iterator();
		while (i.hasNext()) {
			assertEquals(TEST_MSISDN2, i.next());
			assertEquals(TEST_MSISDN1, i.next());
		}
	}
	
	@Test
	void testFindNationalMsisdns6() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		Set<String> results = p.findMsisdnsNational("00447700000000");
		assertEquals(0, results.size());
	}
	
	//////////////////////////////////////////////
	/**
	 * start by finding national format MSISDNs from Strings
	 */
	@Test
	void testFindInternational1Msisdns1() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		Set<String> results = p.findMsisdnsInternational1(TEST_LINE5);
		assertEquals(1, results.size());
		
		Iterator<String> i = results.iterator();
		while (i.hasNext()) {
			assertEquals(TEST_MSISDN0, i.next());
		}
	}
	
	@Test
	void testFindInternational1Msisdns2() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		Set<String> results = p.findMsisdnsInternational1("+44" + TEST_MSISDN1 + "xxx");
		assertEquals(1, results.size());
		
		Iterator<String> i = results.iterator();
		while (i.hasNext()) {
			assertEquals(TEST_MSISDN1, i.next());
		}
	}
	
	@Test
	void testFindInternational1Msisdns3() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		Set<String> results = p.findMsisdnsInternational2(TEST_LINE9);
		assertEquals(1, results.size());
		
		Iterator<String> i = results.iterator();
		while (i.hasNext()) {
			assertEquals(TEST_MSISDN0, i.next());
		}
	}
	
	@Test
	void testFindInternational1Msisdns4() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		Set<String> results = p.findMsisdnsInternational2("0044" + TEST_MSISDN1 + "xxx");
		assertEquals(1, results.size());
		
		Iterator<String> i = results.iterator();
		while (i.hasNext()) {
			assertEquals(TEST_MSISDN1, i.next());
		}
	}
	
	////////////////////////////////////////////////////
	
	/**
	 * start by finding national format MSISDNs from Strings
	 */
	@Test
	void testgetReplacementMsisdns1() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		String results = p.getReplacementMsisdn(TEST_MSISDN0);
		assertEquals(REPLACEMENT_MSISDN0, results);
		
		String results4 = p.getReplacementMsisdn(TEST_MSISDN0);
		assertEquals(REPLACEMENT_MSISDN0, results4);
		
		String results5 = p.getReplacementMsisdn(TEST_MSISDN0);
		assertEquals(REPLACEMENT_MSISDN0, results5);
		
		String results2 = p.getReplacementMsisdn(TEST_MSISDN1);
		assertEquals(REPLACEMENT_MSISDN1, results2);
		
		String results3 = p.getReplacementMsisdn(TEST_MSISDN2);
		assertEquals(REPLACEMENT_MSISDN2, results3);
	}
	
	/**
	 * start by finding national format MSISDNs from Strings
	 */
	@Test
	void testReplaceMsisdns1() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		String results = p.replaceMsisdnNational(SEARCH_PREFIX_NATIONAL + TEST_MSISDN0, TEST_MSISDN0);
		assertEquals(SEARCH_PREFIX_NATIONAL + REPLACEMENT_MSISDN0, results);
	}
	
	/**
	 * start by finding national format MSISDNs from Strings
	 */
	@Test
	void testReplaceMsisdns2() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		String line = SEARCH_PREFIX_NATIONAL + TEST_MSISDN1 
				+ "  " + SEARCH_PREFIX_NATIONAL + TEST_MSISDN2;
		
		String res = SEARCH_PREFIX_NATIONAL + REPLACEMENT_MSISDN0 
				+ "  " + SEARCH_PREFIX_NATIONAL + TEST_MSISDN2;
		
		String results = p.replaceMsisdnNational(line, TEST_MSISDN1);
		assertEquals(res, results);
	}
	
	/**
	 * start by finding national format MSISDNs from Strings
	 */
	@Test
	void testReplaceMsisdns3() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
		String line = SEARCH_PREFIX_NATIONAL + TEST_MSISDN1 
				+ "  " + SEARCH_PREFIX_NATIONAL + TEST_MSISDN2;
		
		String res = SEARCH_PREFIX_NATIONAL + REPLACEMENT_MSISDN0 
				+ "  " + SEARCH_PREFIX_NATIONAL + TEST_MSISDN2;
		
		String results = p.replaceMsisdnNational(line, TEST_MSISDN1);
		assertEquals(res, results);
		
		String res2 = SEARCH_PREFIX_NATIONAL + REPLACEMENT_MSISDN0 
				+ "  " + SEARCH_PREFIX_NATIONAL + REPLACEMENT_MSISDN1;
		
		String results2 = p.replaceMsisdnNational(results, TEST_MSISDN2);
		assertEquals(res2, results2);
		
	}
	
	////////////////////////////////////////
	/**
	 * start by finding national format MSISDNs from Strings
	 */
	@Test
	void testParse1() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
		
        String result = p.parse(TEST_LINE1);
        
        assertEquals(TEST_RESULT_LINE1, result);
		
	}
	
	@Test
	void testParse2() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
        String result = p.parse(TEST_LINE2);
        
        assertEquals(TEST_RESULT_LINE2, result);
	}
	
	@Test
	void testParse3() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
        String result = p.parse(TEST_LINE3);
        
        assertEquals(TEST_RESULT_LINE3, result);
	}
	
	@Test
	void testParse41() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
        String result = p.parseNational("00447700000000");
        
        assertEquals("00447700000000", result);
	}
	
	
	@Test
	void testParse4() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
        String result = p.parse(TEST_LINE4);
        
        assertEquals(TEST_RESULT_LINE4, result);
	}
	
	/**
	 * test International Msisdns starting with +44
	 * We should be able to find the base Msisdn
	 */
	@Test
	void testParse5() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
        String result = p.parse(TEST_LINE5);
        assertEquals(TEST_RESULT_LINE5, result);
	}
	
	@Test
	void testParse6() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
        String result = p.parse(TEST_LINE6);
        assertEquals(TEST_RESULT_LINE6, result);
	}
	
	@Test
	void testParse7() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
        String result = p.parse(TEST_LINE7);
        assertEquals(TEST_RESULT_LINE7, result);
	}
	
	@Test
	void testParse8() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
        String result = p.parse(TEST_LINE8);
        assertEquals(TEST_RESULT_LINE8, result);
	}
	
	/**
	 * new test "0044xxxxx" values
	 */
	@Test
	void testParse9() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
        String result = p.parse(TEST_LINE9);
        assertEquals(TEST_RESULT_LINE9, result);
	}
	
	@Test
	void testParse10() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
        String result = p.parse(TEST_LINE10);
        assertEquals(TEST_RESULT_LINE10 , result);
	}
	
	@Test
	void testParse11() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
        String result = p.parse(TEST_LINE11);
        assertEquals(TEST_RESULT_LINE11, result);
	}
	
	@Test
	void testParse12() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
        String result = p.parse(TEST_LINE12);
        assertEquals(TEST_RESULT_LINE12, result);
	}
	
	// Adhock tests
	
	@Test
	void testParse99() {
		Parser p = new Parser(SEARCH_PREFIX_NATIONAL, PREFIX, SEARCH_PREFIX_INTERNATIONAL);
        String result = p.parse("From: \"+447815122618\" <sip:+447815122618@10.130.100.134>;tag=1077631");
        assertEquals("From: \"+447700000000\" <sip:+447700000000@10.130.100.134>;tag=1077631", result);
	}

}
