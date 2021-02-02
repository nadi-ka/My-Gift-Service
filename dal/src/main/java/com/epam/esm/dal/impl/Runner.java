package com.epam.esm.dal.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Purchase;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

@SpringBootApplication
@ComponentScan("com")
public class Runner {

	private Random random = new Random();
	private List<BigDecimal> prices = new ArrayList<>();

	public Runner() {
		
		prices.add(new BigDecimal(59.00));
		prices.add(new BigDecimal(187.05));
		prices.add(new BigDecimal(234.00));
		prices.add(new BigDecimal(98.40));
		prices.add(new BigDecimal(87.50));
		prices.add(new BigDecimal(66.00));
		prices.add(new BigDecimal(155.00));
		prices.add(new BigDecimal(70.00));
		prices.add(new BigDecimal(221.50));
		prices.add(new BigDecimal(301.40));

		
	}

	public BigDecimal getPrice() {
		int index = random.nextInt(prices.size());
		BigDecimal price = prices.get(index);
		return price;
	}

	public void addPurchases(PurchaseDaoImpl purchaseDao) {
		int certId = 26;
		
		for (long i = 15; i < 1000; i++) {
		
			Purchase purchase = new Purchase();
			purchase.setUser(new User(i));
			purchase.setCreationDate(getTimestamp());
			purchase.setCost(getPrice());
			
			List<GiftCertificate> certs = new ArrayList<GiftCertificate>();
			GiftCertificate certToAdd = new GiftCertificate();
			certToAdd.setId(certId);
			certId = certId + 1;
			certs.add(certToAdd);
			
			purchase.setCertificates(certs);
			
			purchaseDao.addPurchase(purchase);

			System.out.println("Purchase_Added" + i + ": " + purchase);
		}
	}
	
	private LocalDateTime getTimestamp() {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		String zone = "Europe/Minsk";
		ZonedDateTime zoneEuropeMinsk = ZonedDateTime.now(ZoneId.of(zone));
		LocalDateTime ldt = zoneEuropeMinsk.toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		zoneEuropeMinsk.format(formatter);
		return ldt;
	}

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(Runner.class, args);
		PurchaseDaoImpl purchaseDao = context.getBean(PurchaseDaoImpl.class);

		Runner runner = new Runner();
		runner.addPurchases(purchaseDao);

		context.close();
	}

}
