#!/bin/bash

echo -e "Running deploy.sh"
echo -e "TRAVIS_BRANCH=[$TRAVIS_BRANCH]"
echo -e "TRAVIS_TAG=[$TRAVIS_TAG]"

#mvn clean source:jar javadoc:jar deploy --settings=".scripts/settings.xml" -Dmaven.test.skip=true
