package com.danielpasser.mychat.models.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.danielpasser.mychat.models.networkmodels.response.AvatarResponse
import com.danielpasser.mychat.models.networkmodels.response.ProfileData

@Entity(tableName = "user")
data class UserEntity(
 @PrimaryKey val key:Int?=1,
 val name: String? = null,
 val username: String? = null,
 val birthday: String? = null,
 val city: String? = null,
 val vk: String? = null,
 val instagram: String? = null,
 val status: String? = null,
 val _avatar: String? = null,
 val id: Int? = null,
 val last: String? = null,
 val online: Boolean? = null,
 val created: String? = null,
 val phone: String? = null,
 val completedTask: Int? = null,
 @Embedded var avatars: AvatarResponse? = AvatarResponse()
)

fun UserEntity.toProfiled() = ProfileData(
 name = name,
 username = username,
 birthday = birthday,
 city = city,
 vk = vk,
 instagram = instagram,
 status = status,
 avatar = _avatar,
 id = id,
 last = last,
 online = online,
 created = created,
 phone = phone,
 completedTask = completedTask,
 avatars = avatars
)
