CREATE TABLE `minorTasks` (
	`root_task` VARCHAR(100) NOT NULL COLLATE 'latin1_swedish_ci',
	`Task_name` VARCHAR(100) NOT NULL COLLATE 'latin1_swedish_ci',
	`Project` VARCHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	`Assigned_User` CHAR(50) NOT NULL COLLATE 'latin1_swedish_ci',
	`Created_By` CHAR(50) NOT NULL DEFAULT '' COLLATE 'latin1_swedish_ci',
	`Status_int` INT(11) NULL DEFAULT NULL,
	`start_date` DATE NULL DEFAULT NULL,
	`end_date` DATE NULL DEFAULT NULL,
	`Description` TEXT(65535) NULL DEFAULT NULL COLLATE 'latin1_swedish_ci',
	PRIMARY KEY (`root_task`,`Task_name`, `Project`))
;
