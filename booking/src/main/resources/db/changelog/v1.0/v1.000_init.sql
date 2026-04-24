CREATE TABLE groups(
    id UUID PRIMARY KEY,
    current_count INTEGER,
    max_limit INTEGER
);

CREATE TABLE group_inquiry (
   id UUID PRIMARY KEY,
   group_id UUID NOT NULL REFERENCES groups(id) ON DELETE CASCADE,
   inquiry_id UUID NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
