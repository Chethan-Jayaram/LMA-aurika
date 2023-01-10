package com.example.lastmailattendent

import android.app.Application
import android.content.Context
import com.bumptech.glide.annotation.GlideModule
import com.example.lastmailattendent.activity.HomeActivity
import com.example.lastmailattendent.viewmodelfactoy.LoginViewModelFactory
import com.example.lastmailattendent.viewmodelfactoy.TicketViewModelFactory
import com.example.lastmailattendent.helper.Repository
import com.example.lastmailattendent.util.data.network.MyApi
import com.example.lastmailattendent.util.data.network.NetworkConnectionInterceptor
import com.example.lastmailattendent.util.data.preferences.PreferenceProvider
import com.example.lastmailattendent.viewmodelfactoy.EditProfileViewModelFactory
import com.example.lastmailattendent.viewmodelfactoy.TicketDetailsViewModelFactory
import com.llew.huawei.verifier.LoadedApkHuaWei
import com.google.android.gms.common.GooglePlayServicesNotAvailableException

import com.google.android.gms.common.GooglePlayServicesRepairableException

import com.google.android.gms.security.ProviderInstaller
import com.onesignal.OneSignal

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext

const val ONESIGNAL_APP_ID = "cc328b3a-7b1f-414c-bd94-8dbee25a8bf0"



class LastMailAttendent : Application(), KodeinAware {





    override fun onCreate() {
        super.onCreate()
        try {
            ctx=this
            ProviderInstaller.installIfNeeded(getApplicationContext())
            val sslContext: SSLContext
            sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()

            // Logging set to help debug issues, remove before releasing your app.
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

            // OneSignal Initialization
            OneSignal.initWithContext(this)
            OneSignal.setAppId(ONESIGNAL_APP_ID)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }catch (e:Exception){
            e.message
        }

        LoadedApkHuaWei.hookHuaWeiVerifier(applicationContext);
    }


    companion object {
         lateinit var ctx: Context;
        fun getApplicationCtx(): Context {
            return ctx;
        }
    }




    override val kodein = Kodein.lazy {
        import(androidXModule(this@LastMailAttendent))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { MyApi(instance()) }
        bind() from singleton { Repository(instance())}
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from provider {
            LoginViewModelFactory(
                instance(),
                instance()
            )
        }
        bind() from provider {
            TicketDetailsViewModelFactory(
                instance(),
                instance()
            )
        }
        bind() from provider {
            TicketViewModelFactory(
                instance(),
                instance()
            )
        }
        bind() from provider {
            EditProfileViewModelFactory(
                instance(),
                instance()
            )
        }
    }

}