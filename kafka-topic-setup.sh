# Create the main transactions topic
docker exec -it agritech-kafka kafka-topics \
  --create \
  --if-not-exists \
  --bootstrap-server localhost:9092 \
  --topic agritech-transactions \
  --partitions 4 \
  --replication-factor 1

# Create downstream topics for the processor service
docker exec -it agritech-kafka kafka-topics \
  --create \
  --if-not-exists \
  --bootstrap-server localhost:9092 \
  --topic agritech-individual-fraud-alerts \
  --partitions 3 \
  --replication-factor 1

docker exec -it agritech-kafka kafka-topics \
  --create \
  --if-not-exists \
  --bootstrap-server localhost:9092 \
  --topic agritech-velocity-fraud-alerts \
  --partitions 3 \
  --replication-factor 1

docker exec -it agritech-kafka kafka-topics \
  --create \
  --if-not-exists \
  --bootstrap-server localhost:9092 \
  --topic agritech-audit-trail \
  --partitions 2 \
  --replication-factor 1

# Verify all topics have been created
echo "Verifying topics..."
docker exec -it agritech-kafka kafka-topics \
  --bootstrap-server localhost:9092 \
  --list