CREATE TABLE questions
(
    id             int PRIMARY KEY IDENTITY (1, 1),
    name           varchar(128)   NOT NULL,
    version        varchar(16)     NOT NULL,
    title_de       nvarchar(1024) NOT NULL,
    title_fr       nvarchar(1024) NOT NULL,
    title_it       nvarchar(1024) NOT NULL,
    title_en       nvarchar(1024) NOT NULL,
    description_de ntext          NOT NULL,
    description_fr ntext          NOT NULL,
    description_it ntext          NOT NULL,
    description_en ntext          NOT NULL
);

CREATE TABLE answers
(
    id      int PRIMARY KEY IDENTITY (1, 1),
    name    varchar(128)  NOT NULL,
    version varchar(16)    NOT NULL,
    text_de nvarchar(128) NOT NULL,
    text_fr nvarchar(128) NOT NULL,
    text_it nvarchar(128) NOT NULL,
    text_en nvarchar(128) NOT NULL
);

CREATE TABLE recommendations
(
    id             int PRIMARY KEY IDENTITY (1, 1),
    name           varchar(128)   NOT NULL,
    version        varchar(16)     NOT NULL,
    severity       varchar(8)     NOT NULL,
    title_de       nvarchar(1024) NOT NULL,
    title_fr       nvarchar(1024) NOT NULL,
    title_it       nvarchar(1024) NOT NULL,
    title_en       nvarchar(1024) NOT NULL,
    description_de ntext          NOT NULL,
    description_fr ntext          NOT NULL,
    description_it ntext          NOT NULL,
    description_en ntext          NOT NULL
);

CREATE TABLE screenings
(
    id                bigint PRIMARY KEY IDENTITY (1, 1),
    version           varchar(16) NOT NULL,
    canton            varchar(2) NOT NULL,
    language          varchar(2) NOT NULL,
    age               int NOT NULL,
    gender            varchar(8) NOT NULL,
    contact_date      date NOT NULL,
    recommendation_id int        NOT NULL,
    recommendation_code varchar(4) NULL,
    submitted_at      datetime   NOT NULL,
    CONSTRAINT fk_screening_recommendation_id
        FOREIGN KEY (recommendation_id)
            REFERENCES recommendations (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

CREATE TABLE exchanges
(
    id           bigint PRIMARY KEY IDENTITY (1, 1),
    ordering     int    NOT NULL,
    question_id  int    NOT NULL,
    answer_id    int    NOT NULL,
    screening_id bigint NOT NULL,
    CONSTRAINT fk_exchange_question_id
        FOREIGN KEY (question_id)
            REFERENCES questions (id)
            ON UPDATE NO ACTION
            ON DELETE NO ACTION,
    CONSTRAINT fk_exchange_answer_id
        FOREIGN KEY (answer_id)
            REFERENCES answers (id)
            ON UPDATE NO ACTION
            ON DELETE NO ACTION,
    CONSTRAINT fk_exchange_screening_id
        FOREIGN KEY (screening_id)
            REFERENCES screenings (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);
