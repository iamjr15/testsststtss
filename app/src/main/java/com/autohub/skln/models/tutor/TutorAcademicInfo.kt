package com.autohub.skln.models.tutor

import android.os.Parcel
import android.os.Parcelable


data class TutorAcademicInfo(var belongToInstitute: Boolean? = false,
                             var classType: String? = null,
                             var instituteNeme: String? = null,
                             var numberOfStudents: String? = null,
                             var roleInInstitute: String? = null,
                             var selectedCategory: String? = null,
                             var teachingMethod: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(belongToInstitute)
        parcel.writeString(classType)
        parcel.writeString(instituteNeme)
        parcel.writeString(numberOfStudents)
        parcel.writeString(roleInInstitute)
        parcel.writeString(selectedCategory)
        parcel.writeString(teachingMethod)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TutorAcademicInfo> {
        override fun createFromParcel(parcel: Parcel): TutorAcademicInfo {
            return TutorAcademicInfo(parcel)
        }

        override fun newArray(size: Int): Array<TutorAcademicInfo?> {
            return arrayOfNulls(size)
        }
    }
}