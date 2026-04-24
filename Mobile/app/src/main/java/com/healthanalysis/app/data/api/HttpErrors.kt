package com.healthanalysis.app.data.api

fun httpErrorMessage(code: Int): String = when (code) {
    401, 403 -> "Сессия истекла, войдите заново"
    404 -> "Данные не найдены"
    in 500..599 -> "Сервер временно недоступен (код $code). Попробуйте ещё раз через пару секунд."
    else -> "Ошибка $code"
}
