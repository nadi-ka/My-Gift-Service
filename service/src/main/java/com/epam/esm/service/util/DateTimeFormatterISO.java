package com.epam.esm.service.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatterISO {
	
	private static final String formatPattern = "yyyy-MM-dd HH:mm:ss";
	private static final String zone = "Europe/Minsk";
	
	public static LocalDateTime createAndformatDateTime() {
	
	    ZonedDateTime zoneEuropeMinsk = ZonedDateTime.now(ZoneId.of(zone));

	    LocalDateTime ldt = zoneEuropeMinsk.toLocalDateTime();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
	    zoneEuropeMinsk.format(formatter);
	    
	    return ldt;
	
	}

}
