import os

import torch
from torchvision import datasets, transforms

IMG_DIRECTORY = '../dataset/PAD-UFES-20'


# Data augmentation and normalization
data_transforms = {
    'train': transforms.Compose([
        transforms.Resize(256),
        transforms.CenterCrop(224),
        transforms.RandomRotation(45),
        transforms.GaussianBlur(11, sigma=(0.1, 2.0)),
        transforms.ColorJitter(
            brightness=0.1,
            contrast=0.1,
            saturation=0.1,
            hue=0.1
        ),
        transforms.RandomHorizontalFlip(),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
    ]),
    'val': transforms.Compose([
        transforms.Resize(256),
        transforms.CenterCrop(224),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
    ]),
    'test': transforms.Compose([
        transforms.Resize(256),
        transforms.CenterCrop(224),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
    ]),
}

image_datasets = {
    'train': datasets.ImageFolder(
        os.path.join(IMG_DIRECTORY, 'train'), data_transforms['train']
    ),
    'val': datasets.ImageFolder(
        os.path.join(IMG_DIRECTORY, 'val'), data_transforms['val']
    ),
    'test': datasets.ImageFolder(
        os.path.join(IMG_DIRECTORY, 'test'), data_transforms['test']
    )
}

data_loaders = {
    'train': torch.utils.data.DataLoader(
        image_datasets['train'],
        batch_size=10,
        shuffle=True,
        num_workers=8
    ),
    'val': torch.utils.data.DataLoader(
        image_datasets['val'],
        batch_size=10,
        shuffle=True,
        num_workers=8
    ),
    'test': torch.utils.data.DataLoader(
        image_datasets['test'],
        batch_size=10,
        shuffle=True,
        num_workers=8
    )
}
