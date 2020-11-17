#include "database_helper.h"

std::string const_name::dir = nullptr;

std::string const_name::active = "login";

std::string const_name::login_table_name = "login_db";

std::string const_name::primary_key = "Email";

std::string const_name::database_name = "bombay.db";

std::string const_name::sql_login = "CREATE TABLE "+login_table_name+"("
    +primary_key+" NOT NULL, "
    +active+"+ Bool );";

int db_provider::count_data_callback(void *data, int argc, char **argv, char **azColName){

    data = static_cast<void*>(&argc);
    return 0;

}

int db_provider::count_data(sqlite3 *db, std::string tname){

    int rc = 0;
    void* data = nullptr;
    char* error = nullptr;
    std::string sql = "select * from "+tname+" ;";
    rc = sqlite3_exec(db, tname.c_str(), count_data_callback, data, &error);
    int* mcount = static_cast<int*>(data);
    if(rc == SQLITE_OK){
        qInfo("Table found returning the data back to function call");
        return *mcount;
    }else{
        qWarning("not able to find the table");
        return SQLITE_ERROR;
    }
}

//int db_provider::login_table_callback(void *data, int argc, char **argv, char **az_col_name){

//    data = static_cast<void*>(&argc);
//    return 0;

//}

int db_provider::create_table(sqlite3 *db,std::string sql){
    int rc;
    static void* data = nullptr;
    rc = sqlite3_exec(db, sql.c_str(), count_data_callback, data, nullptr);
    if(rc == SQLITE_OK){
        qInfo("Table created successful!");
    }else{
        qWarning("not able to create table");
    }
    return rc;
}

int db_provider::check_table(sqlite3 *db,std::string table_name){
    int rc = 0;
    std::string sql = "select * from "+table_name+";";
    rc = sqlite3_exec(db, sql.c_str(), nullptr, nullptr, nullptr);
    if(rc == SQLITE_OK){
        qInfo("Table found!");
    }else{
        qWarning(std::string("Table ("+table_name+") not found").c_str());
    }
    return rc;
}



databasehelper::databasehelper()
{

}

