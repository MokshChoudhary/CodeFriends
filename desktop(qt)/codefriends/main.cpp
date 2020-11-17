#include "splash_window.h"
#include "iostream"
#include <QApplication>
#include <QDebug>
#include "database/database_helper.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    //const_name::dir = a.applicationDirPath().toUtf8().constData();
    qDebug()<<"Application directry : "<<a.applicationDirPath();
    splash_window w;
    w.show();

    return a.exec();
}
