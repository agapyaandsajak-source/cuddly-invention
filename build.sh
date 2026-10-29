#!/bin/bash
set -e
echo "Building Frost Strength SMP..."
mvn clean package -DskipTests
echo "Build complete! Jar: target/FrostStrengthSMP-1.0.0.jar"
