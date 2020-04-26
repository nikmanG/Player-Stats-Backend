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
       (6, 'Death Eaters', 'DUEL', 6, 100, 'https://i.pinimg.com/originals/fb/2b/a3/fb2ba3cfe3208bc4f6a6ff4d7072174a.png');

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
       (4, 4, 110, 50);

INSERT INTO `Quidditch_Match` (id, match_date, winner_score, loser_score, winner_id, loser_id, snitch_catcher_id)
VALUES (1, '2020-04-21 20:41:28.824', 150, 70, 1, 2, 1),
       (2, '2020-04-25 20:41:28.824', 110, 50, 4, 3, 7),
       (3, '2020-04-23 20:41:28.824', 180, 130, 2, 4, 2),
       (4, '2020-05-21 20:41:28.824', 120, 110, 3, 1, 1),
       (5, '2020-04-21 20:41:28.824', 200, 10, 1, 2, 1);

INSERT INTO `Quidditch_Match` (id, match_date, winner_score, loser_score, winner_id, loser_id)
VALUES (6, '2020-03-21 20:41:28.824', 80, 70, 1, 2);

ALTER SEQUENCE HIBERNATE_SEQUENCE RESTART WITH 4000;