package com.main.climbingdiary.common.parser

import android.app.Activity
import android.net.Uri
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.AuthorizationServiceConfiguration.RetrieveConfigurationCallback
import net.openid.appauth.ResponseTypeValues


class EightAparser(val activity: Activity) {

    private lateinit var authConfig: AuthorizationServiceConfiguration

    fun login(){
        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse("https://vlatka.vertical-life.info/auth/realms/Vertical-Life/protocol/openid-connect/auth?client_id=8a-nu&scope=openid%20email%20profile&response_type=code&redirect_uri=https%3A%2F%2Fwww.8a.nu%2Fcallback"), // authorization endpoint
            Uri.parse("https://vlatka.vertical-life.info/token") // token endpoint
        )

        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            "8a-nu",  // Client ID
            ResponseTypeValues.CODE,
            Uri.parse("https%3A%2F%2Fwww.8a.nu%2Fcallback") // Redirect URI
        ).setScopes("openid email profile").build()

        val service = AuthorizationService(activity)
        val intent = service.getAuthorizationRequestIntent(authRequest)
        activity.startActivityForResult(intent, 123456)
    }
}