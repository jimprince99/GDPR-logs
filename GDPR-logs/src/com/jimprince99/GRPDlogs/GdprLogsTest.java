package com.jimprince99.GRPDlogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GdprLogsTest {
	private static final String SIMPLE_FILE = "testData/testFile1.log";
	private static final String REDACTED = "REDACTED";
	
	private static final String FILE2 = "testData/testFile2.log";
	private static final String FILE2_RESULTS = "testData/testFile2.log-expected-results";
	
	private static final String FILE3 = "testData/testFile3.log";
	private static final String FILE3_RESULTS = "testData/testFile3.log-expected-results";
	
	private static final String FILE4 = "testData/testFile4.log";
	private static final String FILE4_RESULTS = "testData/testFile4.log-expected-results";
	

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

	/**
	 * test for null filename
	 */
	@Test
	void testNullyFileName() {
		GdprLogs g = new GdprLogs();
		
		boolean failed = false;
		try {
			g.processFile(null);
		} catch (IOException e) {
			fail("Unexpected exception");
		} catch (IllegalArgumentException e) {
			failed = true;
		} 
		if (!failed) {
			fail("missed expected exception");
		}
	}
	
	/**
	 * test for empty filename
	 */
	@Test
	void testEmptyFileName() {
		GdprLogs g = new GdprLogs();
		
		boolean failed = false;
		try {
			g.processFile("");
		} catch (IOException e) {
			fail("Unexpected exception");
		} catch (IllegalArgumentException e) {
			failed = true;
		} 
		if (!failed) {
			fail("missed expected exception");
		}
	}
	
	/**
	 * test for file not found
	 */
	@Test
	void testFileNotFound() {
		GdprLogs g = new GdprLogs();
		
		boolean failed = false;
		try {
			g.processFile("fileNotFound");
		} catch (IOException e) {
			failed = true;
		} catch (IllegalArgumentException e) {
			fail("Unexpected exception");
		}catch (NullPointerException e) {
			fail("Unexpected exception");
		} 
		if (!failed) {
			fail("missed expected exception");
		}
	}
	
	
	/**
	 * test for file not found
	 */
	@Test
	void testNewFilename() {
		GdprLogs g = new GdprLogs();
		
		assertEquals("jimREDACTED", g.outputFileName("jim"));
		assertEquals("jim.REDACTED", g.outputFileName("jim."));
	}
	/**
	 * test for file not found
	 */
	@Test
	void testSimpleFile() {
		GdprLogs g = new GdprLogs();
		
		try {
			g.processFile(SIMPLE_FILE);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected exception");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail("Unexpected exception");
		}catch (NullPointerException e) {
			e.printStackTrace();
			fail("Unexpected exception");
		} 
		
		String outputFilename = SIMPLE_FILE + REDACTED;
		assertTrue(filesAreEqual(SIMPLE_FILE, outputFilename));	
	}
	


	/**
	 * test a file with two MSISDNs that need to be replaced
	 */
	@Test
	void testFile2() {
		GdprLogs g = new GdprLogs();
		
		try {
			g.processFile(FILE2);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected exception");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail("Unexpected exception");
		}catch (NullPointerException e) {
			e.printStackTrace();
			fail("Unexpected exception");
		} 
		
		String outputFilename = FILE2 + REDACTED;
		assertTrue(filesAreEqual(FILE2_RESULTS, outputFilename));
	}
	
	/**
	 * test a file with two MSISDNs that need to be replaced
	 */
	@Test
	void testFile3() {
		GdprLogs g = new GdprLogs();
		
		try {
			g.processFile(FILE3);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected exception");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail("Unexpected exception");
		}catch (NullPointerException e) {
			e.printStackTrace();
			fail("Unexpected exception");
		} 
		
		String outputFilename = FILE3 + REDACTED;
		assertTrue(filesAreEqual(FILE3_RESULTS, outputFilename));
	}
	
	
	/**
	 * check that the output files are equal
	 * 
	 * @param f1
	 * @param f2
	 * @return
	 */
	private boolean filesAreEqual(String f1, String f2) {
		
		BufferedReader r1;
		try {
			r1 = new BufferedReader(new FileReader(f1));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return false;
		}
		BufferedReader r2;
		try {
			r2 = new BufferedReader(new FileReader(f2));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		
		String line1;
		String line2;
		try {
			line1 = r1.readLine();
			line2 = r2.readLine();
		} catch (IOException e) {
			return false;
		}
	
		while ((line1 != null) || (line2 != null)) {
			if ((line1 == null) || (line2 == null)) {
				System.out.println("line1 is null");
				return false;
			};
			//System.out.println("Tester Checking " + line1 + " with " + line2);
			
			if (!line1.contentEquals(line2)) {
				System.out.println("lines are not equal");
				return false;
			}
			
			try {
				line1 = r1.readLine();
				line2 = r2.readLine();
			} catch (IOException e) {
				return false;
			}
		}
	
		return true;
	}
	
//	@Test
//	void testFile99() {
//		GdprLogs g = new GdprLogs();
//		
//		try {
//			g.processFile("c:/tmp/jim1.log");
//			//g.processFile("testData/jim1.log");
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail("Unexpected exception");
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//			fail("Unexpected exception");
//		}catch (NullPointerException e) {
//			e.printStackTrace();
//			fail("Unexpected exception");
//		} 
//		
////		String outputFilename = FILE3 + REDACTED;
////		assertTrue(filesAreEqual(FILE3_RESULTS, outputFilename));
//	}

}
