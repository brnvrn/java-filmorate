   DROP TABLE IF EXISTS likes;
   DROP TABLE IF EXISTS user_friends;
   DROP TABLE IF EXISTS users;
   DROP TABLE IF EXISTS films_genres;
   DROP TABLE IF EXISTS genres;
   DROP TABLE IF EXISTS films;
   DROP TABLE IF EXISTS mpa_ratings;

   CREATE TABLE IF NOT EXISTS mpa_ratings(
   id integer GENERATED BY DEFAULT AS IDENTITY primary key,
   name varchar
   );

   CREATE TABLE IF NOT EXISTS films(
   id integer GENERATED BY DEFAULT AS IDENTITY primary key,
   name varchar(255) NOT NULL,
   description varchar(200),
   release_date date,
   duration integer,
   likes integer,
   mpa_id integer,
   foreign key (mpa_id) references mpa_ratings(id) on delete cascade
   );

   CREATE TABLE IF NOT EXISTS genres(
   id integer GENERATED BY DEFAULT AS IDENTITY primary key,
   name varchar
   );

   CREATE TABLE IF NOT EXISTS films_genres(
   id integer GENERATED BY DEFAULT AS IDENTITY primary key,
   film_id integer NOT NULL references films(id) on delete cascade,
   genre_id integer NOT NULL references genres(id) on delete cascade
   );

  CREATE TABLE IF NOT EXISTS users(
  id integer GENERATED BY DEFAULT AS IDENTITY primary key,
  name varchar,
  email varchar NOT NULL,
  login varchar NOT NULL,
  birthday date,
  friend_status boolean
  );

  CREATE TABLE IF NOT EXISTS user_friends(
  user_id integer NOT NULL references users(id) on delete cascade,
  friend_id integer NOT NULL references users(id) on delete cascade,
  primary key (user_id, friend_id)
  );

  CREATE TABLE IF NOT EXISTS likes(
  film_id integer NOT NULL references films(id) on delete cascade,
  user_id integer NOT NULL references users(id) on delete cascade,
  primary key (film_id, user_id)
  );

