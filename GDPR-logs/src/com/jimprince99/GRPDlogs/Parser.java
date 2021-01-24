package com.jimprince99.GRPDlogs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse a String, replace any MSISDNs with anonymous MSISDNs
 * @author jpri1335
 *
 */
public class Parser {
	String searchPrefixNational = "";
	String searchPrefixInternational1 = "+";
	String searchPrefixInternational2 = "00";
	String prefix = "";
	String countryCode = "";
	
	Map<String, String> replacementMsisdns = new HashMap(10000);
	int replacementCount = -1;
	
	public Parser(String searchPrefixNational, String prefix, String countryCode) {
		this.searchPrefixNational = searchPrefixNational;
		this.searchPrefixInternational1 = this.searchPrefixInternational1 + countryCode;
		this.searchPrefixInternational2 = this.searchPrefixInternational2 + countryCode;
		this.prefix = prefix;
		this.countryCode = countryCode;
	}
	
	/**
	 * read in a single line, and replace all the MSISDNs
	 * with redacted MSISDNs
	 * @param line
	 * @return
	 */
	public String parse(String line) {
		String output = parseInternational2(line);
		output = parseInternational1(output);
		output = parseNational(output);
		
		return output;
	}
	
	/**
	 * search for international format MSISDNs, and replace them with dummy Msisdns.
	 * @param line
	 * @return
	 */
	public String parseInternational2(String line) {
		Set<String> msisdns = findMsisdnsInternational2(line);
		String outputString= line;
		
		Iterator<String> i = msisdns.iterator();
		while(i.hasNext()) {
			String msisdn = i.next();
			if (isRedacted.test(msisdn)) continue;
			
			outputString =  replaceMsisdnInternational2(outputString, msisdn);
		}
		return outputString;
	}
	
	/**
	 * search for international format MSISDNs, and replace them with dummy Msisdns.
	 * @param line
	 * @return
	 */
	public String parseInternational1(String line) {
		Set<String> msisdns = findMsisdnsInternational1(line);
		String outputString= line;
		
		Iterator<String> i = msisdns.iterator();
		while(i.hasNext()) {
			String msisdn = i.next();
			if (isRedacted.test(msisdn)) continue;
			
			outputString =  replaceMsisdnInternational1(outputString, msisdn);
		}
		return outputString;
	}
	
	/**
	 * Search for national format MSISDNs, and replace them with dummy Msisdns.
	 * @param line
	 * @return
	 */
	public String parseNational(String line) {
		Set<String> msisdns = findMsisdnsNational(line);
		String outputString= line;
		
		Iterator<String> i = msisdns.iterator();
		while(i.hasNext()) {
			String msisdn = i.next();
			if (isRedacted.test(msisdn)) continue;
			
			outputString =  replaceMsisdnNational(outputString, msisdn);
		}
		return outputString;
	}
	
	/**
	 * Replace a MSISDN that's in National format with
	 * a dummy MSISDN
	 * @param line The log line containing the MSISDN
	 * @param msisdn the MSISDN to be replaces
	 * @return
	 */
	public String replaceMsisdnNational(String line, String msisdn) {
		String replacementMsisdn = getReplacementMsisdn(msisdn);
	
		String nationalSourceMsisdn = searchPrefixNational + msisdn;
		String nationalDestinationMsisdn = searchPrefixNational + replacementMsisdn;

		return line.replaceAll(nationalSourceMsisdn, nationalDestinationMsisdn);
	}
	
	/**
	 * Replace a MSISDN that's in National format with
	 * a dummy MSISDN
	 * @param line The log line containing the MSISDN
	 * @param msisdn the MSISDN to be replaces
	 * @return
	 */
	public String replaceMsisdnInternational1(String line, String msisdn) {
		String replacementMsisdn = getReplacementMsisdn(msisdn);
	
		String internationalSourceMsisdn = searchPrefixInternational1 + msisdn;
		String internationalDestinationMsisdn = searchPrefixInternational1 + replacementMsisdn;

		if (internationalSourceMsisdn.startsWith("+")) {
			internationalSourceMsisdn = "\\" + internationalSourceMsisdn;
		}
		return line.replaceAll(internationalSourceMsisdn, internationalDestinationMsisdn);
	}
	
	/**
	 * Replace a MSISDN that's in National format with
	 * a dummy MSISDN
	 * @param line The log line containing the MSISDN
	 * @param msisdn the MSISDN to be replaces
	 * @return
	 */
	public String replaceMsisdnInternational2(String line, String msisdn) {
		String replacementMsisdn = getReplacementMsisdn(msisdn);
	
		String internationalSourceMsisdn = searchPrefixInternational2 + msisdn;
		String internationalDestinationMsisdn = searchPrefixInternational2 + replacementMsisdn;

		return line.replaceAll(internationalSourceMsisdn, internationalDestinationMsisdn);
	}
	
	Predicate<String> isRedacted = msisdn -> {
		return msisdn.startsWith(prefix);
	};
	
//	Predicate<String> isRedacted = msisdn -> {
//		return msisdn.startsWith(prefix) 
//				|| msisdn.startsWith(searchPrefixInternational1 + prefix) 
//				|| msisdn.startsWith(searchPrefixInternational1 + prefix);
//	};
	
	/**  
	 * 
	 * Find the msisdns from the line.
	 * Search for a national Format MSISDN (07802000001)
	 * For every match that we get, store it in a Set of Msisdns
	 * 
	 * A msisdn is a 10 digit number that is 
	 * preceeded by 0, or 44, or +44, or 0044
	 */
	public Set<String> findMsisdnsNational(String line) {
		String patternString = searchPrefixNational + "(\\d{10})";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(line);
		
		Set<String> results = new HashSet<>();
		while (matcher.find()) {
			int start = matcher.start() + searchPrefixNational.length();

		    String foundMsidn = line.substring(start, matcher.end());
 
		    // is there an extra digit after our match 
		    int length = line.length();
		    if (length > matcher.end()) {
		        char next = line.charAt(matcher.end()+1 );
		        if (Character.isDigit(next)) continue; 
		    }
		    
		    results.add(foundMsidn); 
		}
		return results;
	}
	
	/**  
	 * 
	 * Find the msisdns from the line.
	 * Search for a international Format MSISDN (00447802000001)
	 * For every match that we get, store it in a Set of Msisdns
	 * 
	 * A msisdn is a 10 digit number that is 
	 * preceeded by 0, or 44, or +44, or 0044
	 */
	public Set<String> findMsisdnsInternational1(String line) {
		String patternString = "\\" + searchPrefixInternational1 + "(\\d{10})";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(line);
		
		Set<String> results = new HashSet<>();
		while (matcher.find()) {
			int start = matcher.start() + searchPrefixInternational1.length();

		    String foundMsidn = line.substring(start, matcher.end());
		    results.add(foundMsidn); 
		}
		return results;
	}
	
	/**  
	 * 
	 * Find the msisdns from the line.
	 * Search for a international Format MSISDN (00447802000001)
	 * For every match that we get, store it in a Set of Msisdns
	 * 
	 * A msisdn is a 10 digit number that is 
	 * preceeded by 0, or 44, or +44, or 0044
	 */
	public Set<String> findMsisdnsInternational2(String line) {
		String patternString = searchPrefixInternational2 + "(\\d{10})";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(line);
		
		Set<String> results = new HashSet<>();
		while (matcher.find()) {
			int start = matcher.start() + searchPrefixInternational2.length();

		    String foundMsidn = line.substring(start, matcher.end());
		    results.add(foundMsidn); 
		}
		return results;
	}
		
	/**
	 * file the existing replacement MSISDN, or
	 * generate a new replacement MSISDN
	 * @param msisdn
	 */
	public String getReplacementMsisdn(String key) {
		if (replacementMsisdns.containsKey(key)) {
			return replacementMsisdns.get(key);
		} else {
			String newMsisdn = getNextMsisdn();
			replacementMsisdns.put(key, newMsisdn);
			return newMsisdn;
		}
	}
	
	public String getNextMsisdnLocalFormat() {
		return searchPrefixNational + getNextMsisdn();
	}
	
	public String getNextMsisdnInternationalFormat1() {
		return searchPrefixInternational1 + getNextMsisdn();
	}
	
	public String getNextMsisdnInternationalFormat2() {
		return searchPrefixInternational2 + getNextMsisdn();
	}
	
	/**
	 * get the next MSISDN to use as a replacement
	 * @return
	 */
	public String getNextMsisdn() throws IllegalArgumentException {
		if (replacementCount < 999999) {
			replacementCount = replacementCount + 1;
			return String.format("%s%06d", prefix, replacementCount);
		} else {
			throw new IllegalArgumentException("No more replacement MSISDNs are available to use");
		}
	}

}
