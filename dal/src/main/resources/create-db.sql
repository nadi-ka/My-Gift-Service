
-- -----------------------------------------------------
-- Schema GiftService
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `GiftService` ;

-- -----------------------------------------------------
-- Table `GiftService`.`Tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `GiftService`.`Tag` (
  `Id` BIGINT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Id`));


-- -----------------------------------------------------
-- Table `GiftService`.`GiftCertificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `GiftService`.`GiftCertificate` (
  `Id` BIGINT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `Description` VARCHAR(100) NOT NULL,
  `Price` DECIMAL(7,2) NOT NULL,
  `CreateDate` TIMESTAMP NOT NULL,
  `LastUpdateDate` TIMESTAMP NULL,
  `Duration` INT NOT NULL,
  PRIMARY KEY (`Id`));


-- -----------------------------------------------------
-- Table `GiftService`.`Tag-Certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `GiftService`.`Tag-Certificate` (
  `IdTag` BIGINT NOT NULL,
  `IdCertificate` BIGINT NOT NULL,
  
    FOREIGN KEY (`IdCertificate`)
    REFERENCES `GiftService`.`GiftCertificate` (`Id`),
 
    FOREIGN KEY (`IdTag`)
    REFERENCES `GiftService`.`Tag` (`Id`));


