use gaokao;
-- 修改 school_score 表
ALTER TABLE school_score
    RENAME COLUMN admission_batch TO pici;

-- 修改 subject_score 表
ALTER TABLE subject_score
DROP COLUMN pici,
    RENAME COLUMN admission_batch TO pici;
