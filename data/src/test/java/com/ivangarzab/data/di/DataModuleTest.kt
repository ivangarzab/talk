package com.ivangarzab.data.di

import android.content.Context
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

/**
 * The purpose of this class is to test Koin's [dataModule].
 *
 * Solution derived from:
 * [https://insert-koin.io/docs/reference/koin-test/verify#verifying-with-injected-parameters---jvm-only-40]
 */
@OptIn(KoinExperimentalAPI::class)
class DataModuleTest {

    @Test
    fun verifyDataModule() {
        dataModule.verify(
            // Include an Android [Context] which the app is getting from the [appModule],
            //  but testing the [dataModule] in isolation does not get that field "for free."
            extraTypes = listOf(Context::class)
        )
    }
}