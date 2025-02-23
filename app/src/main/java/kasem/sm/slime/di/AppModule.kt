/*
 * Copyright (C) 2022, Kasem S.M
 * All rights reserved.
 */
package kasem.sm.slime.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.WorkManager
import coil.ImageLoader
import coil.memory.MemoryCache
import com.slime.auth_api.AuthManager
import com.slime.auth_api.Token
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.header
import io.ktor.client.request.host
import io.ktor.client.request.port
import io.ktor.http.URLProtocol
import javax.inject.Singleton
import kasem.sm.core.domain.SlimeDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        authManager: AuthManager,
    ): HttpClient {
        return HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            defaultRequest {
                host = BASE_URL
                when (host) {
                    BASE_URL -> url { protocol = URLProtocol.HTTPS }
                    else -> {
                        url { protocol = URLProtocol.HTTP }
                        port = 8000
                    }
                }

                authManager.getUserData(Token)?.let { value ->
                    header("Authorization", "Bearer $value")
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader {
        return ImageLoader.Builder(context)
            .memoryCache(MemoryCache.Builder(context).maxSizePercent(0.25).build())
            .crossfade(250)
            .build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            "slime_prefs",
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // WorkManager
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    fun provideWorkerConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    @Provides
    fun provideSlimeDispatchers(): SlimeDispatchers {
        return SlimeDispatchers(
            default = Dispatchers.Default,
            main = Dispatchers.Main,
            io = Dispatchers.IO
        )
    }

    private const val BASE_URL = "slime-kt.herokuapp.com"
    private const val LOCAL_BASE_URL = "192.168.0.106"
}
