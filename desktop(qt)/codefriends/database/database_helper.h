#include <iostream>
#include "sqlite/sqlite3.h"

/**
 * @note this is use to provice constent data name for database
 *       all the database data name should allocated here and
 *       the variable name are use all in the project
 */
 const std::string database_name = "bombay.db";

 const std::string login_table_name= "login detail";

 const std::string primary_key = "Email";

 const std::string active = "login";

 const std::string sql_login = "CREATE TABLE "+login_table_name+"("
                        +primary_key+"     NOT NULL, "
                        +active+"+ Bool );";
/*
const std::string fname = "First Name";
const std::string lname = "Last Name";
const std::string age = "Age";
const std::string number = "Phone Number";*/

 class db_provider{
 public:
     static int count_data(sqlite3* db,std::string query);
     static int count_data_callback(void *data, int argc, char **argv, char **azColName);
     //static int count_data(void *data, int argc, char **argv, char **azColName);
 };
