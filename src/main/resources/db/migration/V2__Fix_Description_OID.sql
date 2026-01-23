ALTER TABLE job_applications ADD COLUMN description_fixed TEXT;
UPDATE job_applications
SET description_fixed = convert_from(lo_get(description), 'UTF8')
WHERE description IS NOT NULL;
ALTER TABLE job_applications DROP COLUMN description;
ALTER TABLE job_applications RENAME COLUMN description_fixed TO description;