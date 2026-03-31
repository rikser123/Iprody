CREATE TABLE payments(
   id UUID PRIMARY KEY,
   inquiry_ref_id UUID NOT NULL,
   amount NUMERIC(5,2) NOT NULL,
   currency VARCHAR(3) NOT NULL,
   transaction_ref_id UUID,
   status VARCHAR(20) NOT NULL,
   note VARCHAR,
   created_at TIMESTAMP WITH TIME ZONE NOT NULL,
   updated_at TIMESTAMP WITH TIME ZONE
);