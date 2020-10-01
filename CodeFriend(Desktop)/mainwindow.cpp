#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "msg_box.h"
#include "container_view.h"
#include <string>
#include <QVBoxLayout>

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    /**
    *@brief setting the value of navigation frame to zero
    */

    ui->navigation->resize(0,ui->navigation->height());

    //back arrow  \uFFE9
//    QVBoxLayout *mLayout = new QVBoxLayout();
//    auto msg = new msg_box();

//    ui->msg_area->setLayout(mLayout);
//    msg->set_msg("1 msg");
//    mLayout->addWidget(msg);

//    for (int i = 0 ; i<30;i++){
//        auto msg2 = new msg_box();
//        msg2->set_msg("massage !");
//        mLayout->addWidget(msg2);
//    }
}

MainWindow::~MainWindow()
{
    delete ui;
}



void MainWindow::on_search__textChanged(const QString &arg1)
{
    if(!arg1.isEmpty()){
        ui->clear_search_->setText("×");
        ui->back_search->setText("\uFFE9");
    }else{
        ui->clear_search_->setText("");
        ui->back_search->setText("\u2315 ");
    }
}
