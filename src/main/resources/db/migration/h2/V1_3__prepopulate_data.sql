create sequence hibernate_sequence start with 1 increment by 1;

INSERT INTO `Player` (id, uuid)
VALUES (1, '1461170a-ce2b-4894-9713-ee476a2c703a'),
       (2, '367acdd7-ec2c-4e27-9478-31c1fe5cde8a'),
       (3, '069a79f4-44e9-4726-a5be-fca90e38aaf5'),
       (4, 'e747b20f-0d9a-4a53-8712-821b236ec6ae'),
       (5, '9df174dc-b5fc-483e-9b70-e291e3e96427'),
       (6, 'f84c6a79-0a4e-45e0-879b-cd49ebd4c4e2'),
       (7, '5ab769ce-25f9-46de-9cff-7532129ad56b'),
       (8, '3e70cd57-a0f3-40df-a802-b0b63f7f0a7e'),
       (9, 'ee459773-beea-4890-bdfd-ec2ed6423d16'),
       (10, 'a76456c-02d2-64fd-98a6-3a4ccd70e2d17'),
       (11, 'dc78d78-830b-b427-5b5e-565d97590741f');

INSERT INTO `Team` (id, name, team_type, wins, losses, logo_url)
VALUES (1, 'Gryffindor', 'QUIDDITCH', 4, 0, 'https://p1.hiclipart.com/preview/542/343/666/harry-potter-gryffindor-logo-thumbnail.jpg'),
       (2, 'Slytherin', 'QUIDDITCH', 3, 1, 'https://toppng.com/uploads/preview/harry-potter-slytherin-logo-11549535063t3nuppcxfd.png'),
       (3, 'Hufflepuff', 'QUIDDITCH', 0, 9000, 'https://www.pngfind.com/pngs/m/115-1150321_what-hogwarts-house-am-i-in-harry-potter.png'),
       (4, 'Ravenclaw', 'QUIDDITCH', 5, 1, 'https://p7.hiclipart.com/preview/563/589/122/ravenclaw-house-fictional-universe-of-harry-potter-common-room-hogwarts-gryffindor-harry-potter-ravenclaw-thumbnail.jpg'),
       (5, 'Aurors', 'DUEL', 100, 6, 'https://www.pngfind.com/pngs/m/193-1939784_harrypotter-hp-ministryofmagic-auror-harry-potter-ministry-of.png'),
       (6, 'Death Eaters', 'DUEL', 6, 100, 'https://i.pinimg.com/originals/fb/2b/a3/fb2ba3cfe3208bc4f6a6ff4d7072174a.png'),
       (7, 'Wampus', 'QUIDDITCH', 7, 1, 'https://vignette.wikia.nocookie.net/harrypotter/images/3/3f/Horned_Serpent_ClearBG_2.png/revision/latest/scale-to-width-down/125?cb=20161204071312'),
       (8, 'Horned Serpent', 'QUIDDITCH', 10, 3, 'https://vignette.wikia.nocookie.net/harrypotter/images/e/e6/Wampus_ClearBG_2.png/revision/latest/scale-to-width-down/125?cb=20161204224541'),
       (9, 'Thunderbird', 'QUIDDITCH', 6, 2, 'https://vignette.wikia.nocookie.net/harrypotter/images/1/1a/Thundebird_ClearBG_2.png/revision/latest/scale-to-width-down/150?cb=20161204221417'),
       (10, 'Pukwudgie', 'QUIDDITCH', 0, 12, 'https://vignette.wikia.nocookie.net/harrypotter/images/a/a5/Pukwudgie_ClearBG_2.png/revision/latest/scale-to-width-down/125?cb=20161204075023');

/* INSERT LEAGUES */
INSERT INTO `League` (id, name, league_type)
VALUES (1, 'Hogwarts House League', 'QUIDDITCH'),
       (2, 'Ilvermorny House League', 'QUIDDITCH');

INSERT INTO `League_Teams` (league_id, teams_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 7),
       (2, 8),
       (2, 9),
       (2, 10);

/* INSERT TEAM PAIRINGS */
INSERT INTO `Player_Team` (player_id, team_id)
VALUES (1, 1),
       (3, 1),
       (5, 1),
       (2, 2),
       (4, 2),
       (6, 2),
       (7, 3),
       (8, 3),
       (9, 3),
       (1, 5),
       (2, 5),
       (3, 5),
       (4, 5),
       (5, 5),
       (6, 6),
       (7, 6),
       (8, 6),
       (9, 6),
       (10, 6),
       (11, 6);

/*INSERT DUEL RESULTS AND PROFILES*/
INSERT INTO `Duel_Player` (id, elo, losses, wins, player_id)
VALUES (1, 1200, 1, 0, 6),
       (2, 500, 2, 1, 5),
       (3, 2400, 0, 1, 4),
       (4, 1567, 1, 1, 3),
       (5, 2090, 4, 3, 2),
       (6, 1200, 2, 5, 1),
       (7, 1200, 0, 0, 7),
       (8, 1200, 0, 0, 8),
       (9, 1200, 0, 0, 9),
       (10, 1200, 0, 0, 10),
       (11, 1200, 0, 0, 11);

INSERT INTO `Duel_Match` (id, match_date, new_loser_elo, new_winner_elo, old_loser_elo, old_winner_elo, loser_id, winner_id)
VALUES (1, '2020-04-21 20:41:28.824', 1200, 1500, 1211, 1300, 2, 1),
       (2, '2020-04-22 20:41:28.824', 1500, 1400, 2000, 1300, 5, 2),
       (3, '2020-05-21 20:41:28.824', 2400, 1450, 3000, 1300, 3, 1),
       (4, '2020-03-21 20:41:28.824', 1111, 1400, 1112, 1300, 2, 3),
       (5, '2020-04-23 20:41:28.824', 2431, 1460, 2531, 1300, 6, 1),
       (6, '2020-05-25 20:41:28.824', 1232, 1400, 1111, 1300, 2, 4),
       (7, '2020-04-25 20:41:28.824', 900, 2400, 1111, 1300, 1, 2),
       (8, '2020-04-18 20:41:28.824', 3214, 1400, 4000, 1300, 5, 5),
       (9, '2020-04-05 20:41:28.824', 880, 1400, 1111, 1300, 1, 2),
       (10, '2020-04-05 20:41:28.824', 880, 1400, 1111, 1300, 5, 1),
       (11, '2020-04-20 20:41:28.824', 900, 1400, 1111, 1300, 2, 1);

/* QUIDDITCH INSERTS */
INSERT INTO `Quidditch_Team` (id, team_id, points_for, points_against)
VALUES (1, 1, 540, 270),
       (2, 2, 330, 560),
       (3, 3, 170, 220),
       (4, 4, 110, 50),
       (7, 10, 50, 230),
       (8, 8, 100, 10),
       (9, 9, 240, 50),
       (10, 7, 70, 1000);

INSERT INTO `Quidditch_Match` (id, match_date, winner_score, loser_score, winner_id, loser_id, snitch_catcher_id)
VALUES (1, '2020-04-21 20:41:28.824', 150, 70, 1, 2, 1),
       (2, '2020-04-25 20:41:28.824', 110, 50, 4, 3, 7),
       (3, '2020-04-23 20:41:28.824', 180, 130, 2, 4, 2),
       (4, '2020-05-21 20:41:28.824', 120, 110, 3, 1, 1),
       (5, '2020-04-21 20:41:28.824', 200, 10, 1, 2, 1);

INSERT INTO `Quidditch_Match` (id, match_date, winner_score, loser_score, winner_id, loser_id)
VALUES (6, '2020-03-21 20:41:28.824', 80, 70, 1, 2),
       (7, '2020-05-21 20:41:28.824', 180, 180, 3, 4);

/* Events */
INSERT INTO `Team_Match_Event` (id, event_type, match_date, team1_id, team2_id)
VALUES (1, 'QUIDDITCH', '2020-08-21 20:41:28.824', 1, 2),
       (2, 'QUIDDITCH', '2020-05-27 20:41:28.824', 4, 3),
       (3, 'QUIDDITCH', '2020-05-27 20:41:28.824', 1, 3),
       (4, 'QUIDDITCH', '2020-06-01 20:41:28.824', 4, 1),
       (5, 'QUIDDITCH', '2020-06-02 20:41:28.824', 4, 2),
       (6, 'QUIDDITCH', '2020-06-03 20:41:28.824', 2, 3),
       (7, 'QUIDDITCH', '2020-06-03 20:41:28.824', 4, 3),
       (8, 'QUIDDITCH', '2020-06-04 20:41:28.824', 1, 2);

INSERT INTO `Player_Match_Event` (id, event_type, match_date, player1_id, player2_id)
VALUES (1, 'DUEL_SINGLE', '2020-06-21 20:41:28.824', 1, 2),
       (2, 'DUEL_SINGLE', '2020-05-27 20:41:28.824', 5, 10),
       (3, 'DUEL_SINGLE', '2020-05-27 20:41:28.824', 4, 3),
       (4, 'DUEL_SINGLE', '2020-05-27 20:41:28.824', 1, 3),
       (5, 'DUEL_SINGLE', '2020-05-27 20:41:28.824', 4, 2),
       (6, 'DUEL_SINGLE', '2020-05-27 20:41:28.824', 3, 2),
       (7, 'DUEL_SINGLE', '2020-05-27 20:41:28.824', 4, 3);

INSERT INTO `Event_Group` (id, name, start_date, end_date)
VALUES (1, 'Quidditch House Cup', '2020-06-01 00:00:00.00', '2020-07-01 00:00:00.00');

INSERT INTO `Event_Group_Matches` (event_group_id, matches_id)
VALUES (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8);

/* Quidditch Stats per player */
INSERT INTO `Quidditch_Player` (id, player_id, seeker_games, chaser_games, beater_games, keeper_games, goals_scored, goals_conceded)
VALUES (1, 2, 1, 1, 2, 0, 100, 5),
       (2, 3, 1, 1, 2, 0, 6, 10),
       (3, 5, 5, 1, 0, 0, 0, 5);

INSERT INTO `Snitch_Catches` (id, catcher, opponent, time_length)
VALUES (1, 2, 3, 1000000),
       (2, 5, 2, 100);