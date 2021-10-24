CREATE TABLE `people_per_project` (
	`Project` VARCHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	`User` CHAR(50) NOT NULL DEFAULT '' COLLATE 'latin1_swedish_ci',
	`Role` INT(11) NOT NULL DEFAULT '0',
	PRIMARY KEY (`Project`, `User`) USING BTREE,
	INDEX `FK__projects` (`User`) USING BTREE,
	CONSTRAINT `FK__projects_2` FOREIGN KEY (`Project`) REFERENCES `egh400_test`.`projects` (`Project_Name`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;

CREATE TABLE `projects` (
	`Project_Name` VARCHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	`Created_by` CHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	`Description` TEXT(65535) NOT NULL DEFAULT '' COLLATE 'latin1_swedish_ci',
	`Project_Creation_date` DATE NULL DEFAULT NULL,
	PRIMARY KEY (`Project_Name`) USING BTREE,
	INDEX `users` (`Created_by`) USING BTREE,
	CONSTRAINT `users` FOREIGN KEY (`Created_by`) REFERENCES `egh400_test`.`users` (`ID`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;
CREATE TABLE `tasks` (
	`Task_name` VARCHAR(100) NOT NULL COLLATE 'latin1_swedish_ci',
	`Project` VARCHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	`Assigned_User` CHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	`Created_By` CHAR(50) NOT NULL DEFAULT '' COLLATE 'latin1_swedish_ci',
	`Status_int` INT(11) NULL DEFAULT NULL,
	`start_date` DATE NULL DEFAULT NULL,
	`end_date` DATE NULL DEFAULT NULL,
	`Description` TEXT(65535) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	PRIMARY KEY (`Task_name`, `Project`) USING BTREE,
	INDEX `FK__people_per_project_2` (`Project`) USING BTREE,
	CONSTRAINT `FK__people_per_project_2` FOREIGN KEY (`Project`) REFERENCES `egh400_test`.`people_per_project` (`Project`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;
CREATE TABLE `users` (
	`ID` CHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	`salt` VARCHAR(385) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	`Password` VARCHAR(385) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	PRIMARY KEY (`ID`) USING BTREE
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;

CREATE TABLE `minortasks` (
	`root_task` VARCHAR(100) NOT NULL COLLATE 'latin1_swedish_ci',
	`Task_name` VARCHAR(100) NOT NULL COLLATE 'latin1_swedish_ci',
	`Project` VARCHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	`Assigned_User` CHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	`Created_By` CHAR(50) NOT NULL DEFAULT '' COLLATE 'latin1_swedish_ci',
	`Status_int` INT(11) NULL DEFAULT NULL,
	`start_date` DATE NULL DEFAULT NULL,
	`end_date` DATE NULL DEFAULT NULL,
	`Description` TEXT(65535) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	PRIMARY KEY (`root_task`, `Task_name`, `Project`) USING BTREE
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
;