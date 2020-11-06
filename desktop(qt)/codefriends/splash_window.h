#ifndef SPLASH_WINDOW_H
#define SPLASH_WINDOW_H

#include <QMainWindow>
#include <sqlite/sqlite3.h>

namespace Ui {
class splash_window;
}

class splash_window : public QMainWindow
{
    Q_OBJECT

public:
    explicit splash_window(QWidget *parent = nullptr);
    ~splash_window();

private:
    void open_database();
    Ui::splash_window *ui;
    sqlite3* db;
};

#endif // SPLASH_WINDOW_H
