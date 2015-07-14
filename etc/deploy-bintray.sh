#!/usr/bin/env bash

git fetch --tags
latestTag=$(git describe --tags `git rev-list --tags --max-count=1`)

echo Checking out $latestTag
git checkout $latestTag

mvn clean package deploy:deploy -DaltDeploymentRepository=bintray::default::https://api.bintray.com/maven/jcgay/maven/send-notification

echo Checking out master
git checkout master
