# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table poll (
  uuid                          varchar(40) not null,
  question                      varchar(255),
  answera                       varchar(255),
  answerb                       varchar(255),
  votesa                        integer not null,
  votesb                        integer not null,
  constraint pk_poll primary key (uuid)
);


# --- !Downs

drop table if exists poll;

