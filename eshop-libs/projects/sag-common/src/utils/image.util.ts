const IMG_TYPE = {
    IMG: 'image',
    PDF: 'document',
    THUMB: 'image_300'
};

export function getImages(resources) {
    return resources.filter(item => item.img_typ === IMG_TYPE.IMG);
}

export function getImageThumb(resources) {
    const thumb = resources.find(img => img.img_typ === IMG_TYPE.THUMB);
    if (thumb) {
        return thumb.ref;
    }
    return '';
}
