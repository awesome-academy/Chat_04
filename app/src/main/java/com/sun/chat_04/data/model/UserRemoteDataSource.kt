package com.sun.chat_04.data.model

import com.sun.chat_04.data.datasource.UserDataSource
import com.sun.chat_04.data.remote.UserAsyncTask
import com.sun.chat_04.ui.signup.RemoteCallback

class UserRemoteDataSource : UserDataSource.Remote {

    override fun insertUser(user: User, email: String, pass: String, iSavePresenter: RemoteCallback) {
        val task = UserAsyncTask(email, pass, iSavePresenter)
        task.execute(user)
    }
}
