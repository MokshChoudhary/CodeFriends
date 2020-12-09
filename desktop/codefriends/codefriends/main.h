#pragma once
#include "wx\wx.h"
#include "SplashScreen.h"

class main : public wxApp
{
public:
	main();
	~main();
public:
	virtual bool onInit();
private:
	SplashScreen* splash_screen = nullptr;
};

