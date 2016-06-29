/*==============================================================*/
/* DBMS name:      Sybase SQL Anywhere 12                       */
/* Created on:     04-06-2016 15:48:25                          */
/*==============================================================*/

/*
if exists(select 1 from sys.sysforeignkey where role='FK_DADOS_PERTENECE_CRIATURA') then
    alter table DADOS
       delete foreign key FK_DADOS_PERTENECE_CRIATURA
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_PUZZLEDE_COMPONE_DADOS') then
    alter table PUZZLEDEDADOS
       delete foreign key FK_PUZZLEDE_COMPONE_DADOS
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_PUZZLEDE_TIENEN_USUARIOS') then
    alter table PUZZLEDEDADOS
       delete foreign key FK_PUZZLEDE_TIENEN_USUARIOS
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_USUARIOS_POSEE_JEFEDETE') then
    alter table USUARIOS
       delete foreign key FK_USUARIOS_POSEE_JEFEDETE
end if;

drop index if exists CRIATURA.CRIATURA_PK;

drop table if exists CRIATURA;

drop index if exists DADOS.PERTENECEN_FK;

drop index if exists DADOS.DADOS_PK;

drop table if exists DADOS;

drop index if exists JEFEDETERRENO.JEFEDETERRENO_PK;

drop table if exists JEFEDETERRENO;

drop index if exists PUZZLEDEDADOS.COMPONE_FK;

drop index if exists PUZZLEDEDADOS.TIENEN_FK;

drop index if exists PUZZLEDEDADOS.PUZZLEDEDADOS_PK;

drop table if exists PUZZLEDEDADOS;

drop index if exists USUARIOS.POSEE_FK;

drop index if exists USUARIOS.USUARIOS_PK;

drop table if exists USUARIOS;

/*==============================================================*/
/* Table: CONFIGURACION                                         */
/*==============================================================*/
create table CONFIGURACION 
(
   VOL_MUSICA           integer                        not null default 100,
   VOL_EFECTO           integer                        not null default 100

);

/*==============================================================*/
/* Table: CRIATURA                                              */
/*==============================================================*/
create table CRIATURA 
(
   ID_CRIATURA          integer                        not null primary key GENERATED ALWAYS AS IDENTITY,
   NOMBRE		varchar(20)		       ,
   INFORMACION		varchar(300)		       ,
   IMAGENC              varchar(20)                    ,
   VIDA                 integer                        ,
   ATAQUE               integer                        ,
   DEFENSA              integer                        ,
   NIVEL                integer                        
   
);

/*==============================================================*/
/* Index: CRIATURA_PK                                           */
/*==============================================================*/
create unique index CRIATURA_PK on CRIATURA (
ID_CRIATURA ASC
);

/*==============================================================*/
/* Table: DADO                                                  */
/*==============================================================*/
create table DADO 
(
   ID_DADO              integer                        not null primary key GENERATED ALWAYS AS IDENTITY,
   ID_CRIATURA          integer                        not null,
   NIVEL                integer                        ,
   CARA1                integer                        ,
   CARA2                integer                        ,
   CARA3                integer                        ,
   CARA4                integer                        ,
   CARA5                integer                        ,
   CARA6                integer                        
   
);

/*==============================================================*/
/* Index: DADOS_PK                                              */
/*==============================================================*/
create unique index DADO_PK on DADO (
ID_DADO ASC
);

/*==============================================================*/
/* Index: PERTENECEN_FK                                         */
/*==============================================================*/
create index PERTENECEN_FK on DADO (
ID_CRIATURA ASC
);

/*==============================================================*/
/* Table: JEFEDETERRENO                                         */
/*==============================================================*/
create table JEFEDETERRENO 
(

   ID_JEFEDETERRENO     integer                        not null primary key GENERATED ALWAYS AS IDENTITY,
   NOMBRE		varchar(30)		       , 
   INFORMACION		varchar(300)		       ,
   IMAGENJ              varchar(20)                    ,
   VIDA                 integer                        ,
   INCVIDA              DEC(1,1)                         ,
   INCATAQUE            DEC(1,1)                         ,
   INCDEFENSA           DEC(1,1)                         
   
);

/*==============================================================*/
/* Index: JEFEDETERRENO_PK                                      */
/*==============================================================*/
create unique index JEFEDETERRENO_PK on JEFEDETERRENO (
ID_JEFEDETERRENO ASC
);

/*==============================================================*/
/* Table: JUGADOR                                               */
/*==============================================================*/
create table JUGADOR
(

   ID_JUGADOR           integer                       not null primary key GENERATED ALWAYS AS IDENTITY,
   NOMBREJUGADOR        varchar(15)                   ,
   PASSUSUARIO          varchar(15)                   ,
   ESHUMANO             boolean                       ,
   ID_JEFEDETERRENO     integer                       not null

);

/*==============================================================*/
/* Index: JUGADOR_PK                                            */
/*==============================================================*/
create unique index JUGADOR_PK on JUGADOR (
ID_JUGADOR ASC
);

/*==============================================================*/
/* Index: POSEE_FK                                              */
/*==============================================================*/
create index POSEE_FK on JUGADOR (
ID_JEFEDETERRENO ASC
);

/*==============================================================*/
/* Table: PUZZLEDEDADOS                                         */
/*==============================================================*/
create table PUZZLEDEDADOS 
(
  
   ID_REGISTRODADO      integer                        not null primary key GENERATED ALWAYS AS IDENTITY,
   ID_JUGADOR           integer                        not null,
   ID_DADO              integer                        not null,
   PARAJUGAR            boolean                        
   
);

/*==============================================================*/
/* Index: PUZZLEDEDADOS_PK                                      */
/*==============================================================*/
create unique index PUZZLEDEDADOS_PK on PUZZLEDEDADOS (
ID_REGISTRODADO ASC
);

/*==============================================================*/
/* Index: TIENEN_FK                                             */
/*==============================================================*/
create index TIENEN_FK on PUZZLEDEDADOS (
ID_JUGADOR ASC
);

/*==============================================================*/
/* Index: COMPONE_FK                                            */
/*==============================================================*/
create index COMPONE_FK on PUZZLEDEDADOS (
ID_DADO ASC
);

alter table DADO
   add foreign key (ID_CRIATURA)
      references CRIATURA (ID_CRIATURA);

alter table PUZZLEDEDADOS
   add foreign key (ID_DADO)
      references DADO (ID_DADO);

alter table PUZZLEDEDADOS
   add foreign key (ID_JUGADOR)
      references JUGADOR (ID_JUGADOR);

alter table JUGADOR
   add foreign key (ID_JEFEDETERRENO)
      references JEFEDETERRENO (ID_JEFEDETERRENO);

