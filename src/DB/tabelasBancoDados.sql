-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema Pizzaria
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Pizzaria
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Pizzaria` DEFAULT CHARACTER SET utf8 ;
USE `Pizzaria` ;

-- -----------------------------------------------------
-- Table `Pizzaria`.`Clientes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Pizzaria`.`Clientes` ;

CREATE TABLE IF NOT EXISTS `Pizzaria`.`Clientes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Nome` VARCHAR(100) NOT NULL,
  `Sobrenome` VARCHAR(100) NOT NULL,
  `Telefone` BIGINT(14) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Pizzaria`.`status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Pizzaria`.`status` ;

CREATE TABLE IF NOT EXISTS `Pizzaria`.`status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `situacao` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Pizzaria`.`ingredientes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Pizzaria`.`ingredientes` ;

CREATE TABLE IF NOT EXISTS `Pizzaria`.`ingredientes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Pizzaria`.`saboresIngredientes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Pizzaria`.`saboresIngredientes` ;

CREATE TABLE IF NOT EXISTS `Pizzaria`.`saboresIngredientes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_ingredientes` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_saboresIngredientes_1_idx` (`id_ingredientes` ASC),
  CONSTRAINT `fk_saboresIngredientes_1`
    FOREIGN KEY (`id_ingredientes`)
    REFERENCES `Pizzaria`.`ingredientes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Pizzaria`.`tipos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Pizzaria`.`tipos` ;

CREATE TABLE IF NOT EXISTS `Pizzaria`.`tipos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `classificacao` VARCHAR(45) NOT NULL,
  `precoDoTipo` DECIMAL(8,2) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Pizzaria`.`sabores`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Pizzaria`.`sabores` ;

CREATE TABLE IF NOT EXISTS `Pizzaria`.`sabores` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_saborIngredientes` INT NOT NULL,
  `id_tipos` INT NOT NULL,
  `nome` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_sabores_1_idx` (`id_saborIngredientes` ASC),
  INDEX `fk_sabores_2_idx` (`id_tipos` ASC),
  CONSTRAINT `fk_sabores_1`
    FOREIGN KEY (`id_saborIngredientes`)
    REFERENCES `Pizzaria`.`saboresIngredientes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sabores_2`
    FOREIGN KEY (`id_tipos`)
    REFERENCES `Pizzaria`.`tipos` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Pizzaria`.`formas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Pizzaria`.`formas` ;

CREATE TABLE IF NOT EXISTS `Pizzaria`.`formas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `formatoGeometrico` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Pizzaria`.`pizzas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Pizzaria`.`pizzas` ;

CREATE TABLE IF NOT EXISTS `Pizzaria`.`pizzas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tamanho` VARCHAR(45) NOT NULL,
  `id_sabores` INT NOT NULL,
  `id_formas` INT NOT NULL,
  `quantidade` DECIMAL(8,2) NOT NULL,
  `preco` DECIMAL(8,2) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_pizzas_1_idx` (`id_sabores` ASC),
  INDEX `fk_pizzas_2_idx` (`id_formas` ASC),
  CONSTRAINT `fk_pizzas_1`
    FOREIGN KEY (`id_sabores`)
    REFERENCES `Pizzaria`.`sabores` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pizzas_2`
    FOREIGN KEY (`id_formas`)
    REFERENCES `Pizzaria`.`formas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Pizzaria`.`pedidosStatus`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Pizzaria`.`pedidosStatus` ;

CREATE TABLE IF NOT EXISTS `Pizzaria`.`pedidosStatus` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_status` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_pedidoStatus_idx` (`id_status` ASC),
  CONSTRAINT `fk_pedidoStatus`
    FOREIGN KEY (`id_status`)
    REFERENCES `Pizzaria`.`status` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Pizzaria`.`pedidosPizzas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Pizzaria`.`pedidosPizzas` ;

CREATE TABLE IF NOT EXISTS `Pizzaria`.`pedidosPizzas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_pizzas` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_pedidosPizzas_1_idx` (`id_pizzas` ASC),
  CONSTRAINT `fk_pedidosPizzas_1`
    FOREIGN KEY (`id_pizzas`)
    REFERENCES `Pizzaria`.`pizzas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Pizzaria`.`pedidos`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Pizzaria`.`pedidos` ;

CREATE TABLE IF NOT EXISTS `Pizzaria`.`pedidos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `id_cliente` INT NOT NULL,
  `id_pedidosStatus` INT NOT NULL,
  `id_pedidosPizzas` INT NOT NULL,
  `precoTotal` DECIMAL(8,2) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_pedidos_1_idx` (`id_cliente` ASC),
  INDEX `fk_pedidos_2_idx` (`id_pedidosStatus` ASC),
  INDEX `fk_pedidos_3_idx` (`id_pedidosPizzas` ASC),
  CONSTRAINT `fk_pedidos_1`
    FOREIGN KEY (`id_cliente`)
    REFERENCES `Pizzaria`.`Clientes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedidos_2`
    FOREIGN KEY (`id_pedidosStatus`)
    REFERENCES `Pizzaria`.`pedidosStatus` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedidos_3`
    FOREIGN KEY (`id_pedidosPizzas`)
    REFERENCES `Pizzaria`.`pedidosPizzas` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
