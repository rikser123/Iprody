CREATE TABLE groups(
    id UUID PRIMARY KEY,
    group_ref_id UUID NOT NULL,
    current_count INTEGER,
    "limit" INTEGER
);
