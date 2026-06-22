CREATE DATABASE IF NOT EXISTS gym_platform;
USE gym_platform;

-- ============================================================================
-- 1. USER SERVICE ENTITIES (Factorisés pour l'authentification)
-- ============================================================================

-- Table principale pour l'authentification et la gestion des accès
CREATE TABLE `User` (
    `ID` INT AUTO_INCREMENT PRIMARY KEY,
    `Username` VARCHAR(50) NOT NULL UNIQUE,
    `Password` VARCHAR(255) NOT NULL, -- Stockage sécurisé du hash
    `Role` ENUM('Member', 'Coach', 'Admin') NOT NULL, -- Indique le type d'utilisateur
    `RoleID` INT NOT NULL -- Clé primaire du Member, Coach, ou Admin correspondant
);

-- Table : Member
CREATE TABLE `Member` (
    `ID` INT AUTO_INCREMENT PRIMARY KEY,
    `Surname` VARCHAR(100) NOT NULL,
    `Name` VARCHAR(100) NOT NULL,
    `Email` VARCHAR(150) NOT NULL UNIQUE
);

-- Table : Coach
CREATE TABLE `Coach` (
    `ID` INT AUTO_INCREMENT PRIMARY KEY,
    `Surname` VARCHAR(100) NOT NULL,
    `Name` VARCHAR(100) NOT NULL,
    `Email` VARCHAR(150) NOT NULL UNIQUE
);

-- Table : Admin
CREATE TABLE `Admin` (
    `ID` INT AUTO_INCREMENT PRIMARY KEY
);

-- Ajout des contraintes d'intégrité référentielle (Clés étrangères de User vers les profils)
-- Note : Ces clés ne peuvent pas être définies directement dans le CREATE TABLE car 
-- les tables Member, Coach et Admin doivent exister au préalable.
ALTER TABLE `User` 
    ADD CONSTRAINT `FK_User_Member` FOREIGN KEY (`RoleID`) REFERENCES `Member`(`ID`) ON DELETE CASCADE,
    ADD CONSTRAINT `FK_User_Coach` FOREIGN KEY (`RoleID`) REFERENCES `Coach`(`ID`) ON DELETE CASCADE,
    ADD CONSTRAINT `FK_User_Admin` FOREIGN KEY (`RoleID`) REFERENCES `Admin`(`ID`) ON DELETE CASCADE;


-- ============================================================================
-- 2. CATALOG SERVICE ENTITIES
-- ============================================================================

-- Table : Lesson
CREATE TABLE `Lesson` (
    `ID` INT AUTO_INCREMENT PRIMARY KEY,
    `Name` VARCHAR(150) NOT NULL,
    `Description` TEXT,
    `Duration` INT NOT NULL, -- En minutes
    `Type` VARCHAR(100),
    `CoachID` INT,
    CONSTRAINT `FK_Lesson_Coach` FOREIGN KEY (`CoachID`) REFERENCES `Coach`(`ID`) ON DELETE SET NULL
);

-- Table : Planning (Créneaux de cours)
CREATE TABLE `Planning` (
    `ID` INT AUTO_INCREMENT PRIMARY KEY,
    `Date` DATE NOT NULL,
    `Time` TIME NOT NULL,
    `Capacity` INT NOT NULL,
    `Status` VARCHAR(50) NOT NULL,
    `LessonID` INT NOT NULL,
    CONSTRAINT `FK_Planning_Lesson` FOREIGN KEY (`LessonID`) REFERENCES `Lesson`(`ID`) ON DELETE CASCADE
);


-- ============================================================================
-- 3. BOOKING SERVICE ENTITIES
-- ============================================================================

-- Table : Subscription
CREATE TABLE `Subscription` (
    `ID` INT AUTO_INCREMENT PRIMARY KEY,
    `Price` DECIMAL(10, 2) NOT NULL,
    `Status` VARCHAR(50) NOT NULL,
    `MemberID` INT NOT NULL, -- Déduit de List<SubscriptionID> chez Member
    CONSTRAINT `FK_Subscription_Member` FOREIGN KEY (`MemberID`) REFERENCES `Member`(`ID`) ON DELETE CASCADE
);

-- Table d'association : Subscription_Planning (Pour List<PlanningID> dans Subscription)
CREATE TABLE `Subscription_Planning` (
    `SubscriptionID` INT NOT NULL,
    `PlanningID` INT NOT NULL,
    PRIMARY KEY (`SubscriptionID`, `PlanningID`),
    CONSTRAINT `FK_SubPlan_Subscription` FOREIGN KEY (`SubscriptionID`) REFERENCES `Subscription`(`ID`) ON DELETE CASCADE,
    CONSTRAINT `FK_SubPlan_Planning` FOREIGN KEY (`PlanningID`) REFERENCES `Planning`(`ID`) ON DELETE CASCADE
);

-- Table : Waitlist (Liste d'attente)
CREATE TABLE `Waitlist` (
    `ID` INT AUTO_INCREMENT PRIMARY KEY,
    `SubscriptionID` INT, -- Permet de lier une liste d'attente à un abonnement
    CONSTRAINT `FK_Waitlist_Subscription` FOREIGN KEY (`SubscriptionID`) REFERENCES `Subscription`(`ID`) ON DELETE SET NULL
);

-- Table d'association : Waitlist_Entries (Pour gérer la Map<Timestamp, MemberID>)
CREATE TABLE `Waitlist_Entries` (
    `WaitlistID` INT NOT NULL,
    `MemberID` INT NOT NULL,
    `JoinedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Clé de la Map (Timestamp)
    PRIMARY KEY (`WaitlistID`, `MemberID`),
    CONSTRAINT `FK_WaitEntries_Waitlist` FOREIGN KEY (`WaitlistID`) REFERENCES `Waitlist`(`ID`) ON DELETE CASCADE,
    CONSTRAINT `FK_WaitEntries_Member` FOREIGN KEY (`MemberID`) REFERENCES `Member`(`ID`) ON DELETE CASCADE
);


-- ============================================================================
-- 4. NOTIFICATION SERVICE ENTITIES
-- ============================================================================

-- Table : Template
CREATE TABLE `Template` (
    `ID` INT AUTO_INCREMENT PRIMARY KEY,
    `Name` VARCHAR(150) NOT NULL,
    `Subject` VARCHAR(255) NOT NULL,
    `Content` TEXT NOT NULL
);

-- Table : Notification
CREATE TABLE `Notification` (
    `ID` INT AUTO_INCREMENT PRIMARY KEY,
    `Type` VARCHAR(50) NOT NULL,
    `Subject` VARCHAR(255) NOT NULL,
    `Content` TEXT NOT NULL,
    `SendDate` DATETIME NOT NULL,
    `TemplateID` INT,
    CONSTRAINT `FK_Notification_Template` FOREIGN KEY (`TemplateID`) REFERENCES `Template`(`ID`) ON DELETE SET NULL
);

-- Table d'association : Notification_User (Pour List<UserID> dans Notification)
CREATE TABLE `Notification_User` (
    `NotificationID` INT NOT NULL,
    `UserID` INT NOT NULL,
    PRIMARY KEY (`NotificationID`, `UserID`),
    CONSTRAINT `FK_NotifUser_Notification` FOREIGN KEY (`NotificationID`) REFERENCES `Notification`(`ID`) ON DELETE CASCADE,
    CONSTRAINT `FK_NotifUser_User` FOREIGN KEY (`UserID`) REFERENCES `User`(`ID`) ON DELETE CASCADE
);