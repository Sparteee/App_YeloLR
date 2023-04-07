package com.example.tpnote

import android.os.Parcel
import android.os.Parcelable

class Station(val nbEmplacements:  Int, val nbVelos: Int, val nom: String?, val long: Double, val lat: Double) : Parcelable { // Implémentation Parcel


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()

    ) {

    }

    override fun toString(): String { // Méthode toString pour affichage des stations dans le listView
        return "$nom\n" +
                "$nbVelos place(s) libre(s) sur $nbEmplacements"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(nbEmplacements)
        parcel.writeInt(nbVelos)
        parcel.writeString(nom)
        parcel.writeDouble(long)
        parcel.writeDouble(lat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Station> {
        override fun createFromParcel(parcel: Parcel): Station {
            return Station(parcel)
        }

        override fun newArray(size: Int): Array<Station?> {
            return arrayOfNulls(size)
        }
    }
}