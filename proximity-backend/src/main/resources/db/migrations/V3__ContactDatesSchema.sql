ALTER TABLE screenings
DROP COLUMN contact_date;

CREATE TABLE contacts
(
    id             bigint PRIMARY KEY IDENTITY (1, 1),
    contact_date   date NOT NULL,
    screening_id bigint NOT NULL,
    CONSTRAINT fk_contacts_screening_id
        FOREIGN KEY (screening_id)
            REFERENCES screenings (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

CREATE INDEX index_contacts_date
ON contacts (contact_date);