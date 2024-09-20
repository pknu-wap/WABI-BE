package com.wap.wabi.band.payload.response

import com.wap.wabi.band.entity.Band

class BandDetailData(
    val bandId: Long,
    val bandName: String,
    val bandMemo: String
) {
    companion object {
        fun of(band: Band): BandDetailData {
            return BandDetailData(
                bandId = band.id,
                bandName = band.bandName,
                bandMemo = band.bandMemo
            )
        }
    }
}