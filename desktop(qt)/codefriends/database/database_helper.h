#ifndef DATABASEHELPER_H
#define DATABASEHELPER_H

#include <iostream>
#include "sqlite/sqlite3.h"
#include <QDebug>


class databasehelper
{
public:
    databasehelper();
};


/**
 * @note this is use to provice constent data name for database
 *       all the database data name should allocated here and
 *       the variable name are use all in the project
 */
class const_name{
public:
    static std::string dir ;

    static std::string active;

    static std::string login_table_name;

    static std::string primary_key;

    static std::string database_name;

    static std::string sql_login;

/*
const std::string fname = "First Name";
const std::string lname = "Last Name";
const std::string age = "Age";
const std::string number = "Phone Number";*/

};

class db_provider{
public:
     static int count_data(sqlite3* db,std::string tname);

     static int create_table(sqlite3* db,std::string query);

     static int check_table(sqlite3* db,std::string table_name);

     //static int login_table_callback(void* data,int argc,char** argv,char** az_col_name);
     static int count_data_callback(void* data, int argc, char** argv, char** azColName);
 };

#endif // DATABASEHELPER_H
