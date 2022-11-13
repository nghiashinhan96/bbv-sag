-- 3651
ALTER TABLE [dbo].[COUPON_CONDITIONS] ADD [SINGLE_CUSTOMER] [bit] NOT NULL DEFAULT (0)

--- CREATE WORKING_DAY TABLE
CREATE TABLE [dbo].[WORKING_DAY](
    [ID] [int] IDENTITY(1,1) NOT NULL,
    [CODE] [varchar](255) NOT NULL,
    [DESCRIPTION] [varchar](255) NULL
 CONSTRAINT [PK_WORKING_DAY] PRIMARY KEY CLUSTERED
(
    [ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

--- CREATE OPENING_DAYS_CALENDAR TABLE
CREATE TABLE [dbo].[OPENING_DAYS_CALENDAR](
    [ID] [int] IDENTITY(1,1) NOT NULL,
    [DATETIME] [date] NOT NULL,
    [COUNTRY_ID] [int] NOT NULL,
    [WORKING_DAY_ID] [int] NOT NULL,
    [EXCEPTIONS] [varchar](max) NULL
 CONSTRAINT [PK_OPENING_DAYS_CALENDAR] PRIMARY KEY CLUSTERED
(
    [ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]