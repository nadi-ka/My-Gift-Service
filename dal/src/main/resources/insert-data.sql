-- -----------------------------------------------------
-- Data for table `GiftService`.`Tag`
-- -----------------------------------------------------

USE `GiftService`;
INSERT INTO `GiftService`.`Tag` (`Id`, `Name`) VALUES (1, '#Sport');
INSERT INTO `GiftService`.`Tag` (`Id`, `Name`) VALUES (2, '#Romance');

-- -----------------------------------------------------
-- Data for table `GiftService`.`GiftCertificate`
-- -----------------------------------------------------

USE `GiftService`;
INSERT INTO `GiftService`.`GiftCertificate` (`Id`, `Name`, `Description`, `Price`, `CreateDate`, `LastUpdateDate`, `Duration`) VALUES (1, 'Skydiving', 'For 1 person, jump with the instructor', 75.00, '2020-10-19 11:06:43', '2020-10-19 11:06:43', 90);
INSERT INTO `GiftService`.`GiftCertificate` (`Id`, `Name`, `Description`, `Price`, `CreateDate`, `LastUpdateDate`, `Duration`) VALUES (2, 'Skydiving', 'For 2 persons', 90.35, '2020-10-20 05:06:43', '2020-10-20 06:06:43', 90);
INSERT INTO `GiftService`.`GiftCertificate` (`Id`, `Name`, `Description`, `Price`, `CreateDate`, `LastUpdateDate`, `Duration`) VALUES (3, 'Dinner by candlelight ', 'For couple in the restorant \"Troffea\"', 70.55, '2020-10-21 09:06:43', '2020-10-21 09:06:43', 90);


-- -----------------------------------------------------
-- Data for table `GiftService`.`Tag-Certificate`
-- -----------------------------------------------------

USE `GiftService`;
INSERT INTO `GiftService`.`Tag-Certificate` (`IdTag`, `IdCertificate`) VALUES (1, 1);
INSERT INTO `GiftService`.`Tag-Certificate` (`IdTag`, `IdCertificate`) VALUES (1, 2);
INSERT INTO `GiftService`.`Tag-Certificate` (`IdTag`, `IdCertificate`) VALUES (2, 3);

