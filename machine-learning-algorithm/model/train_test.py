import copy
import torch

# Adapted from PyTorch transfer learning Tutorial
## Author: Sasank Chilamkurthy
## Date: 22/04/2021
## Name: transfer_learning_tutorial.py
## Type: source code
## Web address: https://pytorch.org/tutorials/beginner/transfer_learning_tutorial.html


class Model:
    def __init__(self, device, data_loaders, dataset_sizes, classes):
        self.device = device
        self.data_loaders = data_loaders
        self.dataset_sizes = dataset_sizes
        self.classes = classes

    def train(self, model, criterion, optimizer, scheduler, num_epochs=10):
        best_val_accuracy = 0.0

        for epoch in range(num_epochs):
            print('\nEpoch {}/{}'.format(epoch + 1, num_epochs))
            print('=' * 30)

            # set to train or validate
            for phase in ['train', 'val']:
                if phase == 'train':
                    model.train()
                else:
                    model.eval()

                running_loss = 0.0
                running_corrects = 0

                # iterate over data
                for inputs, labels in self.data_loaders[phase]:
                    inputs = inputs.to(self.device)
                    labels = labels.to(self.device)

                    # zero parameter gradients
                    optimizer.zero_grad()

                    # forward
                    with torch.set_grad_enabled(phase == 'train'):
                        outputs = model(inputs)
                        _, predictions = torch.max(outputs, 1)
                        loss = criterion(outputs, labels)

                        # backward + optimize
                        if phase == 'train':
                            loss.backward()
                            optimizer.step()

                    # statistics
                    running_loss += loss.item() * inputs.size(0)
                    running_corrects += torch.sum(predictions == labels.data)

                if phase == 'train':
                    scheduler.step()

                epoch_loss = running_loss / self.dataset_sizes[phase]
                epoch_accuracy = running_corrects.double() / self.dataset_sizes[phase]

                print('{} Loss: {:.4f} Acc: {:.4f}'.format(
                    phase, epoch_loss, epoch_accuracy)
                )

                # deep copy the best model
                if phase == 'val' and epoch_accuracy > best_val_accuracy:
                    best_val_accuracy = epoch_accuracy
                    best_model = copy.deepcopy(model)

        print('\nBest validation Accuracy: {:4f}'.format(best_val_accuracy))

        # return the best model
        return best_model


    # Adapted from PyTorch TRAINING A CLASSIFIER
    ## Date: 22/04/2021
    ## Name: cifar10_tutorial.py
    ## Type: source code
    ## Web address: https://pytorch.org/tutorials/beginner/blitz/cifar10_tutorial.html#training-a-classifier

    def test(self, model):
        correct_predictions = {classname: 0 for classname in self.classes}
        total_predictions = {classname: 0 for classname in self.classes}

        with torch.no_grad():
            for inputs, labels in self.data_loaders['test']:
                inputs = inputs.to(self.device)
                labels = labels.to(self.device)

                outputs = model(inputs)
                _, predictions = torch.max(outputs, 1)

                # predictions for each class
                for label, prediction in zip(labels, predictions):
                    if label == prediction:
                        correct_predictions[self.classes[label]] += 1

                    total_predictions[self.classes[label]] += 1

        # print accuracy for each class
        for classname, correct_count in correct_predictions.items():
            accuracy = 100 * float(correct_count) / total_predictions[classname]

            print('Accuracy for {:5s} is: {:.4f}'.format(
                classname,
                accuracy
            ))
