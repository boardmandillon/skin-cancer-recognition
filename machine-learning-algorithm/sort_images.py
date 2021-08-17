import os
import pathlib

import cv2
import pandas as pd
import splitfolders

from pre_processing import color_constancy

CURRENT_DIRECTORY = os.getcwd()


def sort_pad(multiclass=False):
    data_directory = os.path.join(CURRENT_DIRECTORY, "dataset/PAD-UFES-20")
    image_directory = os.path.join(data_directory, "images")

    destination_directory = \
        os.path.join(data_directory, "pre-processed-images/")

    metadata = pd.read_csv(os.path.join(data_directory, 'metadata.csv'))

    labels = ['ACK', 'BCC', 'MEL', 'NEV', 'SCC', 'SEK']

    # Move the images into new folder structure:
    for label in labels:
        sample = metadata[metadata['diagnostic'] == label]['img_id']

        if not multiclass:
            if label == 'BCC' or label == 'MEL' or label == 'SCC':
                destination = os.path.join(destination_directory, 'malignant')
            if label == 'NEV' or label == 'SEK' or label == 'ACK':
                destination = os.path.join(destination_directory, 'benign')
        else:
            if label == 'NEV' or label == 'SEK' or label == 'ACK':
                destination = os.path.join(destination_directory, 'benign')
            else:
                destination = os.path.join(destination_directory, label)

        pathlib.Path(destination).mkdir(parents=True, exist_ok=True)

        label_images = []
        label_images.extend(sample)

        for image_id in label_images:
            image = cv2.imread(image_directory + "/" + image_id)

            pre_processed_image = color_constancy(image)
            cv2.imwrite(
                (os.path.join(destination, image_id)),
                pre_processed_image
            )

        splitfolders.ratio(
            destination_directory,
            output=data_directory,
            seed=1337,
            ratio=(.7, .15, .15),
            group_prefix=None
        )


def sort_ham(multiclass=False):
    data_directory = os.path.join(CURRENT_DIRECTORY, "dataset/MNIST-HAM-10000")
    image_directory = os.path.join(data_directory, "images")
    destination_directory = \
        os.path.join(data_directory, "pre-processed-images/")

    metadata = \
        pd.read_csv(os.path.join(data_directory, "HAM10000_metadata.csv"))

    metadata = metadata.drop_duplicates(subset=['lesion_id'], keep=False)

    labels = ['bcc', 'mel', 'akiec', 'bkl', 'df', 'nv', 'vasc']

    # Move the images into new folder structure:
    for label in labels:
        sample = metadata[metadata['dx'] == label]['image_id']

        if not multiclass:
            if label == 'bcc' or label == 'mel' or label == 'akiec':
                destination = os.path.join(destination_directory, 'malignant')
            if label == 'bkl' or label == 'df' or label == 'nv' or label == 'vasc':
                destination = os.path.join(destination_directory, 'benign')
        else:
            destination = os.path.join(destination_directory, label)

        pathlib.Path(destination).mkdir(parents=True, exist_ok=True)

        label_images = []
        label_images.extend(sample)

        for image_id in label_images:
            path = os.path.join(image_directory, image_id + '.jpg')
            image = cv2.imread(path)

            pre_processed_image = color_constancy(image)
            cv2.imwrite(
                (os.path.join(destination, image_id + '.jpg')),
                pre_processed_image
            )

        splitfolders.ratio(
            destination_directory,
            output=data_directory,
            seed=1337,
            ratio=(.7, .3),
            group_prefix=None
        )


if __name__ == "__main__":
    sort_pad()
    # sort_ham()
