-- -----------------------------------------------------
-- Data for table `Certificate_service`.`Tag`
-- -----------------------------------------------------
USE `Certificate_service`;
INSERT INTO `Certificate_service`.`Tag` (`Id`, `Name`, `Created_on`, `Updated_on`) VALUES (1, '#Sport', NULL, NULL);
INSERT INTO `Certificate_service`.`Tag` (`Id`, `Name`, `Created_on`, `Updated_on`) VALUES (2, '#Romance', NULL, NULL);

-- -----------------------------------------------------
-- Data for table `Certificate_service`.`GiftCertificate`
-- -----------------------------------------------------
USE `Certificate_service`;
INSERT INTO `Certificate_service`.`GiftCertificate` (`Id`, `Name`, `Description`, `Price`, `CreateDate`, `LastUpdateDate`, `Duration`) VALUES (1, 'Skydiving', 'For 1 person', 50.0, '2020-10-19 12:40:30', '2020-10-19 12:40:30', 90);
INSERT INTO `Certificate_service`.`GiftCertificate` (`Id`, `Name`, `Description`, `Price`, `CreateDate`, `LastUpdateDate`, `Duration`) VALUES (2, 'Skydiving', 'For 2 persons', 90.0, '2020-10-20 12:40:30', '2020-10-20 12:40:30', 90);
INSERT INTO `Certificate_service`.`GiftCertificate` (`Id`, `Name`, `Description`, `Price`, `CreateDate`, `LastUpdateDate`, `Duration`) VALUES (3, 'Dinner by candlelight ', 'For couple in the restorant \"Troffea\"', 10.50, '2020-10-21 12:40:30', '2020-10-19 12:40:30', 90);


-- -----------------------------------------------------
-- Data for table `Certificate_service`.`Tag-Certificate`
-- -----------------------------------------------------
USE `Certificate_service`;
INSERT INTO `Certificate_service`.`Tag_Certificate` (`IdTag`, `IdCertificate`) VALUES (1, 1);
INSERT INTO `Certificate_service`.`Tag_Certificate` (`IdTag`, `IdCertificate`) VALUES (1, 2);
INSERT INTO `Certificate_service`.`Tag_Certificate` (`IdTag`, `IdCertificate`) VALUES (2, 3);

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
INSERT INTO `Certificate_service`.`Purchase` (`Id`, `Id_user`, `Creation_date`, `Cost`) VALUES (1, 1, '2020-10-19 12:40:30', 150.00);
INSERT INTO `Certificate_service`.`Purchase` (`Id`, `Id_user`, `Creation_date`, `Cost`) VALUES (2, 1, '2020-10-21 12:40:45', 340.50);
INSERT INTO `Certificate_service`.`Purchase` (`Id`, `Id_user`, `Creation_date`, `Cost`) VALUES (3, 2, '2020-10-22 08:40:30', 176.90);
INSERT INTO `Certificate_service`.`Purchase` (`Id`, `Id_user`, `Creation_date`, `Cost`) VALUES (4, 2, '2020-10-23 05:40:22', 130.80);

-- -----------------------------------------------------
-- Data for table `Certificate_service`.`Purchase-Certificate`
-- -----------------------------------------------------
USE `Certificate_service`;
INSERT INTO `Certificate_service`.`Purchase_Certificate` (`Id_order`, `Id_certificate`) VALUES (1, 1);
INSERT INTO `Certificate_service`.`Purchase_Certificate` (`Id_order`, `Id_certificate`) VALUES (1, 3);
INSERT INTO `Certificate_service`.`Purchase_Certificate` (`Id_order`, `Id_certificate`) VALUES (2, 2);
INSERT INTO `Certificate_service`.`Purchase_Certificate` (`Id_order`, `Id_certificate`) VALUES (3, 1);
INSERT INTO `Certificate_service`.`Purchase_Certificate` (`Id_order`, `Id_certificate`) VALUES (3, 3);
INSERT INTO `Certificate_service`.`Purchase_Certificate` (`Id_order`, `Id_certificate`) VALUES (4, 1);
