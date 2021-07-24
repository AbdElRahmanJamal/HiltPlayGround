package com.example.hiltplayground

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltApplication : Application()


/*
* All apps that use Hilt must contain an Application class that is annotated with @HiltAndroidApp.
* To generated Hilt component and provides dependencies.
* Also it is parent component of the app.
* Other components can access the dependencies that it provides.
* */