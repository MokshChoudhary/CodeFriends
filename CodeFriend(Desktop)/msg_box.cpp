#include "msg_box.h"
#include "ui_msg_box.h"

msg_box::msg_box(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::msg_box)
{
    ui->setupUi(this);
}

void msg_box::set_msg(QString msg){
    this->message = msg;
    ui->msg->setText(msg);
}

void msg_box::set_time(QString timestamp){
    this->timestamp = timestamp;
    ui->time->setText(timestamp);
}

QString msg_box::get_msg() {return this->message;}

QString msg_box::get_time() {return this->timestamp;}


msg_box::~msg_box()
{
    delete ui;
}
