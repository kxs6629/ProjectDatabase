-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema project_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema project_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `project_db` DEFAULT CHARACTER SET utf8 ;
USE `project_db` ;

-- -----------------------------------------------------
-- Table `project_db`.`Person`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_db`.`Person` ;

CREATE TABLE IF NOT EXISTS `project_db`.`Person` (
  `personId` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `personType` VARCHAR(15) NOT NULL,
  `Name` VARCHAR(30) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`personId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project_db`.`MajorList`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_db`.`MajorList` ;

CREATE TABLE IF NOT EXISTS `project_db`.`MajorList` (
  `majorTitle` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`majorTitle`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project_db`.`Student`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_db`.`Student` ;

CREATE TABLE IF NOT EXISTS `project_db`.`Student` (
  `year` INT NOT NULL,
  `Person_personId` INT UNSIGNED NOT NULL,
  `MajorList_majorTitle` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Person_personId`, `MajorList_majorTitle`),
  INDEX `fk_Student_MajorList1_idx` (`MajorList_majorTitle` ASC),
  CONSTRAINT `fk_Student_Person1`
    FOREIGN KEY (`Person_personId`)
    REFERENCES `project_db`.`Person` (`personId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Student_MajorList1`
    FOREIGN KEY (`MajorList_majorTitle`)
    REFERENCES `project_db`.`MajorList` (`majorTitle`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project_db`.`DepartmentList`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_db`.`DepartmentList` ;

CREATE TABLE IF NOT EXISTS `project_db`.`DepartmentList` (
  `deptName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`deptName`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project_db`.`Faculty`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_db`.`Faculty` ;

CREATE TABLE IF NOT EXISTS `project_db`.`Faculty` (
  `Person_personId` INT UNSIGNED NOT NULL,
  `DepartmentList_deptName` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`Person_personId`, `DepartmentList_deptName`),
  INDEX `fk_Faculty_DepartmentList1_idx` (`DepartmentList_deptName` ASC),
  CONSTRAINT `fk_Faculty_Person1`
    FOREIGN KEY (`Person_personId`)
    REFERENCES `project_db`.`Person` (`personId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Faculty_DepartmentList1`
    FOREIGN KEY (`DepartmentList_deptName`)
    REFERENCES `project_db`.`DepartmentList` (`deptName`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project_db`.`Interest`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_db`.`Interest` ;

CREATE TABLE IF NOT EXISTS `project_db`.`Interest` (
  `title` VARCHAR(50) NOT NULL,
  `desc` VARCHAR(250) NOT NULL,
  PRIMARY KEY (`title`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project_db`.`Keyword`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_db`.`Keyword` ;

CREATE TABLE IF NOT EXISTS `project_db`.`Keyword` (
  `title` VARCHAR(50) NOT NULL,
  `desc` VARCHAR(250) NOT NULL,
  PRIMARY KEY (`title`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project_db`.`Project`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_db`.`Project` ;

CREATE TABLE IF NOT EXISTS `project_db`.`Project` (
  `idProject` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Faculty_Person_personId` INT UNSIGNED NOT NULL,
  `title` VARCHAR(50) NOT NULL,
  `summary` VARCHAR(250) NOT NULL,
  `start` DATE NOT NULL,
  `end` DATE NULL DEFAULT NULL,
  `ongoing` TINYINT(1) NOT NULL,
  PRIMARY KEY (`idProject`, `Faculty_Person_personId`),
  INDEX `fk_Project_Faculty1_idx` (`Faculty_Person_personId` ASC),
  CONSTRAINT `fk_Project_Faculty1`
    FOREIGN KEY (`Faculty_Person_personId`)
    REFERENCES `project_db`.`Faculty` (`Person_personId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project_db`.`Person_has_Keyword`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_db`.`Person_has_Keyword` ;

CREATE TABLE IF NOT EXISTS `project_db`.`Person_has_Keyword` (
  `Person_personId` INT UNSIGNED NOT NULL,
  `Keyword_title` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`Person_personId`, `Keyword_title`),
  INDEX `fk_Person_has_Keyword_Keyword2_idx` (`Keyword_title` ASC),
  INDEX `fk_Person_has_Keyword_Person2_idx` (`Person_personId` ASC),
  CONSTRAINT `fk_Person_has_Keyword_Person2`
    FOREIGN KEY (`Person_personId`)
    REFERENCES `project_db`.`Person` (`personId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Person_has_Keyword_Keyword2`
    FOREIGN KEY (`Keyword_title`)
    REFERENCES `project_db`.`Keyword` (`title`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project_db`.`Person_has_Interest`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_db`.`Person_has_Interest` ;

CREATE TABLE IF NOT EXISTS `project_db`.`Person_has_Interest` (
  `Person_personId` INT UNSIGNED NOT NULL,
  `Interest_title` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`Person_personId`, `Interest_title`),
  INDEX `fk_Person_has_Interest_Interest2_idx` (`Interest_title` ASC),
  INDEX `fk_Person_has_Interest_Person2_idx` (`Person_personId` ASC),
  CONSTRAINT `fk_Person_has_Interest_Person2`
    FOREIGN KEY (`Person_personId`)
    REFERENCES `project_db`.`Person` (`personId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Person_has_Interest_Interest2`
    FOREIGN KEY (`Interest_title`)
    REFERENCES `project_db`.`Interest` (`title`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `project_db`.`StudentList`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `project_db`.`StudentList` ;

CREATE TABLE IF NOT EXISTS `project_db`.`StudentList` (
  `Student_Person_personId` INT UNSIGNED NOT NULL,
  `Project_idProject` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`Student_Person_personId`, `Project_idProject`),
  INDEX `fk_Student_has_Project_Project1_idx` (`Project_idProject` ASC),
  INDEX `fk_Student_has_Project_Student1_idx` (`Student_Person_personId` ASC),
  CONSTRAINT `fk_Student_has_Project_Student1`
    FOREIGN KEY (`Student_Person_personId`)
    REFERENCES `project_db`.`Student` (`Person_personId`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Student_has_Project_Project1`
    FOREIGN KEY (`Project_idProject`)
    REFERENCES `project_db`.`Project` (`idProject`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `project_db`.`Person`
-- -----------------------------------------------------
START TRANSACTION;
USE `project_db`;
INSERT INTO `project_db`.`Person` (`personId`, `personType`, `Name`, `email`) VALUES (1, 'Faculty', 'Jim Habermas', 'Jim.Habermas@rit.edu');
INSERT INTO `project_db`.`Person` (`personId`, `personType`, `Name`, `email`) VALUES (2, 'Student', 'Kenny Scott', 'kxs6629@rit.edu');
INSERT INTO `project_db`.`Person` (`personId`, `personType`, `Name`, `email`) VALUES (3, 'Faculty', 'Dan Bogaard', 'dan.bogaard@rit.edu');
INSERT INTO `project_db`.`Person` (`personId`, `personType`, `Name`, `email`) VALUES (4, 'Student', 'Jesenia Roberts', 'jtr5657@rit.edu');

COMMIT;


-- -----------------------------------------------------
-- Data for table `project_db`.`MajorList`
-- -----------------------------------------------------
START TRANSACTION;
USE `project_db`;
INSERT INTO `project_db`.`MajorList` (`majorTitle`) VALUES ('Web and Mobile Computing');
INSERT INTO `project_db`.`MajorList` (`majorTitle`) VALUES ('New Media Design');
INSERT INTO `project_db`.`MajorList` (`majorTitle`) VALUES ('Software Engineering');
INSERT INTO `project_db`.`MajorList` (`majorTitle`) VALUES ('Computer Science');
INSERT INTO `project_db`.`MajorList` (`majorTitle`) VALUES ('Game Design and Development');

COMMIT;


-- -----------------------------------------------------
-- Data for table `project_db`.`Student`
-- -----------------------------------------------------
START TRANSACTION;
USE `project_db`;
INSERT INTO `project_db`.`Student` (`year`, `Person_personId`, `MajorList_majorTitle`) VALUES (3, 2, 'Web and Mobile Computing');
INSERT INTO `project_db`.`Student` (`year`, `Person_personId`, `MajorList_majorTitle`) VALUES (2, 4, 'Web and Mobile Computing');

COMMIT;


-- -----------------------------------------------------
-- Data for table `project_db`.`DepartmentList`
-- -----------------------------------------------------
START TRANSACTION;
USE `project_db`;
INSERT INTO `project_db`.`DepartmentList` (`deptName`) VALUES ('iSchool');
INSERT INTO `project_db`.`DepartmentList` (`deptName`) VALUES ('Computing Security');
INSERT INTO `project_db`.`DepartmentList` (`deptName`) VALUES ('Interactive Games and Media');
INSERT INTO `project_db`.`DepartmentList` (`deptName`) VALUES ('Computer Science');
INSERT INTO `project_db`.`DepartmentList` (`deptName`) VALUES ('Software Engineering');

COMMIT;


-- -----------------------------------------------------
-- Data for table `project_db`.`Faculty`
-- -----------------------------------------------------
START TRANSACTION;
USE `project_db`;
INSERT INTO `project_db`.`Faculty` (`Person_personId`, `DepartmentList_deptName`) VALUES (1, 'iSchool');
INSERT INTO `project_db`.`Faculty` (`Person_personId`, `DepartmentList_deptName`) VALUES (3, 'iSchool');

COMMIT;


-- -----------------------------------------------------
-- Data for table `project_db`.`Interest`
-- -----------------------------------------------------
START TRANSACTION;
USE `project_db`;
INSERT INTO `project_db`.`Interest` (`title`, `desc`) VALUES ('Web based Communication', 'Use of the internet to send and recieve information to other users');
INSERT INTO `project_db`.`Interest` (`title`, `desc`) VALUES ('Security', 'Protecting personal user information when using modern technologies');
INSERT INTO `project_db`.`Interest` (`title`, `desc`) VALUES ('Application Development', 'Development of applications that can be used on phones,computers, etc.');
INSERT INTO `project_db`.`Interest` (`title`, `desc`) VALUES ('Medical Technology', 'Developent of software to enhance existing technology in the medical field');

COMMIT;


-- -----------------------------------------------------
-- Data for table `project_db`.`Keyword`
-- -----------------------------------------------------
START TRANSACTION;
USE `project_db`;
INSERT INTO `project_db`.`Keyword` (`title`, `desc`) VALUES ('Professor', 'Teaches courses at RIT');
INSERT INTO `project_db`.`Keyword` (`title`, `desc`) VALUES ('Student', 'Taking courses at RIT');
INSERT INTO `project_db`.`Keyword` (`title`, `desc`) VALUES ('Web', 'Involved in development of web applications');
INSERT INTO `project_db`.`Keyword` (`title`, `desc`) VALUES ('Mobile', 'Involved in development of mobile applications');
INSERT INTO `project_db`.`Keyword` (`title`, `desc`) VALUES ('Software', 'Involved in development of software for computing');
INSERT INTO `project_db`.`Keyword` (`title`, `desc`) VALUES ('IoT', 'Internet of things related development. Includes smartwatches, smart tv\'s, smart homes, etc.');
INSERT INTO `project_db`.`Keyword` (`title`, `desc`) VALUES ('Database', 'Has experience working with back end databases for applications');
INSERT INTO `project_db`.`Keyword` (`title`, `desc`) VALUES ('Front-end', 'Focus on user experience design for applications');
INSERT INTO `project_db`.`Keyword` (`title`, `desc`) VALUES ('Back-end', 'Focus on the technologies used to make an application functional. Includes languages such as Java,C#,C++,etc.');

COMMIT;


-- -----------------------------------------------------
-- Data for table `project_db`.`Project`
-- -----------------------------------------------------
START TRANSACTION;
USE `project_db`;
INSERT INTO `project_db`.`Project` (`idProject`, `Faculty_Person_personId`, `title`, `summary`, `start`, `end`, `ongoing`) VALUES (1, 3, 'Journal Paper', 'Five-year Prospective Study of Pediatric Acute Otitis Media in Rochester, NY: Modeling Analysis of the Risk of Pneumococcal Colonization in the Nasopharynx and Infection.', '2019-12-1', '2019-12-3', 0);
INSERT INTO `project_db`.`Project` (`idProject`, `Faculty_Person_personId`, `title`, `summary`, `start`, `end`, `ongoing`) VALUES (2, 1, 'Project Database', 'Database that displays research projects', '2020-4-10', NULL, 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `project_db`.`Person_has_Keyword`
-- -----------------------------------------------------
START TRANSACTION;
USE `project_db`;
INSERT INTO `project_db`.`Person_has_Keyword` (`Person_personId`, `Keyword_title`) VALUES (1, 'Professor');
INSERT INTO `project_db`.`Person_has_Keyword` (`Person_personId`, `Keyword_title`) VALUES (1, 'Web');
INSERT INTO `project_db`.`Person_has_Keyword` (`Person_personId`, `Keyword_title`) VALUES (1, 'Mobile');
INSERT INTO `project_db`.`Person_has_Keyword` (`Person_personId`, `Keyword_title`) VALUES (2, 'Web');
INSERT INTO `project_db`.`Person_has_Keyword` (`Person_personId`, `Keyword_title`) VALUES (2, 'Mobile');
INSERT INTO `project_db`.`Person_has_Keyword` (`Person_personId`, `Keyword_title`) VALUES (2, 'Student');
INSERT INTO `project_db`.`Person_has_Keyword` (`Person_personId`, `Keyword_title`) VALUES (3, 'Professor');
INSERT INTO `project_db`.`Person_has_Keyword` (`Person_personId`, `Keyword_title`) VALUES (3, 'Web');
INSERT INTO `project_db`.`Person_has_Keyword` (`Person_personId`, `Keyword_title`) VALUES (3, 'Mobile');
INSERT INTO `project_db`.`Person_has_Keyword` (`Person_personId`, `Keyword_title`) VALUES (3, 'Back-end');
INSERT INTO `project_db`.`Person_has_Keyword` (`Person_personId`, `Keyword_title`) VALUES (1, 'Database');

COMMIT;


-- -----------------------------------------------------
-- Data for table `project_db`.`Person_has_Interest`
-- -----------------------------------------------------
START TRANSACTION;
USE `project_db`;
INSERT INTO `project_db`.`Person_has_Interest` (`Person_personId`, `Interest_title`) VALUES (1, 'Medical Technology');
INSERT INTO `project_db`.`Person_has_Interest` (`Person_personId`, `Interest_title`) VALUES (4, 'Medical Technology');
INSERT INTO `project_db`.`Person_has_Interest` (`Person_personId`, `Interest_title`) VALUES (2, 'Application Development');
INSERT INTO `project_db`.`Person_has_Interest` (`Person_personId`, `Interest_title`) VALUES (1, 'Web based Communication');
INSERT INTO `project_db`.`Person_has_Interest` (`Person_personId`, `Interest_title`) VALUES (2, 'Web based Communication');
INSERT INTO `project_db`.`Person_has_Interest` (`Person_personId`, `Interest_title`) VALUES (3, 'Web based Communication');
INSERT INTO `project_db`.`Person_has_Interest` (`Person_personId`, `Interest_title`) VALUES (4, 'Web based Communication');

COMMIT;


-- -----------------------------------------------------
-- Data for table `project_db`.`StudentList`
-- -----------------------------------------------------
START TRANSACTION;
USE `project_db`;
INSERT INTO `project_db`.`StudentList` (`Student_Person_personId`, `Project_idProject`) VALUES (2, 2);

COMMIT;

