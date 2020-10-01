#include "msg_container.h"
#include "ui_msg_container.h"

msg_container::msg_container(QWidget *parent) :
    QFrame(parent),
    ui(new Ui::msg_container)
{
    ui->setupUi(this);
}


void msg_container::set_msg(QString msg){
    this->message = msg;
    ui->msg->setText(msg);
}

void msg_container::set_time(QString timestamp){
    this->timestamp = timestamp;
    ui->time->setText(timestamp);
}

QString msg_container::get_msg() {return this->message;}

QString msg_container::get_time() {return this->timestamp;}


msg_container::~msg_container()
{
    delete ui;
}
