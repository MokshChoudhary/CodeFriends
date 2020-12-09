#include "main.h"

wxIMPLEMENT_APP(main);

main::main()
{
}

main::~main()
{
}

bool main::onInit()
{
	splash_screen = new SplashScreen();
	splash_screen->Show();
	return true;
}


