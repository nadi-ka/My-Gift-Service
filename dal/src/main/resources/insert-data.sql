-- -----------------------------------------------------
-- Data for table `Certificate_service`.`Tag`
-- -----------------------------------------------------
USE `Certificate_service`;
INSERT INTO `Certificate_service`.`Tag` (`Id`, `Name`) VALUES (1, '#Sport');
INSERT INTO `Certificate_service`.`Tag` (`Id`, `Name`) VALUES (2, '#Romance');

-- -----------------------------------------------------
-- Data for table `Certificate_service`.`GiftCertificate`
-- -----------------------------------------------------
USE `Certificate_service`;
INSERT INTO `Certificate_service`.`GiftCertificate` (`Id`, `Name`, `Description`, `Price`, `CreateDate`, `LastUpdateDate`, `Duration`) VALUES (1, 'Skydiving', 'For 1 person', 50.0, '2020-10-19 12:40:30', '2020-10-19 12:40:30', 90);
INSERT INTO `Certificate_service`.`GiftCertificate` (`Id`, `Name`, `Description`, `Price`, `CreateDate`, `LastUpdateDate`, `Duration`) VALUES (2, 'Skydiving', 'For 2 persons', 90.0, '2020-10-19 12:40:30', '2020-10-19 12:40:30', 90);
INSERT INTO `Certificate_service`.`GiftCertificate` (`Id`, `Name`, `Description`, `Price`, `CreateDate`, `LastUpdateDate`, `Duration`) VALUES (3, 'Dinner by candlelight ', 'For couple in the restorant \"Troffea\"', 10.50, '2020-10-19 12:40:30', '2020-10-19 12:40:30', 90);


-- -----------------------------------------------------
-- Data for table `Certificate_service`.`Tag-Certificate`
-- -----------------------------------------------------
USE `Certificate_service`;
INSERT INTO `Certificate_service`.`Tag-Certificate` (`IdTag`, `IdCertificate`) VALUES (1, 1);
INSERT INTO `Certificate_service`.`Tag-Certificate` (`IdTag`, `IdCertificate`) VALUES (1, 2);
INSERT INTO `Certificate_service`.`Tag-Certificate` (`IdTag`, `IdCertificate`) VALUES (2, 3);

-- -----------------------------------------------------
-- Data for table `Certificate_service`.`User`
-- -----------------------------------------------------
USE `Certificate_service`;
INSERT INTO `Certificate_service`.`User` (`Id`, `First_name`, `Last_name`, `Date_of_birth`, `Email`) VALUES (1, 'Marry', 'Smith', '1987-06-06', 'marry@gmail.com');
INSERT INTO `Certificate_service`.`User` (`Id`, `First_name`, `Last_name`, `Date_of_birth`, `Email`) VALUES (2, 'Peter', 'Varga', '1965-07-07', 'varga@gmail.com');

-- -----------------------------------------------------
-- Data for table `Certificate_service`.`Purchase`
-- -----------------------------------------------------
USE `Certificate_service`;
INSERT INTO `Certificate_service`.`Purchase` (`Id`, `Id_user`, `Creation_date`) VALUES (1, 1, '2020-10-19 12:40:30');
INSERT INTO `Certificate_service`.`Purchase` (`Id`, `Id_user`, `Creation_date`) VALUES (2, 1, '2020-10-21 12:40:45');
INSERT INTO `Certificate_service`.`Purchase` (`Id`, `Id_user`, `Creation_date`) VALUES (3, 2, '2020-10-22 08:40:30');
INSERT INTO `Certificate_service`.`Purchase` (`Id`, `Id_user`, `Creation_date`) VALUES (4, 2, '2020-10-23 05:40:22');

-- -----------------------------------------------------
-- Data for table `Certificate_service`.`Purchase-Certificate`
-- -----------------------------------------------------
USE `Certificate_service`;
INSERT INTO `Certificate_service`.`Purchase-Certificate` (`Id_order`, `Id_certificate`) VALUES (1, 1);
INSERT INTO `Certificate_service`.`Purchase-Certificate` (`Id_order`, `Id_certificate`) VALUES (1, 3);
INSERT INTO `Certificate_service`.`Purchase-Certificate` (`Id_order`, `Id_certificate`) VALUES (2, 2);
INSERT INTO `Certificate_service`.`Purchase-Certificate` (`Id_order`, `Id_certificate`) VALUES (3, 1);
INSERT INTO `Certificate_service`.`Purchase-Certificate` (`Id_order`, `Id_certificate`) VALUES (3, 3);
INSERT INTO `Certificate_service`.`Purchase-Certificate` (`Id_order`, `Id_certificate`) VALUES (4, 1);
