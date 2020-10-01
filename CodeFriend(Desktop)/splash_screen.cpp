#include "splash_screen.h"
#include "ui_split_screen.h"
#include "mainwindow.h"

split_screen::split_screen(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::split_screen)
{
    ui->setupUi(this);
    QPixmap bkgnd(":/image/resouce/img/splash_image.png");
    bkgnd = bkgnd.scaled(this->size(), Qt::IgnoreAspectRatio);
    QPalette palette;
    palette.setBrush(QPalette::Background, bkgnd);
    this->setPalette(palette);
    setWindowFlags(Qt::FramelessWindowHint);
}

bool split_screen::checkAuth()
{
    if(true)
    {
        MainWindow *window = new MainWindow();
        window->show();
        return true;
    }
    //return false;
}

split_screen::~split_screen()
{
    delete ui;
}
