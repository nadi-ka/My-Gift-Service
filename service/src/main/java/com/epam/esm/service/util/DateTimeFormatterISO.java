package com.epam.esm.service.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatterISO {
	
	private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String ZONE = "Europe/Minsk";
	
	public static LocalDateTime createAndformatDateTime() {
	    ZonedDateTime zoneEuropeMinsk = ZonedDateTime.now(ZoneId.of(ZONE));
	    LocalDateTime ldt = zoneEuropeMinsk.toLocalDateTime();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_PATTERN);
	    zoneEuropeMinsk.format(formatter);
	    return ldt;
	}

}
