CC=gcc
CFLAGS = -Wall -pedantic -std=gnu99  -pthread -g 

.PHONY: all
.DEFAULT_GOAL: all

all: client server

client: client.o
	$(CC) $(CFLAGS) client.o -o client

server: server.o
	$(CC) $(CFLAGS) server.o -o server
