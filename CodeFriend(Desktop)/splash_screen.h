#ifndef SPLIT_SCREEN_H
#define SPLIT_SCREEN_H

#include <QWidget>

namespace Ui {
class split_screen;
}

class split_screen : public QWidget
{
    Q_OBJECT

public:
    explicit split_screen(QWidget *parent = nullptr);

    /**
     * @brief checkAuth check for authantication of the user , if not authrize do necessary opration
     *
     * @return bool
     */
    bool checkAuth();

    ~split_screen();

private:
    /**
     * @brief checkInternet <p>check for internet connection return the response back</p>
     * @return bool
     */
    bool checkInternet();

    /**
     * @brief store_in_file ,To store the auth key in file for current session
     * @param userid , unique id want to store
     */
    void store_in_file(std::string &userid);

    /**
     * @brief handle_Auth , Handle the auth request if user is not loged_in
     */
    void handle_Auth();

    Ui::split_screen *ui;
};

#endif // SPLIT_SCREEN_H
