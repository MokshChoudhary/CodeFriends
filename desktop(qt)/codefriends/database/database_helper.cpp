#include "database_helper.h"

int db_provider::count_data_callback(void *data, int argc, char **argv, char **azColName){

    return argc;

}

int db_provider::count_data(sqlite3 *db, std::string query){

    int rc = 0;
    const char* data = "dummy";
    char* error = nullptr;
    rc = sqlite3_exec(db, query.c_str(), count_data_callback, (void*)data, &error);

    return 0;
}
