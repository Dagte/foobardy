package org.damte.server.auth

import org.damte.server.model.UserCredentials

object AuthService {
    fun validateCredentials(userCredentials: UserCredentials): Boolean {
        return userCredentials.username == "user" && userCredentials.password == "password"
    }
}
