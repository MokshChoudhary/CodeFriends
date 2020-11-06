#include "splash_window.h"
#include "ui_splash_window.h"
#include <sqlite/sqlite3.h>
#include "database/database_helper.h"

splash_window::splash_window(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::splash_window)
{
    ui->setupUi(this);
    setWindowFlags(Qt::Window | Qt::FramelessWindowHint);
    open_database();

}

void splash_window::open_database(){
    int responce;
    responce = sqlite3_open(database_name.c_str() ,&db);
    if(responce != SQLITE_OK){
        ui->login_dilog->setText("not able to create database!!");
    }else {
        ui->login_dilog->setText("Create the database and gain the access");
    }

    std::string sql = "select * from "+login_table_name+";";
    responce = 0;
    char* e_msg = nullptr;
    responce = sqlite3_exec(db,sql.c_str(),nullptr,nullptr,&e_msg);
    if(responce != SQLITE_OK){
        ui->login_dilog->setText("No table found in the database");
        responce = sqlite3_exec(db,sql_login.c_str(),nullptr,nullptr,&e_msg);
        if(responce == SQLITE_OK){
            ui->login_dilog->setText("login table created");
        }
    }else {
        ui->login_dilog->setText("login table found in database");
        responce  ;
    }

}

splash_window::~splash_window()
{
    sqlite3_close(db);
    delete ui;
}
