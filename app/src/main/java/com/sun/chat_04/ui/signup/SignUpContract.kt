package com.sun.chat_04.ui.signup

import com.sun.chat_04.data.model.User
import com.sun.chat_04.ui.signup.SignUpActivity.Status

interface SignUpContract {
    interface View {
        fun isValid(status: Status)
        fun isSaved(status: Boolean)
    }

    interface Presenter {
        fun checkValid(user: User, email: String, pass: String, cfPass: String)
        fun signUpAccount(user: User, email: String, pass: String)
    }
}
