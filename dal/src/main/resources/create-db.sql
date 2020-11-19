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
CREATE TABLE IF NOT EXISTS `Certificate_service`.`Tag-Certificate` (
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
  PRIMARY KEY (`Id`));

-- -----------------------------------------------------
-- Table `Certificate_service`.`Purchase`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Certificate_service`.`Purchase` (
  `Id` BIGINT NOT NULL AUTO_INCREMENT,
  `Id_user` BIGINT NOT NULL,
  `Creation_date` TIMESTAMP NOT NULL,
  PRIMARY KEY (`Id`),
  CONSTRAINT `Order_User_FK`
    FOREIGN KEY (`Id_user`)
    REFERENCES `Certificate_service`.`User` (`Id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

-- -----------------------------------------------------
-- Table `Certificate_service`.`Purchase-Certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Certificate_service`.`Purchase-Certificate` (
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

