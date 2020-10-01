#ifndef MSG_CONTAINER_H
#define MSG_CONTAINER_H

#include <QFrame>

namespace Ui {
class msg_container;
}

class msg_container : public QFrame
{
    Q_OBJECT

public:
    explicit msg_container(QWidget *parent = nullptr);
    QString get_msg();
    QString get_time();
    void set_msg(QString msg);
    void set_time(QString timestamp);
    ~msg_container();

private:
    Ui::msg_container *ui;
    QString message;
    QString timestamp;
};

#endif // MSG_CONTAINER_H
