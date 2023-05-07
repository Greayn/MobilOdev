package com.example.grads_hw.validator


import com.example.grads_hw.models.AddAnnouncementData

class AddAnnouncementValidator  {

    fun validate(args: AddAnnouncementData) {
        if (args.title.isEmpty()) {
            error("Başlık boş olamaz")
        }
        if (args.title.length < 3) {
            error("Başlık en az 3 karakter olmalıdır")
        }
        if (args.description.isEmpty()) {
            error("Açıklama boş olamaz")
        }
        if (args.description.length < 20) {
            error("Açıklama en az 20 karakter olmalıdır")
        }
        if (args.imageUri == null) {
            error("Resim seçilmelidir")
        }
        if (args.expirationDate == null) {
            error("Duyuru son tarihi boş olamaz")
        }
    }

}