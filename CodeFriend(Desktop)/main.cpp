#include "splash_screen.h"
#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    split_screen w;
    w.show();
    //Check for auth of a user
    if(w.checkAuth()){
       w.close();
    }
    return a.exec();
}
