import torch
import torch.nn as nn
import torch.optim as optim
from torch.optim import lr_scheduler
from torchvision import models

import data
from train_test import Model

DEVICE = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")


# fetch data loaders
data_loaders = data.data_loaders
dataset_sizes = {x: len(data.image_datasets[x]) for x in ['train', 'val']}
class_names = data.image_datasets['train'].classes

# initiate Model class
model = Model(
    DEVICE,
    data_loaders,
    dataset_sizes,
    class_names
)

# initiate pre-trained CNN model
model_ft = models.resnet50(pretrained=True)
num_features = model_ft.fc.in_features
model_ft.fc = nn.Linear(num_features, len(class_names))
model_ft = model_ft.to(DEVICE)
criterion = nn.CrossEntropyLoss()
optimizer_ft = optim.SGD(model_ft.parameters(), lr=0.001, momentum=0.9)

# reduce learning rate by 0.1 after every 5 epochs
exp_lr_scheduler = lr_scheduler.StepLR(optimizer_ft, step_size=5, gamma=0.1)

# train the model
trained_model = model.train(
    model_ft,
    criterion,
    optimizer_ft,
    exp_lr_scheduler,
    num_epochs=20
)

# save the model
torch.save(trained_model, 'model_test.pth')

trained_model = torch.load('model.pth', map_location=DEVICE)

# test the model
model.test(trained_model)
