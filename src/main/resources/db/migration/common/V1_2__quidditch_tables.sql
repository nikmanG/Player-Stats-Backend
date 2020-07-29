create table quidditch_player (
                             id bigint not null,
                             seeker_games integer default 0 not null,
                             chaser_games integer default 0 not null,
                             beater_games integer default 0 not null,
                             keeper_games integer default 0 not null,
                             goals_scored integer default 0 not null,
                             goals_conceded integer default 0 not null,
                             player_id bigint not null,
                             primary key (id)
);

create table snitch_catches (
                                  id bigint not null,
                                  time_length bigint default 0 not null,
                                  catcher bigint not null,
                                  opponent bigint not null,
                                  primary key (id)
);

alter table quidditch_player
    add constraint FK_player_id
        foreign key (player_id)
            references player;

alter table snitch_catches
    add constraint FK_catcher_player_id
        foreign key (catcher)
            references player;

alter table snitch_catches
    add constraint FK_opponent_player__id
        foreign key (opponent)
            references player;