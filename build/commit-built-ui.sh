#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "olivierlemasle/java-certificate-authority" ] && \
   [ "$TRAVIS_JDK_VERSION" == "oraclejdk8" ] && \
   [ "$TRAVIS_PULL_REQUEST" == "false" ] && \
   [ "$TRAVIS_BRANCH" == "master" ]; then

   echo -e "Builing UI...\n"
   cd ui
   npm install
   node_modules/.bin/gulp
   mkdir -p $HOME/ui-latest/ui
   cp -R dist/. $HOME/ui-latest/ui
   cp ui-dockerfile $HOME/ui-latest/Dockerfile
   cp nginx.conf $HOME/ui-latest/nginx.conf
   cd $HOME
   git config --global user.email "travis@travis-ci.org"
   git config --global user.name "travis-ci"
   git clone --quiet --branch=built-ui https://${GH_TOKEN}@github.com/olivierlemasle/java-certificate-authority built-ui > /dev/null

   cd built-ui
   git rm -rf .
   cp -Rf $HOME/ui-latest/. .
   git add -f .
   git commit -m "Lastest UI on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to built-ui"
   git push -fq origin built-ui > /dev/null
   echo -e "Published UI to built-ui.\n"
fi
