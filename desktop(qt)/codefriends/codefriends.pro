#-------------------------------------------------
#
# Project created by QtCreator 2020-11-05T16:01:39
#
#-------------------------------------------------

QT       += core gui
QT       += network

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = codefriends
TEMPLATE = app

# The following define makes your compiler emit warnings if you use
# any feature of Qt which has been marked as deprecated (the exact warnings
# depend on your compiler). Please consult the documentation of the
# deprecated API in order to know how to port your code away from it.
DEFINES += QT_DEPRECATED_WARNINGS

# You can also make your code fail to compile if you use deprecated APIs.
# In order to do so, uncomment the following line.
# You can also select to disable deprecated APIs only up to a certain version of Qt.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

CONFIG += c++17

SOURCES += \
        database/database_helper.cpp \
        m_network.cpp \
        main.cpp \
        main_window.cpp \
        splash_window.cpp \
        sqlite/shell.c \
        sqlite/sqlite3.c

HEADERS += \
        database/database_helper.h \
        main_window.h \
        splash_window.h \
        sqlite/sqlite3.h \
        sqlite/sqlite3ext.h

FORMS += \
        main_window.ui \
        splash_window.ui

