-- Update all existing sessions from PENDING to SCHEDULED
-- This allows students to register for sessions that were created before the status logic change

-- Update sessions that:
-- 1. Currently have status = PENDING (id = 1)
-- 2. Have startTime in the future (not yet started)
UPDATE session 
SET session_status_id = 2  -- SCHEDULED
WHERE session_status_id = 1  -- PENDING
  AND start_time > NOW()     -- Only future sessions
  AND TIMESTAMPDIFF(HOUR, NOW(), start_time) >= 1;  -- At least 1 hour in future

-- Verify the update
SELECT 
    s.id,
    s.subject_id,
    sub.name as subject_name,
    s.start_time,
    s.session_status_id,
    ss.name as status_name,
    s.max_quantity,
    s.current_quantity
FROM session s
LEFT JOIN subject sub ON s.subject_id = sub.id
LEFT JOIN session_status ss ON s.session_status_id = ss.id
WHERE s.start_time > NOW()
ORDER BY s.start_time ASC;
