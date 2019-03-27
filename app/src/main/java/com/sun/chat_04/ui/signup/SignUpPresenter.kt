package com.sun.chat_04.ui.signup

import com.sun.chat_04.data.datasource.UserDataSource
import com.sun.chat_04.data.model.User
import com.sun.chat_04.ui.signup.SignUpActivity.Status.INVALID

class SignUpPresenter(val iSingUpView: SignUpContract.View, val iUserDb: UserDataSource.Remote) : SignUpContract.Presenter {

    private val SIZE_MIN = 6

    override fun checkValid(user: User, email: String, pass: String, cfPass: String) {
        var status = SignUpActivity.Status.VALID
        if (user.userName.isNullOrEmpty() || user.birthday.isNullOrEmpty() ||
            user.gender.isNullOrEmpty() || email.isEmpty() || pass.isEmpty() ||
            cfPass.isEmpty() || !pass.equals(cfPass) || pass.length < SIZE_MIN
        ) {
            status = INVALID
        }
        iSingUpView.isValid(status)
    }

    override fun signUpAccount(user: User, email: String, pass: String) {
        iUserDb.insertUser(user, email, pass, object : RemoteCallback {
            override fun isSaved(status: Boolean) {
                iSingUpView.isSaved(status)
            }
        })
    }
}
