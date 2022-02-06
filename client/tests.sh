#!/bin/bash

echo "LIST=================================="
sh video-store.sh list
echo "END_OF_LIST==========================="

echo "UPLOAD==============================="
sh video-store.sh upload tests/test-file.mp4
sh video-store.sh upload tests/test-file.mpeg
echo "END_OF_UPLOAD========================"
