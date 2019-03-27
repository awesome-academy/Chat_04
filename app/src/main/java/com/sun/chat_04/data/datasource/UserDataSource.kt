package com.sun.chat_04.data.datasource

import com.sun.chat_04.data.model.User
import com.sun.chat_04.ui.signup.RemoteCallback

interface UserDataSource {
    interface Local {
    }

    interface Remote {
        fun insertUser(user: User, email: String, pass: String, iSavePresenter: RemoteCallback)
    }
}
