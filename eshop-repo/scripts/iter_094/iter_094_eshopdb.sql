--- Create MESSAGE_LOCATION_RELATION table
CREATE TABLE [dbo].[MESSAGE_LOCATION_RELATION](
    [ID] [bigint] IDENTITY(1,1) NOT NULL,
    [MESSAGE_ID] [bigint] NOT NULL,
    [MESSAGE_LOCATION_ID] [int] NOT NULL,
    CONSTRAINT [UK_MESSAGE_MESSAGELOCATION] UNIQUE ([MESSAGE_ID], [MESSAGE_LOCATION_ID]),
 CONSTRAINT [PK_MESSAGE_LOCATION_RELATION] PRIMARY KEY CLUSTERED
(
    [ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY];

--- Add foreign key for MESSAGE_LOCATION_RELATION table
ALTER TABLE [dbo].[MESSAGE_LOCATION_RELATION] ADD CONSTRAINT FK_MESSAGE_ID FOREIGN KEY (MESSAGE_ID) REFERENCES [dbo].[MESSAGE] (ID);
ALTER TABLE [dbo].[MESSAGE_LOCATION_RELATION] ADD CONSTRAINT FK_MESSAGE_LOCATION_ID FOREIGN KEY (MESSAGE_LOCATION_ID) REFERENCES [dbo].[MESSAGE_LOCATION] (ID);

--- Modify V_MESSAGE (run this after create MESSAGE_LOCATION_RELATION)
ALTER VIEW [V_MESSAGE] AS
SELECT
    TEMP.*,
 	STUFF(
        (SELECT CAST(', ' AS varchar(max)) + L.VALUE
            FROM MESSAGE_LOCATION  as L
            WHERE L.ID in (
                select LR.MESSAGE_LOCATION_ID FROM MESSAGE_LOCATION_RELATION LR
                WHERE LR.MESSAGE_ID = TEMP.ID)
            FOR XML PATH(''), TYPE
        )
        .value('.', 'varchar(max)'),1, 1,'') AS LOCATION_VALUE
FROM
 	(SELECT DISTINCT
	    M.ID AS ID,
	    M.TITLE AS TITLE,
	    T.TYPE AS TYPE,
	    A.AREA AS AREA,
	    SA.SUB_AREA AS SUB_AREA,
	    M.ACTIVE AS ACTIVE,
	    CREATED_DATE AS CREATED_DATE
	    FROM (
	        MESSAGE M
	        JOIN MESSAGE_TYPE T ON M.MESSAGE_TYPE_ID = T.ID
	    	JOIN MESSAGE_SUB_AREA SA ON M.MESSAGE_SUB_AREA_ID=SA.ID
	        JOIN MESSAGE_AREA A ON SA.MESSAGE_AREA_ID = A.ID
            )
    ) AS TEMP;

--- Drop column on table MESSAGE !!!
ALTER TABLE MESSAGE DROP COLUMN MESSAGE_LOCATION_ID
