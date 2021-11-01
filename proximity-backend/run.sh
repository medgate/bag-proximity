#!/bin/sh

usage() {
  echo
  echo
  echo "Usage: ./run.sh [-h | --help]"
  echo
  echo "Runs Spring Boot component through gradle plugin with the default profile."
  echo
}

if [ $# -eq 0 ]; then
  ./gradlew clean bootRun
  exit
fi

while [ "$1" != "" ]; do
  case $1 in
  -h | --help)
    usage
    exit
    ;;
  *)
    usage
    exit 1
    ;;
  esac
  shift
done
