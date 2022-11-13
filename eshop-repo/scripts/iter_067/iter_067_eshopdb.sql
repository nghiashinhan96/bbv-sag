-- Improve performance for Organisation services by
-- Caching Organisation services APIs
-- Adding more indexes for frequently accessed columns
-- TABLE: ORGANISATION 
-- COLUMNs:  
--++	NAME
--++	ORG_CODE
--++	PARENT_ID
--++	SHORTNAME

CREATE INDEX IDX_NAME ON [dbo].[ORGANISATION] (NAME);
CREATE INDEX IDX_ORG_CODE ON [dbo].[ORGANISATION] (ORG_CODE);
CREATE INDEX IDX_PARENT_ID ON [dbo].[ORGANISATION] (PARENT_ID);
CREATE INDEX IDX_SHORTNAME ON [dbo].[ORGANISATION] (SHORTNAME);
