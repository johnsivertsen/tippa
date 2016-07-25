# Users schema

# --- !Ups

-- user
create table user (
  id integer primary key auto_increment,
  e_mail varchar not null,
  password varchar not null,
  first_name varchar not null,
  last_name varchar not null,
  created_date timestamp not null default now(),
  status varchar not null
);

insert into user (id, e_mail, password, first_name, last_name, status) VALUES (1, 'email@email.com', '$2a$04$JrYh6HuEKF9X4SGKedmFaODNyIJLVwbDrYPJ8QF0sKel.X4Tm27Pa', 'John', 'Doe', 'ACTIVE');

create table user_role (
  id integer primary key auto_increment,
  id_user integer not null,
  role varchar not null,
  created_date timestamp not null default now(),
  foreign key (id_user) references user(id)
);

insert into user_role(id_user, role) values(1, 'ADMIN');

-- tournament
create table tournament (
  id integer primary key auto_increment,
  short_name varchar not null,
  full_name varchar,
  type varchar not null default 'football',
  created_date timestamp not null default now()
);

insert into tournament(short_name, full_name, type) values('WC 2018', 'Football World Cup 2018', 'football');
insert into tournament(short_name, full_name, type) values('WC 2022', 'Football World Cup 2022', 'football');
insert into tournament(short_name, full_name, type) values('WC 2026', 'Football World Cup 2026', 'football');
insert into tournament(short_name, full_name, type) values('WC 2030', 'Football World Cup 2030', 'football');

-- round
create table round (
  id integer primary key auto_increment,
  id_tournament integer not null,
  number integer not null,
  designated_date timestamp not null,
  created_date timestamp not null default now(),
  foreign key (id_tournament) references tournament(id),
  unique (id_tournament, number)
);

-- user_tournament
create table user_tournament (
  id integer primary key auto_increment,
  id_user integer not null,
  id_tournament integer not null,
  foreign key (id_user) references user(id),
  foreign key (id_tournament) references tournament(id),
  unique (id_user, id_tournament)
);

-- team
create table team (
  id integer primary key auto_increment,
  short_name varchar not null,
  long_name varchar,
  description varchar,
  type varchar not null default 'football',
  country varchar not null default 'INTERNATIONAL',
  created_date timestamp not null default now()
);

-- tournament_team
create table tournament_team (
  id integer primary key auto_increment,
  id_tournament integer,
  id_team integer,
  created_date timestamp not null default now(),
  foreign key (id_tournament) references tournament(id),
  foreign key (id_team) references team(id),
  unique (id_tournament, id_team)
);

-- fixture
create table fixture (
  id integer primary key auto_increment,
  id_tournament integer not null,
  id_round integer not null,
  id_team_home integer not null,
  id_team_away integer not null,
  home_points integer,
  away_points integer,
  home_points_awarded integer,
  away_points_awarded integer,
  start_time timestamp,
  status varchar,
  created_date timestamp not null default now(),
  foreign key (id_tournament) references tournament(id),
  foreign key (id_team_home) references team(id),
  foreign key (id_team_away) references team(id),
  unique (id_tournament, id_round, id_team_home),
  unique (id_tournament, id_round, id_team_away),
  check (id_team_home <> id_team_away)
);

-- bet
create table bet (
  id integer primary key auto_increment,
  id_fixture integer not null,
  id_user integer not null,
  home_points integer,
  away_points integer,
  created_date timestamp not null default now(),
  foreign key (id_fixture) references fixture(id),
  foreign key (id_user) references user(id),
  unique (id_fixture, id_user)
);

-- friend_group
create table friend_group (
  id integer primary key auto_increment,
  admin_user integer not null,
  name varchar not null,
  description varchar,
  status varchar,
  created_date timestamp not null default now(),
  foreign key (admin_user) references user(id)
);

-- user_group
create table friend_group_user (
  id integer primary key auto_increment,
  id_user integer not null,
  id_friend_group integer not null,
  status varchar,
  created_date timestamp not null default now(),
  foreign key (id_user) references user(id),
  foreign key (id_friend_group) references friend_group(id),
  unique (id_user, id_friend_group)
);

-- message
create table message (
  id integer primary key auto_increment,
  recipient_id integer not null,
  type varchar,
  message varchar,
  status varchar,
  created_date timestamp not null default now()
);

-- log
create table log (
  id integer primary key auto_increment,
  key_1 integer,
  key_2 integer,
  type varchar,
  message varchar,
  created_date timestamp not null default now()
);

# --- !Downs

drop table log;
drop table message;
drop table user_group;
drop table friend_group;
drop table bet;
drop table fixture;
drop table tournament_team;
drop table team;
drop table user_tournament;
drop table round;
drop table tournament;
drop table user_role;
drop table user;
