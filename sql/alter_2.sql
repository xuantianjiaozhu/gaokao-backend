use gaokao;

ALTER TABLE school_score ADD COLUMN position INT;

UPDATE school_score
SET position = CASE
                   WHEN SUBSTRING_INDEX(min_score_position, '/', -1) REGEXP '^[0-9]+$' THEN
                       CAST(SUBSTRING_INDEX(min_score_position, '/', -1) AS SIGNED)
    END;

UPDATE school_score
SET min_score_position = CASE
                   WHEN SUBSTRING_INDEX(min_score_position, '/', 1) REGEXP '^[0-9]+$' THEN
                       CAST(SUBSTRING_INDEX(min_score_position, '/', 1) AS SIGNED)
    END;

# ALTER TABLE school_score MODIFY min_score_position INT;
ALTER TABLE school_score
    CHANGE COLUMN min_score_position score INT;

ALTER TABLE subject_score DROP COLUMN min_score_position;
