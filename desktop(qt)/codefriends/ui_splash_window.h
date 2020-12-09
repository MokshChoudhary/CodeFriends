/********************************************************************************
** Form generated from reading UI file 'splash_window.ui'
**
** Created by: Qt User Interface Compiler version 5.13.0
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_SPLASH_WINDOW_H
#define UI_SPLASH_WINDOW_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QLabel>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_splash_window
{
public:
    QWidget *centralWidget;
    QLabel *logo;
    QLabel *lable;
    QLabel *login_dilog;

    void setupUi(QMainWindow *splash_window)
    {
        if (splash_window->objectName().isEmpty())
            splash_window->setObjectName(QString::fromUtf8("splash_window"));
        splash_window->resize(600, 400);
        splash_window->setStyleSheet(QString::fromUtf8("background-color: rgb(0, 0, 0);"));
        centralWidget = new QWidget(splash_window);
        centralWidget->setObjectName(QString::fromUtf8("centralWidget"));
        logo = new QLabel(centralWidget);
        logo->setObjectName(QString::fromUtf8("logo"));
        logo->setGeometry(QRect(210, 30, 181, 221));
        QFont font;
        font.setPointSize(120);
        logo->setFont(font);
        logo->setStyleSheet(QString::fromUtf8("color: rgb(255, 255, 255);"));
        lable = new QLabel(centralWidget);
        lable->setObjectName(QString::fromUtf8("lable"));
        lable->setGeometry(QRect(40, 260, 521, 101));
        QFont font1;
        font1.setPointSize(72);
        lable->setFont(font1);
        lable->setStyleSheet(QString::fromUtf8("color: rgb(255, 255, 255);"));
        login_dilog = new QLabel(centralWidget);
        login_dilog->setObjectName(QString::fromUtf8("login_dilog"));
        login_dilog->setGeometry(QRect(0, 380, 281, 16));
        login_dilog->setStyleSheet(QString::fromUtf8("color: rgb(255, 255, 255);"));
        splash_window->setCentralWidget(centralWidget);

        retranslateUi(splash_window);

        QMetaObject::connectSlotsByName(splash_window);
    } // setupUi

    void retranslateUi(QMainWindow *splash_window)
    {
        splash_window->setWindowTitle(QCoreApplication::translate("splash_window", "splash_window", nullptr));
        logo->setText(QCoreApplication::translate("splash_window", "(;)", nullptr));
        lable->setText(QCoreApplication::translate("splash_window", "CodeFriends", nullptr));
        login_dilog->setText(QString());
    } // retranslateUi

};

namespace Ui {
    class splash_window: public Ui_splash_window {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_SPLASH_WINDOW_H
