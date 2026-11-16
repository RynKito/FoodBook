package com.example.foodbook.util

import java.text.Normalizer

/** Bỏ dấu tiếng Việt để tìm kiếm: "Phở" khớp với "pho" */
fun String.noAccent(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .replace('đ', 'd').replace('Đ', 'D')
        .lowercase()
