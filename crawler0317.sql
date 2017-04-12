/* TABLE CATEGORY (category_id is used when the category is a subcategory) */
CREATE TABLE GPages_Category(
	id int NOT NULL COMMENT 'Identifier of Category.',
	name varchar(150) NOT NULL COMMENT 'Name of the Category.',
	parent_id int COMMENT 'Identifier of Subcategorys parent.',
	PRIMARY KEY(id),
	CONSTRAINT FK_GPagesSubcategory FOREIGN KEY(parent_id) REFERENCES GPages_Category(id)
) COMMENT = 'Stores the businesses categories from the Golden Pages website.';

/* TABLE BUSINESS */
CREATE TABLE GPages_Business(
	id int NOT NULL COMMENT 'Identifier of Business.',
	category_id int NOT NULL COMMENT 'Identifier of Category of the business.',
	company varchar(50) NOT NULL COMMENT 'Name of the company.',
	address varchar(350) COMMENT 'Address at which the company is placed.',
	phone varchar(20) COMMENT 'Line phone number of the company.',
	mobile varchar(20) COMMENT 'Mobile phone number of the company.',
	email varchar(50) COMMENT 'Email of the company.',
	PRIMARY KEY(id),
	CONSTRAINT FK_GPagesCategoryBusiness FOREIGN KEY(category_id) REFERENCES GPages_Category(id)
) COMMENT = 'Stores the businesses found on the Golden Pages website.';
