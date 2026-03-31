CREATE TABLE contact_details(
    id UUID PRIMARY KEY,
    email VARCHAR(50) NOT NULL UNIQUE,
    phone_number VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE customer(
   id UUID PRIMARY KEY,
   full_name VARCHAR(30) NOT NULL,
   contact_details_id UUID,
   created_at TIMESTAMP WITH TIME ZONE NOT NULL,
   updated_at TIMESTAMP WITH TIME ZONE,

   CONSTRAINT customer_contact_details FOREIGN KEY (contact_details_id) REFERENCES contact_details(id)
);
