-- Modelo físico (SQL Server) - Parqueadero Autos Colombia, Iteración 1
-- PASO OBLIGATORIO: crear la base de datos (la app crea las tablas sola).
-- El resto del script es OPCIONAL y sirve como evidencia del modelo físico.

IF DB_ID('autos_colombia') IS NULL CREATE DATABASE autos_colombia;
GO
USE autos_colombia;
GO

CREATE TABLE rol (
  id_rol INT IDENTITY(1,1) PRIMARY KEY,
  nombre VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE usuario_sistema (
  id_usuario_sistema INT IDENTITY(1,1) PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  activo BIT NOT NULL DEFAULT 1,
  id_rol INT NOT NULL,
  FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

CREATE TABLE cliente (
  id_cliente INT IDENTITY(1,1) PRIMARY KEY,
  tipo_documento VARCHAR(5) NOT NULL,
  documento VARCHAR(20) NOT NULL UNIQUE,
  nombres VARCHAR(80) NOT NULL,
  apellidos VARCHAR(80) NOT NULL,
  telefono VARCHAR(20),
  email VARCHAR(100)
);

CREATE TABLE tipo_vehiculo (
  id_tipo INT IDENTITY(1,1) PRIMARY KEY,
  nombre VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE vehiculo (
  id_vehiculo INT IDENTITY(1,1) PRIMARY KEY,
  placa VARCHAR(10) NOT NULL UNIQUE,
  marca VARCHAR(50),
  color VARCHAR(30),
  id_cliente INT NOT NULL,
  id_tipo INT NOT NULL,
  FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
  FOREIGN KEY (id_tipo) REFERENCES tipo_vehiculo(id_tipo)
);

CREATE TABLE mensualidad (
  id_mensualidad INT IDENTITY(1,1) PRIMARY KEY,
  fecha_inicio DATE NOT NULL,
  fecha_fin DATE NOT NULL,
  estado VARCHAR(15) NOT NULL DEFAULT 'ACTIVA',  -- ACTIVA, VENCIDA
  id_cliente INT NOT NULL,
  FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

CREATE TABLE celda (
  id_celda INT IDENTITY(1,1) PRIMARY KEY,
  codigo VARCHAR(10) NOT NULL UNIQUE,
  estado VARCHAR(15) NOT NULL DEFAULT 'DISPONIBLE', -- DISPONIBLE, OCUPADA, INACTIVA
  id_tipo INT NOT NULL,
  FOREIGN KEY (id_tipo) REFERENCES tipo_vehiculo(id_tipo)
);

CREATE TABLE registro_acceso (
  id_registro INT IDENTITY(1,1) PRIMARY KEY,
  fecha_hora_entrada DATETIME2 NOT NULL,
  fecha_hora_salida DATETIME2 NULL,
  minutos_permanencia INT NULL,
  id_vehiculo INT NOT NULL,
  id_celda INT NOT NULL,
  id_usuario_entrada INT NOT NULL,
  id_usuario_salida INT NULL,
  FOREIGN KEY (id_vehiculo) REFERENCES vehiculo(id_vehiculo),
  FOREIGN KEY (id_celda) REFERENCES celda(id_celda),
  FOREIGN KEY (id_usuario_entrada) REFERENCES usuario_sistema(id_usuario_sistema),
  FOREIGN KEY (id_usuario_salida) REFERENCES usuario_sistema(id_usuario_sistema)
);

CREATE TABLE novedad (
  id_novedad INT IDENTITY(1,1) PRIMARY KEY,
  tipo VARCHAR(30) NOT NULL,  -- DANO, OBJETO_OLVIDADO, INCIDENTE
  descripcion VARCHAR(1000) NOT NULL,
  fecha_hora DATETIME2 NOT NULL,
  id_registro INT NOT NULL,
  id_usuario_sistema INT NOT NULL,
  FOREIGN KEY (id_registro) REFERENCES registro_acceso(id_registro),
  FOREIGN KEY (id_usuario_sistema) REFERENCES usuario_sistema(id_usuario_sistema)
);

CREATE TABLE comprobante (
  id_comprobante INT IDENTITY(1,1) PRIMARY KEY,
  consecutivo INT NOT NULL UNIQUE,
  tipo VARCHAR(10) NOT NULL,  -- ENTRADA, SALIDA
  fecha_hora DATETIME2 NOT NULL,
  id_registro INT NOT NULL,
  FOREIGN KEY (id_registro) REFERENCES registro_acceso(id_registro)
);
GO
