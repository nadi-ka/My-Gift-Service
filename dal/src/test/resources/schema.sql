-- -----------------------------------------------------
-- Schema Certificate_service
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `Certificate_service` ;

-- -----------------------------------------------------
-- Schema Certificate_service
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Certificate_service`;
-- -----------------------------------------------------
-- Schema new_schema1
-- -----------------------------------------------------
USE `Certificate_service` ;

-- -----------------------------------------------------
-- Table `Certificate_service`.`Tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Certificate_service`.`Tag` (
  `Id` BIGINT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Created_on` TIMESTAMP NULL,
  `Updated_on` TIMESTAMP NULL,
  PRIMARY KEY (`Id`));

-- -----------------------------------------------------
-- Table `Certificate_service`.`GiftCertificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Certificate_service`.`GiftCertificate` (
  `Id` BIGINT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Description` VARCHAR(100) NOT NULL,
  `Price` DECIMAL(7,2) NOT NULL,
  `CreateDate` TIMESTAMP NOT NULL,
  `LastUpdateDate` TIMESTAMP NULL,
  `Duration` INT NOT NULL,
  PRIMARY KEY (`Id`));

-- -----------------------------------------------------
-- Table `Certificate_service`.`Tag-Certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Certificate_service`.`Tag_Certificate` (
  `IdTag` BIGINT NOT NULL,
  `IdCertificate` BIGINT NOT NULL,
  PRIMARY KEY (`IdTag`, `IdCertificate`),
  CONSTRAINT `GiftCertificateTag_CertificateFK`
    FOREIGN KEY (`IdCertificate`)
    REFERENCES `Certificate_service`.`GiftCertificate` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `TagTag_CertificateFK`
    FOREIGN KEY (`IdTag`)
    REFERENCES `Certificate_service`.`Tag` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- Table `Certificate_service`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Certificate_service`.`User` (
  `Id` BIGINT NOT NULL AUTO_INCREMENT,
  `First_name` VARCHAR(45) NOT NULL,
  `Last_name` VARCHAR(45) NOT NULL,
  `Date_of_birth` DATE NOT NULL,
  `Email` VARCHAR(45) NOT NULL,
  `Created_on` TIMESTAMP NULL,
  `Updated_on` TIMESTAMP NULL,
  PRIMARY KEY (`Id`));

-- -----------------------------------------------------
-- Table `Certificate_service`.`Purchase`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Certificate_service`.`Purchase` (
  `Id` BIGINT NOT NULL AUTO_INCREMENT,
  `Id_user` BIGINT NOT NULL,
  `Creation_date` TIMESTAMP NOT NULL,
  `Cost` DECIMAL(10,2) NOT NULL,
  `Created_on` TIMESTAMP NULL,
  `Updated_on` TIMESTAMP NULL,
  PRIMARY KEY (`Id`),
  CONSTRAINT `Order_User_FK`
    FOREIGN KEY (`Id_user`)
    REFERENCES `Certificate_service`.`User` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- Table `Certificate_service`.`Purchase-Certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Certificate_service`.`Purchase_Certificate` (
  `Id_order` BIGINT NOT NULL,
  `Id_certificate` BIGINT NOT NULL,
  PRIMARY KEY (`Id_order`, `Id_certificate`),
  CONSTRAINT `Purchase_M2M_FK`
    FOREIGN KEY (`Id_order`)
    REFERENCES `Certificate_service`.`Purchase` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `GiftCertificate_M2M_FK`
    FOREIGN KEY (`Id_certificate`)
    REFERENCES `Certificate_service`.`GiftCertificate` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
    
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
INSERT INTO `Certificate_service`.`User` (`Id`, `First_name`, `Last_name`, `Date_of_birth`, `Email`, `Created_on`, `Updated_on`) VALUES (1, 'Marry', 'Smith', '1987-06-06', 'marry@gmail.com', NULL, NULL);
INSERT INTO `Certificate_service`.`User` (`Id`, `First_name`, `Last_name`, `Date_of_birth`, `Email`, `Created_on`, `Updated_on`) VALUES (2, 'Peter', 'Varga', '1965-07-07', 'varga@gmail.com', NULL, NULL);

-- -----------------------------------------------------
-- Data for table `Certificate_service`.`Purchase`
-- -----------------------------------------------------
USE `Certificate_service`;
INSERT INTO `Certificate_service`.`Purchase` (`Id`, `Id_user`, `Creation_date`, `Cost`, `Created_on`, `Updated_on`) VALUES (1, 1, '2020-10-19 12:40:30', 150.00, NULL, NULL);
INSERT INTO `Certificate_service`.`Purchase` (`Id`, `Id_user`, `Creation_date`, `Cost`, `Created_on`, `Updated_on`) VALUES (2, 1, '2020-10-21 12:40:45', 340.50, NULL, NULL);
INSERT INTO `Certificate_service`.`Purchase` (`Id`, `Id_user`, `Creation_date`, `Cost`, `Created_on`, `Updated_on`) VALUES (3, 2, '2020-10-22 08:40:30', 176.90, NULL, NULL);
INSERT INTO `Certificate_service`.`Purchase` (`Id`, `Id_user`, `Creation_date`, `Cost`, `Created_on`, `Updated_on`) VALUES (4, 2, '2020-10-23 05:40:22', 130.80, NULL, NULL);

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
