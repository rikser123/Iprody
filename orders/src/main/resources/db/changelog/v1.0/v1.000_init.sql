CREATE TABLE inquiry (
     id UUID PRIMARY KEY,
     product_ref_id UUID NOT NULL,
     customer_ref_id UUID NOT NULL,
     group_ref_id UUID,
     manager_ref_id UUID,
     source VARCHAR(100) NOT NULL,
     comment VARCHAR,
     status VARCHAR(50) NOT NULL,
     note VARCHAR,
     created_at TIMESTAMP WITH TIME ZONE NOT NULL,
     updated_at TIMESTAMP WITH TIME ZONE
);