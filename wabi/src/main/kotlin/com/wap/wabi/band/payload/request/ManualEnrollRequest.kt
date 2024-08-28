package com.wap.wabi.band.payload.request

import com.wap.wabi.band.payload.BandStudentDto

data class ManualEnrollRequest(val bandStudentDtos : List<BandStudentDto>)
