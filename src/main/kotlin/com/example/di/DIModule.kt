package com.example.di

import org.kodein.di.DI
import org.kodein.di.bindConstant
import java.io.File

val diModule by DI.Module{
    bindConstant("images_path"){ getImageResourcePath() }
    import(diConnectionToDatabase)
    import(diService)
    import(diRepos)
    import(diUseCase)
}

private fun Any.getImageResourcePath() =
    File(javaClass.classLoader.getResource("application.conf")!!.path).parent.dropLast(4)+"images"