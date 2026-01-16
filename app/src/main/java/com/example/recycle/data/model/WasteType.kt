package com.example.recycle.data.model

enum class WasteType (
    val id: String,
    val displayName: String
) {
    ORGANIC(
        id = "organic",
        displayName = "Orgánico"
    ),

    PLASTIC(
        id = "plastic",
        displayName = "Envases / Plastico"
    ),

    PAPER(
        id = "paper",
        displayName = "Papel y cartón"
    ),

    GLASS(
        id = "glass",
        displayName = "Vidrio"
    ),

    REST(
        id = "rest",
        displayName = "Resto"
    );

    companion object {
        /**
         * Devuelve el tipo de residuo a partir de un id.
         */
        fun fromId(id: String): WasteType? {
            return WasteType.entries.find { it.id == id}
        }
    }
}