package com.yml.fundo.networking.users

data class FirebaseUsersResponse(
    val documents: ArrayList<UserDocument>
)

data class UserDocument(
    val name: String,
    val fields: UserFields,
    val createTime: String,
    val updateTime: String
)

data class UserFields(
    val mobileNo: StringField,
    val email: StringField,
    val name: StringField
)

data class StringField(
    val stringValue: String
)