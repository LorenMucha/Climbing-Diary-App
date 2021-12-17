#!/bin/bash
branchName=$(git name-rev --name-only HEAD)
if [ "$branchName" = "feature/welcomeSplashScreen" ];
then
      ./gradlew connectedCheck
       #todo git push
else
  echo "branch is not master branch is ${branchName}"
fi