package com.example.grads_hw.validator

import com.example.grads_hw.models.AddGalleryItemData

class AddGalleryItemValidator  {

    fun validate(args: AddGalleryItemData) {
        if (args.title.isEmpty()) {
            error("Başlık boş olamaz")
        }
        if (args.mediaUri == null) {
            error("Resim veya video seçilmeli")
        }
        if (args.thumbnailUri == null) {
            error("Resim veya video seçilmeli")
        }
    }


}