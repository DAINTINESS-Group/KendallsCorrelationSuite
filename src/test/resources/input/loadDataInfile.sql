LOAD DATA INFILE 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/kendall/all_stocks_5yr_clean.csv' 
INTO TABLE all_stocks_5yr 
FIELDS TERMINATED BY ',' 
IGNORE 1 ROWS;
