#!/usr/bin/env bash
echo "Installing Homebrew..."
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
brew doctor

echo "Installing Cask..."
brew install caskroom/cask/brew-cask
