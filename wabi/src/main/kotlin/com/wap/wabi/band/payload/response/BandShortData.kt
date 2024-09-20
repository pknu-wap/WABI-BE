package com.wap.wabi.band.payload.response

import com.wap.wabi.band.entity.Band

data class BandShortData(
    val bandId: Long,
    val bandName: String
) {
    companion object {
        fun of(band: Band): BandShortData {
            return BandShortData(
                bandId = band.id,
                bandName = band.bandName
            )
        }

        fun of(bands: List<Band>): List<BandShortData> {
            return bands.map { of(it) }
        }
    }
}
