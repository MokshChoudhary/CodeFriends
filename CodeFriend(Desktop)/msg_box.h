#ifndef MSG_BOX_H
#define MSG_BOX_H

#include <QWidget>
#include <QString>
#include <QTime>

namespace Ui {
    class msg_box;
}

class msg_box : public QWidget
{
    Q_OBJECT

public:
    explicit msg_box(QWidget *parent = nullptr);
    QString get_msg();
    QString get_time();
    void set_msg(QString msg);
    void set_time(QString timestamp);
    ~msg_box();

private:
    Ui::msg_box *ui;
    QString message;
    QString timestamp;
};

#endif // MSG_BOX_H
