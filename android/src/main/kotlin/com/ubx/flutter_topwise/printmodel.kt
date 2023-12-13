package com.ubx.flutter_topwise

import com.beust.klaxon.*

// To parse the JSON, install Klaxon and do:
//
//   val printmodel = Printmodel.fromJson(jsonString)

private val klaxon = Klaxon()


class Printmodel(elements: Collection<PrintmodelElement>) : ArrayList<PrintmodelElement>(elements) {
    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = Printmodel(klaxon.parseArray<PrintmodelElement>(json)!!)
    }
}

data class PrintmodelElement (
        val data: List<Datum>
)

data class Datum (
        val image: String? = null,
        val align: String? = null,
        val flex: Int = 1,
        val imagewidth: Int? = null,
        val imageheight: Int? = null,
        val text: String? = null,
        val textsize: String? = null,
        val bold: Boolean? = null,
        val textwrap: Boolean? = null
)
