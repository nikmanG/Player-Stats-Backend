
create table duel_match (
                            id bigint not null,
                            match_date timestamp not null,
                            new_loser_elo bigint not null,
                            new_winner_elo bigint not null,
                            old_loser_elo bigint not null,
                            old_winner_elo bigint not null,
                            loser_id bigint not null,
                            winner_id bigint not null,
                            primary key (id)
);

create table duel_player (
                             id bigint not null,
                             elo bigint not null,
                             losses integer default 0 not null,
                             wins integer default 0 not null,
                             player_id bigint not null,
                             primary key (id)
);

create table player (
                        id bigint not null,
                        uuid varchar(36) not null,
                        primary key (id)
);

create table player_match_event (
                                    id bigint not null,
                                    event_type integer not null,
                                    match_date timestamp not null,
                                    player1_id bigint not null,
                                    player2_id bigint not null,
                                    primary key (id)
);

create table player_team (
                             team_id bigint not null,
                             player_id bigint not null,
                             primary key (player_id, team_id)
);

create table quidditch_match (
                                 id bigint not null,
                                 loser_score bigint not null,
                                 match_date timestamp not null,
                                 winner_score bigint not null,
                                 loser_id bigint not null,
                                 snitch_catcher_id bigint,
                                 winner_id bigint not null,
                                 primary key (id)
);

create table quidditch_team (
                                id bigint not null,
                                points_against bigint default 0 not null,
                                points_for bigint default 0 not null,
                                team_id bigint not null,
                                primary key (id)
);

create table team (
                      id bigint not null,
                      draws integer default 0 not null,
                      logo_url varchar(255),
                      losses integer default 0 not null,
                      name varchar(20) not null,
                      team_type varchar(255) not null,
                      wins integer default 0 not null,
                      primary key (id)
);

create table team_match_event (
                                  id bigint not null,
                                  event_type integer not null,
                                  match_date timestamp not null,
                                  team1_id bigint not null,
                                  team2_id bigint not null,
                                  primary key (id)
);

alter table player
    drop constraint if exists UK_57ulfn2rdt6btajrr5p2gee5c;

alter table player
    add constraint UK_57ulfn2rdt6btajrr5p2gee5c unique (uuid);

alter table team
    drop constraint if exists UKg2itlggnudko1wdr49eubx804;

alter table team
    add constraint UKg2itlggnudko1wdr49eubx804 unique (name, team_type);

alter table duel_match
    add constraint FKophu8ravp9x81hb9katyc6noh
        foreign key (loser_id)
            references player;

alter table duel_match
    add constraint FKooe53880odo86fsc8jv11tr96
        foreign key (winner_id)
            references player;

alter table duel_player
    add constraint FKr9u59jtbatr4245webwgwvtm7
        foreign key (player_id)
            references player;

alter table player_match_event
    add constraint FKf6owq8rcrqd4yirmm9ltm744f
        foreign key (player1_id)
            references player;

alter table player_match_event
    add constraint FKga8n2ixikyrlhucwyaay40rgg
        foreign key (player2_id)
            references player;

alter table player_team
    add constraint FKi5rpfhkcbhcotndp7k623f4y0
        foreign key (player_id)
            references player;

alter table player_team
    add constraint FK9axcbkwl4aiy4b9stqe31q2k
        foreign key (team_id)
            references team;

alter table quidditch_match
    add constraint FKca9mdhenh8waok6fripje8kgq
        foreign key (loser_id)
            references team;

alter table quidditch_match
    add constraint FKpxv2a6h8rrh9twadcpcbi4y51
        foreign key (snitch_catcher_id)
            references player;

alter table quidditch_match
    add constraint FKfjldq0uxle4oyktx9mc8xiwxc
        foreign key (winner_id)
            references team;

alter table quidditch_team
    add constraint FKt7d6qgg7i5rfuebdo68dc2cp0
        foreign key (team_id)
            references team;

alter table team_match_event
    add constraint FK4eb04ey2gwwq4o1hafxnq96cr
        foreign key (team1_id)
            references team;

alter table team_match_event
    add constraint FK61e4e4dpi2nn0fsor93ff4a6n
        foreign key (team2_id)
            references team;