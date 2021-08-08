package com.example.hiltplayground

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton

/*
* @AndroidEntryPoint generates an individual Hilt component for each Android class in your project.
* */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var player: Player

    @Inject
    lateinit var club: Club

    @Inject
    lateinit var repo: Repo

    @Inject
    lateinit var objectThatNeedSomeDataButNotPreparedYetFactory: ObjectThatNeedSomeDataButNotPreparedYetFactory

     lateinit var objectThatNeedSomeDataButNotPreparedYet: ObjectThatNeedSomeDataButNotPreparedYet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        objectThatNeedSomeDataButNotPreparedYet= objectThatNeedSomeDataButNotPreparedYetFactory.create("Abdo")


    }
}


 class ObjectThatNeedSomeDataButNotPreparedYet @AssistedInject constructor(
   private val age: Int,
    @Assisted private val name: String
) {
    fun printData() = "$name   $age"

}

@Module
@InstallIn(SingletonComponent::class)
object AssistedInjectExampleModule {
    @Singleton
    @Provides
    fun providesAge(): Int = 27
}

@AssistedFactory
interface ObjectThatNeedSomeDataButNotPreparedYetFactory {
    fun create(name: String): ObjectThatNeedSomeDataButNotPreparedYet
}





data class Player @Inject constructor(val name: String)

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Singleton
    @Provides
    fun providesPlayer(name: String) = Player(name)

    @Singleton
    @Provides
    fun providesPlayerName() = "CR7"

}

data class Club @Inject constructor(val players: List<Player>)

@Module
@InstallIn(SingletonComponent::class)
object ClubModule {

    @Singleton
    @Provides
    fun providesClub(players: List<Player>) = Club(players)

    @Singleton
    @Provides
    fun providesPlayerList(player1: Player, player2: Player) = listOf(player1, player2)
}


interface DataSource {
    fun getDataSourceName(): String
}

class NetworkDataSource @Inject constructor() : DataSource {
    override fun getDataSourceName() = "NetworkDataSource"
}

class LocalDataSource @Inject constructor() : DataSource {
    override fun getDataSourceName() = "LocalDataSource"
}

class Repo @Inject constructor(
    @RemoteSource private val remoteDataSource: DataSource,
    @LocalSource private val localDataSource: DataSource
) {
    fun getDataRemote() = remoteDataSource.getDataSourceName()
    fun getDataLocal() = localDataSource.getDataSourceName()
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @RemoteSource
    @Singleton
    @Provides
    fun providesNetworkDataSource(): DataSource {
        return NetworkDataSource()
    }

    @LocalSource
    @Singleton
    @Provides
    fun providesLocalDataSource(): DataSource {
        return LocalDataSource()
    }

}

//for interface if i have 2 implementation and need to decide which one i wanna use
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalSource