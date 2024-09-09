package com.danielpasser.mychat.network

import android.util.Log
import com.danielpasser.mychat.models.networkmodels.request.RefreshTokenRequest
import com.danielpasser.mychat.models.networkmodels.response.AuthInfoResponse
import com.danielpasser.mychat.repositories.TokenRepository
import com.danielpasser.mychat.utils.BASE_URL
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class AuthAuthenticator1 @Inject constructor(
    private val tokenRepository: TokenRepository,
) : Authenticator {
    private var tokenRefreshInProgress: AtomicBoolean = AtomicBoolean(false)
    private var request: Request? = null
    private var accessToken = ""
    override fun authenticate(route: Route?, response: Response): Request? {
        Log.e("TEST", "authenticate")
        return runBlocking {
            val refreshToken = runBlocking {
                tokenRepository.getRefreshToken().first()
            }
            request = null
            if (!tokenRefreshInProgress.get()) {
                tokenRefreshInProgress.set(true)
                val newToken = getNewToken(refreshToken)
                if (!newToken.isSuccessful || newToken.body() == null) { //Couldn't refresh the token, so restart the login process
                    val deleteJobs = listOf(
                        launch { tokenRepository.deleteRefreshToken() },
                        launch { tokenRepository.deleteAccessToken() },
                    )
                    deleteJobs.joinAll()
                    request = null
                }
                newToken.body()?.let {
                    accessToken = it.accessToken
                    val saveJobs = listOf(
                        launch { tokenRepository.saveAccessToken(it.accessToken) },
                        launch { tokenRepository.saveRefreshToken(it.refreshToken) },
                    )
                    saveJobs.joinAll()
                    request = buildRequest(response.request.newBuilder())

                }
                tokenRefreshInProgress.set(false)
            } else {
                waitForRefresh(response)
            }
            return@runBlocking request
        }
    }

    private suspend fun waitForRefresh(response: Response) {
        while (tokenRefreshInProgress.get()) {
            delay(100)
        }
        request = buildRequest(response.request.newBuilder())
    }

    private fun buildRequest(requestBuilder: Request.Builder) =
        requestBuilder.header("Authorization", "Bearer $accessToken")
            .build()

    private suspend fun getNewToken(refreshToken: String?): retrofit2.Response<AuthInfoResponse> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(AuthApiService::class.java)
        return service.refreshToken(RefreshTokenRequest(refreshToken))
    }
}