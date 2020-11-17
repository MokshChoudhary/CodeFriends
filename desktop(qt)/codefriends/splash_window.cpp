#include "splash_window.h"
#include "ui_splash_window.h"
#include <sqlite/sqlite3.h>
#include <main_window.h>

splash_window::splash_window(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::splash_window)
{
    ui->setupUi(this);
    setWindowFlags(Qt::Window | Qt::FramelessWindowHint);
    if(create_database()==SQLITE_OK){
        auth_user();
    }else{
        qWarning("not able to access the database");
        this->~splash_window();
    }
}

int splash_window::create_database(){
    //creating database object
    int responce = sqlite3_open((/*const_name::dir+*/const_name::database_name).c_str() ,&db);
    if(responce != SQLITE_OK){
        ui->login_dilog->setText("not able to open table!!");
        qWarning("not able to open table!!");
        return SQLITE_ERROR;
    }else {
        ui->login_dilog->setText("Create the database and gain the access");
        qWarning("Create the database and gain the access");
        return SQLITE_OK;
    }
}

void splash_window::auth_user(){
    int responce;
    int count = 0;
    //checking for login_table existence
    responce = helper.check_table(db,const_name::login_table_name);
    //checking the response of sql if the is not SQLITE_OK create a new table and request for login
    if(responce == SQLITE_ERROR){
        ui->login_dilog->setText("No table found in the database");
        qInfo(std::string(SQLITE_ERROR+"").c_str());
        qInfo("No table found in database");
        qInfo("Creating new table");
        responce = helper.create_table(db,const_name::login_table_name);
        if(responce == SQLITE_OK){
            ui->login_dilog->setText("login table created");
            qInfo("login table created");
            /**
             * @todo make a login prompte
             */
        }else{
            qWarning("not able to create table. Closing software" );
            this->~splash_window();
            return;
        }
    }else {
        ui->login_dilog->setText("login table found in database");
        qInfo("login table found in database");
        count = db_provider::count_data(db,const_name::login_table_name) ;
    }



    if(count ==-1){
        ui->login_dilog->setText("no table found");
        ui->login_dilog->setText("counter some error");
        ui->login_dilog->setText("creating new table");
        //sqlite3_exec(db,sql_login.c_str(),nullptr,0,&e_msg);
        qWarning("no table found counter an error creating new table");

    }else if (count == 0) {
        ui->login_dilog->setText("no user found !");
        qInfo("no use found");
        /**
         * @todo show login prompt by user can login
         */
    }else {
        ui->login_dilog->setText("User found prepareing to start");
        qInfo("successfull login!");
        /**
          *@todo call main windiow
          */
        main_window *window = new main_window();
        window->show();
        this->hide();
        this->~splash_window();
    }


}

splash_window::~splash_window()
{
    sqlite3_close(db);
    delete ui;
}
