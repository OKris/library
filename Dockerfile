FROM ubuntu:latest
LABEL authors="kristina"

ENTRYPOINT ["top", "-b"]