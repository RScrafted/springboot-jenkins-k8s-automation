#!/bin/bash
# Temporary k3s cleanup script

echo "Stopping application instances..."
kubectl scale deployment rs-inventory-app --replicas=0

echo "Deleting Kubernetes resources..."
kubectl delete deployment rs-inventory-app --ignore-not-found
kubectl delete service rs-inventory-app-service --ignore-not-found

# Optional: Clean up any lingering resource tagged with your app label
kubectl delete all -l app=rs-inventory-app --ignore-not-found

echo "Cleanup complete! All services and network routes removed."