package com.carlosalbpe.voicemodtest.ui.utils

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineDispatcher

//Extensions
fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


//Globals
fun getDispatcher(
    dispatcher: CoroutineDispatcher?,
    defaultDispatcher: CoroutineDispatcher
) = dispatcher ?: defaultDispatcher