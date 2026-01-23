CREATE TABLE job_applications (
    id BIGINT PRIMARY KEY,
    company_name VARCHAR(255),
    position VARCHAR(255),
    description OID,
    status SMALLINT,
    date_applied DATE
);