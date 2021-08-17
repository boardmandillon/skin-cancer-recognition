# Adapted from isic2018-skin-lesion-classifier-tensorflow
## Author: Abhishek Rana
## Date: 22/04/2021
## Name: utils_image.py
## Type: source code
## Web address: https://github.com/abhishekrana/isic2018-skin-lesion-classifier-tensorflow/blob/master/utils/utils_image.py

import numpy as np


def color_constancy(image, power=6):
    image = image.astype('float32')
    image_power = np.power(image, power)

    rgb_vec = np.power(np.mean(image_power, (0, 1)), 1 / power)
    rgb_norm = np.sqrt(np.sum(np.power(rgb_vec, 2.0)))
    rgb_vec = rgb_vec / rgb_norm
    rgb_vec = 1 / (rgb_vec * np.sqrt(3))

    image = np.multiply(image, rgb_vec)
    image = np.clip(image, a_min=0, a_max=255)

    return image.astype(image.dtype)
